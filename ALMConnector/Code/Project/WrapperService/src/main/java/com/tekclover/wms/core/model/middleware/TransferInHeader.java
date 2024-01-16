package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;

@Data
public class TransferInHeader {

    private Long transferInHeaderId;

    @NotBlank(message = "SourceCompanyCode is mandatory")
    private String sourceCompanyCode;

    @NotBlank(message = "TargetCompanyCode is mandatory")
    private String targetCompanyCode;

    @NotBlank(message = "TransferOrderNo is mandatory")
    private String transferOrderNumber;

    @NotBlank(message = "SourceBranchCode is mandatory")
    private String sourceBranchCode;

    @NotBlank(message = "TargetBranchCode is mandatory")
    private String targetBranchCode;

    @NotBlank(message = "TransferOrder Date is mandatory")
    private Date transferOrderDate;

    private Set<TransferInLine> transferInLines;
}
