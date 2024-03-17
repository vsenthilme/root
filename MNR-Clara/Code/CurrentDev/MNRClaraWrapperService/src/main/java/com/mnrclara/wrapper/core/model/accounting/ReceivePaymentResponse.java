package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

@Data
public class ReceivePaymentResponse {
	private String TxnID;
	private String TimeCreated;
	private Long TxnNumber;
	private String CustomerRef;
	private String ARAccountRef;
	private String PaymentMethodRef;
	private String ListID;
	private String TxnDate;
	private String RefNumber;
	private Double TotalAmount;
}