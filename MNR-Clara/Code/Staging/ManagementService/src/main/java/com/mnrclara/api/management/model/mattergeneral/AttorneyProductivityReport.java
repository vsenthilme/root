package com.mnrclara.api.management.model.mattergeneral;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AttorneyProductivityReport {

	private String clientId;
	private String clientName;
	private String matterNumber;
	private String matterDescription;
	private String caseCategoryId;
	private String caseSubCategoryId;
	private String originatingTimeKeeper;
	private String assignedTimeKeeper;
	private String responsibleTimeKeeper;
	private String flatFee;
	private String legalAssistant;
	private String paraLegal; // REF_FIELD_2 
	private Double invoiceAmount;
	private String statusDescription;
	
	private List<FeeShareAttorneyReport> feeShareAttorneyReports;
	private Date caseOpenDate;
	private Date caseFiledDate;

	private String timeKeeperCode1;
	private String feeSharingPercentage1;
	private Double feeSharingAmount1;

	private String timeKeeperCode2;
	private String feeSharingPercentage2;
	private Double feeSharingAmount2;

	private String timeKeeperCode3;
	private String feeSharingPercentage3;
	private Double feeSharingAmount3;
}
