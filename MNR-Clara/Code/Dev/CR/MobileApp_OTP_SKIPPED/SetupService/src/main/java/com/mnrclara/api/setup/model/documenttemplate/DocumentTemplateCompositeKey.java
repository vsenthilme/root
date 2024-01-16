package com.mnrclara.api.setup.model.documenttemplate;

import java.io.Serializable;

import lombok.Data;

@Data
public class DocumentTemplateCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `CASE_CATEGORY_ID`, `DOC_NO`, `DOC_URL`
	 */
	private String languageId;
	private Long classId;
	private Long caseCategoryId;
	private String documentNumber;
//	private String documentUrl;
}
