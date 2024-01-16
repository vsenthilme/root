package com.tekclover.wms.api.transaction.model.cyclecount.perpetual;

import lombok.Data;

@Data
public class AssignHHTUserCC {

	private String cycleCounterId;
	private String cycleCounterName;

	private String companyCode;
	private String plantId;
	private String languageId;

	
	// UK Fields
	private String warehouseId;
	private String cycleCountNo;
	private String storageBin;
	private String itemCode;
	private String packBarcodes;
}
