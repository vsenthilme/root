package com.mnrclara.spark.core.model.wmscore;

import lombok.Data;

import java.util.List;

@Data
public class FindStagingLine {
	 
	private List<String> warehouseId;
	private List<String> companyCode;
	private List<String> plantId;
	private List<String> languageId;
	private List<String> preInboundNo;
	private List<String> refDocNumber;
	private List<String> stagingNo;
	private List<String> palletCode;
	private List<String> caseCode;
	private List<Long> lineNo;
	private List<String> itemCode;
	private List<Long> statusId;
}
