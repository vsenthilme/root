package com.mnrclara.api.management.model.matteritform;

import java.io.Serializable;

import lombok.Data;

@Data
public class MatterITFormCompositeKey implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5125676428138182450L;
	/*
	 * `LANG_ID`, `CLASS_ID`,`MATTER_NO`, `CLIENT_ID`,  `IT_FORM_ID`,  `IT_FORM_NO`
	 */
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private Long intakeFormId;
	private String intakeFormNumber;
}
