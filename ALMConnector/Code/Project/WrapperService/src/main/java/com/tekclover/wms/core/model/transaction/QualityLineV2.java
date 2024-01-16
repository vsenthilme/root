package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class QualityLineV2 extends QualityLine {

	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}