package com.mnrclara.api.accounting.model.invoice.report;

import java.util.List;

import lombok.Data;

@Data
public class SearchMatterGenAccReport {

	private List<Long> caseCategoryId;
	private List<Long> caseSubCategoryId;
	private String matterNumber;
}
