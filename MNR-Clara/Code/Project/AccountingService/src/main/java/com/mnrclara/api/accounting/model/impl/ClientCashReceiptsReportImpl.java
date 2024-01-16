package com.mnrclara.api.accounting.model.impl;

import java.util.Date;

public interface ClientCashReceiptsReportImpl {
    String getMatterNumber();
    String getMatterText();
    String getClientId();
    String getClientName();
    Date getPaymentDate();
    Double getPaymentAmount();
}
