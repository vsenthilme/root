package com.mnrclara.api.cg.setup.model.city;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddCity {

	private String cityId;
	private String cityName;
	private String stateId;
	private String companyId;
	private String countryId;
	private Long zipCode;
	private String languageId;
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
