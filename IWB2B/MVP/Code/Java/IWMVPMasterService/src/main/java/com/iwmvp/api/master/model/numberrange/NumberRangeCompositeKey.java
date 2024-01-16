package com.iwmvp.api.master.model.numberrange;

import java.io.Serializable;
import lombok.Data;
@Data
public class NumberRangeCompositeKey implements Serializable {

	/*
	 * `LANG_ID`,`COMP_ID`, `NUM_RAN_CODE`,'NUM_RAN_OBJ'
	 */
	private static final long serialVersionUID = -7617672247680004647L;
	private String languageId;
	private String companyId;
	private Long numberRangeCode;
	private String numberRangeObject;
}
