package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

import java.util.Date;

@Data
public class BilledUnBilledHoursReport {
	String classId;
	String caseCategory;
	String caseSubCategory;
	String timeKeeperCode;
	Double billableHours;
	Double nonBillableHours;
	Double noCharge;
	Double totalHours;
	Double totalTimeTicketHours;
}
