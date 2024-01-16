package com.mnrclara.wrapper.core.batch.dto;

import lombok.Data;

import java.util.Date;

@Data
public class StorePartnerListing {

    private String  languageId;
    private String  companyId;
    private String  storeId;
    private Long    versionNumber;
    private String  storeName;
    private Date    validityDateFrom;
    private Date    validityDateTo;
    private String  groupTypeId;
    private String  groupTypeName;
    private String  subGroupId;
    private String  subGroupName;
    private String  groupId;
    private String  groupName;
    private Long    coOwnerId1;
    private Long    coOwnerId2;
    private Long    coOwnerId3;
    private Long    coOwnerId4;
    private Long    coOwnerId5;
    private String  coOwnerName1;
    private String  coOwnerName2;
    private String  coOwnerName3;
    private String  coOwnerName4;
    private String  coOwnerName5;
    private Double  coOwnerPercentage1;
    private Double  coOwnerPercentage2;
    private Double  coOwnerPercentage3;
    private Double  coOwnerPercentage4;
    private Double  coOwnerPercentage5;
    private Long    statusId;
    private Long    deletionIndicator;
    private String  referenceField1;
    private String  referenceField2;
    private String  referenceField3;
    private String  referenceField4;
    private String  referenceField5;
    private String  referenceField6;
    private String  referenceField7;
    private String  referenceField8;
    private String  referenceField9;
    private String  referenceField10;
    private String  createdBy;
    private Date    createdOn;
    private String  updatedBy;
    private Date    updatedOn;
    public StorePartnerListing (String languageId,String companyId,String storeId,Long versionNumber,String storeName,Date validityDateFrom,
                                Date validityDateTo,String groupTypeId,String groupTypeName,String subGroupId,String subGroupName,String groupId,String groupName,
                                Long coOwnerId1,Long coOwnerId2,Long coOwnerId3,Long coOwnerId4,Long coOwnerId5,String coOwnerName1,String coOwnerName2,
                                String coOwnerName3,String coOwnerName4,String coOwnerName5,Double coOwnerPercentage1,Double coOwnerPercentage2,Double coOwnerPercentage3,
                                Double coOwnerPercentage4,Double coOwnerPercentage5,Long statusId,Long deletionIndicator,String referenceField1,String referenceField2,
                                String referenceField3,String referenceField4,String referenceField5,String referenceField6,String referenceField7,String referenceField8,
                                String referenceField9,String referenceField10,String createdBy,Date createdOn,String updatedBy,Date updatedOn) {
        this.languageId   = languageId;
        this.companyId   = companyId;
        this.storeId   = storeId;
        this.versionNumber   = versionNumber;
        this.storeName   = storeName;
        this.validityDateFrom   = validityDateFrom;
        this.validityDateTo   = validityDateTo;
        this.groupTypeId   = groupTypeId;
        this.groupTypeName   = groupTypeName;
        this.subGroupId   = subGroupId;
        this.subGroupName   = subGroupName;
        this.groupId   = groupId;
        this.groupName   = groupName;
        this.coOwnerId1   = coOwnerId1;
        this.coOwnerId2   = coOwnerId2;
        this.coOwnerId3   = coOwnerId3;
        this.coOwnerId4   = coOwnerId4;
        this.coOwnerId5   = coOwnerId5;
        this.coOwnerName1   = coOwnerName1;
        this.coOwnerName2   = coOwnerName2;
        this.coOwnerName3   = coOwnerName3;
        this.coOwnerName4   = coOwnerName4;
        this.coOwnerName5   = coOwnerName5;
        this.coOwnerPercentage1   = coOwnerPercentage1;
        this.coOwnerPercentage2   = coOwnerPercentage2;
        this.coOwnerPercentage2   = coOwnerPercentage3;
        this.coOwnerPercentage4   = coOwnerPercentage4;
        this.coOwnerPercentage5   = coOwnerPercentage5;
        this.statusId   = statusId;
        this.deletionIndicator   = deletionIndicator;
        this.referenceField1   = referenceField1;
        this.referenceField2   = referenceField2;
        this.referenceField3   = referenceField3;
        this.referenceField4   = referenceField4;
        this.referenceField5   = referenceField5;
        this.referenceField6   = referenceField6;
        this.referenceField7   = referenceField7;
        this.referenceField8   = referenceField8;
        this.referenceField9   = referenceField9;
        this.referenceField10   = referenceField10;
        this.createdBy   = createdBy;
        this.createdOn   = createdOn;
        this.updatedBy   = updatedBy;
        this.updatedOn   = updatedOn;
    }
}