package com.tekclover.wms.api.transaction.model.warehouse.outbound.v2;

import com.tekclover.wms.api.transaction.model.warehouse.outbound.InterWarehouseTransferOutLine;
import lombok.Data;

@Data
public class InterWarehouseTransferOutLineV2 extends InterWarehouseTransferOutLine {
	
	private String manufacturerCode;
	private String manufacturerName;
	private String Brand;
}
