package com.mnrclara.api.setup.model.transaction;

import java.io.Serializable;

import lombok.Data;

@Data
public class TransactionCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `TRANS_ID`
	 */
	private String languageId;
	private Long classId;
	private Long transactionId;
}
