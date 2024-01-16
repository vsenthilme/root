package com.tekclover.wms.api.transaction.model.inbound.preinbound.v2;

import com.tekclover.wms.api.transaction.model.inbound.preinbound.PreInboundLine;

import lombok.Data;

@Data
public class PreInboundLineV2 extends PreInboundLine { 
	private String manufacturerCode;
	private String manufacturerName;
	private String origin;
}
