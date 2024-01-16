	package com.tekclover.wms.api.transaction.model.report;

import lombok.Data;

@Data
public class StockReport {

	private String languageId;
	private String companyCodeId;
	private String plantId;
	private String warehouseId;			// WH_ID
	private String itemCode;			// ITM_CODE
	private String manufacturerSKU; 	// MFR_SKU
	private String itemText;			// ITEM_TEXT
	private Double onHandQty;			// INV_QTY
	private Double damageQty;			// Damage Qty
	private Double holdQty;				// Hold Qty 
	private Double availableQty;		// SUM (5+6+7)

}
