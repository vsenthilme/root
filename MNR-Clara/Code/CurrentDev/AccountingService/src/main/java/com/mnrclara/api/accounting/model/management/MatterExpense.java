package com.mnrclara.api.accounting.model.management;

import java.util.Date;

import lombok.Data;

@Data
public class MatterExpense { 
	
	private Long matterExpenseId;
	private String expenseCode;
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String caseInformationNo;
	private String clientId;	
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private Double costPerItem;
	private Double numberofItems;
	private Double expenseAmount;
	private String rateUnit;
	private String expenseDescription;
	private String expenseType;
	private String billType;
	private Boolean writeOff;
	private String expenseAccountNumber;
	private Long statusId;
	private Long deletionIndicator = 0L;
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
	private String createdBy;
    private Date createdOn = new Date();
    private String updatedBy;
	private Date updatedOn = new Date();
}
