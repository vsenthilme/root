package com.mnrclara.wrapper.core.model.accounting;

import lombok.Data;

@Data
public class TimeKeeperSummary {

	private String timeTicketName;
	private Double timeTicketAmount;
	private Double timeTicketHours;
	private Double timeTicketTotal;
	private Double timeTicketAssignedRate;
	private String timeTicketCode;
	private String billType;
	private String userTypeDescription;
}

