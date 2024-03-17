package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

@Data
public class BilledPaidFeesReport {
	String caseCategory;
	String caseSubCategory;
	String timeKeeper;
	Double billedSumA;
	Double billedSumB;
	Double billedPercent;
	Double paidAmount;
	Double billAmount;
	Double totalTime;
	Double assignedRate;
	Double paidPercent;
	Double billedYearToDateA;
	Double billedYearToDateB;
	Double billedPercentYearToDate;
	Double paidYearToDate;
	Double billYearToDate;
	Double totalTimeYearToDate;
	Double assignedRateYearToDate;
	Double paidPercentYearToDate;
}
