package com.mnrclara.api.setup.model.activitycode;

import java.io.Serializable;

import lombok.Data;

@Data
public class ActivityCodeCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `ACT_CODE`
	 */
	private String languageId;
	private Long classId;
	private String activityCode;
}
