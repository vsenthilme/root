package com.mnrclara.api.accounting.model.reports;

import java.util.Date;

import lombok.Data;

@Data
public class BilledHoursPaidReport {
    private String matterNumber;
    private String matterText;
    private String attorney;
    private String invoiceNumber;
    private Date   dateOfBill;
    private Double amountBilled;
    private Double hoursBilled;
    private Double approxHoursPaid;
    private Double approxAmountReceived;

    //PartnerBilledHoursPaidReport
    private String partner;
}
