package com.mnrclara.api.management.model.matterfeesharing;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AddMatterFeeSharing {

    private String languageId;
	
	private Long classId;
	
	@NotBlank(message = "Matter Number is mandatory")
	private String matterNumber;
	
	@NotBlank(message = "Time Keeper Code is mandatory")
	private String timeKeeperCode;
	
	private String clientId;
	
	private Long caseCategoryId;
	
	private Long caseSubCategoryId;
	
	private Double feeSharingPercentage;
	
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
}
