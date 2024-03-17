package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.mnrclara.api.management.model.matterexpense.MatterExpense;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.config.PropertiesConfig;
import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.clientdocument.AddClientDocument;
import com.mnrclara.api.management.model.clientdocument.ClientDocument;
import com.mnrclara.api.management.model.clientdocument.SearchClientDocument;
import com.mnrclara.api.management.model.clientdocument.UpdateClientDocument;
import com.mnrclara.api.management.model.clientgeneral.ClientGeneral;
import com.mnrclara.api.management.model.dto.CaseSubcategory;
import com.mnrclara.api.management.model.dto.DocumentTemplate;
import com.mnrclara.api.management.model.dto.EnvelopeResponse;
import com.mnrclara.api.management.model.dto.EnvelopeStatus;
import com.mnrclara.api.management.model.dto.MailMerge;
import com.mnrclara.api.management.model.dto.UserProfile;
import com.mnrclara.api.management.model.expirationdate.ExpirationDate;
import com.mnrclara.api.management.model.matterassignment.MatterAssignment;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.repository.ClientDocumentRepository;
import com.mnrclara.api.management.repository.MatterGenAccRepository;
import com.mnrclara.api.management.repository.specification.ClientDocumentSpecification;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientDocumentService {

	private static final String D001 = "D001";
	private static final String D002 = "D002";
	private static final String D003 = "D003";
	private static final String D004 = "D004";
	private static final String D018 = "D018";

	@Autowired
	private ClientDocumentRepository clientDocumentRepository;
	
	@Autowired
	private MatterGenAccRepository matterGenAccRepository;

	@Autowired
	private ClientGeneralService clientGeneralService;
	
	@Autowired
	private MatterAssignmentService matterAssignmentService;
	
	@Autowired
	private MatterGenAccService matterGeneralService;

	@Autowired
	private SetupService setupService;

	@Autowired
	CommonService commonService;

	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	ExpirationDateService expirationDateService;
	
	@Autowired
	PropertiesConfig propertiesConfig;

	/**
	 * getClientDocuments
	 * 
	 * @return
	 */
	public List<ClientDocument> getClientDocuments() {
		List<ClientDocument> clientDocumentList = clientDocumentRepository.findAll();
		log.info("clientDocumentList : " + clientDocumentList);
		clientDocumentList = clientDocumentList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return clientDocumentList;
	}

	/**
	 * getClientDocument
	 * 
	 * @param clientDocumentId
	 * @return
	 */
	public ClientDocument getClientDocument(Long clientDocumentId) {
		ClientDocument clientDocument = clientDocumentRepository.findByClientDocumentId(clientDocumentId);
		if (clientDocument != null && clientDocument.getDeletionIndicator() == 0) {
			return clientDocument;
		} else {
			throw new BadRequestException("The given ClientDocument ID : " + clientDocumentId + " doesn't exist.");
		}
	}
	
	/**
	 * findClientDocument
	 * @param searchClientDocument
	 * @return
	 * @throws ParseException 
	 */
	public List<ClientDocument> findClientDocument(SearchClientDocument searchClientDocument) throws ParseException {
		if (searchClientDocument.getSSentOn() != null && searchClientDocument.getESentOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchClientDocument.getSSentOn(), searchClientDocument.getESentOn());
			searchClientDocument.setSSentOn(dates[0]);
			searchClientDocument.setESentOn(dates[1]);
		}
		
		if (searchClientDocument.getSReceivedOn() != null && searchClientDocument.getEReceivedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchClientDocument.getSReceivedOn(), searchClientDocument.getEReceivedOn());
			searchClientDocument.setSReceivedOn(dates[0]);
			searchClientDocument.setEReceivedOn(dates[1]);
		}
		
		ClientDocumentSpecification spec = new ClientDocumentSpecification(searchClientDocument);
		List<ClientDocument> results = clientDocumentRepository.findAll(spec);
		log.info("results: " + results);
		return results;
	}

	/**
	 * createClientDocument
	 * 
	 * @param newClientDocument
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientDocument createClientDocument(AddClientDocument newClientDocument, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		ClientGeneral clientGeneral = clientGeneralService.getClientGeneral(newClientDocument.getClientId());
		
		ClientDocument dbClientDocument = new ClientDocument();
		dbClientDocument.setClientDocumentId(System.currentTimeMillis());
		
		// Copy other attributes
		BeanUtils.copyProperties(newClientDocument, dbClientDocument, CommonUtils.getNullPropertyNames(newClientDocument));

		// CLIENT_ID/FIRST_LAST_NM
		dbClientDocument.setClientId(newClientDocument.getClientId());

		// CLIENT_USR_ID
		dbClientDocument.setClientUserId(newClientDocument.getClientUserId());

		// DOC_NO
		dbClientDocument.setDocumentNo(newClientDocument.getDocumentNo());
		
		// DOC_URL
		dbClientDocument.setDocumentUrl(newClientDocument.getDocumentUrl());

		// MATTER_NO
		dbClientDocument.setMatterNumber(newClientDocument.getMatterNumber());

		// DOC_DATE_TIME
		dbClientDocument.setDocumentDate(new Date());

		// LANG_ID
		dbClientDocument.setLanguageId("EN");
		
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		
		// CLASS_ID
		dbClientDocument.setClassId(clientGeneral.getClassId());
		
		// MAIL_MERGE
		// Pass the selected DOC_NO value and fetch MAIL_MERGE value from
		// DOCUMENTTEMPLATE table and auto populate
		DocumentTemplate documentTemplate = setupService.getDocumentTemplate(newClientDocument.getDocumentNo(),
				authTokenForSetupService.getAccess_token());
		log.info("documentTemplate Mailmerge :" + documentTemplate.getMailMerge());
		
		/*
		 * Assigning documentVersion for every existing set of records
		 */
		List<ClientDocument> existingClientDocuments = 
				clientDocumentRepository.findTopByLanguageIdAndClassIdAndClientIdAndDocumentNoOrderByDocumentUrlVersionDesc (newClientDocument.getLanguageId(),
				newClientDocument.getClassId(), 
				newClientDocument.getClientId(), 
				newClientDocument.getDocumentNo());
		log.info("existingClientDocuments11111----> : " + existingClientDocuments);
		
		Long latestDocuementUrlVersion = 0L;
		if (existingClientDocuments != null && existingClientDocuments.size() > 0) {
			ClientDocument existingClientDocument = existingClientDocuments.get(0);
			if (existingClientDocument.getDocumentUrlVersion() != null) {
				log.info("Latest version number : " + existingClientDocument.getDocumentUrlVersion());
				latestDocuementUrlVersion = Long.valueOf(existingClientDocument.getDocumentUrlVersion());
				latestDocuementUrlVersion ++;
			}
		} 

		// If MailMerge is Enabled
		if (Boolean.valueOf(documentTemplate.getMailMerge()).booleanValue() == true) {
			// Get required data for MailMerge
			Optional<MatterGenAcc> matterGenAcc = matterGenAccRepository.findByMatterNumber(newClientDocument.getMatterNumber());
			if (matterGenAcc.isEmpty()) {
				throw new BadRequestException("Matter General has no relevant record for the given MatterNumber : " + newClientDocument.getMatterNumber());
			}
			
			log.info("matterGenAcc : " + matterGenAcc);
			MatterGenAcc dbMatterGenAcc = matterGenAcc.get();
			MailMerge mailMerge = new MailMerge();
			
			// D001
			if (newClientDocument.getDocumentNo().equalsIgnoreCase(D001)) {
				mailMerge = mailMerge_D001 (clientGeneral, matterGenAcc, dbMatterGenAcc, newClientDocument, 
						documentTemplate, mailMerge, latestDocuementUrlVersion);
			} else if (newClientDocument.getDocumentNo().equalsIgnoreCase(D002)) {
				mailMerge = mailMerge_D002 (newClientDocument.getMatterNumber(), newClientDocument, documentTemplate,
						mailMerge, latestDocuementUrlVersion);
			} else if (newClientDocument.getDocumentNo().equalsIgnoreCase(D003)) {
				mailMerge = mailMerge_D003 (newClientDocument.getMatterNumber(), newClientDocument, documentTemplate,
						mailMerge, latestDocuementUrlVersion);
			} else if (newClientDocument.getDocumentNo().equalsIgnoreCase(D004)) {
				mailMerge = mailMerge_D004 (newClientDocument.getMatterNumber(), newClientDocument, documentTemplate,
						mailMerge, latestDocuementUrlVersion);
			} else if (newClientDocument.getDocumentNo().equalsIgnoreCase(D018)) {
				mailMerge = mailMerge_D018 (newClientDocument.getMatterNumber(), newClientDocument, documentTemplate,
						mailMerge, latestDocuementUrlVersion);
			}
			
			/*
			 * Pass the selected DOC_NO/DOC_TEXT in dropbox and Fetch the respective
			 * document template (stored in combination DOC_CODE/DOC_TEXT) from Document
			 * template folder of Dropbox.
			 */
			AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
			MailMerge mailMergeProcessed = commonService.mailMerge(mailMerge,
					authTokenForCommonService.getAccess_token());
			log.info("-----------filePath--------- :" + mailMergeProcessed);

			 // 1. Update STATUS_ID = 19 for the selected CLIENT_ID in CLIENTDOCUMENT table 
			dbClientDocument.setStatusId(19L);
			dbClientDocument.setDocumentUrlVersion(String.valueOf(latestDocuementUrlVersion));
			dbClientDocument.setReferenceField8(mailMergeProcessed.getProcessedFilePath());
			dbClientDocument.setDocumentUrl(mailMergeProcessed.getProcessedFilePath());
		} else {
			// Download the Template and give the downloaded URL
			String filePath = propertiesConfig.getDocStorageBasePath();
			filePath = filePath + propertiesConfig.getDocStorageTemplatePath() + 
					propertiesConfig.getDocStorageDocumentPath();
			dbClientDocument.setStatusId(20L);
			dbClientDocument.setReferenceField8(documentTemplate.getDocumentUrl());
			dbClientDocument.setDocumentUrl(documentTemplate.getDocumentUrl());
			dbClientDocument.setDocumentUrlVersion(String.valueOf(latestDocuementUrlVersion));
		}
		
		dbClientDocument.setCreatedBy(loginUserID);
		dbClientDocument.setCreatedOn(new Date());
		dbClientDocument.setUpdatedBy(loginUserID);
		dbClientDocument.setUpdatedOn(new Date());
		dbClientDocument.setDeletionIndicator(0L);
		return clientDocumentRepository.save(dbClientDocument);
	}
	
	/**
	 * 
	 * @param matterNumber
	 * @param newClientDocument
	 * @param documentTemplate
	 * @param mailMerge
	 * @param latestDocuementUrlVersion
	 * @return
	 */
	private MailMerge mailMerge_D018(String matterNumber, AddClientDocument newClientDocument, 
			DocumentTemplate documentTemplate, MailMerge mailMerge, Long latestDocuementUrlVersion) {
		MatterGenAcc matterGenAcc = matterGeneralService.getMatterGenAcc(matterNumber);
		
		/*
		 * Client_ID
		 * -------------
		 * Pass the selected CLIENT_ID in CLIENTGENERAL table and VALIDATE  REF_FIELD_2 is Not Null.
		 * If Yes Fetch CLIENT_ID value and fill. 
		 * If No, Error message ""Selected Client Category is not Dependent
		 */
		ClientGeneral clientGeneral = clientGeneralService.getClientGeneral (matterGenAcc.getClientId());
		String name = null;
		if (clientGeneral.getReferenceField2() == null) {
			name = clientGeneral.getFirstNameLastName();
		} else {
			throw new BadRequestException("Selected Client Category is not Dependent.");
		}
		
		// Beneficiary Name
		// Fetch FIRST_LAST_NM of the selected CLIIENT_ID and fill
		String beneficiaryName = clientGeneral.getFirstNameLastName();
		
		String[] arrFieldNames = new String[] {
			"CLIENT_ID", 
			"BENEFICIARY_NAME", 
			"BENEFICIARY_NAME", 
			"BENEFICIARY_NAME"
		};
		
		Object[] arrFieldValues = new Object [] {
			name,								// NAME
			beneficiaryName,					// BENEFICIARY_NAME
			beneficiaryName,					// BENEFICIARY_NAME
			beneficiaryName						// BENEFICIARY_NAME
		};
		
		// Call MailMerge feature
		mailMerge.setDocumentUrl(documentTemplate.getDocumentUrl());
		mailMerge.setDocumentStorageFolder("document");
		mailMerge.setDocumentUrlVersion(latestDocuementUrlVersion);
		mailMerge.setClassId(newClientDocument.getClassId());
		mailMerge.setClientId(newClientDocument.getClientId());
		mailMerge.setDocumentCode(newClientDocument.getDocumentNo());
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setFromClientORMatterDocument(true);
		return mailMerge;
	}
	
	/**
	 * 
	 * @param matterNumber
	 * @param newClientDocument
	 * @param documentTemplate
	 * @param mailMerge
	 * @param latestDocuementUrlVersion
	 * @return
	 */
	private MailMerge mailMerge_D004(String matterNumber, AddClientDocument newClientDocument, 
			DocumentTemplate documentTemplate, MailMerge mailMerge, Long latestDocuementUrlVersion) {
		MatterGenAcc matterGenAcc = matterGeneralService.getMatterGenAcc(matterNumber);
		
		/*
		 * Name
		 * ----------------------
		 * Pass MATTER_NO in MATTERGENACC table and fetch CLIENT_ID. Pass CLIENT_ID in CLIENTGENERAL table and 
		 * fetch FIRST_LAST_NM value and fill
		 */
		ClientGeneral clientGeneral = clientGeneralService.getClientGeneral (matterGenAcc.getClientId());
		String name = clientGeneral.getFirstNameLastName();
		
		/*
		 * CorpName
		 * -----------------------
		 * 1. Pass MATTER_NO in MATTERGENACC table and fetch CLIENT_ID.
		 * 2. Pass CLIENT_ID in CLIENTGENERAL table and fetch CORP_CLIENT_ID value
		 * 3. Pass CORP_CLIENT_ID as CLIENT_ID in CLIENTGENERAL table and fetch FIRST_LAST_NM and autoofill
		 */
		String corpName = null;
		if (clientGeneral.getCorporationClientId() != null) {
			ClientGeneral corpClientGeneral = clientGeneralService.getClientGeneral(clientGeneral.getCorporationClientId());
			corpName = corpClientGeneral.getFirstNameLastName();
		}
		
		/*
		 * Approved_Date
		 * ---------------------
		 * Pass MATTER_NO in EXPIRATIONDATE table and fetch APPROVAL_DATE value and fill
		 */
		ExpirationDate expirationDate = expirationDateService.getExpirationDate(matterGenAcc.getMatterNumber(), matterGenAcc.getLanguageId(), 
				matterGenAcc.getClassId(), matterGenAcc.getClientId());
		
		String[] arrFieldNames = new String[] {
			"Name", 
			"Corporation_Name", 
			"Approved_Date", 
			"Expiration_Date"
		};
		
		Object[] arrFieldValues = new Object [] {
			name,								// NAME
			corpName,							// CORPNAME
			expirationDate.getApprovalDate(),	// ApprovalDate
			expirationDate.getExpirationDate()	// Expiration Date
		};
		
		// Call MailMerge feature
		mailMerge.setDocumentUrl(documentTemplate.getDocumentUrl());
		mailMerge.setDocumentStorageFolder("document");
		mailMerge.setDocumentUrlVersion(latestDocuementUrlVersion);
		mailMerge.setClassId(newClientDocument.getClassId());
		mailMerge.setClientId(newClientDocument.getClientId());
		mailMerge.setDocumentCode(newClientDocument.getDocumentNo());
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setFromClientORMatterDocument(true);
		return mailMerge;
	}
	
	/**
	 * 
	 * @param clientGeneral 
	 * @param matterNumber
	 * @param newClientDocument
	 * @param documentTemplate
	 * @param mailMerge
	 * @param latestDocuementUrlVersion
	 * @return
	 */
	private MailMerge mailMerge_D003(String matterNumber, AddClientDocument newClientDocument, 
			DocumentTemplate documentTemplate, MailMerge mailMerge, Long latestDocuementUrlVersion) {
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		MatterGenAcc matterGenAcc = matterGeneralService.getMatterGenAcc(matterNumber);
		
		/*
		 * Assigned TK
		 * ------------------
		 * Pass the selected MATTER_NO in MATTERASSIGNMENT table and fetch ASSIGNED_TK value.
		 * Pass ASSIGNED_TK in USERPROFILE table as USR_ID and fetch FULL_NM and autofill"
		 */
		MatterAssignment matterAssignment = matterAssignmentService.getMatterAssignment(matterNumber);
		String assignedTK = matterAssignment.getAssignedTimeKeeper();
		
		if (assignedTK == null) {
			throw new BadRequestException("Assigned TK is blank so, cannot be mail merged.");
		}
		
		UserProfile userProfile = setupService.getUserProfile(assignedTK, authTokenForSetupService.getAccess_token());
		if (userProfile == null) {
			throw new BadRequestException("UserProfile is blank so, cannot be mail merged.");
		}
		assignedTK = userProfile.getFullName();
		
		/*
		 * Name
		 * ----------------------
		 * Pass MATTER_NO in MATTERGENACC table and fetch CLIENT_ID. Pass CLIENT_ID in CLIENTGENERAL table and 
		 * fetch FIRST_LAST_NM value and fill
		 */
		ClientGeneral clientGeneral = clientGeneralService.getClientGeneral (matterGenAcc.getClientId());
		String name = clientGeneral.getFirstNameLastName();
		
		/*
		 * CorpName
		 * -----------------------
		 * 1. Pass MATTER_NO in MATTERGENACC table and fetch CLIENT_ID.
		 * 2. Pass CLIENT_ID in CLIENTGENERAL table and fetch CORP_CLIENT_ID value
		 * 3. Pass CORP_CLIENT_ID as CLIENT_ID in CLIENTGENERAL table and fetch FIRST_LAST_NM and autoofill
		 */
		String corpName = null;
		String corpAddress = null;
		if (clientGeneral.getCorporationClientId() != null) {
			ClientGeneral corpClientGeneral = clientGeneralService.getClientGeneral(clientGeneral.getCorporationClientId());
			corpName = corpClientGeneral.getFirstNameLastName();
			corpAddress = corpClientGeneral.getAddressLine1();
		}
		
		String[] arrFieldNames = new String[] {
			"Assigned_TK", 
			"Name", 
			"Corp_Name", 
			"Corp_Address", 
			"Address_Line_1",
			"Name",
			"Email_Address",
			"Cell",
			"Assigned_TK",
			"Assigned_TK"
		};
		
		Object[] arrFieldValues = new Object [] {
			assignedTK,							// ASSIGNED_TK
			name,								// NAME
			corpName,							// CORPNAME
			corpAddress,						// CORP_ADDRESS
			clientGeneral.getAddressLine1(),	// Address Line 1
			name,								// NAME
			clientGeneral.getEmailId(),			// Email_Address
			clientGeneral.getContactNumber(),	// Cell
			assignedTK,							// ASSIGNED_TK
			assignedTK							// ASSIGNED_TK
		};
		
		// Call MailMerge feature
		mailMerge.setDocumentUrl(documentTemplate.getDocumentUrl());
		mailMerge.setDocumentStorageFolder("document");
		mailMerge.setDocumentUrlVersion(latestDocuementUrlVersion);
		mailMerge.setClassId(newClientDocument.getClassId());
		mailMerge.setClientId(newClientDocument.getClientId());
		mailMerge.setDocumentCode(newClientDocument.getDocumentNo());
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setFromClientORMatterDocument(true);
		return mailMerge;
	}
	
	/**
	 * 
	 * @param clientGeneral
	 * @param matterGenAcc
	 * @param dbMatterGenAcc
	 * @param newClientDocument
	 * @param documentTemplate
	 * @param mailMerge
	 * @param latestDocuementUrlVersion
	 * @return
	 */
	private MailMerge mailMerge_D002(String matterNumber, AddClientDocument newClientDocument, DocumentTemplate documentTemplate,
			MailMerge mailMerge, Long latestDocuementUrlVersion) {
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		
		/*
		 * Assigned TK
		 * ------------------
		 * Pass the selected MATTER_NO in MATTERASSIGNMENT table and fetch ASSIGNED_TK value.
		 * Pass ASSIGNED_TK in USERPROFILE table as USR_ID and fetch FULL_NM and autofill"
		 */
		MatterAssignment matterAssignment = matterAssignmentService.getMatterAssignment(matterNumber);
		String assignedTK = matterAssignment.getAssignedTimeKeeper();
		
		if (assignedTK == null) {
			throw new BadRequestException("Assigned TK is blank so, cannot be mail merged.");
		}
		
		UserProfile userProfile = setupService.getUserProfile(assignedTK, authTokenForSetupService.getAccess_token());
		if (userProfile == null) {
			throw new BadRequestException("UserProfile is blank so, cannot be mail merged.");
		}
		assignedTK = userProfile.getFullName();
		
		/*
		 * Responsible TK
		 * ------------------
		 * Pass the selected MATTER_NO in MATTERASSIGNMENT table and fetch RESPONSIBLE_TK value.
		 * Pass RESPONSIBLE_TK in USERPROFILE table as USR_ID and fetch FULL_NM and autofill
		 */
		String responsibleTK = matterAssignment.getResponsibleTimeKeeper();
		if (responsibleTK == null) {
			throw new BadRequestException("Responsible TK is blank so, cannot be mail merged.");
		}
		
		userProfile = setupService.getUserProfile(responsibleTK, authTokenForSetupService.getAccess_token());
		if (userProfile == null) {
			throw new BadRequestException("UserProfile is blank so, cannot be mail merged.");
		}
		
		responsibleTK = userProfile.getFullName();
		
		String[] arrFieldNames = new String[] {
			"Assigned_TK", "Responsible_TK"
		};
		
		Object[] arrFieldValues = new Object [] {
				assignedTK,							// FULL_NM
				responsibleTK						// FULL_NM
		};
		
		// Call MailMerge feature
		mailMerge.setDocumentUrl(documentTemplate.getDocumentUrl());
		mailMerge.setDocumentStorageFolder("document");
		mailMerge.setDocumentUrlVersion(latestDocuementUrlVersion);
		mailMerge.setClassId(newClientDocument.getClassId());
		mailMerge.setClientId(newClientDocument.getClientId());
		mailMerge.setDocumentCode(newClientDocument.getDocumentNo());
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setFromClientORMatterDocument(true);
		return mailMerge;
	}

	/**
	 * 
	 * @param clientGeneral
	 * @param matterGenAcc
	 * @param dbMatterGenAcc 
	 * @param newClientDocument
	 * @param documentTemplate
	 * @param mailMerge
	 * @param latestDocuementUrlVersion 
	 * @return
	 */
	private MailMerge mailMerge_D001 (ClientGeneral clientGeneral, Optional<MatterGenAcc> matterGenAcc, 
			MatterGenAcc dbMatterGenAcc, AddClientDocument newClientDocument, DocumentTemplate documentTemplate,
			MailMerge mailMerge, Long latestDocuementUrlVersion) {
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		
		// Name
		/*
		 * 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch CLIENT_ID
		 * 2. Pass CLIENT_ID in CLIENTGENERAL table and fetch FIRST_LAST_NM
		 */
		String name = clientGeneral.getFirstNameLastName();
		
		//Subject
		/*
		 * 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch CASE_SUB_CATEGORY_ID
		 * 2. Pass  CASE_SUB_CATEGORY_ID in CASESUBCATEGORY table and fetch CASE_SUB_CATEGORY. 
		 * Merge both values and fill
		 */
		if (matterGenAcc.get().getCaseSubCategoryId() == null) {
			throw new BadRequestException ("Error : CaseSubCategoryId is not set for Matter ID : " + newClientDocument.getMatterNumber()
			+ " so, Client Document cannot be created.");
		}
		
		log.info("matterGenAcc.get().getCaseSubCategoryId() ; " + dbMatterGenAcc.getCaseSubCategoryId());
		
		CaseSubcategory caseSubcategory = 
				setupService.getCaseSubcategory(dbMatterGenAcc.getCaseSubCategoryId(), dbMatterGenAcc.getLanguageId(),
						dbMatterGenAcc.getClassId(), dbMatterGenAcc.getCaseCategoryId(), authTokenForSetupService.getAccess_token());
		String subject = newClientDocument.getMatterNumber() + "-" + caseSubcategory.getSubCategory();
		
		// Case category
		// Pass the selected MATTER_NO in MATTERGENACC table and fetch CASE_SUB_CATEGORY_ID
		Long caseSubcategoryId = matterGenAcc.get().getCaseSubCategoryId();
		
		// Petitioning Company
		/*
		 * 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch CLIENT_ID
		 * 2. Pass CLIENT_ID in CLIENTGENERAL table and fetch CORP_CLIENT_ID
		 * 3. Pass CORP_CLIENT_ID as CLIENT_ID in CLIENTGENERAL table and fetch FIRST_LAST_NM
		 */
		if (clientGeneral.getCorporationClientId() == null) {
			throw new BadRequestException("Selected Document is applicable only for Corporation employee.");
		}
		clientGeneral = clientGeneralService.getClientGeneral(clientGeneral.getCorporationClientId());
		String petitioningCompany = clientGeneral.getFirstNameLastName();
		
		if (dbMatterGenAcc.getApprovalDate() == null) {
			throw new BadRequestException("ApprovalDate cannot be null.");
		}
		
		// Approved Date
		// 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch APPROVAL_DATE
		LocalDateTime approvalDate = LocalDateTime.ofInstant(matterGenAcc.get().getApprovalDate().toInstant(), ZoneId.systemDefault());
		
		// Expiration date
		// 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch EXPIRTATION_DATE
		LocalDateTime expirationDate = LocalDateTime.ofInstant(matterGenAcc.get().getExpirationDate().toInstant(), ZoneId.systemDefault());
		
		String[] arrFieldNames = new String[] {
			"Date", "Name", "Subject", "Case_Category", "Petitioning_Company", "Approved_Date", "Expiration_Date"
		};
		Object[] arrFieldValues = new Object [] {
				DateUtils.getCurrentDateTime(), 	// Date
				name,								// Name
				subject,							// Subject
				caseSubcategoryId,					// Case_Category
				petitioningCompany,  				// Petitioning Company
				approvalDate,						// Approved Date
				expirationDate						// Expiration date
		};
		
		// Call MailMerge feature
		mailMerge.setDocumentUrl(documentTemplate.getDocumentUrl());
		mailMerge.setDocumentStorageFolder("document");
		mailMerge.setDocumentUrlVersion(latestDocuementUrlVersion);
		mailMerge.setClassId(newClientDocument.getClassId());
		mailMerge.setClientId(newClientDocument.getClientId());
		mailMerge.setDocumentCode(newClientDocument.getDocumentNo());
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setFromClientORMatterDocument(true);
		return mailMerge;
	}
	
	/**
	 * 
	 * @param clientId
	 * @param documentNumber
	 * @param matterNumber
	 * @param loginUserId
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientDocument sendDocumentToDocusign (Long classId, String clientId, String documentNumber, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException {
		// Validate DocuSign integration is enable for the selected document by validating 
		// REF_FIELD_1= TRUE of DOCUMENTTEMPLATE table by passing DOC_NO. If yes, then
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		DocumentTemplate documentTemplate = setupService.getDocumentTemplate(documentNumber, authTokenForSetupService.getAccess_token());
		log.info("documentTemplate Docusign flag :" + documentTemplate.getReferenceField1());
		
		if (documentTemplate.getReferenceField1() == null) {
			throw new BadRequestException("Docusign flag is not set for the given Document Id: " + documentNumber);
		}
		
		/*
		 * Deciding the FilePath (whether LNE or Immigration
		 */
		String filePath = "";
		if (classId == 1) {								// - LNE
			// Choose Y:\Client\2 Employment-Labor Clients\Clara
			filePath = propertiesConfig.getDocStorageLNEPath() + 
					propertiesConfig.getDocStorageDocumentPath() + "/" + clientId;
			log.info("LNE path : " + filePath);
		} else { 																// - Immigration
			// Choose X:\Firm\Immigration Section\1LawOfficeDoc\Clara
			filePath = propertiesConfig.getDocStorageImmigrationPath() + 
					propertiesConfig.getDocStorageDocumentPath() + "/" + clientId;
			log.info("Immigration path : " + filePath);
		}
		
		// Docusign flag is set 
		if (documentTemplate.getReferenceField1().equalsIgnoreCase("true")) {
			ClientGeneral clientGeneral = clientGeneralService.getClientGeneral(clientId);
			List<ClientDocument> existingClientDocuments = 
					clientDocumentRepository.findTopByLanguageIdAndClassIdAndClientIdAndDocumentNoOrderByDocumentUrlVersionDesc  (
					"EN", classId, clientId, documentNumber);
			log.info("existingClientDocuments----> : " + existingClientDocuments);
			
			if (existingClientDocuments == null) {
				throw new BadRequestException("Error: Record not found for the given values Client ID:" + clientId + ", documentNumber: " + documentNumber);
			}
			
			String filename = null;
			ClientDocument clientDocument = existingClientDocuments.get(0);
			if (clientDocument.getDocumentUrlVersion() != null) {
				log.info("Latest Filename : " + clientDocument.getDocumentUrl());
				filename = clientDocument.getDocumentUrl();
			}
			
			if (filename != null && filename.startsWith("/")) {
				filename = clientDocument.getReferenceField8().substring(1); 		// Removing Front slash from URL
			}
		
			filename = filename.substring(0, filename.lastIndexOf('.'));			// Removing extension of file
			log.info("-------filename ------- : " + filename);
		
			// Call Docusign envelope request
			// Get AuthToken for SetupService
			AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
			
			/*
			 * authToken, agreementCode, documentId, file, docName, signerName, signerEmail	
			 */
			EnvelopeResponse envelopeResponse = commonService.callDocusignEnvelope(
					authTokenForCommonService.getAccess_token(), 
					documentNumber,
					clientDocument.getClientId(),
					existingClientDocuments.get(0).getDocumentUrl(),
					filename,
					clientGeneral.getFirstNameLastName(),
					clientGeneral.getEmailId(),
					filePath);
			log.info("envelopeResponse : " + envelopeResponse);
			
			//1. document will be sent to DocuSign and update STATUS_ID=12 in CLIENTDOCUMENT table by passing CLIENT_ID
			clientDocument.setStatusId(12L);
			clientDocument.setSentBy(loginUserId);
			clientDocument.setSentOn(new Date());
			
			// URL and version need to set
			return clientDocumentRepository.save(clientDocument);
		}
		return null;
	}

	/**
	 * updateClientGeneral
	 * @param clientId
	 * @param updateClientDocument
	 * @param loginUserID
	 * @return
	 */
	public ClientDocument updateClientDocument(Long clientId, @Valid UpdateClientDocument updateClientDocument, String loginUserID) {
		ClientDocument dbClientDocument = getClientDocument(clientId);
		BeanUtils.copyProperties(updateClientDocument, dbClientDocument, CommonUtils.getNullPropertyNames(updateClientDocument));
		dbClientDocument.setUpdatedBy(loginUserID);
		dbClientDocument.setUpdatedOn(new Date());
		log.info("After modified : " + dbClientDocument);
		return clientDocumentRepository.save(dbClientDocument);
	}

	/**
	 * 
	 * @param clientId
	 * @param loginUserID
	 * @return
	 */
	public ClientDocument downloadEnvelopeFromDocusign(Long classId, String clientId, String documentNumber, String loginUserID) {
		/*
		 * Deciding the FilePath (whether LNE or Immigration
		 */
		String filePath = "";
		if (classId == 1) {								// - LNE
			// Choose Y:\Client\2 Employment-Labor Clients\Clara
			filePath = propertiesConfig.getDocStorageLNEPath() + 
					propertiesConfig.getDocStorageDocumentPath() + "/" + clientId;
			log.info("LNE path : " + filePath);
		} else { 																// - Immigration
			// Choose X:\Firm\Immigration Section\1LawOfficeDoc\Clara
			filePath = propertiesConfig.getDocStorageImmigrationPath() + 
					propertiesConfig.getDocStorageDocumentPath() + "/" + clientId;
			log.info("Immigration path : " + filePath);
		}
		
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		String response = 
				commonService.downloadClientMatterDocumentEnvelopeFromDocusign(clientId, 
						authTokenForCommonService.getAccess_token(), filePath);
		List<ClientDocument> existingClientDocuments = 
				clientDocumentRepository.findTopByLanguageIdAndClassIdAndClientIdAndDocumentNoOrderByDocumentUrlVersionDesc  (
				"EN", classId, clientId, documentNumber);
		log.info("existingClientDocuments----> : " + existingClientDocuments);
		
		ClientDocument clientDocument = existingClientDocuments.get(0);
		clientDocument.setDocumentUrl(response);
		clientDocument.setReferenceField8(response);
		clientDocument.setStatusId(15L); //Hard coded as 15
		clientDocument = clientDocumentRepository.save(clientDocument);
		log.info("downloadEnvelopeFromDocusign : " + clientDocument);
		log.info("response : " + response);
		return clientDocument;
	}
	public void deleteClientDocument (Long clientDocumentId, String loginUserID) {
		ClientDocument clientDocument = getClientDocument(clientDocumentId);
		if (clientDocument != null) {
			clientDocument.setDeletionIndicator(1L);
			clientDocument.setUpdatedBy(loginUserID);
			clientDocument.setUpdatedOn(new Date());
			clientDocumentRepository.save(clientDocument);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + clientDocumentId);
		}
	}
	/**
	 * 
	 * @param clientId
	 * @param loginUserID
	 * @return
	 */
	public EnvelopeStatus getEnvelopeStatusFromDocusign(String clientId, String loginUserID) {
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		EnvelopeStatus envStatus = commonService.getDocusignEnvelopeStatus(authTokenForCommonService.getAccess_token(), clientId);
		return envStatus;	
	}

	/**
	 * createNonMailmergeClientDocument
	 * @param classId
	 * @param clientId
	 * @param documentNumber
	 * @param documentUrl
	 * @param loginUserID
	 * @return
	 */
	public ClientDocument doProcessEditedClientDocument(Long classId, String clientId, 
			String documentNumber, String documentUrl, String loginUserID) {
		List<ClientDocument> existingClientDocuments = 
				clientDocumentRepository.findTopByLanguageIdAndClassIdAndClientIdAndDocumentNoOrderByDocumentUrlVersionDesc  (
				"EN", classId, clientId, documentNumber);
		log.info("existingClientDocuments---> : " + existingClientDocuments);
		
		if (existingClientDocuments == null) {
			throw new BadRequestException("Error: Record not found for the given values Client ID:" + clientId + ", documentNumber: " + documentNumber);
		} 
		
		ClientDocument clientDocument = existingClientDocuments.get(0);
		Long docUrlVersion = Long.valueOf(clientDocument.getDocumentUrlVersion());
		docUrlVersion ++;
		
		clientDocument.setDocumentUrlVersion(String.valueOf(docUrlVersion));
		clientDocument.setDocumentDate(new Date());				
		clientDocument.setDocumentUrl(documentUrl);
		clientDocument = clientDocumentRepository.save(clientDocument);
		return clientDocument;
	}
}
