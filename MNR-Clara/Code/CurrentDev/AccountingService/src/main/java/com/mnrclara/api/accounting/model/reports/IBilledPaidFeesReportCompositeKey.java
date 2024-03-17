package com.mnrclara.api.accounting.model.reports;

import lombok.Data;

import java.io.Serializable;

@Data
public class IBilledPaidFeesReportCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 *  `TK_CODE`, `CASE_CATEGORY_ID`, `CASE_SUB_CATEGORY_ID`
	 */
	private Long caseCategoryId;
	private Long caseSubCategoryId;
}
