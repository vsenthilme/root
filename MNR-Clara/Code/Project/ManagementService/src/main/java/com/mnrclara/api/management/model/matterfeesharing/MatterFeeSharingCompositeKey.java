package com.mnrclara.api.management.model.matterfeesharing;

import java.io.Serializable;

import lombok.Data;

@Data
public class MatterFeeSharingCompositeKey implements Serializable { 
	/*
	 * `LANG_ID`, `CLASS_ID`, `MATTER_NO`, `CLIENT_ID`, `TK_CODE`
	 */
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;	
	private String timeKeeperCode;
}
