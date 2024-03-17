package com.mnrclara.api.accounting.model.prebill.outputform;

public interface IMatterTimeTicket {
	public Double getApprovedBillableAmount();
	public Double getTimeTicketHours();
	public Double getTimeTicketAmount();
	public Double getTtHours();
	public Double getTtAmount();
	public String getTimeKeeperCode();
	public String getTimeKeeperName();
	public String getBillType();
}
