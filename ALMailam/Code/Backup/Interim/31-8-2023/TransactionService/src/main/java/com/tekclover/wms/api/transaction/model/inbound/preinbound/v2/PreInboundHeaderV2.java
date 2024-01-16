package com.tekclover.wms.api.transaction.model.inbound.preinbound.v2;

import java.util.List;

import com.tekclover.wms.api.transaction.model.inbound.preinbound.PreInboundHeader;

import lombok.Data;

@Data
public class PreInboundHeaderV2 extends PreInboundHeader { 
	
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	
	private List<PreInboundLineV2> preInboundLineV2;
}
