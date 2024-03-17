package com.mnrclara.api.accounting.model.reports;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BilledPaidFees {

	private int month;
	private int year;
	private List<Long> caseCategoryId;
	private List<String> timeKeeperCode;
}
