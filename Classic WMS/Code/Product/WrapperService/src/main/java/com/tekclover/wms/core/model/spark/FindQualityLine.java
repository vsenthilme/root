package com.tekclover.wms.core.model.spark;

import lombok.Data;

import java.util.List;

@Data
public class FindQualityLine {

	/*
	 * WH_ID PRE_OB_NO REF_DOC_NO PARTNER_CODE OB_LINE_NO QC_NO ITM_CODE
	 */
	private List<String> warehouseId;
	private List<String> companyCodeId;
	private List<String> plantId;
	private List<String> languageId;
	private List<String> preOutboundNo;
	private List<String> refDocNumber;
	private List<String> partnerCode;
	private List<Long> lineNumber;
	private List<String> qualityInspectionNo;
	private List<String> itemCode;

	private List<Long> statusId;
}
