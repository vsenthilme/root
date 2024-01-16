package com.mnrclara.api.cg.setup.model.clientstore;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateClientStore {

    private Long clientId;
    private Long storeId;
    private String companyId;
    private String languageId;
    private String clientName;
    private String storeName;
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
}