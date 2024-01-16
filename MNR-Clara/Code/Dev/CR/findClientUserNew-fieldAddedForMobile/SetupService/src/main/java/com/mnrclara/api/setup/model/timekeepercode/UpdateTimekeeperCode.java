package com.mnrclara.api.setup.model.timekeepercode;

import lombok.Data;

@Data
public class UpdateTimekeeperCode {

	private String languageId;
	
	private Long classId;
    
	private Long userTypeId;
	
	private Double defaultRate;

	private String timeKeeperName;

	private String rateUnit;

	private String timekeeperStatus;
	
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
