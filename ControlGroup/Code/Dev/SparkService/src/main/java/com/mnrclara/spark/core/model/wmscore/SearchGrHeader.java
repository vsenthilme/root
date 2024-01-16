package com.mnrclara.spark.core.model.wmscore;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class SearchGrHeader {

	private List<Long> inboundOrderTypeId;
	private List<String> goodsReceiptNo;
	private List<String> preInboundNo;
	private List<String> refDocNumber;
	private List<Long> statusId;
	private List<String> warehouseId;
	private List<String> caseCode;
	private List<String> createdBy;
	private Date startCreatedOn;
	private Date endCreatedOn;

	private List<String> companyCode;
	private List<String> plantId;
	private List<String> languageId;
}