package com.mnrclara.wrapper.core.model.report;

import java.util.Date;

import lombok.Data;

@Data
public class ARAgingReport {

	private String matterNumber;
	private Long classId;
	private String clientNumber;
	private Double totalAmountDue;
	private Double unpaidCurrent;
	private Double unpaid30To60Days;			// 30-59 Days
	private Double unpaid61To90Days;			// 60-89 Days
	private Double unpaid91DaysTo120Days;		// 91-120 Days
	private Double unpaidOver120;				// Over 120 Days
	private String billingNotes;
	private String clientName;
	private String accountingPhoneNumber;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private Date matterOpenDate;
	private String matterName;
	private Date lastPaymentDate;
	private Double feeReceived;
}
