package com.mnrclara.api.accounting.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class BilledIncomeDashboard {

	private List<String[]> currentMonth;
	private List<String[]> currentQuarter;
	private List<String[]> currentYear;
	private List<String[]> previousMonth;
	private List<String[]> previousQuarter;
	private List<String[]> previousYear;
}
