package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PurchaseReturnLine {

    private Long purchaseReturnLineId;

    private Long purchaseReturnHeaderId;

    @NotBlank(message = "ReturnOrder Number is mandatory")
    private String returnOrderNumber;

    @NotNull(message = "Line Number for Each Item is mandatory")
    private Long lineNoForEachItem;

    @NotBlank(message = "Item Code is mandatory")
    private String itemCode;

    private String itemDescription;

    @NotNull(message = "ReturnOrder Quantity is mandatory")
    private Double returnOrderQty;

    @NotBlank(message = "Manufacturer Short Name is mandatory")
    private String manufacturerShortName;

    @NotBlank(message = "Manufacturer Code is mandatory")
    private String manufacturerCode;

    @NotBlank(message = "UOM is mandatory")
    private String uom;

    private String manufacturerFullName;
}
