package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class PreOutboundLineV2 extends PreOutboundLine {

	private String manufacturerCode;
	private String manufacturerName;
	private String origin;
	private String brand;
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
}