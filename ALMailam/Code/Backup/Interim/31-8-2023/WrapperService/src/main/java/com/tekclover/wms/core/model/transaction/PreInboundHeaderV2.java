package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.List;

@Data
public class PreInboundHeaderV2 extends PreInboundHeader { 
	
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	private String statusDescription;
	
	private List<PreInboundLineV2> preInboundLineV2;
}
