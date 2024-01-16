package com.mnrclara.api.management.model.matterexpense;

import java.util.Date;

import lombok.Data;

@Data
public class AddMatterExpense {

    private String languageId;
	private Long classId;
	private String matterNumber;
	private String caseInformationNo;
	private String clientId;
	private String expenseCode;
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
	private Long deletionIndicator;
	private String referenceField1;
	private Date referenceField2;
	private String referenceField3;
	private String referenceField4;	
	private String referenceField5;
	private String referenceField6;
	private String referenceField7;
	private String referenceField8;
	private String referenceField9;
	private String referenceField10;
	private String createdBy;
	private Date createdOn;
	private String UpdatedBy;
	private Date updatedOn;
}
