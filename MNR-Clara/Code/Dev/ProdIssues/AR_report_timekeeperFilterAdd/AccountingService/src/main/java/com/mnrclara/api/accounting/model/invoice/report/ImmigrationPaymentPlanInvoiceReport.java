package com.mnrclara.api.accounting.model.invoice.report;

import lombok.Data;

@Data
public class ImmigrationPaymentPlanInvoiceReport {

    String invoiceNumber;
    String invoiceAmount;
    String invoiceDate;
    String clientId;
    String clientName;
}
