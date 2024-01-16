package com.mnrclara.api.management.model.mattergeneral;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class WIPAgedPBReportInput {

	private List<Long> classId;
	private List<Long> caseCategoryId;
	private List<Long> caseSubCategoryId;
	private List<String> partner;
	private List<String> clientId;
	private List<String> matterNumber;
	private Long statusId;	
	
	// Date Filter
//	private Date fromDate;
//	private Date toDate;
	
	private String fromDate;
	private String toDate;
}
