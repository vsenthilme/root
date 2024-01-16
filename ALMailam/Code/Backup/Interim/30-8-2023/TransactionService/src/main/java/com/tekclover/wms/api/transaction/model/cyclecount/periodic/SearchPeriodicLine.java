package com.tekclover.wms.api.transaction.model.cyclecount.periodic;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchPeriodicLine {
	
	private String plantId;
	private String warehouseId;
	private String cycleCountNo;
	private List<Long> lineStatusId;
	private List<String> cycleCounterId;
	private Date startCreatedOn;
	private Date endCreatedOn;
}
