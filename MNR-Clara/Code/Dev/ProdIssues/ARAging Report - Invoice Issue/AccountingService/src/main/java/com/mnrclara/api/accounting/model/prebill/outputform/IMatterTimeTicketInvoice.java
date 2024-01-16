package com.mnrclara.api.accounting.model.prebill.outputform;

public interface IMatterTimeTicketInvoice {
	
	public Double getApprovedBillableAmount();
	public Double getApprovedBillableHours();
	public Double getApprovedTotalAmount();
	public String getTimeKeeperCode();
	public String getBillType();
}
