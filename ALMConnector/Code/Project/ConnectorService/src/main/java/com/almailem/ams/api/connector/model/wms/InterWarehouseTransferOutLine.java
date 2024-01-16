package com.almailem.ams.api.connector.model.wms;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class InterWarehouseTransferOutLine {

    @Column(nullable = false)
    @NotNull(message = "Line Reference is mandatory")
    private Long lineReference;                                // IB_LINE_NO

    @Column(nullable = false)
    @NotBlank(message = "SKU is mandatory")
    private String sku;                                    // ITM_CODE

    private String skuDescription;                            // ITEM_TEXT

    @Column(nullable = false)
    @NotNull(message = "Ordered Quantity is mandatory")
    private Double orderedQty;                                // ORD_QTY

    @Column(nullable = false)
    @NotBlank(message = "UOM is mandatory")
    private String uom;                                        // ORD_UOM

    private String orderType;                                // REF_FIELD_1

    @Column(nullable = false)
    @NotBlank(message = "Manufacturer Code is mandatory")
    private String manufacturerCode;

    @Column(nullable = false)
    @NotBlank(message = "Manufacturer Name is mandatory")
    private String manufacturerName;

    private String manufacturerFullName;

    private String brand;

    private String origin;
    private String supplierName;
    private Double packQty;
    private String fromCompanyCode;
    private Double expectedQty;
    protected String storeID;

    private String sourceBranchCode;
    private String countryOfOrigin;
    private String transferOrderNumber;

    //MiddleWare Fields
    private Long middlewareId;
    private Long middlewareHeaderId;
    private String middlewareTable;
}
