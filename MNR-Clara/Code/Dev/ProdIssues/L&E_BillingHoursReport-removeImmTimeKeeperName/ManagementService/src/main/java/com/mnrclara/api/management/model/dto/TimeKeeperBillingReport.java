package com.mnrclara.api.management.model.dto;

import lombok.Data;

@Data
public class TimeKeeperBillingReport {

	private String timekeeperCode;
	private String timekeeperName;

	private Double expectedBillableHours;
	private Double billableHours;
	private Double ratio;
	private Double billedHours;
	private Double nonBillableHours;
	private Double nonBilledHours;
	private Double averageBillingRate;
	private Double actualRates;
	private Double expectedBillingBasedOnExpectedBillableHours;
	private Double expectedBillingBasedOnLoggedBillableHours;
	private Double expectedFeeAndActualFeeRatio;

	private Double billedFees;
	private String billType;
	private Double defaultRate;

}
