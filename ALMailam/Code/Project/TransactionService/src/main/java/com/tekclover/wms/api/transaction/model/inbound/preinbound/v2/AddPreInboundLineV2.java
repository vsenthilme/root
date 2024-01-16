package com.tekclover.wms.api.transaction.model.inbound.preinbound.v2;

import com.tekclover.wms.api.transaction.model.inbound.preinbound.AddPreInboundLine;

import lombok.Data;

@Data
public class AddPreInboundLineV2 extends AddPreInboundLine {

	private String manufacturerCode;
	private String manufacturerName;
	private String origin;
	private String companyDescription;
	private String plantDescription;
	private String warehouseDescription;
}
