package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class StockReceiptHeader {

    private Long stockReceiptHeaderId;

    @NotBlank(message = "BranchCode is mandatory")
    private String branchCode;

    @NotBlank(message = "CompanyCode is mandatory")
    private String companyCode;

    @NotBlank(message = "Receipt Number is mandatory")
    private String receiptNumber;

    private Set<StockReceiptLine> stockReceiptLines;
}
