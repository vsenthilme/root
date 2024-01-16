package com.mnrclara.api.management.model.dto;

public interface IMatterTimeTicketCountAndSum {
	
	/*
	 * TIME_TICKET_COUNT, SUM(TIME_TICKET_AMOUNT) AS TIME_TICKET_AMOUNT
	 */
	public Long getTimeTicketCount();
	public Long getTimeTicketAmount();
	public String getMatterNumber();
}
