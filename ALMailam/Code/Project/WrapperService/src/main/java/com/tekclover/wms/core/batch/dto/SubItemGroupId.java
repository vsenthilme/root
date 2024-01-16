package com.tekclover.wms.core.batch.dto;

import lombok.Data;

@Data
public class SubItemGroupId {

    private String companyCodeId;
    private String plantId;
    private String warehouseId;
    private Long itemTypeId;
    private Long itemGroupId;
    private Long subItemGroupId;
    private String languageId;
    private String subItemGroup;
    private String description;
    private String companyIdAndDescription;
    private String plantIdAndDescription;
    private String warehouseIdAndDescription;
    private String itemTypeIdAndDescription;
    private String itemGroupIdAndDescription;
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
    private String createdBy;


    /**
     *
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param itemTypeId
     * @param itemGroupId
     * @param subItemGroupId
     * @param languageId
     * @param subItemGroup
     * @param description
     * @param companyIdAndDescription
     * @param plantIdAndDescription
     * @param warehouseIdAndDescription
     * @param itemTypeIdAndDescription
     * @param itemGroupIdAndDescription
     * @param deletionIndicator
     * @param referenceField1
     * @param referenceField2
     * @param referenceField3
     * @param referenceField4
     * @param referenceField5
     * @param referenceField6
     * @param referenceField7
     * @param referenceField8
     * @param referenceField9
     * @param referenceField10
     * @param createdBy
     */
    public SubItemGroupId (String companyCodeId,String plantId,String warehouseId,Long itemTypeId,Long itemGroupId,
                           Long subItemGroupId,String languageId,String subItemGroup,String description,String companyIdAndDescription,
                           String plantIdAndDescription,String warehouseIdAndDescription,String itemTypeIdAndDescription,
                           String itemGroupIdAndDescription,Long deletionIndicator,String referenceField1, String referenceField2,
                           String referenceField3,String referenceField4,String referenceField5,String referenceField6,
                           String referenceField7,String referenceField8,String referenceField9,String referenceField10,String createdBy){

        this.companyCodeId = companyCodeId;
        this.plantId = plantId;
        this.warehouseId = warehouseId;
        this.itemTypeId = itemTypeId;
        this.itemGroupId = itemGroupId;
        this.subItemGroupId = subItemGroupId;
        this.languageId = languageId;
        this.subItemGroup = subItemGroup;
        this.description = description;
        this.companyIdAndDescription = companyIdAndDescription;
        this.plantIdAndDescription = plantIdAndDescription;
        this.warehouseIdAndDescription = warehouseIdAndDescription;
        this.itemTypeIdAndDescription = itemTypeIdAndDescription;
        this.itemGroupIdAndDescription = itemGroupIdAndDescription;
        this.deletionIndicator = deletionIndicator;
        this.referenceField1 = referenceField1;
        this.referenceField2 = referenceField2;
        this.referenceField3 = referenceField3;
        this.referenceField4 = referenceField4;
        this.referenceField5 = referenceField5;
        this.referenceField6 = referenceField6;
        this.referenceField7 = referenceField7;
        this.referenceField8 = referenceField8;
        this.referenceField9 = referenceField9;
        this.referenceField10 = referenceField10;
        this.createdBy = createdBy;

    }

}
