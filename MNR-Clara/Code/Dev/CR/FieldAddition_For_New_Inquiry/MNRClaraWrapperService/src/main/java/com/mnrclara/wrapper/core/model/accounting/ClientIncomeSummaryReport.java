package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

import java.util.Date;

@Data
public class ClientIncomeSummaryReport {
	String clientId;
	String clientName;
	String expenseCode;
	Double totalReceipts;
	Double totalRefund;
	Double netReceipts;
}
