package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class AssignHHTUserCC {

	private String cycleCounterId;
	private String cycleCounterName;
	
	// UK Fields
	private String warehouseId;
	private String cycleCountNo;
	private String storageBin;
	private String itemCode;
	private String packBarcodes;

	//Almailem Fields
	private String companyCodeId;
	private String plantId;
	private String languageId;
}