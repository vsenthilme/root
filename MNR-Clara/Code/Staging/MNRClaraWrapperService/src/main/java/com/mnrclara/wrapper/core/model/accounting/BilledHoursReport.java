package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

import java.util.Date;

@Data
public class BilledHoursReport {
	String matterNumber;
	String matterText;
	String attorney;
	String invoiceNumber;
	Date   dateOfBill;
	Double hoursBilled;
	Double amountBilled;
	Double approxHoursPaid;
	Double approxAmountReceived;

	//PartnerBilledHoursPaidReport
	private String partner;
}
