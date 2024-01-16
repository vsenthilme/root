package com.mnrclara.api.accounting.model.impl;

import java.util.Date;

public interface ARReportImpl {
//    Long getClassId();
    String getClassId();
    String getClientId();
    String getClientName();
    String getMatterNumber();
    Long getCaseCategory();
    Long getCaseSubCategory();
    String getMatterText();
    String getBillingFormat();
    String getPhone();
    Double getFeesDue();
    Double getHardCostsDue();
    Double getSoftCostsDue();
    Double getTotalDue();
    Date getPostingDate();
    Date getLastBillDate();
    Date getLastPaymentDate();
    String getPartner();
    String getOriginatingTimeKeeper();
    String getResponsibleTimeKeeper();
    String getAssignedTimeKeeper();
    String getLegalAssistant();
    String getParalegal();
    String getLawClerk();

    Double getPaidAmount();
    Double getInvoiceAmount();
    Date getPaymentDate();
    Date getInvoiceDate();
}
