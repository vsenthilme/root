package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
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
import com.mnrclara.api.management.model.caseinfosheet.LeCaseInfoSheet;
import com.mnrclara.api.management.model.caseinfosheet.SearchCaseSheetParams;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.repository.LeCaseInfoSheetRepository;
import com.mnrclara.api.management.repository.MatterGenAccRepository;
import com.mnrclara.api.management.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LeCaseInfoSheetService {

	@Autowired
	private LeCaseInfoSheetRepository leCaseInfoSheetRepository;

	@Autowired
	private MatterGenAccRepository matterGenAccRepository;

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private SetupService setupService;

	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * getLeCaseInfoSheets
	 * 
	 * @return
	 */
	public List<LeCaseInfoSheet> getLeCaseInfoSheets() {
		List<LeCaseInfoSheet> leCaseInfoSheets = leCaseInfoSheetRepository.findAll();
		leCaseInfoSheets = leCaseInfoSheets.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
				.collect(Collectors.toList());
		return leCaseInfoSheets;
	}

	/**
	 * getLeCaseInfoSheet
	 * 
	 * @param itFormNo
	 * @param leCaseInfoSheetId
	 * @return
	 */
	public LeCaseInfoSheet getLeCaseInfoSheet(String id) {
		LeCaseInfoSheet leCaseInfoSheet = leCaseInfoSheetRepository.findById(id).orElse(null);
		if (leCaseInfoSheet != null && leCaseInfoSheet.getDeletionIndicator() != null
				&& leCaseInfoSheet.getDeletionIndicator() == 0) {
			return leCaseInfoSheet;
		} /*else {
			throw new BadRequestException("The given ClientGeneral ID : " + id + " doesn't exist.");
		}*/
		return null;
	}

	/**
	 * 
	 * @param searchCaseSheetParams
	 * @return
	 */
//	public List<LeCaseInfoSheet> findByMultipleParams(SearchCaseSheetParams searchCaseSheetParams) {
//		List<LeCaseInfoSheet> leCaseInfoSheets = leCaseInfoSheetRepository.findByMultipleParams(
//				searchCaseSheetParams.getCaseInformationId(), searchCaseSheetParams.getFirstNameLastName(),
//				searchCaseSheetParams.getClientId(), searchCaseSheetParams.getMatterNumber(),
//				searchCaseSheetParams.getStatusId(), searchCaseSheetParams.getCreatedBy(),
//				searchCaseSheetParams.getStartCreatedOn(), searchCaseSheetParams.getEndCreatedOn());
//		log.info("Details : " + leCaseInfoSheets);
//		return leCaseInfoSheets;
//	}
	
	/**
	 * 
	 * @param searchCaseSheetParams
	 * @return
	 */
	public List<LeCaseInfoSheet> findByQuery (SearchCaseSheetParams searchCaseSheetParams) {
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
		List<LeCaseInfoSheet> result = mongoTemplate.find(dynamicQuery, LeCaseInfoSheet.class);
		return result;
	}

	/**
	 * createLeCaseInfoSheet
	 * 
	 * @param newLeCaseInfoSheet
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public LeCaseInfoSheet createLeCaseInfoSheet(LeCaseInfoSheet newLeCaseInfoSheet, String loginUserID)
			throws IllegalAccessException, InvocationTargetException {

		// LANG_ID
		newLeCaseInfoSheet.setLanguageId("EN");

		// TRANS_ID
		if (newLeCaseInfoSheet.getTransactionId() == null) {
			newLeCaseInfoSheet.setTransactionId(6L); // Hard Coded Value "06"
		}

		if (newLeCaseInfoSheet.getId() == null) {
			// Pass CLASS_ID=01, NUM_RAN_CODE=08 in NUMBERRANGE table and Fetch
			// NUM_RAN_CURRENT values and add +1 and then insert
			long classID = 1L;
			long NUM_RAN_CODE = 8;

			// Get AuthToken for SetupService
			AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
			String leCaseInfoSheetID = setupService.getNextNumberRange(classID, NUM_RAN_CODE,
					authTokenForSetupService.getAccess_token());
			log.info("nextVal from NumberRange for leCaseInfoSheetID: " + leCaseInfoSheetID);
			newLeCaseInfoSheet.setId(leCaseInfoSheetID);
		}

		// STATUS_ID
		newLeCaseInfoSheet.setStatusId(1L); // Hard Coded value '1'
		newLeCaseInfoSheet.setDeletionIndicator(0L);
		log.info("ID value: " + newLeCaseInfoSheet.getId());
		newLeCaseInfoSheet.setCreatedBy(loginUserID);
		newLeCaseInfoSheet.setUpdatedBy(loginUserID);
		newLeCaseInfoSheet.setCreatedOn(new Date());
		newLeCaseInfoSheet.setUpdatedOn(new Date());
		return leCaseInfoSheetRepository.save(newLeCaseInfoSheet);
	}

	/**
	 * 
	 * @param caseInformationID
	 * @param modifiedLeCaseInfoSheet
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public LeCaseInfoSheet updateLeCaseInfoSheet(String caseInformationID, LeCaseInfoSheet modifiedLeCaseInfoSheet,
			String loginUserID) throws IllegalAccessException, InvocationTargetException {
		LeCaseInfoSheet dbLeCaseInfoSheet = leCaseInfoSheetRepository.findById(caseInformationID).orElse(null);
		log.info("ID value: " + dbLeCaseInfoSheet);
		BeanUtils.copyProperties(modifiedLeCaseInfoSheet, dbLeCaseInfoSheet,
				CommonUtils.getNullPropertyNames(modifiedLeCaseInfoSheet));

		Long statusId = 0L;
		if (modifiedLeCaseInfoSheet.getStatusId() == 1L) {
			statusId = 2L;
		} else if (modifiedLeCaseInfoSheet.getStatusId() == 2L) {
			statusId = 2L;
		} else if (modifiedLeCaseInfoSheet.getStatusId() == 26L) {
			statusId = 26L;
		}
		
		// STATUS_ID
		dbLeCaseInfoSheet.setStatusId(statusId);
		dbLeCaseInfoSheet.setUpdatedBy(loginUserID);
		dbLeCaseInfoSheet.setUpdatedOn(new Date());
		return leCaseInfoSheetRepository.save(dbLeCaseInfoSheet);
	}

	/**
	 * 
	 * @param caseInformationID
	 * @param clientID
	 */
	public void deleteLeCaseInfoSheet(String id, String loginUserID) throws Exception{
		LeCaseInfoSheet dbLeCaseInfoSheet = leCaseInfoSheetRepository.findById(id).orElse(null);
		log.info("ID value: " + dbLeCaseInfoSheet.getId());
		dbLeCaseInfoSheet.setDeletionIndicator(1L);
		dbLeCaseInfoSheet.setReferenceField10("DELETED");
		dbLeCaseInfoSheet.setUpdatedBy(loginUserID);
		dbLeCaseInfoSheet.setUpdatedOn(new Date());
		leCaseInfoSheetRepository.save(dbLeCaseInfoSheet);
	}

	/**
	 * 
	 * @param caseInformationID
	 * @param loginUserID
	 * @return
	 */
	public MatterGenAcc createMatter(String caseInformationID, String loginUserID) {
		LeCaseInfoSheet dbLeCaseInfoSheet = leCaseInfoSheetRepository.findById(caseInformationID).orElse(null);
		log.info("ID value: " + dbLeCaseInfoSheet.getId());
		MatterGenAcc matterGeneralAccount = new MatterGenAcc();

		// LANG_ID
		matterGeneralAccount.setLanguageId("EN");

		// CLASS_ID
		matterGeneralAccount.setClassId(dbLeCaseInfoSheet.getClassId());

		// CLIENT_ID
		matterGeneralAccount.setClientId(dbLeCaseInfoSheet.getClientId());

		// CASEINFO_NO
		matterGeneralAccount.setCaseInformationNo(caseInformationID);

		// MATTER_NO
		/*
		 * "Pass the selected CLIENT_ID in MATTERGENACC table and fetch MATTER_NO
		 * values. 1. sort by descending, add the next number to the latest no and
		 * insert (Example : if the last no is 10001-05, insert a no 10001-06) 2. If
		 * MATTER_NO value is null, insert a number start with '01' by adding CLIENT_ID
		 * (Example : 10001-01)"
		 * 
		 */
		String MATTER_NO = "";
		List<MatterGenAcc> matterGeneralAccounts = matterGenAccRepository
				.findByClientId(dbLeCaseInfoSheet.getClientId());
		MATTER_NO = getNextMatterNo(dbLeCaseInfoSheet.getClientId(), matterGeneralAccounts);
		matterGeneralAccount.setMatterNumber(MATTER_NO);

		// CASE_CATEGORY_ID
		matterGeneralAccount.setCaseCategoryId(dbLeCaseInfoSheet.getCaseCategoryId());

		// CASE_SUB_CATEGORY_ID
		matterGeneralAccount.setCaseSubCategoryId(dbLeCaseInfoSheet.getCaseSubCategoryId());

		// MATTER_TEXT
		matterGeneralAccount.setMatterDescription(dbLeCaseInfoSheet.getMatterDescription());

		// TRANS_ID
		matterGeneralAccount.setTransactionId(8L); // Hard Coded Value "08"

		// CASE_OPEN_DATE
		matterGeneralAccount.setCaseOpenedDate(new Date());
//		matterGeneralAccount.setCaseOpenedDate(LocalDateTime.now());

		// STATUS_ID
		matterGeneralAccount.setStatusId(25L); // Hard coded Value "25"

		matterGeneralAccount.setCreatedBy(loginUserID);
		matterGeneralAccount.setUpdatedBy(loginUserID);
		matterGeneralAccount.setCreatedOn(new Date());
		matterGeneralAccount.setUpdatedOn(new Date());

		MatterGenAcc createdMatterGenAcc = matterGenAccRepository.save(matterGeneralAccount);

		/*
		 * When a MATTER_NO is created and inserted in MATTERGENACC table successfully,
		 * update LECASEINFOSHEET table by CASEINFO_NO with below fields
		 */
		// MATTER_NO
		dbLeCaseInfoSheet.setMatterNumber(MATTER_NO);

		// STATUS_ID
		dbLeCaseInfoSheet.setStatusId(25L); // Update with Hardcoded value "25"
		leCaseInfoSheetRepository.save(dbLeCaseInfoSheet);

		return createdMatterGenAcc;
	}

	/**
	 * getNextMatterNo
	 * 
	 * @param clientId
	 * @param matterGeneralAccounts
	 * @return
	 */
	private String getNextMatterNo(String clientId, List<MatterGenAcc> matterGeneralAccounts) {
		if (matterGeneralAccounts.isEmpty()) {
			// Generate new Matter NO
			return clientId + "-01"; // 10001-01
		} else {
			// Increment Matter No by 1
			List<Integer> matterSubnumbers = new ArrayList<>();
			for (MatterGenAcc matter : matterGeneralAccounts) {
				if (matter != null && matter.getMatterNumber() != null) {
					String[] sSplitted = matter.getMatterNumber().split("-");
					matterSubnumbers.add(Integer.valueOf(sSplitted[1]));
				}
			}
			Collections.sort(matterSubnumbers);
			int maxNo = matterSubnumbers.get(matterSubnumbers.size() - 1);
			maxNo++;
			if (maxNo < 10) {
				log.info("Max Matter No : " + clientId + "-0" + maxNo);
				return clientId + "-0" + maxNo;
			}
		}
		return clientId;
	}
}