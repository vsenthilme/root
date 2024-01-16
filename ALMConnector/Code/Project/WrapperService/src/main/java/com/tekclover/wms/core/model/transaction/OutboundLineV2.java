package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class OutboundLineV2 extends OutboundLine {

	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}