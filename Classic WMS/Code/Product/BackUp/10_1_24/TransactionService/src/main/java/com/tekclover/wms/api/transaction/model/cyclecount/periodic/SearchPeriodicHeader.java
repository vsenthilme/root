package com.tekclover.wms.api.transaction.model.cyclecount.periodic;

import java.util.Date;
import java.util.List;

import lombok.Data;

import javax.xml.transform.sax.SAXResult;

@Data
public class SearchPeriodicHeader {
	
	/*
	 * WH_ID
	 * CC_TYP_ID
	 * CC_NO
	 * STATUS_ID
	 * CC_CTD_BY
	 * CC_CTD_ON
	 */
	 
	private List<String> warehouseId;
	private List<String> companyCodeId;
	private List<String> plantId;
	private List<String> languageId;
	private List<Long> cycleCountTypeId;
	private List<String> cycleCountNo;
	private List<Long> headerStatusId;
	private List<Long> lineStatusId;
	private List<String> createdBy;
	
	private Date startCreatedOn;
	private Date endCreatedOn;
	
	// From Line table
	private List<String> cycleCounterId;
}
