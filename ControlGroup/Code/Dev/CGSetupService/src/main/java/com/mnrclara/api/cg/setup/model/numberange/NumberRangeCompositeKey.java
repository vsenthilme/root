package com.mnrclara.api.cg.setup.model.numberange;

import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Data
@Transactional
public class NumberRangeCompositeKey implements Serializable {

	//`LANG_ID`, `CLASS_ID`, `NUM_RAN_CODE`, `NUM_RAN_OBJ`
	
	private String languageId;
	private String companyId;
	private Long numberRangeCode;
	private String numberRangeObject;
}
