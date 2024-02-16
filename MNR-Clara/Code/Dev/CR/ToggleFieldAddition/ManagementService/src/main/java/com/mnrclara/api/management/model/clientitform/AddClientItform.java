package com.mnrclara.api.management.model.clientitform;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddClientItform {

	@NotBlank(message = "Language ID is mandatory")
	private String languageId;

	@NotNull(message = "Class ID is mandatory")
	private Long classId;

	@NotBlank(message = "Client ID is mandatory")
	private String clientId;

	@NotBlank(message = "Matter No is mandatory")
	private String matterNumber;

	@NotNull(message = "Intake Form ID is mandatory")
	private Long intakeFormId;

	@NotNull(message = "Intake Form No is mandatory")
	private String intakeFormNumber;

	private Long statusId;
	private String clientUserId;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private String notesNumber;
	private Date sentDate = new Date();
	private Date receivedDate = new Date();
	private Date resentDate = new Date();
	private Date approvedDate = new Date();
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
	private String createdBy;
}
