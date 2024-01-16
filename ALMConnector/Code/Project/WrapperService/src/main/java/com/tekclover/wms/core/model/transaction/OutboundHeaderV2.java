package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class OutboundHeaderV2 extends OutboundHeader {

	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}