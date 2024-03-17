package com.mnrclara.api.accounting.model.impl;

public interface BilledPaidFeesImpl {
    String getCaseCategory();
    String getCaseSubCategory();
    String getTimeKeeper();
    Double getBilledSumA();
    Double getBilledSumB();
    Double getBilledPercent();
    Double getPaidAmount();
    Double getBillAmount();
    Double getTotalTime();
    Double getAssignedRate();
    Double getPaidPercent();
    Double getBilledYearToDateA();
    Double getBilledYearToDateB();
    Double getBilledPercentYearToDate();
    Double getPaidYearToDate();
    Double getBillYearToDate();
    Double getTotalTimeYearToDate();
    Double getAssignedRateYearToDate();
    Double getPaidPercentYearToDate();
}
