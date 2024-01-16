package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

import java.util.Date;

@Data
public class BilledHoursReport {
    private String matterNumber;
    private String matterText;
    private String attorney;
    private String invoiceNumber;
    private Date   dateOfBill;
    private Double amountBilled;
    private Double hoursBilled;
    private Double approxHoursPaid;

	//PartnerBilledHoursPaidReport
	private String partner;
}
