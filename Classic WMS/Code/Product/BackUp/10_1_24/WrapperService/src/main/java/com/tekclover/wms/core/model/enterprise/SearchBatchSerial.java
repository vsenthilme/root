package com.tekclover.wms.core.model.enterprise;

import java.util.Date;

import lombok.Data;

@Data
public class SearchBatchSerial {
	/*
	 * WH_ID
	 * STR_MTD
	 * MAINT
	 * LEVEL_ID
	 * CTD_BY
	 * CTD_ON
	 */
	private String warehouseId;
	private String storageMethod;
	private String maintenance;
	private Long levelId;
	private String languageId;
	private String createdBy;
	private Date startCreatedOn;
	private Date endCreatedOn;
	private String companyId;
	private String plantId;
}
