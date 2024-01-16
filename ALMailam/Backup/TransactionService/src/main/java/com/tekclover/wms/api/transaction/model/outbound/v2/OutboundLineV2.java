package com.tekclover.wms.api.transaction.model.outbound.v2;

import com.tekclover.wms.api.transaction.model.outbound.OutboundLine;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class OutboundLineV2 extends OutboundLine {

	@Column(name = "C_TEXT", columnDefinition = "nvarchar(255)")
	private String companyDescription;

	@Column(name = "PLANT_TEXT", columnDefinition = "nvarchar(255)")
	private String plantDescription;

	@Column(name = "WH_TEXT", columnDefinition = "nvarchar(255)")
	private String warehouseDescription;

	@Column(name = "STATUS_TEXT", columnDefinition = "nvarchar(150)")
	private String statusDescription;

	@Column(name = "MFR_NAME", columnDefinition = "nvarchar(255)")
	private String manufacturerName;

	@Column(name = "MIDDLEWARE_ID")
	private Long middlewareId;

	@Column(name = "MIDDLEWARE_HEADER_ID")
	private Long middlewareHeaderId;

	@Column(name = "MIDDLEWARE_TABLE", columnDefinition = "nvarchar(50)")
	private String middlewareTable;

	@Column(name = "REF_DOC_TYPE", columnDefinition = "nvarchar(150)")
	private String referenceDocumentType;

	@Column(name = "SUPPLIER_INVOICE_NO", columnDefinition = "nvarchar(150)")
	private String supplierInvoiceNo;

	@Column(name = "SALES_ORDER_NUMBER", columnDefinition = "nvarchar(150)")
	private String salesOrderNumber;

	@Column(name = "MANUFACTURER_FULL_NAME", columnDefinition = "nvarchar(150)")
	private String manufacturerFullName;

	@Column(name = "SALES_INVOICE_NUMBER", columnDefinition = "nvarchar(150)")
	private String salesInvoiceNumber;

	@Column(name = "PICK_LIST_NUMBER", columnDefinition = "nvarchar(150)")
	private String pickListNumber;

	@Column(name = "INVOICE_DATE")
	private Date invoiceDate;

	@Column(name = "DELIVERY_TYPE", columnDefinition = "nvarchar(100)")
	private String deliveryType;

	@Column(name = "CUSTOMER_ID", columnDefinition = "nvarchar(150)")
	private String customerId;

	@Column(name = "CUSTOMER_NAME", columnDefinition = "nvarchar(150)")
	private String customerName;

	@Column(name = "ADDRESS", columnDefinition = "nvarchar(500)")
	private String address;

	@Column(name = "PHONE_NUMBER", columnDefinition = "nvarchar(100)")
	private String phoneNumber;

	@Column(name = "ALTERNATE_NO", columnDefinition = "nvarchar(100)")
	private String alternateNo;

	@Column(name = "STATUS", columnDefinition = "nvarchar(100)")
	private String status;
}