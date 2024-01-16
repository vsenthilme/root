package com.mnrclara.wrapper.core.model.accounting;

import java.util.List;

import lombok.Data;

@Data
public class InvoiceCreateResponse {

	private List<AddInvoiceHeader> createdInvoiceHeaders;
	private List<AddInvoiceHeader> duplicateInvoiceHeaders;
	private List<InvoiceHeader> invoiceHeaders;
	private List<AddInvoiceHeader> erroredOutPrebillNumbers;
}
