package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

@Data
public class MatterBillingActvityReportInput {
	private String matterNumber;
	private String fromPostingDate;
	private String toPostingDate;
//	private Date fromPostingDate;
//	private Date toPostingDate;
}
