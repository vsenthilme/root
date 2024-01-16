package com.tekclover.wms.api.transaction.model.warehouse.outbound.confirmation;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;

@Data
public class InterWarehouseShipment {
	
	@Valid
	private InterWarehouseShipmentHeader toHeader;
	
	@Valid
	private List<InterWarehouseShipmentLine> toLines;
}
