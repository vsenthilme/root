package com.tekclover.wms.core.model.masters;

import java.util.Date;

import lombok.Data;

@Data

public class ImStrategies {

	private Long strategyTypeId;
	private String languageId;
	private String companyCodeId;
	private String plantId;
	private String warehouseId;
	private String itemCode;
	private Long sequenceIndicator;
	private Boolean capacityCheck;
	private String preferredStorageBin;
	private Long statusId;
	private String referenceField1;
	private String referenceField2;
	private String referenceField3;
	private String referenceField4;
	private String referenceField5;
	private String referenceField6;
	private String referenceField7;
	private String referenceField8;
	private String referenceField9;
	private String referenceField10;
	private Long deletionIndicator;
	private String createdBy;
	private Date createdOn = new Date();
	private String updatedBy;
	private Date updatedOn = new Date();
}
