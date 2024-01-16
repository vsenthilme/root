package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.mnrclara.api.management.model.matterexpense.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.repository.MatterExpenseRepository;
import com.mnrclara.api.management.repository.specification.MatterExpenseSpecification;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatterExpenseService {
	
	private static final String MATTEREXPENSE = "MATTEREXPENSE";

	@Autowired
	private MatterExpenseRepository matterExpenseRepository;
	
	@Autowired
	private MatterGenAccService matterGenAccService;
	
	@Autowired
	private SetupService setupService;

	@Autowired
	AuthTokenService authTokenService;
	
	/**
	 * getMatterExpenses
	 * @return
	 */
	public List<MatterExpense> getMatterExpenses () {
		List<MatterExpense> matterExpenseList =  matterExpenseRepository.findAll();
		matterExpenseList = matterExpenseList.stream()
				.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0).collect(Collectors.toList());
		return matterExpenseList;
	}
	
	/**
	 * getMatterExpense
	 * @param matterExpenseId
	 * @return
	 */
	public MatterExpense getMatterExpense (Long matterExpenseId) {
		MatterExpense matterExpense = matterExpenseRepository.findByMatterExpenseIdAndDeletionIndicator(matterExpenseId, 0L);
		if (matterExpense != null && matterExpense.getDeletionIndicator() != null && matterExpense.getDeletionIndicator() == 0) {
			return matterExpense;
		} else {
			throw new BadRequestException("The given MatterExpense ID : " + matterExpenseId + " doesn't exist.");
		}
	}
	
	/**
	 * 
	 * @param preBillNumber
	 * @return
	 */
	public List<MatterExpense> getMatterExpenseForApprove(String preBillNumber) {
		List<MatterExpense> matterExpense = 
				matterExpenseRepository.findByReferenceField1AndDeletionIndicator(preBillNumber, 0L);
		if (matterExpense != null) {
			return matterExpense;
		} else {
			throw new BadRequestException("The given MatterExpense/Prebill ID : " + preBillNumber + " doesn't exist.");
		}
	}
	
	/**
	 * createMatterExpense
	 * @param newMatterExpense
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterExpense createMatterExpense (AddMatterExpense newMatterExpense, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterExpense dbMatterExpense = new MatterExpense();
		BeanUtils.copyProperties(newMatterExpense, dbMatterExpense, CommonUtils.getNullPropertyNames(newMatterExpense));
		
		MatterGenAcc matterGenAcc = matterGenAccService.getMatterGenAcc(newMatterExpense.getMatterNumber());
		
		// MATTER_EXP_ID
		dbMatterExpense.setMatterExpenseId(System.currentTimeMillis());
		
		// LANG_ID
		dbMatterExpense.setLanguageId(matterGenAcc.getLanguageId());

		// CLASS_ID
		dbMatterExpense.setClassId(matterGenAcc.getClassId());

		// CLIENT_ID
		dbMatterExpense.setClientId(matterGenAcc.getClientId());
		// CASE_CATEGORY_ID
		dbMatterExpense.setCaseCategoryId(matterGenAcc.getCaseCategoryId());	
		
		// CASE_SUB_CATEGORY_ID
		dbMatterExpense.setCaseSubCategoryId(matterGenAcc.getCaseSubCategoryId());
		
		dbMatterExpense.setReferenceField2(DateUtils.addTimeToDate(newMatterExpense.getReferenceField2()));
		
		// STATUS_ID
		dbMatterExpense.setStatusId(38L);
		dbMatterExpense.setDeletionIndicator(0L);
		dbMatterExpense.setCreatedBy(loginUserID);
		dbMatterExpense.setUpdatedBy(loginUserID);
		dbMatterExpense.setCreatedOn(new Date());
		dbMatterExpense.setUpdatedOn(new Date());		
		return matterExpenseRepository.save(dbMatterExpense);
	}
	
	/**
	 * 
	 * @param newMatterExpenses
	 * @param loginUserID
	 */
	public void createBulkMatterExpenses(@Valid AddMatterExpense[] newMatterExpenses, String loginUserID) {
		List<MatterExpense> createMatterExpenses = new ArrayList<>();
		for (AddMatterExpense newMatterExpense : newMatterExpenses) {
			MatterExpense dbMatterExpense = new MatterExpense();
			BeanUtils.copyProperties(newMatterExpense, dbMatterExpense, CommonUtils.getNullPropertyNames(newMatterExpense));
			
			MatterGenAcc matterGenAcc = matterGenAccService.getMatterGenAcc(newMatterExpense.getMatterNumber());
			
			// MATTER_EXP_ID
			dbMatterExpense.setMatterExpenseId(System.currentTimeMillis());
			
			// LANG_ID
			if (matterGenAcc.getLanguageId() != null) {
				dbMatterExpense.setLanguageId(matterGenAcc.getLanguageId().trim());
			}

			// CLASS_ID
			dbMatterExpense.setClassId(matterGenAcc.getClassId());

			// CLIENT_ID
			if (matterGenAcc.getClientId() != null) {
				dbMatterExpense.setClientId(matterGenAcc.getClientId().trim());
			}
			
			// CASE_CATEGORY_ID
			dbMatterExpense.setCaseCategoryId(matterGenAcc.getCaseCategoryId());	
			
			// CASE_SUB_CATEGORY_ID
			dbMatterExpense.setCaseSubCategoryId(matterGenAcc.getCaseSubCategoryId());
			
			// STATUS_ID
			dbMatterExpense.setStatusId(38L);
			dbMatterExpense.setDeletionIndicator(0L);
			createMatterExpenses.add(dbMatterExpense);
		}
		List<MatterExpense> createdMatterExpenses = matterExpenseRepository.saveAll(createMatterExpenses);
		log.info("createdMatterExpenses : " + createdMatterExpenses);
	}
	
	/**
	 * updateMatterExpense
	 * @param matterexpenseId
	 * @param updateMatterExpense
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public MatterExpense updateMatterExpense (Long matterExpenseId, UpdateMatterExpense updateMatterExpense, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException {
		MatterExpense dbMatterExpense = getMatterExpense(matterExpenseId);
		
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();

		// NO_ITEMS
		if (updateMatterExpense.getNumberofItems() != null
				&& updateMatterExpense.getNumberofItems().longValue() != dbMatterExpense.getNumberofItems().longValue()) {
			log.info("Inserting Audit log for NO_ITEMS");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(matterExpenseId), 3L, MATTEREXPENSE,
					"NO_ITEMS", String.valueOf(dbMatterExpense.getNumberofItems()),
					String.valueOf(updateMatterExpense.getNumberofItems()), authTokenForSetupService.getAccess_token());
		}
		
		// EXP_TEXT
		if (updateMatterExpense.getExpenseDescription() != null
				&& !updateMatterExpense.getExpenseDescription().equalsIgnoreCase(dbMatterExpense.getExpenseDescription())) {
			log.info("Inserting Audit log for EXP_TEXT");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(matterExpenseId), 3L, MATTEREXPENSE,
					"EXP_TEXT", dbMatterExpense.getExpenseDescription(),
					updateMatterExpense.getExpenseDescription(), authTokenForSetupService.getAccess_token());
		}
				
		// BILL_TYPE
		if (updateMatterExpense.getBillType() != null
				&& !updateMatterExpense.getBillType().equalsIgnoreCase(dbMatterExpense.getBillType())) {
			log.info("Inserting Audit log for BILL_TYPE");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(matterExpenseId), 3L, MATTEREXPENSE,
					"BILL_TYPE", dbMatterExpense.getBillType(), updateMatterExpense.getBillType(), 
					authTokenForSetupService.getAccess_token());
		}
		
		// EXP_TYPE
		if (updateMatterExpense.getExpenseType() != null
				&& !updateMatterExpense.getExpenseType().equalsIgnoreCase(dbMatterExpense.getExpenseType())) {
			log.info("Inserting Audit log for EXP_TYPE");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(matterExpenseId), 3L, MATTEREXPENSE,
					"EXP_TYPE", dbMatterExpense.getExpenseType(), updateMatterExpense.getExpenseType(), 
					authTokenForSetupService.getAccess_token());
		}
		
		// WRITE_OFF
		if (updateMatterExpense.getWriteOff() != null
				&& updateMatterExpense.getWriteOff() != dbMatterExpense.getWriteOff()) {
			log.info("Inserting Audit log for WRITE_OFF");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(matterExpenseId), 3L, MATTEREXPENSE,
					"WRITE_OFF", String.valueOf(dbMatterExpense.getWriteOff()), String.valueOf(updateMatterExpense.getWriteOff()), 
					authTokenForSetupService.getAccess_token());
		}
		
		// EXP_ACCOUNT_NO
		if (updateMatterExpense.getExpenseAccountNumber() != null
				&& !updateMatterExpense.getExpenseAccountNumber().equalsIgnoreCase(dbMatterExpense.getExpenseAccountNumber())) {
			log.info("Inserting Audit log for EXP_ACCOUNT_NO");
			setupService.createAuditLogRecord(loginUserID, String.valueOf(matterExpenseId), 3L, MATTEREXPENSE,
					"EXP_ACCOUNT_NO", dbMatterExpense.getExpenseAccountNumber(), 
					updateMatterExpense.getExpenseAccountNumber(), 
					authTokenForSetupService.getAccess_token());
		}
		BeanUtils.copyProperties(updateMatterExpense, dbMatterExpense, CommonUtils.getNullPropertyNames(updateMatterExpense));
		dbMatterExpense.setStatusId(updateMatterExpense.getStatusId());
		dbMatterExpense.setUpdatedBy(loginUserID);
		dbMatterExpense.setUpdatedOn(new Date());
		log.info("dbMatterExpense : " + dbMatterExpense);
		
		return matterExpenseRepository.save(dbMatterExpense);
	}
	
	/**
	 * 
	 * @param matterExpenseId
	 * @param updateMatterExpenses
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<MatterExpense> updateMatterExpense (List<UpdateMatterExpense> updateMatterExpenses, 
			String loginUserID) throws IllegalAccessException, InvocationTargetException {
		// Get AuthToken for SetupService
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		List<MatterExpense> matterExpenseList = new ArrayList<>();
		for (UpdateMatterExpense updateMatterExpense : updateMatterExpenses) {
			Long matterExpenseId = updateMatterExpense.getMatterExpenseId();
			MatterExpense dbMatterExpense = getMatterExpense(matterExpenseId);
			
			// NO_ITEMS
			if (updateMatterExpense.getNumberofItems() != null
					&& updateMatterExpense.getNumberofItems().longValue() != dbMatterExpense.getNumberofItems().longValue()) {
				log.info("Inserting Audit log for NO_ITEMS");
				setupService.createAuditLogRecord(loginUserID, String.valueOf(matterExpenseId), 3L, MATTEREXPENSE,
						"NO_ITEMS", String.valueOf(dbMatterExpense.getNumberofItems()),
						String.valueOf(updateMatterExpense.getNumberofItems()), authTokenForSetupService.getAccess_token());
			}
			
			// EXP_TEXT
			if (updateMatterExpense.getExpenseDescription() != null
					&& !updateMatterExpense.getExpenseDescription().equalsIgnoreCase(dbMatterExpense.getExpenseDescription())) {
				log.info("Inserting Audit log for EXP_TEXT");
				setupService.createAuditLogRecord(loginUserID, String.valueOf(matterExpenseId), 3L, MATTEREXPENSE,
						"EXP_TEXT", dbMatterExpense.getExpenseDescription(),
						updateMatterExpense.getExpenseDescription(), authTokenForSetupService.getAccess_token());
			}
					
			// BILL_TYPE
			if (updateMatterExpense.getBillType() != null
					&& !updateMatterExpense.getBillType().equalsIgnoreCase(dbMatterExpense.getBillType())) {
				log.info("Inserting Audit log for BILL_TYPE");
				setupService.createAuditLogRecord(loginUserID, String.valueOf(matterExpenseId), 3L, MATTEREXPENSE,
						"BILL_TYPE", dbMatterExpense.getBillType(), updateMatterExpense.getBillType(), 
						authTokenForSetupService.getAccess_token());
			}
			
			// EXP_TYPE
			if (updateMatterExpense.getExpenseType() != null
					&& !updateMatterExpense.getExpenseType().equalsIgnoreCase(dbMatterExpense.getExpenseType())) {
				log.info("Inserting Audit log for EXP_TYPE");
				setupService.createAuditLogRecord(loginUserID, String.valueOf(matterExpenseId), 3L, MATTEREXPENSE,
						"EXP_TYPE", dbMatterExpense.getExpenseType(), updateMatterExpense.getExpenseType(), 
						authTokenForSetupService.getAccess_token());
			}
			
			// WRITE_OFF
			if (updateMatterExpense.getWriteOff() != null
					&& updateMatterExpense.getWriteOff() != dbMatterExpense.getWriteOff()) {
				log.info("Inserting Audit log for WRITE_OFF");
				setupService.createAuditLogRecord(loginUserID, String.valueOf(matterExpenseId), 3L, MATTEREXPENSE,
						"WRITE_OFF", String.valueOf(dbMatterExpense.getWriteOff()), String.valueOf(updateMatterExpense.getWriteOff()), 
						authTokenForSetupService.getAccess_token());
			}
			
			// EXP_ACCOUNT_NO
			if (updateMatterExpense.getExpenseAccountNumber() != null
					&& !updateMatterExpense.getExpenseAccountNumber().equalsIgnoreCase(dbMatterExpense.getExpenseAccountNumber())) {
				log.info("Inserting Audit log for EXP_ACCOUNT_NO");
				setupService.createAuditLogRecord(loginUserID, String.valueOf(matterExpenseId), 3L, MATTEREXPENSE,
						"EXP_ACCOUNT_NO", dbMatterExpense.getExpenseAccountNumber(), 
						updateMatterExpense.getExpenseAccountNumber(), 
						authTokenForSetupService.getAccess_token());
			}
			BeanUtils.copyProperties(updateMatterExpense, dbMatterExpense, CommonUtils.getNullPropertyNames(updateMatterExpense));
			dbMatterExpense.setUpdatedBy(loginUserID);
			dbMatterExpense.setUpdatedOn(new Date());
			MatterExpense createdMatterExpense = matterExpenseRepository.save(dbMatterExpense);
			matterExpenseList.add(createdMatterExpense);
		}
		return matterExpenseList;
	}
	
	/**
	 * deleteMatterExpense
	 * @param matterExpenseId
	 * @param loginUserID
	 */
	public void deleteMatterExpense (Long matterExpenseId, String loginUserID) {
		MatterExpense matterExpense = getMatterExpense(matterExpenseId);
		if (matterExpense != null && matterExpense.getStatusId() == 38L) {
			matterExpense.setDeletionIndicator(1L);
			matterExpense.setUpdatedBy(loginUserID);
			matterExpense.setUpdatedOn(new Date());
			matterExpenseRepository.save(matterExpense);
		} else if (matterExpense.getStatusId() == 37L) {
			throw new EntityNotFoundException("Selected Expense Code can't be deleted as this is already billed");
		} else {
			throw new EntityNotFoundException("Error in deleting Id: " + matterExpenseId);
		}
	}

	/**
	 * findMatterExpenses
	 * @param searchMatterExpense
	 * @return
	 * @throws ParseException 
	 */
	public List<MatterExpense> findMatterExpenses(SearchMatterExpense searchMatterExpense) throws ParseException {
		if (searchMatterExpense.getStartCreatedOn() != null && searchMatterExpense.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterExpense.getStartCreatedOn(), 
					searchMatterExpense.getEndCreatedOn());
			searchMatterExpense.setStartCreatedOn(dates[0]);
			searchMatterExpense.setEndCreatedOn(dates[1]);
		}
		
		MatterExpenseSpecification spec = new MatterExpenseSpecification (searchMatterExpense);
		List<MatterExpense> results = matterExpenseRepository.findAll(spec);
		log.info("results: " + results);
		return results;
	}

	/**
	 *
	 * @param searchMatterExpense
	 * @return
	 * @throws ParseException
	 */
	public List<IMatterExpense> findMatterExpensesNew(SearchMatterExpense searchMatterExpense) throws ParseException {
		if (searchMatterExpense.getStartCreatedOn() != null && searchMatterExpense.getEndCreatedOn() != null) {
			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterExpense.getStartCreatedOn(),
					searchMatterExpense.getEndCreatedOn());
			searchMatterExpense.setStartCreatedOn(dates[0]);
			searchMatterExpense.setEndCreatedOn(dates[1]);
		}

		List<IMatterExpense> results = matterExpenseRepository.getExpenseList(searchMatterExpense.getMatterNumber(),
				searchMatterExpense.getStatusId(),
				searchMatterExpense.getCreatedBy(),
				searchMatterExpense.getExpenseCode(),
				searchMatterExpense.getExpenseType(),
				searchMatterExpense.getStartCreatedOn(),
				searchMatterExpense.getEndCreatedOn()
		);
		log.info("results: " + results);
		return results;
	}
}
