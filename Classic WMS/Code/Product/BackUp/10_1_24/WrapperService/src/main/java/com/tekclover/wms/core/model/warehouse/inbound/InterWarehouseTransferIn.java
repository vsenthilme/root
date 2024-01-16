package com.tekclover.wms.core.model.warehouse.inbound;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class InterWarehouseTransferIn {

	@Valid
	private InterWarehouseTransferInHeader interWarehouseTransferInHeader;
	
	@Valid
	private List<InterWarehouseTransferInLine> interWarehouseTransferInLine;
}
