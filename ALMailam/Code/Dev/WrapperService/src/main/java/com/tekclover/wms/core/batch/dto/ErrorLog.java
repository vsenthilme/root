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

    public ErrorLog (
            String orderId, String orderTypeId, Date orderDate, String errorMessage,
            String languageId, String companyCode, String plantId, String warehouseId,
            String refDocNumber
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
    }
}