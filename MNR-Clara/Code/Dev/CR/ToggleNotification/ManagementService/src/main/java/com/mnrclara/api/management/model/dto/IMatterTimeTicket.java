package com.mnrclara.api.management.model.dto;

import java.util.Date;

public interface IMatterTimeTicket {
	
	public String getTimeTicketNumber();
	public String getLanguageId();
	public Long getClassId();
	public String getMatterNumber();
	public String getClientId();
	public String getClientName();
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
	public Long getDeletionIndicator();
	public String getCreatedBy();
    public Date getCreatedOn();
    public String getUpdatedBy();
	public Date getUpdatedOn();
	public Double getAssignedRatePerHour();

	public String getReferenceField1();
	public String getReferenceField2();
	public String getReferenceField3();
	public String getReferenceField4();
	public String getReferenceField5();
	public String getReferenceField6();
	public String getReferenceField7();
	public String getReferenceField8();
	public String getReferenceField9();
	public String getReferenceField10();
	public String getClassIdDesc();
	public String getMatterIdDesc();
	public String getClientIdDesc();
	public String getStatusDesc();
	public String getStimeTicketDate();
	public String getScreatedOn();
}
