package com.mnrclara.wrapper.core.model.report;

import lombok.Data;

@Data
public class ImmigrationPaymentPlanInvoiceReport {

    String invoiceNumber;
    String invoiceAmount;
    String invoiceDate;
    String clientId;
    String clientName;
}
