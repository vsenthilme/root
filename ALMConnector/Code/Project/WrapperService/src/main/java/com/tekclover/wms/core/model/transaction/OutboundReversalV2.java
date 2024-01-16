package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class OutboundReversalV2 extends OutboundReversal {

	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}