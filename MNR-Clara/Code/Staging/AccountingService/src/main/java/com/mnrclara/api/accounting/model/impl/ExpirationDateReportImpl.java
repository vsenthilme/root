package com.mnrclara.api.accounting.model.impl;

import java.util.Date;

public interface ExpirationDateReportImpl {
    String getClientName();
    String getMatterText();
    String getEmployerPetitionerName();
    String getDocType();
    Date getEligibilityDate();
    Date getApprovalDate();
    Date getExpirationDate();
    String getOriginatingTk();
    String getResponsibleTk();
    String getAssignedTk();
    String getParalegal();
    String getLegalAssistant();
    String getReceiptNumber();
}
