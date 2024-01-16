package com.mnrclara.api.setup.model.numberange;

import java.io.Serializable;

import lombok.Data;

@Data
public class NumberRangeCompositeKey implements Serializable {

	//`LANG_ID`, `CLASS_ID`, `NUM_RAN_CODE`, `NUM_RAN_OBJ`
	
	private String languageId = "EN";
	private Long classId;
	private Long numberRangeCode;
	private String numberRangeObject;
}
