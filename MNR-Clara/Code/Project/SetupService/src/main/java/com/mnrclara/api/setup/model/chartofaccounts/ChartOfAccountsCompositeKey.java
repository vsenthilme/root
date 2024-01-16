package com.mnrclara.api.setup.model.chartofaccounts;

import java.io.Serializable;

import lombok.Data;

@Data
public class ChartOfAccountsCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `ACCOUNT_NO`
	 */
	private String languageId;
	private Long classId;
	private String accountNumber;
}
