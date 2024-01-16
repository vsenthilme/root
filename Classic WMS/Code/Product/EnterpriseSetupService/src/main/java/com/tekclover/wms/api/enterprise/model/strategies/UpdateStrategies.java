package com.tekclover.wms.api.enterprise.model.strategies;

import lombok.Data;

@Data
public class UpdateStrategies {

	private Long strategyTypeId;
    private String companyId;
	private String plantId;
    private String warehouseId;
	private Long sequenceIndicator;
	private String strategyNo;
	private String strategyText;
	private Long priority;
	private String languageId;
	private String description;
	private String updatedBy;
}
