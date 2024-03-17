package com.mnrclara.api.setup.model.chartofaccounts;

import lombok.Data;

@Data
public class UpdateChartOfAccounts {

	private String languageId;
	
	private Long classId;
	
	private String accountDescription;
	
	private String accountType;
	
	private String incomeTaxLine;
	
	private String accountStatus;
	
	private Long deletionIndicator;

	private String referenceField1;

	private String referenceField2;

	private String referenceField3;

	private String referenceField4;

	private String referenceField5;

	private String referenceField6;

	private String referenceField7;

	private String referenceField8;

	private String referenceField9;

	private String referenceField10;
	
	private String updatedBy;
}
