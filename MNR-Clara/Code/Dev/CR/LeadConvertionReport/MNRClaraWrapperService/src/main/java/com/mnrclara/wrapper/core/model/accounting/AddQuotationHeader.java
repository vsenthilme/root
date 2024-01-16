package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddQuotationHeader {
	
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private String quotationNo;
	private Long quotationRevisionNo;
	private String firstNameLastName;
	private String corporation;
	private Date quotationDate;
	private Double quotationAmount;
	private String currency;
	private Date dueDate;
	private String paymentPlanNumber;
	private String termDetails;
	private Date sentDate;
	private Date approvedDate;
	private Long statusId;
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
	
	// QuoteLine Attributes
	private List<AddQuotationLine> addQuotationLine;
}
