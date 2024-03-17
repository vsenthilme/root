package com.mnrclara.wrapper.core.model.report;

import lombok.Data;

@Data
public class ImmigrationPaymentPlanLineReport {

    String dueDate;
    String remainderDate;
    String balanceAmount;
    String paidAmount;
}
