package com.mnrclara.api.management.model.mattergeneral;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchMatterLNEReport {

	/*
	 * CLASS_ID
	 * CASE_CATEGORY_ID
	 * CASE_SUB_CATEGORY_ID
	 * BILL_MODE_ID
	 * CASE_OPEN_DATE
	 * CASE_CLOSED_DATE
	 * STATUS_ID
	 * REF_FIELD_12
	 */
	 private Long classId;
	 private List<Long> caseCategoryId;
	 private List<Long> caseSubCategoryId;
	 private List<String> billingModeId;
	 
	 private Date fromCaseOpenDate;
	 private Date toCaseOpenDate;
	 
	 private Date fromCaseClosedDate;
	 private Date toCaseClosedDate;
	 
	 private List<Long> statusId;
	 private List<String> refferedBy;	// REF_FIELD_12
}
