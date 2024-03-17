package com.mnrclara.wrapper.core.model.accounting;

import java.util.List;

import lombok.Data;

@Data
public class PreBillOutputForm {

	private ReportHeader reportHeader;						// 1. Header 
	private TimeTicketDetail timeTicketDetail;				// 2. Time Tickets
	private List<ExpenseEntry> expenseEntry;				// 3. Expense Entries
	private List<TimeKeeperSummary> timeKeeperSummary;		// 4. TimeKeeper Summary 
	private List<PaymentDetails> paymentDetail;				// 5. Payment Details 
	private AccountAgingDetails accountAgingDetail;			// 6. Account Aging Details
	private FinalSummary finalSummary;						// 7. Final Summary
}
