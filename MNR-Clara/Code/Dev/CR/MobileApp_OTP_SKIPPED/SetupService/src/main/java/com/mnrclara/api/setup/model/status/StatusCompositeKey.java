package com.mnrclara.api.setup.model.status;

import java.io.Serializable;

import lombok.Data;

@Data
public class StatusCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `STATUS_ID`
	 */
	private String languageId;
	private Long statusId;
}
