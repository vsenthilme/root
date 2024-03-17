package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PreBillApproveSaveDetails {

	@NotNull
	private String timeTicketNumber;
	@NotNull
	private String matterNumber;
	@NotNull
	private String preBillNumber;
	@NotNull
	private Double approvedBillTime;
	@NotNull
	private Double approvedBillAmount;
	@NotNull
	private String approvedDescription;
	@NotNull
	private String billType;
	
	private String activityCode;
	private String taskCode;
}
