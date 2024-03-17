package com.mnrclara.api.accounting.model.invoice.report;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchInvoiceHeaderBillingReport {

	private List<Long> classId;
	private Date fromBillingDate;
	private Date toBillingDate;
	
	private Date fromPostingDate;
	private Date toPostingDate;

	private List<String> clientId;
	private List<String> matterNumber;
	private List<Long> statusId;
	private List<Long> caseCategoryId;
	private List<Long> caseSubCategoryId;
	private String timeKeepers;
}
