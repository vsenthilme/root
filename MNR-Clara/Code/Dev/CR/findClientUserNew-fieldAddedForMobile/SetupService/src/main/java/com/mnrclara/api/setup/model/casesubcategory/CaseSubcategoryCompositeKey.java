package com.mnrclara.api.setup.model.casesubcategory;

import java.io.Serializable;

import lombok.Data;

@Data
public class CaseSubcategoryCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `CASE_CATEGORY_ID`, `CASE_SUB_CATEGORY_ID`
	 */
	private String languageId;
	private Long classId;
	private Long caseCategoryId;
	private Long caseSubcategoryId;
}
