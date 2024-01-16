package com.mnrclara.api.accounting.model.invoice;

import java.util.List;

import lombok.Data;

@Data
public class InvoiceCreateResponse {
	private List<AddInvoiceHeader> createdInvoiceHeaders;
	private List<AddInvoiceHeader> duplicateInvoiceHeaders;
	private List<AddInvoiceHeader> erroredOutPrebillNumbers;
}
