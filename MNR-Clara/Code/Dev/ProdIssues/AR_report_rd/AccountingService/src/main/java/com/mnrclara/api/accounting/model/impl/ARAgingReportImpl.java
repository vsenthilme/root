package com.mnrclara.api.accounting.model.impl;

import java.util.Date;

public interface ARAgingReportImpl {
    String getMatterNumber();
    Long getClassId();
    String getClientId();
    Double getTotalAmountDue();
    Double getUnpaidCurrent();
    Double getUnpaid30To60Days();
    Double getUnpaid61To90Days();
    Double getUnpaid91DaysTo120Days();
    Double getUnpaidOver120();
    String getBillingNotes();
    String getClientName();
    String getAccountingPhoneNumber();
    Long getCaseCategoryId();
    Long getCaseSubCategoryId();
    Date getMatterOpenDate();
    String getMatterName();
    Date getLastPaymentDate();
    Double getFeeReceived();
    String getPartner();
    String getOriginatingTimeKeeper();
    String getResponsibleTimeKeeper();
    String getAssignedTimeKeeper();
    String getLegalAssistant();
    String getParalegal();
    String getLawClerk();
}
