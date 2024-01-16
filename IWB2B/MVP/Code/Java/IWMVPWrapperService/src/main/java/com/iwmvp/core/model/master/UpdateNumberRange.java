package com.iwmvp.core.model.master;

import lombok.Data;
@Data
public class UpdateNumberRange {
	private Long numberRangeFrom;
	private Long numberRangeTo;
	private Long numberRangeCurrent;
	private Long numberRangeStatus;
	private Long deletionIndicator;
}
