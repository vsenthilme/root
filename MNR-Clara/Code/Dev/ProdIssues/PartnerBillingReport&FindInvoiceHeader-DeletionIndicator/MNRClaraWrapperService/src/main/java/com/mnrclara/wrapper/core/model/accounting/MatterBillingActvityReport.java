package com.mnrclara.wrapper.core.model.accounting;

import java.util.List;

import lombok.Data;

@Data
public class MatterBillingActvityReport {
	private List<InvoiceActivity> invoice;
	private List<PaymentActivity> payment;
}
