package com.mnrclara.api.accounting.model.invoice;

import lombok.Data;

@Data
public class InvoiceRet {
	private Double BalanceRemaining;
	private Long RefNumber;
	private Double Subtotal;
	private Double AppliedAmount;
	private String ShipDate;
	private Boolean IsFinanceCharge;
	private Boolean IsToBeEmailed;
	private String TimeCreated;
	private String TimeModified;
	private String TxnID;
	private Double SalesTaxTotal;
	private Boolean IsPaid;
	private Boolean IsToBePrinted;
	private String error;
}
