package com.mnrclara.wrapper.core.model.report;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ImmigrationPaymentPlanReportInput {

	private String fromRemainderDate;
	private String toRemainderDate;

	private String fromDueDate;
	private String toDueDate;

	private List<String> matterNumber;
	private List<String> clientNumber;

}
