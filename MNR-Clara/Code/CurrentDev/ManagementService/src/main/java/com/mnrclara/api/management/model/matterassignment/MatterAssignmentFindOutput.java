package com.mnrclara.api.management.model.matterassignment;

import lombok.Data;

import java.util.Date;

@Data
public class MatterAssignmentFindOutput {

	private String caseInformationNo;
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String matterDescription;
	private String clientId;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private Date caseOpenedDate;
	private String partner;
	private String originatingTimeKeeper;
	private String responsibleTimeKeeper;
	private String assignedTimeKeeper;
	private String legalAssistant;
	private Long statusId;
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
	private Date createdOn;
	private String UpdatedBy;
	private Date updatedOn;

	private String clientName;
}
