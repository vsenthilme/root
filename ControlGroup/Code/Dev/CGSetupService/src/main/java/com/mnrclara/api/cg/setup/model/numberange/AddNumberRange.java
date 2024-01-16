package com.mnrclara.api.cg.setup.model.numberange;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AddNumberRange {

	private String companyId;
	private String languageId;
	private Long numberRangeCode;
	private String numberRangeObject;
	private String numberRangeFrom;
	private String numberRangeTo;
	private String numberRangeCurrent;
	private Long numberRangeStatus;
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
	private String createdBy;
}
