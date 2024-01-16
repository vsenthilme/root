package com.mnrclara.api.accounting.model.impl;

import java.util.Date;

public interface PartnerBillingReportImpl {
    String getClassId();
    String getClassDescription();
    String getClientId();
    String getClientName();
    String getMatterNumber();
    String getMatterDescription();
    String getPartner();
    String getResponsibleTimekeeper();
    String getAssignedTimekeeper();

    Double getPaidAmount();
    Double getTotalBilled();
    Double getSoftCost();
    Double getHardCost();
    Double getFeeBilled();
    Double getBalance();
}
