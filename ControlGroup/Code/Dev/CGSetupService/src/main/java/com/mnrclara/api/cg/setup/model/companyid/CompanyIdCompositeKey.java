package com.mnrclara.api.cg.setup.model.companyid;

import lombok.Data;

import java.io.Serializable;

@Data
public class CompanyIdCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `C_ID`, `LANG_ID`
	 */
	 
	private String companyId;
	private String languageId;
}
