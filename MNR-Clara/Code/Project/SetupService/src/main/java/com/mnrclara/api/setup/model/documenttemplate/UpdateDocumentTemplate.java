package com.mnrclara.api.setup.model.documenttemplate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateDocumentTemplate {

	// `LANG_ID`, `CLASS_ID`, `CASE_CATEGORY_ID`, `DOC_NO`, `DOC_URL`
	@NotBlank(message = "Language ID is mandatory")
    private String languageId;
	
	@NotNull(message = "Class ID is mandatory")
	private Long classId;
	
	@NotNull(message = "Case Category ID is mandatory")
	private Long caseCategoryId;

	@NotBlank(message = "Document Number is mandatory")
	private String documentNumber;
	
	@NotBlank(message = "Document URL is mandatory")
	private String documentUrl;
	
	private String documentFileDescription;
	
	private String documentStatus;
	
	private Boolean mailMerge;
	
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
	
	private String updatedBy;
}
