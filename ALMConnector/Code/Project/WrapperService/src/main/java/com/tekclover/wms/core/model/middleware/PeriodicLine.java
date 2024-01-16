package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PeriodicLine {

    private Long periodicLineId;

    private Long periodicHeaderId;

    @NotBlank(message = "CycleCount Number is mandatory")
    private String cycleCountNumber;

    @NotNull(message = "line Number Of Each Item Code is mandatory")
    private Long lineNoOfEachItemCode;

    @NotBlank(message = "itemCode is mandatory")
    private String itemCode;

    private String itemDescription;

    @NotBlank(message = "Unit Of Measure is mandatory")
    private String uom;

    @NotBlank(message = "Manufacturer Code is mandatory")
    private String manufacturerCode;

    @NotBlank(message = "Manufacturer Name is mandatory")
    private String manufacturerName;
}
