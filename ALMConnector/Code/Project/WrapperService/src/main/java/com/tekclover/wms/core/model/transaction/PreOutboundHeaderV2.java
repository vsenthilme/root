package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class PreOutboundHeaderV2 extends PreOutboundHeader {

	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}