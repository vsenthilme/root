package com.tekclover.wms.api.transaction.model.outbound.v2;

import com.tekclover.wms.api.transaction.model.outbound.OutboundHeader;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class OutboundHeaderV2 extends OutboundHeader {

	@Column(name = "INVOICE_NO", columnDefinition = "nvarchar(100)")
	private String invoiceNumber;

	@Column(name = "C_TEXT", columnDefinition = "nvarchar(255)")
	private String companyDescription;

	@Column(name = "PLANT_TEXT", columnDefinition = "nvarchar(255)")
	private String plantDescription;

	@Column(name = "WH_TEXT", columnDefinition = "nvarchar(255)")
	private String warehouseDescription;

	@Column(name = "STATUS_TEXT", columnDefinition = "nvarchar(150)")
	private String statusDescription;

	@Column(name = "MIDDLEWARE_ID")
	private Long middlewareId;

	@Column(name = "MIDDLEWARE_TABLE", columnDefinition = "nvarchar(50)")
	private String middlewareTable;

	@Column(name = "SALES_ORDER_NUMBER", columnDefinition = "nvarchar(150)")
	private String salesOrderNumber;

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