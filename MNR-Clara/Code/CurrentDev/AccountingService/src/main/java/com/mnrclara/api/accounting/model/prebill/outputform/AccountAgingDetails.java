package com.mnrclara.api.accounting.model.prebill.outputform;

import lombok.Data;

@Data
public class AccountAgingDetails {

	private Double currentAmount;
	private Double amount30To59Days;			// 30-59 Days
	private Double amount60To89Days;			// 60-89 Days
	private Double amount90DaysAndOver;			// 90 Days and Over
	
	private Double feesBilledToDate;	
	private Double costsBilledToDate;
}
