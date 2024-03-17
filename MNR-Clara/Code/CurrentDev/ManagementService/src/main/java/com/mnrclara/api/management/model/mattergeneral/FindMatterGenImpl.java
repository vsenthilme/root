package com.mnrclara.api.management.model.mattergeneral;

import java.util.Date;

public interface FindMatterGenImpl {

    String getClassId();
    String getMatterNumber();
    String getMatterDescription();
    String getClientId();
    String getCaseCategoryId();
    String getCaseSubCategoryId();
    String getCaseInformationNo();
    Date getCaseOpenedDate();
    String getStatusId();

}
