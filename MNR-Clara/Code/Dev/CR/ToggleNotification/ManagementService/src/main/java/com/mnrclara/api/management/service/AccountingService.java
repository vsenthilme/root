package com.mnrclara.api.management.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.model.dto.BillByGroup;
import com.mnrclara.api.management.model.dto.IMatterExpenseCountAndSum;
import com.mnrclara.api.management.model.dto.IMatterTimeTicketCountAndSum;
import com.mnrclara.api.management.model.dto.MatterTimeExpenseTicket;
import com.mnrclara.api.management.model.matterexpense.MatterExpense;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.model.mattertimeticket.MatterTimeTicket;
import com.mnrclara.api.management.repository.MatterExpenseRepository;
import com.mnrclara.api.management.repository.MatterGenAccRepository;
import com.mnrclara.api.management.repository.MatterTimeTicketRepository;
import com.mnrclara.api.management.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountingService {

	@Autowired
	CommonService commonService;

	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	private MatterGenAccRepository matterGenAccRepository;
	
	@Autowired
	private MatterTimeTicketRepository matterTimeTicketRepository;
	
	@Autowired
	private MatterExpenseRepository matterExpenseRepository;
	
	private static final String BILLMODE_HOURLY = "1";
	private static final String BILLMODE_FLATFEE = "2";
	private static final String BILLMODE_CONTIGENCY = "3";

	/**
	 * 
	 * @param billByGroup
	 * @return
	 * @throws ParseException 
	 */
	public List<MatterTimeExpenseTicket> getMatterTimeNExpenseTicketsByIndividual(BillByGroup billByGroup) throws ParseException {
		log.info("Mgmt-AccService : getMatterTimeNExpenseTicketsByIndividual: " + billByGroup);
		List<MatterTimeExpenseTicket> matterTimeExpenseTickets = new ArrayList<>();
		List<MatterGenAcc> matterGenAccs = billByGroup.getMatterGeneral();
		
		Date[] dates = DateUtils.addTimeToDatesForSearch(billByGroup.getStartDate(), billByGroup.getFeesCutoffDate());
		Date startDate = dates[0];
		Date feesCutoffDate = dates[1];
		
		log.info("startDate-------->: " + startDate);
		log.info("feesCutoffDate-------->: " + feesCutoffDate);
		
		Long statusId = 30L;
		
		// Filtering matterNumbers
		List<String> matterNumber = matterGenAccs.stream().map(MatterGenAcc::getMatterNumber).collect(Collectors.toList());
		
		// classId = 1, billmodeId = 'Hourly', selected MatterNumber and StatusId != 30
		Long classId = 1L;
		List<String> billingModeId = Arrays.asList(BILLMODE_HOURLY, BILLMODE_FLATFEE);
		List<MatterGenAcc> hourlyByIndividualMatterGenerals = 
				matterGenAccRepository.findByClassIdAndBillingModeIdInAndMatterNumberInAndDeletionIndicator(classId, billingModeId, matterNumber, 0L);
		
		if (!hourlyByIndividualMatterGenerals.isEmpty()) {
			List<IMatterTimeTicketCountAndSum> matterTimeTicketsCountSum = 
					matterTimeTicketRepository.findCountAndSumOfTimeTicketsByIndividual(matterNumber, startDate, feesCutoffDate);
			List<IMatterExpenseCountAndSum> matterExpenseTicketsCountSum = 
					matterExpenseRepository.findExpenseByIndividual(matterNumber, startDate, feesCutoffDate);
			
			// Selecting TimeTicket Records
			List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets = 
					matterTimeTicketRepository.findTimeTicketsByIndividual(matterNumber, startDate, feesCutoffDate);
			log.info("hourlyByIndividualMatterGenerals:-------> " + matterTimeTickets);
			
			// Selecting Expense Records
			List<com.mnrclara.api.management.model.dto.IMatterExpense> expenseTickets = 
					matterExpenseRepository.findExpenseRecordsByIndividual(matterNumber, startDate, feesCutoffDate);
			
			matterTimeExpenseTickets = calculateTotalAmount(billByGroup.getPreBillDate(), matterTimeTicketsCountSum, 
					matterExpenseTicketsCountSum, matterTimeTickets, expenseTickets, matterNumber);
			log.info("Finalized--List-------> : " + matterTimeExpenseTickets);
		}
		
		// classId = 1, billmodeId = 'Contingency' selected MatterNumber and StatusId = 30
		String billmodeId = BILLMODE_CONTIGENCY;
		List<MatterGenAcc> contigencyByIndividualMatterGenerals = 		// If Status_ID in (30) 
				matterGenAccRepository.findByClassIdAndBillingModeIdAndStatusIdAndMatterNumberInAndDeletionIndicator(classId, 
						billmodeId, statusId, matterNumber, 0L);
		log.info("contigencyByIndividualMatterGenerals:-------> " + contigencyByIndividualMatterGenerals);
				
		if (!contigencyByIndividualMatterGenerals.isEmpty()) {
			List<IMatterTimeTicketCountAndSum> matterTimeTicketsCountSum = 
					matterTimeTicketRepository.findCountAndSumOfTimeTicketsByIndividual(matterNumber, startDate, feesCutoffDate);
			List<IMatterExpenseCountAndSum> matterExpenseTicketsCountSum = 
					matterExpenseRepository.findExpenseByIndividual(matterNumber, startDate, feesCutoffDate);
			// Selecting TimeTicket Records
			List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets = 
					matterTimeTicketRepository.findTimeTicketsByIndividual(matterNumber, startDate, feesCutoffDate);
			
			// Selecting Expense Records
			List<com.mnrclara.api.management.model.dto.IMatterExpense> expenseTickets = 
					matterExpenseRepository.findExpenseRecordsByIndividual(matterNumber, startDate, feesCutoffDate);
			
			matterTimeExpenseTickets = calculateTotalAmount(billByGroup.getPreBillDate(), matterTimeTicketsCountSum, 
					matterExpenseTicketsCountSum, matterTimeTickets, expenseTickets, matterNumber);
			log.info("Finalized--List-------> : " + matterTimeExpenseTickets);
		}
			
		// classId = 2, billmodeId = 'Hourly', 'Flat_Fees', selected MatterNumber and StatusId <> 1 & 30
		classId = 2L;
		List<String> billingModeIds = Arrays.asList(BILLMODE_HOURLY, BILLMODE_FLATFEE);
//		List<Long> statusIds = Arrays.asList(1L, 30L);
		List<MatterGenAcc> hourlyByIndividualMatterGeneralsForClass2 = 
				matterGenAccRepository.findByClassIdAndBillingModeIdInAndMatterNumberInAndDeletionIndicator(
				classId, billingModeIds, matterNumber, 0L);
		log.info("hourlyByIndividualMatterGeneralsForClass2:--2-----> " + hourlyByIndividualMatterGeneralsForClass2);
		
		if (!hourlyByIndividualMatterGeneralsForClass2.isEmpty()) {
			List<IMatterTimeTicketCountAndSum> matterTimeTicketsCountAndSum = 
					matterTimeTicketRepository.findCountAndSumOfTimeTicketsByIndividual(matterNumber, startDate, feesCutoffDate);
			List<IMatterExpenseCountAndSum> matterExpenseTicketsCountAndSum = 
					matterExpenseRepository.findExpenseByIndividual(matterNumber, startDate, feesCutoffDate);
			log.info("matterTimeTickets:--Individual-----> " + matterTimeTicketsCountAndSum);
			log.info("matterExpenseTickets:--Individual-----> " + matterExpenseTicketsCountAndSum);
			
			// Selecting TimeTicket Records
			List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets = 
					matterTimeTicketRepository.findTimeTicketsByIndividual(matterNumber, startDate, feesCutoffDate);
			
			// Selecting Expense Records
			List<com.mnrclara.api.management.model.dto.IMatterExpense> expenseTickets = 
					matterExpenseRepository.findExpenseRecordsByIndividual(matterNumber, startDate, feesCutoffDate);
			
			matterTimeExpenseTickets = calculateTotalAmount(billByGroup.getPreBillDate(), matterTimeTicketsCountAndSum, 
					matterExpenseTicketsCountAndSum, matterTimeTickets, expenseTickets, matterNumber);
			log.info("Finalized--List-------> : " + matterTimeExpenseTickets);
		}
		
		// classId = 2, billmodeId = 'Contingency' selected MatterNumber and StatusId = 30
		billmodeId = BILLMODE_CONTIGENCY;
		List<MatterGenAcc> contigencyByIndividualMatterGeneralsForClass2 = 		// If Status_ID in (30) 
				matterGenAccRepository.findByClassIdAndBillingModeIdAndStatusIdAndMatterNumberInAndDeletionIndicator(classId, 
						billmodeId, statusId, matterNumber, 0L);
		log.info("contigencyByIndividualMatterGenerals:-------> " + contigencyByIndividualMatterGenerals);
				
		if (!contigencyByIndividualMatterGeneralsForClass2.isEmpty()) {
			List<IMatterTimeTicketCountAndSum> matterTimeTicketsCountAndSum = 
					matterTimeTicketRepository.findCountAndSumOfTimeTicketsByIndividual(matterNumber, startDate, feesCutoffDate);
			List<IMatterExpenseCountAndSum> matterExpenseTicketsCountAndSum = 
					matterExpenseRepository.findExpenseByIndividual(matterNumber, startDate, feesCutoffDate);
			
			// Selecting TimeTicket Records
			List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets = 
					matterTimeTicketRepository.findTimeTicketsByIndividual(matterNumber, startDate, feesCutoffDate);
			
			// Selecting Expense Records
			List<com.mnrclara.api.management.model.dto.IMatterExpense> expenseTickets = 
					matterExpenseRepository.findExpenseRecordsByIndividual(matterNumber, startDate, feesCutoffDate);
						
			matterTimeExpenseTickets = calculateTotalAmount(billByGroup.getPreBillDate(), matterTimeTicketsCountAndSum,
					matterExpenseTicketsCountAndSum, matterTimeTickets, expenseTickets, matterNumber);
			log.info("Finalized--List-------> : " + matterTimeExpenseTickets);
		}
		
		return matterTimeExpenseTickets;
	}
	
	/**
	 * 
	 * @param billByGroup
	 * @return
	 */
	public List<MatterTimeExpenseTicket> getMatterTimeNExpenseTicketsByGroup(BillByGroup billByGroup) {
		List<MatterTimeExpenseTicket> matterTimeExpenseTickets = new ArrayList<>();
		List<MatterGenAcc> matterGenAccs = billByGroup.getMatterGeneral();
		Date startDate = billByGroup.getStartDate();
		Date feesCutoffDate = billByGroup.getFeesCutoffDate();
		
		if (billByGroup.getFeesCutoffDate() != null) {
			Date[] dates = null;
			try {
				dates = DateUtils.addTimeToDatesForSearch(billByGroup.getStartDate(), billByGroup.getFeesCutoffDate());
			} catch (ParseException e) {
			} 
			startDate = dates[0];
			feesCutoffDate = dates[1];
		}
		
		log.info("startDate: " + startDate);
		log.info("feesCutoffDate : " + feesCutoffDate);
		log.info("billByGroup.getClassId() : " + billByGroup.getClassId());
		log.info("billByGroup.getBillingMode() : " + billByGroup.getBillingMode());
		log.info("billByGroup.getStartDate() : " + billByGroup.getStartDate());
		log.info("billByGroup.getFeesCutoffDate() : " + billByGroup.getFeesCutoffDate());
		
		List<String> originatingTimeKeeper = billByGroup.getOriginatingTimeKeeper();
		List<String> responsibleTimeKeeper = billByGroup.getResponsibleTimeKeeper();
		List<String> assignedTimeKeeper = billByGroup.getAssignedTimeKeeper();
		Long statusId = 30L;
		
		// Check Inputs for avoiding query execution error
		if (originatingTimeKeeper == null) {
			originatingTimeKeeper = new ArrayList<>();
		}
		
		if (responsibleTimeKeeper == null) {
			responsibleTimeKeeper = new ArrayList<>();
		}
		
		if (assignedTimeKeeper == null) {
			assignedTimeKeeper = new ArrayList<>();
		}
		
		// Filtering matterNumbers
		List<String> matterNumbers = matterGenAccs.stream().map(MatterGenAcc::getMatterNumber).collect(Collectors.toList());
		
		/*-------------------------------------------------------------------------------*/
		// classId = 1, billmodeId = 'Hourly', selected MatterNumber and StatusId != 30
		/*-------------------------------------------------------------------------------*/
//		Long classId = 1L;
		List<Long> classIdList = billByGroup.getClassId(); // Input value
		Long classId = classIdList.get(0);
		if (classId == 1L) {
			List<String> billingModeId = Arrays.asList(BILLMODE_HOURLY, BILLMODE_FLATFEE);
			List<Long> inputBillModeId = billByGroup.getBillingMode();
			if (inputBillModeId != null && inputBillModeId.size() == 1 && inputBillModeId.contains(1L)) {
				billingModeId = Arrays.asList(BILLMODE_HOURLY);
			} else if (inputBillModeId != null && inputBillModeId.size() == 1 && inputBillModeId.contains(2L)) {
				billingModeId = Arrays.asList(BILLMODE_FLATFEE);
			} else if (inputBillModeId != null && inputBillModeId.size() > 1 && (inputBillModeId.contains(1L) && inputBillModeId.contains(2L))) {
				billingModeId = Arrays.asList(BILLMODE_HOURLY, BILLMODE_FLATFEE);
			} else if (inputBillModeId == null) {
				billingModeId = Arrays.asList(BILLMODE_HOURLY, BILLMODE_FLATFEE);
			}
			
			log.info("----------billingModeId---------1--------> " + billingModeId);
			
			List<MatterGenAcc> hourlyByGroupMatterGenerals = null;
			if (matterNumbers != null && !matterNumbers.isEmpty()) {
				hourlyByGroupMatterGenerals = matterGenAccRepository.findByClassIdAndBillingModeIdInAndMatterNumberInAndDeletionIndicator(classId, 
								billingModeId, matterNumbers, 0L);
			} else {
				hourlyByGroupMatterGenerals = matterGenAccRepository.findByClassIdAndBillingModeIdInAndDeletionIndicator(classId, billingModeId, 0L);
			}
			
			if (!hourlyByGroupMatterGenerals.isEmpty()) {
				log.info("hourlyByGroupMatterGenerals:-------> " + hourlyByGroupMatterGenerals.size());
				List<String> matterNumbersFiltered = hourlyByGroupMatterGenerals.stream().map(MatterGenAcc::getMatterNumber).collect(Collectors.toList());
				List<String[]> matterTimeTicketsCountAndSum = 
						matterTimeTicketRepository.findCountAndSumOfTimeTicketsByGroup1 (matterNumbersFiltered, startDate, feesCutoffDate, originatingTimeKeeper, responsibleTimeKeeper, assignedTimeKeeper);
				List<String[]> matterExpenseTicketsCountAndSum = 
						matterExpenseRepository.findExpenseByGroup1(matterNumbersFiltered, startDate, feesCutoffDate, originatingTimeKeeper, responsibleTimeKeeper, assignedTimeKeeper);
				
				// Select MatterTimeTickets by Group
				// Filtering all matched matched MatterNumbers
//				List<String> filteredMatterNumbers = new ArrayList<>();
//				if (matterTimeTicketsCountAndSum !=  null && !matterTimeTicketsCountAndSum.isEmpty()) {
//					for (String[] imatter : matterTimeTicketsCountAndSum) {
//						filteredMatterNumbers.add(imatter[2]);
//					}
//				} else {
//					filteredMatterNumbers.addAll(matterNumbers);
//				}
				
				List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets = 
						matterTimeTicketRepository.findTimeTicketsByGroup (matterNumbers, startDate, feesCutoffDate);
				
				// Select MatterExpense by Group
				List<com.mnrclara.api.management.model.dto.IMatterExpense> expenseTickets = 
						matterExpenseRepository.findExpenseRecordsByGroup(matterNumbers, startDate, feesCutoffDate);
				matterTimeExpenseTickets = calculateTotalAmount1(billByGroup.getPreBillDate(), matterTimeTicketsCountAndSum, 
						matterExpenseTicketsCountAndSum, matterTimeTickets, expenseTickets, matterNumbers);
				
//				List<String> matterNumbersFiltered = hourlyByGroupMatterGenerals.stream().map(MatterGenAcc::getMatterNumber).collect(Collectors.toList());
//				List<IMatterTimeTicketCountAndSum> matterTimeTicketsCountAndSum = 
//						matterTimeTicketRepository.findCountAndSumOfTimeTicketsByGroup(matterNumbersFiltered, startDate, feesCutoffDate, originatingTimeKeeper, responsibleTimeKeeper, assignedTimeKeeper);
//				List<IMatterExpenseCountAndSum> matterExpenseTicketsCountAndSum = 
//						matterExpenseRepository.findExpenseByGroup(matterNumbersFiltered, startDate, feesCutoffDate, originatingTimeKeeper, 
//								responsibleTimeKeeper, assignedTimeKeeper);
//				
//				// Select MatterTimeTickets by Group
//				// Filtering all matched matched MatterNumbers
//				List<String> matterNumbers = new ArrayList<>();
//				for (IMatterTimeTicketCountAndSum imatter : matterTimeTicketsCountAndSum) {
//					matterNumbers.add(imatter.getMatterNumber());
//				}
//	//			log.info("matterNumbers---------> : " + matterNumbers);
//				
//				List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets = 
//						matterTimeTicketRepository.findTimeTicketsByGroup (filteredMatterNumbers, startDate, feesCutoffDate);
//				
//				// Select MatterExpense by Group
//				List<com.mnrclara.api.management.model.dto.IMatterExpense> expenseTickets = 
//						matterExpenseRepository.findExpenseRecordsByGroup(filteredMatterNumbers, startDate, feesCutoffDate);
//				
//				matterTimeExpenseTickets = calculateTotalAmount(billByGroup.getPreBillDate(), matterTimeTicketsCountAndSum, 
//						matterExpenseTicketsCountAndSum, matterTimeTickets, expenseTickets, filteredMatterNumbers);
//				log.info("Finalized--List-----#1--> : " + matterTimeExpenseTickets.size());
			}
			
			/*-------------------------------------------------------------------------------*/
			// classId = 1, billmodeId = 'Contingency' selected MatterNumber and StatusId = 30
			/*-------------------------------------------------------------------------------*/
			String billmodeId = null;
			if (inputBillModeId == null) { 											// Input is not given
				billmodeId = BILLMODE_CONTIGENCY;
			} else if (inputBillModeId != null && inputBillModeId.contains(3L)) {	// Input is given
				billmodeId = BILLMODE_CONTIGENCY;
			}
			log.info("billmodeId----BILLMODE_CONTIGENCY------2------> " + billmodeId);
			
			List<MatterGenAcc> contigencyByGroupMatterGenerals = null;
			if (matterNumbers != null && !matterNumbers.isEmpty()) {
				contigencyByGroupMatterGenerals = 		// If Status_ID in (30) 
						matterGenAccRepository.findByClassIdAndBillingModeIdAndStatusIdAndMatterNumberInAndDeletionIndicator(classId, 
								billmodeId, statusId, matterNumbers, 0L);
			} else {
				contigencyByGroupMatterGenerals = matterGenAccRepository.findByClassIdAndBillingModeIdAndStatusIdAndDeletionIndicator(classId, billmodeId, statusId, 0L);
			}
				
			if (billmodeId != null) { // Execute only if the BillingMode is given
	//			log.info("contigencyByGroupMatterGenerals:-------> " + contigencyByGroupMatterGenerals);
				if (!contigencyByGroupMatterGenerals.isEmpty()) {
					log.info("contigencyByGroupMatterGenerals------#2------> " + contigencyByGroupMatterGenerals.size());
					List<String> matterNumbersFiltered = hourlyByGroupMatterGenerals.stream().map(MatterGenAcc::getMatterNumber).collect(Collectors.toList());
					List<String[]> matterTimeTicketsCountAndSum = 
							matterTimeTicketRepository.findCountAndSumOfTimeTicketsByGroup1 (matterNumbersFiltered, startDate, feesCutoffDate, originatingTimeKeeper, responsibleTimeKeeper, assignedTimeKeeper);
					List<String[]> matterExpenseTicketsCountAndSum = 
							matterExpenseRepository.findExpenseByGroup1(matterNumbersFiltered, startDate, feesCutoffDate, originatingTimeKeeper, responsibleTimeKeeper, assignedTimeKeeper);
					
					// Select MatterTimeTickets by Group
					// Filtering all matched matched MatterNumbers
//					List<String> filteredMatterNumbers = new ArrayList<>();
//					if (matterTimeTicketsCountAndSum !=  null && !matterTimeTicketsCountAndSum.isEmpty()) {
//						for (String[] imatter : matterTimeTicketsCountAndSum) {
//							filteredMatterNumbers.add(imatter[2]);
//						}
//					} else {
//						filteredMatterNumbers.addAll(matterNumbers);
//					}
					
					List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets = 
							matterTimeTicketRepository.findTimeTicketsByGroup (matterNumbers, startDate, feesCutoffDate);
					
					// Select MatterExpense by Group
					List<com.mnrclara.api.management.model.dto.IMatterExpense> expenseTickets = 
							matterExpenseRepository.findExpenseRecordsByGroup(matterNumbers, startDate, feesCutoffDate);
					matterTimeExpenseTickets = calculateTotalAmount1(billByGroup.getPreBillDate(), matterTimeTicketsCountAndSum, 
							matterExpenseTicketsCountAndSum, matterTimeTickets, expenseTickets, matterNumbers);
					
//					List<String> matterNumbersFiltered = hourlyByGroupMatterGenerals.stream().map(MatterGenAcc::getMatterNumber).collect(Collectors.toList());
//					List<IMatterTimeTicketCountAndSum> matterTimeTicketsCountAndSum = 
//							matterTimeTicketRepository.findCountAndSumOfTimeTicketsByGroup(matterNumbersFiltered, startDate, feesCutoffDate, 
//									originatingTimeKeeper, responsibleTimeKeeper, assignedTimeKeeper);
//					List<IMatterExpenseCountAndSum> matterExpenseTicketsCountAndSum = 
//							matterExpenseRepository.findExpenseByGroup(matterNumbersFiltered, startDate, feesCutoffDate, originatingTimeKeeper, 
//									responsibleTimeKeeper, assignedTimeKeeper);
//					
//					// Select MatterTimeTickets by Group
//					// Filtering all matched matched MatterNumbers
//					List<String> filteredMatterNumbers = new ArrayList<>();
//					for (IMatterTimeTicketCountAndSum imatter : matterTimeTicketsCountAndSum) {
//						filteredMatterNumbers.add(imatter.getMatterNumber());
//					}
//	//				log.info("filteredMatterNumbers---------> : " + filteredMatterNumbers);
//					List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets = 
//							matterTimeTicketRepository.findTimeTicketsByGroup (filteredMatterNumbers, startDate, feesCutoffDate);
//					
//					// Select MatterExpense by Group
//					List<com.mnrclara.api.management.model.dto.IMatterExpense> expenseTickets = 
//							matterExpenseRepository.findExpenseRecordsByGroup(filteredMatterNumbers, startDate, feesCutoffDate);
//					matterTimeExpenseTickets = calculateTotalAmount(billByGroup.getPreBillDate(), matterTimeTicketsCountAndSum, 
//							matterExpenseTicketsCountAndSum, matterTimeTickets, expenseTickets, filteredMatterNumbers);
				}
			}
			log.info("Finalized--List-----#2--> : " + matterTimeExpenseTickets.size());
			return matterTimeExpenseTickets;
		} // end of classId = 1
		
		/*----------------------------------------------------------------------------------------------*/
		// classId = 2, billmodeId = 'Hourly', 'Flat_Fees', selected MatterNumber and StatusId <> 1 & 30
		/*----------------------------------------------------------------------------------------------*/
//		classId = 2L;
		if (classId == 2L) {
			String billmodeId = null;
			List<String> billingModeId = Arrays.asList(BILLMODE_HOURLY, BILLMODE_FLATFEE);
			List<Long> inputBillModeId = billByGroup.getBillingMode();
			if (inputBillModeId != null && inputBillModeId.size() == 1 && inputBillModeId.contains(1L)) {
				billingModeId = Arrays.asList(BILLMODE_HOURLY);
			} else if (inputBillModeId != null && inputBillModeId.size() == 1 && inputBillModeId.contains(2L)) {
				billingModeId = Arrays.asList(BILLMODE_FLATFEE);
			} else if (inputBillModeId != null && inputBillModeId.size() > 1 && (inputBillModeId.contains(1L) && inputBillModeId.contains(2L))) {
				billingModeId = Arrays.asList(BILLMODE_HOURLY, BILLMODE_FLATFEE);
			} else if (inputBillModeId == null) {
				billingModeId = Arrays.asList(BILLMODE_HOURLY, BILLMODE_FLATFEE);
			}
			
			log.info("----------billingModeId---------3--------> " + billingModeId);
			
			List<MatterGenAcc> hourlyByGroupMatterGeneralsForClass2 = null;
			if (matterNumbers != null && !matterNumbers.isEmpty()) {
				hourlyByGroupMatterGeneralsForClass2 = matterGenAccRepository.findByClassIdAndBillingModeIdInAndMatterNumberInAndDeletionIndicator(
						classId, billingModeId, matterNumbers, 0L);
			} else {
				hourlyByGroupMatterGeneralsForClass2 = matterGenAccRepository.findByClassIdAndBillingModeIdInAndDeletionIndicator(classId, billingModeId, 0L);
			}
			
			if (!hourlyByGroupMatterGeneralsForClass2.isEmpty()) {
				log.info("matterTimeTickets:--GROUP INPUTS-----> " + matterNumbers + "," + startDate + "," + feesCutoffDate + "," + 
							originatingTimeKeeper + "," + responsibleTimeKeeper + "," + assignedTimeKeeper);
				List<String> matterNumbersFiltered = hourlyByGroupMatterGeneralsForClass2.stream().map(MatterGenAcc::getMatterNumber).collect(Collectors.toList());
				List<String[]> matterTimeTicketsCountAndSum = 
						matterTimeTicketRepository.findCountAndSumOfTimeTicketsByGroup1 (matterNumbersFiltered, startDate, feesCutoffDate, originatingTimeKeeper, responsibleTimeKeeper, assignedTimeKeeper);
				List<String[]> matterExpenseTicketsCountAndSum = 
						matterExpenseRepository.findExpenseByGroup1(matterNumbersFiltered, startDate, feesCutoffDate, originatingTimeKeeper, responsibleTimeKeeper, assignedTimeKeeper);
				
				// Select MatterTimeTickets by Group
				// Filtering all matched matched MatterNumbers
//				List<String> filteredMatterNumbers = new ArrayList<>();
//				if (matterTimeTicketsCountAndSum !=  null && !matterTimeTicketsCountAndSum.isEmpty()) {
//					for (String[] imatter : matterTimeTicketsCountAndSum) {
//						filteredMatterNumbers.add(imatter[2]);
//					}
//				} else {
//					filteredMatterNumbers.addAll(matterNumbers);
//				}
				
				List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets = 
						matterTimeTicketRepository.findTimeTicketsByGroup (matterNumbers, startDate, feesCutoffDate);
				
				// Select MatterExpense by Group
				List<com.mnrclara.api.management.model.dto.IMatterExpense> expenseTickets = 
						matterExpenseRepository.findExpenseRecordsByGroup(matterNumbers, startDate, feesCutoffDate);
				
				matterTimeExpenseTickets = calculateTotalAmount1(billByGroup.getPreBillDate(), matterTimeTicketsCountAndSum, 
						matterExpenseTicketsCountAndSum, matterTimeTickets, expenseTickets, matterNumbers);
	//			log.info("Finalized--List-------> : " + matterTimeExpenseTickets);
			}
			
			/*----------------------------------------------------------------------------------*/
			// classId = 2, billmodeId = 'Contingency' selected MatterNumber and StatusId = 30
			/*----------------------------------------------------------------------------------*/
			String billmodeIdC2 = null;
			if (inputBillModeId == null) { 											// Input is not given
				billmodeIdC2 = BILLMODE_CONTIGENCY;
			} else if (inputBillModeId != null && inputBillModeId.contains(3L)) {	// Input is given
				billmodeIdC2 = BILLMODE_CONTIGENCY;
			}
			
			log.info("billmodeId-----4------> " + billmodeId);
			
			List<MatterGenAcc> contigencyByGroupMatterGeneralsForClass2 = null;
			if (matterNumbers != null && !matterNumbers.isEmpty()) {
				contigencyByGroupMatterGeneralsForClass2 = 		// If Status_ID in (30) 
						matterGenAccRepository.findByClassIdAndBillingModeIdAndStatusIdAndMatterNumberInAndDeletionIndicator(classId, 
								billmodeIdC2, statusId, matterNumbers, 0L);
			} else {
				contigencyByGroupMatterGeneralsForClass2 = 		// If Status_ID in (30) 
						matterGenAccRepository.findByClassIdAndBillingModeIdAndStatusIdAndDeletionIndicator(classId, billmodeIdC2, statusId, 0L);
			}
			
			if (billmodeIdC2 != null) { // Execute only if the BillingMode is given
				if (!contigencyByGroupMatterGeneralsForClass2.isEmpty()) {
					List<String> matterNumbersFiltered = hourlyByGroupMatterGeneralsForClass2.stream().map(MatterGenAcc::getMatterNumber).collect(Collectors.toList());
					List<String[]> matterTimeTicketsCountAndSum = 
							matterTimeTicketRepository.findCountAndSumOfTimeTicketsByGroup1 (matterNumbersFiltered, startDate, feesCutoffDate, originatingTimeKeeper, responsibleTimeKeeper, assignedTimeKeeper);
					List<String[]> matterExpenseTicketsCountAndSum = 
							matterExpenseRepository.findExpenseByGroup1(matterNumbersFiltered, startDate, feesCutoffDate, originatingTimeKeeper, responsibleTimeKeeper, assignedTimeKeeper);
					
					// Select MatterTimeTickets by Group
					// Filtering all matched matched MatterNumbers
//					List<String> filteredMatterNumbers = new ArrayList<>();
//					if (matterTimeTicketsCountAndSum !=  null && !matterTimeTicketsCountAndSum.isEmpty()) {
//						for (String[] imatter : matterTimeTicketsCountAndSum) {
//							filteredMatterNumbers.add(imatter[2]);
//						}
//					} else {
//						filteredMatterNumbers.addAll(matterNumbers);
//					}
					
					List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets = 
							matterTimeTicketRepository.findTimeTicketsByGroup (matterNumbers, startDate, feesCutoffDate);
					
					// Select MatterExpense by Group
					List<com.mnrclara.api.management.model.dto.IMatterExpense> expenseTickets = 
							matterExpenseRepository.findExpenseRecordsByGroup(matterNumbers, startDate, feesCutoffDate);
					
					matterTimeExpenseTickets = calculateTotalAmount1(billByGroup.getPreBillDate(), matterTimeTicketsCountAndSum, 
							matterExpenseTicketsCountAndSum, matterTimeTickets, expenseTickets, matterNumbers);
//					List<String> matterNumbersFiltered = hourlyByGroupMatterGeneralsForClass2.stream().map(MatterGenAcc::getMatterNumber).collect(Collectors.toList());
//					List<IMatterTimeTicketCountAndSum> matterTimeTicketsCountAndSum = 
//							matterTimeTicketRepository.findCountAndSumOfTimeTicketsByGroup(matterNumbersFiltered, startDate, feesCutoffDate, 
//									originatingTimeKeeper, responsibleTimeKeeper, assignedTimeKeeper);
//					List<IMatterExpenseCountAndSum> matterExpenseTicketsCountAndSum = 
//							matterExpenseRepository.findExpenseByGroup(matterNumbersFiltered, startDate, feesCutoffDate, originatingTimeKeeper, 
//									responsibleTimeKeeper, assignedTimeKeeper);
//					
//					// Select MatterTimeTickets by Group
//					// Filtering all matched matched MatterNumbers
//					List<String> filteredMatterNumbers = new ArrayList<>();
//					for (IMatterTimeTicketCountAndSum imatter : matterTimeTicketsCountAndSum) {
//						filteredMatterNumbers.add(imatter.getMatterNumber());
//					}
//					List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets = 
//							matterTimeTicketRepository.findTimeTicketsByGroup (filteredMatterNumbers, startDate, feesCutoffDate);
//					
//					// Select MatterExpense by Group
//					List<com.mnrclara.api.management.model.dto.IMatterExpense> expenseTickets = 
//							matterExpenseRepository.findExpenseRecordsByGroup(filteredMatterNumbers, startDate, feesCutoffDate);
//								
//					matterTimeExpenseTickets = calculateTotalAmount(billByGroup.getPreBillDate(), matterTimeTicketsCountAndSum, 
//							matterExpenseTicketsCountAndSum, matterTimeTickets, expenseTickets, filteredMatterNumbers);
				}
			}
			return matterTimeExpenseTickets;
		} // end of classId = 2
		return matterTimeExpenseTickets;
	}
	
	/**
	 * 
	 * @param preBillDate 
	 * @param matterTimeExpenseTicket
	 * @param matterTimeTickets
	 * @param matterExpenseTickets
	 * @param matterTimeTickets2 
	 * @return
	 */
	private List<MatterTimeExpenseTicket> calculateTotalAmount(Date preBillDate, 
			List<IMatterTimeTicketCountAndSum> matterTimeTicketsCountAndSum,
			List<IMatterExpenseCountAndSum> matterExpenseTicketsCountAndSum, 
			List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets,
			List<com.mnrclara.api.management.model.dto.IMatterExpense> matterExpenses,
			List<String> matterNumbers) {
		long count = 0;
		double totalAmount = 0;
		
		long timeTicketCount = 0;
		double timeTicketAmount = 0;
		
		long expenseCount = 0;
		double expenseAmount = 0;
		
		List<MatterTimeTicket> selectedMatterTimeTickets = populateMatterTimeTickets (matterTimeTickets);
		List<MatterExpense> selectedMatterExpenses = populateMatterExpenseTickets (matterExpenses);
		List<MatterTimeExpenseTicket> matterTimeExpenseTicketList = new ArrayList<>();
		
//		if (matterTimeTicketsCountAndSum != null) {
////			log.info("----matterTimeTicketsCountAndSum---> " + matterTimeTicketsCountAndSum.size());
//		}
		
		if (matterExpenseTicketsCountAndSum != null) {
			log.info("----matterExpenseTicketsCountAndSum---> " + matterExpenseTicketsCountAndSum.size());
			for (IMatterExpenseCountAndSum expense : matterExpenseTicketsCountAndSum) {
				log.info("----matterExpenseTicketsCountAndSum------$-> " + expense.getMatterNumber());
			}
		}
		
		if (matterTimeTicketsCountAndSum != null && !matterTimeTicketsCountAndSum.isEmpty()) {
			log.info("----matterTimeTicketsCountAndSum---matched-----");
			try {
				for (IMatterTimeTicketCountAndSum timeTicket : matterTimeTicketsCountAndSum) {
					Optional<MatterGenAcc> matterGenAcc = matterGenAccRepository.findByMatterNumber(timeTicket.getMatterNumber());
//					log.info("----matterGenAcc---##########----->" + matterGenAcc);
					
					if (timeTicket != null && timeTicket.getTimeTicketAmount() != null) {
						timeTicketAmount = timeTicket.getTimeTicketAmount();
					}
					
					if (timeTicket != null && timeTicket.getTimeTicketCount() != null) {
						timeTicketCount = timeTicket.getTimeTicketCount();
					}
					
					MatterTimeExpenseTicket matterTimeExpenseTicket = new MatterTimeExpenseTicket();
					matterTimeExpenseTicket.setPreBillDate(preBillDate);
					matterTimeExpenseTicket.setClientId(getClientId(timeTicket.getMatterNumber()));
					matterTimeExpenseTicket.setMatterNo(timeTicket.getMatterNumber());
					
					//-------------BILL_MODE_ID--2----LOGIC-----------------------------------------------
//					log.info("matterGenAcc.get().getRemainingAmount() : " + matterGenAcc.get().getRemainingAmount());
					if (matterGenAcc.get().getBillingModeId() != null && matterGenAcc.get().getBillingModeId().equalsIgnoreCase("2")) {
						if (matterGenAcc.get().getRemainingAmount() != null && matterGenAcc.get().getRemainingAmount() != 0) {
							matterTimeExpenseTicket.setTotalAmount(matterGenAcc.get().getRemainingAmount());
						} else {
							matterTimeExpenseTicket.setTotalAmount(timeTicketAmount);
						}
					} else {
						matterTimeExpenseTicket.setTotalAmount(timeTicketAmount);
					}
//					log.info("matterTimeExpenseTicket-----------1---------> : " + matterTimeExpenseTicket);
					
					for (IMatterExpenseCountAndSum expense : matterExpenseTicketsCountAndSum) {
						if (expense != null && expense.getExpenseCount() != null && expense.getMatterNumber() != null && 
								expense.getMatterNumber().equalsIgnoreCase(timeTicket.getMatterNumber())) {
							expenseCount = expense.getExpenseCount();
							count = timeTicketCount + expenseCount;
							matterTimeExpenseTicket.setTimeTicketCount(count);
						} else {
							matterTimeExpenseTicket.setTimeTicketCount(timeTicketCount);
						}
						
//						log.info("expenseAmount:" + expense.getExpenseAmount());						
						if (matterGenAcc.get().getBillingModeId() != null && matterGenAcc.get().getBillingModeId().equalsIgnoreCase("2")) {
//							log.info("-------matterGenAcc.get().getRemainingAmount()------------>:" + matterGenAcc.get().getRemainingAmount());
							if (matterGenAcc.get().getRemainingAmount() != null && matterGenAcc.get().getRemainingAmount() != null 
									&& matterGenAcc.get().getRemainingAmount() == 0) {
								if (expense != null && expense.getExpenseAmount() != null && expense.getMatterNumber() != null && 
										expense.getMatterNumber().equalsIgnoreCase(timeTicket.getMatterNumber()) ) {
									totalAmount = timeTicketAmount + expense.getExpenseAmount();
//									log.info("==========totalAmount====####===:" + totalAmount + "=" + timeTicketAmount + "+" + expense.getExpenseAmount());
									matterTimeExpenseTicket.setTotalAmount(totalAmount);
								} else {
									matterTimeExpenseTicket.setTotalAmount(timeTicketAmount);
								}
							}
						} else {
							if (expense != null && expense.getExpenseAmount() != null && expense.getMatterNumber() != null && 
									expense.getMatterNumber().equalsIgnoreCase(timeTicket.getMatterNumber()) ) {
								totalAmount = timeTicketAmount + expense.getExpenseAmount();
//								log.info("==========totalAmount====%%%%===:" + totalAmount + "=" + timeTicketAmount + "+" + expense.getExpenseAmount());
								matterTimeExpenseTicket.setTotalAmount(totalAmount);
							} else {
								matterTimeExpenseTicket.setTotalAmount(timeTicketAmount);
							}
						}
					}
//					log.info("matterTimeExpenseTicket-----------2---------> : " + matterTimeExpenseTicket);
					
					// Associating respective MatterTimeTickets to respective MatterNumber
					List<MatterTimeTicket> matterSpecificTimeTickets = new ArrayList<>();
					List<MatterExpense> matterSpecificExpenses = new ArrayList<>();
					
					for (MatterTimeTicket matterTimeTicket : selectedMatterTimeTickets) {
						if (matterTimeTicket != null 
								&& matterTimeTicket.getMatterNumber().equalsIgnoreCase(timeTicket.getMatterNumber())) {
							matterSpecificTimeTickets.add(matterTimeTicket);
						}
					}
					
					for (MatterExpense matterExpense : selectedMatterExpenses) {
						if (matterExpense != null && 
								matterExpense.getMatterNumber().equalsIgnoreCase(timeTicket.getMatterNumber())) {
							matterSpecificExpenses.add(matterExpense);
						}
					}
					matterTimeExpenseTicket.setMatterTimeTicket(matterSpecificTimeTickets);
					matterTimeExpenseTicket.setMatterExpense(matterSpecificExpenses);
					matterTimeExpenseTicketList.add(matterTimeExpenseTicket);
					log.info("----matterTimeExpenseTicketList-~~~~~~~~~~~~~~~~~~----> " + matterTimeExpenseTicketList.size());
				}
				log.info("----matterTimeExpenseTicketList--@@@@@@@@@--#1----> " + matterTimeExpenseTicketList);
				return matterTimeExpenseTicketList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		
		if (matterExpenseTicketsCountAndSum != null && !matterExpenseTicketsCountAndSum.isEmpty()) {
			log.info("----matterTimeTicketsCountAndSum---else-if-------matched-----");
			try {
				MatterTimeExpenseTicket matterTimeExpenseTicket = new MatterTimeExpenseTicket();
				matterTimeExpenseTicket.setPreBillDate(preBillDate);
				String matterNumber = null;
				for (IMatterExpenseCountAndSum expense : matterExpenseTicketsCountAndSum) {
//					log.info("========expense=============>>>>>>> : " + expense.getMatterNumber());
					Optional<MatterGenAcc> matterGenAcc = matterGenAccRepository.findByMatterNumber(expense.getMatterNumber());
					matterNumber = expense.getMatterNumber();
					if (expense != null && expense.getExpenseCount() != null && expense.getMatterNumber() != null) {
						expenseCount = expense.getExpenseCount();
					}
					
					if (expense != null && expense.getExpenseAmount() != null && expense.getMatterNumber() != null) {
						expenseAmount = expense.getExpenseAmount();
					}
					matterTimeExpenseTicket.setTimeTicketCount(expenseCount);
					
					//-------------BILL_MODE_ID--2----LOGIC--------------------------------------------------------------------------
//					log.info("timeTicketAmount + expenseAmount  : " + timeTicketAmount + "," + expenseAmount);
					if (matterGenAcc.get().getBillingModeId() != null && matterGenAcc.get().getBillingModeId().equalsIgnoreCase("2")) {
						if (matterGenAcc.get().getRemainingAmount() != null && matterGenAcc.get().getRemainingAmount() != 0) {
							matterTimeExpenseTicket.setTotalAmount(matterGenAcc.get().getRemainingAmount() + expenseAmount);
						} else {
							matterTimeExpenseTicket.setTotalAmount(timeTicketAmount + expenseAmount);
						}
					} else {
						matterTimeExpenseTicket.setTotalAmount(timeTicketAmount + expenseAmount);
					}
				}
				log.info("matterTimeExpenseTicket : " + matterTimeExpenseTicket);
				
				matterTimeExpenseTicket.setClientId(getClientId(matterNumber));
				matterTimeExpenseTicket.setMatterNo(matterNumber);
				
				// Associating respective MatterTimeTickets to respective MatterNumber
				List<MatterTimeTicket> matterSpecificTimeTickets = new ArrayList<>();
				List<MatterExpense> matterSpecificExpenses = new ArrayList<>();
				
				for (MatterExpense matterExpense : selectedMatterExpenses) {
					if (matterExpense != null && 
							matterExpense.getMatterNumber().equalsIgnoreCase(matterNumber)) {
						matterSpecificExpenses.add(matterExpense);
					}
				}
				matterTimeExpenseTicket.setMatterTimeTicket(matterSpecificTimeTickets);
				matterTimeExpenseTicket.setMatterExpense(matterSpecificExpenses);
//				log.info("matterSpecificExpenses : " + matterSpecificExpenses);
				
				matterTimeExpenseTicketList.add(matterTimeExpenseTicket);
				log.info("----matterTimeExpenseTicketList-$$$$$$$$--#2----> " + matterTimeExpenseTicketList.size());
				return matterTimeExpenseTicketList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 

		if (matterTimeTicketsCountAndSum.isEmpty() && matterExpenseTicketsCountAndSum.isEmpty()) {
			log.info("---------else--block-------> : " + matterNumbers);
			try {
				MatterTimeExpenseTicket matterTimeExpenseTicket = new MatterTimeExpenseTicket();
				matterTimeExpenseTicket.setPreBillDate(preBillDate);
				
				//-------------BILL_MODE_ID--2----LOGIC--------------------------------------------------------------------------
				matterNumbers.forEach(m -> {
					Optional<MatterGenAcc> matterGenAccOpt = matterGenAccRepository.findByMatterNumber(m);
					if (!matterGenAccOpt.isEmpty()) {
						MatterGenAcc matterGenAcc = matterGenAccOpt.get();
						if (matterGenAcc.getBillingModeId() != null && matterGenAcc.getBillingModeId().equalsIgnoreCase("2")) {
							if (matterGenAcc.getRemainingAmount() != null && matterGenAcc.getRemainingAmount() != 0) {
								matterTimeExpenseTicket.setTotalAmount(matterGenAcc.getRemainingAmount());
							} else {
								matterTimeExpenseTicket.setTotalAmount(0D);
							}
						} else {
							matterTimeExpenseTicket.setTotalAmount(0D);
						}
					}
					matterTimeExpenseTicket.setMatterNo(m);
					matterTimeExpenseTicketList.add(matterTimeExpenseTicket);
					log.info("----matterTimeExpenseTicketList-&&&&&&&&&&--#3----> " + matterTimeExpenseTicketList.size());
				});
				return matterTimeExpenseTicketList;
			} catch (Exception e) {
				e.printStackTrace();
			};
		}
		
		//----------------------------------Only for Non-Billable--------------------------------------------------
		if (matterTimeExpenseTicketList.isEmpty()) { 
			MatterTimeExpenseTicket matterTimeExpenseTicket = new MatterTimeExpenseTicket();
			matterTimeExpenseTicket.setMatterTimeTicket(selectedMatterTimeTickets);
			matterTimeExpenseTicket.setMatterExpense(selectedMatterExpenses);
			matterTimeExpenseTicketList.add(matterTimeExpenseTicket);
			
			// preBillDate
			matterTimeExpenseTicket.setPreBillDate(preBillDate);
			
			// Getting ClientId and MatterNumber
			if (!selectedMatterTimeTickets.isEmpty()) {
				MatterTimeTicket matterTimeTicket = selectedMatterTimeTickets.get(0);
				matterTimeExpenseTicket.setClientId(matterTimeTicket.getClientId());
				matterTimeExpenseTicket.setMatterNo(matterTimeTicket.getMatterNumber());
			}
		}
		log.info("matterTimeExpenseTicketList============>: " + matterTimeExpenseTicketList.size());
		return matterTimeExpenseTicketList;
	}
	
	/**
	 * 
	 * @param preBillDate
	 * @param matterTimeTicketsCountAndSum
	 * @param matterExpenseTicketsCountAndSum
	 * @param matterTimeTickets
	 * @param matterExpenses
	 * @return
	 */
	private List<MatterTimeExpenseTicket> calculateTotalAmount1(Date preBillDate, 
			List<String[]> matterTimeTicketsCountAndSum,
			List<String[]> matterExpenseTicketsCountAndSum, 
			List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets,
			List<com.mnrclara.api.management.model.dto.IMatterExpense> matterExpenses,
			List<String> matterNumbers) {
		try {
			List<MatterTimeTicket> selectedMatterTimeTickets = populateMatterTimeTickets (matterTimeTickets);
			List<MatterExpense> selectedMatterExpenses = populateMatterExpenseTickets (matterExpenses);
			List<MatterTimeExpenseTicket> matterTimeExpenseTicketList = new ArrayList<>();
			
			log.info("--------matterNumbers------passed>>>>>>>>>>>> " + matterNumbers);
			
			// Data List preparation
			matterNumbers.stream().forEach(inputMatterNumber -> {
				double timeTicketAmount = 0;
				long timeTicketCount = 0;
				long count = 0;
				double totalAmount = 0;
				long expenseCount = 0;
				double expenseAmount = 0;
				
				MatterTimeExpenseTicket matterTimeExpenseTicket = new MatterTimeExpenseTicket();
				matterTimeExpenseTicket.setPreBillDate(preBillDate);
				matterTimeExpenseTicket.setClientId(getClientId(inputMatterNumber));
				matterTimeExpenseTicket.setMatterNo(inputMatterNumber);
				if (matterTimeTicketsCountAndSum != null && !matterTimeTicketsCountAndSum.isEmpty()) {
					String[] timeTicket = matterTimeTicketsCountAndSum.stream()
							.filter(ticket -> ticket[2].equalsIgnoreCase(inputMatterNumber)).findFirst().orElse(null);
					if (timeTicket != null) {
						if (timeTicket[1] != null) {
							timeTicketAmount = Double.valueOf(timeTicket[1]).doubleValue();
						}
						
						if (timeTicket [0] != null) {
							timeTicketCount = Long.valueOf(timeTicket[0]);
						}
						matterTimeExpenseTicket.setTimeTicketCount(timeTicketCount);
						
						Optional<MatterGenAcc> matterGenAccOpt = matterGenAccRepository.findByMatterNumber(inputMatterNumber);
						if (!matterGenAccOpt.isEmpty()) {
							MatterGenAcc matterGenAcc = matterGenAccOpt.get();
							if (matterGenAcc.getBillingModeId() != null && matterGenAcc.getBillingModeId().equalsIgnoreCase("2")) {
								if (matterGenAcc.getRemainingAmount() != null && matterGenAcc.getRemainingAmount() != 0) {
									matterTimeExpenseTicket.setTotalAmount(matterGenAcc.getRemainingAmount());
								} else {
									matterTimeExpenseTicket.setTotalAmount(0D);
								}
							} else {
								matterTimeExpenseTicket.setTotalAmount(timeTicketAmount);
							}
						}
						
						// Associating respective MatterTimeTickets to respective MatterNumber
						List<MatterTimeTicket> matterSpecificTimeTickets = new ArrayList<>();
						for (MatterTimeTicket matterTimeTicket : selectedMatterTimeTickets) {
							if (matterTimeTicket != null 
									&& matterTimeTicket.getMatterNumber().equalsIgnoreCase(timeTicket[2])) {
								matterSpecificTimeTickets.add(matterTimeTicket);
							}
						}
						matterTimeExpenseTicket.setMatterTimeTicket(matterSpecificTimeTickets);
					}
				}
				
				log.info("--------timeTicketAmount------#############>>>>>>>>>>>> " + timeTicketAmount);
				
				if (matterExpenseTicketsCountAndSum != null && !matterExpenseTicketsCountAndSum.isEmpty()) {
					String[] expense = matterExpenseTicketsCountAndSum.stream()
							.filter(e -> e[2].equalsIgnoreCase(inputMatterNumber)).findFirst().orElse(null);
					if (expense != null) {
						expenseCount = Long.valueOf(expense[0]);
						if (expense[1] != null) {
							expenseAmount = Double.valueOf(expense[1]).doubleValue();
						}
						count = timeTicketCount + expenseCount;
							
						if(expense[3].equalsIgnoreCase("Credit")){
							totalAmount = timeTicketAmount - expenseAmount;
						} else {
							totalAmount = timeTicketAmount + expenseAmount;
						}
						
						matterTimeExpenseTicket.setTimeTicketCount(count);
						
						Optional<MatterGenAcc> matterGenAccOpt = matterGenAccRepository.findByMatterNumber(inputMatterNumber);
						if (!matterGenAccOpt.isEmpty()) {
							MatterGenAcc matterGenAcc = matterGenAccOpt.get();
							if (matterGenAcc.getBillingModeId() != null && matterGenAcc.getBillingModeId().equalsIgnoreCase("2")) {
								if (matterGenAcc.getRemainingAmount() != null && matterGenAcc.getRemainingAmount() != 0) {
									matterTimeExpenseTicket.setTotalAmount(matterGenAcc.getRemainingAmount() + expenseAmount);
								} else {
									matterTimeExpenseTicket.setTotalAmount(timeTicketAmount + expenseAmount);
								}
							} else {
								matterTimeExpenseTicket.setTotalAmount(timeTicketAmount + expenseAmount);
							}
						}
						log.info("--------timeTicketAmount----expenseAmount--####$$$$$$####>>>>>>>>>>>> " + timeTicketAmount + "," + expenseAmount);
						log.info("##########------------matterTimeExpenseTicket-----------> : " + matterTimeExpenseTicket);
						
						// Associating respective MatterTimeTickets to respective MatterNumber
						List<MatterExpense> matterSpecificExpenses = new ArrayList<>();
						for (MatterExpense matterExpense : selectedMatterExpenses) {
							if (matterExpense != null && matterExpense.getMatterNumber().equalsIgnoreCase(inputMatterNumber)) {
								matterSpecificExpenses.add(matterExpense);
								log.info("matterSpecificExpenses : " + matterExpense);
//								matterTimeExpenseTicket.setTotalAmount(timeTicketAmount + matterExpense.getExpenseAmount());
							}
						}
						matterTimeExpenseTicket.setMatterExpense(matterSpecificExpenses);
					}
				}
				
				log.info("---------Both--Timeticket & expense count----> : " + matterTimeTicketsCountAndSum + "," + matterExpenseTicketsCountAndSum);
				
				// -----------Both MatterTimeTickets && MatterExpenseTickets are Empty -------------------
				if (matterTimeTicketsCountAndSum.isEmpty() && matterExpenseTicketsCountAndSum.isEmpty()) {
					log.info("---------Both MatterTimeTickets && MatterExpenseTickets are Empty---block----> : " + matterNumbers);
					try {
						//-------------BILL_MODE_ID--2----LOGIC--------------------------------------------------------------------------
						Optional<MatterGenAcc> matterGenAccOpt = matterGenAccRepository.findByMatterNumber(inputMatterNumber);
						if (!matterGenAccOpt.isEmpty()) {
							MatterGenAcc matterGenAcc = matterGenAccOpt.get();
							if (matterGenAcc.getBillingModeId() != null && matterGenAcc.getBillingModeId().equalsIgnoreCase("2")) {
								if (matterGenAcc.getRemainingAmount() != null && matterGenAcc.getRemainingAmount() != 0) {
									matterTimeExpenseTicket.setTotalAmount(matterGenAcc.getRemainingAmount());
								} else {
									matterTimeExpenseTicket.setTotalAmount(0D);
								}
							} else {
								matterTimeExpenseTicket.setTotalAmount(0D);
							}
							log.info("----matterTimeExpenseTicketList-matterNumber----> " + inputMatterNumber);
						}
					} catch (Exception e) {
						e.printStackTrace();
					};
				}
				matterTimeExpenseTicketList.add(matterTimeExpenseTicket);
			});
		
		log.info("matterTimeExpenseTicketList-----> : " + matterTimeExpenseTicketList);
			
//			if (matterTimeTicketsCountAndSum != null && !matterTimeTicketsCountAndSum.isEmpty()) {
//				for (String[] timeTicket : matterTimeTicketsCountAndSum) {
//					if (timeTicket[1] != null) {
//						timeTicketAmount = Double.valueOf(timeTicket[1]).doubleValue();
//					}
//					
//					if (timeTicket [0] != null) {
//						timeTicketCount = Long.valueOf(timeTicket[0]);
//					}
//					
//					MatterTimeExpenseTicket matterTimeExpenseTicket = new MatterTimeExpenseTicket();
//					matterTimeExpenseTicket.setPreBillDate(preBillDate);
//					matterTimeExpenseTicket.setClientId(getClientId(timeTicket[2]));
//					matterTimeExpenseTicket.setMatterNo(timeTicket[2]);
//					matterTimeExpenseTicket.setTotalAmount(timeTicketAmount);
//					
//					for (String[] expense : matterExpenseTicketsCountAndSum) {
//						if (expense[0] != null && expense[2] != null && expense[2].equalsIgnoreCase(timeTicket[2])) {
//							expenseCount = Long.valueOf(expense[0]);
//							count = timeTicketCount + expenseCount;
//							matterTimeExpenseTicket.setTimeTicketCount(count);
//						} else {
//							matterTimeExpenseTicket.setTimeTicketCount(timeTicketCount);
//						}
//						
//						if (expense[1] != null && expense[2] != null && expense[2].equalsIgnoreCase(timeTicket[2]) ) {
//							expenseAmount = Double.valueOf(expense[1]).doubleValue();
//							if(expense[3].equalsIgnoreCase("Credit")){
//								totalAmount = timeTicketAmount - expenseAmount;
//							} else {
//								totalAmount = timeTicketAmount + expenseAmount;
//							}
//							matterTimeExpenseTicket.setTotalAmount(totalAmount);
//						} else {
//							matterTimeExpenseTicket.setTotalAmount(timeTicketAmount);
//						}
//					}
//					
//					matterTimeExpenseTicket.setTotalAmount(timeTicketAmount + expenseAmount);
//					
//					// Associating respective MatterTimeTickets to respective MatterNumber
//					List<MatterTimeTicket> matterSpecificTimeTickets = new ArrayList<>();
//					List<MatterExpense> matterSpecificExpenses = new ArrayList<>();
//					
//					for (MatterTimeTicket matterTimeTicket : selectedMatterTimeTickets) {
//						if (matterTimeTicket != null 
//								&& matterTimeTicket.getMatterNumber().equalsIgnoreCase(timeTicket[2])) {
//							matterSpecificTimeTickets.add(matterTimeTicket);
//						}
//					}
//					
//					for (MatterExpense matterExpense : selectedMatterExpenses) {
//						if (matterExpense != null && 
//								matterExpense.getMatterNumber().equalsIgnoreCase(timeTicket[2])) {
//							matterSpecificExpenses.add(matterExpense);
//						}
//					}
//					log.info("----timeTicketCount---timeTicketAmount------> : " + timeTicketCount + "," + timeTicketAmount
//							+ "," + expenseCount + "," + expenseAmount);
//					
//					matterTimeExpenseTicket.setMatterTimeTicket(matterSpecificTimeTickets);
//					matterTimeExpenseTicket.setMatterExpense(matterSpecificExpenses);
//					matterTimeExpenseTicketList.add(matterTimeExpenseTicket);
//				}
//			} 
//
//			if (matterExpenseTicketsCountAndSum != null && !matterExpenseTicketsCountAndSum.isEmpty()) {
//				MatterTimeExpenseTicket matterTimeExpenseTicket = new MatterTimeExpenseTicket();
//				matterTimeExpenseTicket.setPreBillDate(preBillDate);
//				String matterNumber = null;
//				List<MatterExpense> matterSpecificExpenses = new ArrayList<>();
//				
//				for (String[] expense : matterExpenseTicketsCountAndSum) {
//					matterNumber = expense[2];
//					if (expense != null && expense[0] != null && expense[2] != null) {
//						expenseCount = Long.valueOf(expense[0]);
//					}
//					
//					if (expense != null && expense[1] != null && expense[2] != null) {
//						expenseAmount = Double.valueOf(expense[1]).doubleValue();
//					}
//					matterTimeExpenseTicket.setTimeTicketCount(expenseCount);
//					matterTimeExpenseTicket.setTotalAmount(expenseAmount);
//					matterTimeExpenseTicket.setClientId(getClientId(matterNumber));
//					matterTimeExpenseTicket.setMatterNo(matterNumber);
//					log.info("matterTimeExpenseTicket :: ---> " + matterTimeExpenseTicket);
//					
//					// Associating respective MatterTimeTickets to respective MatterNumber
//					List<MatterTimeTicket> matterSpecificTimeTickets = new ArrayList<>();
//					for (MatterExpense matterExpense : selectedMatterExpenses) {
//						log.info("matterExpense.getMatterNumber() : matterNumber $$$ " + matterExpense.getMatterNumber() + "," + matterNumber);
//						if (matterExpense != null && matterExpense.getMatterNumber().equalsIgnoreCase(matterNumber)) {
//							matterSpecificExpenses.add(matterExpense);
//							log.info("matterSpecificExpenses : " + matterExpense);
//						}
//					}
//					
//					matterTimeExpenseTicket.setMatterTimeTicket(matterSpecificTimeTickets);
//					matterTimeExpenseTicket.setMatterExpense(matterSpecificExpenses);
//					matterTimeExpenseTicketList.add(matterTimeExpenseTicket);
//				}
//			}
//			
			//----------------------------------Only for Non-Billable--------------------------------------------------
			if (matterTimeExpenseTicketList.isEmpty()) {
				matterNumbers.stream().forEach(inputMatterNumber -> {
					MatterTimeExpenseTicket matterTimeExpenseTicket = new MatterTimeExpenseTicket();
					matterTimeExpenseTicket.setPreBillDate(preBillDate);
					matterTimeExpenseTicket.setClientId(getClientId(inputMatterNumber));
					matterTimeExpenseTicket.setMatterNo(inputMatterNumber);
					matterTimeExpenseTicket.setMatterTimeTicket(selectedMatterTimeTickets);
					matterTimeExpenseTicket.setMatterExpense(selectedMatterExpenses);
					matterTimeExpenseTicket.setTimeTicketCount(0L);
					matterTimeExpenseTicket.setTotalAmount(0D);
					matterTimeExpenseTicketList.add(matterTimeExpenseTicket);
				});
			}
			
			log.info("----matterTimeExpenseTicketList---> " + matterTimeExpenseTicketList);
			return matterTimeExpenseTicketList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param matterNumber
	 * @return
	 */
	private String getClientId (String matterNumber) {
		Optional<MatterGenAcc> matterGenAcc = matterGenAccRepository.findByMatterNumber(matterNumber);
		if (!matterGenAcc.isEmpty()) {
//			log.info("----matterGenAcc----> " + matterGenAcc.get());
			return matterGenAcc.get().getClientId();
		}
		return null;
	}
	
	/**
	 * 
	 * @param iMatterExpenseTickets
	 * @return
	 */
	private List<MatterExpense> populateMatterExpenseTickets(
			List<com.mnrclara.api.management.model.dto.IMatterExpense> matterExpenses) {
		List<MatterExpense> dbMatterExpenseList = new ArrayList<>();
		for (com.mnrclara.api.management.model.dto.IMatterExpense iMatterExpense : matterExpenses) {
			MatterExpense matterExpense = new MatterExpense();
			
			log.info("MatterExpense MatterNumber: " + iMatterExpense.getMatterNumber());
//			log.info("MatterExpense Status ID: " + matterExpense.getStatusId());
			
			matterExpense.setMatterNumber(iMatterExpense.getMatterNumber());
			matterExpense.setMatterExpenseId(iMatterExpense.getMatterExpenseId());
			matterExpense.setExpenseCode(iMatterExpense.getExpenseCode());
			matterExpense.setCreatedOn(iMatterExpense.getCreatedOn());
			matterExpense.setCreatedBy(iMatterExpense.getCreatedBy());
			matterExpense.setExpenseDescription(iMatterExpense.getExpenseDescription());
			matterExpense.setNumberofItems(iMatterExpense.getNumberOfItems());
			matterExpense.setExpenseAmount(iMatterExpense.getExpenseAmount());
			matterExpense.setBillType(iMatterExpense.getBillType());
			matterExpense.setStatusId(iMatterExpense.getStatusId());
			matterExpense.setReferenceField2(iMatterExpense.getReferenceField2());
			matterExpense.setExpenseType(iMatterExpense.getExpenseType());
			dbMatterExpenseList.add(matterExpense);
		}
		return dbMatterExpenseList;
	}

	/**
	 * 
	 * @param iMatterTimeTickets
	 * @return
	 */
	private List<MatterTimeTicket> populateMatterTimeTickets (
			List<com.mnrclara.api.management.model.dto.IMatterTimeTicket> matterTimeTickets) {
		List<MatterTimeTicket> dbMatterTimeTicketList = new ArrayList<>();
		for (com.mnrclara.api.management.model.dto.IMatterTimeTicket iMatterTimeTicket : matterTimeTickets) {
			MatterTimeTicket dbMatterTimeTicket = new MatterTimeTicket();
			
//			log.info("iMatterTimeTicket.getMatterNumber() : " + iMatterTimeTicket.getMatterNumber());
//			log.info("iMatterTimeTicket.Status ID : " + iMatterTimeTicket.getStatusId());
			
			dbMatterTimeTicket.setClientId(iMatterTimeTicket.getClientId());
			dbMatterTimeTicket.setMatterNumber(iMatterTimeTicket.getMatterNumber());
			dbMatterTimeTicket.setTimeTicketNumber(iMatterTimeTicket.getTimeTicketNumber());
			dbMatterTimeTicket.setTimeTicketDate(iMatterTimeTicket.getTimeTicketDate());
			dbMatterTimeTicket.setTimeKeeperCode(iMatterTimeTicket.getTimeKeeperCode());
			dbMatterTimeTicket.setTimeTicketDescription(iMatterTimeTicket.getTimeTicketDescription());
			dbMatterTimeTicket.setTimeTicketHours(iMatterTimeTicket.getTimeTicketHours());
			dbMatterTimeTicket.setTimeTicketAmount(iMatterTimeTicket.getTimeTicketAmount());
			dbMatterTimeTicket.setBillType(iMatterTimeTicket.getBillType());
			dbMatterTimeTicket.setApprovedBillableTimeInHours(iMatterTimeTicket.getApprovedBillableTimeInHours());
			dbMatterTimeTicket.setApprovedBillableAmount(iMatterTimeTicket.getApprovedBillableAmount());
			dbMatterTimeTicket.setStatusId(iMatterTimeTicket.getStatusId());
			dbMatterTimeTicketList.add(dbMatterTimeTicket);
		}
		return dbMatterTimeTicketList;
	}
}
