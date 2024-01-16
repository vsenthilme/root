package com.mnrclara.wrapper.core.model.report;

import java.util.List;

import lombok.Data;

@Data
public class BillingReportInput {

	private List<Long> classId;
	private List<Long> caseCategoryId;
	private List<Long> caseSubCategoryId;
	
	private String fromBillingDate;
	private String toBillingDate;
	private String fromPostingDate;
	private String toPostingDate;
	
	private List<String> clientId;
	private List<String> matterNumber;
	private String timeKeepers;
	private List<Long> statusId;

	private List<String> assignedTimeKeeper;
	private List<String> responsibleTimeKeeper;
	private List<String> partner;
}
