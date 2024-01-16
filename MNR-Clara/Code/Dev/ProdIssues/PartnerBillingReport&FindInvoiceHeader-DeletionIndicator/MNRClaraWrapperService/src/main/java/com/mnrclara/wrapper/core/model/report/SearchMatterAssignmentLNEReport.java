package com.mnrclara.wrapper.core.model.report;

import java.util.List;

import lombok.Data;

@Data
public class SearchMatterAssignmentLNEReport {

	/*
	 * ORIGINATING_TK
	 * PARTNER
	 * RESPONSIBLE_TK
	 * ASSIGNED_TK
	 * LEGAL_ASSIST
	 * REF_FIELD_1
	 */
	private List<String> matterNumber;
	private List<String> originatingTimeKeeper;	// Case Sold by 
	private List<String> partner;
	private List<String> responsibleTimeKeeper;	// Main Attorney
	private List<String> assignedTimeKeeper;
	private List<String> legalAssistant;
	private List<String> lawClerks;				// Law clerks - REF_FIELD_1
}
