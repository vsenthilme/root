package com.mnrclara.wrapper.core.model.management;

import java.time.LocalDate;
import java.util.Date;

import lombok.Data;

@Data
public class MatterTask {

	private String taskNumber;
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private String taskTypeCode;
	private String taskName;
	private String taskDescription;
	private String priority;
	private String taskAssignedTo;
	private String taskEmailId;
	private Date timeEstimate;
	private Date courtDate;
	private Long deadlineCalculationDays;
	private Date deadlineDate;
	private Long reminderCalculationDays;
	private Date reminderDate;
	private Date taskCompletedOn;
	private String taskCompletedBy;
	private Long statusId;
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
	private Date createdOn = new Date();
	private String UpdatedBy;
	private Date updatedOn = new Date();
}
