package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class GrHeaderV2 extends GrHeader {


	private String billStatus;
	private Double length;
	private Double width;
	private Double height;
	private Double cbm;
	private String cbmUnit;
	private Double acceptedQuantity;
	private Double damagedQuantity;
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}
