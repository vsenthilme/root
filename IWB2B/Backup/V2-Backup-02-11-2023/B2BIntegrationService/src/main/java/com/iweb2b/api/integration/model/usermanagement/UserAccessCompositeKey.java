package com.iweb2b.api.integration.model.usermanagement;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAccessCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * LANG_ID C_ID USR_ID
	 */
	private String languageId;
	private String companyCode;
	private String userId;
}
