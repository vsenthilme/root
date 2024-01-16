package com.mnrclara.wrapper.core.model.report;

import lombok.Data;

import java.util.List;

@Data
public class ImmigrationPaymentPlanReport {

    String paymentPlanNumber;
    String clientId;
    String matterNumber;
    String quoteNumber;
    String paymentPlanAmount;
    String instalmentAmount;
    String startDate;
    String paymentPlanDate;
    String status;
    String clientName;
    String clientCell;
    String clientWorkNumber;
    String clientPhoneNumber;
    String dueDate;
    String remainderDate;
    String balanceAmount;
    String paidAmount;
//    private ImmigrationPaymentPlanLineReport lines;
    private List<ImmigrationPaymentPlanInvoiceReport> invoices;
}
