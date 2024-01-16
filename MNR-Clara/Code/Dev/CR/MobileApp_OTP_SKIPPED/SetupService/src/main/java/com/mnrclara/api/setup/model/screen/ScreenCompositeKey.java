package com.mnrclara.api.setup.model.screen;

import java.io.Serializable;

import lombok.Data;

@Data
public class ScreenCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `SCREEN_ID`, `SUB_SCREEN_ID`
	 */
	private String languageId;
	private Long screenId;
	private Long subScreenId;
}
