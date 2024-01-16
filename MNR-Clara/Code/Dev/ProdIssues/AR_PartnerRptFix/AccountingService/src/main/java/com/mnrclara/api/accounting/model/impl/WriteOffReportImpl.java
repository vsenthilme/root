package com.mnrclara.api.accounting.model.impl;

import java.util.Date;

public interface WriteOffReportImpl {
    String getClientId();
    String getClientName();
    String getMatterNumber();
    String getMatterText();
    Date getDate();
    String getResponsibleTimeKeeper();
    Double getFees();
    Double getHardCosts();
    Double getSoftCosts();
    Double getTaxes();
    Double getLateCharges();
    Double getTotal();
}
