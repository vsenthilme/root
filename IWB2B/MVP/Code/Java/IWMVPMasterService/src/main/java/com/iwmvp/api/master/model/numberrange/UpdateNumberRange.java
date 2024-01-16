package com.iwmvp.api.master.model.numberrange;

import lombok.Data;
@Data
public class UpdateNumberRange {

	private Long numberRangeFrom;
	private Long numberRangeTo;
	private Long numberRangeCurrent;
	private Long numberRangeStatus;
	private Long deletionIndicator;
}
