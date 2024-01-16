package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.caseinfosheet.ImmCaseInfoSheet;
import com.mnrclara.api.management.model.caseinfosheet.LeCaseInfoSheet;
import com.mnrclara.api.management.model.caseinfosheet.SearchCaseSheetParams;
import com.mnrclara.api.management.repository.ImmCaseInfoSheetRepository;
import com.mnrclara.api.management.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImmCaseInfoSheetService {

	@Autowired
	ImmCaseInfoSheetRepository immCaseInfoSheetRepository;

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private SetupService setupService;
	
	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * getImmCaseInfoSheets
	 * 
	 * @return
	 */
	public List<ImmCaseInfoSheet> getImmCaseInfoSheets() {
		List<ImmCaseInfoSheet> immCaseInfoSheet = immCaseInfoSheetRepository.findAll();
		immCaseInfoSheet = immCaseInfoSheet.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return immCaseInfoSheet;
	}

	/**
	 * getImmCaseInfoSheet
	 * 
	 * @param itFormNo
	 * @param immCaseInfoSheetId
	 * @return
	 */
	public ImmCaseInfoSheet getImmCaseInfoSheet(String id) {
		ImmCaseInfoSheet immCaseInfoSheet = immCaseInfoSheetRepository.findById(id).orElse(null);
		if (immCaseInfoSheet != null && immCaseInfoSheet.getDeletionIndicator() != null
				&& immCaseInfoSheet.getDeletionIndicator() == 0) {
			return immCaseInfoSheet;
		} else {
			throw new BadRequestException("The given Case InformationID : " + id + " doesn't exist.");
		}
	}

	/**
	 * 
	 * @param searchCaseSheetParams
	 * @return
	 */
//	public List<ImmCaseInfoSheet> findByMultipleParams(SearchCaseSheetParams searchCaseSheetParams) {
//		List<ImmCaseInfoSheet> immCaseInfoSheets = immCaseInfoSheetRepository.findByMultipleParams(
//				searchCaseSheetParams.getCaseInformationId(), searchCaseSheetParams.getFirstNameLastName(),
//				searchCaseSheetParams.getClientId(), searchCaseSheetParams.getMatterNumber(),
//				searchCaseSheetParams.getStatusId(), searchCaseSheetParams.getCreatedBy(),
//				searchCaseSheetParams.getStartCreatedOn(), searchCaseSheetParams.getEndCreatedOn());
//		log.info("Details : " + immCaseInfoSheets);
//		return immCaseInfoSheets;
//	}
	
	/**
	 * 
	 * @param searchCaseSheetParams
	 * @return
	 */
	public List<ImmCaseInfoSheet> findByQuery (SearchCaseSheetParams searchCaseSheetParams) {
		log.info("searchCaseSheetParams : " + searchCaseSheetParams);
		Query dynamicQuery = new Query();
		
		// Query Params
		/*
		 * clientId;
		 * firstNameLastName;
		 * caseInformationId;
		 * matterNumber;
		 * statusId;
		 * createdBy;
		 * startCreatedOn;
		 * endCreatedOn;
		 */
		String clientId = searchCaseSheetParams.getClientId();
		String firstNameLastName = searchCaseSheetParams.getFirstNameLastName();
		String caseInformationId = searchCaseSheetParams.getCaseInformationId();
		String matterNumber = searchCaseSheetParams.getMatterNumber();
		Long statusId = searchCaseSheetParams.getStatusId();
		String createdBy = searchCaseSheetParams.getCreatedBy();
		Date startCreatedOn = searchCaseSheetParams.getStartCreatedOn();
		Date endCreatedOn = searchCaseSheetParams.getEndCreatedOn();
		
		log.info("CreatedOn : " + startCreatedOn + ":" + endCreatedOn);
		
		// Client ID
		if (clientId != null) {
		   Criteria clientIdCriteria = Criteria.where("clientId").is(clientId);
		   dynamicQuery.addCriteria(clientIdCriteria);
		}
		
		// FirstNameLastName
		if (firstNameLastName != null) {
		   Criteria nameCriteria = Criteria.where("firstNameLastName").is(firstNameLastName);
		   dynamicQuery.addCriteria(nameCriteria);
		}
		
		// CaseInformationId
		if (caseInformationId != null) {
		   Criteria idCriteria = Criteria.where("id").is(caseInformationId);
		   dynamicQuery.addCriteria(idCriteria);
		}
		
		// MatterNumber
		if (matterNumber != null) {
		   Criteria matterNumberCriteria = Criteria.where("matterNumber").is(matterNumber);
		   dynamicQuery.addCriteria(matterNumberCriteria);
		}
		
		// StatusId
		if (statusId != null) {
		   Criteria statusIdCriteria = Criteria.where("statusId").is(statusId);
		   dynamicQuery.addCriteria(statusIdCriteria);
		}
		
		// CreatedBy
		if (createdBy != null) {
		   Criteria createdByCriteria = Criteria.where("createdBy").is(createdBy);
		   dynamicQuery.addCriteria(createdByCriteria);
		}
		
		// StartCreatedOn
		if (startCreatedOn != null && endCreatedOn != null) {
			Criteria createdOnCriteria = Criteria.where("createdOn").gte(startCreatedOn).lt(endCreatedOn);
			dynamicQuery.addCriteria(createdOnCriteria);
		}
		
		log.info("dynamicQuery---> : " + dynamicQuery);
		List<ImmCaseInfoSheet> result = mongoTemplate.find(dynamicQuery, ImmCaseInfoSheet.class);
		return result;
	}

	/**
	 * createImmCaseInfoSheet
	 * 
	 * @param newImmCaseInfoSheet
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ImmCaseInfoSheet createImmCaseInfoSheet(ImmCaseInfoSheet newImmCaseInfoSheet, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {
		// LANG
		newImmCaseInfoSheet.setLanguageId("EN");

		// TRANS_ID
		if (newImmCaseInfoSheet.getTransactionId() == null) {
			newImmCaseInfoSheet.setTransactionId(7L); // Hard Coded Value "07"
		}

		if (newImmCaseInfoSheet.getId() == null) {
			// Pass CLASS_ID=02, NUM_RAN_CODE=09 in NUMBERRANGE table and Fetch
			// NUM_RAN_CURRENT values and add +1 and then insert

			long classID = 2L;
			long NUM_RAN_CODE = 9;

			// Get AuthToken for SetupService
			AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
			String ImmCaseInfoSheetID = setupService.getNextNumberRange(classID, NUM_RAN_CODE,
					authTokenForSetupService.getAccess_token());
			log.info("nextVal from NumberRange for ImmCaseInfoSheetID: " + ImmCaseInfoSheetID);
			newImmCaseInfoSheet.setId(ImmCaseInfoSheetID);
		}

		log.info("ID value: " + newImmCaseInfoSheet.getId());

		// STATUS_ID
		newImmCaseInfoSheet.setStatusId(1L); // Hard Coded value '1'
		newImmCaseInfoSheet.setDeletionIndicator(0L);
		newImmCaseInfoSheet.setCreatedBy(loginUserID);
		newImmCaseInfoSheet.setUpdatedBy(loginUserID);
		newImmCaseInfoSheet.setCreatedOn(new Date());
		newImmCaseInfoSheet.setUpdatedOn(new Date());
		return immCaseInfoSheetRepository.save(newImmCaseInfoSheet);
	}

	/**
	 * 
	 * @param caseInformationID
	 * @param modifiedImmCaseInfoSheet
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public ImmCaseInfoSheet updateImmCaseInfoSheet(String caseInformationID, ImmCaseInfoSheet modifiedImmCaseInfoSheet,
			String loginUserID) throws IllegalAccessException, InvocationTargetException {
		ImmCaseInfoSheet dbImmCaseInfoSheet = immCaseInfoSheetRepository.findById(caseInformationID).orElse(null);
		BeanUtils.copyProperties(modifiedImmCaseInfoSheet, dbImmCaseInfoSheet,
				CommonUtils.getNullPropertyNames(modifiedImmCaseInfoSheet));

		Long statusId = 0L;
		if (modifiedImmCaseInfoSheet.getStatusId() == 1L) {
			statusId = 2L;
		} else if (modifiedImmCaseInfoSheet.getStatusId() == 2L) {
			statusId = 2L;
		} else if (modifiedImmCaseInfoSheet.getStatusId() == 26L) {
			statusId = 26L;
		}
		
		// STATUS_ID
		dbImmCaseInfoSheet.setStatusId(statusId); 
		dbImmCaseInfoSheet.setUpdatedBy(loginUserID);
		dbImmCaseInfoSheet.setUpdatedOn(new Date());

		log.info("Tag : " + dbImmCaseInfoSheet);
		return immCaseInfoSheetRepository.save(dbImmCaseInfoSheet);
	}

	/**
	 * 
	 * @param caseInformationID
	 * @param loginUserID
	 */
	public void deleteImmCaseInfoSheet(String caseInformationID, String loginUserID) {
		ImmCaseInfoSheet dbImmCaseInfoSheet = immCaseInfoSheetRepository.findById(caseInformationID).orElse(null);
		log.info("ID value: " + dbImmCaseInfoSheet.getId());
		dbImmCaseInfoSheet.setDeletionIndicator(1L);
		dbImmCaseInfoSheet.setReferenceField10("DELETED");
		dbImmCaseInfoSheet.setUpdatedBy(loginUserID);
		dbImmCaseInfoSheet.setUpdatedOn(new Date());
		immCaseInfoSheetRepository.save(dbImmCaseInfoSheet);
	}
	
	/**
	 * 
	 * @param caseInformationID
	 */
	public void deleteImmCaseInfoSheet (String caseInformationID) {
		ImmCaseInfoSheet dbImmCaseInfoSheet = immCaseInfoSheetRepository.findById(caseInformationID).orElse(null);
		immCaseInfoSheetRepository.delete(dbImmCaseInfoSheet);
	}
}