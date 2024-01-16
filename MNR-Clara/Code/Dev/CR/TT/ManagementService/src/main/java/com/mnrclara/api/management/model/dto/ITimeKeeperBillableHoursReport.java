package com.mnrclara.api.management.model.dto;

public interface ITimeKeeperBillableHoursReport {

	public Double getBillableHours();
	public Double getBilledHours();
	public Double getNonBillableHours();
	public Double getBilledFees();
	public String getBillType();

}
