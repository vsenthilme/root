package com.mnrclara.wrapper.core.model.accounting;

import java.util.List;

import lombok.Data;

@Data
public class CaseAssignmentDashboard {

	private List<String[]> partners;
	private List<String[]> originatingTimekeepers;
	private List<String[]> responsibleTimekeepers;
	private List<String[]> legalAssistants;
}
