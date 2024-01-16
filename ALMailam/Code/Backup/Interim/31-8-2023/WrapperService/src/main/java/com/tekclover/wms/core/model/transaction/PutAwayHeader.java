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
public class PutAwayHeader {

    protected String languageId;
    protected String companyCodeId;
    protected String plantId;
    protected String warehouseId;
    protected String preInboundNo;
    protected String refDocNumber;
    protected String goodsReceiptNo;
    protected String palletCode;
    protected String caseCode;
    protected String packBarcodes;
    protected Long inboundOrderTypeId;
    protected String putAwayNumber;
    protected String proposedStorageBin;
    protected Double putAwayQuantity;
    protected String putAwayUom;
    protected Long strategyTypeId;
    protected String strategyNo;
    protected String proposedHandlingEquipment;
    protected String assignedUserId;
    protected Long statusId;
    protected String quantityType;
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

    //v2
    protected String companyDescription;
    protected String plantDescription;
    protected String warehouseDescription;
    protected String statusDescription;
    protected String barcodeId;
    private String manufacturerName;
    private String manufacturerCode;
}
