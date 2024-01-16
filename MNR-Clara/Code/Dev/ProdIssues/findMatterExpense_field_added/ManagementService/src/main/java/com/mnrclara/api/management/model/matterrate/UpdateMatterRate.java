package com.mnrclara.api.management.model.matterrate;

import lombok.Data;

@Data
public class UpdateMatterRate {

	private String languageId;
	
	private Long classId;

	private String clientId;
	
	private String timeKeeperCode;
	
	private Long caseCategoryId;
	
	private Long caseSubCategoryId;
	
	private Double defaultRatePerHour;
	
	private Double assignedRatePerHour;
	
	private String rateUnit;
	
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
	
	private String updatedBy;
}
