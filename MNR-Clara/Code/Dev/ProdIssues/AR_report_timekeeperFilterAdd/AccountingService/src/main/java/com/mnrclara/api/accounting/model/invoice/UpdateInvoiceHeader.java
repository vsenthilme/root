package com.mnrclara.api.accounting.model.invoice;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateInvoiceHeader {

	private String languageId;
	private Long classId;
	private String matterNumber;
	private String preBillBatchNumber;
	private String preBillNumber;
	private Date preBillDate;	
	private String clientId;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private String invoiceFiscalYear;
	private String invoicePeriod;
	private Date postingDate;
	private String referenceText;
	private String partnerAssigned;
	private Date invoiceDate;
	private Double totalBillableHours;
	private Double invoiceAmount;
	private String currency;
	private String arAccountNumber;
	private String paymentPlanNumber;
	private Date billStartDate;
	private Date costCutDate;
	private Date paymentCutDate;
	private Date trustCutoffDate;
	private Double totalPaidAmount;
	private Double remainingBalance;
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
