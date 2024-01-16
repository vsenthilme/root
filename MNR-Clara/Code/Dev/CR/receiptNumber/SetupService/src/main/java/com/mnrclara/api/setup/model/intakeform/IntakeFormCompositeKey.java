package com.mnrclara.api.setup.model.intakeform;

import java.io.Serializable;

import lombok.Data;

@Data
public class IntakeFormCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `CLIENT_TYP_ID`, `IT_FORM_ID`
	 */
	private String languageId;
	private Long classId;
	private Long clientTypeId;
	private Long intakeFormId;
}
