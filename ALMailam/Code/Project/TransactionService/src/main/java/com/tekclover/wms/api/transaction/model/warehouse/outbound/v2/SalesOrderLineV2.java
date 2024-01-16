package com.tekclover.wms.api.transaction.model.warehouse.outbound.v2;

import com.tekclover.wms.api.transaction.model.warehouse.outbound.SalesOrderLine;
import lombok.Data;

@Data
public class SalesOrderLineV2 extends SalesOrderLine {
	
	private String manufacturerCode;
	private String ManufacturerName;
	private String Brand;
}
