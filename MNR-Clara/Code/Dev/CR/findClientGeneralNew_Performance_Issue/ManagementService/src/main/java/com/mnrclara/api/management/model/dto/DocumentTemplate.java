package com.mnrclara.api.management.model.dto;

import lombok.Data;

@Data
public class DocumentTemplate {

	private String documentNumber;
	private String languageId;
	private Long classId;
	private Long caseCategoryId;
	private Boolean mailMerge;
	private String documentUrl;
	private String documentFileDescription;
	private String documentStatus;
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
