package com.mnrclara.wrapper.core.model.management;

import lombok.Data;

import javax.persistence.Transient;
import java.util.Date;

@Data
public class ExpirationDate {

    private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private String documentType;
	private Long statusId;
	
	private String approvalDate;	
	private String expirationDate;
	private String eligibilityDate;	
	private String reminderDate;

	private Long reminderDays;
	private String reminderDescription;
	private Long toggleNotification;
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
	private Date createdOn;
	private String updatedBy;
	private Date updatedOn;

	@Transient
	private String corporationName;

	@Transient
	private String className;

	@Transient
	private String clientName;

	@Transient
	private String clientEmail;

	@Transient
	private String matterDescription;
}
