package com.mnrclara.api.accounting.model.invoice.report;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblaragingreport")
public class ARAgingReport {
	@Id
	@Column(name = "ARAGING_ID")
	private Long arAgingId;
	
	@Column(name = "MATTER_NO") 
	private String matterNumber;
	
	@Column(name = "CLASS_ID") 
	private Long classId;
	
	@Column(name = "CLIENT_ID") 
	private String clientId;
	
	@Column(name = "TOTAL_AMT_DUE") 
	private Double totalAmountDue;
	
	@Column(name = "UNPAID_CURRENT") 
	private Double unpaidCurrent;
	
	@Column(name = "UNPAID_31_60_DAYS") 
	private Double unpaid30To60Days;			// 30-59 Days
	
	@Column(name = "UNPAID_61_90_DAYS") 
	private Double unpaid61To90Days;			// 60-89 Days
	
	@Column(name = "UNPAID_91_120_DAYS") 
	private Double unpaid91DaysTo120Days;		// 91-120 Days
	
	@Column(name = "UNPAID_OVER120") 
	private Double unpaidOver120;				// Over 120 Days
	
	@Column(name = "BILLING_NOTES") 
	private String billingNotes;
	
	@Column(name = "CLIENT_NAME") 
	private String clientName;
	
	@Column(name = "ACC_PHONE_NO") 
	private String accountingPhoneNumber;
	
	@Column(name = "CASE_CAT_ID") 
	private Long caseCategoryId;
	
	@Column(name = "CASE_SUB_CAT_ID") 
	private Long caseSubCategoryId;
	
	@Column(name = "MATTER_OPEN_DATE") 
	private Date matterOpenDate;
	
	@Column(name = "MATTER_NAME") 
	private String matterName;
	
	@Column(name = "LAST_PMT_DATE") 
	private Date lastPaymentDate;
	
	@Column(name = "FEE_RECEIVED") 
	private Double feeReceived;

	//Time Keeper

	@Column(name = "PARTNER")
	private String partner;

	@Column(name = "ORIGINATING_TK")
	private String originatingTimeKeeper;

	@Column(name = "RESPONSIBLE_TK")
	private String responsibleTimeKeeper;

	@Column(name = "ASSIGNED_TK")
	private String assignedTimeKeeper;

	@Column(name = "LEGAL_ASSIST")
	private String legalAssistant;

	@Column(name = "PARALEGEL")
	private String paralegal;

	@Column(name = "LAW_CLERK")
	private String lawClerk;

	@Column(name = "STATUS_ID")
	private Long statusId;
}
