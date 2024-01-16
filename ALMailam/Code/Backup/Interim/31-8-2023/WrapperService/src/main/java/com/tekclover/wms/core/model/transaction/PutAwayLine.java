package com.tekclover.wms.core.model.transaction;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PutAwayLine {

    protected String languageId;
    protected String companyCode;
    protected String plantId;
    protected String warehouseId;
    protected String goodsReceiptNo;
    protected String preInboundNo;
    protected String refDocNumber;
    protected String putAwayNumber;
    protected Long lineNo;
    protected String itemCode;
    protected String proposedStorageBin;
    protected String confirmedStorageBin;
    protected Double putAwayQuantity;
    protected String putAwayUom;
    protected Double putawayConfirmedQty;
    protected Long variantCode;
    protected String variantSubCode;
    protected String storageMethod;
    protected String batchSerialNumber;
    protected Long outboundOrderTypeId;
    protected Long stockTypeId;
    protected Long specialStockIndicatorId;
    protected String referenceOrderNo;
    protected Long statusId;
    protected String description;
    protected String specificationActual;
    protected String vendorCode;
    protected String manufacturerPartNo;
    protected String hsnCode;
    protected String itemBarcode;
    protected Date manufacturerDate;
    protected Date expiryDate;
    protected Double storageQty;
    protected String storageTemperature;
    protected String storageUom;
    protected String quantityType;
    protected String proposedHandlingEquipment;
    protected String assignedUserId;
    protected Long workCenterId;
    protected String putAwayHandlingEquipment;
    protected String putAwayEmployeeId;
    protected String remarks;
    protected String referenceField1;
    protected String referenceField2;
    protected String referenceField3;
    protected String referenceField4;
    protected String referenceField5;
    protected String referenceField6;
    protected String referenceField7;
    protected String referenceField8;
    protected String referenceField9;
    protected String referenceField10;
    protected Long deletionIndicator = 0L;
    protected String createdBy;
    protected Date createdOn = new Date();
    protected String confirmedBy;
    protected Date confirmedOn = new Date();
    protected String updatedBy;
    protected Date updatedOn = new Date();
    protected String packBarcodes;
}
