package com.tekclover.wms.api.transaction.model.outbound.quality;

import java.util.List;

import lombok.Data;

@Data
public class SearchQualityLine {

	/*
	 * WH_ID PRE_OB_NO REF_DOC_NO PARTNER_CODE OB_LINE_NO QC_NO ITM_CODE
	 */
	private List<String> warehouseId;
	private List<String> preOutboundNo;
	private List<String> refDocNumber;
	private List<String> partnerCode;
	private List<Long> lineNumber;
	private List<String> qualityInspectionNo;
	private List<String> itemCode;

	private List<Long> statusId;
}
