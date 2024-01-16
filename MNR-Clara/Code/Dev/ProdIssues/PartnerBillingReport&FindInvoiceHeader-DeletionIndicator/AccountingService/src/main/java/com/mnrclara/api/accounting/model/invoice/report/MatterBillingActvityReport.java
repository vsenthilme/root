package com.mnrclara.api.accounting.model.invoice.report;

import java.util.List;

import lombok.Data;

@Data
public class MatterBillingActvityReport {
	private List<InvoiceActivity> invoice;
	private List<PaymentActivity> payment;
}
