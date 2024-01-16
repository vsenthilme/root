package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PickListLine {

    private Long pickListLineId;

    private Long pickListHeaderId;

    @NotBlank(message = "SalesOrderNumber is mandatory")
    private String salesOrderNumber;

    @NotBlank(message = "PickListNumber is mandatory")
    private String pickListNumber;

    @NotBlank(message = "Item Code is mandatory")
    private String itemCode;

    @NotBlank(message = "Item Description is mandatory")
    private String itemDescription;

    @NotNull(message = "PickList Quantity is mandatory")
    private Double pickListQty;

    @NotBlank(message = "Manufacturer Short Name is mandatory")
    private String manufacturerShortName;

    @NotBlank(message = "Manufacturer Code is mandatory")
    private String manufacturerCode;

    @NotBlank(message = "UOM is mandatory")
    private String uom;

    private String manufacturerFullName;
}
