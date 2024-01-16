package com.mnrclara.wrapper.core.model.accounting;

import java.util.List;

import lombok.Data;

@Data
public class TransferBilling {
	private List<AddInvoiceHeader> invoiceHeaders;
	private List<PaymentUpdate> paymentUpdates;
}
