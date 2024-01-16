package com.mnrclara.wrapper.core.model.crm;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
//@JsonInclude(Include.NON_NULL)
public class PCIntakeForm {
	private String intakeFormNumber;
	private Long classId;
	private String languageId;
	private String inquiryNumber;
	private Long transactionId;
	private Long intakeFormId;	
	private Long statusId;
	private String email;
	private String alternateEmailId;
	private String intakeNotesNumber;
    private Date sentOn;
    private Date receivedOn;
    private Date resentOn;
    private String sentOnString;
    private String receivedOnString;
	private String approvedBy;
    private Date approvedOn;
    private String approvedOnString;
	private Long deletionIndicator;
	
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
    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;
}
