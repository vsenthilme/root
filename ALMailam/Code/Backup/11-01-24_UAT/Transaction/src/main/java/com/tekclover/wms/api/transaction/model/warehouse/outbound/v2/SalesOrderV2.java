package com.tekclover.wms.api.transaction.model.warehouse.outbound.v2;

import com.tekclover.wms.api.transaction.model.warehouse.outbound.SalesOrderHeader;
import com.tekclover.wms.api.transaction.model.warehouse.outbound.SalesOrderLine;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class SalesOrderV2 {
	
	@Valid
	private SalesOrderHeaderV2 salesOrderHeader;
	
	@Valid
	private List<SalesOrderLineV2> salesOrderLine;
}
