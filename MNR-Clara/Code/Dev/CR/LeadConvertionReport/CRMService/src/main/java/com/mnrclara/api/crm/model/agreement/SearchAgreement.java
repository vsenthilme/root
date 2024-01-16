package com.mnrclara.api.crm.model.agreement;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchAgreement {

	/*
	 * AGREEMENT_CODE/AGREEMENT_TEXT
	 * POT_CLIENT_ID
	 * INQ_NO
	 * CASE_CATEGORY_ID
	 * STATUS_ID/STATUS_TEXT
	 * SENT_ON
	 * RECEIVED_ON
	 * RESENT_ON
	 * APPROVED_ON
	 * ------------------
	 * multisearch
	 * ===========
	 * Agreement
	 * Prospective Client
	 * Inquiry
	 * Case Category
	 * Status
	 */
	private List<String> agreementCode;
	private List<String> potentialClientId;
	private List<String> inquiryNumber;
	private List<Long> caseCategoryId;
	private List<Long> statusId;
	
    private Date sSentOn;
    private Date eSentOn;
    
    private Date sReceivedOn;
    private Date eReceivedOn;
    
    private Date sResentOn;
    private Date eResentOn;
    
    private Date sApprovedOn;
    private Date eApprovedOn;
}
