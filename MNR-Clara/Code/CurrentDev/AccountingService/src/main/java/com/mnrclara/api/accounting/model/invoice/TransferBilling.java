package com.mnrclara.api.accounting.model.invoice;

import java.util.List;

import lombok.Data;

@Data
public class TransferBilling {
	private List<AddInvoiceHeader> invoiceHeaders;
	private List<PaymentUpdate> paymentUpdates;
}
