package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class PreInboundLineV2 extends PreInboundLine { 
	private String manufacturerCode;
	private String manufacturerName;
	private String origin;
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}
