package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class InboundHeaderV2 extends InboundHeader {

	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}
