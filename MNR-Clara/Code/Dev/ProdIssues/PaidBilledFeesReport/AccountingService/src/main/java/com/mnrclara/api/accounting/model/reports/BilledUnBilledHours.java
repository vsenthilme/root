package com.mnrclara.api.accounting.model.reports;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BilledUnBilledHours {

	private List<Long> classId;
	private List<Long> caseCategory;
	private List<Long> caseSubCategory;
	private List<String> timeKeeper;
	private Date fromDate;
	private Date toDate;
}
