package com.mnrclara.api.cg.setup.model.country;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddCountry {

    private String countryId;
	private String countryName;
	private String companyId;
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
