package com.tekclover.wms.api.transaction.model.report;

import lombok.Data;

@Data
public class SearchImBasicData1 {
	
	private String warehouseId;
	private String itemCode;
	private String fromCreatedOn;
	private String toCreatedOn;
}