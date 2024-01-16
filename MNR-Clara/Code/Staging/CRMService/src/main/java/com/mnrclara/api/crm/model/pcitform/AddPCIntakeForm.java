package com.mnrclara.api.crm.model.pcitform;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddPCIntakeForm {

	@NotNull(message = "Class ID is mandatory")
	private Long classId;
	
	@NotBlank(message = "Inquiry Number is mandatory")
	private String inquiryNumber;
	
	@NotBlank(message = "Intake Form Number is mandatory")
	private String intakeFormNumber;
	
	@NotEmpty(message = "Intake Form ID is mandatory")
	private Long intakeFormId;
	
	@Email (message = "Please enter correct Email ID.")
	private String emailId;
	
	@Email (message = "Please enter correct Email ID.")
	private String alternateEmailId;
	
	@NotNull(message = "Status ID is mandatory")
	private Long statusId;
	
	private String intakeNotesNumber;
    private Date sentDate = new Date();
    private Date receivedDate = new Date();
	private String approvedBy;
    private Date approvedDate;
	private String referenceField1;
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
}
