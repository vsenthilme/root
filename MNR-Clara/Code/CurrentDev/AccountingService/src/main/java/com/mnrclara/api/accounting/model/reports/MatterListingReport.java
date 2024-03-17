package com.mnrclara.api.accounting.model.reports;

import lombok.Data;

import java.util.Date;

@Data
public class MatterListingReport {
	Long classId;
	String matterNumber;
	String matterText;
	String clientId;
	String firstLastName;
	Date caseOpenedDate;
	Date caseClosedDate;
	String billModeText;
	String billFrequencyText;
	String caseCategory;
	String caseSubCategory;
	String responsibleTk;
	String originatingTk;
	String timeKeeperCode;
	Double assignedRate;
	
	String partner;
    String assignedTk;
    String legalAssist;
    String paraLegal; 
    String corporateName;
    String petitionerName;
}
