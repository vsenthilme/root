package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class SupplierInvoiceHeader {


    private Long supplierInvoiceHeaderId;

    @NotBlank(message = "Branch Code is mandatory")
    private String branchCode;

    @NotBlank(message = "Company Code is mandatory")
    private String companyCode;

    @NotBlank(message = "Purchase Order No is mandatory")
    private String purchaseOrderNumber;

    @NotBlank(message = "Supplier Invoice No is mandatory")
    private String supplierInvoiceNumber;

    private Set<SupplierInvoiceLine> supplierInvoiceLine;

}
