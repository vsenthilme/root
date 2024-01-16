package com.mnrclara.wrapper.core.model.cgtransaction;


import lombok.Data;

import java.util.Date;

@Data

public class BSEffectiveControl {

    private String languageId;

    private String companyId;

    private Long validationId;

    private Long clientId;

    private String clientName;

    private String subGroupId;

    private String subGroupName;

    private String groupId;

    private String groupName;

    private Long storeId1;

    private Long storeId2;

    private Long storeId3;

    private Long storeId4;

    private Long storeId5;

    private String storeName1;

    private String storeName2;

    private String storeName3;

    private String storeName4;

    private String storeName5;

    private Double storePercentage1;

    private Double storePercentage2;

    private Double storePercentage3;

    private Double storePercentage4;

    private Double storePercentage5;

    private Double effectiveControlPercentage;

    private Long statusId;

    private Long deletionIndicator = 0L;

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

    private String createdBy;

    private Date createdOn = new Date();

    private String updatedBy;

    private Date updatedOn = new Date();

}
