package com.mnrclara.api.management.model.mattergeneral;

import java.util.Date;

public interface ImmigrationReportImpl {

    String getClientId();
    String getClientName();
    String getFirstNameLastName();
    String getPetitionerContactNumber();
    String getPetitionerEmailId();
    String getBeneficiaryContactNumber();
    String getBenerficiaryEmailId();
    Long getClientCategoryId();
    String getCorporationClientId();
    String getPetitionerName();
    Date getClientOpenedDate();
    String getPotentialClientId();
    String getMatterNumber();
    String getMatterDescription();
    Date getCaseFiledDate();
    String getCreatedBy();
    String getReferredBy();
    String getStatus();
    String getNewMatterExistingClient();
    String getContactNumber();
    String getEmailId();
    String getBillModeText();
    Date getMatterOpenedDate();
    Double getFlatFeeAmount();
    Double getExpensesFee();
    Date getMatterClosedDate();
    String getRetainerPaid();
    String getCaseCategoryId();
    String getCaseSubCategoryId();
    String getOriginatingTimeKeeper();
    String getAssignedTimeKeeper();
    String getResponsibleTimeKeeper();
    String getLegalAssistant();
    String getParalegal();
    String getLawClerk();
    String getMatterNotes();
    String getPartner();
    String getAttorneysNotes();
}
