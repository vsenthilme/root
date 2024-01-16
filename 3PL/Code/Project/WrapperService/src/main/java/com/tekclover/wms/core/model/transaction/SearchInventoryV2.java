package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.List;

@Data
public class SearchInventoryV2 extends SearchInventory {

	private List<String> languageId;
	private List<String> companyCodeId;
	private List<String> plantId;

	private List<String> barcodeId;
	private List<String> manufacturerCode;
	private List<String> referenceDocumentNo;
}