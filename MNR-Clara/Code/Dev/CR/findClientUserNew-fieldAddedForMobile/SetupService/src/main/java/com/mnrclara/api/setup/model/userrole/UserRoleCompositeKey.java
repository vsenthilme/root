package com.mnrclara.api.setup.model.userrole;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserRoleCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `USR_ROLE_ID`, `SCREEN_ID`, `SUB_SCREEN_ID`
	 */
	private String languageId;
	private Long userRoleId;
	private Long screenId;
	private Long subScreenId;
}
