package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;

import lombok.Data;

@Data
public class InvoiceActivity {
	/*
	 * POSTING_DATE
	 * ITEM_NO
	 * EXP_AMOUNT
	 * EXP_AMOUNT
	 * BILL_AMOUNT
	 * INVOICE_TEXT
	 * INVOICE_NO
	 */
	
	private String transactionID;
	private Date postingDate;
	private String code;
	private Double hardCost;
	private Double softCost;
	private Double debit;
	private String description;

}
