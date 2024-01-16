package com.tekclover.wms.api.transaction.model.warehouse.outbound.v2;

import com.tekclover.wms.api.transaction.model.warehouse.outbound.InterWarehouseTransferOutHeader;
import lombok.Data;

@Data
public class InterWarehouseTransferOutHeaderV2 extends InterWarehouseTransferOutHeader {
	
	private String fromBranchCode;
	private String toBranchCode;
}
