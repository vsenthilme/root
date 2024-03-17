package com.mnrclara.wrapper.core.model.management;

import lombok.Data;

import java.util.Date;
@Data
public class FindMatterGenNew {

    String classId;
    String matterNumber;
    String matterDescription;
    String clientId;
    String caseCategoryId;
    String caseSubCategoryId;
    String caseInformationNo;
    Date caseOpenedDate;
    String sCaseOpenedDate;
    String statusId;

}
