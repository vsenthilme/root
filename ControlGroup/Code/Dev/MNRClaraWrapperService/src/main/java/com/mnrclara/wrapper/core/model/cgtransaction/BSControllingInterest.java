package com.mnrclara.wrapper.core.model.cgtransaction;


import lombok.Data;

import java.util.Date;

@Data
public class BSControllingInterest {

    private String languageId;

    private String companyId;

    private Long validationId;

    private String storeId;

    private String storeName;

    private String subGroupId;

    private String subGroupName;

    private String groupId;

    private String groupName;

    private Long coOwnerId1;

    private Long coOwnerId2;

    private Long coOwnerId3;

    private Long coOwnerId4;

    private String coOwnerName1;

    private String coOwnerName2;

    private String coOwnerName3;

    private String coOwnerName4;

    private String coOwnerName5;

    private Double coOwnerPercentage1;

    private Double coOwnerPercentage2;

    private Double coOwnerPercentage3;

    private Double coOwnerPercentage4;

    private Double coOwnerPercentage5;

    private Double controlInterestPercentage;

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
