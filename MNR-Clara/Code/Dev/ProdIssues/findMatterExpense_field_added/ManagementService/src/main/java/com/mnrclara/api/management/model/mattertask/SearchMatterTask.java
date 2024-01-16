package com.mnrclara.api.management.model.mattertask;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchMatterTask {
	
	/*
	 * TASK_NO
	 * TASK_TYP_CODE/TASK_TYP_CODE_TEXT
	 * TASK_NM
	 * TASK_ASSIGN
	 * CTD_ON
	 * DEADLINE_DATE
	 * REMINDER_DATE
	 * STATUS_ID/STATUS_TEXT
	 * ----------------------
	 * multisearch
	 * ============
	 * Task No
	 * Task type
	 * Status 
	 */
	 private List<String> taskNumber; 
	 private List<String> taskTypeCode;
	 private List<Long> statusId;
	 private List<Long> classId;
	 private List<String> matterNumber;

	 private List<String> createdBy;
	 private String taskName; 
	 private String taskAssignedTo; 
	 private Date sCreatedOn;
	 private Date eCreatedOn;
	 private Date sDeadlineDate;
	 private Date eDeadlineDate;
	 private Date sReminderDate;
	 private Date eReminderDate;

}
