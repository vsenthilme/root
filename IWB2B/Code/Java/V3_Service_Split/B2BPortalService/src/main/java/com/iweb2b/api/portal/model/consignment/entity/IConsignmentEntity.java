package com.iweb2b.api.portal.model.consignment.entity;

import java.util.Date;

public interface IConsignmentEntity {
	
    public String getReferenceNumber();
    public String getCustomerReferenceNumber();
    public String getStatusDescription();
    public Date getCreatedAt();
    public Boolean getIsAwbPrinted();
    public String getActionTime();
    public String getQpWebhookStatus();
    public String getCustomerCode();
    public String getOrderType();
	public String getAwb3rdParty();
	public String getScanType();
	public String getActionName();
}
