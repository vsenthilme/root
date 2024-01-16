package com.mnrclara.api.accounting.model.impl;

import java.util.Date;

public interface ARReportImpl {
    String getClassId();
    String getClientId();
    String getClientName();
    String getMatterNumber();
    String getMatterText();
    String getBillingFormat();
    String getPhone();
    Double getFeesDue();
    Double getHardCostsDue();
    Double getSoftCostsDue();
    Double getTotalDue();
    Date getLastBillDate();
    Date getLastPaymentDate();
}
