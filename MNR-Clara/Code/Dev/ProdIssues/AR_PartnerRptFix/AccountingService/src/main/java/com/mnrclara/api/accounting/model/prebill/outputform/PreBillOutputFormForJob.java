package com.mnrclara.api.accounting.model.prebill.outputform;

import java.util.List;

import lombok.Data;

@Data
public class PreBillOutputFormForJob {

	private List<ReportHeader> reportHeader;				// 1. Header 
	private List<TimeTicketDetail> timeTicketDetail;		// 2. Time Tickets
	private List<ExpenseEntry> expenseEntries;				// 3. Expense Entries
	private List<TimeKeeperSummary> timeKeeperSummary;		// 4. TimeKeeper Summary 
	private List<PaymentDetails> paymentDetails;			// 5. Payment Details 
	private List<AccountAgingDetails> accountAgingDetails;	// 6. Account Aging Details
	private List<FinalSummary> finalSummary;				// 7. Final Summary
}
