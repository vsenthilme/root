package com.mnrclara.api.accounting.model.invoice.report;

import lombok.Data;

@Data
public class ImmigrationPaymentPlanLineReport {

    String dueDate;
    String remainderDate;
    String balanceAmount;
    String paidAmount;
}
