package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class QualityHeaderV2 extends QualityHeader {

	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}