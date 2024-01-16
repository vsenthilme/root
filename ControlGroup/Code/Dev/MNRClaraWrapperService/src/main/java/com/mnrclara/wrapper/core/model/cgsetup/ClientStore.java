package com.mnrclara.wrapper.core.model.cgsetup;

import lombok.Data;
import java.util.Date;

@Data
public class ClientStore {

    private Long clientId;
    private String clientName;
    private Long storeId;
    private String storeName;
    private String companyId;
    private String languageId;
    private Long versionNumber;
    private Long statusId;
    private Long deletionIndicator;
    private String referenceField1;
    private String referenceField2;
    private String referenceField3;
    private String referenceField4;
    private String referenceField5;
    private String referenceField6;
    private String referenceField7;
    private String referenceField8;
    private String referenceField9;
    private String referenceField10;
    private Date validityDateFrom;
    private Date validityDateTo;
    private String createdBy;
    private Date createdOn = new Date();
    private String updatedBy;
    private Date updatedOn = new Date();
}