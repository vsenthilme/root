package com.tekclover.wms.core.batch.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PreInboundLine {

    private String languageId;
    private String companyCode;
    private String plantId;
    private String warehouseId;
    private String preInboundNo;
    private String refDocNumber;
    private Long lineNo;
    private String itemCode;
    private Long inboundOrderTypeId;
    private Long variantCode;
    private String variantSubCode;
    private Long statusId;
    private String itemDescription;
    private String containerNo;
    private String invoiceNo;
    private String businessPartnerCode;
    private String partnerItemNo;
    private String brandName;
    private String manufacturerPartNo;
    private String hsnCode;
    private Date expectedArrivalDate;
    private Double orderQty;
    private String orderUom;
    private Long stockTypeId;
    private Long specialStockIndicatorId;
    private String numberOfPallets;
    private String numberOfCases;
    private Double itemPerPalletQty;
    private Double itemCaseQty; // PACK_QTY in AX_API
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
    private String referenceField11;
    private String referenceField12;
    private String referenceField13;
    private String referenceField14;
    private String referenceField15;
    private String referenceField16;
    private String referenceField17;
    private String referenceField18;
    private String referenceField19;
    private String referenceField20;
    private Long deletionIndicator;
    private String createdBy;
    private String dType;

    //v2 fields
    private String manufacturerCode;
    private String manufacturerName;
    private String origin;
    private String companyDescription;
    private String plantDescription;
    private String warehouseDescription;
    private String statusDescription;


    /**
     *
     * @param languageId
     * @param companyCode
     * @param plantId
     * @param warehouseId
     * @param preInboundNo
     * @param refDocNumber
     * @param lineNo
     * @param itemCode
     * @param inboundOrderTypeId
     * @param variantCode
     * @param variantSubCode
     * @param statusId
     * @param itemDescription
     * @param containerNo
     * @param invoiceNo
     * @param businessPartnerCode
     * @param partnerItemNo
     * @param brandName
     * @param manufacturerPartNo
     * @param hsnCode
     * @param expectedArrivalDate
     * @param orderQty
     * @param orderUom
     * @param stockTypeId
     * @param specialStockIndicatorId
     * @param numberOfPallets
     * @param numberOfCases
     * @param itemPerPalletQty
     * @param itemCaseQty
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
     * @param referenceField11
     * @param referenceField12
     * @param referenceField13
     * @param referenceField14
     * @param referenceField15
     * @param referenceField16
     * @param referenceField17
     * @param referenceField18
     * @param referenceField19
     * @param referenceField20
     * @param deletionIndicator
     * @param createdBy
     * @param dType
     * @param manufacturerCode
     * @param manufacturerName
     * @param origin
     * @param companyDescription
     * @param plantDescription
     * @param warehouseDescription
     * @param statusDescription
     */
    public PreInboundLine(String languageId, String companyCode, String plantId, String warehouseId, String preInboundNo, String refDocNumber,
                          Long lineNo, String itemCode, Long inboundOrderTypeId, Long variantCode, String variantSubCode, Long statusId, String itemDescription,
                          String containerNo, String invoiceNo, String businessPartnerCode, String partnerItemNo, String brandName, String manufacturerPartNo,
                          String hsnCode, Date expectedArrivalDate, Double orderQty, String orderUom, Long stockTypeId, Long specialStockIndicatorId,
                          String numberOfPallets, String numberOfCases, Double itemPerPalletQty, Double itemCaseQty, String referenceField1, String referenceField2,
                          String referenceField3, String referenceField4, String referenceField5, String referenceField6, String referenceField7, String referenceField8,
                          String referenceField9, String referenceField10, String referenceField11, String referenceField12, String referenceField13, String referenceField14,
                          String referenceField15, String referenceField16, String referenceField17, String referenceField18, String referenceField19, String referenceField20,
                          Long deletionIndicator, String createdBy, String dType, String manufacturerCode, String manufacturerName, String origin, String companyDescription,
                          String plantDescription, String warehouseDescription, String statusDescription){

        this.languageId = languageId;
        this.companyCode = companyCode;
        this.plantId = plantId;
        this.warehouseId = warehouseId;
        this.preInboundNo = preInboundNo;
        this.refDocNumber = refDocNumber;
        this.lineNo = lineNo;
        this.itemCode = itemCode;
        this.inboundOrderTypeId = inboundOrderTypeId;
        this.variantCode = variantCode;
        this.variantSubCode = variantSubCode;
        this.statusId = statusId;
        this.itemDescription = itemDescription;
        this.containerNo = containerNo;
        this.invoiceNo = invoiceNo;
        this.businessPartnerCode = businessPartnerCode;
        this.partnerItemNo = partnerItemNo;
        this.brandName = brandName;
        this.manufacturerPartNo = manufacturerPartNo;
        this.hsnCode = hsnCode;
        this.expectedArrivalDate = expectedArrivalDate;
        this.orderQty = orderQty;
        this.orderUom = orderUom;
        this.stockTypeId = stockTypeId;
        this.specialStockIndicatorId = specialStockIndicatorId;
        this.numberOfPallets = numberOfPallets;
        this.numberOfCases = numberOfCases;
        this.itemPerPalletQty = itemPerPalletQty;
        this.itemCaseQty = itemCaseQty;
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
        this.referenceField11 = referenceField11;
        this.referenceField12 = referenceField12;
        this.referenceField13 = referenceField13;
        this.referenceField14 = referenceField14;
        this.referenceField15 = referenceField15;
        this.referenceField16 = referenceField16;
        this.referenceField17 = referenceField17;
        this.referenceField18 = referenceField18;
        this.referenceField19 = referenceField19;
        this.referenceField20 = referenceField20;
        this.deletionIndicator = deletionIndicator;
        this.createdBy = createdBy;
        this.dType = dType;
        this.manufacturerCode = manufacturerCode;
        this.manufacturerName = manufacturerName;
        this.origin = origin;
        this.companyDescription = companyDescription;
        this.plantDescription = plantDescription;
        this.warehouseDescription = warehouseDescription;
        this.statusDescription = statusDescription;
    }
}
