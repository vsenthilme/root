package com.mnrclara.wrapper.core.model.report;

import lombok.Data;

import java.util.List;

@Data
public class MatterPLReportInput {

	private List<Long> classId;
	private List<Long> caseCategoryId;
	private List<Long> caseSubCategoryId;
	private String fromInvoiceDate;
	private String toInvoiceDate;

	private List<String> partner;
	private List<String> matterNumber;
	private List<String> clientNumber;
}
