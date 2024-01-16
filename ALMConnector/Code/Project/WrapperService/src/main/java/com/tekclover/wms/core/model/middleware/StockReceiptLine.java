package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class StockReceiptLine {

    private Long stockReceiptLineId;

    private Long stockReceiptHeaderId;

    @NotBlank(message = "BranchCode is mandatory")
    private String branchCode;

    @NotBlank(message = "CompanyCode is mandatory")
    private String companyCode;

    @NotBlank(message = "Receipt Number is mandatory")
    private String receiptNumber;

    @NotNull(message = "Line Number for Each Item is mandatory")
    private Long lineNoForEachItem;

    @NotBlank(message = "Item Code is mandatory")
    private String itemCode;

    @NotBlank(message = "Item Description is mandatory")
    private String itemDescription;

    private String supplierCode;

    private String supplierPartNo;

    @NotBlank(message = "Manufacturer Short Name is mandatory")
    private String manufacturerShortName;

    @NotBlank(message = "Manufacturer Code is mandatory")
    private String manufacturerCode;

    @NotBlank(message = "Receipt Date is mandatory")
    private String receiptDate;

    @NotNull(message = "Receipt Quantity is mandatory")
    private Double receiptQty;

    @NotBlank(message = "UOM is mandatory")
    private String uom;

    private String supplierName;

    private String manufacturerFullName;
}
