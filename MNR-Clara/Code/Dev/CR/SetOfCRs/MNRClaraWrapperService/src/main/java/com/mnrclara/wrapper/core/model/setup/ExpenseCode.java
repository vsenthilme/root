package com.mnrclara.wrapper.core.model.setup;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class ExpenseCode { 
	
	private String expenseCode;
	private String languageId;
	private Long classId;
	private String costType;
	private Double costPerItem;
	private String expenseCodeDescription;
	private String rateUnit;
	private String expenseCodeStatus;
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
	private String createdBy;
	private String updatedBy;
	private Date createdOn;
	private Date updatedOn;
}
