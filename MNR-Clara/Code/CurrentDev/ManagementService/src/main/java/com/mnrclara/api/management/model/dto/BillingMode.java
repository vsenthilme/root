package com.mnrclara.api.management.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BillingMode { 
	
	private String billingModeId;
	private String languageId;
	private Long classId;
	private String billingModeDescription;
	private String billingModeStatus;
	private Long deletionIndicator;
	private String createdBy;
    private Date createdOn;
    private String updatedBy;
	private Date updatedOn;
}
