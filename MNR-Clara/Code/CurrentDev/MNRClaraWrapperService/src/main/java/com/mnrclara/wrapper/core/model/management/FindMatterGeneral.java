package com.mnrclara.wrapper.core.model.management;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindMatterGeneral {

	/*
	 * Multisearch
	 * ------------
	 * MATTER_NO
	 * CASEINFO_NO
	 * CASE_CATEGORY
	 * STATUS_ID/STATUS_TEXT
	 */
	 private List<String> matterNumber;
	 private List<String> caseInformationNo;
	 private List<Long> caseCategoryId; 
	 private List<Long> statusId;
	 private List<Long> classId;
	 private List<String> clientId;
	 private Date fromDate;
	 private Date endDate;

	 private String	fromDateString;
	 private String endDateString;
}
