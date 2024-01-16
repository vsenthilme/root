package com.mnrclara.api.accounting.model.invoice.report;

import java.util.List;

import lombok.Data;

@Data
public class ARAgingReportInput {

	private Long classId;
	private List<Long> caseCategoryId;
	private List<Long> caseSubCategoryId;
	private List<String> clientId;
	private List<String> matterNumber;
	private List<String> daysAging;
	private List<String> timekeepers;
	private List<Long> statusId;
}
