package com.mnrclara.api.accounting.model.impl;

import java.util.Date;

public interface BilledUnBillerHoursReportImpl {
    String getClassId();
    String getCaseCategory();
    String getCaseSubCategory();
    String getTimeKeeperCode();
    Double getBillableHours();
    Double getNonBillableHours();
    Double getNoCharge();
    Double getTotalHours();
    Double getTotalTimeTicketHours();
}
