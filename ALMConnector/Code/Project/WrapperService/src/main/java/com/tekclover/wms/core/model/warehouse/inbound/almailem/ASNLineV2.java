package com.tekclover.wms.core.model.warehouse.inbound.almailem;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ASNLineV2 {
	@NotNull(message = "Line Reference is mandatory")
	private Long lineReference;
	@NotBlank(message = "sku is mandatory")
	private String sku;
	@NotBlank(message = "sku description is mandatory")
	private String skuDescription;
	private String containerNumber;
	private String supplierCode;
	private String supplierPartNumber;
	@NotBlank(message = "Manufacturer Name is mandatory")
	private String manufacturerName;
	@NotBlank(message = "Manufacturer Code is mandatory")
	private String manufacturerCode;
	@NotBlank(message = "Expected Date is mandatory")
	private String expectedDate;
	@NotNull(message = "expectedQty is mandatory")
	private Double expectedQty;
	@NotBlank(message = "Uom is mandatory")
	private String uom;
	private Double packQty;
	private String origin;
	private String supplierName;
	private String brand;
}
