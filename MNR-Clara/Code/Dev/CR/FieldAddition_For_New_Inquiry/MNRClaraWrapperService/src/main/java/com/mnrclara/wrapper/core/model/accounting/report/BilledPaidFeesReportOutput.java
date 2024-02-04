package com.mnrclara.wrapper.core.model.accounting.report;

import lombok.Data;

@Data
public class BilledPaidFeesReportOutput {
	String caseCategory;
	String caseSubCategory;
	String timekeeperCode;
	String timekeeperName;
	Double monthlyBilledAmount;
	Double monthlyTimeTicketAmount;
	Double yearlyBilledAmount;
	Double yearlyTimeTicketAmount;
	Double monthlyBilledPercentage;
	Double monthlyPaidAmount;
	Double monthlyPaidPercentage;
	Double yearlyBilledPercentage;
	Double yearlyPaidAmount;
	Double yearlyPaidPercentage;
}
