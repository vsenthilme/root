package com.tekclover.wms.core.model.warehouse.outbound;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class InterWarehouseTransferOut {
	
	@Valid
	private InterWarehouseTransferOutHeader interWarehouseTransferOutHeader;
	
	@Valid
	private List<InterWarehouseTransferOutLine> interWarehouseTransferOutLine;
}
