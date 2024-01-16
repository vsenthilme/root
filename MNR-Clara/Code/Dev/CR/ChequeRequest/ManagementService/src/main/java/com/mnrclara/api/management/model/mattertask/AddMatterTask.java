package com.mnrclara.api.management.model.mattertask;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AddMatterTask {
	private String taskName;
	private String priority;
	private String taskAssignedTo;
	private String taskEmailId;
	private String taskTypeCode;
	private String taskDescription;
	private Date timeEstimate;
	private Date courtDate;
	private Long deadlineCalculationDays;
//	private LocalDate deadlineDate;
	private Date deadlineDate;
	private Long reminderCalculationDays;
//	private LocalDate reminderDate;
	private Date reminderDate;
	private Date taskCompletedOn;
	@NotBlank(message = "Matter Number is mandatory")
	private String matterNumber;

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
}
