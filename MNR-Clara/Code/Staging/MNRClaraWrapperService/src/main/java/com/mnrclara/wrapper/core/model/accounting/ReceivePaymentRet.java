package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

@Data
public class ReceivePaymentRet {
	private String TxnID;
	private String TimeCreated;
	private Long TxnNumber;
	private CustomerRef customerRef;
	private ARAccountRef arAccountRef;
	private PaymentMethodRef paymentMethodRef;
	private String ListID;
	private String TxnDate;
	private String RefNumber;
	private Double TotalAmount;
	private String PaymentMethod;
}

@Data class CustomerRef {
	private String ListID;
	private String FullName;
}

@Data class ARAccountRef {
	private String ListID;
	private String FullName;
}

@Data class PaymentMethodRef {
	private String ListID;
	private String FullName;
}