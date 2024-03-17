package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.config.PropertiesConfig;
import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.caseinfosheet.ImmCaseInfoSheet;
import com.mnrclara.api.management.model.caseinfosheet.LeCaseInfoSheet;
import com.mnrclara.api.management.model.clientgeneral.AddClientGeneral;
import com.mnrclara.api.management.model.clientgeneral.ClientGeneral;
import com.mnrclara.api.management.model.clientgeneral.ClientGeneralIMMReport;
import com.mnrclara.api.management.model.clientgeneral.ClientGeneralLNEReport;
import com.mnrclara.api.management.model.clientgeneral.*;
import com.mnrclara.api.management.model.clientgeneral.SearchClientGeneral;
import com.mnrclara.api.management.model.clientgeneral.SearchClientGeneralIMMReport;
import com.mnrclara.api.management.model.clientgeneral.SearchClientGeneralLNEReport;
import com.mnrclara.api.management.model.clientgeneral.UpdateClientGeneral;
import com.mnrclara.api.management.model.dto.AddClientUser;
import com.mnrclara.api.management.model.dto.Agreement;
import com.mnrclara.api.management.model.dto.ClientCategory;
import com.mnrclara.api.management.model.dto.ClientUser;
import com.mnrclara.api.management.model.dto.DashboardReport;
import com.mnrclara.api.management.model.dto.IKeyValuePair;
import com.mnrclara.api.management.model.dto.KeyValuePair;
import com.mnrclara.api.management.model.dto.PotentialClient;
import com.mnrclara.api.management.model.dto.UserProfile;
import com.mnrclara.api.management.model.dto.docketwise.AddressAttribute;
import com.mnrclara.api.management.model.dto.docketwise.Contact;
import com.mnrclara.api.management.model.dto.docketwise.CreateContact;
import com.mnrclara.api.management.model.dto.docketwise.Data;
import com.mnrclara.api.management.model.dto.docketwise.Matter;
import com.mnrclara.api.management.model.dto.docketwise.PhoneNumberAttribute;
import com.mnrclara.api.management.model.dto.docketwise.UpdateContact;
import com.mnrclara.api.management.repository.ClientGeneralRepository;
import com.mnrclara.api.management.repository.specification.ClientGeneralIMMReportSpecification;
import com.mnrclara.api.management.repository.specification.ClientGeneralLNEReportSpecification;
import com.mnrclara.api.management.repository.specification.ClientGeneralSpecification;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientGeneralService {

	@Autowired
	private ClientGeneralRepository clientGeneralRepository;

	@Autowired
	private SetupService setupService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private CRMService crmService;

	@Autowired
	private LeCaseInfoSheetService leCaseInfoSheetService;

	@Autowired
	private ImmCaseInfoSheetService immCaseInfoSheetService;

	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	PropertiesConfig propertiesConfig;

	private static final String CLIENTGENERAL = "CLIENTGENERAL";
	private static final String POTENTIAL_CLIENT_ID = "999999";
	private static final String INQUIRY_NO = "999999";

	/**
	 * getClientGenerals
	 * 
	 * @return
	 */
	public List<ClientGeneral> getClientGenerals() {
		List<ClientGeneral> clientGeneralList = clientGeneralRepository.findAll();
		clientGeneralList = clientGeneralList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return clientGeneralList;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<KeyValuePair> getAllClientList() {
		List<IKeyValuePair> ikeyValues = clientGeneralRepository.findClientNames();
		List<KeyValuePair> clientList = new ArrayList<>();
		for (IKeyValuePair iKeyValuePair : ikeyValues) {
			KeyValuePair keyValuePair = new KeyValuePair();
			keyValuePair.setKey(iKeyValuePair.getKeyIndex());
			keyValuePair.setValue(iKeyValuePair.getValue());
			clientList.add(keyValuePair);
		} 
		return clientList;
	}
	
	/**
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param sortBy
	 * @param classId 
	 * @return
	 */
	public Page<ClientGeneral> getAllClientGenerals(Integer pageNo, Integer pageSize, String sortBy, List<Long> classId) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		Page<IClientGeneral> pagedResult = clientGeneralRepository.findAllClientsByClassId (classId, pageable);
		
		List<IClientGeneral> iClientGeneralList = pagedResult.getContent();
		List<ClientGeneral> clientGeneralList = new ArrayList<>();
		for (IClientGeneral iClientGeneral: iClientGeneralList) {
			ClientGeneral clientGeneral = new ClientGeneral();
			
			clientGeneral.setClientId(iClientGeneral.getClientId());
			clientGeneral.setLanguageId(iClientGeneral.getLanguageId());
			clientGeneral.setClassId(iClientGeneral.getClassId());
			clientGeneral.setClientCategoryId(iClientGeneral.getClientCategoryId());
			clientGeneral.setTransactionId(iClientGeneral.getTransactionId());
			clientGeneral.setPotentialClientId(iClientGeneral.getPotentialClientId());
			clientGeneral.setInquiryNumber(iClientGeneral.getInquiryNumber());
			clientGeneral.setIntakeFormId(iClientGeneral.getIntakeFormId());
			clientGeneral.setIntakeFormNumber(iClientGeneral.getIntakeFormNumber());
			clientGeneral.setFirstName(iClientGeneral.getFirstName());
			clientGeneral.setLastName(iClientGeneral.getLastName());
			clientGeneral.setFirstNameLastName(iClientGeneral.getFirstNameLastName());
			clientGeneral.setLastNameFirstName(iClientGeneral.getLastNameFirstName());
			clientGeneral.setCorporationClientId(iClientGeneral.getCorporationClientId());
			clientGeneral.setReferralId(iClientGeneral.getReferralId());
			clientGeneral.setEmailId(iClientGeneral.getEmailId());
			clientGeneral.setContactNumber(iClientGeneral.getContactNumber());
			clientGeneral.setAddressLine1(iClientGeneral.getAddressLine1());
			clientGeneral.setAddressLine2(iClientGeneral.getAddressLine2());
			clientGeneral.setAddressLine3(iClientGeneral.getAddressLine3());
			clientGeneral.setCity(iClientGeneral.getCity());
			clientGeneral.setState(iClientGeneral.getState());
			clientGeneral.setCountry(iClientGeneral.getCountry());
			clientGeneral.setZipCode(iClientGeneral.getZipCode());
			clientGeneral.setConsultationDate(iClientGeneral.getConsultationDate());
			clientGeneral.setSocialSecurityNo(iClientGeneral.getSocialSecurityNo());
			clientGeneral.setMailingAddress(iClientGeneral.getMailingAddress());
			clientGeneral.setOccupation(iClientGeneral.getOccupation());
			clientGeneral.setStatusId(iClientGeneral.getStatusId());
			clientGeneral.setSuiteDoorNo(iClientGeneral.getSuiteDoorNo());
			clientGeneral.setWorkNo(iClientGeneral.getWorkNo());
			clientGeneral.setHomeNo(iClientGeneral.getHomeNo());
			clientGeneral.setFax(iClientGeneral.getFax());
			clientGeneral.setAlternateEmailId(iClientGeneral.getAternateEmailId());
			clientGeneral.setIsMailingAddressSame(iClientGeneral.getIsMailingAddressSame());
			clientGeneral.setCreatedBy(iClientGeneral.getCreatedBy());
			clientGeneral.setCreatedOn(iClientGeneral.getCreatedOn());
			clientGeneral.setUpdatedBy(iClientGeneral.getUpdatedBy());
			clientGeneral.setUpdatedOn(iClientGeneral.getUpdatedOn());
			
			// Class
			clientGeneral.setReferenceField29(clientGeneralRepository.getClassName(clientGeneral.getClientId()));
			
			clientGeneral.setCreatedOnString(DateUtils.dateToString(clientGeneral.getCreatedOn()));
			
			// Status
			clientGeneral.setReferenceField30(clientGeneralRepository.getStatusId(clientGeneral.getClientId()));
			clientGeneralList.add(clientGeneral);
		}
		final Page<ClientGeneral> page = new PageImpl<>(clientGeneralList, pageable, pagedResult.getTotalElements());
		return page;
	}
	
	/**
	 * getClientGeneral
	 * 
	 * @param clientGeneralId
	 * @return
	 */
	public ClientGeneral getClientGeneral(String clientGeneralId) {
		ClientGeneral clientGeneral = clientGeneralRepository.findByClientId(clientGeneralId).orElse(null);
		if (clientGeneral != null && clientGeneral.getDeletionIndicator() != null
				&& clientGeneral.getDeletionIndicator() == 0) {
			return clientGeneral;
		} 
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public ClientGeneral getLatestClientGeneral() {
		ClientGeneral clientGeneral = clientGeneralRepository.findTopByOrderByCreatedOnDesc();
		return clientGeneral;
	}

	/**
	 * 
	 * @return
	 */
	public ClientGeneral getClientGeneralByLimit() {
		ClientGeneral clientGeneral = clientGeneralRepository
				.findTopBySentToQBAndDeletionIndicatorOrderByCreatedOn(0L, 0L);
		return clientGeneral;
	}

	/**
	 * 
	 * @param fullTextSearch
	 * @return
	 */
	public List<ClientGeneral> findRecords(String fullTextSearch) {
		List<ClientGeneral> clientGeneral = clientGeneralRepository.findRecords(fullTextSearch);
		return clientGeneral;
	}

	/**
	 * createClientGeneral
	 * 
	 * @param newClientGeneral
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientGeneral createClientGeneral(AddClientGeneral newClientGeneral, String loginUserID,
			boolean isCalledFromPotentialEndpoint) throws IllegalAccessException, InvocationTargetException {
		ClientGeneral dbClientGeneral = new ClientGeneral();

		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		UserProfile userProfile = setupService.getUserProfile(loginUserID, authTokenForSetupService.getAccess_token());
		log.info("userProfile : " + userProfile);

		// LANG_ID
		dbClientGeneral.setLanguageId(userProfile.getLanguageId());

		// CLASS_ID
		dbClientGeneral.setClassId(userProfile.getClassId());

		// During Save, Pass CLASS_ID, NUM_RAN_CODE=05 in NUMBERRANGE table and Fetch
		// NUM_RAN_CURRENT values and add +1 and then insert
		long classID = newClientGeneral.getClassId().longValue();
		long NUM_RAN_CODE = 5;
		if (classID == 1) {
			NUM_RAN_CODE = 5;
		} else if (classID == 2) {
			NUM_RAN_CODE = 6;
		}
		String newClientId = setupService.getNextNumberRange(classID, NUM_RAN_CODE,
				authTokenForSetupService.getAccess_token());
		log.info("nextVal from NumberRange for newClientId: " + newClientId);

		// CLIENT_ID
		dbClientGeneral.setClientId(newClientId);

		// LAST_NM
		dbClientGeneral.setLastName(newClientGeneral.getLastName());

		// FIRST_NM
		dbClientGeneral.setFirstName(newClientGeneral.getFirstName());

		// CLIENT_CAT_ID
		dbClientGeneral.setClientCategoryId(newClientGeneral.getClientCategoryId());

		// CORP_CLIENT_ID
		dbClientGeneral.setCorporationClientId(newClientGeneral.getCorporationClientId());

		if (isCalledFromPotentialEndpoint) {
			// POT_CLIENT_ID
			dbClientGeneral.setPotentialClientId(newClientGeneral.getPotentialClientId());

			// INQ_NO
			dbClientGeneral.setInquiryNumber(newClientGeneral.getInquiryNumber());
		} else {
			// POT_CLIENT_ID
			dbClientGeneral.setPotentialClientId(POTENTIAL_CLIENT_ID); // Hard Coded value - 999999

			// INQ_NO
			dbClientGeneral.setInquiryNumber(INQUIRY_NO); // Hard Coded value - 999999
		}

		// REFERRAL_ID/REFERRAL_TEXT
		dbClientGeneral.setReferralId(newClientGeneral.getReferralId());

		// IS_MAIL_SAME
		dbClientGeneral.setIsMailingAddressSame(newClientGeneral.getIsMailingAddressSame());

		// MAIL_ADDRESS
		if (newClientGeneral.getIsMailingAddressSame().booleanValue()) {
			dbClientGeneral.setMailingAddress(newClientGeneral.getAddressLine1());
		}

		// STATUS_ID
		// During Save, insert hard coded value "18"
		dbClientGeneral.setStatusId(18L);

		// Copies other attributes
		BeanUtils.copyProperties(newClientGeneral, dbClientGeneral, CommonUtils.getNullPropertyNames(newClientGeneral));

		// CTD_BY - logged in USR_ID
		dbClientGeneral.setCreatedBy(loginUserID);

		// CTD_ON - Server time
		dbClientGeneral.setCreatedOn(new Date());

		// UTD_BY - logged in USR_ID
		dbClientGeneral.setUpdatedBy(loginUserID);

		// UTD_ON - Server time
		dbClientGeneral.setUpdatedOn(new Date());

		// IS_DELETED = 0
		dbClientGeneral.setDeletionIndicator(0L);
		dbClientGeneral.setSentToQB(0L);

		//address section
		dbClientGeneral.setReferenceField15(newClientGeneral.getReferenceField15());
		dbClientGeneral.setReferenceField16(newClientGeneral.getReferenceField16());
		dbClientGeneral.setReferenceField17(newClientGeneral.getReferenceField17());
		dbClientGeneral.setReferenceField18(newClientGeneral.getReferenceField18());
		dbClientGeneral.setReferenceField19(newClientGeneral.getReferenceField19());
		dbClientGeneral.setReferenceField20(newClientGeneral.getReferenceField20());
		
		ClientGeneral clientGeneral = clientGeneralRepository.save(dbClientGeneral);
		log.info("clientGeneral created: " + clientGeneral);

		/*
		 * -----------------------------------------------------------------------------
		 * Create Client Portal User creation Once a record is inserted
		 * successfully into CLIENTGENERAL table, insert a record in CLIENTUSER table as
		 * below
		 * -----------------------------------------------------------------------------
		 */
		ClientUser createdClientUser = createClientPortalUser(clientGeneral);
		clientGeneral.setReferenceField11(createdClientUser.getClientUserId());

		String immCaseInfoSheetID = null;
		// When a CLIENT_ID with CLASS_ID=1 is created by inserting a record in
		// CLIENTGENERAL table, insert a record in LECASEINFOSHEET with below Logic
		if (clientGeneral.getClassId().longValue() == 1 && clientGeneral.getClientId() != null) {
			LeCaseInfoSheet leCaseInfoSheet = new LeCaseInfoSheet();

			// LANG_ID
			leCaseInfoSheet.setLanguageId("EN");

			// CLASS_ID
			leCaseInfoSheet.setClassId(clientGeneral.getClassId());

			// CASEINFO_NO
			// Pass CLASS_ID=01, NUM_RAN_CODE=08 in NUMBERRANGE table and Fetch
			// NUM_RAN_CURRENT values and add +1 and then insert
			classID = 1L;
			NUM_RAN_CODE = 8;
			String leCaseInfoSheetID = setupService.getNextNumberRange(classID, NUM_RAN_CODE,
					authTokenForSetupService.getAccess_token());
			log.info("nextVal from NumberRange for leCaseInfoSheetID: " + leCaseInfoSheetID);
			leCaseInfoSheet.setId(leCaseInfoSheetID);

			leCaseInfoSheet.setClientId(clientGeneral.getClientId());

			// Copy the rest of the parameters
			BeanUtils.copyProperties(clientGeneral, leCaseInfoSheet, CommonUtils.getNullPropertyNames(clientGeneral));

			// STATUS_ID
			leCaseInfoSheet.setStatusId(1L); // Hard Coded Value "1"

			// CTD_BY - logged in USR_ID
			leCaseInfoSheet.setCreatedBy(loginUserID);

			// CTD_ON - Server time
			leCaseInfoSheet.setCreatedOn(new Date());

			// UTD_BY - logged in USR_ID
			leCaseInfoSheet.setUpdatedBy(loginUserID);

			// UTD_ON - Server time
			leCaseInfoSheet.setUpdatedOn(new Date());

			// IS_DELETED = 0
			leCaseInfoSheet.setDeletionIndicator(0L);

			leCaseInfoSheetService.createLeCaseInfoSheet(leCaseInfoSheet, loginUserID);
			log.info("LeCaseInfoSheet created.");
		} else if (clientGeneral.getClassId().longValue() == 2 && clientGeneral.getClientId() != null) {
			// When a CLIENT_ID with CLASS_ID=2 is created by inserting a record in
			// CLIENTGENERAL table, insert a record in IMMCASEINFOSHEET with below Logic
			ImmCaseInfoSheet immCaseInfoSheet = new ImmCaseInfoSheet();

			// LANG_ID
			immCaseInfoSheet.setLanguageId("EN");

			// CLASS_ID
			immCaseInfoSheet.setClassId(clientGeneral.getClassId());

			// CASEINFO_NO
			// Pass CLASS_ID=02, NUM_RAN_CODE=09 in NUMBERRANGE table and Fetch
			// NUM_RAN_CURRENT values and add +1 and then insert
			classID = 2L;
			NUM_RAN_CODE = 9;
			immCaseInfoSheetID = setupService.getNextNumberRange(classID, NUM_RAN_CODE,
					authTokenForSetupService.getAccess_token());
			log.info("nextVal from NumberRange for immCaseInfoSheetID: " + immCaseInfoSheetID);
			immCaseInfoSheet.setId(immCaseInfoSheetID);

			// Copy other attributes from ClientGeneral to immCaseInfoSheet
			BeanUtils.copyProperties(clientGeneral, immCaseInfoSheet, CommonUtils.getNullPropertyNames(clientGeneral));

			// STATUS_ID
			immCaseInfoSheet.setStatusId(1L); // Hard Coded Value "1"

			// CTD_BY - logged in USR_ID
			immCaseInfoSheet.setCreatedBy(loginUserID);

			// CTD_ON - Server time
			immCaseInfoSheet.setCreatedOn(new Date());

			// UTD_BY - logged in USR_ID
			immCaseInfoSheet.setUpdatedBy(loginUserID);

			// UTD_ON - Server time
			immCaseInfoSheet.setUpdatedOn(new Date());

			// IS_DELETED = 0
			immCaseInfoSheet.setDeletionIndicator(0L);

			immCaseInfoSheetService.createImmCaseInfoSheet(immCaseInfoSheet, loginUserID);
			log.info("ImmCaseInfoSheet created.");
		}

		// -------------------- Docketwise Integration-----------------------------------------------
		log.info("--------clientGeneral.getClassId()----------> : " + clientGeneral.getClassId());
		log.info("--------propertiesConfig--Docketwise-flag---> : " + propertiesConfig.getDocketwiseStoreFlag());
		
		if (clientGeneral.getClassId().longValue() == 2 && 
				clientGeneral.getClientId() != null && propertiesConfig.getDocketwiseStoreFlag().equalsIgnoreCase("true")) {
			try {
				Contact docketwiseContact = populateDocketwiseContact(clientGeneral);
				CreateContact createContact = new CreateContact();
				createContact.setContact(docketwiseContact);

				// Get AuthToken for CommonService
				AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
				Contact createdContact = commonService.createDocketwiseContact(createContact,
						authTokenForCommonService.getAccess_token());
				log.info("createdContact ::: " + createdContact);

				// Updating ClientGeneral with new created ID
				ClientGeneral updateClientGeneral = getClientGeneral(clientGeneral.getClientId());
				updateClientGeneral.setReferenceField9(String.valueOf(createdContact.getId()));
				updateClientGeneral.setUpdatedBy(loginUserID);
				updateClientGeneral.setUpdatedOn(new Date());
				clientGeneral = clientGeneralRepository.save(updateClientGeneral);
				log.info("clientGeneral ::: " + clientGeneral);
			} catch (Exception e) {
				// deleteCreatedClientFromClaraDB
				clientGeneralRepository.delete(clientGeneral);
				log.info("ClientGeneral deleted: " + clientGeneral + " because of Docketwise error.");
				
				immCaseInfoSheetService.deleteImmCaseInfoSheet(immCaseInfoSheetID);
				log.info("ImmCaseInfoSheet deleted.");
				
				throw new BadRequestException("Docketwise Error: " + e.toString());
			}

			return clientGeneral;
		}
		return clientGeneral;
	}

	/**
	 * Creation of Client Portal ID Once a record is inserted successfully into
	 * CLIENTGENERAL table, insert a record in CLIENTUSER table as below
	 * 
	 * @param createdClientGeneral
	 * @return
	 */
	private ClientUser createClientPortalUser(ClientGeneral createdClientGeneral) {
		AddClientUser newClientUser = new AddClientUser();
		BeanUtils.copyProperties(createdClientGeneral, newClientUser,
				CommonUtils.getNullPropertyNames(createdClientGeneral));

		/*
		 * CLIENT_USR_ID ------------------ Pass CLASS_ID=03, NUM_RAN_CODE=19 in
		 * NUMBERRANGE table and Fetch NUM_RAN_CURRENT values and add +1 and then insert
		 */
		long classID = 3;
		long NUM_RAN_CODE = 19;
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		String clientUserId = setupService.getNextNumberRange(classID, NUM_RAN_CODE,
				authTokenForSetupService.getAccess_token());
		newClientUser.setClientUserId(clientUserId);

		// QUOTATION - Hardcoded Value "1" for CLASS_ID = 2, Hardcoded Value "0" for
		// CLASS_ID=1
		// PAYMENTPLAN - Hardcoded Value "1" for CLASS_ID = 2, Hardcoded Value "0" for
		// CLASS_ID=1
		// DOCUMENT CHECKLIST Hardcoded Value "1" for CLASS_ID = 2, Hardcoded Value "0"
		// for CLASS_ID=1
		if (createdClientGeneral.getClassId() == 1L) {
			newClientUser.setQuotation(0);
			newClientUser.setPaymentPlan(0);
			newClientUser.setDocuments(0);
		} else if (createdClientGeneral.getClassId() == 2L) {
			newClientUser.setQuotation(1);
			newClientUser.setPaymentPlan(1);
			newClientUser.setDocuments(1);
		}

		//clientId
		newClientUser.setClientId(createdClientGeneral.getClientId());
		newClientUser.setMatter(1);
		newClientUser.setInvoice(1);
		newClientUser.setAgreement(1);

		ClientUser createdClientUser = setupService.createClientUser(newClientUser, createdClientGeneral.getCreatedBy(),
				authTokenForSetupService.getAccess_token());
		log.info("createdClientUser :  " + createdClientUser);
		return createdClientUser;
	}

	/**
	 * updateClientGeneral
	 * 
	 * @param clientGeneralId
	 * @param updateClientGeneral
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientGeneral updateClientGeneral(String clientGeneralId, UpdateClientGeneral updateClientGeneral,
			String loginUserID) throws IllegalAccessException, InvocationTargetException {
		ClientGeneral dbClientGeneral = getClientGeneral(clientGeneralId);
		log.info("Retrieving DB Inquiry: " + dbClientGeneral);

		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();

		// LAST_NM
		if (updateClientGeneral.getLastName() != null
				&& !updateClientGeneral.getLastName().equalsIgnoreCase(dbClientGeneral.getLastName())) {
			log.info("Inserting Audit log for LAST_NM");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"LAST_NM", String.valueOf(dbClientGeneral.getLastName()),
					String.valueOf(updateClientGeneral.getLastName()), authTokenForSetupService.getAccess_token());
		}

		// FIRST_NM
		if (updateClientGeneral.getFirstName() != null
				&& !updateClientGeneral.getFirstName().equalsIgnoreCase(dbClientGeneral.getFirstName())) {
			log.info("Inserting Audit log for FIRST_NM");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"FIRST_NM", String.valueOf(dbClientGeneral.getFirstName()),
					String.valueOf(updateClientGeneral.getFirstName()), authTokenForSetupService.getAccess_token());
		}

		// CLIENT_CAT_ID
		if (updateClientGeneral.getClientCategoryId() != null
				&& updateClientGeneral.getClientCategoryId() != dbClientGeneral.getClientCategoryId()) {
			log.info("Inserting Audit log for CLIENT_CAT_ID");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"CLIENT_CAT_ID", String.valueOf(dbClientGeneral.getClientCategoryId()),
					String.valueOf(updateClientGeneral.getClientCategoryId()),
					authTokenForSetupService.getAccess_token());
		}

		// CORP_CLIENT_ID
		if (updateClientGeneral.getCorporationClientId() != null && !updateClientGeneral.getCorporationClientId()
				.equalsIgnoreCase(dbClientGeneral.getCorporationClientId())) {
			log.info("Inserting Audit log for CORP_CLIENT_ID");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"CORP_CLIENT_ID", String.valueOf(dbClientGeneral.getCorporationClientId()),
					String.valueOf(updateClientGeneral.getCorporationClientId()),
					authTokenForSetupService.getAccess_token());
		}

		// REFERRAL_ID
		if (updateClientGeneral.getReferralId() != null
				&& updateClientGeneral.getReferralId() != dbClientGeneral.getReferralId()) {
			log.info("Inserting Audit log for REFERRAL_ID");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"REFERRAL_ID", String.valueOf(dbClientGeneral.getReferralId()),
					String.valueOf(updateClientGeneral.getReferralId()), authTokenForSetupService.getAccess_token());
		}

		// ADDRESS_LINE1
		if (updateClientGeneral.getAddressLine1() != null
				&& !updateClientGeneral.getAddressLine1().equalsIgnoreCase(dbClientGeneral.getAddressLine1())) {
			log.info("Inserting Audit log for ADDRESS_LINE1");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"ADDRESS_LINE1", String.valueOf(dbClientGeneral.getAddressLine1()),
					String.valueOf(updateClientGeneral.getAddressLine1()), authTokenForSetupService.getAccess_token());
		}

		// SUITE_NO
		if (updateClientGeneral.getSuiteDoorNo() != null
				&& !updateClientGeneral.getSuiteDoorNo().equalsIgnoreCase(dbClientGeneral.getSuiteDoorNo())) {
			log.info("Inserting Audit log for SUITE_NO");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"SUITE_NO", String.valueOf(dbClientGeneral.getSuiteDoorNo()),
					String.valueOf(updateClientGeneral.getSuiteDoorNo()), authTokenForSetupService.getAccess_token());
		}

		// CITY
		if (updateClientGeneral.getCity() != null
				&& !updateClientGeneral.getCity().equalsIgnoreCase(dbClientGeneral.getCity())) {
			log.info("Inserting Audit log for CITY");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL, "CITY",
					String.valueOf(dbClientGeneral.getCity()), String.valueOf(updateClientGeneral.getCity()),
					authTokenForSetupService.getAccess_token());
		}

		// STATE
		if (updateClientGeneral.getState() != null
				&& !updateClientGeneral.getState().equalsIgnoreCase(dbClientGeneral.getState())) {
			log.info("Inserting Audit log for STATE");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL, "STATE",
					String.valueOf(dbClientGeneral.getState()), String.valueOf(updateClientGeneral.getState()),
					authTokenForSetupService.getAccess_token());
		}

		// COUNTRY
		if (updateClientGeneral.getCountry() != null
				&& !updateClientGeneral.getCountry().equalsIgnoreCase(dbClientGeneral.getCountry())) {
			log.info("Inserting Audit log for COUNTRY");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"COUNTRY", String.valueOf(dbClientGeneral.getCountry()),
					String.valueOf(updateClientGeneral.getCountry()), authTokenForSetupService.getAccess_token());
		}

		// ZIP_CODE
		if (updateClientGeneral.getZipCode() != null
				&& !updateClientGeneral.getZipCode().equalsIgnoreCase(dbClientGeneral.getZipCode())) {
			log.info("Inserting Audit log for ZIP_CODE");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"ZIP_CODE", String.valueOf(dbClientGeneral.getZipCode()),
					String.valueOf(updateClientGeneral.getZipCode()), authTokenForSetupService.getAccess_token());
		}

		// MAIL_ADDRESS
		if (updateClientGeneral.getMailingAddress() != null
				&& !updateClientGeneral.getMailingAddress().equalsIgnoreCase(dbClientGeneral.getMailingAddress())) {
			log.info("Inserting Audit log for MAIL_ADDRESS");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"MAIL_ADDRESS", String.valueOf(dbClientGeneral.getMailingAddress()),
					String.valueOf(updateClientGeneral.getMailingAddress()),
					authTokenForSetupService.getAccess_token());
		}

		// CONT_NO
		if (updateClientGeneral.getContactNumber() != null
				&& !updateClientGeneral.getContactNumber().equalsIgnoreCase(dbClientGeneral.getContactNumber())) {
			log.info("Inserting Audit log for CONT_NO");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"CONT_NO", String.valueOf(dbClientGeneral.getContactNumber()),
					String.valueOf(updateClientGeneral.getContactNumber()), authTokenForSetupService.getAccess_token());
		}

		// WORK
		if (updateClientGeneral.getWorkNo() != null
				&& !updateClientGeneral.getWorkNo().equalsIgnoreCase(dbClientGeneral.getWorkNo())) {
			log.info("Inserting Audit log for WORK");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL, "WORK",
					String.valueOf(dbClientGeneral.getWorkNo()), String.valueOf(updateClientGeneral.getWorkNo()),
					authTokenForSetupService.getAccess_token());
		}

		// HOME
		if (updateClientGeneral.getHomeNo() != null
				&& !updateClientGeneral.getHomeNo().equalsIgnoreCase(dbClientGeneral.getHomeNo())) {
			log.info("Inserting Audit log for HOME");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL, "HOME",
					String.valueOf(dbClientGeneral.getHomeNo()), String.valueOf(updateClientGeneral.getHomeNo()),
					authTokenForSetupService.getAccess_token());
		}

		// FAX
		if (updateClientGeneral.getFax() != null
				&& !updateClientGeneral.getFax().equalsIgnoreCase(dbClientGeneral.getFax())) {
			log.info("Inserting Audit log for FAX");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL, "FAX",
					String.valueOf(dbClientGeneral.getFax()), String.valueOf(updateClientGeneral.getFax()),
					authTokenForSetupService.getAccess_token());
		}

		// EMail_ID
		if (updateClientGeneral.getEmailId() != null
				&& !updateClientGeneral.getEmailId().equalsIgnoreCase(dbClientGeneral.getEmailId())) {
			log.info("Inserting Audit log for EMail_ID");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"EMail_ID", String.valueOf(dbClientGeneral.getEmailId()),
					String.valueOf(updateClientGeneral.getEmailId()), authTokenForSetupService.getAccess_token());
		}

		// ALT_EMAIL_ID
		if (updateClientGeneral.getAlternateEmailId() != null
				&& !updateClientGeneral.getAlternateEmailId().equalsIgnoreCase(dbClientGeneral.getAlternateEmailId())) {
			log.info("Inserting Audit log for ALT_EMAIL_ID");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"ALT_EMAIL_ID", String.valueOf(dbClientGeneral.getAlternateEmailId()),
					String.valueOf(updateClientGeneral.getAlternateEmailId()),
					authTokenForSetupService.getAccess_token());
		}

		// CONSULT_DATE
		if (updateClientGeneral.getConsultationDate() != null
				&& !updateClientGeneral.getConsultationDate().equalsIgnoreCase(dbClientGeneral.getConsultationDate())) {
			log.info("Inserting Audit log for CONSULT_DATE");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"CONSULT_DATE", String.valueOf(dbClientGeneral.getConsultationDate()),
					String.valueOf(updateClientGeneral.getConsultationDate()),
					authTokenForSetupService.getAccess_token());
		}

		// SSN_ID
		if (updateClientGeneral.getSocialSecurityNo() != null
				&& !updateClientGeneral.getSocialSecurityNo().equalsIgnoreCase(dbClientGeneral.getSocialSecurityNo())) {
			log.info("Inserting Audit log for SSN_ID");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL, "SSN_ID",
					String.valueOf(dbClientGeneral.getSocialSecurityNo()),
					String.valueOf(updateClientGeneral.getSocialSecurityNo()),
					authTokenForSetupService.getAccess_token());
		}

		// OCCUPATION
		if (updateClientGeneral.getOccupation() != null
				&& !updateClientGeneral.getOccupation().equalsIgnoreCase(dbClientGeneral.getOccupation())) {
			log.info("Inserting Audit log for OCCUPATION");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(clientGeneralId), 3L, CLIENTGENERAL,
					"OCCUPATION", String.valueOf(dbClientGeneral.getOccupation()),
					String.valueOf(updateClientGeneral.getOccupation()), authTokenForSetupService.getAccess_token());
		}

		// IT_FORM_NO
		dbClientGeneral.setIntakeFormNumber(updateClientGeneral.getIntakeFormNumber());
		dbClientGeneral.setCorporationClientId(updateClientGeneral.getCorporationClientId());

		BeanUtils.copyProperties(updateClientGeneral, dbClientGeneral,
				CommonUtils.getNullPropertyNames(updateClientGeneral));

		log.info("Before modified : " + dbClientGeneral);
		if (updateClientGeneral.getStatusId() != null) {
			dbClientGeneral.setStatusId(updateClientGeneral.getStatusId());
		}

		log.info("updateClientGeneral.getSentToQB() : " + updateClientGeneral.getSentToQB());
		dbClientGeneral.setSentToQB(updateClientGeneral.getSentToQB());
		if (!loginUserID.equalsIgnoreCase("QBUpdate")) {
			dbClientGeneral.setUpdatedBy(loginUserID);
		}
		
		dbClientGeneral.setUpdatedOn(new Date());

		//Closed Date Update
		if (updateClientGeneral.getReferenceField4() == null) {
			dbClientGeneral.setReferenceField4(null);
		}

		//address section
		dbClientGeneral.setReferenceField15(updateClientGeneral.getReferenceField15());
		dbClientGeneral.setReferenceField16(updateClientGeneral.getReferenceField16());
		dbClientGeneral.setReferenceField17(updateClientGeneral.getReferenceField17());
		dbClientGeneral.setReferenceField18(updateClientGeneral.getReferenceField18());
		dbClientGeneral.setReferenceField19(updateClientGeneral.getReferenceField19());
		dbClientGeneral.setReferenceField20(updateClientGeneral.getReferenceField20());

		log.info("After modified : " + dbClientGeneral);

		ClientGeneral updatedClientGeneral = clientGeneralRepository.save(dbClientGeneral);

		// ----------------------Docketwise
		// Integration------------------------------------------------------------
		log.info("--------clientGeneral.getClassId()----------> : " + updatedClientGeneral.getClassId());
		if (updatedClientGeneral.getClassId().longValue() == 2 && updatedClientGeneral.getClientId() != null) {
			try {
				Contact docketwiseContact = populateDocketwiseContact(updatedClientGeneral);
				log.info("docketwiseContact : " + docketwiseContact);
				UpdateContact updateContact = new UpdateContact();
				updateContact.setContact(docketwiseContact);

				// Get AuthToken for CommonService
				AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
				Contact updatedContact = commonService.updateContact(updatedClientGeneral.getReferenceField9(),
						updateContact, authTokenForCommonService.getAccess_token());
				log.info("updatedContact : " + updatedContact);
			} catch (Exception e) {
				log.info("Docketwise Error:" + e.getLocalizedMessage());
			}
		}
		return updatedClientGeneral;
	}

	/**
	 * findClientGenerals
	 * 
	 * @param searchClientGeneral
	 * @return
	 * @throws ParseException
	 */
	public List<ClientGeneral> findClientGenerals(SearchClientGeneral searchClientGeneral) throws ParseException {
		if (searchClientGeneral.getStartCreatedOn() != null && searchClientGeneral.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchClientGeneral.getStartCreatedOn(),
					searchClientGeneral.getEndCreatedOn());
			searchClientGeneral.setStartCreatedOn(dates[0]);
			searchClientGeneral.setEndCreatedOn(dates[1]);
		}

		ClientGeneralSpecification spec = new ClientGeneralSpecification(searchClientGeneral);
		List<ClientGeneral> clientGeneralList = clientGeneralRepository.findAll(spec);
		/*for (ClientGeneral clientGeneral: clientGeneralList) {
			// Class
			clientGeneral.setReferenceField29(clientGeneralRepository.getClassName(clientGeneral.getClientId()));
			
			// Status
			clientGeneral.setReferenceField30(clientGeneralRepository.getStatusId(clientGeneral.getClientId()));
		}*/
		return clientGeneralList;
	}

	/**
	 * findClientGeneralNew
	 *
	 * @param searchClientGeneral
	 * @return
	 * @throws ParseException
	 */
	public List<IClientGeneralNew> findClientGeneralNew(SearchClientGeneral searchClientGeneral) throws ParseException {
		if (searchClientGeneral.getStartCreatedOn() != null && searchClientGeneral.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchClientGeneral.getStartCreatedOn(),
					searchClientGeneral.getEndCreatedOn());
			searchClientGeneral.setStartCreatedOn(dates[0]);
			searchClientGeneral.setEndCreatedOn(dates[1]);
		}

		List<IClientGeneralNew> clientGeneralList = clientGeneralRepository.getClientGeneralList(
														searchClientGeneral.getClientId(),
														searchClientGeneral.getIntakeFormNumber(),
														searchClientGeneral.getStatusId(),
														searchClientGeneral.getClassId(),
														searchClientGeneral.getFirstNameLastName(),
														searchClientGeneral.getEmailId(),
														searchClientGeneral.getContactNumber(),
														searchClientGeneral.getAddressLine1(),
														searchClientGeneral.getStartCreatedOn(),
														searchClientGeneral.getEndCreatedOn());
			return clientGeneralList;
	}

	/**
	 * 
	 * @param loginUserID
	 * @return
	 */
	private Long getUserProfileClassId(String loginUserID) {
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		UserProfile userProfile = setupService.getUserProfile(loginUserID, authTokenForSetupService.getAccess_token());
		log.info("userProfile : " + userProfile);
		return userProfile.getClassId();
	}

	/**
	 * getDaashboardTotal
	 * 
	 * Fetch CLASS_ID values from USERPROFILE by passing Logged in USR_ID then pass
	 * the fetched CLASS_ID value in CLIENTGENERAL table and fetch all CLIENT_ID
	 * values and display the count and values in the dashboard
	 * 
	 * @param loginUserID
	 * @return
	 */
	public DashboardReport getDashboardTotal(String loginUserID) {
		DashboardReport dashboardReport = new DashboardReport();
		Long classId = getUserProfileClassId(loginUserID);

		// If CLASS_ID = 03 for the passed USR_ID, fetch the count of CLIENT_ID values
		// from CLIENTGENERAL table
		if (classId != null && classId.longValue() == 3) {
			List<ClientGeneral> clientGeneralList = clientGeneralRepository.findAllByDeletionIndicator(0L);
			dashboardReport.setCount(clientGeneralList.size());
			return dashboardReport;
		}

		List<ClientGeneral> clientGeneralList = clientGeneralRepository.findByClassIdAndDeletionIndicator(classId, 0L);
		List<String> clientIdList = clientGeneralList.stream().map(c -> c.getClientId()).collect(Collectors.toList());
		dashboardReport.setListOfValues(clientIdList);
		dashboardReport.setCount(clientIdList.size());
		return dashboardReport;
	}

	/**
	 * getDashboardActive
	 * 
	 * 1. ISDELETED value = 0, 2. STATUS_ID = 18 3. Fetch CLASS_ID values from
	 * USERPROFILE by passing Logged in USR_ID then pass the fetched CLASS_ID value
	 * in CLIENTGENERAL table and fetch all CLIENT_ID values and display the count
	 * and values in the dash board
	 * 
	 * @param loginUserID
	 * @return
	 */
	public DashboardReport getDashboardActive(String loginUserID) {
		DashboardReport dashboardReport = new DashboardReport();
		Long classId = getUserProfileClassId(loginUserID);

		// If CLASS_ID = 03 for the passed USR_ID, fetch the count of CLIENT_ID values
		// from CLIENTGENERAL table
		if (classId != null && classId.longValue() == 3) {
			List<ClientGeneral> clientGeneralList = clientGeneralRepository.findAllByStatusIdAndDeletionIndicator(18L,
					0L);
			List<String> clientIdList = clientGeneralList.stream().map(c -> c.getClientId())
					.collect(Collectors.toList());
			dashboardReport.setListOfValues(clientIdList);
			dashboardReport.setCount(clientGeneralList.size());
			return dashboardReport;
		}

		List<ClientGeneral> clientGeneralList = clientGeneralRepository
				.findByClassIdAndDeletionIndicatorAndStatusId(classId, 0L, 18L);
		List<String> clientIdList = clientGeneralList.stream().map(c -> c.getClientId()).collect(Collectors.toList());
		dashboardReport.setListOfValues(clientIdList);
		dashboardReport.setCount(clientIdList.size());
		return dashboardReport;
	}

	/**
	 * 
	 * @param loginUserID
	 * @return
	 */
	public DashboardReport getDashboardRecentClients(String loginUserID) {
		DashboardReport dashboardReport = new DashboardReport();
		String[] currentMonthFirstAndLastDate = DateUtils.getCurrentMonthFirstAndLastDates();
		Long classId = getUserProfileClassId(loginUserID);

		// If CLASS_ID = 03 for the passed USR_ID, fetch the count of CLIENT_ID values
		// from CLIENTGENERAL table
		if (classId != null && classId.longValue() == 3) {
			List<ClientGeneral> clientGeneralList = clientGeneralRepository
					.findByCreatedOnWithStatusId(currentMonthFirstAndLastDate[0], currentMonthFirstAndLastDate[1], 18L);
			List<String> clientIdList = clientGeneralList.stream().map(c -> c.getClientId())
					.collect(Collectors.toList());
			dashboardReport.setListOfValues(clientIdList);
			dashboardReport.setCount(clientGeneralList.size());
			return dashboardReport;
		}

		List<ClientGeneral> clientGeneralList = clientGeneralRepository.findByCreatedOn(classId,
				currentMonthFirstAndLastDate[0], currentMonthFirstAndLastDate[1], 18L);

		List<String> clientIdList = clientGeneralList.stream().map(c -> c.getClientId()).collect(Collectors.toList());
		dashboardReport.setListOfValues(clientIdList);
		dashboardReport.setCount(clientIdList.size());
		return dashboardReport;
	}

	/**
	 * getDocketwiseContacts
	 * 
	 * @return
	 */
	public List<Contact> getDocketwiseContacts() {
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		Contact[] contacts = commonService.getDocketwiseContacts(authTokenForCommonService.getAccess_token());
		return Arrays.asList(contacts);
	}

	/**
	 * 
	 * @return
	 */
	public List<Matter> getDocketwiseMatters() {
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		Matter[] matters = commonService.getDocketwiseMatters(authTokenForCommonService.getAccess_token());
		return Arrays.asList(matters);
	}

	/**
	 * populateDocketwiseContact
	 * 
	 * @param clientGeneral
	 * @return
	 */
	private Contact populateDocketwiseContact(ClientGeneral clientGeneral) {
		// Docketwise Mapping - Contact
		Contact docketwiseContact = new Contact();
		log.info("clientGeneral.getReferenceField9() : " + clientGeneral.getReferenceField9());

		if (clientGeneral.getReferenceField9() != null) {
			docketwiseContact.setId(Long.valueOf(clientGeneral.getReferenceField9()));
			docketwiseContact.setUpdated_at(new Date());
		}

		// First_Name
		docketwiseContact.setFirst_name(clientGeneral.getFirstName());

		// Last_Name
		docketwiseContact.setLast_name(clientGeneral.getLastName());

		// Middle_name
		docketwiseContact.setMiddle_name(clientGeneral.getReferenceField1());

		// Email
		docketwiseContact.setEmail(clientGeneral.getEmailId());

		// Type
		/*
		 * 1. If CLIENT_CATEGORY_ID=1, pass the value as Institution 2. If
		 * CLIENT_CATEGORY_ID=2, pass the value as Person 3. If CLIENT_CATEGORY_ID=3,
		 * pass the value as Person
		 */
		String type = "";
		long clientCategoryId = 0;
		if (clientGeneral.getClientCategoryId() != null) {
			clientCategoryId = clientGeneral.getClientCategoryId().longValue();
		}

		if (clientCategoryId == 1) {
			type = "Institution";
			docketwiseContact.setCompany_name(clientGeneral.getFirstNameLastName());
		} else if (clientCategoryId == 2 || clientCategoryId == 3) {
			type = "Person";
		}
		docketwiseContact.setType(type);
		docketwiseContact.setLead(false);

		Data data = new Data();

		// Street_number_and_name
		data.setStreet_number_and_name(clientGeneral.getAddressLine1());

		// Apartment_number
		data.setApartment_number(clientGeneral.getSuiteDoorNo());

		// City
		data.setCity(clientGeneral.getCity());

		// State
		data.setState(clientGeneral.getState());

		// Zip_code
		data.setZip_code(clientGeneral.getZipCode());

		// Physical
		data.setPhysical(true);

		AddressAttribute addressAttribute = new AddressAttribute();
		addressAttribute.setData(data);

		List<AddressAttribute> addresses_attributes = new ArrayList<>();
		addresses_attributes.add(addressAttribute);
		docketwiseContact.setAddresses_attributes(addresses_attributes);

		List<PhoneNumberAttribute> phone_numbers_attributes = new ArrayList<>();
		PhoneNumberAttribute phoneNumberAttribute = new PhoneNumberAttribute();
		Data phontData = new Data();

		// CONT_NO
		phontData.setNumber(clientGeneral.getContactNumber());
		phoneNumberAttribute.setData(phontData);
		phone_numbers_attributes.add(phoneNumberAttribute);
		docketwiseContact.setPhone_numbers_attributes(phone_numbers_attributes);

		return docketwiseContact;
	}

	/**
	 * 
	 * @param newClientGenerals
	 * @param loginUserID
	 */
	public void createBulkClientGenerals(AddClientGeneral[] newClientGenerals, String loginUserID) {
		List<ClientGeneral> clientGenerals = new ArrayList<ClientGeneral>();
		for (AddClientGeneral addClientGeneral : newClientGenerals) {
			log.info("addClientGeneral : " + addClientGeneral);
			ClientGeneral clientGeneral = new ClientGeneral();
			BeanUtils.copyProperties(addClientGeneral, clientGeneral,
					CommonUtils.getNullPropertyNames(addClientGeneral));
			clientGeneral.setDeletionIndicator(0L);

			// Removing trailing spaces
			if (clientGeneral.getEmailId() != null) {
				clientGeneral.setEmailId(clientGeneral.getEmailId().trim());
			}

			if (clientGeneral.getContactNumber() != null) {
				clientGeneral.setContactNumber(clientGeneral.getContactNumber().trim());
			}

			if (clientGeneral.getAddressLine1() != null) {
				clientGeneral.setAddressLine1(clientGeneral.getAddressLine1().trim());
			}

			if (clientGeneral.getAddressLine2() != null) {
				clientGeneral.setAddressLine2(clientGeneral.getAddressLine2().trim());
			}

			if (clientGeneral.getAddressLine3() != null) {
				clientGeneral.setAddressLine3(clientGeneral.getAddressLine3().trim());
			}

			clientGeneral.setClientCategoryId(addClientGeneral.getClientCategoryId());

			Date ctd_date = DateUtils.convertStringToDate(addClientGeneral.getCreatedOn());
			Date utd_date = DateUtils.convertStringToDate(addClientGeneral.getUpdatedOn());

			clientGeneral.setCreatedOn(ctd_date);
			clientGeneral.setUpdatedOn(utd_date);
			clientGenerals.add(clientGeneral);
		}
		clientGeneralRepository.saveAll(clientGenerals);
		log.info("ClientGeneral created...");

		// -------------------- Docketwise Integration-----------------------------------------------
		List<ClientGeneral> clientGeneralDocketwise = new ArrayList<>();
		for (ClientGeneral clientGeneral : clientGenerals) {
			log.info("--------clientGeneral.getClassId()----------> : " + clientGeneral.getClassId());
			if (clientGeneral.getClassId().longValue() == 2 && clientGeneral.getClientId() != null) {
				Contact docketwiseContact = populateDocketwiseContact(clientGeneral);
				CreateContact createContact = new CreateContact();
				createContact.setContact(docketwiseContact);

				// Get AuthToken for CommonService
				AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
				Contact createdContact = commonService.createDocketwiseContact(createContact,
						authTokenForCommonService.getAccess_token());
				log.info("createdContact in Docketwise::: " + createdContact);

				// Updating ClientGeneral with new created ID
				ClientGeneral updateClientGeneral = getClientGeneral(clientGeneral.getClientId());
				updateClientGeneral.setReferenceField9(String.valueOf(createdContact.getId()));
				updateClientGeneral.setUpdatedBy(loginUserID);
				updateClientGeneral.setUpdatedOn(new Date());
				clientGeneral = clientGeneralRepository.save(updateClientGeneral);
				log.info("clientGeneral updated::: " + clientGeneral);
				clientGeneralDocketwise.add(clientGeneral);
			}
		}
		log.info("Batch upload is done");
	}
	
	//------------------------------------Docketwise Syncup--------------------------------------------
	/**
	 * Client General Syncup
	 */
	public void	syncupClientWithDocket () {
		try {
			log.info ("Date : " + new Date());
			ClientGeneral clientGeneral = 
					clientGeneralRepository.findTopByClassIdAndSentToDocketwiseAndDeletionIndicatorOrderByCreatedOn (2L, 0L, 0L);
			if (clientGeneral != null && clientGeneral.getReferenceField9() != null) {
				log.info("clientGeneral is null::: " + clientGeneral);
				
				// Updating ClientGeneral with new created ID
				clientGeneral.setSentToDocketwise(1L);
				clientGeneral = clientGeneralRepository.save(clientGeneral);
				log.info("SentToDocketwise updated::: " + clientGeneral.getClientId());
			}
			
			if (clientGeneral != null && clientGeneral.getReferenceField9() == null) {
				log.info("clientGeneral is not null::: " + clientGeneral.getClientId());
				
				Contact docketwiseContact = populateDocketwiseContact(clientGeneral);
				CreateContact createContact = new CreateContact();
				createContact.setContact(docketwiseContact);

				// Get AuthToken for CommonService
				AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
				Contact createdContact = commonService.createDocketwiseContact(createContact,
						authTokenForCommonService.getAccess_token());
				log.info("createdContact in Docketwise::: " + createdContact.getId());

				if (createdContact.getId() != null) {
					// Updating ClientGeneral with new created ID
					ClientGeneral updateClientGeneral = getClientGeneral(clientGeneral.getClientId());
					updateClientGeneral.setReferenceField9(String.valueOf(createdContact.getId()));
					updateClientGeneral.setSentToDocketwise(1L);
					clientGeneral = clientGeneralRepository.save(updateClientGeneral);
					log.info("clientGeneral updated::: " + clientGeneral.getClientId());
				}
			}
		} catch (Exception e) {
			log.error("Error in Docketwise: " + e.toString());
		}
	}
	
	// ------------------------------------Report------------------------------------------------------

	/**
	 * getLNEClientGeneralReport
	 * 
	 * @param searchClientGeneralReport
	 * @return
	 * @throws ParseException
	 */
	public List<ClientGeneralLNEReport> getLNEClientGeneralReport(
			SearchClientGeneralLNEReport searchClientGeneralReport) throws ParseException {
		if (searchClientGeneralReport.getFromCreatedOn() != null
				&& searchClientGeneralReport.getToCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchClientGeneralReport.getFromCreatedOn(),
					searchClientGeneralReport.getToCreatedOn());
			searchClientGeneralReport.setFromCreatedOn(dates[0]);
			searchClientGeneralReport.setToCreatedOn(dates[1]);
		}

		if (searchClientGeneralReport.getFromDateClosed() != null
				&& searchClientGeneralReport.getToDateClosed() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchClientGeneralReport.getFromDateClosed(),
					searchClientGeneralReport.getToDateClosed());
			searchClientGeneralReport.setFromDateClosed(dates[0]);
			searchClientGeneralReport.setToDateClosed(dates[1]);
		}

		ClientGeneralLNEReportSpecification spec = new ClientGeneralLNEReportSpecification(searchClientGeneralReport);
		List<ClientGeneral> clientGeneralSearchResults = clientGeneralRepository.findAll(spec);
		log.info("clientGeneralSearchResults: " + clientGeneralSearchResults);

		AuthToken authToken = authTokenService.getSetupServiceAuthToken();
		AuthToken crmAuthToken = authTokenService.getCrmServiceAuthToken();
		List<ClientGeneralLNEReport> clientGeneralReportList = new ArrayList<>();
		for (ClientGeneral clientGeneral : clientGeneralSearchResults) {
			ClientGeneralLNEReport clientGeneralReport = new ClientGeneralLNEReport();
			BeanUtils.copyProperties(clientGeneral, clientGeneralReport,
					CommonUtils.getNullPropertyNames(clientGeneral));

			// Corporate Client Name
			clientGeneralReport.setCorporationClientId(clientGeneral.getFirstNameLastName());

			// CLIENT_CAT_ID
			try{
				ClientCategory clientCategory = setupService.getClientCategory(clientGeneral.getClientCategoryId(),
						authToken.getAccess_token());
				if (clientCategory != null) {
					clientGeneralReport.setClientCategoryDesc(clientCategory.getClientCategoryDescription());
				}
			} catch (Exception e){
				log.error("Error in get data for clientCategory from setup Service", clientGeneral.getClientCategoryId());
			}

			// Consulted by
			try{
				PotentialClient potentialClient = crmService.getPotentialClient(clientGeneral.getPotentialClientId(),
						crmAuthToken.getAccess_token());
				if (potentialClient != null) {
					clientGeneralReport.setConsultedBy(potentialClient.getReferenceField4());
				}
			} catch (Exception e){
				log.error("Error in get data for potentialClient from CRM Service", clientGeneral.getPotentialClientId());
			}

			// Signed Agreement
			try{
				Agreement agreement = crmService.getAgreementByPotentialClientId(clientGeneral.getPotentialClientId(),
						crmAuthToken.getAccess_token());
				log.info("Agreement : " + agreement);
				if (agreement != null) {
					if (agreement.getReceivedOn() != null) {
						clientGeneralReport.setSignedAgreement("YES");
					} else {
						clientGeneralReport.setSignedAgreement("NO");
					}
				} else {
					clientGeneralReport.setSignedAgreement("NA");
				}
			} catch (Exception e){
				log.error("Error in get data for agreement from CRM Service", clientGeneral.getPotentialClientId());
			}

			clientGeneralReportList.add(clientGeneralReport);
		}
		return clientGeneralReportList;
	}

	/**
	 * 
	 * @param searchClientGeneralIMMReport
	 * @return
	 * @throws ParseException
	 */
	public List<ClientGeneralIMMReport> getIMMClientGeneralReport(
			SearchClientGeneralIMMReport searchClientGeneralIMMReport) throws ParseException {
		if (searchClientGeneralIMMReport.getFromCreatedOn() != null
				&& searchClientGeneralIMMReport.getToCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchClientGeneralIMMReport.getFromCreatedOn(),
					searchClientGeneralIMMReport.getToCreatedOn());
			searchClientGeneralIMMReport.setFromCreatedOn(dates[0]);
			searchClientGeneralIMMReport.setToCreatedOn(dates[1]);
		}

		if (searchClientGeneralIMMReport.getFromDateClosed() != null
				&& searchClientGeneralIMMReport.getToDateClosed() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchClientGeneralIMMReport.getFromDateClosed(),
					searchClientGeneralIMMReport.getToDateClosed());
			searchClientGeneralIMMReport.setFromDateClosed(dates[0]);
			searchClientGeneralIMMReport.setToDateClosed(dates[1]);
		}

		ClientGeneralIMMReportSpecification spec = new ClientGeneralIMMReportSpecification(
				searchClientGeneralIMMReport);
		List<ClientGeneral> clientGeneralSearchResults = clientGeneralRepository.findAll(spec);
		log.info("clientGeneralSearchResults: " + clientGeneralSearchResults);

		AuthToken authToken = authTokenService.getSetupServiceAuthToken();
		AuthToken crmAuthToken = authTokenService.getCrmServiceAuthToken();
		List<ClientGeneralIMMReport> clientGeneralReportList = new ArrayList<>();
		for (ClientGeneral clientGeneral : clientGeneralSearchResults) {
			ClientGeneralIMMReport clientGeneralReport = new ClientGeneralIMMReport();
			BeanUtils.copyProperties(clientGeneral, clientGeneralReport,
					CommonUtils.getNullPropertyNames(clientGeneral));

			// Corporate Client Name
			clientGeneralReport.setCorporationClientId(clientGeneral.getFirstNameLastName());

			// CLIENT_CAT_ID
			ClientCategory clientCategory = setupService.getClientCategory(clientGeneral.getClientCategoryId(),
					authToken.getAccess_token());
			clientGeneralReport.setClientCategoryDesc(clientCategory.getClientCategoryDescription());

			// Consulted by
			try{
				PotentialClient potentialClient = crmService.getPotentialClient(clientGeneral.getPotentialClientId(),
						crmAuthToken.getAccess_token());
				if(potentialClient != null){
					clientGeneralReport.setConsultedBy(potentialClient.getReferenceField4());
				}
			} catch (Exception e){
				log.error("Error in get data for potentialClient from CRM Service", clientGeneral.getPotentialClientId());
			}

			// Signed Agreement
			try{
				Agreement agreement = crmService.getAgreementByPotentialClientId(clientGeneral.getPotentialClientId(),
						crmAuthToken.getAccess_token());
				if(agreement != null){
					if (agreement.getReceivedOn() != null) {
						clientGeneralReport.setSignedAgreement("YES");
					} else {
						clientGeneralReport.setSignedAgreement("NO");
					}
				}
			} catch (Exception e){
				log.error("Error in get data for agreement from CRM Service", clientGeneral.getPotentialClientId());
			}

			// Docketwise ReferenceId
			clientGeneralReport.setDocketwiseReferenceId(clientGeneral.getReferenceField9());

			// Closed Date
			clientGeneralReport.setClosedDate(clientGeneral.getReferenceField4());

			//HAREESH - 26-08-2022 - for getting the name of the passed Id
			clientGeneralReport.setStatusIdDescription(clientGeneralRepository.getStatusName(clientGeneral.getStatusId()));
			clientGeneralReport.setReferralIdDescription(clientGeneralRepository.getReferralIdName(clientGeneral.getReferralId()));

			clientGeneralReportList.add(clientGeneralReport);
		}
		return clientGeneralReportList;
	}
}
