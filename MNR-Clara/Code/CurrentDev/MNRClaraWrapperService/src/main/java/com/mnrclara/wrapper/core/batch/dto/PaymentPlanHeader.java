package com.mnrclara.wrapper.core.batch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PaymentPlanHeader { 
	
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
	
	/**
	* 
	* @param paymentPlanNumber
	* @param paymentPlanRevisionNo
	* @param languageId
	* @param classId
	* @param matterNumber
	* @param clientId
	* @param paymentPlanDate
	* @param quotationNo
	* @param paymentPlanStartDate
	* @param endDate
	* @param noOfInstallment
	* @param paymentPlanTotalAmount
	* @param dueAmount
	* @param installmentAmount
	* @param currency
	* @param paymentCalculationDayDate
	* @param paymentPlanText
	* @param sentOn
	* @param approvedOn
	* @param statusId
	* @param deletionIndicator
	* @param referenceField1
	* @param referenceField2
	* @param referenceField3
	* @param referenceField4
	* @param referenceField5
	* @param referenceField6
	* @param referenceField7
	* @param referenceField8
	* @param referenceField9
	* @param referenceField10
	* @param createdBy
	* @param createdOn
	* @param updatedBy
	* @param updatedOn
	*/
	
	public PaymentPlanHeader (String paymentPlanNumber, Long paymentPlanRevisionNo, String languageId, Long classId, String matterNumber,
				String clientId, Date paymentPlanDate, String quotationNo, Date paymentPlanStartDate, Date endDate, Long noOfInstallment,
				Double paymentPlanTotalAmount, Double dueAmount, Double installmentAmount, String currency, 
				Long paymentCalculationDayDate, String paymentPlanText, Date sentOn, Date approvedOn, Long statusId, 
				Long deletionIndicator, String referenceField1, String referenceField2, String referenceField3, String referenceField4,
				String referenceField5, String referenceField6, String referenceField7, String referenceField8, String referenceField9, 
				String referenceField10, String createdBy) {
		this.paymentPlanNumber = paymentPlanNumber;
		this.paymentPlanRevisionNo = paymentPlanRevisionNo;
		this.languageId = languageId;
		this.classId = classId;
		this.matterNumber = matterNumber;
		this.clientId = clientId;
		this.paymentPlanDate = paymentPlanDate;
		this.quotationNo = quotationNo;
		this.paymentPlanStartDate = paymentPlanStartDate;
		this.endDate = endDate;
		this.noOfInstallment = noOfInstallment;
		this.paymentPlanTotalAmount = paymentPlanTotalAmount;
		this.dueAmount = dueAmount;
		this.installmentAmount = installmentAmount;
		this.currency = currency;
		this.paymentCalculationDayDate = paymentCalculationDayDate;
		this.paymentPlanText = paymentPlanText;
		this.sentOn = sentOn;
		this.approvedOn = approvedOn;
		this.statusId = statusId;
		this.deletionIndicator = deletionIndicator;
		this.referenceField1 = referenceField1;
		this.referenceField2 = referenceField2;
		this.referenceField3 = referenceField3;
		this.referenceField4 = referenceField4;
		this.referenceField5 = referenceField5;
		this.referenceField6 = referenceField6;
		this.referenceField7 = referenceField7;
		this.referenceField8 = referenceField8;
		this.referenceField9 = referenceField9;
		this.referenceField10 = referenceField10;
		this.createdBy = createdBy;
	}
}
