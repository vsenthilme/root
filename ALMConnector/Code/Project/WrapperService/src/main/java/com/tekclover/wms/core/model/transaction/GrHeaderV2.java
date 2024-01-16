package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class GrHeaderV2 extends GrHeader {

	private Double acceptedQuantity;
	private Double damagedQuantity;
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}
