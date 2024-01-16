package com.tekclover.wms.api.transaction.model.warehouse.outbound.v2;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SalesInvoice {

    @NotBlank(message = "Company Code is mandatory")
    private String companyCode;

    @NotBlank(message = "Sales Invoice Number is mandatory")
    private String salesInvoiceNumber;

    @NotBlank(message = "Branch Code is mandatory")
    private String branchCode;

    @NotBlank(message = "Sales Order Number is mandatory")
    private String salesOrderNumber;

    @NotBlank(message = "Pick List Number is mandatory")
    private String pickListNumber;

    @NotBlank(message = "Invoice Date is mandatory")
    private String invoiceDate;

    private String deliveryType;
    private String customerId;
    private String customerName;
    private String address;
    private String phoneNumber;
    private String alternateNo;
    private String status;

    private String warehouseID;

    private String languageId;
    private Long middlewareId;
    private String middlewareTable;
}
