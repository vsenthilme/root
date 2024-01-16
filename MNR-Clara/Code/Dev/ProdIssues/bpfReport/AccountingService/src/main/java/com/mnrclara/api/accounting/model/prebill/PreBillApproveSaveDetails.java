package com.mnrclara.api.accounting.model.prebill;

import lombok.Data;

@Data
public class PreBillApproveSaveDetails {

	private String timeTicketNumber;
	private String matterNumber;
	private String preBillNumber;
	private Double approvedBillTime;
	private Double approvedBillAmount;
	private String approvedDescription;
	private String billType;
	private String activityCode;
	private String taskCode;
}
