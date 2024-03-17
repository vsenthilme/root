package com.mnrclara.api.accounting.model.impl;

import java.util.Date;

public interface BilledHoursPaidImpl {
    String getMatterNumber();
    String getMatterText();
    String getAttorney();
    String getInvoiceNumber();
    Date   getDateOfBill();
    Double getAmountBilled();
    Double getHoursBilled();
    Double getApproxHoursPaid();
    Double getAmountReceived();
}
