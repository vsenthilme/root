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
public class GrHeader {

    protected String languageId;
    protected String companyCode;
    protected String plantId;
    protected String warehouseId;
    protected String preInboundNo;
    protected String refDocNumber;
    protected String stagingNo;
    protected String goodsReceiptNo;
    protected String palletCode;
    protected String caseCode;
    protected Long inboundOrderTypeId;
    protected Long statusId;
    protected String grMethod;
    protected String containerReceiptNo;
    protected String dockAllocationNo;
    protected String containerNo;
    protected String vechicleNo;
    protected Date expectedArrivalDate;
    protected Date goodsReceiptDate;
    protected Long deletionIndicator = 0L;
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
    protected String createdBy;
    protected Date createdOn = new Date();
    protected String updatedBy;
    protected Date updatedOn = new Date();
    protected String confirmedBy;
    protected Date confirmedOn = new Date();
}
