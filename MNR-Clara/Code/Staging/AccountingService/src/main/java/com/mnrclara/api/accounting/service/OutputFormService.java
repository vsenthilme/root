package com.mnrclara.api.accounting.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.accounting.model.auth.AuthToken;
import com.mnrclara.api.accounting.model.dto.ActivityCode;
import com.mnrclara.api.accounting.model.dto.BillingFormat;
import com.mnrclara.api.accounting.model.dto.BillingFrequency;
import com.mnrclara.api.accounting.model.dto.BillingMode;
import com.mnrclara.api.accounting.model.dto.CaseCategory;
import com.mnrclara.api.accounting.model.dto.CaseSubcategory;
import com.mnrclara.api.accounting.model.dto.Status;
import com.mnrclara.api.accounting.model.dto.TaskbasedCode;
import com.mnrclara.api.accounting.model.dto.TimekeeperCode;
import com.mnrclara.api.accounting.model.invoice.InvoiceHeader;
import com.mnrclara.api.accounting.model.invoice.InvoiceLine;
import com.mnrclara.api.accounting.model.invoice.PaymentUpdate;
import com.mnrclara.api.accounting.model.management.ClientGeneral;
import com.mnrclara.api.accounting.model.management.MatterAssignment;
import com.mnrclara.api.accounting.model.management.MatterGenAcc;
import com.mnrclara.api.accounting.model.prebill.MatterExpense;
import com.mnrclara.api.accounting.model.prebill.PreBillDetails;
import com.mnrclara.api.accounting.model.prebill.outputform.AccountAgingDetails;
import com.mnrclara.api.accounting.model.prebill.outputform.ExpenseEntry;
import com.mnrclara.api.accounting.model.prebill.outputform.FinalSummary;
import com.mnrclara.api.accounting.model.prebill.outputform.IMatterTimeTicket;
import com.mnrclara.api.accounting.model.prebill.outputform.IMatterTimeTicketInvoice;
import com.mnrclara.api.accounting.model.prebill.outputform.PaymentDetails;
import com.mnrclara.api.accounting.model.prebill.outputform.PreBillOutputForm;
import com.mnrclara.api.accounting.model.prebill.outputform.ReportHeader;
import com.mnrclara.api.accounting.model.prebill.outputform.TimeKeeperSummary;
import com.mnrclara.api.accounting.model.prebill.outputform.TimeTicket;
import com.mnrclara.api.accounting.model.prebill.outputform.TimeTicketDetail;
import com.mnrclara.api.accounting.repository.InvoiceHeaderRepository;
import com.mnrclara.api.accounting.repository.InvoiceLineRepository;
import com.mnrclara.api.accounting.repository.MatterExpenseRepository;
import com.mnrclara.api.accounting.repository.MatterTimeTicketRepository;
import com.mnrclara.api.accounting.repository.PaymentUpdateRepository;
import com.mnrclara.api.accounting.repository.PreBillDetailsRepository;
import com.mnrclara.api.accounting.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OutputFormService {
	
	@Autowired
	PreBillDetailsRepository preBillDetailsRepository;
	
	@Autowired
	MatterTimeTicketRepository matterTimeTicketRepository;
	
	@Autowired
	MatterExpenseRepository matterExpenseRepository;
	
	@Autowired
	PaymentUpdateRepository paymentUpdateRepository;
	
	@Autowired
	InvoiceHeaderRepository invoiceHeaderRepository;
	
	@Autowired
	InvoiceLineRepository invoiceLineRepository;
	
	@Autowired
	ManagementService managementService;
	
	@Autowired
	PreBillDetailsService preBillDetailsService;
	
	@Autowired
	AuthTokenService authTokenService;
	
	@Autowired
	SetupService setupService;
	
	@Autowired
	InvoiceLineService invoiceLineService;
	
	/**
	 * -------------------------------Output-Forms-----SINGLE---------------------------------------
	 * As and when the record is inserted in the PREBILLDETAILS table with STATUS_ID = 45, 
	 * Generate the Report in PDF format. 
	 * 
	 * Fetch the details PREBILL_NO,MATTER_NO,CLASS_ID,CLIENT_ID from PREBILLDETAILS table and 
	 * get the below values
	 * @param preBillNumber 
	 * @return
	 * @throws ParseException 
	 */
	public PreBillOutputForm getPreBillDetailsOutputForm(String preBillNumber, String matterNumber) throws ParseException {
		AuthToken mgmtAuthToken = authTokenService.getManagementServiceAuthToken();
		AuthToken setupAuthToken = authTokenService.getSetupServiceAuthToken();
		PreBillOutputForm preBillOutputForm = new PreBillOutputForm();
		
		PreBillDetails preBillDetails = preBillDetailsService.getPreBillDetails(preBillNumber);
		
		// Fetch Matter General - ClassID, ClientID, MatterNo
		MatterGenAcc matterGenAcc = managementService.getMatterGenAcc(matterNumber, mgmtAuthToken.getAccess_token());
		log.info("matterGenAcc : " + matterGenAcc);
		
		ReportHeader reportHeader = buildHeader (matterGenAcc, preBillDetails, preBillNumber, mgmtAuthToken, setupAuthToken);
		log.info("ReportHeader : " + reportHeader);
		
		preBillOutputForm.setReportHeader(reportHeader);
			
		/*
		 * 2. Time Tickets
		 * ---------------------
		 * Fetch BILL_FORMAT_ID from MATTERGENACC table by passing MATTER_NO. pass BILL_FORMAT_ID and 
		 * Validate REF_FIELD_1 of BILLINGFORMAT table. If the value is TRUE, display the below in report.
		 */
		BillingFormat billingFormat =
				setupService.getBillingFormat(Long.valueOf(matterGenAcc.getBillingFormatId()),
				setupAuthToken.getAccess_token());
		log.info("2.billingFormat : " + billingFormat);
		if (billingFormat != null) {
			TimeTicketDetail timeTicketDetail = 
					buildPreBillTimeTickets (matterGenAcc.getMatterNumber(), preBillDetails.getStatusId(), preBillNumber, billingFormat, setupAuthToken);
			
			preBillOutputForm.setTimeTicketDetail(timeTicketDetail);
		}
		
		/*
		 * 3. Expense Entries
		 * ------------------------
		 * Fetch BILL_FORMAT_ID from MATTERGENACC table by passing MATTER_NO. pass BILL_FORMAT_ID and 
		 * Validate REF_FIELD_2 of BILLINGFORMAT table. If the value is TRUE, display the below in report
		 * 
		 * Pass Matter_No in Matter Expense table to fetch the below details
		 */
		billingFormat = setupService.getBillingFormat(Long.valueOf(matterGenAcc.getBillingFormatId()), 
				setupAuthToken.getAccess_token());
		log.info("3.billingFormat : " + billingFormat);
		if (billingFormat != null) {
			List<ExpenseEntry> expenseEntries = 
					buildExpenseEntries (matterGenAcc.getMatterNumber(), preBillNumber, billingFormat, setupAuthToken);
			preBillOutputForm.setExpenseEntry (expenseEntries);
		}
		
		/*
		 * 4. TimeKeeper Summary 
		 * -----------------------
		 * Fetch BILL_FORMAT_ID from MATTERGENACC table by passing MATTER_NO. pass BILL_FORMAT_ID and 
		 * Validate REF_FIELD_9 of BILLINGFORMAT table. If the value is TRUE, display the below in report
		 */
		billingFormat = setupService.getBillingFormat(Long.valueOf(matterGenAcc.getBillingFormatId()), 
				setupAuthToken.getAccess_token());
		log.info("4.TimeKeeper Summary : " + billingFormat);
		if (billingFormat != null) {
			/*
			 * Select Sum(App_Bill_Time), Sum(App_Bill_Amount) from above MatterTimeTicket 
			 * by passing selected matterNo Groupby(TK_CODE/TK_NAME)
			 */
			try {
				List<IMatterTimeTicket> matterTimeTicket = matterTimeTicketRepository.getTimeKeeperSummary(matterGenAcc.getMatterNumber(),preBillDetails.getPreBillNumber());
				List<TimeKeeperSummary> timeKeeperSummaryList = new ArrayList<>();
				for (IMatterTimeTicket iMatterTimeTicket : matterTimeTicket) {
					TimeKeeperSummary timeKeeperSummary = new TimeKeeperSummary();
					//HAREESH 02/09/2022 got time keeper name in join query
					log.info("MatterNumber, TimeKeeperCode: " + matterGenAcc.getMatterNumber() + "," + iMatterTimeTicket.getTimeKeeperCode());
					Double rate = matterTimeTicketRepository.getAssignedRateFromMatterRate (matterGenAcc.getMatterNumber(), iMatterTimeTicket.getTimeKeeperCode());
					timeKeeperSummary.setTimeTicketName(iMatterTimeTicket.getTimeKeeperName());
					timeKeeperSummary.setBillType(iMatterTimeTicket.getBillType());
					timeKeeperSummary.setTimeTicketAmount(iMatterTimeTicket.getTimeTicketAmount());
					timeKeeperSummary.setTimeTicketHours(iMatterTimeTicket.getTimeTicketHours());
					timeKeeperSummary.setTimeTicketAssignedRate(rate);
					
					double timeTicketTotal = iMatterTimeTicket.getTimeTicketAmount() * iMatterTimeTicket.getTimeTicketHours();
					timeKeeperSummary.setTimeTicketTotal(timeTicketTotal);
					timeKeeperSummaryList.add(timeKeeperSummary);
				}
				preBillOutputForm.setTimeKeeperSummary(timeKeeperSummaryList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * 5. Payment Details
		 * ----------------------
		 * Fetch BILL_FORMAT_ID from MATTERGENACC table by passing MATTER_NO. pass BILL_FORMAT_ID and 
		 * Validate REF_FIELD_3 of BILLINGFORMAT table. If the value is TRUE, display the below in report
		 */
		billingFormat = setupService.getBillingFormat(Long.valueOf(matterGenAcc.getBillingFormatId()), 
				setupAuthToken.getAccess_token());
		var paymentReceivedStartDate = 
				DateUtils.addTimeToSingleFromDateForSearch(LocalDate.ofInstant(preBillDetails.getStartDateForPreBill().toInstant(), 
						ZoneId.systemDefault()));
		var paymentReceivedEndDate = 
				DateUtils.addTimeToSingleToDateForSearch(LocalDate.ofInstant(preBillDetails.getPaymentCutoffDate().toInstant(), 
						ZoneId.systemDefault()));
		var feesCutOffDate = 
				DateUtils.addTimeToSingleToDateForSearch(LocalDate.ofInstant(preBillDetails.getFeesCostCutoffDate().toInstant(), 
						ZoneId.systemDefault()));
		
		log.info("5.Payment Details :-------------> ");
		if (billingFormat != null && billingFormat.getReferenceField3() != null && 
				billingFormat.getReferenceField3().equalsIgnoreCase("true")) {
			List<PaymentDetails> paymentDetailsList = new ArrayList<>();
			
			//------------------------------------------------------------------------------------------------------
			paymentReceivedStartDate = getEndDateForPreBillDetails (matterGenAcc.getMatterNumber(), preBillDetails);
//			paymentReceivedStartDate = DateUtils.addTimeToDate(prebillEndDate, 1);
			paymentReceivedEndDate = preBillDetails.getCreatedOn();
			
			Date[] dates = DateUtils.addTimeToDatesForSQL (paymentReceivedStartDate, paymentReceivedEndDate);
			paymentReceivedStartDate = dates[0];
			paymentReceivedEndDate = dates [1];
			
			log.info("5.1------>paymentReceivedStartDate : " + paymentReceivedStartDate);
			log.info("5.2------>paymentReceivedEndDate : " + paymentReceivedEndDate + "," + matterNumber);
			
			// Exact MatterNumber
			List<PaymentUpdate> paymentUpdateList = 
					paymentUpdateRepository.findByMatterNumberAndPaymentDateBetweenAndDeletionIndicator (matterNumber,
					paymentReceivedStartDate, paymentReceivedEndDate, 0L);
			log.info("5.2------>paymentUpdateList : " + paymentUpdateList);
			
			//------------------------------------------------------------------------------------------------------
			
			for (PaymentUpdate paymentUpdate : paymentUpdateList) {
				PaymentDetails paymentDetails = new PaymentDetails();
				log.info("5.11---------->Payment Details : " + paymentDetails);
				// Description
				if (paymentUpdate.getPaymentText() != null) {
					paymentDetails.setDescription(paymentUpdate.getPaymentText());
				}
				
				// Date
				if (paymentUpdate.getPaymentDate() != null) {
					paymentDetails.setPostingDate(paymentUpdate.getPaymentDate());
				}
				
				// Amount
				if (paymentUpdate.getPaymentAmount() != null && paymentUpdate.getPaymentAmount() != 0D) {
					paymentDetails.setAmount(paymentUpdate.getPaymentAmount());
				}
				
				paymentDetailsList.add(paymentDetails);
			}
			preBillOutputForm.setPaymentDetail(paymentDetailsList);
		}
		
		/*
		 * 6. Account Aging Details
		 * -----------------------------
		 */
		AccountAgingDetails accountAgingDetails = buildTimeTicketAmount (matterGenAcc.getMatterNumber());
		preBillOutputForm.setAccountAgingDetail(accountAgingDetails);
		
		/*
		 * 7. Final Summary
		 */
		FinalSummary finalSummary = new FinalSummary();
		billingFormat = setupService.getBillingFormat(Long.valueOf(matterGenAcc.getBillingFormatId()), 
				setupAuthToken.getAccess_token());
		log.info("7.billingFormat : " + billingFormat);
		if (billingFormat != null && billingFormat.getReferenceField5() != null && 
				billingFormat.getReferenceField5().equalsIgnoreCase("true")) {
			// Prior Balance
			Date prebillEndDate = getEndDateForPreBillDetails (matterGenAcc.getMatterNumber(), preBillDetails);
			Double priorBalance = getPrebillDetailsPriorBalance (matterGenAcc.getMatterNumber(), prebillEndDate);
			log.info("7a-----priorBalance----> : " + priorBalance);
			
			// Payment Received
			// Pass MATTER_NO in PAYMENTUPDATE table and fetch sum of PAYMENT_AMT values
			Double paymentReceived = getPaymentReceived (matterGenAcc.getMatterNumber(), paymentReceivedStartDate, paymentReceivedEndDate);
			finalSummary.setPriorBalance(priorBalance);
			finalSummary.setPaymentReceived(paymentReceived);
		}
			
		// Date of Last Payment
		log.info("7b.billingFormat : " + billingFormat);
		
		Date paymentDate = getTopRecord (matterGenAcc.getMatterNumber(), paymentReceivedStartDate, feesCutOffDate);
		log.info("paymentUpdate : " + paymentDate);
		if (paymentDate != null) {
			finalSummary.setDateOfLastPayment(paymentDate);
		}
		
		// Current Fees
		if (matterGenAcc.getBillingModeId().equalsIgnoreCase("1")) {
			Double currentFees = matterTimeTicketRepository.getCurrentFees(preBillNumber);
			finalSummary.setCurrentFees(currentFees);
		} else if (matterGenAcc.getBillingModeId().equalsIgnoreCase("2")) {
			finalSummary.setCurrentFees(matterGenAcc.getFlatFeeAmount());
		}
		
		// Advanced Cost
		Double advancedCost = matterExpenseRepository.getSumOfExpenseAmount(preBillNumber);
		finalSummary.setAdvancedCost(advancedCost);
		preBillOutputForm.setFinalSummary(finalSummary);
		
		return preBillOutputForm;
	}
	
	/**
	 * 
	 * @param matterNumber
	 * @param invoiceNumber
	 * @return
	 * @throws ParseException 
	 */
	private Date getStartDate (String matterNumber, String invoiceNumber) throws ParseException {
		List<InvoiceHeader> invoiceHeaderList = invoiceHeaderRepository.findByMatterNumberAndDeletionIndicator(matterNumber, 0L);
		if (!invoiceHeaderList.isEmpty() && invoiceHeaderList.size() == 1) {
			String preBillNumber = invoiceHeaderList.get(0).getPreBillNumber();
			PreBillDetails preBillDetails = preBillDetailsRepository.findByPreBillNumber(preBillNumber);
			return preBillDetails.getStartDateForPreBill();
		} else {
			List<String> listInvoiceNumbers = invoiceHeaderList.stream().map(InvoiceHeader::getInvoiceNumber).collect(Collectors.toList());
			List<Integer> listInvoiceNumberInt = listInvoiceNumbers.stream().map(Integer::parseInt).collect(Collectors.toList());
			
			Date prevInvNumberDate = getPreviousIndex (listInvoiceNumberInt, invoiceNumber);
			return prevInvNumberDate;
		}
	}
	
	/**
	 * 
	 * @param matterNumber
	 * @param preBillDetails2
	 * @return
	 * @throws ParseException
	 */
	private Date getEndDateForPreBillDetails (String matterNumber, PreBillDetails preBillDetails2) throws ParseException {
		List<InvoiceHeader> invoiceHeaderList = invoiceHeaderRepository.findByMatterNumberAndDeletionIndicator(matterNumber, 0L);
		if (invoiceHeaderList != null && invoiceHeaderList.isEmpty()) {
//			return preBillDetails2.getCreatedOn();
			return DateUtils.convertStringToDate ("2022-07-01");
		} else {
			List<String> listInvoiceNumbers = invoiceHeaderList.stream().map(InvoiceHeader::getInvoiceNumber).collect(Collectors.toList());
			List<Integer> listInvoiceNumberInt = listInvoiceNumbers.stream().map(Integer::parseInt).collect(Collectors.toList());
			Date prevInvNumberDate = getCreatedOnFromLatestInvoiceNumber (listInvoiceNumberInt);
			prevInvNumberDate = DateUtils.addTimeToDate(prevInvNumberDate, 1);
			return prevInvNumberDate;
		}
	}
	
	/**
	 * 
	 * @param matterNumber
	 * @param invoiceNumber
	 * @return
	 * @throws ParseException
	 */
	private Date getEndDateForPriorBalance (String matterNumber, String invoiceNumber) throws ParseException {
		String INIT_DATE = "2022-07-01";
		
		List<InvoiceHeader> invoiceHeaderList = invoiceHeaderRepository.findByMatterNumberAndDeletionIndicator(matterNumber, 0L);
		if (!invoiceHeaderList.isEmpty() && invoiceHeaderList.size() == 1) {
			return DateUtils.convertStringToDate (INIT_DATE);
		} else {
			List<String> listInvoiceNumbers = invoiceHeaderList.stream().map(InvoiceHeader::getInvoiceNumber).collect(Collectors.toList());
			List<Integer> listInvoiceNumberInt = listInvoiceNumbers.stream().map(Integer::parseInt).collect(Collectors.toList());
			Date prevInvNumberDate = getCreatedOnFromPreviousInvoiceNumber (listInvoiceNumberInt, invoiceNumber);
			
			prevInvNumberDate = DateUtils.addTimeToDate(prevInvNumberDate, 1);
			log.info("prevInvNumberDate : " + prevInvNumberDate);
			
			SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = sdformat.parse(INIT_DATE);
			
			String formattedInvDate = sdformat.format(prevInvNumberDate);
			Date date2 = sdformat.parse(formattedInvDate);
			
			if (date1.compareTo(date2) > 0) {
				return DateUtils.convertStringToDate (INIT_DATE);
			}
			return prevInvNumberDate;
		}
	}
	
	/**
	 * 
	 * @param listInvoiceNumbers
	 * @param invoiceNumber
	 * @return
	 * @throws ParseException 
	 */
	private Date getPreviousIndex (List<Integer> listInvoiceNumbers, String invoiceNumber) throws ParseException {
		Date date = invoiceHeaderRepository.getPreviosInvoiceDate (listInvoiceNumbers, invoiceNumber);
		log.info("---------#2---------date : " + date);
		return date;
	}
	
	/**
	 * 
	 * @param listInvoiceNumbers
	 * @return
	 * @throws ParseException
	 */
	private Date getCreatedOnFromLatestInvoiceNumber (List<Integer> listInvoiceNumbers) throws ParseException {
		Collections.sort(listInvoiceNumbers);
		long latestInvoiceNumber = listInvoiceNumbers.get(listInvoiceNumbers.size() - 1);
		log.info("latestInvoiceNumber : " + latestInvoiceNumber);
		
		InvoiceHeader invoiceHeader = invoiceHeaderRepository.findByInvoiceNumber(String.valueOf(latestInvoiceNumber));
		LocalDate ldtstartDate = LocalDate.ofInstant(invoiceHeader.getCreatedOn().toInstant(), ZoneId.systemDefault());
		return DateUtils.addTimeToSingleToDateForSearch(ldtstartDate);
	}
	
	/**
	 * 
	 * @param listInvoiceNumbers
	 * @param invoiceNumber
	 * @return
	 * @throws ParseException
	 */
	private Date getCreatedOnFromPreviousInvoiceNumber (List<Integer> listInvoiceNumbers, String invoiceNumber) throws ParseException {
		Collections.sort(listInvoiceNumbers);
		log.info("listInvoiceNumbers : " + listInvoiceNumbers);
		for (int i = 0; i < listInvoiceNumbers.size(); i ++){
			if ( listInvoiceNumbers.get(i) == Integer.parseInt(invoiceNumber)) {
				log.info("invNumber : " + listInvoiceNumbers.get(i));
				if (i == 0) {		// If it is first index itself then return the same index.
//					InvoiceHeader invoiceHeader = invoiceHeaderRepository.findByInvoiceNumber(String.valueOf(listInvoiceNumbers.get(i)));
//					LocalDate ldtstartDate = LocalDate.ofInstant(invoiceHeader.getCreatedOn().toInstant(), ZoneId.systemDefault());
//					return DateUtils.addTimeToSingleToDateForSearch(ldtstartDate);
					String date = "2022-07-01";
				    LocalDate localDate = LocalDate.parse(date);
					return DateUtils.addTimeToSingleToDateForSearch(localDate);
				}
				InvoiceHeader invoiceHeader = invoiceHeaderRepository.findByInvoiceNumber(String.valueOf(listInvoiceNumbers.get(--i)));
				LocalDate ldtstartDate = LocalDate.ofInstant(invoiceHeader.getCreatedOn().toInstant(), ZoneId.systemDefault());
				return DateUtils.addTimeToSingleToDateForSearch(ldtstartDate);
			}
		}
		return null;
	}
	
	/**
	 * ---------------------Invoice Outputform----------------------------------------------------------------------
	 * As and when the record is inserted in the INVOICEHEADER and INVOICELINE tables, Generate the Report in PDF 
	 * format. Store the report Under folder BILLING/INVOICES/ CLIENT_ID/MATTER_NO and the filename with INVOICE_NO & 
	 * Date and Time Fetch the details PREBILL_NO,MATTER_NO,CLASS_ID,CLIENT_ID from INVOICEHEADER and INVOICELINE
	 * tables and get the below values
	 * @param invoiceNumber
	 * @return
	 */
	public PreBillOutputForm getInvoiceOutputForm (String invoiceNumber) {
		AuthToken mgmtAuthToken = authTokenService.getManagementServiceAuthToken();
		AuthToken setupAuthToken = authTokenService.getSetupServiceAuthToken();
		PreBillOutputForm preBillOutputForm = new PreBillOutputForm();
		
		try {
			// Fetch the details PREBILL_NO,MATTER_NO,CLASS_ID,CLIENT_ID from INVOICEHEADER and INVOICELINE tables and 
			// get the below values
			InvoiceHeader invoiceHeader = invoiceHeaderRepository.findByInvoiceNumber(invoiceNumber);
			
			// Fetch Matter Details
			MatterGenAcc matterGenAcc = managementService.getMatterGenAcc(invoiceHeader.getMatterNumber(), 
					mgmtAuthToken.getAccess_token());
					
			ReportHeader reportHeader = buildInvoiceHeader (invoiceHeader, matterGenAcc, mgmtAuthToken, setupAuthToken);
			log.info("ReportHeader : " + reportHeader);
			preBillOutputForm.setReportHeader(reportHeader);
			
			/*
			 * 2. Time Tickets
			 * ---------------------
			 * (Validate REF_FIELD_1 of BILLINGFORMAT table. If the value is TRUE, display the below in report) or
			 * (If BILL_MODE_ID of the MATTER_NO is 2 in MATTERGENACC table , then this is not to be displayed)
			 * Based on the above user input select the Pre_bill_No from the PreBillDetails and pass to 
			 * MatterTimeTicket (in field Ref_Field_1) to fetch the below details
			 */
			
			Long statusId = 0L;
			BillingFormat billingFormat = 
					setupService.getBillingFormat(Long.valueOf(matterGenAcc.getBillingFormatId()), 
					setupAuthToken.getAccess_token());
			log.info("2.TimeTicketDetail : " + billingFormat);
			if (billingFormat != null && billingFormat.getReferenceField1() != null && billingFormat.getReferenceField1().equalsIgnoreCase("true")){
				if(!matterGenAcc.getBillingModeId().equalsIgnoreCase("2")){
					TimeTicketDetail timeTicketDetail = buildInvoiceTimeTickets (matterGenAcc.getMatterNumber(), statusId,
							invoiceHeader.getPreBillNumber(), billingFormat, setupAuthToken);
					preBillOutputForm.setTimeTicketDetail(timeTicketDetail);
				}
			}
			
			/*
			 * 3. Expense Entries
			 * ------------------------
			 * Fetch BILL_FORMAT_ID from MATTERGENACC table by passing MATTER_NO. pass BILL_FORMAT_ID and 
			 * Validate REF_FIELD_2 of BILLINGFORMAT table. If the value is TRUE, display the below in report
			 * 
			 * Pass Matter_No in Matter Expense table to fetch the below details
			 */
			billingFormat = setupService.getBillingFormat(Long.valueOf(matterGenAcc.getBillingFormatId()), 
					setupAuthToken.getAccess_token());
			log.info("3.Expense Entries ------------------>");
			if (billingFormat != null && billingFormat.getReferenceField2() != null && 
					billingFormat.getReferenceField2().equalsIgnoreCase("true")) {
				List<ExpenseEntry> expenseEntries = 
						buildExpenseEntries (matterGenAcc.getMatterNumber(), invoiceHeader.getPreBillNumber(), 
								billingFormat, setupAuthToken);
				preBillOutputForm.setExpenseEntry (expenseEntries);
			}
			
			/*
			 * 4. TimeKeeper Summary 
			 * -----------------------
			 * Fetch BILL_FORMAT_ID from MATTERGENACC table by passing MATTER_NO. pass BILL_FORMAT_ID and 
			 * Validate REF_FIELD_9 of BILLINGFORMAT table. If the value is TRUE, display the below in report
			 */
			billingFormat = setupService.getBillingFormat(Long.valueOf(matterGenAcc.getBillingFormatId()), 
					setupAuthToken.getAccess_token());
			log.info("4.TimeKeeper Summary ----------------->");
			if (billingFormat != null && billingFormat.getReferenceField9() != null && 
					billingFormat.getReferenceField9().equalsIgnoreCase("true")) {
				/*
				 * Select Sum(App_Bill_Time), Sum(App_Bill_Amount) from above MatterTimeTicket
				 * by passing selected matterNo Groupby(TK_CODE/TK_NAME)
				 */
				List<IMatterTimeTicketInvoice> matterTimeTicket = 
						matterTimeTicketRepository.getTimeKeeperSummaryForInvoice (matterGenAcc.getMatterNumber(),invoiceHeader.getPreBillNumber());
				List<TimeKeeperSummary> timeKeeperSummaryList = new ArrayList<>();
				for (IMatterTimeTicketInvoice iMatterTimeTicket : matterTimeTicket) {
					TimeKeeperSummary timeKeeperSummary = new TimeKeeperSummary();
					TimekeeperCode TimekeeperCode = setupService.getTimekeeperCode(iMatterTimeTicket.getTimeKeeperCode(), setupAuthToken.getAccess_token());
					timeKeeperSummary.setTimeTicketName(TimekeeperCode.getTimekeeperName());
					timeKeeperSummary.setTimeTicketCode(TimekeeperCode.getTimekeeperCode());
					timeKeeperSummary.setTimeTicketAmount(iMatterTimeTicket.getApprovedBillableAmount());
					timeKeeperSummary.setTimeTicketHours(iMatterTimeTicket.getApprovedBillableHours());
					timeKeeperSummary.setBillType(iMatterTimeTicket.getBillType());

					//UserTypeIdDescription - later added in code as requested by user
					timeKeeperSummary.setUserTypeDescription(
							matterTimeTicketRepository.getUserTypeIdDescription(TimekeeperCode.getUserTypeId()));
					
					Double assignedRate = matterTimeTicketRepository.getAssignedRateFromMatterRate(matterGenAcc.getMatterNumber(), iMatterTimeTicket.getTimeKeeperCode());
					timeKeeperSummary.setTimeTicketAssignedRate(assignedRate);
					
					Double timeTicketTotal = iMatterTimeTicket.getApprovedBillableAmount() * iMatterTimeTicket.getApprovedBillableHours();
					timeKeeperSummary.setTimeTicketTotal(timeTicketTotal);
					timeKeeperSummaryList.add(timeKeeperSummary);
				}
				
				preBillOutputForm.setTimeKeeperSummary(timeKeeperSummaryList);
			}
			
			PreBillDetails preBillDetails = preBillDetailsService.getPreBillDetails(invoiceHeader.getPreBillNumber());
			
			var paymentReceivedStartDate = 
					DateUtils.addTimeToSingleFromDateForSearch(LocalDate.ofInstant(preBillDetails.getStartDateForPreBill().toInstant(), 
							ZoneId.systemDefault()));
			var paymentReceivedEndDate = 
					DateUtils.addTimeToSingleToDateForSearch(LocalDate.ofInstant(preBillDetails.getPaymentCutoffDate().toInstant(), 
							ZoneId.systemDefault()));
			/*
			 * 5. Payment Details
			 * ----------------------
			 * Fetch BILL_FORMAT_ID from MATTERGENACC table by passing MATTER_NO. pass BILL_FORMAT_ID and 
			 * Validate REF_FIELD_3 of BILLINGFORMAT table. If the value is TRUE, display the below in report
			 */
			billingFormat = setupService.getBillingFormat(Long.valueOf(matterGenAcc.getBillingFormatId()), 
					setupAuthToken.getAccess_token());
			log.info("5.Payment Details --------------------->");
			try {
				if (billingFormat != null && billingFormat.getReferenceField3() != null && 
						billingFormat.getReferenceField3().equalsIgnoreCase("true")) {
					List<PaymentDetails> paymentDetailsList = new ArrayList<>();
					
					paymentReceivedStartDate = getEndDateForPriorBalance (invoiceHeader.getMatterNumber(), invoiceNumber);
//					paymentReceivedStartDate = DateUtils.addTimeToDate(prebillEndDate, 1);
					paymentReceivedEndDate = invoiceHeader.getCreatedOn();
					
					Date[] dates = DateUtils.addTimeToDatesForSQL (paymentReceivedStartDate, paymentReceivedEndDate);
					paymentReceivedStartDate = dates[0];
					paymentReceivedEndDate = dates [1];
					
					log.info("5.1------>paymentReceivedStartDate : " + paymentReceivedStartDate);
					log.info("5.2------>paymentReceivedEndDate : " + paymentReceivedEndDate);
					
					// Exact MatterNumber
					List<PaymentUpdate> paymentUpdateList = 
							paymentUpdateRepository.findByMatterNumberAndPaymentDateBetweenAndDeletionIndicator(matterGenAcc.getMatterNumber(),
							paymentReceivedStartDate, paymentReceivedEndDate, 0L);
					log.info("5.1------>paymentUpdateList : " + paymentUpdateList);
					for (PaymentUpdate paymentUpdate : paymentUpdateList) {
						PaymentDetails paymentDetails = new PaymentDetails();
						
						// Description
						if (paymentUpdate.getPaymentText() != null) {
							paymentDetails.setDescription(paymentUpdate.getPaymentText());
						}
						
						// Date
						if (paymentUpdate.getPaymentDate() != null) {
							paymentDetails.setPostingDate(paymentUpdate.getPaymentDate());
						}
						
						// Amount
						if (paymentUpdate.getPaymentAmount() != null && paymentUpdate.getPaymentAmount() != 0D) {
							paymentDetails.setAmount(paymentUpdate.getPaymentAmount());
						}
						
						paymentDetails.setFeesCutoffDate(preBillDetails.getFeesCostCutoffDate());
						paymentDetails.setStartDateForPreBill(paymentReceivedStartDate);
						paymentDetails.setPaymentCutoffDate(paymentReceivedEndDate);
						paymentDetailsList.add(paymentDetails);
					}
					preBillOutputForm.setPaymentDetail(paymentDetailsList);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 * 7. Final Summary
			 */
			//HAREESH - 2022-10-02 - (CLARA/AMS/2022/118)
			if(invoiceHeader != null) {
				if(invoiceHeader.getInvoiceDate() != null) {
					Date dateBeforeRangeForInvoiceDate = getStartDate (invoiceHeader.getMatterNumber(), invoiceNumber);
					log.info("7.1--->dateBeforeRangeForInvoiceDate : " + dateBeforeRangeForInvoiceDate);
					
					// Prior Balance
					FinalSummary finalSummary = new FinalSummary();
					billingFormat = setupService.getBillingFormat(Long.valueOf(matterGenAcc.getBillingFormatId()),
							setupAuthToken.getAccess_token());
					
					log.info("6.billingFormat : " + billingFormat);
					if (billingFormat != null && billingFormat.getReferenceField5() != null &&
							billingFormat.getReferenceField5().equalsIgnoreCase("true")) {
						Date prebillEndDate = getEndDateForPriorBalance (invoiceHeader.getMatterNumber(), invoiceNumber);
						log.info("6.01----prebillEndDate---> : " + prebillEndDate);
						
						Double priorBalance = getInvoicePriorBalance (matterGenAcc.getMatterNumber(), prebillEndDate, dateBeforeRangeForInvoiceDate);
						log.info("6.02----dateBeforeRangeForInvoiceDate---> : " + dateBeforeRangeForInvoiceDate);
						
						log.info("priorBalance : " + priorBalance);
						finalSummary.setPriorBalance(priorBalance);

						// Payment Received
						if(preBillDetails != null && preBillDetails.getPaymentCutoffDate() != null) {
							log.info("6.1---getPaymentReceivedForInvoice : " + matterGenAcc.getMatterNumber());
							
							/*
							 * From Date: 
								Corresponding matter no is passed in invoice table to find if there are any previous invoices for that matter.
								 If yes, then CTD_ON from the last invoice is taken, add+1 and this will be FROM date.
								Else
								consider FROM date as Bill start date from the Prebill.
								TO Date:
								Pass Invoice NO in INVOICEHEADER table and fetch CTD_ON date.
							 */
							paymentReceivedStartDate = DateUtils.addTimeToDate(prebillEndDate, 1);
							paymentReceivedEndDate = invoiceHeader.getCreatedOn();
							
							Date[] dates = DateUtils.addTimeToDatesForSQL (paymentReceivedStartDate, paymentReceivedEndDate);
							paymentReceivedStartDate = dates[0];
							paymentReceivedEndDate = dates [1];
							
							Double paymentReceived = getPaymentReceivedForInvoice (matterGenAcc.getMatterNumber(), paymentReceivedStartDate, paymentReceivedEndDate);
							log.info("6.3y---paymentReceived : " + paymentReceived);
							
							if (paymentReceived == null) {
								paymentReceived = 0D;
							}
							
							finalSummary.setPaymentReceived(paymentReceived);
						}
					}

					// Date of Last Payment
					log.info("7.billingFormat : " + billingFormat);

					// Pass MATTER_NO in PAYMENTUPDATE table and fetch POSTING_DATE value
					// for the latest record
					Date paymentUpdate = getTopRecord (matterGenAcc.getMatterNumber(), paymentReceivedStartDate, paymentReceivedEndDate);
					log.info("paymentUpdate : " + paymentUpdate);
					if (paymentUpdate != null) {
						finalSummary.setDateOfLastPayment(paymentUpdate);
					}

					// Current Fees
					InvoiceLine invoiceLine = invoiceLineRepository.findByInvoiceNumberAndItemNumber(invoiceNumber, 1L);
					finalSummary.setCurrentFees(invoiceLine.getBillableAmount());

					// Advanced Cost
					invoiceLine = invoiceLineRepository.findByInvoiceNumberAndItemNumber(invoiceNumber, 2L);
					finalSummary.setAdvancedCost(invoiceLine.getBillableAmount());

					log.info("finalSummary : " + finalSummary);
					preBillOutputForm.setFinalSummary(finalSummary);
				}
			}
			log.info("preBillOutputForm=======> : " + preBillOutputForm);
			return preBillOutputForm;
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
	private Date getTopRecord (String matterNumber, Date startDate, Date feesCutOffDate) {
		matterNumber = "%" + matterNumber + "%";
		return paymentUpdateRepository.getPaymentDate(matterNumber, startDate, feesCutOffDate);
	}
	
	/**
	 * 
	 * @param matterNumber
	 * @param feesCutOffDate 
	 * @param startDate 
	 * @return
	 */
	private Double getPaymentReceived (String matterNumber, Date startDate, Date paymentCutOffDate) {
		return paymentUpdateRepository.getPmtRec(matterNumber, startDate, paymentCutOffDate);
	}
	
	//HAREESH - 2022-10-02 - (CLARA/AMS/2022/118)
	private Double getPaymentReceivedForInvoice (String matterNumber, Date startDate, Date paymentCutOffDate) {
		return invoiceHeaderRepository.getPaymentReceivedForInvoice(matterNumber, startDate, paymentCutOffDate);
	}
	
	/**
	 * 
	 * @param matterNumber
	 * @param preBillNumber
	 * @param dateBeforeRange
	 * @return
	 */
	private Double getInvoicePriorBalance(String matterNumber, Date preBillEndDate, Date dateBeforeRange) {
		return invoiceHeaderRepository.getInvoicePriorBalance(matterNumber, preBillEndDate, dateBeforeRange);
	}
	
	/**
	 * 
	 * @param matterNumber
	 * @param preBillNumber
	 * @return
	 */
	private Double getPrebillDetailsPriorBalance(String matterNumber, Date endDate) {
		return invoiceHeaderRepository.getPrebillDetailsPriorBalance(matterNumber, endDate);
	}
	
	/**
	 * 
	 * @param matterNumber
	 * @param preBillNumber
	 * @return
	 */
	private List<Double> getSumOfPaymentAmountByMatterNumberLike (String matterNumber, String preBillNumber) {
		String matterNumberLike = "%" + matterNumber + "%";
		return paymentUpdateRepository.getSumOfPaymentAmountByMatterNumberLike(matterNumber, matterNumberLike, preBillNumber);
	}
	
	/**
	 * 
	 * @param matterNumber
	 * @return
	 */
	private Double getRemainingBalanaceForPB(String matterNumber) {
		return invoiceHeaderRepository.getRemBal(matterNumber);
	}

	/**
	 * 
	 * @param matterNumber
	 * @return
	 */
	private AccountAgingDetails buildTimeTicketAmount(String matterNumber) {
		AccountAgingDetails accountAgingDetails = new AccountAgingDetails();
		/*
		 * Current
		 * ---------------
		 * Pass MATTER_NO in MATTERTIMETICKET table and CTD_ON as (TO_DATE = CURRENT DATE and 
		 * FROM_DATE = CURRENT DATE - 29) and fetch APP_BILL_AMOUNT for STATUS_ID = 33 and 34. 
		 * If APP_BILL_AMOUNT field value is NULL fetch the value from TIME_TICKET_AMOUNT
		 */
		List<Double> current = matterTimeTicketRepository.getAccountAgingDetails(-1L, 29L, matterNumber);
		List<Double> days_30_60 = matterTimeTicketRepository.getAccountAgingDetails(30L, 60L, matterNumber);
		List<Double> days_60_90 = matterTimeTicketRepository.getAccountAgingDetails(61L, 90L, matterNumber);
		List<Double> days_90_120 = matterTimeTicketRepository.getAccountAgingDetails(90L, 8186L, matterNumber);
		
		if (!current.isEmpty()){
			Double hardCost = matterExpenseRepository.findHarcCostExpAmount(-1L, 29L, matterNumber);
			Double softCost = matterExpenseRepository.findSoftCostExpAmount(-1L, 29L, matterNumber);
			if (hardCost == null) {
				hardCost = 0D;
			}
			
			if (softCost == null) {
				softCost = 0D;
			}
			
			if (current.get(0) != null) {
				Double amount = current.get(0) + hardCost + softCost;
				accountAgingDetails.setCurrentAmount(amount);
			}
		}
			
		if (!days_30_60.isEmpty()){
			Double hardCost = matterExpenseRepository.findHarcCostExpAmount(30L, 60L, matterNumber);
			Double softCost = matterExpenseRepository.findSoftCostExpAmount(30L, 60L, matterNumber);
			if (hardCost == null) {
				hardCost = 0D;
			}
			
			if (softCost == null) {
				softCost = 0D;
			}
			
			if (days_30_60.get(0) != null) {
				Double amount = days_30_60.get(0) + hardCost + softCost;
				accountAgingDetails.setCurrentAmount(amount);
			}
		}
		
		if (!days_60_90.isEmpty()){
			Double hardCost = matterExpenseRepository.findHarcCostExpAmount(61L, 90L, matterNumber);
			Double softCost = matterExpenseRepository.findSoftCostExpAmount(61L, 90L, matterNumber);
			if (hardCost == null) {
				hardCost = 0D;
			}
			
			if (softCost == null) {
				softCost = 0D;
			}
			
			if (days_60_90.get(0) != null) {
				Double amount = days_60_90.get(0) + hardCost + softCost;
				accountAgingDetails.setCurrentAmount(amount);
			}
		}
		
		if (!days_90_120.isEmpty()){
			Double hardCost = matterExpenseRepository.findHarcCostExpAmount(90L, 8186L, matterNumber);
			Double softCost = matterExpenseRepository.findSoftCostExpAmount(90L, 8186L, matterNumber);
			if (hardCost == null) {
				hardCost = 0D;
			}
			
			if (softCost == null) {
				softCost = 0D;
			}
			
			if (days_60_90.get(0) != null) {
				Double amount = days_60_90.get(0) + hardCost + softCost;
				accountAgingDetails.setCurrentAmount(amount);
			}
		}
		
		/*
		 * Fees Billed to Date
		 * -------------------------
		 * Pass MATTER_NO in INVOICELINE table and fetch sum of BILL_AMT values where ITEM_NO=1
		 */
		Double feesAmount = getBilledAmount (matterNumber, 1L);
		
		/*
		 * Costs Billed to Date
		 * -----------------------
		 * Pass MATTER_NO in INVOICELINE table and fetch sum of BILL_AMT values where ITEM_NO=2
		 */
		Double costsAmount = getBilledAmount (matterNumber, 2L);
		accountAgingDetails.setFeesBilledToDate(feesAmount);
		accountAgingDetails.setCostsBilledToDate(costsAmount);
		return accountAgingDetails;
	}
	
	
	/**
	 * 
	 * @param matterNumber
	 * @param itemNumber
	 * @return
	 */
	private Double getBilledAmount(String matterNumber, Long itemNumber) {
		return invoiceLineRepository.getBilledAmount(matterNumber, itemNumber);
	}

	/**
	 * 
	 * @param fromDiff
	 * @param toDiff
	 * @return
	 */
	private List<Double> getInvoiceAgingDetils(Long fromDiff, Long toDiff) {
		List<Double> amountValues = invoiceHeaderRepository.getAccountAgingDetails(fromDiff, toDiff);
		return amountValues;
	}

	/**
	 * buildExpenseEntries
	 * @param matterNumber
	 * @param billingFormat
	 * @param setupAuthToken
	 * @return
	 */
	private List<ExpenseEntry> buildExpenseEntries(String matterNumber, String preBillNumber, BillingFormat billingFormat,
			AuthToken setupAuthToken) {
		List<MatterExpense> matterExpenses = 
				matterExpenseRepository.findByMatterNumberAndReferenceField1(matterNumber, preBillNumber);
		List<ExpenseEntry> expenseEntryList = new ArrayList<>();
		for (MatterExpense matterExpense : matterExpenses) {
			ExpenseEntry expenseEntry = new ExpenseEntry();
			
			// Expense Number
			expenseEntry.setMatterExpenseId(matterExpense.getMatterExpenseId());
			
			// Expense date
			expenseEntry.setCreatedOn(matterExpense.getReferenceField2());
			
			// Expense Text
			expenseEntry.setExpenseDescription(matterExpense.getExpenseDescription());
			
			// Expense Items
			expenseEntry.setNumberofItems(matterExpense.getNumberofItems());
			
			// Expense Amount
			expenseEntry.setExpenseAmount(matterExpense.getExpenseAmount());
			
			// Bill type
			expenseEntry.setBillType(matterExpense.getBillType());

			expenseEntry.setExpenseCode(matterExpense.getExpenseCode());
			
			expenseEntryList.add(expenseEntry);
		}
		return expenseEntryList;
	}

	/**
	 * buildPreBillTimeTickets
	 * @param matterNumber
	 * @param preBillStatusId 
	 * @param billingFormat
	 * @param setupAuthToken
	 * @return
	 */
	private TimeTicketDetail buildPreBillTimeTickets(String matterNumber, Long preBillStatusId, String preBillNumber,
			BillingFormat billingFormat, AuthToken setupAuthToken) {
		// Pass Matter_No in MatterTimeTicket table to fetch the below details
		List<com.mnrclara.api.accounting.model.prebill.MatterTimeTicket> matterTimeTickets = 
				matterTimeTicketRepository.findByMatterNumberAndReferenceField1OrderByTimeTicketDateAscTimeTicketNumberAsc(matterNumber, preBillNumber);
		
		List<TimeTicket> timeTickets = new ArrayList<>();
		double totalHours = 0;
		double totalAmount = 0;
		double totalBillableAmount = 0;
		double totalBillableHours = 0;
		
		for (com.mnrclara.api.accounting.model.prebill.MatterTimeTicket ticket : matterTimeTickets) {
			TimeTicket timeTicket = new TimeTicket();
			
			// Ticket_No
			timeTicket.setTimeTicketNumber(ticket.getTimeTicketNumber());
			
			// Timekeeper_Code
			timeTicket.setTimeTicketName(ticket.getTimeKeeperCode());
			
			// Time ticket Date
			timeTicket.setTimeTicketDate(ticket.getTimeTicketDate());
			
			// CTD_ON
			timeTicket.setCreatedOn(ticket.getCreatedOn());
			
			// Ref_Field_4
			timeTicket.setTicketDescription(ticket.getReferenceField4());
			
			// App_Bill_Amount
			if (ticket.getApprovedBillableAmount() != null) {
				timeTicket.setBillableAmount(ticket.getApprovedBillableAmount());
				totalAmount = totalAmount + ticket.getApprovedBillableAmount();
				if(ticket.getBillType().equalsIgnoreCase("Billable")){
					totalBillableAmount = totalBillableAmount + ticket.getApprovedBillableAmount();
				}
			}
			
			if (ticket.getTimeTicketHours() != null) {
				timeTicket.setBillableTimeInHours(ticket.getApprovedBillableTimeInHours());
				totalHours = totalHours + ticket.getApprovedBillableTimeInHours();
				if(ticket.getBillType().equalsIgnoreCase("Billable")){
					totalBillableHours = totalBillableHours + ticket.getApprovedBillableTimeInHours();
				}
			}
			
			// Bill_Type
			timeTicket.setBillType(ticket.getBillType());
			
			/*
			 * ACTIVITY_CODE/ACTIVITY_TEXT
			 * --------------------------------
			 * 1. Pass BILL_FORMAT_ID fetched from MATTERGENACC table into BILLINGFORMAT table and 
			 * check the Value of REF_FIELD_6.
			 * 2. If Value is True, then Pass MATTER_NO into table MatterTimeTicket and fetch ACTIVITY_CODE. 
			 * 3. Pass ACTIVITY CODE into ACTIVITYCODE table and fetch ACTIVITY_TEXT
			 */
			log.info("ticket -----getReferenceField6--------------> : " + billingFormat.getReferenceField6());
			if (billingFormat != null && billingFormat.getReferenceField6().equalsIgnoreCase("true")) {
				log.info("ticket -------------------> : " + ticket);
				if (ticket.getActivityCode() != null) {
					ActivityCode activityCode = 
							setupService.getActivityCode(ticket.getActivityCode(),
									setupAuthToken.getAccess_token());
					timeTicket.setActivityCode(activityCode.getActivityCode());
					timeTicket.setActivityText(activityCode.getActivityCodeDescription());
				}
				
				/*
				 * TASK_CODE/TASK_TEXT
				 * ------------------------
				 * 1. Pass BILL_FORMAT_ID fetched from MATTERGENACC table into BILLINGFORMAT table and 
				 * check the Value of REF_FIELD_6 .
				 * 2. If Value is True, then Pass MATTER_NO into table MatterTimeTicket and fetch TASK_BASED_CODE. 
				 * 3.  Pass TASK CODE into TASKCODE table and fetch TASK_CODE_TEXT	
				 */
				if (ticket.getTaskCode() != null) {
					TaskbasedCode taskbasedCode = 
							setupService.getTaskbasedCode(ticket.getTaskCode(), 
									setupAuthToken.getAccess_token());
					timeTicket.setTaskCode(taskbasedCode.getTaskCode());
					timeTicket.setTaskText(taskbasedCode.getTaskcodeDescription());
				}
			}
			log.info("timeTicket -------------------> : " + timeTicket);
			timeTickets.add(timeTicket);
		}
		
		// Setting up TotalHours and Amount
		TimeTicketDetail timeTicketDetail = new TimeTicketDetail();
		timeTicketDetail.setTimeTickets(timeTickets);
		timeTicketDetail.setSumOfTotalHours(totalHours);
		timeTicketDetail.setSumOfTotalAmount(totalAmount);
		timeTicketDetail.setSumOfTotalBillableAmount(totalBillableAmount);
		timeTicketDetail.setSumOfTotalBillableHours(totalBillableHours);
		return timeTicketDetail;
	}

	/**
	 * buildInvoiceTimeTickets
	 * @param matterNumber
	 * @param preBillStatusId
	 * @param billingFormat
	 * @param setupAuthToken
	 * @return
	 */
	private TimeTicketDetail buildInvoiceTimeTickets(String matterNumber, Long preBillStatusId, String preBillNumber,
													 BillingFormat billingFormat, AuthToken setupAuthToken) {
		// Pass Matter_No in MatterTimeTicket table to fetch the below details
		List<com.mnrclara.api.accounting.model.prebill.MatterTimeTicket> matterTimeTickets =
				matterTimeTicketRepository.findByMatterNumberAndReferenceField1AndBillTypeNotOrderByTimeTicketDateAscTimeTicketNumberAsc(matterNumber, preBillNumber, "Non-Billable");
		List<TimeTicket> timeTickets = new ArrayList<>();
		Double totalHours = 0D;
		Double totalAmount = 0D;

		for (com.mnrclara.api.accounting.model.prebill.MatterTimeTicket ticket : matterTimeTickets) {
			TimeTicket timeTicket = new TimeTicket();

			// Ticket_No
			timeTicket.setTimeTicketNumber(ticket.getTimeTicketNumber());

			// Timekeeper_Code
			timeTicket.setTimeTicketName(ticket.getTimeKeeperCode());

			// CTD_ON
			timeTicket.setCreatedOn(ticket.getTimeTicketDate());

			// Ref_Field_4
			timeTicket.setTicketDescription(ticket.getReferenceField4());

			// App_Bill_Amount
			if (ticket.getApprovedBillableAmount() != null) {
				log.info("App bill amount=======> : " + ticket.getApprovedBillableAmount());				
				timeTicket.setBillableAmount(ticket.getApprovedBillableAmount());
				totalAmount = totalAmount + ticket.getApprovedBillableAmount();
			}

			if (ticket.getTimeTicketHours() != null) {
				timeTicket.setBillableTimeInHours(ticket.getApprovedBillableTimeInHours());
				totalHours = totalHours + ticket.getApprovedBillableTimeInHours();
			}

			// Bill_Type
			timeTicket.setBillType(ticket.getBillType());

			/*
			 * ACTIVITY_CODE/ACTIVITY_TEXT
			 * --------------------------------
			 * 1. Pass BILL_FORMAT_ID fetched from MATTERGENACC table into BILLINGFORMAT table and
			 * check the Value of REF_FIELD_6.
			 * 2. If Value is True, then Pass MATTER_NO into table MatterTimeTicket and fetch ACTIVITY_CODE.
			 * 3. Pass ACTIVITY CODE into ACTIVITYCODE table and fetch ACTIVITY_TEXT
			 */
			log.info("ticket -----getReferenceField6--------------> : " + billingFormat.getReferenceField6());
			if (billingFormat != null && billingFormat.getReferenceField6().equalsIgnoreCase("true")) {
				log.info("ticket -------------------> : " + ticket);
				if (ticket.getActivityCode() != null) {
					ActivityCode activityCode =
							setupService.getActivityCode(ticket.getActivityCode(),
									setupAuthToken.getAccess_token());
					timeTicket.setActivityCode(activityCode.getActivityCode());
					timeTicket.setActivityText(activityCode.getActivityCodeDescription());
				}

				/*
				 * TASK_CODE/TASK_TEXT
				 * ------------------------
				 * 1. Pass BILL_FORMAT_ID fetched from MATTERGENACC table into BILLINGFORMAT table and
				 * check the Value of REF_FIELD_6 .
				 * 2. If Value is True, then Pass MATTER_NO into table MatterTimeTicket and fetch TASK_BASED_CODE.
				 * 3.  Pass TASK CODE into TASKCODE table and fetch TASK_CODE_TEXT
				 */
				if (ticket.getTaskCode() != null) {
					TaskbasedCode taskbasedCode =
							setupService.getTaskbasedCode(ticket.getTaskCode(),
									setupAuthToken.getAccess_token());
					timeTicket.setTaskCode(taskbasedCode.getTaskCode());
					timeTicket.setTaskText(taskbasedCode.getTaskcodeDescription());
				}
			}
			log.info("timeTicket -------------------> : " + timeTicket);
			timeTickets.add(timeTicket);
		}

		// Setting up TotalHours and Amount
		TimeTicketDetail timeTicketDetail = new TimeTicketDetail();
		timeTicketDetail.setTimeTickets(timeTickets);
		timeTicketDetail.setSumOfTotalHours(totalHours);
		timeTicketDetail.setSumOfTotalAmount(totalAmount);
		return timeTicketDetail;
	}

	/**
	 * 1. Header - REPORT OUTPUT
	 * ----------------
	 * @param matterGenAcc 
	 * @param preBillDetails 
	 * @param preBillNumber 
	 * @param setupAuthToken 
	 * @param setupAuthToken 
	 * @return 
	 */
	private ReportHeader buildHeader (MatterGenAcc matterGenAcc, PreBillDetails preBillDetails, String preBillNumber, AuthToken mgmtAuthToken, AuthToken setupAuthToken) {
		ReportHeader reportHeader = new ReportHeader();
		com.mnrclara.api.accounting.model.dto.Class objClass = 
				setupService.getClass(matterGenAcc.getClassId(), setupAuthToken.getAccess_token());
		Status status = setupService.getStatus(matterGenAcc.getStatusId(), setupAuthToken.getAccess_token());
		BillingFormat billingFormat = setupService.getBillingFormat(Long.valueOf(matterGenAcc.getBillingFormatId()), 
				setupAuthToken.getAccess_token());
		ClientGeneral clientGeneral = managementService.getClientGeneral(matterGenAcc.getClientId(), 
				mgmtAuthToken.getAccess_token());
		
		MatterAssignment matterAssignment = managementService.getMatterAssignment(matterGenAcc.getMatterNumber(), mgmtAuthToken.getAccess_token());
				
		// Class_Id
		reportHeader.setClassId(matterGenAcc.getClassId());
		
		// Class Description
		if (objClass != null) {
			reportHeader.setClassDescription(objClass.getClassDescription());
		}
		
		// Client_ID
		reportHeader.setClientId(matterGenAcc.getClientId());
		
		if (clientGeneral != null) {
			// FIRST_LAST_NM
			reportHeader.setClientName(clientGeneral.getFirstNameLastName());
			
			// ADDRESS_LINE - RefField15, 16, 17, 18
			reportHeader.setAddressLine15(clientGeneral.getReferenceField15());
			reportHeader.setAddressLine16(matterGenAcc.getReferenceField3());
			reportHeader.setAddressLine17(matterGenAcc.getReferenceField5());
			reportHeader.setAddressLine18(matterGenAcc.getReferenceField6());
			reportHeader.setAddressLine19(matterGenAcc.getReferenceField7());
			reportHeader.setAddressLine20(matterGenAcc.getReferenceField8());
		}
		
		if (preBillDetails != null) {
			// PARTNE_RASSIGNED
			reportHeader.setPartnerAssigned(preBillDetails.getPartnerAssigned());
			
			// PREBILL_BATCHNUMBER
			reportHeader.setPreBillBatchNumber(preBillDetails.getPreBillBatchNumber());
			
			// PREBILLNUMBER
			reportHeader.setPreBillNumber(preBillDetails.getPreBillNumber());
			
			// PREBILLDATE
			reportHeader.setPreBillDate(preBillDetails.getPreBillDate());
			
			// CREATEDBY
			reportHeader.setCreatedBy(preBillDetails.getCreatedBy());
			
			// STARTDATE_FOR_PREBILL
			reportHeader.setStartDateForPreBill(preBillDetails.getStartDateForPreBill());
			
			// FEES_COST_CUTOFF_DATE
			reportHeader.setFeesCostCutoffDate(preBillDetails.getFeesCostCutoffDate());
			
			// PAYMENT_CUTOFF_DATE
			reportHeader.setPaymentCutoffDate(preBillDetails.getPaymentCutoffDate());
		}
		
		// Matter_No
		reportHeader.setMatterNumber(matterGenAcc.getMatterNumber());
		
		// Matter_Text
		reportHeader.setMatterDescription(matterGenAcc.getMatterDescription());
		
		// Case_Category
		CaseCategory caseCategory = 
				setupService.getCaseCategory(matterGenAcc.getCaseCategoryId(), setupAuthToken.getAccess_token());
		if (caseCategory != null) {
			reportHeader.setCaseCategory(caseCategory.getCaseCategory());
		}
		
		// CASESUBCATEGORY
		CaseSubcategory subCategory = setupService.getCaseSubcategory(matterGenAcc.getLanguageId(), matterGenAcc.getClassId(),
				matterGenAcc.getCaseCategoryId(), matterGenAcc.getCaseSubCategoryId(), setupAuthToken.getAccess_token());
		if (subCategory != null) {
			reportHeader.setCaseSubCategory(subCategory.getSubCategory());
		}
		
		// BILL_REMARK
		reportHeader.setBillingRemarks(matterGenAcc.getBillingRemarks());
		
		// Case_Open_Date
		reportHeader.setCaseOpenedDate(matterGenAcc.getCaseOpenedDate());
		
		// STATUS_ID
		reportHeader.setStatusId(matterGenAcc.getStatusId());
		
		// Status_Text
		if (status != null) {
			reportHeader.setStatusText(status.getStatus());
		}
		
		// Bill_Mode_Id
		BillingMode billingMode = setupService.getBillingMode(Long.valueOf(matterGenAcc.getBillingModeId()), setupAuthToken.getAccess_token());
		if (billingMode != null) {
			reportHeader.setBillingModeId(billingMode.getBillingModeDescription());
		}
		
		// Bill_Freq_Id
		BillingFrequency billingFrequency = 
				setupService.getBillingFrequency(Long.valueOf(matterGenAcc.getBillingFrequencyId()), setupAuthToken.getAccess_token());
		if (billingFrequency != null) {
			reportHeader.setBillingFrequencyId(billingFrequency.getBillingFrequencyDescription());
		}
		
		// Bill_Format_Id
		reportHeader.setBillingFormatId(billingFormat.getBillingFormatDescription());
		
		// Bill_Remark
		reportHeader.setBillingRemarks(matterGenAcc.getBillingRemarks());
		
		if (matterAssignment != null) {
			// Partner_Assigned
			reportHeader.setPartnerAssigned(matterAssignment.getPartner());
			
			// ORIGINATING_TK
			reportHeader.setOriginatingTimeKeeper(matterAssignment.getOriginatingTimeKeeper());
			
			// RESPONSIBLE_TK
			reportHeader.setResponsibleTimeKeeper(matterAssignment.getResponsibleTimeKeeper());
			
			// ASSIGNED_TK
			reportHeader.setAssignedTimeKeeper(matterAssignment.getAssignedTimeKeeper());
		}
		
		if (billingFormat != null) {
			// REF_FIELD_7 [Comments] - Pass Bill_format_ID into table BillFormat and fetch the field
			reportHeader.setComments(billingFormat.getReferenceField7());
			
			// REF_FIELD_8 [Message] - Pass Bill_format_ID into table BillFormat and fetch the field
			reportHeader.setMessage(billingFormat.getReferenceField8());
		}
		
		/*
		 * FLAT_FEE
		 * -----------
		 * If BILL_MODE_ID of the MATTER_NO is 2 in MATTERGENACC table, then fetch FLAT_FEE value.
		 * From table MatterGenAcc
		 */
		if (matterGenAcc.getBillingModeId().equalsIgnoreCase("2")) {
			reportHeader.setFlatFeeAmount(matterGenAcc.getFlatFeeAmount());
		}
		
		return reportHeader;
	}

	/**
	 * 
	 * @param invoiceHeader
	 * @param matterGenAcc 
	 * @param mgmtAuthToken
	 * @param setupAuthToken
	 * @return
	 */
	private ReportHeader buildInvoiceHeader (InvoiceHeader invoiceHeader, MatterGenAcc matterGenAcc, AuthToken mgmtAuthToken, AuthToken setupAuthToken) {
		ReportHeader reportHeader = new ReportHeader();
		
		// Class
		com.mnrclara.api.accounting.model.dto.Class objClass = 
				setupService.getClass(invoiceHeader.getClassId(), setupAuthToken.getAccess_token());
		
		// Status
		Status status = setupService.getStatus(invoiceHeader.getStatusId(), setupAuthToken.getAccess_token());
		
		// BillingFormat
		BillingFormat billingFormat = setupService.getBillingFormat(Long.valueOf(matterGenAcc.getBillingFormatId()), 
				setupAuthToken.getAccess_token());
		// ClientGeneral
		ClientGeneral clientGeneral = managementService.getClientGeneral(invoiceHeader.getClientId(), 
				mgmtAuthToken.getAccess_token());
		
		MatterAssignment matterAssignment = managementService.getMatterAssignment(invoiceHeader.getMatterNumber(), mgmtAuthToken.getAccess_token());
				
		// Class_Id
		reportHeader.setClassId(invoiceHeader.getClassId());
		
		// Class Description
		if (objClass != null) {
			reportHeader.setClassDescription(objClass.getClassDescription());
		}
		
		// Client_ID
		reportHeader.setClientId(invoiceHeader.getClientId());
		
		if (clientGeneral != null) {
			// FIRST_LAST_NM
			reportHeader.setClientName(clientGeneral.getFirstNameLastName());
			
			// ADDRESS_LINE - RefField15, 16, 17, 18
			reportHeader.setAddressLine15(clientGeneral.getReferenceField15());
			reportHeader.setAddressLine16(clientGeneral.getReferenceField16());
			reportHeader.setAddressLine17(clientGeneral.getReferenceField17());
			reportHeader.setAddressLine18(clientGeneral.getReferenceField18());
			reportHeader.setAddressLine19(clientGeneral.getReferenceField19());
			reportHeader.setAddressLine20(clientGeneral.getReferenceField20());
		}
		
		// Matter_No
		reportHeader.setMatterNumber(invoiceHeader.getMatterNumber());
		
		// Matter_Text
		reportHeader.setMatterDescription(matterGenAcc.getMatterDescription());

		//REFERANCE FIELDS FROM MATTER GEN
		reportHeader.setReferenceField3(matterGenAcc.getReferenceField3());
		reportHeader.setReferenceField4(matterGenAcc.getReferenceField4());
		reportHeader.setReferenceField5(matterGenAcc.getReferenceField5());
		reportHeader.setReferenceField6(matterGenAcc.getReferenceField6());
		reportHeader.setReferenceField7(matterGenAcc.getReferenceField7());
		reportHeader.setReferenceField8(matterGenAcc.getReferenceField8());

		reportHeader.setAdministrativeCost(matterGenAcc.getAdministrativeCost());
		
		// Case_Category
		CaseCategory caseCategory = 
				setupService.getCaseCategory(matterGenAcc.getCaseCategoryId(), setupAuthToken.getAccess_token());
		if (caseCategory != null) {
			reportHeader.setCaseCategory(caseCategory.getCaseCategory());
		}
		
		// BILL_REMARK
		reportHeader.setBillingRemarks(matterGenAcc.getBillingRemarks());
		
		// Case_Open_Date
		reportHeader.setCaseOpenedDate(matterGenAcc.getCaseOpenedDate());
				
		// STATUS_ID
		reportHeader.setStatusId(matterGenAcc.getStatusId());
		
		// Status_Text
		if (status != null) {
			reportHeader.setStatusText(status.getStatus());
		}
		
		// Bill_Mode_Id
		reportHeader.setBillingModeId(matterGenAcc.getBillingModeId());
		
		// Bill_Freq_Id
		reportHeader.setBillingFrequencyId(matterGenAcc.getBillingFrequencyId());
		
		// Bill_Format_Id
		reportHeader.setBillingFormatId(matterGenAcc.getBillingFormatId());
		
		// Bill_Remark
		reportHeader.setBillingRemarks(matterGenAcc.getBillingRemarks());
		
		if (matterAssignment != null) {
			// Partner_Assigned
			reportHeader.setPartnerAssigned(matterAssignment.getPartner());
			
			// ORIGINATING_TK
			reportHeader.setOriginatingTimeKeeper(matterAssignment.getOriginatingTimeKeeper());
			
			// RESPONSIBLE_TK
			reportHeader.setResponsibleTimeKeeper(matterAssignment.getResponsibleTimeKeeper());
			
			// ASSIGNED_TK
			reportHeader.setAssignedTimeKeeper(matterAssignment.getAssignedTimeKeeper());
		}
		
		if (billingFormat != null) {
			// REF_FIELD_7 [Comments] - Pass Bill_format_ID into table BillFormat and fetch the field
			reportHeader.setComments(billingFormat.getReferenceField7());
			
			// REF_FIELD_8 [Message] - Pass Bill_format_ID into table BillFormat and fetch the field
			reportHeader.setMessage(billingFormat.getReferenceField8());
		}
		
		/*
		 * FLAT_FEE
		 * -----------
		 * If BILL_MODE_ID of the MATTER_NO is 2 in MATTERGENACC table, then fetch FLAT_FEE value.
		 * From table MatterGenAcc
		 */
		if (matterGenAcc.getBillingModeId().equalsIgnoreCase("2")) {
			reportHeader.setFlatFeeAmount(matterGenAcc.getFlatFeeAmount());
		}
		
		// INVOICE REMARKS
		reportHeader.setInvoiceRemarks(invoiceHeader.getReferenceText());
		return reportHeader;
	}
}
