package com.mnrclara.api.crm.model.pcitform;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class UpdatePCIntakeForm {
	
	private Long classId;
	private String inquiryNumber;
	private Long intakeFormId;
	private String languageId;
	private Long transactionId;
	private Long statusId;
	
	@Email (message = "Please enter correct Email ID.")
	private String email;
	
	@Email (message = "Please enter correct Email ID.")
	private String alternateEmailId;
	
	private String intakeNotesNumber;
    private Date sentOn;
    private Date receivedOn;
    private Date resentOn;
	private String approvedBy;
    private Date approvedOn;
    
    // Consultation Date
	private String referenceField1;
	
	// Attorney Assigned
	private String referenceField2;
	
	private String referenceField3;
	private String referenceField4;
	private String referenceField5;
	private String referenceField6;
	private String referenceField7;
	private String referenceField8;
	private String referenceField9;
	private String referenceField10;

	private String feedbackStatus;
	private Boolean smsStatus;
}
