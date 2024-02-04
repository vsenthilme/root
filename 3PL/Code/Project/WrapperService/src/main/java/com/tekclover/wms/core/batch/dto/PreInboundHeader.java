package com.tekclover.wms.core.batch.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PreInboundHeader {

    private String languageId;
    private String companyCode;
    private String plantId;
    private String warehouseId;
    private String preInboundNo;
    private String refDocNumber;
    private Long inboundOrderTypeId;
    private String referenceDocumentType;
    private Long statusId;
    private String containerNo;
    private Long noOfContainers;
    private String containerType;
    private Date refDocDate;
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
    private Long deletionIndicator;
    private String createdBy;
    private String dType;

    //v2 fields
    private String companyDescription;
    private String plantDescription;
    private String warehouseDescription;
    private String statusDescription;


    /**
     * @param languageId
     * @param companyCode
     * @param plantId
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @param inboundOrderTypeId
     * @param referenceDocumentType
     * @param statusId
     * @param containerNo
     * @param noOfContainers
     * @param containerType
     * @param refDocDate
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
     * @param deletionIndicator
     * @param createdBy
     * @param companyDescription
     * @param plantDescription
     * @param warehouseDescription
     * @param statusDescription
     */
    public PreInboundHeader(String languageId, String companyCode, String plantId, String warehouseId, String preInboundNo, String refDocNumber,
                            Long inboundOrderTypeId, String referenceDocumentType, Long statusId, String containerNo, Long noOfContainers,
                            String containerType, Date refDocDate, String referenceField1, String referenceField2, String referenceField3,
                            String referenceField4, String referenceField5, String referenceField6, String referenceField7, String referenceField8,
                            String referenceField9, String referenceField10, Long deletionIndicator, String createdBy, String dType, String companyDescription,
                            String plantDescription, String warehouseDescription, String statusDescription) {

        this.languageId = languageId;
        this.companyCode = companyCode;
        this.plantId = plantId;
        this.warehouseId = warehouseId;
        this.preInboundNo = preInboundNo;
        this.refDocNumber = refDocNumber;
        this.inboundOrderTypeId = inboundOrderTypeId;
        this.referenceDocumentType = referenceDocumentType;
        this.statusId = statusId;
        this.containerNo = containerNo;
        this.noOfContainers = noOfContainers;
        this.containerType = containerType;
        this.refDocDate = refDocDate;
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
        this.deletionIndicator = deletionIndicator;
        this.createdBy = createdBy;
        this.dType = dType;
        this.companyDescription = companyDescription;
        this.plantDescription = plantDescription;
        this.warehouseDescription = warehouseDescription;
        this.statusDescription = statusDescription;
    }

}
