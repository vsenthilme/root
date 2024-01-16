package com.tekclover.wms.api.transaction.model.warehouse.outbound.v2;

import lombok.Data;

import java.util.Date;

@Data
public class SalesInvoice {

	private String companyCode;
	private String salesInvoiceNumber;
	private String branchCode;
	private String salesOrderNumber;
	private String pickListNumber;
	private Date invoiceDate;
}
