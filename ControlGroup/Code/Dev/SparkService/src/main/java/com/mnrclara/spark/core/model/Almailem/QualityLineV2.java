    package com.mnrclara.spark.core.model.Almailem;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class QualityLineV2 {
    private String languageId;
    private String companyCodeId;
    private String plantId;
    private String warehouseId;
    private String preOutboundNo;
    private String refDocNumber;
    private String partnerCode;
    private Long lineNumber;
    private String qualityInspectionNo;
    private String itemCode;
    private String actualHeNo;
    private String pickPackBarCode;
    private Long outboundOrderTypeId;
    private Long statusId;
    private Long stockTypeId;
    private Long specialStockIndicatorId;
    private String description;
    private String manufacturerPartNo;
    private String packingMaterialNo;
    private Long variantCode;
    private String variantSubCode;
    private String batchSerialNumber;
    private Double qualityQty;
    private Double pickConfirmQty;
    private String qualityConfirmUom;
    private String rejectQty;
    private String rejectUom;
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
    private String qualityCreatedBy;
    private Timestamp qualityCreatedOn ;
    private String qualityConfirmedBy;
    private Timestamp qualityConfirmedOn;
    private String qualityUpdatedBy;
    private Timestamp qualityUpdatedOn ;
    private String qualityReversedBy;
    private Timestamp qualityReversedOn ;

    private String companyDescription;
    private String plantDescription;
    private String warehouseDescription;
    private String statusDescription;
    private String barcodeId;
    private Long middlewareId;
    private Long middlewareHeaderId;
    private String middlewareTable;
    private String referenceDocumentType;
    private String salesInvoiceNumber;
    private String supplierInvoiceNo;
    private String salesOrderNumber;
    private String manufacturerFullName;
    private String pickListNumber;
    private String tokenNumber;
    private String manufacturerName;
}