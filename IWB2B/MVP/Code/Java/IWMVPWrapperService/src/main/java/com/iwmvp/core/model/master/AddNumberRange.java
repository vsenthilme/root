package com.iwmvp.core.model.master;

import lombok.Data;
@Data
public class AddNumberRange {
	private String languageId;
	private String companyId;
	private Long numberRangeCode;
	private String numberRangeObject;
	private Long numberRangeFrom;
	private Long numberRangeTo;
	private Long numberRangeCurrent;
	private Long numberRangeStatus;
	private Long deletionIndicator;

}
