package com.mnrclara.api.accounting.model.invoice.report;

import lombok.Data;

@Data
public class MatterBillingActvityReportInput {
	private String matterNumber;
	private String fromPostingDate;
	private String toPostingDate;
}
