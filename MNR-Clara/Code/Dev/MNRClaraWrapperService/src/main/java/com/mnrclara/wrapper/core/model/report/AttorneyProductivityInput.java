package com.mnrclara.wrapper.core.model.report;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AttorneyProductivityInput {

	/*
	 * Multisearch
	 * ------------
	 * MATTER_NO
	 * CASE_CATEGORY
	 */
	 private List<String> matterNumber;
	 private List<String> caseCategoryId;
	 private List<String> caseSubCategoryId;
	 private List<String> assignedTimeKeeper;
	 private List<String> originatingTimeKeeper;
	 private List<String> responsibleTimeKeeper;
	 private String fromDate;
	 private String toDate;
}
