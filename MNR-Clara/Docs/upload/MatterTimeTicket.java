package com.mnrclara.wrapper.core.model.management;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MatterTimeTicket {
	
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
    private Date createdOn = new Date();
    private String UpdatedBy;
	private Date updatedOn = new Date();
}
