package com.tekclover.wms.core.model.middleware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class SalesInvoice {

    private Long salesInvoiceId;

    @NotBlank(message = "CompanyCode is mandatory")
    private String companyCode;

    @NotBlank(message = "BranchCode is mandatory")
    private String branchCode;

    @NotBlank(message = "SalesInvoice Number is mandatory")
    private String salesInvoiceNumber;

    @NotBlank(message = "Delivery Type is mandatory")
    private String deliveryType;

    @NotBlank(message = "SalesOrderNumber is mandatory")
    private String salesOrderNumber;

    @NotBlank(message = "PickListNumber is mandatory")
    private String pickListNumber;

    @NotBlank(message = "Invoice Date is mandatory")
    private Date invoiceDate;

    @NotBlank(message = "CustomerId is mandatory")
    private String customerId;

    @NotBlank(message = "Customer Name is mandatory")
    private String customerName;

    @NotBlank(message = "Address is mandatory")
    private String address;

    @NotBlank(message = "Phone Number is mandatory")
    private String phoneNumber;

    @NotBlank(message = "Alternate Number is mandatory")
    private String alternateNumber;

    private String status;
}
