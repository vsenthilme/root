package com.mnrclara.wrapper.core.batch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PaymentPlanLine { 
	
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private String paymentPlanNumber;
	private Long paymentPlanRevisionNo;
	private Long itemNumber;
	private String quotationNo;
	private Date installmentduedate;
	private Date dueDate;
	private Double dueAmount;
	private Double remainingDueNow;
	private String currency;
	private Long paymentReminderDays;
	private Date reminderDate;
	private String reminderStatus;
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
	
	/**
	* 
	* @param languageId
	* @param classId
	* @param matterNumber
	* @param clientId
	* @param paymentPlanNumber
	* @param paymentPlanRevisionNo
	* @param itemNumber
	* @param quotationNo
	* @param installmentduedate
	* @param dueDate
	* @param dueAmount
	* @param remainingDueNow
	* @param currency
	* @param paymentReminderDays
	* @param reminderDate
	* @param reminderStatus
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
	
	public PaymentPlanLine (String languageId, Long classId, String matterNumber, String clientId, String paymentPlanNumber, 
			Long paymentPlanRevisionNo, Long itemNumber, String quotationNo, Date installmentduedate, Date dueDate, Double dueAmount, 
			Double remainingDueNow, String currency, Long paymentReminderDays, Date reminderDate, String reminderStatus, Long statusId,
			Long deletionIndicator, String referenceField1, String referenceField2, String referenceField3, String referenceField4, 
			String referenceField5, String referenceField6, String referenceField7, String referenceField8, String referenceField9, 
			String referenceField10, String createdBy) {
		this.languageId = languageId;
		this.classId = classId;
		this.matterNumber = matterNumber;
		this.clientId = clientId;
		this.paymentPlanNumber = paymentPlanNumber;
		this.paymentPlanRevisionNo = paymentPlanRevisionNo;
		this.itemNumber = itemNumber;
		this.quotationNo = quotationNo;
		this.installmentduedate = installmentduedate;
		this.dueDate = dueDate;
		this.dueAmount = dueAmount;
		this.remainingDueNow = remainingDueNow;
		this.currency = currency;
		this.paymentReminderDays = paymentReminderDays;
		this.reminderDate = reminderDate;
		this.reminderStatus = reminderStatus;
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
