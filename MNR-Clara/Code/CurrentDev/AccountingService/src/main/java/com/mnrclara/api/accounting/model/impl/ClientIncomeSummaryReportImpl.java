package com.mnrclara.api.accounting.model.impl;

public interface ClientIncomeSummaryReportImpl {
    String getClientId();
    String getClientName();
    String getExpenseCode();
    Double getTotalReceipts();
    Double getTotalRefund();
    Double getNetReceipts();
}
