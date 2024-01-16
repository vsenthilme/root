package com.mnrclara.api.accounting.model.invoice;

import java.io.Serializable;

import lombok.Data;

@Data
public class InvoiceHeaderCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * "LANG_ID" , "CLASS_ID" , "MATTER_NO" , "CLIENT_ID" , "INVOICE_NO" , "INVOICE_FISCAL_YEAR" , "INVOICE_PERIOD"
	 */
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private String invoiceNumber;
	private String invoiceFiscalYear;
	private String invoicePeriod;
}
