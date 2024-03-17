package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

import java.util.Date;

@Data
public class MatterListingReport {
	String classId;
	String matterNumber;
	String statusId;
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
