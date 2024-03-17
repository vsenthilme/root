package com.mnrclara.wrapper.core.model.accounting.report;

import lombok.Data;

import java.util.Date;

@Data
public class ExpirationDateReport {
    String clientName;
    String matterText;
    String employerPetitionerName;
    String docType;
    Date eligibilityDate;
    Date approvalDate;
    Date expirationDate;
    String originatingTk;
    String responsibleTk;
    String assignedTk;
    String paralegal;
    String legalAssistant;
}
