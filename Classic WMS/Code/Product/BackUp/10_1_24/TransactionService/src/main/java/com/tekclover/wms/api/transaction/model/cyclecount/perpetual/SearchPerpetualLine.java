package com.tekclover.wms.api.transaction.model.cyclecount.perpetual;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchPerpetualLine {
	
	private List<String> cycleCountNo;
	private List<Long> lineStatusId;
	private List<String> companyCodeId;
	private List<String> plantId;
	private List<String> languageId;
	private String cycleCounterId;
	private String warehouseId;
	private Date startCreatedOn;
	private Date endCreatedOn;

	private List<String> itemCode;
	private List<String> storageBin;
	private List<String> packBarcodes;
	private List<Long> stockTypeId;
	private List<String> manufacturerPartNo;
	private List<String> storageSectionId;
}
