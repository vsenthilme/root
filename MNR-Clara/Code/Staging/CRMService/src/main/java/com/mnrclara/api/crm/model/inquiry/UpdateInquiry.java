package com.mnrclara.api.crm.model.inquiry;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class UpdateInquiry {

	@Email (message = "Please enter correct Email ID.")
	private String email;
	
	@Email (message = "Please enter correct Email ID.")
	private String alternateEmail;
	
	private Long classId;
	private String contactNumber;
	private Long inquiryModeId;
	private String firstName;
	private String lastName;
	private String assignedUserId;
	private Long intakeFormId;
	private Long statusId;
	private String intakeNotesNumber;
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
	private String referenceField11;
    private String createdBy;
    private String updatedBy;
}
