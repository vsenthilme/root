package com.mnrclara.api.management.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Agreement {

private String agreementCode;
	
	private String potentialClientId;
	private String inquiryNumber;
	private String agreementURL;
	private String agreementURLVersion;
	private String languageId;
	private Long classId;
	private String clientId;
	private Long caseCategoryId;
	private Long statusId;
	private Long transactionId;
	private String emailId;
    private Date sentOn;
    private Date receivedOn;
    private Date resentOn;
    private Date approvedOn;
}
