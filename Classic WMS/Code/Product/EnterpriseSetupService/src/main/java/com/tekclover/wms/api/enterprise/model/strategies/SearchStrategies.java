package com.tekclover.wms.api.enterprise.model.strategies;

import java.util.Date;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class SearchStrategies {
	/*
	 * WH_ID
	 * STR_TYP_ID
	 * SEQ_IND
	 * ST_NO
	 * PRIORITY
	 * CTD_BY
	 * CTD_ON
	 */
    private String warehouseId;
    private Long strategyTypeId;
	private String languageId;
    private Long sequenceIndicator;
    private String strategyNo;
	private Long priority;
    private String createdBy;
    private Date startCreatedOn;
	private Date endCreatedOn;
	private String companyId;
	private String plantId;

}
