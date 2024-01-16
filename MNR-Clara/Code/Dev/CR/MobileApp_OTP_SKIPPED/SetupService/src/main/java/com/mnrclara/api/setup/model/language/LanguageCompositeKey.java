package com.mnrclara.api.setup.model.language;

import java.io.Serializable;

import lombok.Data;

@Data
public class LanguageCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`
	 */
	private String languageId;
	private Long classId;
}
