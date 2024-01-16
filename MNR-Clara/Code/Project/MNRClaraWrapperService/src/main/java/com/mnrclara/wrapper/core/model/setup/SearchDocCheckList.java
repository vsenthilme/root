package com.mnrclara.wrapper.core.model.setup;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchDocCheckList {
	
	/*
	 * CLASS_ID
	 * CHK_LIST_NO
	 * CASE_CATEGORY_ID
	 * CASE_SUB_CATEGORY_ID
	 * STATUS_ID
	 * CTD_BY
	 * CTD_ON
	 */
	private List<Long> classId;
	private List<Long> checkListNo;
	private List<Long> caseCategoryId;
	private List<Long> caseSubCategoryId;
	private List<Long> statusId;
	private List<String> createdBy;
	
	private Date startCreatedOn;
	private Date endCreatedOn;	
}
