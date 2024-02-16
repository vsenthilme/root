package com.mnrclara.api.crm.model.inquiry;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class AddInquiry {

	@Email (message = "Please enter correct Email ID.")
	@NotBlank(message = "E-Mail is mandatory")
	private String email;
	private String alternateEmail;
	
	@NotBlank(message = "Contact Number is mandatory")
	private String contactNumber;
	
	@NotNull(message = "Inquiry Mode ID is mandatory")
	private Long inquiryModeId;
	
	private String firstName;
	private String lastName;
	private String assignedUserId;
	private Long intakeFormId;
	private String intakeNotesNumber;
	private String howDidYouHearAboutUs;
	private String others;
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
	
	@JsonIgnore
	private Date createdOn = new Date();
}
