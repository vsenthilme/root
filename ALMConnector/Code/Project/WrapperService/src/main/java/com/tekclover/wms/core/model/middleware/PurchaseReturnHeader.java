package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;

@Data
public class PurchaseReturnHeader {

    private Long purchaseReturnHeaderId;

    @NotBlank(message = "CompanyCode is mandatory")
    private String companyCode;

    @NotBlank(message = "BranchCode is mandatory")
    private String branchCode;

    @NotBlank(message = "ReturnOrderNumber is mandatory")
    private String returnOrderNumber;

    @NotBlank(message = "ReturnOrder Date is mandatory")
    private Date returnOrderDate;

    private String supplierInvoiceNumber;

    private Set<PurchaseReturnLine> purchaseReturnLines;
}
