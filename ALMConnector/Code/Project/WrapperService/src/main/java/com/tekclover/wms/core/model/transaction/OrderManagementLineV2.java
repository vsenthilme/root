package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class OrderManagementLineV2 extends OrderManagementLine {

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
