package com.tekclover.wms.core.model.warehouse.outbound.almailem;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReturnPOLineV2 {
	
	@NotNull(message = "Line Reference is mandatory")
	private Long lineReference;								// IB_LINE_NO
	
	@NotBlank(message = "SKU is mandatory")
	private String sku; 									// ITM_CODE

	private String skuDescription; 							// ITEM_TEXT

	@NotNull(message = "Return Quantity is mandatory")
	private Double returnQty;								// ORD_QTY
	
	@NotBlank(message = "UOM is mandatory")
	private String uom;										// ORD_UOM
	
	@NotBlank(message = "Order type is mandatory")
	private String orderType;								// REF_FIELD_1

	private String manufacturerCode;
	private String origin;
	private String supplierName;
	private String brand;
	private Double packQty;
	private String fromCompanyCode;
	private Double expectedQty;
	protected String storeID;

	private String sourceBranchCode;
	private String countryOfOrigin;
}
