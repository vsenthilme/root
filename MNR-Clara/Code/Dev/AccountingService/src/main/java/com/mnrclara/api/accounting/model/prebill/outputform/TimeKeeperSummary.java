package com.mnrclara.api.accounting.model.prebill.outputform;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TimeKeeperSummary {
	
	private String timeTicketName;
	private Double timeTicketAmount;
	private Double timeTicketHours;
	private Double timeTicketTotal;
	private Double timeTicketAssignedRate;
	private String timeTicketCode;
	private String billType;
}

