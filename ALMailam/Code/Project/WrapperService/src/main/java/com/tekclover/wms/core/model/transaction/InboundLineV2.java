package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class InboundLineV2 extends InboundLine {

	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}
