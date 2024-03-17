package com.mnrclara.api.setup.model.clientuser;

import java.util.Date;

public interface ClientUserImpl {

    String getLanguageId();
    Long getClassId();
    String getClientId();
    String getContactNumber();
    String getFirstName();
    String getLastName();
    String getEmailId();
    String getDeletionIndicator();
    String getReferenceField5();
    String getReferenceField6();
    String getReferenceField7();
    String getReferenceField8();
    String getReferenceField10();
    Integer getQuotation();
    Integer getPaymentPlan();
    Integer getMatter();
    Integer getInvoice();
    Integer getDocuments();

    Integer getReceiptNumber();
    String getReferenceField2();
    Integer getAgreement();
    String getClientUserId();
    String getCreatedBy();
    Date getCreatedOn();
    String getUpdatedBy();
    Date getUpdatedOn();
}
