package com.mnrclara.api.accounting.model.paymentplan;

import java.util.Date;

import lombok.Data;

@Data
public class UpdatePaymentPlanLine {

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
