package com.tekclover.wms.api.transaction.model.warehouse.inbound;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class InterWarehouseTransferInLine { 
	
	@NotNull(message = "Line Reference is mandatory")
	private Long lineReference;								// IB_LINE_NO
	
	@NotBlank(message = "SKU Code is mandatory")
	private String sku; 									// ITM_CODE

	@NotBlank(message = "SKU Description is mandatory")
	private String skuDescription; 							// ITEM_TEXT

	private String supplierPartNumber;						// PARTNER_ITM_CODE
	@NotBlank(message = "Manufacturer Name is mandatory")
	private String manufacturerName;						// BRND_NM
	private String manufacturerPartNo;						// MFR_PART
	
	@NotBlank(message = "Expected Date is mandatory")
	private String expectedDate;							// EA_DATE
	
	@NotNull(message = "Expected Quantity is mandatory")
	private Double expectedQty;								// ORD_QTY
	
	@NotBlank(message = "UOM is mandatory")
	private String uom;										// ORD_UOM
	
	@NotNull(message = "Pack Quantity is mandatory")
	private Long packQty;									// ITM_CASE_QTY

	private String origin;
	private String supplierName;
	@NotBlank(message = "Manufacturer Code is mandatory")
	private String manufacturerCode;
	private String brand;
}
