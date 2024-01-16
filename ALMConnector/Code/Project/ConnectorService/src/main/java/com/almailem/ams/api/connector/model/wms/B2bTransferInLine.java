package com.almailem.ams.api.connector.model.wms;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class B2bTransferInLine {

    @Column(nullable = false)
    @NotNull(message = "Line Reference is mandatory")
    private Long lineReference;

    @Column(nullable = false)
    @NotBlank(message = "sku is mandatory")
    private String sku;

    @Column(nullable = false)
    @NotBlank(message = "sku Description is mandatory")
    private String skuDescription;

    @Column(nullable = false)
    @NotBlank(message = "Store Id is mandatory")
    private String storeID;

    private String supplierPartNumber;
    private String manufacturerName;

    @Column(nullable = false)
    @NotBlank(message = "Excepted Date is mandatory")
    private String expectedDate;

    @Column(nullable = false)
    @NotNull(message = "Expected Qty is mandatory")
    private Double expectedQty;

    @Column(nullable = false)
    @NotBlank(message = "uom is mandatory")
    private String uom;

    private Long packQty;

    @Column(nullable = false)
    @NotBlank(message = "Origin is mandatory")
    private String origin;

    @Column(nullable = false)
    @NotBlank(message = "Manufacturer Code is mandatory")
    private String manufacturerCode;

    private String manufacturerFullName;
    private String brand;
    private String supplierName;
    private String transferOrderNo;
    private String isCompleted;

    //MiddleWare Fields
    private Long middlewareId;
    private Long middlewareHeaderId;
    private String middlewareTable;
}
