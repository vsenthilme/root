package com.mnrclara.api.setup.model.expensecode;

import java.io.Serializable;

import lombok.Data;

@Data
public class ExpenseCodeCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `EXP_CODE`
	 */
	private String languageId;
	private Long classId;
	private String expenseCode;
}
