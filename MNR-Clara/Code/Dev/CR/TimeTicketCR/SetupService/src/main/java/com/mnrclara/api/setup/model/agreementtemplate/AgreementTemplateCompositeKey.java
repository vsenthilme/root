package com.mnrclara.api.setup.model.agreementtemplate;

import java.io.Serializable;

import lombok.Data;

@Data
public class AgreementTemplateCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `CASE_CATEGORY_ID`, `AGREEMENT_CODE`, `AGREEMENT_URL`
	 */
	private String languageId;
	private Long classId;
	private Long caseCategoryId;
	private String agreementCode;
	private String agreementUrl;
}
