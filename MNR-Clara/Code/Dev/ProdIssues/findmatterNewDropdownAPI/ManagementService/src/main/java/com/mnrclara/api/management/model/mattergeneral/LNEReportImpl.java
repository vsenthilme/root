package com.mnrclara.api.management.model.mattergeneral;

import java.util.Date;

public interface LNEReportImpl {
    String getCorporateName();
    String getClientId();
    String getCorporationClientId();
    String getCaseInfoNumber();
    String getPotentialClientId();
    String getMatterNumber();
    String getMatterDescription();
    String getCreatedBy();
    String getUpdatedBy();
    String getReferredBy();
    String getStatus();
    String getBillingmodeText();
    Date getMatterOpenedDate();
    Date getMatterClosedDate();
    String getCaseCategory();
    String getCaseSubCategory();
    String getNoteDescription();
    String getOriginatingTimeKeeper();
    String getAssignedTimeKeeper();
    String getResponsibleTimeKeeper();
    String getLegalAssistant();
    String getLawclerk();
    String getParalegal();
    String getPartner();
}
