package com.tekclover.wms.api.transaction.model.outbound.preoutbound.v2;

import com.tekclover.wms.api.transaction.model.outbound.preoutbound.OutboundIntegrationLine;
import lombok.Data;

import javax.persistence.Column;

@Data
public class OutboundIntegrationLineV2 extends OutboundIntegrationLine {

	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;

	//---------------------ALM Changes-----------------------------------------
	private String manufacturerCode;						// MFR_CODE
	private String manufacturerName;						// MFR_CODE
	private String origin;									// ORIGIN
	private String brand;									// BRAND
	private String companyCode;
	private String branchCode;

	private String salesOrderNumber;

	private String manufacturerFullName;

	private Long middlewareId;
	private Long middlewareHeaderId;
	private String middlewareTable;
	private String supplierInvoiceNo;
	private String referenceDocumentType;
}