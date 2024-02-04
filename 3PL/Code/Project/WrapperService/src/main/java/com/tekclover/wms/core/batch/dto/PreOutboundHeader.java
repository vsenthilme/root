package com.tekclover.wms.core.batch.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PreOutboundHeader {

    private String languageId;
    private String companyCodeId;
    private String plantId;
    private String warehouseId;
    private String refDocNumber;
    private String preOutboundNo;
    private String partnerCode;
    private Long outboundOrderTypeId;
    private String referenceDocumentType;
    private Long statusId;
    private Date refDocDate;
    private Date requiredDeliveryDate;
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
    private String remarks;
    private String createdBy;
    private String dType;
    private String companyDescription;
    private String plantDescription;
    private String warehouseDescription;
    private String statusDescription;


    /**
     * @param languageId
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param refDocNumber
     * @param preOutboundNo
     * @param partnerCode
     * @param outboundOrderTypeId
     * @param referenceDocumentType
     * @param statusId
     * @param refDocDate
     * @param requiredDeliveryDate
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
     * @param remarks
     * @param createdBy
     * @param companyDescription
     * @param plantDescription
     * @param warehouseDescription
     * @param statusDescription
     */
    public PreOutboundHeader(String languageId, String companyCodeId, String plantId, String warehouseId, String refDocNumber,
                             String preOutboundNo, String partnerCode, Long outboundOrderTypeId, String referenceDocumentType,
                             Long statusId, Date refDocDate, Date requiredDeliveryDate, String referenceField1, String referenceField2,
                             String referenceField3, String referenceField4, String referenceField5, String referenceField6, String referenceField7,
                             String referenceField8, String referenceField9, String referenceField10, Long deletionIndicator, String remarks,
                             String createdBy,String dType, String companyDescription, String plantDescription, String warehouseDescription, String statusDescription) {

        this.languageId = languageId;
        this.companyCodeId = companyCodeId;
        this.plantId = plantId;
        this.warehouseId = warehouseId;
        this.refDocNumber = refDocNumber;
        this.preOutboundNo = preOutboundNo;
        this.partnerCode = partnerCode;
        this.outboundOrderTypeId = outboundOrderTypeId;
        this.referenceDocumentType = referenceDocumentType;
        this.statusId = statusId;
        this.refDocDate = refDocDate;
        this.requiredDeliveryDate = requiredDeliveryDate;
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
        this.remarks = remarks;
        this.createdBy = createdBy;
        this.dType = dType;
        this.companyDescription = companyDescription;
        this.plantDescription = plantDescription;
        this.warehouseDescription = warehouseDescription;
        this.statusDescription = statusDescription;
    }
}
