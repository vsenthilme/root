package com.mnrclara.api.management.model.mattergeneral;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchMatterLNENAssignmentReport {

	 private Long classId;
	 private List<Long> caseCategoryId;
	 private List<Long> caseSubCategoryId;
	 private List<String> billModeId;
	 
	 private Date fromCaseOpenDate;
	 private Date toCaseOpenDate;
	 
	 private Date fromCaseClosedDate;
	 private Date toCaseClosedDate;
	 
	 private List<Long> statusId;
	 private List<String> refferedBy;	// REF_FIELD_12
	 
	 private List<String> matterNumber;
	 private List<String> originatingTimeKeeper;	// Case Sold by 
	 private List<String> partner;
	 private List<String> responsibleTimeKeeper;	// Main Attorney
	 private List<String> assignedTimeKeeper;
	 private List<String> legalAssistant;
	 private List<String> lawClerks;				// Law clerks - REF_FIELD_1
}
