package com.mnrclara.wrapper.core.model.management;

import java.util.List;

import lombok.Data;

@Data
public class SearchMatterExpense {

	/*
	 * Multisearch
	 * ------------
	 * MATTER_NO
	 * EXP_CODE/EXP_CODE_TEXT 	- Multiple search values
	 * EXP_TYPE					- Multiple search values
	 * STATUS_ID/STATUS_TEXT	- Multiple search values
	 * CTD_BY					- Multiple search values
	 * CTD_ON
	 */
	 private String matterNumber;
	 private List<String> expenseCode;
	 private List<String> expenseType;
	 private List<Long> statusId;
	 private List<String> createdBy;
	 private String startCreatedOn;
	 private String endCreatedOn;
}
