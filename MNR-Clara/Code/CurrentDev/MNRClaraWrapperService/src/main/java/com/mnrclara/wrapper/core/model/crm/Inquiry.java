package com.mnrclara.wrapper.core.model.crm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(Include.NON_NULL)
public class Inquiry {

	private String inquiryNumber;
	private Long classId;
	private Long transactionId;
	private String languageId;
	private String email;
	private String alternateEmail;
	private String contactNumber;
	private Long inquiryModeId;
	private Date inquiryDate = new Date();
	private String sInquiryDate;
	private String firstName;
	private String lastName;
	private Long statusId;
	private String inquiryNotesNumber;
	private String assignedUserId;
	private Long intakeFormId;
	private String intakeNotesNumber;
	private String howDidYouHearAboutUs;
	private String others;
	private Long deletionIndicator;
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
    private Date createdOn;
	private String sCreatedOn;
    private String updatedBy;
    private Date updatedOn;
}
