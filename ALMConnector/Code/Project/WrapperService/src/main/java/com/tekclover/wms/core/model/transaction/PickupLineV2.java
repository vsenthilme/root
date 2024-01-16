package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class PickupLineV2 extends PickupLine {

	private Double inventoryQuantity;
	private Double pickedCbm;
	private String cbmUnit;
    private String manufacturerCode;
	private String manufacturerName;
	private String origin;
	private String brand;
	private String partnerItemBarcode;
	private String levelId;
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}