package com.mnrclara.api.crm.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.crm.config.PropertiesConfig;
import com.mnrclara.api.crm.model.agreement.AddAgreement;
import com.mnrclara.api.crm.model.agreement.Agreement;
import com.mnrclara.api.crm.model.agreement.SearchAgreement;
import com.mnrclara.api.crm.model.agreement.UpdateAgreement;
import com.mnrclara.api.crm.model.auth.AuthToken;
import com.mnrclara.api.crm.model.dto.Dashboard;
import com.mnrclara.api.crm.model.dto.EnvelopeResponse;
import com.mnrclara.api.crm.model.dto.EnvelopeStatus;
import com.mnrclara.api.crm.model.dto.UserProfile;
import com.mnrclara.api.crm.model.potentialclient.PotentialClient;
import com.mnrclara.api.crm.repository.AgreementRepository;
import com.mnrclara.api.crm.repository.PotentialClientRepository;
import com.mnrclara.api.crm.repository.specification.AgreementSpecification;
import com.mnrclara.api.crm.util.CommonUtils;
import com.mnrclara.api.crm.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AgreementService {
	
	@Autowired
	PotentialClientService potentialClientService;
	
	@Autowired
	PotentialClientRepository potentialClientRepository;
	
	@Autowired
	AgreementRepository agreementRepository;
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	SetupService setupService;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	PropertiesConfig propertiesConfig;

	@Autowired
	NotificationService notificationService;
	
	/**
	 * getAgreements
	 * @return
	 */
	public List<Agreement> getAgreements () {
		List<Agreement> agreements = agreementRepository.findAll();
		return agreements.stream().filter(n -> n.getDeletionIndicator() != null && 
				n.getDeletionIndicator() == 0).collect(Collectors.toList());
	}
	
	/**
	 * getAgreement
	 * @param agreementModuleCode
	 * @return
	 */
	public Agreement getAgreement (String agreementCode) {
		Agreement agreement = agreementRepository.findByAgreementCode(agreementCode).orElse(null);
		if (agreement != null && agreement.getDeletionIndicator() != 1) {
			return agreement;
		}
		return null; // Record got deleted
	}
	
	/**
	 * 
	 * @param potentialClientId
	 * @return
	 */
	public Agreement getAgreementByPotentialClientId (String potentialClientId) {
		Optional<Agreement> agreement = agreementRepository.findByPotentialClientId(potentialClientId);
    	log.info("Agreement : " + agreement);
    	
    	if (!agreement.isEmpty()) {
    		return agreement.get();
    	} 
    	return null;
	}
	
	/**
	 * findAgreement
	 * @param searchAgreement
	 * @return
	 * @throws ParseException 
	 */
	public List<Agreement> findAgreement(SearchAgreement searchAgreement) throws ParseException {
		if (searchAgreement.getSSentOn() != null && searchAgreement.getESentOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchAgreement.getSSentOn(), searchAgreement.getESentOn());
			searchAgreement.setSSentOn(dates[0]);
			searchAgreement.setESentOn(dates[1]);
		}
		
		if (searchAgreement.getSReceivedOn() != null && searchAgreement.getEReceivedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchAgreement.getSReceivedOn(), searchAgreement.getEReceivedOn());
			searchAgreement.setSReceivedOn(dates[0]);
			searchAgreement.setEReceivedOn(dates[1]);
		}
		
		if (searchAgreement.getSResentOn() != null && searchAgreement.getEResentOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchAgreement.getSResentOn(), searchAgreement.getEResentOn());
			searchAgreement.setSResentOn(dates[0]);
			searchAgreement.setEResentOn(dates[1]);
		}
		
		if (searchAgreement.getSApprovedOn() != null && searchAgreement.getEApprovedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchAgreement.getSApprovedOn(), searchAgreement.getEApprovedOn());
			searchAgreement.setSApprovedOn(dates[0]);
			searchAgreement.setEApprovedOn(dates[1]);
		}
		
		AgreementSpecification spec = new AgreementSpecification(searchAgreement);
		List<Agreement> searchResults = agreementRepository.findAll(spec);
		log.info("results: " + searchResults);
		return searchResults;
	}
	
	/**
	 * getAgreementCount
	 * 
	 * STATUS_ID=12,13,14
	 * @param classId
	 * @param typeOfDashboard 
	 * @return
	 */
	public Dashboard getAgreementCount (Long classId, String typeOfDashboard) {
		Dashboard dashboard = new Dashboard();
		if (typeOfDashboard.equalsIgnoreCase(CommonUtils.DashboardTypes.AGREEMENT.name())) {
			List<Agreement> agreementList =  null;
			if (classId == 1 || classId == 2) {
				agreementList = agreementRepository.findByClassId(classId);
			} else if (classId == 3) {
				// return all records count
				agreementList = agreementRepository.findAll();
			}
			Long totCount = agreementList.stream().filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0).count();
			
			// STATUS_ID=12,13,14
			Long count = agreementList.stream().filter(i -> i.getDeletionIndicator() != null &&
				i.getDeletionIndicator() == 0 && (i.getStatusId() == 12 || i.getStatusId() == 13 || i.getStatusId() == 14)
			).count();
			dashboard.setFiteredCount(count);
			dashboard.setTotalCount(totCount);
			return dashboard;
		} else if (typeOfDashboard.equalsIgnoreCase(CommonUtils.DashboardTypes.AGREEMENT_TOTAL.name())) {
			List<Agreement> agreementList =  null;
			if (classId == 1 || classId == 2) {
				agreementList = agreementRepository.findByClassId(classId);
			} else if (classId == 3) {
				// return all records count
				agreementList = agreementRepository.findAll();
			}
			Long totCount = agreementList.stream().filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0).count();
			
			// STATUS_ID=12,13,14,15,16
			Long count = agreementList.stream().filter(i -> i.getDeletionIndicator() != null &&
				i.getDeletionIndicator() == 0 && 
				(i.getStatusId() == 12 || i.getStatusId() == 13 || i.getStatusId() == 14 || i.getStatusId() == 15 || i.getStatusId() == 16)
			).count();
			
			dashboard.setFiteredCount(count);
			dashboard.setTotalCount(totCount);
			return dashboard;
		}  else if (typeOfDashboard.equalsIgnoreCase(CommonUtils.DashboardTypes.AGREEMENT_SENT.name())) {
			List<Agreement> agreementList =  null;
			if (classId == 1 || classId == 2) {
				agreementList = agreementRepository.findByClassId(classId);
			} else if (classId == 3) {
				// return all records count
				agreementList = agreementRepository.findAll();
			}
			Long totCount = agreementList.stream().filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0).count();
			
			// STATUS_ID=12,13
			Long count = agreementList.stream().filter(i -> i.getDeletionIndicator() != null &&
				i.getDeletionIndicator() == 0 && 
				(i.getStatusId() == 12 || i.getStatusId() == 13)
			).count();
			
			dashboard.setFiteredCount(count);
			dashboard.setTotalCount(totCount);
			return dashboard;
		} else if (typeOfDashboard.equalsIgnoreCase(CommonUtils.DashboardTypes.AGREEMENT_RECEIVED.name())) {
			List<Agreement> agreementList =  null;
			if (classId == 1 || classId == 2) {
				agreementList = agreementRepository.findByClassId(classId);
			} else if (classId == 3) {
				// return all records count
				agreementList = agreementRepository.findAll();
			}
			Long totCount = agreementList.stream().filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0).count();
			
			// STATUS_ID = 14,15
			Long count = agreementList.stream().filter(i -> i.getDeletionIndicator() != null &&
				i.getDeletionIndicator() == 0 && 
				(i.getStatusId() == 14 || i.getStatusId() == 15)
			).count();
			
			dashboard.setFiteredCount(count);
			dashboard.setTotalCount(totCount);
			return dashboard;
		} else if (typeOfDashboard.equalsIgnoreCase(CommonUtils.DashboardTypes.AGREEMENT_RESENT.name())) {
			List<Agreement> agreementList =  null;
			if (classId == 1 || classId == 2) {
				agreementList = agreementRepository.findByClassId(classId);
			} else if (classId == 3) {
				// return all records count
				agreementList = agreementRepository.findAll();
			}
			Long totCount = agreementList.stream().filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0).count();
			
			// STATUS_ID = 14,15
			Long count = agreementList.stream().filter(i -> i.getDeletionIndicator() != null && i.getDeletionIndicator() == 0 && i.getStatusId() == 16).count();
			dashboard.setFiteredCount(count);
			dashboard.setTotalCount(totCount);
			return dashboard;
		}
		return null;
	}
	
	/**
	 * getAgreementCount
	 * @param loginUserId
	 * @param typeOfDashboard 
	 * @return
	 */
	public Dashboard getAgreementCount (String loginUserId, String typeOfDashboard) {
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		UserProfile userProfile = setupService.getUserProfile(loginUserId, authTokenForSetupService.getAccess_token());
		return getAgreementCount (userProfile.getClassId(), typeOfDashboard);
	}
	
	/**
	 * createAgreement
	 * @param addAgreement 
	 * @param newAgreement
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Agreement createAgreement (AddAgreement addAgreement, String potentialClientId, String loginUserId) 
			throws IllegalAccessException, InvocationTargetException {		
		PotentialClient dbPotentialClient = potentialClientService.getPotentialClient(potentialClientId);
		
		String filename = dbPotentialClient.getReferenceField8();
		if (filename != null && filename.startsWith("/")) {
			filename = dbPotentialClient.getReferenceField8().substring(1); 	// Removing Front slash from URL
		}
		
		log.info("dbPotentialClient.getClassId()  : " + dbPotentialClient.getClassId() );
		
		/*
		 * Deciding the FilePath (whether LNE or Immigration
		 */
		String filePath = "";
		if (dbPotentialClient.getClassId() == 1) {								// - LNE
			// Choose Y:\Client\2 Employment-Labor Clients\Clara
			filePath = propertiesConfig.getDocStorageLNEPath() + 
					propertiesConfig.getDocStorageAgreementPath() + "/" + dbPotentialClient.getPotentialClientId();
			log.info("LNE path : " + filePath);
		} else { 																// - Immigration
			// Choose X:\Firm\Immigration Section\1LawOfficeDoc\Clara
			filePath = propertiesConfig.getDocStorageImmigrationPath() + 
					propertiesConfig.getDocStorageAgreementPath() + "/" + dbPotentialClient.getPotentialClientId();
			log.info("Immigration path : " + filePath);
		}
		
		filename = filename.substring(0, filename.lastIndexOf('.'));			// Removing extension of file
		log.info("-------filename ------- : " + filename);
		
		// Call Docusign envelope request
		// Get AuthToken for SetupService
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		EnvelopeResponse envelopeResponse = commonService.callDocusignEnvelope(authTokenForCommonService.getAccess_token(), 
				addAgreement.getAgreementCode(),
				dbPotentialClient.getPotentialClientId(),
				dbPotentialClient.getAgreementUrl(),
				filename,
				dbPotentialClient.getFirstNameLastName(),
				dbPotentialClient.getEmailId(),
				filePath);
		log.info("envelopeResponse : " + envelopeResponse);
		
		Agreement dbAgreement = new Agreement();
		
		// LANG_ID - Pass the selected POT_CLIENT_ID in POTENTIALCLIENT table and fetch LANG_ID and insert
		dbAgreement.setLanguageId(dbPotentialClient.getLanguageId());
		
		// CLASS_ID
		dbAgreement.setClassId(dbPotentialClient.getClassId());
		
		// AGREEMENT_CODE
		dbAgreement.setAgreementCode(addAgreement.getAgreementCode());
		
		// POT_CLIENT_ID
		dbAgreement.setPotentialClientId(potentialClientId);
		
		// INQ_NO
		dbAgreement.setInquiryNumber(dbPotentialClient.getInquiryNumber());
		
		// AGREEMENT_URL
		dbAgreement.setAgreementURL(dbPotentialClient.getAgreementUrl());
		
		// AGREEMENT_URL_VER - Hard Coded Value 1.0
		dbAgreement.setAgreementURLVersion(String.valueOf(dbPotentialClient.getAgreementCurrentVerion()));
		
		// CASE_CATEGORY_ID
		dbAgreement.setCaseCategoryId(dbPotentialClient.getCaseCategoryId());
		
		// STATUS_ID - Hard Coded Value '12"
		dbAgreement.setStatusId(12L);
		
		// TRANS_ID - Hard Coded Value "04"
		dbAgreement.setTransactionId(4L);
		
		// EMail_ID
		dbAgreement.setEmailId(dbPotentialClient.getEmailId());
		
		// SENT_ON
		dbAgreement.setSentOn(new Date());
		
		// CTD_BY
		dbAgreement.setCreatedBy(loginUserId);
		
		// CTD_ON
		dbAgreement.setCreatedOn(new Date());
		
		// UTD_BY
		dbAgreement.setUpdatedBy(loginUserId);
		
		// UTD_ON
		dbAgreement.setUpdatedOn(new Date());
		
		dbAgreement.setDeletionIndicator(0L);
		Agreement agreement = agreementRepository.save(dbAgreement);
		return agreement;
	}
	
	/**
	 * updateAgreement
	 * @param agreementCode
	 * @param updateAgreement
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Agreement updateAgreement (String agreementCode, UpdateAgreement updateAgreement, String loginUserID ) 
			throws IllegalAccessException, InvocationTargetException {
		Agreement dbAgreement = getAgreement(agreementCode);
		log.info ("DB : " + dbAgreement);
		BeanUtils.copyProperties(updateAgreement, dbAgreement, CommonUtils.getNullPropertyNames(updateAgreement));
		dbAgreement.setUpdatedBy(loginUserID);
		dbAgreement.setUpdatedOn(new Date());
		log.info ("After modified : " + dbAgreement);
		return agreementRepository.save(dbAgreement);
	}
	
	/**
	 * 
	 * @param agreementCode
	 * @param potentialClientId
	 * @param updateAgreement
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public Agreement updateAgreementFromDocusignFlow(String agreementCode, String potentialClientId,
			@Valid UpdateAgreement updateAgreement, String loginUserID) throws IllegalAccessException, InvocationTargetException { 
		Agreement dbAgreement = getAgreement(agreementCode);
		PotentialClient dbPotentialClient = potentialClientService.getPotentialClient(potentialClientId);
		BeanUtils.copyProperties(dbPotentialClient, dbAgreement, CommonUtils.getNullPropertyNames(dbPotentialClient));
		
		// AGREEMENT_CODE
		dbAgreement.setAgreementCode(agreementCode);
		
		// POT_CLIENT_ID
		dbAgreement.setPotentialClientId(potentialClientId);
		
		// INQ_NO
		dbAgreement.setInquiryNumber(dbPotentialClient.getInquiryNumber());
		
		// AGREEMENT_URL
		dbAgreement.setAgreementURL(dbPotentialClient.getAgreementUrl());
		
		// AGREEMENT_URL_VER - Hard Coded Value 1.1
		dbAgreement.setAgreementURLVersion(String.valueOf(dbPotentialClient.getAgreementCurrentVerion()));
		
		// STATUS_ID - Hard Coded Value "15"
		dbAgreement.setStatusId(15L);
		
		// EMail_ID
		dbAgreement.setEmailId(dbPotentialClient.getEmailId());
		
		// RECEIVED_ON
		dbAgreement.setReceivedOn(new Date());
		
		//APPROVED_ON
		dbAgreement.setApprovedOn(new Date());
		
		//REF_FIELD_10
		dbAgreement.setReferenceField10("RECEIVED UPDATE FROM DOCUSIGN");
		
		// UTD_BY
		dbAgreement.setUpdatedBy(loginUserID);
		
		// UTD_ON
		dbAgreement.setUpdatedOn(new Date());
		Agreement createdAgreement = agreementRepository.save(dbAgreement);
		
		potentialClientService.createClientGeneral(potentialClientId, loginUserID);
		dbPotentialClient.setStatusId(17L);
		dbPotentialClient.setUpdatedBy(loginUserID);
		dbPotentialClient.setUpdatedOn(new Date());
		potentialClientRepository.save(dbPotentialClient);
		return createdAgreement;
	}
	
	/**
	 * 
	 * @param agreementCode
	 * @param status
	 * @return
	 */
	public Agreement updateAgreementStatus(String agreementCode, Long status, String loginUserID) {
		Agreement dbAgreement = getAgreement(agreementCode);
		dbAgreement.setStatusId(status);
		
		// UTD_BY
		dbAgreement.setUpdatedBy(loginUserID);
		
		// UTD_ON
		dbAgreement.setUpdatedOn(new Date());
		return agreementRepository.save(dbAgreement);
	}
	
	/**
	 * deleteAgreement
	 * @param agreementCode
	 */
	public void deleteAgreement (String agreementCode, String loginUserID) {
		Agreement agreement = getAgreement(agreementCode);
		if ( agreement != null) {
			agreement.setDeletionIndicator(1L);
			agreement.setUpdatedBy(loginUserID);
			agreement.setUpdatedOn(new Date());
			agreementRepository.save(agreement);
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + agreementCode);
		}
	}

	/**
	 * findRecords
	 * @param searchFieldValue
	 * @return
	 */
	public List<Agreement> findRecords(String searchFieldValue) {
		List<Agreement> agreements = agreementRepository.findRecords(searchFieldValue);
		log.info("agreements : " + agreements	);
		return agreements.stream().filter(a -> a.getDeletionIndicator() == 0L).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public AuthToken genToken(String code) {
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		return commonService.genToken(code, authTokenForCommonService.getAccess_token());
	}
	
	/**
	 * getDocusignEnvelopeStatus
	 * @return
	 */
	public EnvelopeStatus getDocusignEnvelopeStatus (String potentialClientId) {
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		EnvelopeStatus envStatus = 
				commonService.getDocusignEnvelopeStatus(authTokenForCommonService.getAccess_token(), potentialClientId);
		return envStatus;		
	}
	
	/**
	 * downloadEnvelopeFromDocusign
	 * @param potentialClientId
	 * @return
	 */
	public String downloadEnvelopeFromDocusign (String potentialClientId, String loginUserID) {
		/*
		 * Deciding the FilePath (whether LNE or Immigration
		 */
		String filePath = "";
		PotentialClient potentialClient = potentialClientService.getPotentialClient(potentialClientId);
		if (potentialClient.getClassId() == 1) {								// - LNE
			// Choose Y:\Client\2 Employment-Labor Clients\Clara
			filePath = propertiesConfig.getDocStorageLNEPath() + 
					propertiesConfig.getDocStorageAgreementPath() + "/" + potentialClient.getPotentialClientId();
			log.info("LNE path : " + filePath);
		} else { 																// - Immigration
			// Choose X:\Firm\Immigration Section\1LawOfficeDoc\Clara
			filePath = propertiesConfig.getDocStorageImmigrationPath() + 
					propertiesConfig.getDocStorageAgreementPath() + "/" + potentialClient.getPotentialClientId();
			log.info("Immigration path : " + filePath);
		}
		
		AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
		String response = commonService.downloadEnvelopeFromDocusign(authTokenForCommonService.getAccess_token(), potentialClientId, filePath);
		
		// Updating AgreementURL column in Potential Client table
		potentialClient.setAgreementUrl(response);
		potentialClient.setReferenceField8(response);
		potentialClient.setStatusId(15L); //Hard coded as 15
		potentialClient = potentialClientService.updatePotentialClient(potentialClient, loginUserID);
		log.info("downloadEnvelopeFromDocusign : " + potentialClient);
		
		Optional<Agreement> optAgreement = agreementRepository.findByPotentialClientId(potentialClientId);
		if (!optAgreement.isEmpty()) {
			Agreement objAgreement = optAgreement.get();
			objAgreement.setApprovedOn(new Date());
			objAgreement.setReceivedOn(new Date());
			objAgreement.setStatusId(15L);
			agreementRepository.save(objAgreement);
		}
		return response;
	}

	/**
	 * 
	 * @param potentialClientId
	 */
	public void sendNotification(String potentialClientId){
		PotentialClient potentialClient = potentialClientService.getPotentialClient(potentialClientId);
		if(potentialClient != null && potentialClient.getStatusId() == 15L) {
			List<String> userId = new ArrayList<>();
			if(potentialClient.getReferenceField2() != null){
				userId.add(potentialClient.getReferenceField2());
			}
			if(potentialClient.getReferenceField4() != null){
				userId.add(potentialClient.getReferenceField4());
			}
			this.notificationService.saveNotifications(
					userId,
					null, "Agreement Sign has been done on ",
					"Agreement Sign",
					new Date(), "");
		}
	}
}
