package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class GrLineV2 extends GrLine {

	private String billStatus;
	private Double length;
	private Double width;
	private Double height;
	private Double inventoryQuantity;
	private String barcodeId;
	private Double cbm;
	private String cbmUnit;
	private String manufacturerCode;
	private String manufacturerName;
	private String origin;
	private String brand;
	private String rejectType;
	private String rejectReason;
	private Double cbmQuantity;										//CBM per Quantity
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String interimStorageBin;
	private String statusDescription;
}
