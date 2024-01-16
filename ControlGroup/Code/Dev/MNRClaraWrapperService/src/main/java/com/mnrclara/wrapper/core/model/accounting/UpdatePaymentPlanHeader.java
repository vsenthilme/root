package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class UpdatePaymentPlanHeader {

	private String paymentPlanNumber;
	private Long paymentPlanRevisionNo;
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;	
	private Date paymentPlanDate;
	private String quotationNo;
	private Date paymentPlanStartDate;
	private Date endDate;
	private Long noOfInstallment;
	private Double paymentPlanTotalAmount;
	private Double dueAmount;
	private Double installmentAmount;
	private String currency;
	private Long paymentCalculationDayDate;
	private String paymentPlanText;
	private Date sentOn;
	private Date approvedOn;
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
	private Date createdOn = new Date();
	private String updatedBy;
	private Date updatedOn = new Date();
	
	private List<AddPaymentPlanLine> addPaymentPlanLines;
}
