package com.mnrclara.wrapper.core.model.management;

import lombok.Data;

import java.util.Date;

@Data
public class MatterTimeTicketMobile {

	private String timeTicketNumber;
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private String timeKeeperCode;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private Double timeTicketHours;
	private Date timeTicketDate;

	private String sTimeTicketDate;

	private String activityCode;
	private String taskCode;
	private Double defaultRate;
	private String rateUnit;
	private Double timeTicketAmount;
	private String billType;
	private String timeTicketDescription;
	private String assignedPartner;
	private Date assignedOn;
	private Double approvedBillableTimeInHours;
	private Double approvedBillableAmount;
	private Date approvedOn;
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

	private String sCreatedOn;

	private Date createdOn;
	private String UpdatedBy;
	private Date updatedOn;
	private Double assignedRatePerHour;
	private String clientName;
	private String clientIdDesc;
	private String matterIdDesc;
	private String classIdDesc;
	private String statusDesc;

	private Double assignedRatePerHourNew;
	private Double defaultRatePerHourNew;
}