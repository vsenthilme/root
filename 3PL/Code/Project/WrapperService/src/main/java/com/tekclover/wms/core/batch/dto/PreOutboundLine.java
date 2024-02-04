package com.tekclover.wms.core.batch.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PreOutboundLine {

    private String languageId;
    private String companyCodeId;
    private String plantId;
    private String warehouseId;
    private String refDocNumber;
    private String preOutboundNo;
    private String partnerCode;
    private Long lineNumber;
    private String itemCode;
    private Long outboundOrderTypeId;
    private Long variantCode;
    private String variantSubCode;
    private Long statusId;
    private Long stockTypeId;
    private Long specialStockIndicatorId;
    private String description;
    private String manufacturerPartNo;
    private String hsnCode;
    private String itemBarcode;
    private Double orderQty;
    private String orderUom;
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
    private String createdBy;
    private String dType;
    private String manufacturerCode;
    private String manufacturerName;
    private String origin;
    private String brand;
    private String companyDescription;
    private String plantDescription;
    private String warehouseDescription;
    private String statusDescription;


    /**
     *
     * @param languageId
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param refDocNumber
     * @param preOutboundNo
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param outboundOrderTypeId
     * @param variantCode
     * @param variantSubCode
     * @param statusId
     * @param stockTypeId
     * @param specialStockIndicatorId
     * @param description
     * @param manufacturerPartNo
     * @param hsnCode
     * @param itemBarcode
     * @param orderQty
     * @param orderUom
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
     * @param createdBy
     * @param manufacturerCode
     * @param manufacturerName
     * @param origin
     * @param brand
     * @param companyDescription
     * @param plantDescription
     * @param warehouseDescription
     * @param statusDescription
     */

    public PreOutboundLine(String languageId, String companyCodeId, String plantId, String warehouseId, String refDocNumber,
                           String preOutboundNo, String partnerCode, Long lineNumber, String itemCode, Long outboundOrderTypeId,
                           Long variantCode, String variantSubCode, Long statusId, Long stockTypeId, Long specialStockIndicatorId,
                           String description, String manufacturerPartNo, String hsnCode, String itemBarcode, Double orderQty,
                           String orderUom, Date requiredDeliveryDate, String referenceField1, String referenceField2, String referenceField3,
                           String referenceField4, String referenceField5, String referenceField6, String referenceField7, String referenceField8,
                           String referenceField9, String referenceField10, Long deletionIndicator, String createdBy, String dType, String manufacturerCode,
                           String manufacturerName, String origin, String brand, String companyDescription, String plantDescription,
                           String warehouseDescription, String statusDescription){

        this.languageId = languageId;
        this.companyCodeId = companyCodeId;
        this.plantId = plantId;
        this.warehouseId = warehouseId;
        this.refDocNumber = refDocNumber;
        this.preOutboundNo = preOutboundNo;
        this.partnerCode = partnerCode;
        this.lineNumber = lineNumber;
        this.itemCode = itemCode;
        this.outboundOrderTypeId = outboundOrderTypeId;
        this.variantCode = variantCode;
        this.variantSubCode = variantSubCode;
        this.statusId = statusId;
        this.stockTypeId = stockTypeId;
        this.specialStockIndicatorId = specialStockIndicatorId;
        this.description = description;
        this.manufacturerPartNo = manufacturerPartNo;
        this.hsnCode = hsnCode;
        this.itemBarcode = itemBarcode;
        this.orderQty = orderQty;
        this.orderUom = orderUom;
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
        this.createdBy = createdBy;
        this.dType = dType;
        this.manufacturerCode = manufacturerCode;
        this.manufacturerName = manufacturerName;
        this.origin = origin;
        this.brand = brand;
        this.companyDescription = companyDescription;
        this.plantDescription = plantDescription;
        this.warehouseDescription = warehouseDescription;
        this.statusDescription = statusDescription;

    }
}
