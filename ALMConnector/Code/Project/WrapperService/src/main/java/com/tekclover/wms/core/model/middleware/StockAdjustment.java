package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
public class StockAdjustment {

    private Long stockAdjustmentId;

    @NotBlank(message = "Company Code is mandatory")
    private String companyCode;

    @NotBlank(message = "Stock count Branch code is mandatory")
    private String branchCode;

    private String branchName;

    @NotBlank(message = "Date of Adjustment is mandatory")
    private Date dateofAdjustment;

    @NotBlank(message = "Is Damaged is mandatory")
    private String isDamaged;

    @NotBlank(message = "Item Code is mandatory")
    private String itemCode;

    private String itemDescription;

    @NotNull(message = "Adjustment Qty is mandatory")
    private Double adjustmentQty;

    @NotBlank(message = "UOM is mandatory")
    private String uom;

    @NotBlank(message = "Mfr Code is mandatory")
    private String mfrCode;

    private String mfrName;

    private String remarks;
}
