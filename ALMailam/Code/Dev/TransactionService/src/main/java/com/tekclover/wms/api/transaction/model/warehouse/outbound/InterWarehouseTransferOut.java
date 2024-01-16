package com.tekclover.wms.api.transaction.model.warehouse.outbound;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;

@Data
public class InterWarehouseTransferOut {
	
	@Valid
	private InterWarehouseTransferOutHeader interWarehouseTransferOutHeader;
	
	@Valid
	private List<InterWarehouseTransferOutLine> interWarehouseTransferOutLine;
}
