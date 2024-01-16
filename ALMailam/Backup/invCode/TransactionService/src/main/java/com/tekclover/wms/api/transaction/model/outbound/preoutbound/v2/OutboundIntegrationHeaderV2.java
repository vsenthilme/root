package com.tekclover.wms.api.transaction.model.outbound.preoutbound.v2;

import com.tekclover.wms.api.transaction.model.outbound.preoutbound.OutboundIntegrationHeader;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class OutboundIntegrationHeaderV2 extends OutboundIntegrationHeader {

	private String companyDescription;

	private String plantDescription;

	private String warehouseDescription;

	private String statusDescription;

	// For ALM Orders
	private String branchCode;
	private String companyCode;
	private String languageId;

	private Long middlewareId;
	private String middlewareTable;
	private String referenceDocumentType;

	private String salesOrderNumber;
	private String salesInvoiceNumber;
	private String pickListNumber;
	private String deliveryType;
	private String customerId;
	private String customerName;
	private String address;
	private String phoneNumber;
	private String alternateNo;
	private String status;

	private List<OutboundIntegrationLineV2> outboundIntegrationLines;
}