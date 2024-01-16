package com.mnrclara.api.setup.model.docchecklist;

import java.io.Serializable;

import lombok.Data;

@Data
public class DocCheckListCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `CHK_LIST_NO`, `CASE_CATEGORY_ID`, `CASE_SUB_CATEGORY_ID`, `SEQ_NO`
	 */
	private String languageId;
	private Long classId;
	private Long checkListNo;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private Long sequenceNo;
}
