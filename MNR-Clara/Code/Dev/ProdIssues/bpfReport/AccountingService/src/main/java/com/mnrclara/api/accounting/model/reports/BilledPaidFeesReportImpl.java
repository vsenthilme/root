package com.mnrclara.api.accounting.model.reports;

public interface BilledPaidFeesReportImpl {

    String getTimekeeperCode();
    String getTimekeeperName();
    String getCaseCategory();
    String getCaseSubCategory();
    Double getMonthlyBilledAmount();
    Double getYearlyBilledAmount();
    Double getMonthlyBilledPercentage();
    Double getMonthlyPaidAmount();
    Double getMonthlyPaidPercentage();
    Double getYearlyBilledPercentage();
    Double getYearlyPaidAmount();
    Double getYearlyPaidPercentage();
    Double getMonthlyTimeTicketAmount();
    Double getYearlyTimeTicketAmount();
    Double getMonthlyInvoiceAmount();
    Double getYearlyInvoiceAmount();
    Double getMonthlyPaymentAmount();
    Double getYearlyPaymentAmount();
}