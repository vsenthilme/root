package com.mnrclara.api.management.model.dto;

public interface IMatterExpenseCountAndSum {
	
	/*
	 * TIME_TICKET_COUNT, SUM(TIME_TICKET_AMOUNT) AS TIME_TICKET_AMOUNT
	 */
	public Long getExpenseCount();
	public Long getExpenseAmount();
	public String getMatterNumber();
	public String getExpenseType();
}
