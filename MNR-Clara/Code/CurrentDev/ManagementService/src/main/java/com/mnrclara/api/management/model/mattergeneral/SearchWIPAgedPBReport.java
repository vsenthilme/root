package com.mnrclara.api.management.model.mattergeneral;

import lombok.Data;

@Data
public class SearchWIPAgedPBReport {

	/*
	 * CLASS_ID
	 * CASE_CATEGORY_ID
	 * CASE_SUB_CATEGORY_ID
	 * CLIENT_ID
	 * MATTER_NO
	 * Status_ID
	 */
	 private Long classId;
	 private Long caseCategoryId;
	 private Long caseSubCategoryId;
	 private String clientId;
	 private String matterNumber;
	 private Long statusId;
}
