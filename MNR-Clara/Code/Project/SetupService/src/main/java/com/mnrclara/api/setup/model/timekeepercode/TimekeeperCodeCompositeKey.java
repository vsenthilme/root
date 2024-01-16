package com.mnrclara.api.setup.model.timekeepercode;

import java.io.Serializable;

import lombok.Data;

@Data
public class TimekeeperCodeCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `TK_CODE`, `USR_TYP_ID`, `DEF_RATE`
	 */
	private String languageId;
	private Long classId;
	private String timekeeperCode;
	private Long userTypeId;
	private Double defaultRate;
}
