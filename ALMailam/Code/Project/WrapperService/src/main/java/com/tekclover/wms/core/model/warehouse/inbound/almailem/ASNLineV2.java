package com.tekclover.wms.core.model.warehouse.inbound.almailem;

import com.tekclover.wms.core.model.warehouse.inbound.ASNLine;
import lombok.Data;

@Data
public class ASNLineV2 extends ASNLine {

	private String manufacturerCode;
	private String origin;
	private String supplierName;
	private String brand;
}
