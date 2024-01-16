package com.mnrclara.api.accounting.model.impl;

import java.util.Date;

public interface MatterPLReportImpl {

    String getClientId();
    String getClientName();
    String getMatterNumber();
    String getMatterDescription();
    String getPartnerAssigned();
    String getInvoiceDate();
    String getInvoiceNumber();
    Double getTimeticketCaptured();
    Double getCostCaptured();
}
