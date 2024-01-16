package com.tekclover.wms.api.enterprise.model.strategies;

import lombok.Data;

@Data
public class AddStrategies {

	private String languageId;
	private String companyId;
	private String plantId;
	private String warehouseId;
	private Long strategyTypeId;
	private String description;
	private String strategyText;
	private Long sequenceIndicator;
	private String strategyNo;
	private Long priority;
	private Long deletionIndicator;
}
