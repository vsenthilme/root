package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchInboundLine {
	/*
	* WH_ID
	* REF_DOC_NO
	* IB_CNF_ON
	*/
	private List<String> warehouseId;
	private List<String> companyCode;
	private List<String> plantId;
	private List<String> languageId;
	private String referenceField1;	
	private Date startConfirmedOn;
	private Date endConfirmedOn;

	private List<Long> statusId;
	private List<String> itemCode;
	private List<String> manufacturerPartNo;
	private List<String> refDocNumber;
}
