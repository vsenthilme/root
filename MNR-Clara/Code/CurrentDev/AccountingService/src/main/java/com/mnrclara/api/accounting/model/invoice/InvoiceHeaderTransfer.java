package com.mnrclara.api.accounting.model.invoice;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
/*
 * `LANG_ID` , `CLASS_ID` , `MATTER_NO` , `CLIENT_ID` , `INVOICE_NO` , `INVOICE_FISCAL_YEAR`, `INVOICE_PERIOD`
 */
@Table(
		name = "tblinvoiceheadertransfer", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_invoiceheader_transfer", 
						columnNames = { "LANG_ID" , "CLASS_ID" , "MATTER_NO" , "CLIENT_ID" , 
								"INVOICE_NO" , "INVOICE_FISCAL_YEAR" , "INVOICE_PERIOD"})
				}
		)
@IdClass(InvoiceHeaderCompositeKey.class)
public class InvoiceHeaderTransfer { 
	
	@Id
	@Column(name = "INVOICE_NO") 
	private String invoiceNumber;
	
	@Id
	@Column(name = "LANG_ID") 
	private String languageId;
	
	@Id
	@Column(name = "CLASS_ID") 
	private Long classId;
	
	@Id
	@Column(name = "MATTER_NO") 
	private String matterNumber;
	
	@Id
	@Column(name = "CLIENT_ID") 
	private String clientId;
	
	@Id
	@Column(name = "INVOICE_FISCAL_YEAR") 
	private String invoiceFiscalYear;
	
	@Id
	@Column(name = "INVOICE_PERIOD") 
	private String invoicePeriod;
	
	@Column(name = "POSTING_DATE") 
	private Date postingDate;
	
	@Column(name = "INVOICE_REFERENCE") 
	private String referenceText;
	
	@Column(name = "CASE_CATEGORY_ID")
	private Long caseCategoryId;
	
	@Column(name = "CASE_SUB_CATEGORY_ID") 
	private Long caseSubCategoryId;	
	
	/////////////////////////////////////
	
	@Column(name = "PRE_BILL_BATCH_NO") 
	private String preBillBatchNumber;
	
	@Id
	@Column(name = "PRE_BILL_NO")
	private String preBillNumber;
	
	@Id
	@Column(name = "PRE_BILL_DATE") 
	private Date preBillDate;
	
	@Column(name = "PARTNER_ASSIGNED") 
	private String partnerAssigned;
	
	@Column(name = "INVOICE_DATE") 
	private Date invoiceDate;
	
	@Column(name = "BILL_HOURS") 
	private Double totalBillableHours;
	
	@Column(name = "INVOICE_AMT") 
	private Double invoiceAmount;
	
	@Column(name = "CURRENCY") 
	private String currency;
	
	@Column(name = "AR_ACCOUNT_NO")
	private String arAccountNumber;
	
	@Column(name = "PAYMENT_PLAN_NO") 
	private String paymentPlanNumber;
	
	@Column(name = "START_DATE") 
	private Date billStartDate;
	
	@Column(name = "COST_CUT_DATE") 
	private Date costCutDate;
	
	@Column(name = "PAYMENT_CUT_DATE") 
	private Date paymentCutDate;
	
	@Column(name = "TRUST_CUT_DATE") 
	private Date trustCutoffDate;
	
	@Column(name = "PAID_AMOUNT") 
	private Double totalPaidAmount;
	
	@Column(name = "REMAIN_BAL") 
	private Double remainingBalance;
	
	@Column(name = "PAYMENT_LINK_DATE") 
	private Date paymentLinkDate;
	
	@Column(name = "STATUS_ID") 
	private Long statusId;
	
	@Column(name = "IS_DELETED") 
	private Long deletionIndicator = 0L;
	
	@Column(name = "REF_FIELD_1") 
	private String referenceField1;
	
	@Column(name = "REF_FIELD_2") 
	private String referenceField2;
	
	@Column(name = "REF_FIELD_3") 
	private String referenceField3;
	
	@Column(name = "REF_FIELD_4") 
	private String referenceField4;
	
	@Column(name = "REF_FIELD_5") 
	private String referenceField5;
	
	@Column(name = "REF_FIELD_6")
	private String referenceField6;
	
	@Column(name = "REF_FIELD_7") 
	private String referenceField7;
	
	@Column(name = "REF_FIELD_8") 
	private String referenceField8;
	
	@Column(name = "REF_FIELD_9") 
	private String referenceField9;
	
	@Column(name = "REF_FIELD_10") 
	private String referenceField10;
	
	@Column(name = "CTD_BY")
	private String createdBy;
	
	@Column(name = "CTD_ON")
	private java.util.Date createdOn = new java.util.Date();
	
	@Column(name = "UTD_BY")
	private String updatedBy;
	
	@Column(name = "UTD_ON")
	private java.util.Date updatedOn = new java.util.Date();
	
	@JsonIgnore
	@Column(name = "SENT_TO_QB")
	private Long sentToQB = 0L;
	
	@JsonIgnore
	@Column(name = "QB_QUERY")
	private Long qbQuery = 0L;
}
