package com.mnrclara.api.accounting.model.invoice.report;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BillingReportInput {

	private List<Long> classId;
	private List<Long> caseCategoryId;
	private List<Long> caseSubCategoryId;
	private Date fromBillingDate;
	private Date toBillingDate;
	
	private Date fromPostingDate;
	private Date toPostingDate;
	private List<String> clientId;
	private List<String> matterNumber;
	private String timeKeepers;
	private List<Long> statusId;
}
