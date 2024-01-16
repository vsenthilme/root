package com.mnrclara.wrapper.core.model.accounting.report;

import lombok.Data;

import java.util.Date;

@Data
public class BilledHoursReport {
	String matterNumber;
	String matterText;
	String attorney;
	String invoiceNumber;
	Date   dateOfBill;
	Double amountBilled;
	Double approxHoursPaid;
	Double amountReceived;
}
