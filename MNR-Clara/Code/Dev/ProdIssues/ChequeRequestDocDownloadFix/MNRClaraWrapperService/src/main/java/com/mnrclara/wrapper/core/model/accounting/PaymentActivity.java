package com.mnrclara.wrapper.core.model.accounting;

import java.util.Date;

import lombok.Data;

@Data
public class PaymentActivity {
	
	/*
	 * PAYMENT_DATE
	 * PAYMENT_CODE
	 * PAYMENT_AMT
	 * PAYMENT_TEXT
	 * STATUS
	 * PAYMENT_NUMBER
	 * TRANSACTION_TYPE
	 */
	private Date paymentDate;
	private String paymentCode;
	private Double credit;
	private String paymentDesc;
	private String statusId;
	private String paymentNumber;
	private String transactionType;
}
