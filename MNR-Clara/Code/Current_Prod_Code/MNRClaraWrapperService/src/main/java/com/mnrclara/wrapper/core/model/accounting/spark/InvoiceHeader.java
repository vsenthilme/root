package com.mnrclara.wrapper.core.model.accounting.spark;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class InvoiceHeader {

	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private String invoiceNumber;
	private String invoiceFiscalYear;
	private String invoicePeriod;
	private String preBillBatchNumber;
	private String preBillNumber;
	private Timestamp postingDate;
}
