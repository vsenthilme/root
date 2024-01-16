package com.tekclover.wms.api.transaction.model.warehouse.outbound.v2;

import com.tekclover.wms.api.transaction.model.warehouse.outbound.SalesOrderHeader;
import lombok.Data;

@Data
public class SalesOrderHeaderV2 extends SalesOrderHeader {
	
	private String companyCode;
	private String pickListNumber;
	
}
