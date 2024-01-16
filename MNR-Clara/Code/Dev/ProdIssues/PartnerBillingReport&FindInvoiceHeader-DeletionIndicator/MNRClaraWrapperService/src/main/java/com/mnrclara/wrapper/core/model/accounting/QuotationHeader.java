package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data

public class QuotationHeader { 
	
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private String quotationNo;
	private String quotationRevisionNo;
	private Date quotationDate;
	private Double quotationAmount;
	private String currency;	
	private String corporation;
	private Date dueDate;
	private String paymentPlanNumber;
	private String termDetails;
	private Date sentDate;
	private Date approvedDate;
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
	
	// QuoteLine Attributes
	private List<QuotationLine> quotationLine;
}
