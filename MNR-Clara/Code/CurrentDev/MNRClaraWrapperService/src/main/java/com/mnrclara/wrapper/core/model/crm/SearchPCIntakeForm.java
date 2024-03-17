package com.mnrclara.wrapper.core.model.crm;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchPCIntakeForm {
	
	/*
	 * INQ_NO
	 * IT_FORM_NO
	 * IT_FORM_ID/IT_FORM_TEXT
	 * EMAIL_ID
	 * SENT_ON
	 * RECEIVED_ON
	 * APPROVED_ON
	 * STATUS_ID/STATUS_TEXT
	 * 
	 * -------------------------------
	 * multi search 
	 * -------------
	 * Inquiry Number
	 * Intake form No
	 * Intake Form ID
	 * Status 
	 */
	private List<String> inquiryNumber;
	private List<String> intakeFormNumber;
	private List<Long> intakeFormId;
	private List<Long> statusId;
	
	private String email;
    private Date sSentOn;
    private Date eSentOn;
    
    private Date sReceivedOn;
    private Date eReceivedOn;
    
    private Date sApprovedOn;
    private Date eApprovedOn;
    
    private String startSentOn;
    private String endSentOn;
    private String startReceivedOn;
    private String endReceivedOn;
    private String startApprovedOn;
    private String endApprovedOn;
}
