package com.tekclover.wms.core.batch.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorLog {

    private String orderId;
    private String orderTypeId;
    private Date orderDate;
    private String errorMessage;
    private String languageId;
    private String companyCode;
    private String plantId;
    private String warehouseId;
    private String refDocNumber;
    private String itemCode;
    private String manufacturerName;
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
    private Date createdOn;

    public ErrorLog (
            String orderId, String orderTypeId, Date orderDate, String errorMessage,
            String languageId, String companyCode, String plantId, String warehouseId,
            String refDocNumber, String itemCode, String manufacturerName, String referenceField1, String referenceField2,
            String referenceField3, String referenceField4, String referenceField5, String referenceField6, String referenceField7,
            String referenceField8, String referenceField9, String referenceField10, String createdBy, Date createdOn
    ) {
        this.orderId        =   orderId;
        this.orderTypeId    =   orderTypeId;
        this.orderDate      =   orderDate;
        this.errorMessage   =   errorMessage;
        this.languageId     =   languageId;
        this.companyCode    =   companyCode;
        this.plantId        =   plantId;
        this.warehouseId    =   warehouseId;
        this.refDocNumber   =   refDocNumber;
        this.itemCode = itemCode;
        this.manufacturerName = manufacturerName;
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
        this.createdOn = createdOn;
    }
}