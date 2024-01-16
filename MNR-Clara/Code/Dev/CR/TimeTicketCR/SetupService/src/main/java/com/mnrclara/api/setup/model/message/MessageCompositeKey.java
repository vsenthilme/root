package com.mnrclara.api.setup.model.message;

import java.io.Serializable;

import lombok.Data;

@Data
public class MessageCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `MESSAGE_ID`, `MESSAGE_TYP`
	 */
	private String languageId;
	private Long classId;
	private Long messageId;
	private String messageType;
}
