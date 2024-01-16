package com.mnrclara.api.accounting.model.prebill;

import java.util.Date;

public interface IMatterTimeTicket {
	
	public String getTimeTicketNumber();
	public String getLnguageId();
	public Long getClassId();
	public String getMatterNumber();
	public String getClientId();
	public String getTimeKeeperCode();
	public Long getCaseCategoryId();
	public Long getCaseSubCategoryId();
	public Double getTimeTicketHours();
	public Date getTimeTicketDate();
	public String getActivityCode();
	public String getTaskCode();
	public Double getDefaultRate();
	public String getRateUnit();
	public Double getTimeTicketAmount();
	public String getBillType();
	public String getTimeTicketDescription();
	public String getAssignedPartner();
	public Date getAssignedOn();
	public Double getApprovedBillableTimeInHours();
	public Double getApprovedBillableAmount();
	public Date getApprovedOn();
	public Long getStatusId();
	public String getCreatedBy();
    public Date getCreatedOn();
    public String getUpdatedBy();
	public Date getUpdatedOn();
}
