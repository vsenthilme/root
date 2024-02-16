package com.mnrclara.api.management.model.mattertimeticket;

import java.util.Date;

public interface IMatterTimeTicket {
	
	public String getMatterNumber();
	public String getTimeTicketNo();
	public Date getTimeTicketDate();
	public String getTimeKeeperCode();
	public String getTimeTicketDescription();
	public Double getTimeTicketHours();
	public Double getTimeTicketAmount();
	public String getBillType();
	public String getReferenceField4();
	public Double getApprovedBillableTimeInHours();
	public Double getApprovedBillableAmount();
}
