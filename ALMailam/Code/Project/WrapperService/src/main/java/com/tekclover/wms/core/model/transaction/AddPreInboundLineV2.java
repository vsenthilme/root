package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class AddPreInboundLineV2 extends AddPreInboundLine {

	private String manufacturerCode;
	private String manufacturerName;
	private String origin;
}
