package com.tekclover.wms.api.transaction.model.warehouse.inbound;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SOReturnLine {

	@NotNull(message = "Line Reference is mandatory")
	private Long lineReference;

	@NotBlank(message = "SKU Code is mandatory")
	private String sku;

	@NotBlank(message = "SKU Description is mandatory")
	private String skuDescription;

	@NotBlank(message = "Invoice Number is mandatory")
	private String invoiceNumber;

	@NotBlank(message = "StoreID is mandatory")
	private String storeID;

	private String supplierPartNumber;

	@NotBlank(message = "Manufacturer Name is mandatory")
	private String manufacturerName;

	@NotBlank(message = "Expected Date is mandatory")
	private String expectedDate;

	@NotNull(message = "Expected Quantity is mandatory")
	private Double expectedQty;

	@NotBlank(message = "UOM is mandatory")
	private String uom;

	@NotNull(message = "Pack Quantity is mandatory")
	private Double packQty;

	private String origin;

	@NotBlank(message = "Manufacturer Code is mandatory")
	private String manufacturerCode;

	private String brand;

}
