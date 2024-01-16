package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.crm.config.PropertiesConfig;
import com.mnrclara.api.crm.controller.exception.BadRequestException;
import com.mnrclara.api.crm.model.auth.AuthToken;
import com.mnrclara.api.crm.model.dto.Agreement;
import com.mnrclara.api.crm.model.dto.ClientUser;
import com.mnrclara.api.crm.model.dto.Dashboard;
import com.mnrclara.api.crm.model.dto.MailMerge;
import com.mnrclara.api.crm.model.dto.Referral;
import com.mnrclara.api.crm.model.dto.UserProfile;
import com.mnrclara.api.crm.model.inquiry.Inquiry;
import com.mnrclara.api.crm.model.itform.ClientContactNumberInfo;
import com.mnrclara.api.crm.model.itform.ClientReferenceMedium;
import com.mnrclara.api.crm.model.itform.ITForm001;
import com.mnrclara.api.crm.model.itform.ITForm002;
import com.mnrclara.api.crm.model.itform.ITForm003;
import com.mnrclara.api.crm.model.itform.ITForm004;
import com.mnrclara.api.crm.model.itform.ITForm005;
import com.mnrclara.api.crm.model.itform.ITForm006;
import com.mnrclara.api.crm.model.pcitform.PCIntakeForm;
import com.mnrclara.api.crm.model.potentialclient.AddClientGeneral;
import com.mnrclara.api.crm.model.potentialclient.ClientGeneral;
import com.mnrclara.api.crm.model.potentialclient.PotentialClient;
import com.mnrclara.api.crm.model.potentialclient.PotentialClientReport;
import com.mnrclara.api.crm.model.potentialclient.ReferralReport;
import com.mnrclara.api.crm.model.potentialclient.SearchPotentialClient;
import com.mnrclara.api.crm.model.potentialclient.SearchPotentialClientReport;
import com.mnrclara.api.crm.model.potentialclient.SearchReferralReport;
import com.mnrclara.api.crm.model.potentialclient.UpdatePotentialClient;
import com.mnrclara.api.crm.model.potentialclient.UpdatePotentialClientAgreement;
import com.mnrclara.api.crm.repository.ClientGeneralRepository;
import com.mnrclara.api.crm.repository.PotentialClientRepository;
import com.mnrclara.api.crm.repository.specification.PotentialClientReportSpecification;
import com.mnrclara.api.crm.repository.specification.PotentialClientSpecification;
import com.mnrclara.api.crm.repository.specification.ReferralReportSpecification;
import com.mnrclara.api.crm.util.CommonUtils;
import com.mnrclara.api.crm.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PotentialClientService {
	
	private static final String POTENTIALCLIENT = "POTENTIALCLIENT";

	private static final String A001 = "A001";
	private static final String A003 = "A003";
	private static final String A004 = "A004";
	private static final String A005 = "A005";
	private static final String A018 = "A018";

	@Autowired
	SetupService setupService;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	ManagementService managementService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	InquiryService inquiryService;
	
	@Autowired
	NotesService notesService;
	
	@Autowired
	ITForm001Service itFormm001Service;
	
	@Autowired
	ITForm002Service itFormm002Service;
	
	@Autowired
	ITForm003Service itFormm003Service;
	
	@Autowired
	ITForm004Service itFormm004Service;
	
	@Autowired
	ITForm005Service itFormm005Service;
	
	@Autowired
	ITForm006Service itFormm006Service;
	
	@Autowired
	MailMergeService mailMergeService;
	
	@Autowired
	PCIntakeFormService pcIntakeFormService;
	
	@Autowired
	PotentialClientRepository potentialClientRepository;
	
	@Autowired
	ClientGeneralRepository clientGeneralRepository;
	
	@Autowired
	PropertiesConfig propertiesConfig;
	
	/**
	 * getPotentialClients
	 * @return
	 */
	public List<PotentialClient> getPotentialClients () {
		List<PotentialClient> potentialClients = potentialClientRepository.findAll();
		potentialClients = potentialClients.stream().filter(a -> a.getDeletionIndicator() != 1).collect(Collectors.toList());
		return potentialClients;
	}
	
	/**
	 * getPotentialClient
	 * @param potentialClientModuleCode
	 * @return
	 */
	public PotentialClient getPotentialClient (String potentialClientCode) {
		PotentialClient potentialClient = potentialClientRepository.findByPotentialClientId(potentialClientCode).orElse(null);
		if (potentialClient != null && potentialClient.getDeletionIndicator() != 1) {
			return potentialClient;
		}
		return null; // Record got deleted
	}
	
	/**
	 * 
	 * @param inquiryNumber
	 * @return
	 */
	public List<PotentialClient> getPotentialClientByInquiryNumber (String inquiryNumber) {
		List<PotentialClient> potentialClient =
				potentialClientRepository.findByInquiryNumberAndDeletionIndicator(inquiryNumber, 0L);
		return potentialClient;
	}
	
	/**
	 * STATUS_ID=11,12,13,14
	 * @param classId
	 * @return
	 */
	public Dashboard getPotentialClientCount (Long classId) {
		log.info("classId : " + classId);
		Dashboard dashboard = new Dashboard();
		if (classId == 1 || classId == 2) {
			List<PotentialClient> potentialClientList = potentialClientRepository.findByClassId(classId);
			log.info("filtered potentialClientList : " + potentialClientList);
			Long count = potentialClientList.stream().filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0 
					&& (i.getStatusId() == 11 || i.getStatusId() == 12 || i.getStatusId() == 13 || i.getStatusId() == 14)
					).count();
			dashboard.setFiteredCount(count);
			dashboard.setTotalCount(Long.valueOf(potentialClientList.size()));
			return dashboard;
		}
		
		if (classId == 3) { // return all records count
			List<PotentialClient> potentialClientList = potentialClientRepository.findAll();
			log.info("All potentialClientList : " + potentialClientList);
			Long totCount = potentialClientList.stream().filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0).count();
			Long count = potentialClientList.stream().filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0 
					&& (i.getStatusId() == 11 || i.getStatusId() == 12 || i.getStatusId() == 13 || i.getStatusId() == 14)
					).count();
			dashboard.setFiteredCount(count);
			dashboard.setTotalCount(totCount);
			return dashboard;
		}
		return dashboard;
	}
	
	/**
	 * 
	 * @param loginUserId
	 * @return
	 */
	public Dashboard getPotentialClientCount(String loginUserId) {
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		UserProfile userProfile = setupService.getUserProfile(loginUserId, authTokenForSetupService.getAccess_token());
		return getPotentialClientCount(userProfile.getClassId());
	}
	
	/**
	 * createPotentialClient
	 * @param intakeNotesNumber 
	 * @param string2 
	 * @param string 
	 * @param newPotentialClient
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PotentialClient createPotentialClient (String loginUserId, 
			String intakeFormNumber, String inquiryNumber, String intakeNotesNumber, Date refField1, String refField2) 
			throws IllegalAccessException, InvocationTargetException {
		PotentialClient dbPotentialClient = new PotentialClient();
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		
		// LANG_ID
		PCIntakeForm pcIntakeForm = pcIntakeFormService.getPCIntakeForm(intakeFormNumber, inquiryNumber);
		dbPotentialClient.setLanguageId(pcIntakeForm.getLanguageId());
		
		// CLASS_ID
		dbPotentialClient.setClassId(pcIntakeForm.getClassId());
		
		// POT_CLIENT_ID
		/*
		 *  During Save, Pass CLASS_ID=03, NUM_RAN_CODE=04 in NUMBERRANGE table and 
		 *  Fetch NUM_RAN_CURRENT values and add +1 and then insert
		 */
		Long classID = 3L;
		Long numberRangeCode = 4L;
		String newPotentialClientId = setupService.getNextNumberRange(classID, numberRangeCode, authTokenForSetupService.getAccess_token());
		log.info("nextVal from NumberRange : " + newPotentialClientId);
		dbPotentialClient.setPotentialClientId(newPotentialClientId);
		
		// INQ_NO
		dbPotentialClient.setInquiryNumber(inquiryNumber);
		
		// IT_FORM_NO
		dbPotentialClient.setIntakeFormNumber(intakeFormNumber);
		
		// IT_FORM_ID
		dbPotentialClient.setIntakeFormId(pcIntakeForm.getIntakeFormId());
		
		// FIRST_NM - Pass INQ_NO in INQUIRY table and fetch FIRST_NM and insert
		Inquiry inquiry = inquiryService.getInquiry(inquiryNumber);
		dbPotentialClient.setFirstName(inquiry.getFirstName());
		
		// LAST_NM - Pass INQ_NO in INQUIRY table and fetch LAST_NM and insert
		dbPotentialClient.setLastName(inquiry.getLastName());
		
		// LAST_FIRST_NM - Concatenate LAST_NM and FIRST_NM
		dbPotentialClient.setLastNameFirstName(inquiry.getLastName() + " " + inquiry.getFirstName());

		// EMail_ID - Pass INQ_NO/IT_FORM_NO in PCINTAKEFORM table and fetch EMAIL_ID and insert
		dbPotentialClient.setEmailId(pcIntakeForm.getEmail());
		
		// CONT_NO - Pass INQ_NO in INQUIRY table and fetch CONT_NO and insert
		dbPotentialClient.setContactNumber(inquiry.getContactNumber());
		
		// Referral

		// CONSULT_DATE - Pass INQ_NO/IT_FORM_NO in PCINTAKEFORM table and fetch APPROVED_ON and insert
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
		String sConsultationDate = formatter.format(pcIntakeForm.getApprovedOn());
		dbPotentialClient.setConsultationDate(sConsultationDate);
		
		log.info("intakeNotesNumber----------> : " + intakeNotesNumber);
		dbPotentialClient.setPcNotesNumber(intakeNotesNumber);
		
		// STATUS_ID
		dbPotentialClient.setStatusId(11L); // Hard Coded Value "11"
		
		// TRANS_ID
		dbPotentialClient.setTransactionId(3L); // Hard Coded Value "03"
		
		// REF_FIELD_ID 1
		dbPotentialClient.setReferenceField1(refField1);
		
		//REF_FIELD_ID 2
		dbPotentialClient.setReferenceField2(refField2);
		
		//REF_FIELD_ID 4
		dbPotentialClient.setReferenceField4(inquiry.getAssignedUserId());

		// REF_FIELD_ID
		dbPotentialClient.setReferenceField10("POTENTIAL CLIENT CREATED");
		
		// CTD_BY
		dbPotentialClient.setCreatedBy(loginUserId);
		
		// CTD_ON
		dbPotentialClient.setCreatedOn(new Date());
		
		dbPotentialClient.setDeletionIndicator(0L);
		
		//-------------------Mongo values insertion----------------------------------
		
		if (dbPotentialClient.getIntakeFormId() == 1) {
			dbPotentialClient = fillOtherFieldsFrom001 (dbPotentialClient);
		}
		
		if (dbPotentialClient.getIntakeFormId() == 2) {
			dbPotentialClient = fillOtherFieldsFrom002 (dbPotentialClient);
		}
		
		if (dbPotentialClient.getIntakeFormId() == 3) {
			dbPotentialClient = fillOtherFieldsFrom003 (dbPotentialClient);
		}
		
		if (dbPotentialClient.getIntakeFormId() == 4) {
			dbPotentialClient = fillOtherFieldsFrom004 (dbPotentialClient);
		}
		
		if (dbPotentialClient.getIntakeFormId() == 5) {
			dbPotentialClient = fillOtherFieldsFrom005 (dbPotentialClient);
		}
		
		if (dbPotentialClient.getIntakeFormId() == 6) {
			dbPotentialClient = fillOtherFieldsFrom006 (dbPotentialClient);
		}
		
		return potentialClientRepository.save(dbPotentialClient);
	}
	
	/**
	 * 
	 * @param dbPotentialClient
	 * @return
	 */
	private PotentialClient fillOtherFieldsFrom001 (PotentialClient dbPotentialClient) {
		ITForm001 objITForm001 = itFormm001Service.getITForm001(dbPotentialClient.getInquiryNumber(), 
				dbPotentialClient.getClassId(), 
				dbPotentialClient.getLanguageId(), 
				dbPotentialClient.getIntakeFormNumber(), 
				dbPotentialClient.getIntakeFormId());
		if (objITForm001 != null) {
			// FIRST_LAST_NM
			dbPotentialClient.setFirstNameLastName(objITForm001.getName());
			
			// ADDRESS_LINE1
			dbPotentialClient.setAddressLine1(objITForm001.getAddress());
			
			// MAIL_ADDRESS
			dbPotentialClient.setEmailId(objITForm001.getEmailAddress());
			
			// ContactNumber
			/*
			 *  "cellNo" : "999-402-2998",
			 *  "homeNo" : "959-727-5656",
			 *  "workNo" : "999-496-9644"
			 */
			ClientContactNumberInfo contactNumberInfo = objITForm001.getContactNumber();
			dbPotentialClient.setContactNumber(contactNumberInfo.getCellNo());
			dbPotentialClient.setAlternateTelephone1(contactNumberInfo.getHomeNo());
			dbPotentialClient.setAlternateTelephone2(contactNumberInfo.getWorkNo());
			
			// NOTE_NO
//			dbPotentialClient.setPcNotesNumber(objITForm001.getNotes() + objITForm001.getBillingDepartmentNotes());
		}
		
		return dbPotentialClient;
	}
	
	/**
	 * 
	 * @param dbPotentialClient
	 * @return
	 */
	private PotentialClient fillOtherFieldsFrom002 (PotentialClient dbPotentialClient) {
		ITForm002 objITForm002 = itFormm002Service.getITForm002(dbPotentialClient.getInquiryNumber(), 
				dbPotentialClient.getClassId(), 
				dbPotentialClient.getLanguageId(), 
				dbPotentialClient.getIntakeFormNumber(), 
				dbPotentialClient.getIntakeFormId());
		if (objITForm002 != null) {
			// FIRST_LAST_NM
			dbPotentialClient.setFirstNameLastName(objITForm002.getName());
			
			// REFERRAL_ID
			ClientReferenceMedium medium = objITForm002.getReferenceMedium();
			List<String> selectedMediumList = medium.getListOfMediumAboutFirm();
			if (selectedMediumList != null) {
				String sMedium = selectedMediumList.get(0);
				dbPotentialClient.setReferralId(Long.valueOf(sMedium)); 
			}
			
			// ADDRESS_LINE1
			dbPotentialClient.setAddressLine1(objITForm002.getAddress());
			
			// MAIL_ADDRESS
			dbPotentialClient.setEmailId(objITForm002.getEmailAddress());
			
			// ContactNumber
			/*
			 *  "cellNo" : "999-402-2998",
			 *  "homeNo" : "959-727-5656",
			 *  "workNo" : "999-496-9644"
			 */
			ClientContactNumberInfo contactNumberInfo = objITForm002.getContactNumber();
			dbPotentialClient.setContactNumber(contactNumberInfo.getCellNo());
			dbPotentialClient.setAlternateTelephone1(contactNumberInfo.getHomeNo());
			dbPotentialClient.setAlternateTelephone2(contactNumberInfo.getWorkNo());

			dbPotentialClient.setCity(objITForm002.getClientGeneralInfo().getCity());
			dbPotentialClient.setState(objITForm002.getClientGeneralInfo().getState());
			dbPotentialClient.setZipCode(objITForm002.getClientGeneralInfo().getZipcode());
			
			// NOTE_NO
//			dbPotentialClient.setPcNotesNumber(objITForm002.getNotes());
		}
		return dbPotentialClient;
	}
	
	/**
	 * 
	 * @param dbPotentialClient
	 * @return
	 */
	private PotentialClient fillOtherFieldsFrom003 (PotentialClient dbPotentialClient) {
		ITForm003 objITForm003 = itFormm003Service.getITForm003(dbPotentialClient.getInquiryNumber(), 
				dbPotentialClient.getClassId(), 
				dbPotentialClient.getLanguageId(), 
				dbPotentialClient.getIntakeFormNumber(), 
				dbPotentialClient.getIntakeFormId());
		if (objITForm003 != null) {
			// FIRST_LAST_NM
			dbPotentialClient.setFirstNameLastName(objITForm003.getName());
			
			// REFERRAL_ID
			ClientReferenceMedium medium = objITForm003.getReferenceMedium();
			List<String> selectedMediumList = medium.getListOfMediumAboutFirm();
			if (selectedMediumList != null) {
				String sMedium = selectedMediumList.get(0);
				dbPotentialClient.setReferralId(Long.valueOf(sMedium)); 
			}
			
			// ADDRESS_LINE1
			dbPotentialClient.setAddressLine1(objITForm003.getAddress());
			
			// MAIL_ADDRESS
			dbPotentialClient.setEmailId(objITForm003.getEmailAddress());
			
			// ContactNumber
			/*
			 *  "cellNo" : "999-402-2998",
			 *  "homeNo" : "959-727-5656",
			 *  "workNo" : "999-496-9644"
			 */
			ClientContactNumberInfo contactNumberInfo = objITForm003.getContactNumber();
			dbPotentialClient.setContactNumber(contactNumberInfo.getCellNo());
			dbPotentialClient.setAlternateTelephone1(contactNumberInfo.getHomeNo());
			dbPotentialClient.setAlternateTelephone2(contactNumberInfo.getWorkNo());

			dbPotentialClient.setCity(objITForm003.getClientGeneralInfo().getCity());
			dbPotentialClient.setState(objITForm003.getClientGeneralInfo().getState());
			dbPotentialClient.setZipCode(objITForm003.getClientGeneralInfo().getZipcode());
			
			// NOTE_NO
//			dbPotentialClient.setPcNotesNumber(objITForm003.getNotes());
		}
		return dbPotentialClient;
	}
	
	/**
	 * 
	 * @param dbPotentialClient
	 * @return
	 */
	private PotentialClient fillOtherFieldsFrom004 (PotentialClient dbPotentialClient) {
		ITForm004 objITForm004 = itFormm004Service.getITForm004(dbPotentialClient.getInquiryNumber(), 
				dbPotentialClient.getClassId(), 
				dbPotentialClient.getLanguageId(), 
				dbPotentialClient.getIntakeFormNumber(), 
				dbPotentialClient.getIntakeFormId());
		if (objITForm004 != null) {
			// FIRST_LAST_NM
			dbPotentialClient.setFirstNameLastName(objITForm004.getName());
			
			// REFERRAL_ID
			ClientReferenceMedium medium = objITForm004.getReferenceMedium();
			List<String> selectedMediumList = medium.getListOfMediumAboutFirm();
			if (selectedMediumList != null) {
				String sMedium = selectedMediumList.get(0);
				dbPotentialClient.setReferralId(Long.valueOf(sMedium)); 
			}
			
			// ADDRESS_LINE1
			dbPotentialClient.setAddressLine1(objITForm004.getAddress());
			
			// MAIL_ADDRESS
			dbPotentialClient.setEmailId(objITForm004.getEmailAddress());
			
			// ContactNumber
			/*
			 *  "cellNo" : "999-402-2998",
			 *  "homeNo" : "959-727-5656",
			 *  "workNo" : "999-496-9644"
			 */
			ClientContactNumberInfo contactNumberInfo = objITForm004.getContactNumber();
			dbPotentialClient.setContactNumber(contactNumberInfo.getCellNo());
			dbPotentialClient.setAlternateTelephone1(contactNumberInfo.getHomeNo());
			dbPotentialClient.setAlternateTelephone2(contactNumberInfo.getWorkNo());

			dbPotentialClient.setCity(objITForm004.getClientGeneralInfo().getCity());
			dbPotentialClient.setState(objITForm004.getClientGeneralInfo().getState());
			dbPotentialClient.setZipCode(objITForm004.getClientGeneralInfo().getZipcode());
			
			// NOTE_NO
//			dbPotentialClient.setPcNotesNumber(objITForm004.getNotes());
		}
		return dbPotentialClient;
	}
	
	/**
	 * 
	 * @param dbPotentialClient
	 * @return
	 */
	private PotentialClient fillOtherFieldsFrom005 (PotentialClient dbPotentialClient) {
		ITForm005 objITForm005 = itFormm005Service.getITForm005(dbPotentialClient.getInquiryNumber(), 
				dbPotentialClient.getClassId(), 
				dbPotentialClient.getLanguageId(), 
				dbPotentialClient.getIntakeFormNumber(), 
				dbPotentialClient.getIntakeFormId());
		if (objITForm005 != null) {
			// FIRST_LAST_NM
			dbPotentialClient.setFirstNameLastName(objITForm005.getName());
			
			// REFERRAL_ID
			dbPotentialClient.setReferralId(1L); 
			
			// ADDRESS_LINE1
			dbPotentialClient.setAddressLine1(objITForm005.getAddress());
			
			// MAIL_ADDRESS
			dbPotentialClient.setEmailId(objITForm005.getEmailAddress());
			
			// ContactNumber
			/*
			 *  "cellNo" : "999-402-2998",
			 *  "homeNo" : "959-727-5656",
			 *  "workNo" : "999-496-9644"
			 */
			ClientContactNumberInfo contactNumberInfo = objITForm005.getContactNumber();
			dbPotentialClient.setContactNumber(contactNumberInfo.getCellNo());
			dbPotentialClient.setAlternateTelephone1(contactNumberInfo.getHomeNo());
			dbPotentialClient.setAlternateTelephone2(contactNumberInfo.getWorkNo());

			// NOTE_NO
//			dbPotentialClient.setPcNotesNumber(objITForm005.getNotes() + objITForm005.getBillingDepartmentNotes());
		}
		return dbPotentialClient;
	}
	
	/**
	 * 
	 * @param dbPotentialClient
	 * @return
	 */
	private PotentialClient fillOtherFieldsFrom006 (PotentialClient dbPotentialClient) {
		ITForm006 objITForm006 = itFormm006Service.getITForm006(dbPotentialClient.getInquiryNumber(), 
				dbPotentialClient.getClassId(), 
				dbPotentialClient.getLanguageId(), 
				dbPotentialClient.getIntakeFormNumber(), 
				dbPotentialClient.getIntakeFormId());
		if (objITForm006 != null) {
			// FIRST_LAST_NM
			dbPotentialClient.setFirstNameLastName(objITForm006.getName());
			
			// REFERRAL_ID
			ClientReferenceMedium medium = objITForm006.getClientReferenceMedium();
			List<String> selectedMediumList = medium.getListOfMediumAboutFirm();
			if (selectedMediumList != null) {
				String sMedium = selectedMediumList.get(0);
				dbPotentialClient.setReferralId(Long.valueOf(sMedium)); 
			}
			
			// ADDRESS_LINE1
			dbPotentialClient.setAddressLine1(objITForm006.getAddress().getStreetAddress()); // Street Address1
			
			// ADDRESS_LINE2
			dbPotentialClient.setAddressLine2(objITForm006.getBillingAddress().getStreetAddress()); // Street Address2
			
			// ContactNumber
			/*
			 *  "cellNo" : "999-402-2998",
			 *  "homeNo" : "959-727-5656",
			 *  "workNo" : "999-496-9644"
			 */
			ClientContactNumberInfo contactNumberInfo = objITForm006.getContactNumber();
			dbPotentialClient.setContactNumber(contactNumberInfo.getCellNo());
			dbPotentialClient.setAlternateTelephone1(contactNumberInfo.getHomeNo());
			dbPotentialClient.setAlternateTelephone2(contactNumberInfo.getWorkNo());
			
			// CITY
			dbPotentialClient.setCity(objITForm006.getAddress().getCity());
			
			// STATE
			dbPotentialClient.setState(objITForm006.getAddress().getState());
			
			// ZIP_CODE
			dbPotentialClient.setZipCode(objITForm006.getAddress().getZipCode());
			
			// MAIL_ADDRESS
			dbPotentialClient.setEmailId(objITForm006.getEmailAddress());
		}
		return dbPotentialClient;
	}
	
	/**
	 * findPotentialClient
	 * @param loginUserId
	 * @param searchPotentialClient
	 * @return
	 * @throws ParseException 
	 */
	public List<PotentialClient> findPotentialClient (SearchPotentialClient searchPotentialClient) 
			throws ParseException {
		
		if (searchPotentialClient.getSCreatedOn() != null && searchPotentialClient.getECreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchPotentialClient.getSCreatedOn(), searchPotentialClient.getECreatedOn());
			searchPotentialClient.setSCreatedOn(dates[0]);
			searchPotentialClient.setECreatedOn(dates[1]);
		}
		
		if (searchPotentialClient.getSConsultationDate() != null && searchPotentialClient.getEConsultationDate() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchPotentialClient.getSConsultationDate(), searchPotentialClient.getEConsultationDate());
			searchPotentialClient.setSConsultationDate(dates[0]);
			searchPotentialClient.setEConsultationDate(dates[1]);
		}
		
		PotentialClientSpecification spec = new PotentialClientSpecification(searchPotentialClient);
		List<PotentialClient> searchResults = potentialClientRepository.findAll(spec);
		log.info("results: " + searchResults);
		return searchResults;
	}
	
	/**
	 * updatePotentialClient
	 * @param potentialClientCode
	 * @param updatePotentialClient
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public PotentialClient updatePotentialClient (String potentialClientCode, String loginUserID, 
			UpdatePotentialClient updatePotentialClient) throws IllegalAccessException, InvocationTargetException {
		PotentialClient dbPotentialClient = getPotentialClient(potentialClientCode);
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		
		/*
		 * Audit Log Entry
		 */
		// CASE_CATEGORY_ID
		if (updatePotentialClient.getCaseCategoryId() != null && 
				updatePotentialClient.getCaseCategoryId()  != dbPotentialClient.getCaseCategoryId()) {
			log.info("Inserting Audit log for CASE_CATEGORY_ID");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "CASE_CATEGORY_ID", 
					String.valueOf(dbPotentialClient.getCaseCategoryId()), 
					String.valueOf(updatePotentialClient.getCaseCategoryId()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// FIRST_LAST_NM
		if (updatePotentialClient.getFirstNameLastName() != null && 
				!updatePotentialClient.getFirstNameLastName().equalsIgnoreCase(dbPotentialClient.getFirstNameLastName())) {
			log.info("Inserting Audit log for FIRST_LAST_NM");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "FIRST_LAST_NM", 
					String.valueOf(dbPotentialClient.getFirstNameLastName()), 
					String.valueOf(updatePotentialClient.getFirstNameLastName()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// REFERRAL_ID/REFERRAL_TEXT
		if (updatePotentialClient.getReferralId() != null && 
				updatePotentialClient.getReferralId() != dbPotentialClient.getReferralId()) {
			log.info("Inserting Audit log for REFERRAL_ID");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "REFERRAL_ID", 
					String.valueOf(dbPotentialClient.getReferralId()), 
					String.valueOf(updatePotentialClient.getReferralId()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// Email_ID
		if (updatePotentialClient.getEmailId() != null && 
				!updatePotentialClient.getEmailId().equalsIgnoreCase(dbPotentialClient.getEmailId())) {
			log.info("Inserting Audit log for Email_ID");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "Email_ID", 
					String.valueOf(dbPotentialClient.getEmailId()), 
					String.valueOf(updatePotentialClient.getEmailId()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// CONT_NO
		if (updatePotentialClient.getContactNumber() != null && 
				!updatePotentialClient.getContactNumber().equalsIgnoreCase(dbPotentialClient.getContactNumber())) {
			log.info("Inserting Audit log for CONT_NO");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "CONT_NO", 
					String.valueOf(dbPotentialClient.getContactNumber()), 
					String.valueOf(updatePotentialClient.getContactNumber()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// ALT_PHONE_1
		if (updatePotentialClient.getAlternateTelephone1() != null && 
				!updatePotentialClient.getAlternateTelephone1().equalsIgnoreCase(dbPotentialClient.getAlternateTelephone1())) {
			log.info("Inserting Audit log for ALT_PHONE_1");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "ALT_PHONE_1", 
					String.valueOf(dbPotentialClient.getAlternateTelephone1()), 
					String.valueOf(updatePotentialClient.getAlternateTelephone1()), 
					authTokenForSetupService.getAccess_token());
		}
				
		// ALT_PHONE_2
		if (updatePotentialClient.getAlternateTelephone2() != null && 
				!updatePotentialClient.getAlternateTelephone2().equalsIgnoreCase(dbPotentialClient.getAlternateTelephone2())) {
			log.info("Inserting Audit log for ALT_PHONE_2");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "ALT_PHONE_2", 
					String.valueOf(dbPotentialClient.getAlternateTelephone2()), 
					String.valueOf(updatePotentialClient.getAlternateTelephone2()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// ADDRESS_LINE1
		if (updatePotentialClient.getAddressLine1() != null && 
				!updatePotentialClient.getAddressLine1().equalsIgnoreCase(dbPotentialClient.getAddressLine1())) {
			log.info("Inserting Audit log for ADDRESS_LINE1");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "ADDRESS_LINE1", 
					String.valueOf(dbPotentialClient.getAddressLine1()), 
					String.valueOf(updatePotentialClient.getAddressLine1()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// CITY
		if (updatePotentialClient.getCity() != null && 
				!updatePotentialClient.getCity().equalsIgnoreCase(dbPotentialClient.getCity())) {
			log.info("Inserting Audit log for CITY");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "CITY", 
					String.valueOf(dbPotentialClient.getCity()), 
					String.valueOf(updatePotentialClient.getCity()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// STATE
		if (updatePotentialClient.getState() != null && 
				!updatePotentialClient.getState().equalsIgnoreCase(dbPotentialClient.getState())) {
			log.info("Inserting Audit log for STATE");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "STATE", 
					String.valueOf(dbPotentialClient.getState()), 
					String.valueOf(updatePotentialClient.getState()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// COUNTRY
		if (updatePotentialClient.getCountry() != null && 
				!updatePotentialClient.getCountry().equalsIgnoreCase(dbPotentialClient.getCountry())) {
			log.info("Inserting Audit log for COUNTRY");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "COUNTRY", 
					String.valueOf(dbPotentialClient.getCountry()), 
					String.valueOf(updatePotentialClient.getCountry()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// ZIP_CODE
		if (updatePotentialClient.getZipCode() != null && 
				!updatePotentialClient.getZipCode().equalsIgnoreCase(dbPotentialClient.getZipCode())) {
			log.info("Inserting Audit log for ZIP_CODE");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "ZIP_CODE", 
					String.valueOf(dbPotentialClient.getZipCode()), 
					String.valueOf(updatePotentialClient.getZipCode()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// CONSULT_DATE
		if (updatePotentialClient.getConsultationDate() != null && 
				updatePotentialClient.getConsultationDate() != dbPotentialClient.getConsultationDate()) {
			log.info("Inserting Audit log for CONSULT_DATE");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "CONSULT_DATE", 
					String.valueOf(dbPotentialClient.getConsultationDate()), 
					String.valueOf(updatePotentialClient.getConsultationDate()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// SSN_ID
		if (updatePotentialClient.getSocialSecurityNo() != null && 
				!updatePotentialClient.getSocialSecurityNo().equalsIgnoreCase(dbPotentialClient.getSocialSecurityNo())) {
			log.info("Inserting Audit log for SSN_ID");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "SSN_ID", 
					String.valueOf(dbPotentialClient.getSocialSecurityNo()), 
					String.valueOf(updatePotentialClient.getSocialSecurityNo()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// MAIL_ADDRESS
		if (updatePotentialClient.getMailingAddress() != null && 
				!updatePotentialClient.getMailingAddress().equalsIgnoreCase(dbPotentialClient.getMailingAddress())) {
			log.info("Inserting Audit log for MAIL_ADDRESS");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "MAIL_ADDRESS", 
					String.valueOf(dbPotentialClient.getMailingAddress()), 
					String.valueOf(updatePotentialClient.getMailingAddress()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// OCCUPATION
		if (updatePotentialClient.getOccupation() != null && 
				!updatePotentialClient.getOccupation().equalsIgnoreCase(dbPotentialClient.getOccupation())) {
			log.info("Inserting Audit log for OCCUPATION");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "OCCUPATION", 
					String.valueOf(dbPotentialClient.getOccupation()), 
					String.valueOf(updatePotentialClient.getOccupation()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// PC_NOTE_NO
		if (updatePotentialClient.getPcNotesNumber() != null && 
				!updatePotentialClient.getPcNotesNumber().equalsIgnoreCase(dbPotentialClient.getPcNotesNumber())) {
			log.info("Inserting Audit log for PC_NOTE_NO");
			setupService.createAuditLogRecord(loginUserID, 
					dbPotentialClient.getPotentialClientId(), 
					dbPotentialClient.getClassId(),
					POTENTIALCLIENT, "PC_NOTE_NO", 
					String.valueOf(dbPotentialClient.getPcNotesNumber()), 
					String.valueOf(updatePotentialClient.getPcNotesNumber()), 
					authTokenForSetupService.getAccess_token());
		}
		
		BeanUtils.copyProperties(updatePotentialClient, dbPotentialClient, CommonUtils.getNullPropertyNames(updatePotentialClient));
		
		dbPotentialClient.setUpdatedBy(loginUserID);
		dbPotentialClient.setUpdatedOn(new Date());
		log.info ("After modified : " + dbPotentialClient);
		return potentialClientRepository.save(dbPotentialClient);
	}
	
	/**
	 * updatePotentialClientAgreement
	 * @param potetnialClientId
	 * @param loginUserID
	 * @param updatePotentialClientAgreement
	 * @return
	 */
	public PotentialClient updatePotentialClientAgreement(String potetnialClientId, String loginUserID,
			@Valid UpdatePotentialClientAgreement updatePotentialClientAgreement) {
		PotentialClient dbPotentialClient = getPotentialClient(potetnialClientId);
		String agreementCode = updatePotentialClientAgreement.getAgreementCode();
		
		// Call AgreementTemplate Service
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		Agreement agreement = setupService.getAgreementURL(agreementCode, authTokenForSetupService.getAccess_token());
		log.info("agreement URL :" + agreement.getDocumentUrl());
		log.info("agreement Mailmerge :" + agreement.getMailMerge());
		
		// If MailMerge is Enabled
		if (Boolean.valueOf(agreement.getMailMerge()).booleanValue() == true) {
			// Call MailMerge feature
			AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
			MailMerge mailMerge = new MailMerge();
			if (agreementCode.equalsIgnoreCase(A001)){
				mailMerge = mailMerge_A001 (potetnialClientId, agreement.getDocumentUrl(), agreementCode, dbPotentialClient, mailMerge);
			} else if (agreementCode.equalsIgnoreCase(A003)) {
				mailMerge = mailMerge_A003 (potetnialClientId, agreement.getDocumentUrl(), agreementCode, dbPotentialClient, mailMerge);
			} else if (agreementCode.equalsIgnoreCase(A004)) {
				mailMerge = mailMerge_A004 (potetnialClientId, agreement.getDocumentUrl(), agreementCode, dbPotentialClient, mailMerge);
			} else if (agreementCode.equalsIgnoreCase(A005)) {
				mailMerge = mailMerge_A005 (potetnialClientId, agreement.getDocumentUrl(), agreementCode, dbPotentialClient, mailMerge);
			} else if (agreementCode.equalsIgnoreCase(A018)) {
				mailMerge = mailMerge_A018 (potetnialClientId, agreement.getDocumentUrl(), agreementCode, dbPotentialClient, mailMerge);
			}
			
			MailMerge mailMergeProcessed = commonService.mailMerge(mailMerge, authTokenForCommonService.getAccess_token());
			log.info("-----------filePath--------- :" + mailMergeProcessed.getDocumentUrlVersion());
			
			updatePotentialClientAgreement.setStatusId(19L); // Setting status as 19L after successful MailMerge
			dbPotentialClient.setReferenceField8(mailMergeProcessed.getProcessedFilePath());
			dbPotentialClient.setAgreementUrl(mailMergeProcessed.getProcessedFilePath());
			dbPotentialClient.setAgreementCurrentVerion(mailMergeProcessed.getDocumentUrlVersion());
		} else {
			log.info("MailMerge is not Enabled");
			// Download the Template and give the downloaded URL
			String filePath = propertiesConfig.getDocStorageBasePath();
			
			// template download alone
			filePath = filePath + propertiesConfig.getDocStorageTemplatePath() + 
					propertiesConfig.getDocStorageAgreementPath();
				
			updatePotentialClientAgreement.setStatusId(20L); // Setting status as 20L after successful MailMerge
			dbPotentialClient.setAgreementUrl(agreement.getDocumentUrl());
			dbPotentialClient.setReferenceField8(agreement.getDocumentUrl());
		}
		
		BeanUtils.copyProperties(updatePotentialClientAgreement, dbPotentialClient, CommonUtils.getNullPropertyNames(updatePotentialClientAgreement));
		dbPotentialClient.setUpdatedBy(loginUserID);
		dbPotentialClient.setUpdatedOn(new Date());
		
		log.info("-----------dbPotentialClient--------- :" + dbPotentialClient);
		return potentialClientRepository.save(dbPotentialClient);
	}
	
	/**
	 * 
	 * @param potetnialClientId
	 * @param documentUrl
	 * @param agreementCode
	 * @param dbPotentialClient
	 * @param mailMerge
	 * @return
	 */
	private MailMerge mailMerge_A018 (String potetnialClientId, String documentUrl, String agreementCode,
			PotentialClient dbPotentialClient, MailMerge mailMerge) {
		// Call MailMerge filling component
		String homeNo = mailMergeService.getHomeNo(potetnialClientId);
		log.info("-----------workNo--------- :" + homeNo);
		
		String[] arrFieldNames = new String[] {
				"Client_Name", 
				"Email_Address", 
				"Mailing_Address", 
				"Cell", 
				"Client_Name", 
				"Mailing_Address",
				"City", 
				"State",
				"Zip_Code", 
				"Home_Phone" ,
				"Email_Address",
				"Date"
		};
		Object[] arrFieldValues = new Object [] {
				dbPotentialClient.getFirstNameLastName(),	// Client_Name
				dbPotentialClient.getEmailId(),				// Email_Address
				dbPotentialClient.getAddressLine1(),		// Mailing_Address
				dbPotentialClient.getContactNumber(),		// Cell
				dbPotentialClient.getFirstNameLastName(),	// Client_Name
				dbPotentialClient.getAddressLine1(),		// Mailing_Address
				dbPotentialClient.getCity(),				// City
				dbPotentialClient.getState(),				// State
				dbPotentialClient.getZipCode(),				// Zip_Code
				homeNo,										// Home_Phone
				dbPotentialClient.getEmailId(),				// Email_Address
				DateUtils.getCurrentDateTime(), 			// Date
		};
		
		mailMerge.setClientId(potetnialClientId);
		mailMerge.setDocumentUrl(documentUrl);
		mailMerge.setDocumentUrlVersion(dbPotentialClient.getAgreementCurrentVerion());
		mailMerge.setAgreementCode(agreementCode);		
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setDocumentStorageFolder("agreement");
		mailMerge.setClassId(dbPotentialClient.getClassId());
		return mailMerge;
	}
	
	/**
	 * mailMerge_A005
	 * @param potetnialClientId
	 * @param documentUrl
	 * @param agreementCode
	 * @param dbPotentialClient
	 * @param mailMerge
	 * @return
	 */
	private MailMerge mailMerge_A005 (String potetnialClientId, String documentUrl, String agreementCode,
			PotentialClient dbPotentialClient, MailMerge mailMerge) {
		// Call MailMerge filling component
		String workNo = mailMergeService.getWorkNo(potetnialClientId);
		log.info("-----------workNo--------- :" + workNo);
		
		String[] arrFieldNames = new String[] {
				"Name","Email_Address","Address_Line_1","Cell","Name","Date","Name","Title","Address_Line_1","Work_Phone","Cell","Email_Address"
		};
		Object[] arrFieldValues = new Object [] {
				dbPotentialClient.getFirstNameLastName(),	// First_Name
				dbPotentialClient.getEmailId(),				// Email_Address
				dbPotentialClient.getAddressLine1(),		// Address_ Line_1
				dbPotentialClient.getContactNumber(),		// Cell
				dbPotentialClient.getFirstNameLastName(),	// First_Name
				DateUtils.getCurrentDateTime(), 			// Date
				dbPotentialClient.getFirstNameLastName(),	// First_Name
				dbPotentialClient.getReferenceField11(),	// REF_FIELD_11
				dbPotentialClient.getAddressLine1(),		// Address_ Line_1
				workNo,										// Work_Phone
				dbPotentialClient.getContactNumber(),		// Cell
				dbPotentialClient.getEmailId()				// Email_Address
		};
		
		mailMerge.setClientId(potetnialClientId);
		mailMerge.setDocumentUrl(documentUrl);
		mailMerge.setDocumentUrlVersion(dbPotentialClient.getAgreementCurrentVerion());
		mailMerge.setAgreementCode(agreementCode);		
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setDocumentStorageFolder("agreement");
		mailMerge.setClassId(dbPotentialClient.getClassId());
		return mailMerge;
	}
	
	/**
	 * mailMerge_A004
	 * @param potetnialClientId
	 * @param documentUrl
	 * @param agreementCode
	 * @param dbPotentialClient
	 * @param mailMerge
	 * @return
	 */
	private MailMerge mailMerge_A004 (String potetnialClientId, String documentUrl, String agreementCode,
			PotentialClient dbPotentialClient, MailMerge mailMerge) {
		// Call MailMerge filling component
		String workNo = mailMergeService.getWorkNo(potetnialClientId);
		log.info("-----------workNo--------- :" + workNo);
		
		String[] arrFieldNames = new String[] {
				"Name","Date","Name","Title","Address_Line_1","Cell","Home_Phone","Work_Phone","Email_Address","Date"
		};
		Object[] arrFieldValues = new Object [] {
				dbPotentialClient.getFirstNameLastName(),	// First_Name
				DateUtils.getCurrentDateTime(), 			// Date
				dbPotentialClient.getFirstNameLastName(),	// First_Name
				dbPotentialClient.getReferenceField11(),	// REF_FIELD_11
				dbPotentialClient.getAddressLine1(),		// Address_ Line_1
				dbPotentialClient.getContactNumber(),		// Cell
				dbPotentialClient.getContactNumber(),		// Home_Phone
				workNo,										// Work_Phone
				dbPotentialClient.getEmailId(),				// Email_Address
				DateUtils.getCurrentDateTime(), 			// Date
		};
		
		mailMerge.setClientId(potetnialClientId);
		mailMerge.setDocumentUrl(documentUrl);
		mailMerge.setDocumentUrlVersion(dbPotentialClient.getAgreementCurrentVerion());
		mailMerge.setAgreementCode(agreementCode);		
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setDocumentStorageFolder("agreement");
		mailMerge.setClassId(dbPotentialClient.getClassId());
		return mailMerge;
	}
	
	/**
	 * mailMerge_A003
	 * @param potetnialClientId
	 * @param documentUrl
	 * @param agreementCode
	 * @param dbPotentialClient
	 * @param mailMerge
	 * @return
	 */
	private MailMerge mailMerge_A003 (String potetnialClientId, String documentUrl, String agreementCode,
			PotentialClient dbPotentialClient, MailMerge mailMerge) {
		// Call MailMerge filling component
		String workNo = mailMergeService.getWorkNo(potetnialClientId);
		log.info("-----------workNo--------- :" + workNo);
		
		String[] arrFieldNames = new String[] {
				"Name","Date","Name","Title","Address_Line_1","Cell","Work_Phone","Email_Address","Date"
		};
		Object[] arrFieldValues = new Object [] {
				dbPotentialClient.getFirstNameLastName(),	// First_Name
				DateUtils.getCurrentDateTime(), 			// Date
				dbPotentialClient.getFirstNameLastName(),	// First_Name
				dbPotentialClient.getReferenceField11(),	// REF_FIELD_11
				dbPotentialClient.getAddressLine1(),		// Address_ Line_1
				dbPotentialClient.getContactNumber(),		// Home_Phone
				workNo,										// Work_Phone
				dbPotentialClient.getEmailId(),				// Email_Address
				DateUtils.getCurrentDateTime()	 			// Date
		};
		
		mailMerge.setClientId(potetnialClientId);
		mailMerge.setDocumentUrl(documentUrl);
		mailMerge.setDocumentUrlVersion(dbPotentialClient.getAgreementCurrentVerion());
		mailMerge.setAgreementCode(agreementCode);		
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setDocumentStorageFolder("agreement");
		mailMerge.setClassId(dbPotentialClient.getClassId());
		return mailMerge;
	}

	/**
	 * mailMerge_A001
	 * @param potetnialClientId
	 * @param documentUrl
	 * @param agreementCode
	 * @param dbPotentialClient
	 * @param mailMerge
	 * @return 
	 */
	private MailMerge mailMerge_A001 (String potetnialClientId,  String documentUrl, String agreementCode, 
			PotentialClient dbPotentialClient, MailMerge mailMerge) {
		// Call MailMerge filling component
		String workNo = mailMergeService.getWorkNo(potetnialClientId);
		log.info("-----------workNo--------- :" + workNo);
		
		String[] arrFieldNames = new String[] {
				"Date","First_Name","First_Name","Address_Line_1","Home_Phone","Work_Phone","Email_Address","Date"	
		};
		Object[] arrFieldValues = new Object [] {
				DateUtils.getCurrentDate(), 				// Date
				dbPotentialClient.getFirstNameLastName(),	// First_Name
				dbPotentialClient.getFirstNameLastName(),	// First_Name
				dbPotentialClient.getAddressLine1(),		// Address_ Line_1
				dbPotentialClient.getContactNumber(),		// Home_Phone
				workNo,										// Work_Phone
				dbPotentialClient.getEmailId(),				// Email_Address
				DateUtils.getCurrentDate()					// Date
		};
		
		mailMerge.setClientId(potetnialClientId);
		mailMerge.setDocumentUrl(documentUrl);
		mailMerge.setDocumentUrlVersion(dbPotentialClient.getAgreementCurrentVerion());
		mailMerge.setAgreementCode(agreementCode);		
		mailMerge.setFieldNames(arrFieldNames);
		mailMerge.setFieldValues(arrFieldValues);
		mailMerge.setDocumentStorageFolder("agreement");
		mailMerge.setClassId(dbPotentialClient.getClassId());
		return mailMerge;
	}
	
	/**
	 * deletePotentialClient
	 * @param loginUserID 
	 * @param potentialClientCode
	 * @return 
	 */
	public boolean deletePotentialClient (String potentialClientId, String loginUserID) {
		PotentialClient potentialClient = getPotentialClient(potentialClientId);
		log.info ("potentialClient in delete : " + potentialClient);
		if ( potentialClient != null && potentialClient.getStatusId() != null && 
				potentialClient.getStatusId().longValue() == 17) {
			throw new BadRequestException("Potential Client cannot be deleted as it has been already converted into Client: " + potentialClientId);
		}
		log.info ("potentialClient in delete ---1---->: " + potentialClient);
		potentialClient.setDeletionIndicator(1L);
		potentialClient.setUpdatedBy(loginUserID);
		potentialClient.setUpdatedOn(new Date());
		potentialClientRepository.save(potentialClient);
		return true;
	}
	
	//------------------------CLIENT CREATION---------------------------------------------------------------
	/**
	 * createClientGeneral
	 * @param loginUserId
	 * @param potentialClientId
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ClientGeneral createClientGeneral (String potentialClientId, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		PotentialClient dbPotentialClient = getPotentialClient(potentialClientId);
		AddClientGeneral dbClientGeneral = new AddClientGeneral();
		
		// POT_CLIENT_ID
		dbClientGeneral.setPotentialClientId(potentialClientId);
		
		// Class ID
		dbClientGeneral.setClassId(dbPotentialClient.getClassId());		
		
		// INQ_NO
		dbClientGeneral.setInquiryNumber(dbPotentialClient.getInquiryNumber());
		
		// IT_FORM_ID
		dbClientGeneral.setIntakeFormId(dbPotentialClient.getIntakeFormId());
		
		// IT_FORM_NUMBER
		dbClientGeneral.setIntakeFormNumber(dbPotentialClient.getIntakeFormNumber());
		
		// Client Category ID
		if (dbPotentialClient.getClientCategoryId() == null) {
			dbClientGeneral.setClientCategoryId(1L);
		} else {
			dbClientGeneral.setClientCategoryId(dbPotentialClient.getClientCategoryId());
		}
		
		// FIRST_NM - Pass POT_CLIENT_ID in POTENTIALCLIENT table , fetch FIRST_NM  and Insert

		if(dbPotentialClient.getFirstName() != null){
			dbClientGeneral.setFirstName(dbPotentialClient.getFirstName());
		}
		
		// LAST_NM - Pass POT_CLIENT_ID in POTENTIALCLIENT table , fetch LAST_NM  and Insert
		if(dbPotentialClient.getLastName() != null){
			dbClientGeneral.setLastName(dbPotentialClient.getLastName());
		}
		
		// FIRST_LAST_NM - Concatenate FIRST_NM and LAST_NM
		var name = dbPotentialClient.getFirstName() + " " + dbPotentialClient.getLastName();
		dbClientGeneral.setFirstNameLastName(name.replaceAll("&"," "));
		
		// LAST_FIRST_NM - Concatenate LAST_NM and FIRST_NM
		name = dbPotentialClient.getLastName() + " " + dbPotentialClient.getFirstName();
		dbClientGeneral.setLastNameFirstName(name.replaceAll("&"," "));

		// REFERRAL_ID
		dbClientGeneral.setReferralId(dbPotentialClient.getReferralId());
		
		// EMail_ID - Pass POT_CLIENT_ID in POTENTIALCLIENT table , fetch EMAIL_ID  and Insert
		dbClientGeneral.setEmailId(dbPotentialClient.getEmailId());
		
		// CONT_NO - Pass POT_CLIENT_ID in POTENTIALCLIENT table , fetch CONT_NO  and Insert
		dbClientGeneral.setContactNumber(dbPotentialClient.getContactNumber());

		// ADDRESS_LINE1 - Pass POT_CLIENT_ID in POTENTIALCLIENT table , fetch ADDRESS_LINE1 and Insert
		dbClientGeneral.setAddressLine1(dbPotentialClient.getAddressLine1());
		
		// ADDRESS_LINE2 - Pass POT_CLIENT_ID in POTENTIALCLIENT table , fetch ADDRESS_LINE2 and Insert
		dbClientGeneral.setAddressLine2(dbPotentialClient.getAddressLine2());
		
		// ADDRESS_LINE3 - Pass POT_CLIENT_ID in POTENTIALCLIENT table , fetch ADDRESS_LINE3 and Insert
		dbClientGeneral.setAddressLine3(dbPotentialClient.getAddressLine3());
		
		// CITY - Pass POT_CLIENT_ID in POTENTIALCLIENT table , fetch CITY and Insert
		dbClientGeneral.setCity(dbPotentialClient.getCity());
		
		// STATE
		dbClientGeneral.setState(dbPotentialClient.getState());
		
		// COUNTRY
		dbClientGeneral.setCountry(dbPotentialClient.getCountry());
		
		// ZIP_CODE
		dbClientGeneral.setZipCode(dbPotentialClient.getZipCode());
		
		// CONSULT_DATE
		dbClientGeneral.setConsultationDate(dbPotentialClient.getConsultationDate());
		
		// SSN_ID
		dbClientGeneral.setSocialSecurityNo(dbPotentialClient.getSocialSecurityNo());
		
		// IS_MAIL_SAME
		dbClientGeneral.setIsMailingAddressSame(Boolean.FALSE);
		
		// MAIL_ADDRESS
		dbClientGeneral.setMailingAddress(dbPotentialClient.getMailingAddress());
		
		// OCCUPATION
		dbClientGeneral.setOccupation(dbPotentialClient.getOccupation());
		
		// TRANS_ID
		dbClientGeneral.setTransactionId(3L); // Hard Coded Value "03"

		dbClientGeneral.setWorkNo(dbPotentialClient.getAlternateTelephone1());
		dbClientGeneral.setHomeNo(dbPotentialClient.getAlternateTelephone2());
		dbClientGeneral.setAlternateEmailId(dbPotentialClient.getReferenceField5());

		AuthToken authTokenForManagementService = authTokenService.getManagementServiceAuthToken();
		ClientGeneral createdClientGeneral = managementService.createClientGeneral(dbClientGeneral, loginUserID, authTokenForManagementService.getAccess_token());
		log.info("createdClientGeneral : " + createdClientGeneral);
		
		/*
		 * ------------------------------------------------------------------------------------------
		 * Create Client Portal User creation
		 * ------------------------------------------------------------------------------------------
		 */
//		ClientUser createdClientUser = createClientPortalUser (createdClientGeneral);
//		createdClientGeneral.setReferenceField11(createdClientUser.getClientUserId());
		
		// Updating Potential Client Status as 17L
		updatePotentialClientStatus (potentialClientId, loginUserID, 17L);
		
		return createdClientGeneral;
	}

	/**
	 * 
	 * @param potentialClientId
	 * @param loginUserID
	 * @param status
	 * @return
	 */
	public PotentialClient updatePotentialClientStatus(String potentialClientId, String loginUserID, Long status) {
		PotentialClient dbPotentialClient = getPotentialClient(potentialClientId);
		dbPotentialClient.setStatusId(status);
		dbPotentialClient.setUpdatedBy(loginUserID);
		dbPotentialClient.setUpdatedOn(new Date());
		potentialClientRepository.save(dbPotentialClient);
		return dbPotentialClient;
	}
	
	/**
	 * 
	 * @param dbPotentialClient
	 * @param loginUserID
	 * @return
	 */
	public PotentialClient updatePotentialClient(PotentialClient dbPotentialClient, String loginUserID) {
		dbPotentialClient = potentialClientRepository.save(dbPotentialClient);
		dbPotentialClient.setUpdatedBy(loginUserID);
		dbPotentialClient.setUpdatedOn(new Date());
		return dbPotentialClient;
	}
	
	/**
	 * findRecords
	 * @param searchFieldValue 
	 * @param searchTableName 
	 * @return
	 */
	public List<PotentialClient> findRecords (String searchFieldValue) {
		List<PotentialClient> potentialClients = potentialClientRepository.findRecords(searchFieldValue);
		log.info("potentialClients : " + potentialClients);
		return potentialClients.stream().filter(a -> a.getDeletionIndicator() == 0L).collect(Collectors.toList());
	}

	/**
	 * getPotentialClientReport
	 * @param searchPotentialClientReport
	 * @return
	 * @throws ParseException 
	 */
	public List<PotentialClient> getPotentialClientReport (SearchPotentialClientReport searchPotentialClientReport) throws ParseException {
		if (searchPotentialClientReport.getClassId() == null) {
			throw new BadRequestException("Class ID can't be blank.");
		}
		
		if (searchPotentialClientReport.getStatusId() == null || searchPotentialClientReport.getStatusId().isEmpty()) {
			throw new BadRequestException("StatusId can't be blank.");
		}
	
		if (searchPotentialClientReport.getFromCreatedOn() == null && 
				searchPotentialClientReport.getToCreatedOn() == null) {
			throw new BadRequestException("CreatedOn can't be blank.");
		}
		
		if (searchPotentialClientReport.getFromCreatedOn() != null && searchPotentialClientReport.getToCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchPotentialClientReport.getFromCreatedOn(), 
					searchPotentialClientReport.getToCreatedOn());
			searchPotentialClientReport.setFromCreatedOn(dates[0]);
			searchPotentialClientReport.setToCreatedOn(dates[1]);
		}
		
		PotentialClientReportSpecification spec = new PotentialClientReportSpecification (searchPotentialClientReport);
		List<PotentialClient> searchResults = potentialClientRepository.findAll(spec);
		log.info("results: " + searchResults);
		
		AuthToken authToken = authTokenService.getSetupServiceAuthToken();
		List<PotentialClientReport> potentialClientReportList = new ArrayList<>();
		for (PotentialClient potentialClient : searchResults) {
			 PotentialClientReport potentialClientReport = new PotentialClientReport ();
			/*
	    	 * CLASS_ID
	    	 * -----------------
	    	 * Fetch from POTENTIALCLIENT table.Pass CLASS_ID and fetch CLASS values from CLASSID table and display both the values
	    	 */
			com.mnrclara.api.crm.model.dto.Class classId = setupService.getClassId(potentialClient.getClassId(), authToken.getAccess_token());
			log.info("classId: " + classId);
			
			potentialClientReport.setClassId(classId.getClassDescription());
			potentialClientReport.setPotentialClientId(potentialClient.getPotentialClientId());
			potentialClientReport.setFirstNameLastName(potentialClient.getFirstNameLastName());
			potentialClientReport.setAddressLine1(potentialClient.getAddressLine1());
			potentialClientReport.setEmailId(potentialClient.getEmailId());
			potentialClientReport.setContactNumber(potentialClient.getContactNumber());
			
			/*
			 * REFERRAL_ID
			 * -------------------------
			 * Fetch from POTENTIALCLIENT table. Pass REFERRAL_ID in referral table and 
			 * fetch REFERRAL_TEXT. Need to display both the values
			 */
			if (potentialClient.getReferralId() != null) {
				Referral referral = setupService.getReferralId(potentialClient.getReferralId(), authToken.getAccess_token());
				log.info("referral: " + referral);
				
				if (referral != null) {
					potentialClientReport.setReferralId(potentialClient.getReferralId());
					potentialClientReport.setReferralIdDesc(referral.getReferralDescription());
					potentialClientReport.setSocialSecurityNo(potentialClient.getSocialSecurityNo());
					potentialClientReport.setReferenceField2(potentialClient.getReferenceField2());
					potentialClientReport.setReferenceField3(potentialClient.getReferenceField3());
					potentialClientReport.setReferenceField4(potentialClient.getReferenceField4());
					potentialClientReport.setStatusId(potentialClient.getStatusId());
				}
			}
			potentialClientReportList.add(potentialClientReport);
		}
		return searchResults;
	}

	/**
	 * 
	 * @param searchReferralReport
	 * @return
	 * @throws ParseException 
	 */
	public List<ReferralReport> getReferralReport(SearchReferralReport searchReferralReport) throws ParseException {
		if (searchReferralReport.getClassId() == null) {
			throw new BadRequestException("Class ID can't be blank.");
		}
		
		if (searchReferralReport.getFromCreatedOn() == null && 
				searchReferralReport.getToCreatedOn() == null) {
			throw new BadRequestException("CreatedOn can't be blank.");
		}
		
		if (searchReferralReport.getFromCreatedOn() != null && searchReferralReport.getToCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchReferralReport.getFromCreatedOn(), 
					searchReferralReport.getToCreatedOn());
			searchReferralReport.setFromCreatedOn(dates[0]);
			searchReferralReport.setToCreatedOn(dates[1]);
		}
		
		ReferralReportSpecification spec = new ReferralReportSpecification (searchReferralReport);
		List<PotentialClient> potentialClientSearchResults = potentialClientRepository.findAll(spec);
		log.info("potentialClientSearchResults: " + potentialClientSearchResults);
		
		AuthToken authToken = authTokenService.getSetupServiceAuthToken();
		List<ReferralReport> referralReportList = new ArrayList<>();
		for (PotentialClient potentialClient : potentialClientSearchResults) {
			ReferralReport referralReport = new ReferralReport();
			
			/*
	    	 * CLASS_ID
	    	 * -----------------
	    	 * Fetch from POTENTIALCLIENT table.Pass CLASS_ID and fetch CLASS values from CLASSID table and display both the values
	    	 */
			com.mnrclara.api.crm.model.dto.Class classId = setupService.getClassId(potentialClient.getClassId(), authToken.getAccess_token());
			log.info("classId: " + classId);
			referralReport.setClassId(classId.getClassDescription());
			referralReport.setPotentialClientId(potentialClient.getPotentialClientId());
			referralReport.setEmailId(potentialClient.getEmailId());
			referralReport.setReferralId(potentialClient.getReferralId());
			/*
			 * REFERRAL_ID
			 * -------------------------
			 * Fetch from POTENTIALCLIENT table. Pass REFERRAL_ID in referral table and 
			 * fetch REFERRAL_TEXT. Need to display both the values
			 */
			if (potentialClient.getReferralId() != null) {
				Referral referral = setupService.getReferralId(potentialClient.getReferralId(), authToken.getAccess_token());
				log.info("referral : " + referral);
				
				if (referral != null) {
					referralReport.setReferralDesc(referral.getReferralDescription());
				}
			}
			
			//---------Inquiry Fields----------------------------------------------
			Inquiry inquiry = inquiryService.getInquiry(potentialClient.getInquiryNumber());
			log.info("inquiry : " + inquiry);
			if (inquiry != null) {
				referralReport.setInquiryFirstNameLastName(inquiry.getFirstName() + " " + inquiry.getLastName());
			}
			
			//---------Client General----------------------------------------------
			/*
			 * Pass POT_CLIENT_ID in CLIENTGENERAL table and validate CLIENT_CATEGORY = 4. 
			 * If yes Pass the fetched CLIENT_ID in  into CLIENTGENERAL table as REF_FIELD_2 
			 * and fetch the CLIENT_ID
			 */
			ClientGeneral clientGeneral =
					clientGeneralRepository.findByPotentialClientId(potentialClient.getPotentialClientId());
			if (clientGeneral != null) {
				referralReport.setClientId(clientGeneral.getClientId());
				referralReport.setClientFirstNameLastName(clientGeneral.getFirstNameLastName());
			}

//			boolean is4CatIDAvbl = true;
//			ClientGeneral clientGeneral =
//					clientGeneralRepository.findByPotentialClientIdAndClientCategoryId(potentialClient.getPotentialClientId(), 4L);
//			if (clientGeneral != null && clientGeneral.getClientId() != null) {
//				// Beneficiary Client Number
//				ClientGeneral benClientGeneral = clientGeneralRepository.findByReferenceField2(clientGeneral.getClientId());
//				if (benClientGeneral != null) {
//					referralReport.setClientId(benClientGeneral.getClientId());
//					// Beneficiary Client Name
//					referralReport.setClientFirstNameLastName(benClientGeneral.getFirstNameLastName());
//				} else {
//					is4CatIDAvbl = false;
//				}
//			} else {
//				is4CatIDAvbl = false;
//			}
//
//			if (!is4CatIDAvbl) {
//				clientGeneral = clientGeneralRepository.findByPotentialClientIdAndClientCategoryId(potentialClient.getPotentialClientId(), 3L);
//				if (clientGeneral != null) {
//					// Beneficiary Client Number
//					if (clientGeneral != null) {
//						referralReport.setClientId(clientGeneral.getCorporationClientId());
//
//						// Beneficiary Client Name
//						referralReport.setClientFirstNameLastName(clientGeneral.getFirstNameLastName());
//					}
//				}
//			}
			
			referralReportList.add(referralReport);
		}
		log.info("referralReportList: " + referralReportList);
		return referralReportList;
	}
	
	/**
	 * 
	 * @param createdClientGeneral
	 * @return
	 */
	private ClientUser createClientPortalUser (ClientGeneral createdClientGeneral) {
		ClientUser newClientUser = new ClientUser ();
		BeanUtils.copyProperties(createdClientGeneral, newClientUser, CommonUtils.getNullPropertyNames(createdClientGeneral));
		
		/*
		 * CLIENT_USR_ID
		 * ------------------
		 * Pass CLASS_ID=03, NUM_RAN_CODE=19 in NUMBERRANGE table and Fetch NUM_RAN_CURRENT values and add +1 and then insert 
		 */
		long classID = 3;
		long NUM_RAN_CODE = 19;
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		String clientUserId = setupService.getNextNumberRange(classID, NUM_RAN_CODE, authTokenForSetupService.getAccess_token());
		newClientUser.setClientUserId(clientUserId);
		
		// QUOTATION - Hardcoded Value "1" for  CLASS_ID = 2, Hardcoded Value "0" for CLASS_ID=1
		// PAYMENTPLAN - Hardcoded Value "1" for  CLASS_ID = 2, Hardcoded Value "0" for CLASS_ID=1
		// DOCUMENT CHECKLIST	Hardcoded Value "1" for  CLASS_ID = 2, Hardcoded Value "0" for CLASS_ID=1
		if (createdClientGeneral.getClassId() == 1L) {
			newClientUser.setQuotation(0);
			newClientUser.setPaymentPlan(0);
			newClientUser.setDocuments(0);
		} else if (createdClientGeneral.getClassId() == 2L) {
			newClientUser.setQuotation(1);
			newClientUser.setPaymentPlan(1);
			newClientUser.setDocuments(1);
		}
		
		newClientUser.setMatter(1);
		newClientUser.setInvoice(1);
		newClientUser.setAgreement(1);
		
		ClientUser createdClientUser = setupService.createClientUser(newClientUser, 
				createdClientGeneral.getCreatedBy(), authTokenForSetupService.getAccess_token());
		log.info("createdClientUser :  " + createdClientUser);
		return createdClientUser;
	}
}
