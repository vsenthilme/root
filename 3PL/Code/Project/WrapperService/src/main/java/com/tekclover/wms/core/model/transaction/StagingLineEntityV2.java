package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class StagingLineEntityV2 extends StagingLineEntity {

	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
	private Double inventoryQuantity;
	private String manufacturerCode;
	private String manufacturerName;
	private String origin;
	private String brand;
	private String partner_item_barcode;
	private Double rec_accept_qty;
	private Double rec_damage_qty;
}
