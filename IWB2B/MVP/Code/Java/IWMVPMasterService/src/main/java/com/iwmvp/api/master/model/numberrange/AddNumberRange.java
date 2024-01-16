package com.iwmvp.api.master.model.numberrange;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
@Data
public class AddNumberRange {
	private String languageId;
	private String companyId;
	@NotNull(message = "Number Range Code is Mandatory")
	private Long numberRangeCode;
	@NotBlank(message = "Number Range Object is Mandatory")
	private String numberRangeObject;
	private Long numberRangeFrom;
	private Long numberRangeTo;
	private Long numberRangeCurrent;
	private Long numberRangeStatus;
	private Long deletionIndicator;

}
