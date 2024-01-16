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
public class InboundLine {

    protected String languageId;
    protected String companyCodeId;
    protected String plantId;
    protected String warehouseId;
    protected String refDocNumber;
    protected String preInboundNo;
    protected Long lineNo;
    protected String itemCode;
    protected Double orderQty;
    protected String orderUom;
    protected Double acceptedQty;
    protected Double damageQty;
    protected Double putawayConfirmedQty;
    protected Double varianceQty;
    protected Long variantCode;
    protected String variantSubCode;
    protected Long inboundOrderTypeId;
    protected Long stockTypeId;
    protected Long specialStockIndicatorId;
    protected String referenceOrderNo;
    protected Long statusId;
    protected String vendorCode;
    protected Date expectedArrivalDate;
    protected String containerNo;
    protected String invoiceNo;
    protected String description;
    protected String manufacturerPartNo;
    protected String hsnCode;
    protected String itemBarcode;
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
    protected Date confirmedBy;
    protected Date confirmedOn = new Date();

}
