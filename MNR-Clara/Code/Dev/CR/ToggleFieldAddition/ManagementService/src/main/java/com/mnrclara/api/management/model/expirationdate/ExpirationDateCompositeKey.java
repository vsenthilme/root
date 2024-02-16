package com.mnrclara.api.management.model.expirationdate;

import java.io.Serializable;

import lombok.Data;

@Data
public class ExpirationDateCompositeKey implements Serializable { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8525273480383747790L;
	/*
	 * `LANG_ID`, `CLASS_ID`, `MATTER_NO`, `CLIENT_ID`, `DOC_TYPE`
	 */
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;	
	private String documentType;
}
