package com.mnrclara.api.management.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.config.PropertiesConfig;
import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.clientgeneral.ClientGeneral;
import com.mnrclara.api.management.model.dto.CaseSubcategory;
import com.mnrclara.api.management.model.dto.ClientUser;
import com.mnrclara.api.management.model.dto.DocumentTemplate;
import com.mnrclara.api.management.model.dto.EMail;
import com.mnrclara.api.management.model.dto.EnvelopeResponse;
import com.mnrclara.api.management.model.dto.EnvelopeStatus;
import com.mnrclara.api.management.model.dto.MailMerge;
import com.mnrclara.api.management.model.dto.UserProfile;
import com.mnrclara.api.management.model.expirationdate.ExpirationDate;
import com.mnrclara.api.management.model.matterassignment.MatterAssignment;
import com.mnrclara.api.management.model.matterdocument.AddMatterDocument;
import com.mnrclara.api.management.model.matterdocument.MatterDocument;
import com.mnrclara.api.management.model.matterdocument.SearchMatterDocument;
import com.mnrclara.api.management.model.matterdocument.UpdateMatterDocument;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.repository.ClientGeneralRepository;
import com.mnrclara.api.management.repository.MatterDocumentRepository;
import com.mnrclara.api.management.repository.MatterGenAccRepository;
import com.mnrclara.api.management.repository.specification.MatterDocumentSpecification;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatterDocumentService {
	
	private static final String D001 = "D001";
	private static final String D002 = "D002";
	private static final String D003 = "D003";
	private static final String D004 = "D004";
	private static final String D018 = "D018";

	@Autowired
	private MatterDocumentRepository matterDocumentRepository;
	
	@Autowired
	private MatterGenAccRepository matterGenAccRepository;
	
	@Autowired
	private MatterAssignmentService matterAssignmentService;
	
	@Autowired
	private MatterGenAccService matterGeneralService;

	@Autowired
	private ClientGeneralService clientGeneralService;
	
	@Autowired
	private ClientGeneralRepository clientGeneralRepository;

	@Autowired
	private SetupService setupService;
	
	@Autowired
	ExpirationDateService expirationDateService;

	@Autowired
	CommonService commonService;

	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	PropertiesConfig propertiesConfig;

	/**
	 * getMatterDocuments
	 * 
	 * @return
	 */
	public List<MatterDocument> getMatterDocuments() {
		List<MatterDocument> matterDocumentList = matterDocumentRepository.findAll();
//		log.info("matterDocumentList : " + matterDocumentList);
		matterDocumentList = matterDocumentList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return matterDocumentList;
	}

	/**
	 * getMatterDocument
	 * 
	 * @param matterDocumentId
	 * @return
	 */
	public MatterDocument getMatterDocument(Long matterDocumentId) {
		MatterDocument matterDocument = matterDocumentRepository.findByMatterDocumentId(matterDocumentId).orElse(null);
		if (matterDocument != null && 
				matterDocument.getDeletionIndicator() != null && matterDocument.getDeletionIndicator() == 0) {
			return matterDocument;
		} else {
			throw new BadRequestException("The given MatterDocument ID : " + matterDocumentId + " doesn't exist.");
		}
	}
	
	/**
	 * findMatterDocument
	 * @param searchMatterDocument
	 * @return
	 * @throws ParseException 
	 */
	public List<MatterDocument> findMatterDocument(SearchMatterDocument searchMatterDocument) throws ParseException {
		if (searchMatterDocument.getSSentOn() != null && searchMatterDocument.getESentOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterDocument.getSSentOn(), searchMatterDocument.getESentOn());
			searchMatterDocument.setSSentOn(dates[0]);
			searchMatterDocument.setESentOn(dates[1]);
		}
		
		if (searchMatterDocument.getSReceivedOn() != null && searchMatterDocument.getEReceivedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterDocument.getSReceivedOn(), searchMatterDocument.getEReceivedOn());
			searchMatterDocument.setSReceivedOn(dates[0]);
			searchMatterDocument.setEReceivedOn(dates[1]);
		}
		
		MatterDocumentSpecification spec = new MatterDocumentSpecification(searchMatterDocument);
		List<MatterDocument> results = matterDocumentRepository.findAll(spec);
		log.info("results: " + results);
		return results;
	}

	/**
	 * createMatterDocument
	 * 
	 * @param newMatterDocument
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterDocument createMatterDocument(AddMatterDocument newMatterDocument, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		Optional<MatterGenAcc> matterGenAcc = matterGenAccRepository.findByMatterNumber(newMatterDocument.getMatterNumber());
		if (matterGenAcc.isEmpty()) {
			throw new BadRequestException("Matter General has no relevant record for the given MatterNumber : " + newMatterDocument.getMatterNumber());
		}
		log.info("matterGenAcc : " + matterGenAcc);
		MatterGenAcc dbMatterGenAcc = matterGenAcc.get();
		MatterDocument dbMatterDocument = new MatterDocument();
		
		// Copy other attributes
		BeanUtils.copyProperties(newMatterDocument, dbMatterDocument, CommonUtils.getNullPropertyNames(newMatterDocument));
		dbMatterDocument.setMatterDocumentId(System.currentTimeMillis());
		
		// CLIENT_ID/FIRST_LAST_NM
		String clientId = newMatterDocument.getMatterNumber();
		clientId = clientId.substring(0, clientId.indexOf("-"));
		dbMatterDocument.setClientId(clientId);

		// CLIENT_USR_ID
		dbMatterDocument.setClientUserId(newMatterDocument.getClientUserId());

		// DOC_NO
		dbMatterDocument.setDocumentNo(newMatterDocument.getDocumentNo());
		
		// DOC_URL
		dbMatterDocument.setDocumentUrl(newMatterDocument.getDocumentUrl());

		// MATTER_NO
		dbMatterDocument.setMatterNumber(newMatterDocument.getMatterNumber());

		// SEQ
		dbMatterDocument.setSequenceNo(newMatterDocument.getSequenceNo());
		
		// DOC_DATE_TIME
		dbMatterDocument.setDocumentDate(new Date());

		// LANG_ID
		dbMatterDocument.setLanguageId("EN");
		
		// CLASS_ID
		dbMatterDocument.setClassId(dbMatterGenAcc.getClassId());
		
		// MAIL_MERGE
		// Pass the selected DOC_NO value and fetch MAIL_MERGE value from
		// DOCUMENTTEMPLATE table and auto populate
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		DocumentTemplate documentTemplate = setupService.getDocumentTemplate(newMatterDocument.getDocumentNo(),
				authTokenForSetupService.getAccess_token());
		log.info("documentTemplate Mailmerge :" + documentTemplate.getMailMerge());
		
		/*
		 * Assigning documentVersion for every existing set of records
		 */
		List<MatterDocument> existingMatterDocuments = matterDocumentRepository.findTopByLanguageIdAndClassIdAndMatterNumberAndClientIdAndDocumentNoOrderByDocumentUrlVersionDesc (
				dbMatterDocument.getLanguageId(),
				dbMatterDocument.getClassId(), 
				newMatterDocument.getMatterNumber(),
				clientId, // Client ID
				newMatterDocument.getDocumentNo());
		log.info("existingMatterDocuments----> : " + existingMatterDocuments);
		
		Long latestDocuementUrlVersion = 0L;
		if (existingMatterDocuments != null && existingMatterDocuments.size() > 0) {
			MatterDocument existingMatterDocument = existingMatterDocuments.get(0);
			if (existingMatterDocument.getDocumentUrlVersion() != null) {
				log.info("Latest version number : " + existingMatterDocument.getDocumentUrlVersion());
				latestDocuementUrlVersion = Long.valueOf(existingMatterDocument.getDocumentUrlVersion());
				latestDocuementUrlVersion ++;
			}
		} 

		// If MailMerge is Enabled
		if (Boolean.valueOf(documentTemplate.getMailMerge()).booleanValue() == true) {
			// Get required data for MailMerge
			MailMerge mailMerge = new MailMerge();
			if (newMatterDocument.getDocumentNo().equalsIgnoreCase(D001)) {
				mailMerge = mailMerge_D001 (dbMatterGenAcc, newMatterDocument, mailMerge, 
						documentTemplate, latestDocuementUrlVersion);
			} else if (newMatterDocument.getDocumentNo().equalsIgnoreCase(D002)) {
				mailMerge = mailMerge_D002 (dbMatterGenAcc, newMatterDocument, mailMerge, 
						documentTemplate, latestDocuementUrlVersion);
			} else if (newMatterDocument.getDocumentNo().equalsIgnoreCase(D003)) {
				mailMerge = mailMerge_D003 (dbMatterGenAcc, newMatterDocument, mailMerge, 
						documentTemplate, latestDocuementUrlVersion);
			} else if (newMatterDocument.getDocumentNo().equalsIgnoreCase(D004)) {
				mailMerge = mailMerge_D004 (dbMatterGenAcc, newMatterDocument, mailMerge, 
						documentTemplate, latestDocuementUrlVersion);
			} else if (newMatterDocument.getDocumentNo().equalsIgnoreCase(D018)) {
				mailMerge = mailMerge_D018 (dbMatterGenAcc, newMatterDocument, mailMerge, 
						documentTemplate, latestDocuementUrlVersion);
			}
						
			/*
			 * Pass the selected DOC_NO/DOC_TEXT in dropbox and Fetch the respective
			 * document template (stored in combination DOC_CODE/DOC_TEXT) from Document
			 * template folder of Dropbox.
			 */
			AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
			MailMerge mailMergeProcessed = commonService.mailMerge(mailMerge, authTokenForCommonService.getAccess_token());
			log.info("-----------filePath--------- :" + mailMergeProcessed);

			 // 1. Update STATUS_ID = 19 for the selected CLIENT_ID in CLIENTDOCUMENT table 
			dbMatterDocument.setStatusId(19L);
			dbMatterDocument.setDocumentUrlVersion(String.valueOf(latestDocuementUrlVersion));
			dbMatterDocument.setReferenceField8(mailMergeProcessed.getProcessedFilePath());
			dbMatterDocument.setDocumentUrl(mailMergeProcessed.getProcessedFilePath());
		} else {
			// Download the Template and give the downloaded URL
			dbMatterDocument.setStatusId(20L);
			dbMatterDocument.setReferenceField8(documentTemplate.getDocumentUrl());
			dbMatterDocument.setDocumentUrl(documentTemplate.getDocumentUrl());
			dbMatterDocument.setDocumentUrlVersion(String.valueOf(latestDocuementUrlVersion));
		}
		
		dbMatterDocument.setCreatedBy(loginUserID);
		dbMatterDocument.setCreatedOn(new Date());
		dbMatterDocument.setUpdatedBy(loginUserID);
		dbMatterDocument.setUpdatedOn(new Date());
		dbMatterDocument.setDeletionIndicator(0L);
		
		return matterDocumentRepository.save(dbMatterDocument);
	}
	
	/**
	 * 
	 * @param dbMatterGenAcc
	 * @param newMatterDocument
	 * @param mailMerge
	 * @param documentTemplate
	 * @param latestDocuementUrlVersion
	 * @return
	 */
	private MailMerge mailMerge_D018(MatterGenAcc dbMatterGenAcc, AddMatterDocument newMatterDocument, MailMerge mailMerge,
			DocumentTemplate documentTemplate, Long latestDocuementUrlVersion) {
		MatterGenAcc matterGenAcc = matterGeneralService.getMatterGenAcc(newMatterDocument.getMatterNumber());
		
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
		mailMerge.setClassId(newMatterDocument.getClassId());
		mailMerge.setClientId(newMatterDocument.getClientId() + "/" + newMatterDocument.getMatterNumber());
		mailMerge.setDocumentCode(newMatterDocument.getDocumentNo());
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setFromClientORMatterDocument(true);
		return mailMerge;
	}
	
	/**
	 * 
	 * @param dbMatterGenAcc
	 * @param newMatterDocument
	 * @param mailMerge
	 * @param documentTemplate
	 * @param latestDocuementUrlVersion
	 * @return
	 */
	private MailMerge mailMerge_D004(MatterGenAcc dbMatterGenAcc, AddMatterDocument newMatterDocument, MailMerge mailMerge,
			DocumentTemplate documentTemplate, Long latestDocuementUrlVersion) {
		MatterGenAcc matterGenAcc = matterGeneralService.getMatterGenAcc(newMatterDocument.getMatterNumber());
		
		/*
		 * Name
		 * ----------------------
		 * Pass MATTER_NO in MATTERGENACC table and fetch CLIENT_ID. Pass CLIENT_ID in CLIENTGENERAL table and 
		 * fetch FIRST_LAST_NM value and fill
		 */
		ClientGeneral clientGeneral = clientGeneralService.getClientGeneral (newMatterDocument.getClientId());
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
		mailMerge.setClassId(newMatterDocument.getClassId());
		mailMerge.setClientId(newMatterDocument.getClientId() + "/" + newMatterDocument.getMatterNumber());
		mailMerge.setDocumentCode(newMatterDocument.getDocumentNo());
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setFromClientORMatterDocument(true);
		return mailMerge;
	}
	
	/**
	 * 
	 * @param dbMatterGenAcc
	 * @param newMatterDocument
	 * @param mailMerge
	 * @param documentTemplate
	 * @param latestDocuementUrlVersion
	 * @return
	 */
	private MailMerge mailMerge_D003(MatterGenAcc dbMatterGenAcc, AddMatterDocument newMatterDocument, MailMerge mailMerge,
			DocumentTemplate documentTemplate, Long latestDocuementUrlVersion) {
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		MatterGenAcc matterGenAcc = matterGeneralService.getMatterGenAcc(newMatterDocument.getMatterNumber());
		
		/*
		 * Assigned TK
		 * ------------------
		 * Pass the selected MATTER_NO in MATTERASSIGNMENT table and fetch ASSIGNED_TK value.
		 * Pass ASSIGNED_TK in USERPROFILE table as USR_ID and fetch FULL_NM and autofill
		 */
		MatterAssignment matterAssignment = matterAssignmentService.getMatterAssignment(dbMatterGenAcc.getMatterNumber());
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
		mailMerge.setClassId(newMatterDocument.getClassId());
		mailMerge.setClientId(newMatterDocument.getClientId() + "/" + newMatterDocument.getMatterNumber());
		mailMerge.setDocumentCode(newMatterDocument.getDocumentNo());
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setFromClientORMatterDocument(true);
		return mailMerge;
	}
	
	/**
	 * 
	 * @param dbMatterGenAcc
	 * @param newMatterDocument
	 * @param mailMerge
	 * @param documentTemplate
	 * @param latestDocuementUrlVersion
	 * @return
	 */
	private MailMerge mailMerge_D002(MatterGenAcc dbMatterGenAcc, AddMatterDocument newMatterDocument, MailMerge mailMerge,
			DocumentTemplate documentTemplate, Long latestDocuementUrlVersion) {
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		
		/*
		 * Assigned TK
		 * ------------------
		 * Pass the selected MATTER_NO in MATTERASSIGNMENT table and fetch ASSIGNED_TK value.
		 * Pass ASSIGNED_TK in USERPROFILE table as USR_ID and fetch FULL_NM and autofill"
		 */
		MatterAssignment matterAssignment = matterAssignmentService.getMatterAssignment(newMatterDocument.getMatterNumber());
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
		mailMerge.setClassId(newMatterDocument.getClassId());
		mailMerge.setClientId(newMatterDocument.getClientId() + "/" + newMatterDocument.getMatterNumber());
		mailMerge.setDocumentCode(newMatterDocument.getDocumentNo());
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setFromClientORMatterDocument(true);
		return mailMerge;
	}
	
	/**
	 * 
	 * @param dbMatterGenAcc
	 * @param newMatterDocument
	 * @param mailMerge
	 * @param documentTemplat
	 * @param latestDocuementUrlVersione
	 * @return 
	 */
	private MailMerge mailMerge_D001 (MatterGenAcc dbMatterGenAcc, AddMatterDocument newMatterDocument, MailMerge mailMerge,
			DocumentTemplate documentTemplate, Long latestDocuementUrlVersion) {
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		// Name
		/*
		 * 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch CLIENT_ID,
		 * 
		 * 2. Pass CLIENT_ID in CLIENTGENERAL table and fetch FIRST_LAST_NM
		 */
		ClientGeneral clientGeneral = clientGeneralService.getClientGeneral(dbMatterGenAcc.getClientId());
		String name = clientGeneral.getFirstNameLastName();
		
		//Subject
		/*
		 * 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch CASE_SUB_CATEGORY_ID
		 * 2. Pass  CASE_SUB_CATEGORY_ID in CASESUBCATEGORY table and fetch CASE_SUB_CATEGORY. 
		 * Merge both values and fill
		 */
		CaseSubcategory caseSubcategory = 
				setupService.getCaseSubcategory(dbMatterGenAcc.getCaseSubCategoryId(), 
						dbMatterGenAcc.getLanguageId(),
						dbMatterGenAcc.getClassId(), 
						dbMatterGenAcc.getCaseCategoryId(), 
						authTokenForSetupService.getAccess_token());
		String subject = newMatterDocument.getMatterNumber() + "-" + caseSubcategory.getSubCategory();
		
		// Case category
		// Pass the selected MATTER_NO in MATTERGENACC table and fetch CASE_SUB_CATEGORY_ID
		Long caseSubcategoryId = dbMatterGenAcc.getCaseSubCategoryId();
		
		// Petitioning Company
		/*
		 * 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch CLIENT_ID
		 * 2. Pass CLIENT_ID in CLIENTGENERAL table and fetch CORP_CLIENT_ID
		 * 3. Pass CORP_CLIENT_ID as CLIENT_ID in CLIENTGENERAL table and fetch FIRST_LAST_NM
		 */
		clientGeneral = clientGeneralService.getClientGeneral(clientGeneral.getCorporationClientId());
		String petitioningCompany = clientGeneral.getFirstNameLastName();
		
		// Approved Date
		// 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch APPROVAL_DATE
		if (dbMatterGenAcc.getApprovalDate() == null) {
			throw new BadRequestException("ApprovalDate cannot be null.");
		}
		
		LocalDateTime approvalDate = 
				LocalDateTime.ofInstant(dbMatterGenAcc.getApprovalDate().toInstant(), ZoneId.systemDefault());
		
		// Expiration date
		// 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch EXPIRTATION_DATE
		LocalDateTime expirationDate = 
				LocalDateTime.ofInstant(dbMatterGenAcc.getExpirationDate().toInstant(), ZoneId.systemDefault());
		
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
		mailMerge.setClassId(newMatterDocument.getClassId());
		mailMerge.setClientId(newMatterDocument.getClientId() + "/" + newMatterDocument.getMatterNumber());
		mailMerge.setDocumentCode(newMatterDocument.getDocumentNo());
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setFromClientORMatterDocument(true);	
		return mailMerge;
	}
	
	/**
	 * 
	 * @param newMatterDocument
	 * @param loginUserID
	 * @return
	 */
	public MatterDocument createClientPortalMatterDocuemnt(@Valid AddMatterDocument newMatterDocument,
			String loginUserID) {
		MatterDocument dbMatterDocument = new MatterDocument();
		
		// Copy other attributes
		BeanUtils.copyProperties(newMatterDocument, dbMatterDocument, CommonUtils.getNullPropertyNames(newMatterDocument));

		dbMatterDocument.setMatterDocumentId(System.currentTimeMillis());
		
		// CLIENT_ID/FIRST_LAST_NM
		String clientId = newMatterDocument.getMatterNumber();
		clientId = clientId.substring(0, clientId.indexOf("-"));
		dbMatterDocument.setClientId(clientId);

		// CLIENT_USR_ID
		dbMatterDocument.setClientUserId(newMatterDocument.getClientUserId());

		// DOC_NO
		dbMatterDocument.setDocumentNo(newMatterDocument.getDocumentNo());
		
		// DOC_URL
		dbMatterDocument.setDocumentUrl(newMatterDocument.getDocumentUrl());

		// MATTER_NO
		dbMatterDocument.setMatterNumber(newMatterDocument.getMatterNumber());

		// SEQ
		dbMatterDocument.setSequenceNo(newMatterDocument.getSequenceNo());
		
		// DOC_DATE_TIME
		dbMatterDocument.setDocumentDate(new Date());

		// LANG_ID
		dbMatterDocument.setLanguageId("EN");
		
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		
		// CLASS_ID
		log.info("UserProfile classId :" + newMatterDocument.getClassId());
		dbMatterDocument.setClassId(newMatterDocument.getClassId());
		
		// MAIL_MERGE
		// Pass the selected DOC_NO value and fetch MAIL_MERGE value from
		// DOCUMENTTEMPLATE table and auto populate
		DocumentTemplate documentTemplate = setupService.getDocumentTemplate(newMatterDocument.getDocumentNo(),
				authTokenForSetupService.getAccess_token());
		log.info("documentTemplate Mailmerge :" + documentTemplate.getMailMerge());
		
		/*
		 * Assigning documentVersion for every existing set of records
		 */
		List<MatterDocument> existingMatterDocuments = matterDocumentRepository.findTopByLanguageIdAndClassIdAndMatterNumberAndClientIdAndDocumentNoOrderByDocumentUrlVersionDesc (
				dbMatterDocument.getLanguageId(),
				dbMatterDocument.getClassId(), 
				newMatterDocument.getMatterNumber(),
				clientId, // Client ID
				newMatterDocument.getDocumentNo());
		log.info("existingMatterDocuments----> : " + existingMatterDocuments);
		
		Long latestDocuementUrlVersion = 0L;
		if (existingMatterDocuments != null && existingMatterDocuments.size() > 0) {
			MatterDocument existingMatterDocument = existingMatterDocuments.get(0);
			if (existingMatterDocument.getDocumentUrlVersion() != null) {
				log.info("Latest version number : " + existingMatterDocument.getDocumentUrlVersion());
				latestDocuementUrlVersion = Long.valueOf(existingMatterDocument.getDocumentUrlVersion());
				latestDocuementUrlVersion ++;
			}
		} 

		// If MailMerge is Enabled
		if (Boolean.valueOf(documentTemplate.getMailMerge()).booleanValue() == true) {
			try {
				// Get required data for MailMerge
				Optional<MatterGenAcc> matterGenAcc = matterGenAccRepository.findByMatterNumber(newMatterDocument.getMatterNumber());
				if (matterGenAcc.isEmpty()) {
					throw new BadRequestException("Matter General has no relevant record for the given MatterNumber : " + newMatterDocument.getMatterNumber());
				}
				
				log.info("matterGenAcc : " + matterGenAcc.get());
				MatterGenAcc dbMatterGenAcc = matterGenAcc.get();
				
				/*
				 * Name
				 * --------
				 * 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch CLIENT_ID
				 * 2. Pass CLIENT_ID in CLIENTGENERAL table and fetch FIRST_LAST_NM
				 */
				ClientGeneral clientGeneral = clientGeneralService.getClientGeneral(dbMatterGenAcc.getClientId());
				String name = clientGeneral.getFirstNameLastName();
				
				/*
				 * Subject
				 * ----------
				 * 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch CASE_SUB_CATEGORY_ID
				 * 2. Pass  CASE_SUB_CATEGORY_ID in CASESUBCATEGORY table and fetch CASE_SUB_CATEGORY. 
				 * Merge both values and fill
				 */
				CaseSubcategory caseSubcategory = 
						setupService.getCaseSubcategory(dbMatterGenAcc.getCaseSubCategoryId(), dbMatterGenAcc.getLanguageId(),
								dbMatterGenAcc.getClassId(), dbMatterGenAcc.getCaseCategoryId(), authTokenForSetupService.getAccess_token());
				log.info("CaseSubcategory: " + dbMatterGenAcc.getCaseSubCategoryId() + "," + dbMatterGenAcc.getLanguageId() + "," + 
								dbMatterGenAcc.getClassId() + "," + dbMatterGenAcc.getCaseCategoryId());;
				String subject = null;
				if (caseSubcategory != null) {
					subject = newMatterDocument.getMatterNumber() + "-" + caseSubcategory.getSubCategory();
				} else {
					subject = newMatterDocument.getMatterNumber();
				}
				
				// Case category
				// Pass the selected MATTER_NO in MATTERGENACC table and fetch CASE_SUB_CATEGORY_ID
				Long caseSubcategoryId = dbMatterGenAcc.getCaseSubCategoryId();
				
				/*
				 * Petitioning Company
				 * ---------------------
				 * 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch CLIENT_ID
				 * 2. Pass CLIENT_ID in CLIENTGENERAL table and fetch CORP_CLIENT_ID
				 * 3. Pass CORP_CLIENT_ID as CLIENT_ID in CLIENTGENERAL table and fetch FIRST_LAST_NM
				 */
				log.info("-----clientGeneral.getCorporationClientId()----> : " + clientGeneral.getCorporationClientId());
				clientGeneral = clientGeneralService.getClientGeneral(clientGeneral.getCorporationClientId());
				if (clientGeneral == null) {
					throw new BadRequestException("Corporation ClientId does not exist.");
				}
				String petitioningCompany = clientGeneral.getFirstNameLastName();
				
				// Approved Date
				// 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch APPROVAL_DATE
				if (dbMatterGenAcc.getApprovalDate() == null) {
					throw new BadRequestException("ApprovalDate cannot be null.");
				}
				
				LocalDateTime approvalDate = 
						LocalDateTime.ofInstant(dbMatterGenAcc.getApprovalDate().toInstant(), ZoneId.systemDefault());
				
				// Expiration date
				// 1. Pass the selected MATTER_NO in MATTERGENACC table and fetch EXPIRTATION_DATE
				if (dbMatterGenAcc.getExpirationDate() == null) {
					throw new BadRequestException("ExpirationDate cannot be null.");
				}
				LocalDateTime expirationDate = 
						LocalDateTime.ofInstant(dbMatterGenAcc.getExpirationDate().toInstant(), ZoneId.systemDefault());
				
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
				AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
				MailMerge mailMerge = new MailMerge();
				mailMerge.setDocumentUrl(documentTemplate.getDocumentUrl());
				mailMerge.setDocumentStorageFolder("clientportal");
				mailMerge.setDocumentUrlVersion(latestDocuementUrlVersion);
				mailMerge.setClassId(newMatterDocument.getClassId());
				mailMerge.setClientId(newMatterDocument.getClientId() + "/" + newMatterDocument.getMatterNumber());
				mailMerge.setDocumentCode(newMatterDocument.getDocumentNo());
				mailMerge.setFieldNames(arrFieldNames);
				mailMerge.setFieldValues(arrFieldValues);
				mailMerge.setFromClientORMatterDocument(true);

				/*
				 * Pass the selected DOC_NO/DOC_TEXT and Fetch the respective
				 * document template (stored in combination DOC_CODE/DOC_TEXT) from Document
				 * template folder.
				 */
				MailMerge mailMergeProcessed = commonService.mailMerge(mailMerge,
						authTokenForCommonService.getAccess_token());
				log.info("-----------filePath--------- :" + mailMergeProcessed);

				 // 4. update STATUS_ID=19 in MATTERDOCUMENT table by passing CLIENT_ID
				dbMatterDocument.setStatusId(19L);
				dbMatterDocument.setDocumentUrlVersion(String.valueOf(latestDocuementUrlVersion));
				dbMatterDocument.setReferenceField8(mailMergeProcessed.getProcessedFilePath());
				dbMatterDocument.setDocumentUrl(mailMergeProcessed.getProcessedFilePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// Download the Template and give the downloaded URL
			String filePath = propertiesConfig.getDocStorageBasePath() + propertiesConfig.getDocStorageTemplatePath() + 
			propertiesConfig.getDocStorageDocumentPath();
			String srcFile = filePath + "/" + documentTemplate.getDocumentUrl();
			log.info("srcFile-------> : " + srcFile);
			
			// /home/admini/mrclara/desktop/winfiles/clara/document10016/10016-01Dual_Representation_Letter_2020.docx
			String tgtFile = propertiesConfig.getDocStorageBasePath() + 
					"/clientportal/" + newMatterDocument.getClientId() + "/" + newMatterDocument.getMatterNumber() + "/" +
					documentTemplate.getDocumentUrl();
			log.info("tgtFile-------> : " + tgtFile);
			
			Path sourceFile = Paths.get(srcFile);
			Path targetFile = Paths.get(tgtFile);
			
			try {
				if (!Files.exists(targetFile)) {
					targetFile = Files.createDirectories(targetFile);
				}
			    Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ex) {
				log.info("File Error: " + ex.getLocalizedMessage());
				ex.printStackTrace();
			}
			
			// For Non-mail merge - Status ID: 20L
			dbMatterDocument.setStatusId(20L);
			dbMatterDocument.setReferenceField8(documentTemplate.getDocumentUrl());
			dbMatterDocument.setDocumentUrl(documentTemplate.getDocumentUrl());
			dbMatterDocument.setDocumentUrlVersion(String.valueOf("0"));
			log.info("dbMatterDocument--docurl-----> : " + documentTemplate.getDocumentUrl());
		}
		
		dbMatterDocument.setCreatedBy(loginUserID);
		dbMatterDocument.setCreatedOn(new Date());
		dbMatterDocument.setUpdatedBy(loginUserID);
		dbMatterDocument.setUpdatedOn(new Date());
		dbMatterDocument.setDeletionIndicator(0L);
		return matterDocumentRepository.save(dbMatterDocument);
	}
	
	/**
	 * 
	 * @param newMatterDocument
	 * @param loginUserID
	 * @return
	 */
	public MatterDocument createMatterDocuemntForClientPortalDocsUpload (@Valid AddMatterDocument newMatterDocument,
			String loginUserID) {
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		MatterDocument dbMatterDocument = new MatterDocument();
		
		// Copy other attributes
		BeanUtils.copyProperties(newMatterDocument, dbMatterDocument, CommonUtils.getNullPropertyNames(newMatterDocument));
		dbMatterDocument.setMatterDocumentId(System.currentTimeMillis());
		
		// CLIENT_ID/
		ClientUser clientUser = setupService.getClientUser(loginUserID, authTokenForSetupService.getAccess_token());
		dbMatterDocument.setClientId(clientUser.getClientId());

		// CLIENT_USR_ID
		dbMatterDocument.setClientUserId(loginUserID);

		/*
		 * DOC_NO
		 * -----------
		 * Pass CLASS_ID = 03, NUM_RAN_CODE = 21 in NUMBERRANGE table and Fetch NUM_RAN_CURRENT values and 
		 * add +1 and then insert 
		 */
		long CLASS_ID = 3;
		long NUM_RAN_CODE = 21;
		String DOC_NUMBER = setupService.getNextNumberRange(CLASS_ID, NUM_RAN_CODE, authTokenForSetupService.getAccess_token());
		dbMatterDocument.setDocumentNo(DOC_NUMBER);
		
		// DOC_URL
		dbMatterDocument.setDocumentUrl(newMatterDocument.getDocumentUrl());

		// MATTER_NO
		dbMatterDocument.setMatterNumber(newMatterDocument.getMatterNumber());

		// SEQ
		dbMatterDocument.setSequenceNo(newMatterDocument.getSequenceNo());
		
		// DOC_DATE_TIME
		dbMatterDocument.setDocumentDate(new Date());

		// LANG_ID
		dbMatterDocument.setLanguageId("EN");
		
		// CLASS_ID
		// Pass MATTER_NO  in MATTERGENERALACC table and fetch CLASS_ID
		Optional<MatterGenAcc> optMatterGenAcc = matterGenAccRepository.findByMatterNumber(dbMatterDocument.getMatterNumber());
		log.info("optMatterGenAcc for classId :" + optMatterGenAcc);
		if (!optMatterGenAcc.isEmpty()) {
			dbMatterDocument.setClassId(optMatterGenAcc.get().getClassId());
		}
		
		// DOC_URL_VER
		dbMatterDocument.setDocumentUrlVersion("1");
		dbMatterDocument.setStatusId(newMatterDocument.getStatusId());
		
		// RECEIVED_ON
		dbMatterDocument.setReceivedOn(new Date());
		
		// REF_FIELD_2 - Hard Coded Value "CLIENTPORTAL"
		dbMatterDocument.setReferenceField2("CLIENTPORTAL");
		
		dbMatterDocument.setDeletionIndicator(0L);
		dbMatterDocument.setUpdatedBy(loginUserID);
		dbMatterDocument.setUpdatedOn(new Date());
		dbMatterDocument.setCreatedBy(loginUserID);
		dbMatterDocument.setCreatedOn(new Date());
		MatterDocument createdMatterDocument = matterDocumentRepository.save(dbMatterDocument);
		log.info("createdMatterDocument :" + createdMatterDocument);
		
		/*
		 * Sending Email
		 * ---------------------
		 * 1. Pass MatterNo to MatterAssignment and get Ref_field_2 is Paralegal, Legal Assist is LegalAssist
		 * 2. Pass Ref_field_2 and LegalAssit (User IDs) to UserProfile table and get respective email and send mail
		 * only if STATUS_ID is 23
		 */
		if (createdMatterDocument.getStatusId() == 23L) {
			MatterAssignment matterAssignment = matterAssignmentService.getMatterAssignment(dbMatterDocument.getMatterNumber());
			Optional<ClientGeneral> clientGeneral = clientGeneralRepository.findByClientId(matterAssignment.getClientId());
			
			if (matterAssignment != null) {
				String paraLegalUserId = matterAssignment.getReferenceField2(); // Para Legal
				String legalAssistUserId = matterAssignment.getLegalAssistant(); // LegalAssist
				
				log.info("paraLegalUserId : " + paraLegalUserId);
				log.info("legalAssistUserId : " + legalAssistUserId);
				
				// Para Legal
				if (paraLegalUserId != null) {
					UserProfile userProfile = setupService.getUserProfile(paraLegalUserId, authTokenForSetupService.getAccess_token());
					if (userProfile != null ) {
						sendMail (userProfile.getEmailId(), userProfile.getFullName(), dbMatterDocument.getMatterNumber(),
								clientGeneral.get().getFirstNameLastName());				
					}
				}
				
				if (legalAssistUserId != null) {
					UserProfile userProfile = setupService.getUserProfile(legalAssistUserId, authTokenForSetupService.getAccess_token());
					if (userProfile != null ) {
						sendMail (userProfile.getEmailId(), userProfile.getFullName(), dbMatterDocument.getMatterNumber(),
								clientGeneral.get().getFirstNameLastName());	
					}
				}
			}
		}
		return createdMatterDocument;
	}
	
	/**
	 * 
	 * @param emailId
	 * @param firstName
	 * @param matterNo
	 */
	private void sendMail (String emailId, String firstName, String matterNo, String firstNameLastName) {
		EMail email = new EMail();
		String subject = "Document uploaded for Matter Number:" + matterNo;
		String msg = "<p style=\"font-family:'Times New Roman';color:blue;size:18px;\">" +
				" Dear " + firstName + ", <br/><br/> "
				+ firstNameLastName + " has uploaded a new document for Matter Number: [" + matterNo + "]. "
				+ "Please view it in Client Portal Documents within Matter Management.";
		msg += "<br/><br/>";
		msg += "Thank you,";
		msg += "<br/>M&R CLARA</p>";
		
		email.setFromAddress(propertiesConfig.getLoginEmailFromAddress());
		email.setSubject(subject);
		email.setBodyText(msg);
		email.setToAddress(emailId);
		email.setSenderName("ClaraITAdmin");
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getCommonServiceAuthToken();
		boolean isEMailSent = commonService.sendEmail(email, authTokenForSetupService.getAccess_token());
		log.info("isEMailSent:" + isEMailSent);
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
	public MatterDocument sendDocumentToDocusign (Long classId, String matterNumber, String documentNumber, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException {
		
		// Validate DocuSign integration is enable for the selected document by validating 
		// REF_FIELD_1= TRUE of DOCUMENTTEMPLATE table by passing DOC_NO. If yes, then
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		DocumentTemplate documentTemplate = setupService.getDocumentTemplate(documentNumber,
				authTokenForSetupService.getAccess_token());
		log.info("documentTemplate Docusign flag :" + documentTemplate.getReferenceField1());
		
		if (documentTemplate.getReferenceField1() == null) {
			throw new BadRequestException("Docusign flag is not set for the given Document Id: " + documentNumber);
		}
		
		// Client ID
		String clientId = matterNumber.substring(0, matterNumber.indexOf("-"));
				
		/*
		 * Deciding the FilePath (whether LNE or Immigration
		 */
		String filePath = "";
		if (classId == 1) {														// - LNE
			// Choose Y:\Client\2 Employment-Labor Clients\Clara
			filePath = propertiesConfig.getDocStorageLNEPath() + 
					propertiesConfig.getDocStorageDocumentPath() + "/" + clientId + "/" + matterNumber;
			log.info("LNE path : " + filePath);
		} else { 																// - Immigration
			// Choose X:\Firm\Immigration Section\1LawOfficeDoc\Clara
			filePath = propertiesConfig.getDocStorageImmigrationPath() + 
					propertiesConfig.getDocStorageDocumentPath() + "/" + clientId + "/" + matterNumber;
			log.info("Immigration path : " + filePath);
		}
		
		if (documentTemplate.getReferenceField1().equalsIgnoreCase("true")) {
			ClientGeneral clientGeneral = clientGeneralService.getClientGeneral(clientId);
			List<MatterDocument> existingMatterDocuments = 
					matterDocumentRepository.findTopByLanguageIdAndClassIdAndMatterNumberAndClientIdAndDocumentNoOrderByDocumentUrlVersionDesc (
					"EN", classId, matterNumber, clientId, documentNumber);
			log.info("existingMatterDocuments----> : " + existingMatterDocuments);
			
			if (existingMatterDocuments == null) {
				throw new BadRequestException("Error: Record not found for the given values, MatterNumber:" + matterNumber + ", documentNumber: " + documentNumber);
			}
			
			MatterDocument matterDocument = existingMatterDocuments.get(0);
			String filename = matterDocument.getDocumentUrl();
			if (filename != null && filename.startsWith("/")) {
				filename = matterDocument.getReferenceField8().substring(1); 	// Removing Front slash from URL
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
					matterDocument.getMatterNumber(),
					matterDocument.getDocumentUrl(),
					filename,
					clientGeneral.getFirstNameLastName(),
					clientGeneral.getEmailId(),
					filePath);
			log.info("envelopeResponse : " + envelopeResponse);
			
			//1. document will be sent to DocuSign and update STATUS_ID=12 in MATTERDOCUMENT table by passing CLIENT_ID
			matterDocument.setStatusId(12L);
			matterDocument.setSentBy(loginUserId);
			matterDocument.setSentOn(new Date());
			
			// URL and version need to set
			return matterDocumentRepository.save(matterDocument);
		}
		return null;
	}
	
	/**
	 * doProcessEditedMatterDocument
	 * @param classId
	 * @param matterNumber
	 * @param clientId
	 * @param documentNumber
	 * @param documentUrldoProcessEditedMatterDocument
	 * @param loginUserID
	 * @return
	 */
//	public MatterDocument doProcessEditedMatterDocument(Long classId, String matterNumber, String documentNumber, String documentUrl, String loginUserID) {
//		// Client ID
//		String clientId = matterNumber.substring(0, matterNumber.indexOf("-"));
//		List<MatterDocument> existingMatterDocuments = 
//				matterDocumentRepository.findTopByLanguageIdAndClassIdAndMatterNumberAndClientIdAndDocumentNoOrderByDocumentUrlVersionDesc  (
//				"EN", classId, matterNumber, clientId, documentNumber);
//		log.info("existingMatterDocuments---> : " + existingMatterDocuments);
//		
//		if (existingMatterDocuments.isEmpty()) {
//			throw new BadRequestException("Error: Record not found for the given values Client ID:" + matterNumber + ", documentNumber: " + documentNumber);
//		} 
//		
//		MatterDocument matterDocument = existingMatterDocuments.get(0);
//		Long docUrlVersion = Long.valueOf(matterDocument.getDocumentUrlVersion());
//		docUrlVersion ++;
//		
//		matterDocument.setDocumentUrlVersion(String.valueOf(docUrlVersion));
//		matterDocument.setDocumentDate(new Date());				
//		matterDocument.setDocumentUrl(documentUrl);
//		matterDocument = matterDocumentRepository.save(matterDocument);
//		return matterDocument;
//	}
	
	/**
	 * 
	 * @param classId
	 * @param matterNumber
	 * @param documentNumber
	 * @param documentUrl
	 * @param loginUserID
	 * @return
	 */
	public MatterDocument doProcessEditedMatterDocument(Long matterDocumentId, String documentUrl, String loginUserID) {
		Optional<MatterDocument> existingMatterDocument = matterDocumentRepository.findByMatterDocumentId(matterDocumentId);
		log.info("existingMatterDocuments---> : " + existingMatterDocument);
		
		if (existingMatterDocument.isEmpty()) {
			throw new BadRequestException("Error: Record not found for the given value :" + matterDocumentId);
		} 
		
		MatterDocument matterDocument = existingMatterDocument.get();
		matterDocument.setDocumentDate(new Date());				
		matterDocument.setDocumentUrl(documentUrl);
		matterDocument = matterDocumentRepository.save(matterDocument);
		return matterDocument;
	}
	
	/**
	 * 
	 * @param classId
	 * @param matterNumber
	 * @param documentNumber
	 * @param documentUrl
	 * @param loginUserID
	 * @return
	 */
	public MatterDocument doProcessEditedMatterDocumentForClientPortal (Long classId, String matterNumber, 
			String documentNumber, String documentUrl, String loginUserID) {
		// Client ID
		String clientId = matterNumber.substring(0, matterNumber.indexOf("-"));
		List<MatterDocument> existingMatterDocuments = 
				matterDocumentRepository.findTopByLanguageIdAndClassIdAndMatterNumberAndClientIdAndDocumentNoOrderByDocumentUrlVersionDesc  (
				"EN", classId, matterNumber, clientId, documentNumber);
		log.info("existingMatterDocuments---> : " + existingMatterDocuments);
		
		if (existingMatterDocuments == null) {
			throw new BadRequestException("Error: Record not found for the given values Client ID:" + matterNumber + ", documentNumber: " + documentNumber);
		} 
		
		MatterDocument matterDocument = existingMatterDocuments.get(0);
		Long docUrlVersion = Long.valueOf(matterDocument.getDocumentUrlVersion());
		docUrlVersion ++;
		
		matterDocument.setDocumentUrlVersion(String.valueOf(docUrlVersion));
		matterDocument.setDocumentDate(new Date());				
		matterDocument.setDocumentUrl(documentUrl);
		matterDocument = matterDocumentRepository.save(matterDocument);
		return matterDocument;
	}

	/**
	 * updateMatterDocument
	 * @param matterDocumentId
	 * @param updateMatterDocument
	 * @param loginUserID
	 * @return
	 */
	public MatterDocument updateMatterDocument(Long matterDocumentId,
			@Valid UpdateMatterDocument updateMatterDocument, String loginUserID) {
		MatterDocument dbMatterDocument = getMatterDocument(matterDocumentId);
		BeanUtils.copyProperties(updateMatterDocument, dbMatterDocument, CommonUtils.getNullPropertyNames(updateMatterDocument));
		dbMatterDocument.setUpdatedBy(loginUserID);
		dbMatterDocument.setUpdatedOn(new Date());
		log.info("After modified : " + dbMatterDocument);
		return matterDocumentRepository.save(dbMatterDocument);
	}
	public void deleteMatterDocument (Long matterDocumentId, String loginUserID) {
		MatterDocument matterDocument = getMatterDocument(matterDocumentId);
		if (matterDocument != null) {
			matterDocument.setDeletionIndicator(1L);
			matterDocument.setUpdatedBy(loginUserID);
			matterDocument.setUpdatedOn(new Date());
			matterDocumentRepository.save(matterDocument);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + matterDocumentId);
		}
	}
	/**
	 * 
	 * @param matterNumber
	 * @param loginUserID
	 * @return
	 */
	public MatterDocument downloadEnvelopeFromDocusign(Long classId, String matterNumber, String documentNumber, String loginUserID) {
		String clientId = matterNumber.substring(0, matterNumber.indexOf("-"));
		
		/*
		 * Deciding the FilePath (whether LNE or Immigration
		 */
		String filePath = "";
		if (classId == 1) {								// - LNE
			filePath = propertiesConfig.getDocStorageLNEPath() + 
					propertiesConfig.getDocStorageDocumentPath() + "/" + clientId + "/" + matterNumber;
			log.info("LNE path : " + filePath);
		} else { 																// - Immigration
			filePath = propertiesConfig.getDocStorageImmigrationPath() + 
					propertiesConfig.getDocStorageDocumentPath() + "/" + clientId + "/" + matterNumber;
			log.info("Immigration path : " + filePath);
		}
		
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		
		String response = 
				commonService.downloadClientMatterDocumentEnvelopeFromDocusign(matterNumber, 
						authTokenForCommonService.getAccess_token(), filePath);
		List<MatterDocument> existingMatterDocuments = matterDocumentRepository.findTopByLanguageIdAndClassIdAndMatterNumberAndClientIdAndDocumentNoOrderByDocumentUrlVersionDesc (
				"EN", classId, matterNumber, clientId, documentNumber);
		log.info("existingMatterDocuments----> : " + existingMatterDocuments);
		
		MatterDocument matterDocument = existingMatterDocuments.get(0);
		matterDocument.setDocumentUrl(response);
		matterDocument.setReferenceField8(response);
		matterDocument.setStatusId(15L); //Hard coded as 15
		matterDocument = matterDocumentRepository.save(matterDocument);
		log.info("downloadEnvelopeFromDocusign : " + matterNumber);
		log.info("response : " + response);
		return matterDocument;
	}
	
	/**
	 * getEnvelopeStatusFromDocusign
	 * @param matterNumber
	 * @param loginUserID
	 * @return
	 */
	public EnvelopeStatus getEnvelopeStatusFromDocusign(String matterNumber, String loginUserID) {
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		EnvelopeStatus envStatus = commonService.getDocusignEnvelopeStatus(authTokenForCommonService.getAccess_token(), matterNumber);
		return envStatus;	
	}
}
