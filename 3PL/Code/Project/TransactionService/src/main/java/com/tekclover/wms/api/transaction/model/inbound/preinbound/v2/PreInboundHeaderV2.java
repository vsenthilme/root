package com.tekclover.wms.api.transaction.model.inbound.preinbound.v2;

import com.tekclover.wms.api.transaction.model.inbound.preinbound.PreInboundHeader;
import lombok.Data;

import java.util.List;

@Data
public class PreInboundHeaderV2 extends PreInboundHeader { 
	
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
	
	private List<PreInboundLineEntityV2> preInboundLineV2;
}
