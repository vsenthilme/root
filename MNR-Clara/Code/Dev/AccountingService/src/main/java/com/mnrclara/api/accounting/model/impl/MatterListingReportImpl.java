package com.mnrclara.api.accounting.model.impl;

import java.util.Date;

public interface MatterListingReportImpl {
    String getClassId();
    String getMatterNumber();
    String getStatusId();
    String getMatterText();
    String getClientId();
    String getFirstLastName();
    Date getCaseOpenedDate();
    Date getCaseClosedDate();
    String getBillModeText();
    String getBillFrequencyText();
    String getCaseCategory();
    String getCaseSubCategory();
    String getResponsibleTk();
    String getOriginatingTk();
    String getTimeKeeperCode();
    Double getAssignedRate();
    
    String getPartner();
    String getAssignedTk();
    String getLegalAssist();
    String getParaLegal(); // referenceField2;
    String getPetitionerName();
    String getCorporateName();
}
