package com.mnrclara.api.setup.model.clientuser;

import java.io.Serializable;

import lombok.Data;

@Data
public class ClientUserCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `CLIENT_USR_ID`, `CLIENT_ID`
	 */
	private String languageId;
	private Long classId;
	private String clientUserId;
	private String clientId;
}
