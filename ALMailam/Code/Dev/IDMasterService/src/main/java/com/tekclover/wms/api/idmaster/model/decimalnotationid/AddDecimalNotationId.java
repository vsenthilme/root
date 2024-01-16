package com.tekclover.wms.api.idmaster.model.decimalnotationid;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddDecimalNotationId {

	private String companyCodeId;
	private String plantId;
	
	@NotBlank(message = "Warehouse Id is mandatory")
	private String warehouseId;
	
	@NotBlank(message = "Decimal Notation Id is mandatory")
	private String decimalNotationId;
	
	private String languageId;
	private String decimalNotation;
	private Long deletionIndicator;
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
}
