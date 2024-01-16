package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SupplierInvoiceLine {

    private Long supplierInvoiceLineId;

    private Long supplierInvoiceHeaderId;

    @NotBlank(message = "Branch Code is mandatory")
    private String branchCode;

    @NotBlank(message = "Company Code is mandatory")
    private String companyCode;

    @NotBlank(message = "Supplier Invoice No is mandatory")
    private String supplierInvoiceNumber;

    @NotNull(message = "Line No for each item is mandatory")
    private Long lineNoForEachItem;

    @NotBlank(message = "Item Code is mandatory")
    private String itemCode;

    @NotBlank(message = "Item Description is mandatory")
    private String itemDescription;

    private String containerNumber;

    @NotBlank(message = "Supplier Code is mandatory")
    private String supplierCode;

    private String supplierPartNo;

    @NotBlank(message = "Manufacturer Short Name is mandatory")
    private String manufacturerShortName;

    @NotBlank(message = "Manufacturer Code is mandatory")
    private String manufacturerCode;

    @NotNull(message = "Invoice Date is mandatory")
    private Date invoiceDate;

    @NotNull(message = "Invoice Qty is mandatory")
    private Double invoiceQty;

    @NotBlank(message = "UOM is mandatory")
    private String uom;

    private String supplierName;

    private String manufacturerFullName;
}
