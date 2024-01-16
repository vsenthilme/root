package com.mnrclara.api.management.model.matterdocument;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateMatterDocument {

    private String languageId;
	
	private Long classId;
	
	private String matterNumber;
	
	private String clientId;
	
	private String documentUrl;
	
	private Date docuemntDate;
	
	private String documentUrlVersion;

	private Long sequenceNo;
	
	private String clientUserId;
	
	private Long statusId;
	
	private String sentBy;

	private Date sentDate;
	
	private Date receivedDate;
	
	private Date resentDate;
	
	private Date approvedDate;
	
	private Long caseCategoryId;
	
	private Long caseSubCategoryId;	
	
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
	
	private String updatedBy;
}
