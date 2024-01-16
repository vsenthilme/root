package com.mnrclara.api.management.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TimekeeperCode { 
	
	private String timekeeperCode;
	private String languageId;
	private Long classId;
	private Long userTypeId;
	private Double defaultRate;
	private String timekeeperName;
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
	private String createdBy;
    private Date createdOn = new Date();
    private String updatedBy;
	private Date updatedOn = new Date();
}
