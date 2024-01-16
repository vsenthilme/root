package com.mnrclara.api.management.model.mattergeneral;

import java.util.Date;

public interface AttorneyProductivityReportImpl {

    String getClassId();
    String getClientId();
    String getClientName();
    String getMatterNumber();
    String getMatterDescription();
    String getCaseCategoryId();
    String getCaseSubCategoryId();
    String getFlatFee();
    String getOriginatingTimeKeeper();
    String getAssignedTimeKeeper();
    String getResponsibleTimeKeeper();
    String getLegalAssistant();
    String getParalegal();
    Date getCaseOpenDate();
    Date getCaseFiledDate();
    String getStatusDescription();

}
