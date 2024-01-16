package com.mnrclara.api.management.model.mattertask;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblmattertaskid")
public class MatterTask {

	@Id
	@Column(name = "TASK_NO")
	private String taskNumber;

	@Column(name = "LANG_ID")
	private String languageId;

	@Column(name = "CLASS_ID")
	private Long classId;

	@Column(name = "MATTER_NO")
	private String matterNumber;

	@Column(name = "CLIENT_ID")
	private String clientId;

	@Column(name = "CASE_CATEGORY_ID")
	private Long caseCategoryId;

	@Column(name = "CASE_SUB_CATEGORY_ID")
	private Long caseSubCategoryId;

	@Column(name = "TASK_TYP_CODE")
	private String taskTypeCode;

	@Column(name = "TASK_NM")
	private String taskName;

	@Column(name = "TASK_TEXT")
	private String taskDescription;

	@Column(name = "PRIORITY")
	private String priority;

	@Column(name = "TASK_ASSIGN")
	private String taskAssignedTo;

	@Column(name = "TASK_EMAIL_ID")
	private String taskEmailId;

	@Column(name = "TIME_ESTIMATE")
	private Date timeEstimate;

	@Column(name = "COURT_DATE")
	private Date courtDate;

	@Column(name = "DEADLINE_DAYS")
	private Long deadlineCalculationDays;

	@Column(name = "DEADLINE_DATE")
	private Date deadlineDate;

	@Column(name = "REMINDER_DAYS")
	private Long reminderCalculationDays;

	@Column(name = "REMINDER_DATE")
	private Date reminderDate;

	@Column(name = "TASK_COMP_ON")
	private Date taskCompletedOn;

	@Column(name = "TASK_COMP_BY")
	private String taskCompletedBy;

	@Column(name = "STATUS_ID")
	private Long statusId;

	@Column(name = "IS_DELETED")
	private Long deletionIndicator = 0L;

	@Column(name = "REF_FIELD_1")
	private String referenceField1;

	@Column(name = "REF_FIELD_2")
	private String referenceField2;

	@Column(name = "REF_FIELD_3")
	private String referenceField3;

	@Column(name = "REF_FIELD_4")
	private String referenceField4;

	@Column(name = "REF_FIELD_5")
	private String referenceField5;

	@Column(name = "REF_FIELD_6")
	private String referenceField6;

	@Column(name = "REF_FIELD_7")
	private String referenceField7;

	@Column(name = "REF_FIELD_8")
	private String referenceField8;

	@Column(name = "REF_FIELD_9")
	private String referenceField9;

	@Column(name = "REF_FIELD_10")
	private String referenceField10;

	@Column(name = "CTD_BY")
	private String createdBy;

	@Column(name = "CTD_ON")
	private Date createdOn = new Date();

	@Column(name = "UTD_BY")
	private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
}
