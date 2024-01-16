package com.mnrclara.wrapper.core.model.report;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TimeKeeperBillingReportInput {

	private List<String> timekeeperCode;
	private List<Long> classId;

	private Date startDate;
	private Date endDate;

	private String FromDate;
	private String toDate;

}
