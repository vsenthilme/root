package com.mnrclara.api.management.model.expirationdate;

import java.util.Date;

import lombok.Data;

@Data
public class AddExpirationDate {

    private String languageId;
	private Long classId;
	private String matterNumber;	
	private String clientId;
	private String documentType;
	private Long statusId;
	private Date approvalDate;
	private Date expirationDate;
	private Date eligibilityDate;
	private Long reminderDays;
	private Date reminderDate;
	private String reminderDescription;
	private Long deletionIndicator = 0L;
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
