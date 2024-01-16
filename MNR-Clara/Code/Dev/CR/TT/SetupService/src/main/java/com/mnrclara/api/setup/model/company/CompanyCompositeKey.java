package com.mnrclara.api.setup.model.company;

import java.io.Serializable;

import lombok.Data;

@Data
public class CompanyCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `COMP_ID`, `LANG_ID`
	 */
	private String companyId;
	private String languageId;
}
