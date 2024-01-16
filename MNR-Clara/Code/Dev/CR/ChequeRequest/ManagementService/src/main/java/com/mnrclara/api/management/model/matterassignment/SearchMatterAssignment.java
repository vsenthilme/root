package com.mnrclara.api.management.model.matterassignment;

import java.util.List;

import lombok.Data;

@Data
public class SearchMatterAssignment {
	/*
	 * MATTER_NO
	 * MATTER_TEXT
	 * CLIENT_ID
	 * CASE_CATEGORY
	 * CASE_SUB_CATEGORY
	 * PARTNER
	 * ORIGINATING_TK
	 * RESPONSIBLE_TK
	 * ASSIGNED_TK
	 * LEGAL_ASSIST
	 * STATUS_ID/STATUS_TEXT
	 */
	private List<String> matterNumber;
	private List<String> matterDescription;
	private List<String> clientId;
	private List<Long> caseCategoryId;
	private List<Long> caseSubCategoryId;
	private List<String> partner;
	private List<String> originatingTimeKeeper;
	private List<String> responsibleTimeKeeper;
	private List<String> assignedTimeKeeper;
	private List<String> legalAssistant;
	private List<Long> statusId;
}
