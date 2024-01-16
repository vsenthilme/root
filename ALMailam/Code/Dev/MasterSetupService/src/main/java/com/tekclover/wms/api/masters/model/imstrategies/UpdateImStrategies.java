package com.tekclover.wms.api.masters.model.imstrategies;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateImStrategies {

	private String languageId;
	
	private String companyCodeId;

	private Long strategyTypeId;

	private String plantId;
	
	private String warehouseId;
	
	private String itemCode;

	private Long sequenceIndicator;
	
	private Boolean capacityCheck ;
	
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

	private String updatedBy;
}
