package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

import java.util.List;

@Data
public class BilledPaidFees {

	private int month;
	private int year;
	private List<Long> caseCategoryId;
	private List<String> timeKeeperCode;
}
