package com.mnrclara.api.accounting.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.mnrclara.api.accounting.model.prebill.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mnrclara.api.accounting.controller.exception.BadRequestException;
import com.mnrclara.api.accounting.model.auth.AuthToken;
import com.mnrclara.api.accounting.model.management.MatterExpense;
import com.mnrclara.api.accounting.model.management.MatterGenAcc;
import com.mnrclara.api.accounting.model.management.MatterTimeTicket;
import com.mnrclara.api.accounting.model.management.SearchMatterGeneral;
import com.mnrclara.api.accounting.repository.MatterExpenseRepository;
import com.mnrclara.api.accounting.repository.MatterGenAccRepository;
import com.mnrclara.api.accounting.repository.MatterTimeTicketRepository;
import com.mnrclara.api.accounting.repository.PreBillDetailsRepository;
import com.mnrclara.api.accounting.repository.specification.MatterGenAccSpecification;
import com.mnrclara.api.accounting.repository.specification.PreBillDetailsSpecification;
import com.mnrclara.api.accounting.util.CommonUtils;
import com.mnrclara.api.accounting.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PreBillDetailsService {
	
	@Autowired
	PreBillDetailsRepository preBillDetailsRepository;
	
	@Autowired
	MatterTimeTicketRepository matterTimeTicketRepository;
	
	@Autowired
	MatterExpenseRepository matterExpenseRepository;
	
	@Autowired
	MatterGenAccRepository matterGenAccRepository;
	
	@Autowired
	SetupService setupService;
	
	@Autowired
	ManagementService managementService;
	
	@Autowired
	QuotationHeaderService quotationHeaderService;
	
	@Autowired
	PaymentPlanLineService paymentPlanLineService;
	
	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	CRMService crmService;
	
	/**
	 * getPreBillDetailss
	 * @return
	 */
	public List<AddPreBillDetails> getPreBillDetailss () {
		List<PreBillDetails> preBillDetailsList =  preBillDetailsRepository.findAll();
		preBillDetailsList = preBillDetailsList.stream()
							.filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
							.collect(Collectors.toList());
		
		List<AddPreBillDetails> listAddPreBillDetails = new ArrayList<>();
		for (PreBillDetails preBillDetails : preBillDetailsList) {
			AddPreBillDetails newPreBillDetails = new AddPreBillDetails();
			BeanUtils.copyProperties(preBillDetails, newPreBillDetails, CommonUtils.getNullPropertyNames(preBillDetails));
			listAddPreBillDetails.add(newPreBillDetails);
		}
		return listAddPreBillDetails;
	}
	
	/**
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param sortBy
	 * @return
	 */
	public Page<PreBillDetails> getAllPreBillDetailss(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		Page<PreBillDetails> pagedResult = preBillDetailsRepository.findByDeletionIndicator(0L,paging);
		return pagedResult;
	}
	
	/**
	 * 
	 * @param preBillBatchNumber
	 * @param preBillNumber
	 * @param preBillDate
	 * @param matterNumber
	 * @return
	 */
	public PreBillDetails getPreBillDetails (String preBillBatchNumber, String preBillNumber, 
			Date preBillDate, String matterNumber) {
		Optional<PreBillDetails> preBillDetails = 
				preBillDetailsRepository.findByPreBillBatchNumberAndPreBillNumberAndPreBillDateAndMatterNumberAndDeletionIndicator(
						 preBillBatchNumber, preBillNumber, preBillDate, matterNumber, 0L);
		if (preBillDetails.isEmpty()) {
			throw new BadRequestException("The given PreBillDetails ID : " + preBillBatchNumber + 
					" and paymentPlanRevisionNo:" + preBillNumber + 
					" and preBillDate:" + preBillDate + 
					" and matterNumber:" + matterNumber + 
					" doesn't exist.");
		}
		return preBillDetails.get();
	}
	
	/**
	 * 
	 * @param preBillNumber
	 * @return
	 */
	public PreBillDetails getPreBillDetails (String preBillNumber) {
		PreBillDetails preBillDetails = preBillDetailsRepository.findByPreBillNumber(preBillNumber);
		if (preBillDetails == null) {
			throw new BadRequestException("The given PreBillDetails ID : " + preBillNumber + 
					" doesn't exist.");
		}
		return preBillDetails;
	}
	
	/**
	 * 
	 * @param searchPreBillDetails
	 * @return
	 * @throws ParseException
	 */
	public List<AddPreBillDetails> findPreBillDetails(SearchPreBillDetails searchPreBillDetails) throws ParseException {
		try {
			if (searchPreBillDetails.getStartPreBillDate() != null && searchPreBillDetails.getEndPreBillDate() != null) {
				Date[] dates = DateUtils.addTimeToDatesForSearch(searchPreBillDetails.getStartPreBillDate(), 
								searchPreBillDetails.getEndPreBillDate());
				searchPreBillDetails.setStartPreBillDate(dates[0]);
				searchPreBillDetails.setEndPreBillDate(dates[1]);
			}
			
			PreBillDetailsSpecification spec = new PreBillDetailsSpecification(searchPreBillDetails);
			List<PreBillDetails> results = preBillDetailsRepository.findAll(spec);
			List<AddPreBillDetails> listAddPreBillDetails = new ArrayList<>();
			for (PreBillDetails preBillDetails : results) {
				AddPreBillDetails newPreBillDetails = new AddPreBillDetails();
				BeanUtils.copyProperties(preBillDetails, newPreBillDetails, CommonUtils.getNullPropertyNames(preBillDetails));
				newPreBillDetails.setStartDateForPreBill(DateUtils.addTimeToDate(preBillDetails.getStartDateForPreBill()));
				newPreBillDetails.setFeesCostCutoffDate(DateUtils.addTimeToDate(preBillDetails.getFeesCostCutoffDate()));
				newPreBillDetails.setPaymentCutoffDate(DateUtils.addTimeToDate(preBillDetails.getPaymentCutoffDate()));
				newPreBillDetails.setPreBillDate(DateUtils.addTimeToDate(preBillDetails.getPreBillDate()));
				
//				log.info("newPreBillDetails------> " + newPreBillDetails.getPreBillDate());
				listAddPreBillDetails.add(newPreBillDetails);
			}
			return listAddPreBillDetails;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param newPreBillDetails
	 * @param loginUserID
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ParseException 
	 */
	public List<AddPreBillDetails> createPreBillDetails (@Valid List<AddPreBillDetails> newPreBillDetails, String loginUserID) 
			throws IllegalAccessException, InvocationTargetException, ParseException {
		Long STATUS_ID = 33L;
		Long STATUS_ID_37 = 37L;
		
		List <AddPreBillDetails> createdAddPreBillDetails = new ArrayList<>();
		
		/*
		 * During Save, Pass CLASS_ID=02, NUM_RAN_CODE=17 in NUMBERRANGE table and Fetch NUM_RAN_CURRENT values and 
		 * add +1 and then insert
		 */
		long classID = 3;
		long NUM_RAN_CODE = 17;
		AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
		String PREBILL_BATCH_NO = setupService.getNextNumberRange(classID, NUM_RAN_CODE, authTokenForSetupService.getAccess_token());
		log.info("nextVal from NumberRange for PREBILL_BATCH_NO: " + PREBILL_BATCH_NO);
		
		int counter = 1;
		for (AddPreBillDetails newPreBillDetail : newPreBillDetails) {
			// Converting Date as CST zone
			Date prebillDate = DateUtils.addTimeToDate(newPreBillDetail.getPreBillDate());
			Date startDate = DateUtils.addTimeToDate(newPreBillDetail.getStartDateForPreBill());
			Date feesCutOffDate = DateUtils.addTimeToDate(newPreBillDetail.getFeesCostCutoffDate());
			Date paymentCutOffDate = DateUtils.addTimeToDate(newPreBillDetail.getPaymentCutoffDate());
			
			log.info ("prebillDate1 : " + prebillDate);
			log.info ("startDate1 : " + startDate);
			log.info ("feesCutOffDate1: " + feesCutOffDate);
			log.info ("paymentCutOffDate1 : " + paymentCutOffDate);
			
			/* -----------------Checking for duplicates ---------------------------------*/
			Date[] startDates = DateUtils.addTimeToDatesForSearch(startDate, startDate);
			Date[] feesCutOffDates = DateUtils.addTimeToDatesForSearch(feesCutOffDate, feesCutOffDate);
			
			List<PreBillDetails> existingRecordList = 
					preBillDetailsRepository.findByMatterNumberAndStatusIdAndStartDateForPreBillBetweenAndFeesCostCutoffDateBetweenAndDeletionIndicator (
							newPreBillDetail.getMatterNumber(), 45L, startDates[0], startDates[1], feesCutOffDates[0], feesCutOffDates[1], 0L);
			if (existingRecordList != null && !existingRecordList.isEmpty()) {
				throw new BadRequestException("Prebill already created for this Matter: " + newPreBillDetail.getMatterNumber());
			}
			
			PreBillDetails dbPreBillDetails = new PreBillDetails();
			BeanUtils.copyProperties(newPreBillDetail, dbPreBillDetails, CommonUtils.getNullPropertyNames(newPreBillDetail));

			// Pre_Bill_Batch_No
			dbPreBillDetails.setPreBillBatchNumber(PREBILL_BATCH_NO);
			
			// Pre_Bill_No
			/*
			 * Concatenate Pr_Bill_Batch_No + "-001", Pre_Bill_Batch_No + "-002", etc continuous number for each matter
			 */
			String PREBILL_NO = PREBILL_BATCH_NO + "-00" + counter++;
			dbPreBillDetails.setPreBillNumber(PREBILL_NO);
			log.info("PREBILL_NO----------> : " + PREBILL_NO);
			
			// During save derive Lang_id value from MatterGenAcc table for selected matter_No
			// Get AuthToken for ManagementService
			AuthToken authTokenForManagementService = authTokenService.getManagementServiceAuthToken();
			MatterGenAcc matterGenAcc = managementService.getMatterGenAcc(newPreBillDetail.getMatterNumber(), 
					authTokenForManagementService.getAccess_token());
						
			// Lang_ID
			dbPreBillDetails.setLanguageId(matterGenAcc.getLanguageId());
			
			// Class ID
			dbPreBillDetails.setClassId(matterGenAcc.getClassId());
			
			// Status_Id - Hardcoded value 45 for first time creating the Pre Bill 
			dbPreBillDetails.setStatusId(45L);
			
			dbPreBillDetails.setPreBillDate(prebillDate);
			dbPreBillDetails.setStartDateForPreBill(startDate);
			dbPreBillDetails.setFeesCostCutoffDate(feesCutOffDate);
			dbPreBillDetails.setPaymentCutoffDate(paymentCutOffDate);
			
			dbPreBillDetails.setMatterNumber(newPreBillDetail.getMatterNumber());
			dbPreBillDetails.setClientId(matterGenAcc.getClientId());
			dbPreBillDetails.setCreatedBy(loginUserID);
			dbPreBillDetails.setCreatedOn(new Date());
			dbPreBillDetails.setUpdatedBy(loginUserID);
			dbPreBillDetails.setUpdatedOn(new Date());
			dbPreBillDetails.setDeletionIndicator(0L);
			
			PreBillDetails createdPreBillDetails = preBillDetailsRepository.save(dbPreBillDetails);
			log.info("------createdPreBillDetails--------> : " + createdPreBillDetails);
			
			//--------------------Matter-TimeTickets--------------------------------------------------------------------
			
			Date[] dates = DateUtils.addTimeToDatesForSearch(createdPreBillDetails.getStartDateForPreBill(), 
					createdPreBillDetails.getFeesCostCutoffDate());
			log.info("------Date[start&Fees]--------> : " + dates[0] + "::" + dates[1]);
			
			List<com.mnrclara.api.accounting.model.prebill.MatterTimeTicket> matterTimeTickets = 
					matterTimeTicketRepository.findByMatterNumberAndStatusIdAndDeletionIndicatorAndTimeTicketDateBetween(newPreBillDetail.getMatterNumber(), 
							STATUS_ID, 0L, dates[0], dates[1]);
//			log.info("------matterTimeTickets--------> : " + matterTimeTickets);
			
			List<com.mnrclara.api.accounting.model.prebill.MatterExpense> matterExpenses = 
					matterExpenseRepository.findByMatterNumberAndStatusIdAndDeletionIndicatorAndReferenceField2Between(newPreBillDetail.getMatterNumber(),
							STATUS_ID_37, 0L, dates[0], dates[1]);
			log.info("------matterExpenses--------> : " + matterExpenses);
			
			// MatterTimeTicket (Update)
			for (com.mnrclara.api.accounting.model.prebill.MatterTimeTicket matterTimeTicket : matterTimeTickets) {
				
				// Status_Id - Update Status_Id=34 for all the time tickets selected in the above query
				matterTimeTicket.setStatusId(34L);
				
				// Ass_Partner - Update Ass_Partner = Value (Partner_Assigned) field from the screen for all the time tickets
				matterTimeTicket.setAssignedPartner(createdPreBillDetails.getPartnerAssigned());
				
				// Ass_On - Update Ass_On = Value (Pre_Bill_Date) field from the screen for all the time tickets
				matterTimeTicket.setAssignedOn(createdPreBillDetails.getPreBillDate());
				
				// Ref_Field_1 - Update Ref_Field_1= Value(Pre_Bill_No) field generated during the save
				matterTimeTicket.setReferenceField1(createdPreBillDetails.getPreBillNumber());

				matterTimeTicket.setApprovedBillableTimeInHours(matterTimeTicket.getTimeTicketHours());
				matterTimeTicket.setApprovedBillableAmount(matterTimeTicket.getTimeTicketAmount());
				matterTimeTicket.setReferenceField4(matterTimeTicket.getTimeTicketDescription());

				com.mnrclara.api.accounting.model.prebill.MatterTimeTicket updatedMatterTimeTicket = 
						matterTimeTicketRepository.save(matterTimeTicket);
				log.info("updatedMatterTimeTicket : " + updatedMatterTimeTicket);
			}
			
			// MatterExpense (Update)
			for (com.mnrclara.api.accounting.model.prebill.MatterExpense matterExpense : matterExpenses) {
				
				// Status_Id - Update Status_Id=34 for all the Expense entries selected in the above query
				matterExpense.setStatusId(34L);
				
				// Ref_Field_1 - Update Ref_Field_1= Value(Pre_Bill_No) field generated during the save
				matterExpense.setReferenceField1(createdPreBillDetails.getPreBillNumber());
				com.mnrclara.api.accounting.model.prebill.MatterExpense updatedMatterExpense = 
						matterExpenseRepository.save(matterExpense);
				log.info("updatedMatterExpense : " + updatedMatterExpense);
			}
			
			AddPreBillDetails addedPreBillDetails = new AddPreBillDetails();
			BeanUtils.copyProperties(createdPreBillDetails, addedPreBillDetails, CommonUtils.getNullPropertyNames(createdPreBillDetails));
			createdAddPreBillDetails.add(addedPreBillDetails);
		}
		return createdAddPreBillDetails;
	}
	
	/**
	 * 
	 * @param preBillBatchNumber
	 * @param preBillNumber
	 * @param preBillDate
	 * @param matterNumber
	 * @param updatePreBillDetails
	 * @param loginUserID
	 * @return 
	 */
	public PreBillDetails updatePreBillDetails(String preBillBatchNumber, String preBillNumber, Date preBillDate,
			String matterNumber, UpdatePreBillDetails updatePreBillDetails, String loginUserID) {
		PreBillDetails dbPreBillDetails = getPreBillDetails(preBillBatchNumber, preBillNumber, preBillDate, matterNumber);
		BeanUtils.copyProperties(updatePreBillDetails, dbPreBillDetails, CommonUtils.getNullPropertyNames(updatePreBillDetails));
		
		dbPreBillDetails.setUpdatedBy(loginUserID);
		dbPreBillDetails.setUpdatedOn(new Date());
		
		PreBillDetails updatedPreBillDetails = preBillDetailsRepository.save(dbPreBillDetails);
		return updatedPreBillDetails;
	}
	
	/**
	 * deletePreBillDetails
	 * @param preBillNumber
	 * @param paymentPlanRevisionNo
	 */
	public void deletePreBillDetails (String preBillNumber, String loginUserID) {
		List<Long> statusList = Arrays.asList(45L, 29L, 51L, 56L);
		List<PreBillDetails> preBillDetails = preBillDetailsRepository.findByPreBillNumberAndStatusIdIn(preBillNumber, statusList);
		
		if ( preBillDetails.isEmpty()) {
			throw new EntityNotFoundException("Error in deleting Id: " + preBillNumber);
		}
		
		for (PreBillDetails preBillDetail : preBillDetails) {
			preBillDetail.setDeletionIndicator(1L);
			preBillDetail.setUpdatedBy(loginUserID);
			preBillDetail.setUpdatedOn(new Date());
			preBillDetailsRepository.save(preBillDetail);
		} 
		
		AuthToken authTokenForManagementService = authTokenService.getManagementServiceAuthToken();
		
		// Obtaining MatterTimeTicket
		List<com.mnrclara.api.accounting.model.prebill.MatterTimeTicket> matterTimeTickets = 
				matterTimeTicketRepository.findByMatterNumberAndReferenceField1OrderByTimeTicketDateAscTimeTicketNumberAsc(preBillDetails.get(0).getMatterNumber(), preBillNumber);
		
		// MatterTimeTicket (Update)
		for (com.mnrclara.api.accounting.model.prebill.MatterTimeTicket matterTimeTicket : matterTimeTickets) {
			
			// Status_Id - Update Status_Id=33 for all the time tickets comes under the Matter_No in selected Pre_Bill_Batch_No
			matterTimeTicket.setStatusId(33L);
			
			// Ass_Partner - Update Ass_Partner = blank for all the time tickets comes under the Matter_No in selected Pre_Bill_Batch_No
			matterTimeTicket.setAssignedPartner(null);
			
			// Ass_On - Update Ass_On = blank for all the time tickets comes under the Matter_No in selected Pre_Bill_Batch_No
			matterTimeTicket.setAssignedOn(null);
			
			// Ref_Field_1 - Update Ref_Field_1= blank for all the time tickets comes under the Matter_No in selected Pre_Bill_Batch_No
			matterTimeTicket.setReferenceField1(null);
			
			matterTimeTicket.setApprovedBillableAmount(null);
			matterTimeTicket.setApprovedBillableTimeInHours(null);
			
			MatterTimeTicket updatedMatterTimeTicket = 
					managementService.updateMatterTimeTicket(matterTimeTicket.getTimeTicketNumber(), loginUserID, matterTimeTicket, 
					authTokenForManagementService.getAccess_token());
			log.info("updatedMatterTimeTicket : " + updatedMatterTimeTicket);
		}
		
		// Obtaining Matter Expense
		List<com.mnrclara.api.accounting.model.prebill.MatterExpense> matterExpenses = 
				matterExpenseRepository.findByMatterNumberAndReferenceField1(preBillDetails.get(0).getMatterNumber(), preBillNumber);
		
		// MatterExpense (Update)
		for (com.mnrclara.api.accounting.model.prebill.MatterExpense matterExpense : matterExpenses) {
			log.info("\n MatterExpense : " + matterExpense.getMatterExpenseId());
			// Status_Id - Update Status_Id=37 for all the Expense entries comes under the Matter_No for selected Pre_Bill_Batch_No
			matterExpense.setStatusId(37L);
			
			// Ref_Field_1 - Update Ref_Field_1=  for all the Expense entries comes under the Matter_No for selected Pre_Bill_Batch_No
			matterExpense.setReferenceField1(null);
			
			MatterExpense updatedMatterExpense = managementService.updateMatterExpense(matterExpense.getMatterExpenseId(), loginUserID, 
					matterExpense, authTokenForManagementService.getAccess_token());
			log.info("updatedMatterExpense : " + updatedMatterExpense);
		}
	}
	
	/**
	 * 
	 * @param searchMatterGeneral
	 * @return
	 */
	public List<com.mnrclara.api.accounting.model.prebill.MatterGenAcc> searchMatterGeneral (SearchMatterGeneral searchMatterGeneral) {
		MatterGenAccSpecification spec = new MatterGenAccSpecification(searchMatterGeneral);
		List<com.mnrclara.api.accounting.model.prebill.MatterGenAcc> searchResults = matterGenAccRepository.findAll(spec);
		return searchResults;
	}
	
	/**
	 * executeBill
	 * @param newBillByGroup
	 * @param isByIndividual 
	 * @return
	 * @throws ParseException 
	 */
	public MatterTimeExpenseTicket[] executeBill(BillByGroup newBillByGroup, Boolean isByIndividual) throws ParseException {
		List<String> matterNumbers = null;
		
		// If not billByIndividual
		if (!isByIndividual) {
			/*
			 * Only if MatterNumbers are NULL, the below logic will execute to find out the MatterNumbers 
			 * from MatterTimeTickets Table
			 */
			if (newBillByGroup.getMatterNumber() == null) {
				Date[] dates = DateUtils.addTimeToDatesForSearch(newBillByGroup.getStartDate(), newBillByGroup.getFeesCutoffDate());
		
				// Getting MatterNumbers based on TimeTicketDate from MatterTimeTickets table
				List<com.mnrclara.api.accounting.model.prebill.MatterTimeTicket> matterTimeTickets = 
						matterTimeTicketRepository.findByTimeTicketDateBetween(dates[0], dates[1]);
				log.info("----executeBill---group----matterTimeTickets------> : " + matterTimeTickets );
				
				// Filtering out only MatterNumbers
				matterNumbers = matterTimeTickets.stream()
								.map(com.mnrclara.api.accounting.model.prebill.MatterTimeTicket::getMatterNumber)
								.collect(Collectors.toList());
				log.info("----executeBill---group----matterNumbers------> : " + matterTimeTickets );
			}
			
			// Retrieving Matter General details
			newBillByGroup.setMatterGeneral(searchMatterGeneral (newBillByGroup, matterNumbers));
			
			// Get AuthToken for ManagementService
			AuthToken authTokenForManagementService = authTokenService.getManagementServiceAuthToken();
			MatterTimeExpenseTicket[] arrMmatterCountAndSum = 
					managementService.getMatterTimeNExpenseTicketsByGroup(newBillByGroup, authTokenForManagementService.getAccess_token());
			return arrMmatterCountAndSum;
		} else {
			matterNumbers = newBillByGroup.getMatterNumber();
			
			// Retrieving Matter General details
			newBillByGroup.setMatterGeneral(searchMatterGeneral (newBillByGroup, matterNumbers));
			
			// Get AuthToken for ManagementService
			AuthToken authTokenForManagementService = authTokenService.getManagementServiceAuthToken();
			MatterTimeExpenseTicket[] arrMmatterCountAndSum = 
					managementService.getMatterTimeNExpenseTicketsByIndividual(newBillByGroup, authTokenForManagementService.getAccess_token());
			return arrMmatterCountAndSum;
		}
	}
	
	/**
	 * 
	 * @param preBillBatchNumber
	 * @param preBillNumber
	 * @param matterNumber
	 * @param timeTicketNumber 
	 * @param approvedBillTime
	 * @param approvedBillAmount
	 * @param approvedDescription
	 * @param loginUserID
	 * @return
	 */
	public List<AddPreBillDetails> approvePreBillDetails(List<PreBillApproveSaveDetails> preBillApproveSaveDetails , String loginUserId) {
		log.info("preBillApproveSaveDetails -----> " + preBillApproveSaveDetails);
		
		/*
		 * Once click the Approve button update the below fields in PreBillDetails,
		 * MatterTimeTicket and MatterExpense table
		 */
		List<AddPreBillDetails> addedPreBillDetails = new ArrayList<>();
		for(PreBillApproveSaveDetails preBillApproveData : preBillApproveSaveDetails) {
			try {
				/*
				 * Matter Expense Update
				 */
				List<com.mnrclara.api.accounting.model.prebill.MatterExpense> dbMatterExpenseList =
						matterExpenseRepository.findByReferenceField1(preBillApproveData.getPreBillNumber());
				log.info("dbMatterExpenseList -----> " + dbMatterExpenseList);
				
//				double deliveryLinesCount = 0;
//				if (dbMatterExpenseList != null && dbMatterExpenseList.size() > 0){
//					deliveryLinesCount = dbMatterExpenseList.stream().mapToDouble(data->data.getExpenseAmount()).sum();
//				}
				
				for (com.mnrclara.api.accounting.model.prebill.MatterExpense expense : dbMatterExpenseList) {
					// Update Status_Id = 46 for the respective Expense Number from the list
					expense.setStatusId(46L);
					matterExpenseRepository.save(expense);
				}
				
				log.info("dbMatterExpenseList -----> saved---------");
				
				if (preBillApproveData.getMatterNumber() != null) {
					List<PreBillDetails> dbPreBillDetailsList =
							preBillDetailsRepository.findByPreBillNumberAndMatterNumberAndDeletionIndicator(preBillApproveData.getPreBillNumber(), 
									preBillApproveData.getMatterNumber(), 0L);
					log.info("dbPreBillDetailsList -----> : " + dbPreBillDetailsList);
					
					for (PreBillDetails preBillDetails : dbPreBillDetailsList) {
						// Update Status_Id=29 for the selected Matter_No and Pre_Bill_No from the list
						preBillDetails.setStatusId(29L);
						
						String referenceField5 = preBillDetails.getReferenceField5();
						Double approvedBillAmount = preBillApproveData.getApprovedBillAmount();
						if (referenceField5 == null) {
							referenceField5 = "0";
						}
						if (approvedBillAmount == null) {
							approvedBillAmount = 0D;
						}
						
						// ----Need to remove after Testing
//						preBillDetails.setReferenceField5(
//								(referenceField5 != null) ? String.valueOf(Double.sum(Double.valueOf(referenceField5), 
//										approvedBillAmount)) : String.valueOf(Double.sum(deliveryLinesCount, approvedBillAmount)));
						
						preBillDetails = preBillDetailsRepository.save(preBillDetails);
						log.info(" Updated preBillDetails : " + preBillDetails);
					}
				}
				
				/*
				 * Matter Timeticket Update
				 */
				if (preBillApproveData.getTimeTicketNumber() != null) {
					List<com.mnrclara.api.accounting.model.prebill.MatterTimeTicket> dbMatterTimeTickets =
							matterTimeTicketRepository.findByTimeTicketNumber(preBillApproveData.getTimeTicketNumber());
					for (com.mnrclara.api.accounting.model.prebill.MatterTimeTicket dbTimeTicket : dbMatterTimeTickets) {
						// Update Status_Id = 46 for the respective time ticket from the list
						dbTimeTicket.setStatusId(46L);

						// App_Bill_Time
						dbTimeTicket.setApprovedBillableTimeInHours(preBillApproveData.getApprovedBillTime());

						// App_Amount
						dbTimeTicket.setApprovedBillableAmount(preBillApproveData.getApprovedBillAmount());

						// Ref_Field_4 - Approved Description
						dbTimeTicket.setReferenceField4(preBillApproveData.getApprovedDescription());

						dbTimeTicket.setBillType(preBillApproveData.getBillType());
						
						//--------------------------CR-------------------------------------
						dbTimeTicket.setActivityCode(preBillApproveData.getActivityCode());
						dbTimeTicket.setTaskCode(preBillApproveData.getTaskCode());
						dbTimeTicket = matterTimeTicketRepository.save(dbTimeTicket);
						log.info("Updated MatterTimeTicket : " + dbTimeTicket);
					}
				}
				
				// Sum of app_bill_amount from mattertimeticket + sum of expense_amt from matterexpense by prebillnumber in ref_field_1
				List<PreBillDetails> createdPreBillDetailsList =
						preBillDetailsRepository.findByPreBillNumberAndMatterNumberAndDeletionIndicator(preBillApproveData.getPreBillNumber(), 
								preBillApproveData.getMatterNumber(), 0L);
				log.info("----------1.0--------createdPreBillDetailsList-----" + createdPreBillDetailsList);
				
				createdPreBillDetailsList.stream().forEach(p -> {
					Double appBillAmount = matterTimeTicketRepository.getTimeTicketBillAmount (preBillApproveData.getPreBillNumber());
					Double expenseAmount = matterExpenseRepository.getExpenseAmount(preBillApproveData.getPreBillNumber());
					
					if (appBillAmount == null) {
						appBillAmount = 0D;
					}
					
					if (expenseAmount == null) {
						expenseAmount = 0D;
					}
					log.info("----------1.0.1--------setReferenceField5-----" + appBillAmount + "," + expenseAmount);
					Double total = appBillAmount + expenseAmount;
					log.info("----------1.0.2--------total-----" + total);
					p.setReferenceField5(String.valueOf(total));
				});
				
				for (PreBillDetails p : createdPreBillDetailsList) {
					AddPreBillDetails addPreBillDetails = new AddPreBillDetails();
					BeanUtils.copyProperties(p, addPreBillDetails, CommonUtils.getNullPropertyNames(p));
					addedPreBillDetails.add(addPreBillDetails);
				}
				
				log.info("----------1.1--------addedPreBillDetails-----updated------" + addedPreBillDetails);
				log.info("----------1.2-------------------");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return addedPreBillDetails;
	}

	/**
	 * Save Changes
	 * @param preBillNumber
	 * @param matterNumber
	 * @param timeTicketNumber
	 * @param approvedBillTime
	 * @param approvedBillAmount
	 * @param approvedDescription
	 * @param loginUserID
	 * @return
	 */
	public List<AddPreBillDetails> savePreBillDetails(List<PreBillApproveSaveDetails> preBillApproveSaveDetails , String loginUserId) {
		/*
		 * Once click the Approve button update the below fields in PreBillDetails,
		 * MatterTimeTicket and MatterExpense table
		 */
		List<AddPreBillDetails> addedPreBillDetails = new ArrayList<>();

		for(PreBillApproveSaveDetails preBillApproveData : preBillApproveSaveDetails) {
			log.info("preBillNumber, matterNumber -----> : " + preBillApproveData.getPreBillNumber() + "," + preBillApproveData.getMatterNumber());

			List<PreBillDetails> dbPreBillDetailsList =
					preBillDetailsRepository.findByPreBillNumberAndMatterNumberAndDeletionIndicator(preBillApproveData.getPreBillNumber(), preBillApproveData.getMatterNumber(), 0L);
			log.info("dbPreBillDetailsList -----> : " + dbPreBillDetailsList);

			List<com.mnrclara.api.accounting.model.prebill.MatterExpense> dbMatterExpenseList =
					matterExpenseRepository.findByReferenceField1(preBillApproveData.getPreBillNumber());

			for (PreBillDetails preBillDetails : dbPreBillDetailsList) {
				// Update Status_Id=29 for the selected Matter_No and Pre_Bill_No from the list
				preBillDetails.setStatusId(56L);
				preBillDetails = preBillDetailsRepository.save(preBillDetails);
				log.info(" Updated preBillDetails : " + preBillDetails);
			}
			
			/*
			 * Matter Timeticket Update
			 */
			List<com.mnrclara.api.accounting.model.prebill.MatterTimeTicket> dbMatterTimeTickets =
					matterTimeTicketRepository.findByTimeTicketNumber(preBillApproveData.getTimeTicketNumber());
			for (com.mnrclara.api.accounting.model.prebill.MatterTimeTicket dbTimeTicket : dbMatterTimeTickets) {
				// Update Status_Id = 46 for the respective time ticket from the list
				dbTimeTicket.setStatusId(56L);

				// App_Bill_Time
				dbTimeTicket.setApprovedBillableTimeInHours(preBillApproveData.getApprovedBillTime());

				// App_Amount
				dbTimeTicket.setApprovedBillableAmount(preBillApproveData.getApprovedBillAmount());

				// Ref_Field_4 - Approved Description
				dbTimeTicket.setReferenceField4(preBillApproveData.getApprovedDescription());

				dbTimeTicket.setBillType(preBillApproveData.getBillType());
				
				//----------CR-----------------------------------------------
				dbTimeTicket.setActivityCode(preBillApproveData.getActivityCode());
				dbTimeTicket.setTaskCode(preBillApproveData.getTaskCode());

				dbTimeTicket = matterTimeTicketRepository.save(dbTimeTicket);
				log.info("Updated MatterTimeTicket : " + dbTimeTicket);
			}

			/*
			 * Matter Expense Update
			 */
			for (com.mnrclara.api.accounting.model.prebill.MatterExpense expense : dbMatterExpenseList) {
				// Update Status_Id=46 for the respective Expense Number from the list
				expense.setStatusId(56L);
				matterExpenseRepository.save(expense);
			}
			
			// Sum of app_bill_amount from mattertimeticket + sum of expense_amt from matterexpense by prebillnumber in ref_field_1
			List<PreBillDetails> createdPreBillDetailsList =
					preBillDetailsRepository.findByPreBillNumberAndMatterNumberAndDeletionIndicator(preBillApproveData.getPreBillNumber(), 
							preBillApproveData.getMatterNumber(), 0L);
			log.info("--------1.0-------createdPreBillDetailsList---------> " + createdPreBillDetailsList);;
			createdPreBillDetailsList.stream().forEach(p -> {
				Double appBillAmount = matterTimeTicketRepository.getTimeTicketBillAmount (preBillApproveData.getPreBillNumber());
				Double expenseAmount = matterExpenseRepository.getExpenseAmount(preBillApproveData.getPreBillNumber());
				log.info("--------1.1-------appBillAmount,expenseAmount---------> " + appBillAmount + "," + expenseAmount);
				
				if (appBillAmount == null) {
					appBillAmount = 0D;
				}
				
				if (expenseAmount == null) {
					expenseAmount = 0D;
				}
				log.info("----------1.0.1--------setReferenceField5-----" + appBillAmount + "," + expenseAmount);
				Double total = appBillAmount + expenseAmount;
				log.info("----------1.0.2--------total-----" + total);
				p.setReferenceField5(String.valueOf(total));
			});
			
			for (PreBillDetails p : createdPreBillDetailsList) {
				AddPreBillDetails addPreBillDetails = new AddPreBillDetails();
				BeanUtils.copyProperties(p, addPreBillDetails, CommonUtils.getNullPropertyNames(p));
				addedPreBillDetails.add(addPreBillDetails);
			}
		}
		return addedPreBillDetails;
	}
	
	/**
	 * 
	 * @param newBillByGroup
	 * @param matterNumbers
	 * @return
	 */
	private List<com.mnrclara.api.accounting.model.prebill.MatterGenAcc> searchMatterGeneral (BillByGroup newBillByGroup, 
			List<String> matterNumbers) {
		SearchMatterGeneral searchMatterGeneral = new SearchMatterGeneral();
		BeanUtils.copyProperties(newBillByGroup, searchMatterGeneral, CommonUtils.getNullPropertyNames(newBillByGroup));
		
		if (matterNumbers != null) { // If the MatterNumber did not provide then MatterNumbers are setting it here explicitly
			searchMatterGeneral.setMatterNumber(matterNumbers);
		}
		
		List<com.mnrclara.api.accounting.model.prebill.MatterGenAcc> matterGeneralList = searchMatterGeneral(searchMatterGeneral);
		log.info("\n----searchMatterGeneral::------> " + matterGeneralList);
		return matterGeneralList;
	}

	@Async
	public void sendNotification(AddPreBillDetails preBillDetails,String topic){
		log.info("Send notification to CRM service for PreBill creation : " + preBillDetails);
		AuthToken authTokenForCRMService = authTokenService.getCrmServiceAuthToken();
		String message = "A new prebill for matter number " + preBillDetails.getMatterNumber() + " has been assigned to you on ";
		String userId = preBillDetails.getPartnerAssigned();
		Date createdOn = preBillDetails.getCreatedOn();
		String createdby = preBillDetails.getCreatedBy();
		if(topic.equals("Prebill Approve")){
			message = "Prebill for matter number " + preBillDetails.getMatterNumber() + " has been approved on ";
			userId = preBillDetails.getCreatedBy();
			createdOn = preBillDetails.getUpdatedOn();
			createdby = preBillDetails.getUpdatedBy();
		}
		crmService.setNotificationMessage (
				topic,message,
				userId != null ? Arrays.asList(userId) : new ArrayList<>(),null,
				createdOn,createdby,
				authTokenForCRMService.getAccess_token());
	}
	
	/**
	 * getNextPrebillNo
	 * @param preBillBatchNo
	 * @param existingPreBillDetails
	 * @return
	 */
//	private String getNextPrebillNo (String preBillBatchNo, List<PreBillDetails> existingPreBillDetails) {
//		log.info("existingPreBillDetails-----1----> : " + existingPreBillDetails);
//		if (existingPreBillDetails.isEmpty()) {
//			// Generate new preBillBatchNo
//			return preBillBatchNo + "-001";
//		} else {
//			// Increment PrebillNo by 1
//			List<Integer> prebillSubnumbers = new ArrayList<>();
//			for (PreBillDetails preBillDetails : existingPreBillDetails) {
//				if (preBillDetails != null && preBillDetails.getPreBillNumber() != null) {
//					String[] sSplitted = preBillDetails.getPreBillNumber().split("-");
//					prebillSubnumbers.add(Integer.valueOf(sSplitted[1]));
//				}
//			}
//			Collections.sort(prebillSubnumbers);
//			int maxNo = prebillSubnumbers.get(prebillSubnumbers.size() - 1);
//			maxNo++;
//			if (maxNo < 10) {
//				log.info("Max PreBill No : " + preBillBatchNo + "-00" + maxNo);
//				return preBillBatchNo + "-00" + maxNo;
//			} else {
//				log.info("Max PreBill No : " + preBillBatchNo + "-0" + maxNo);
//				return preBillBatchNo + "-0" + maxNo;
//			}
//		}
//	}
}
