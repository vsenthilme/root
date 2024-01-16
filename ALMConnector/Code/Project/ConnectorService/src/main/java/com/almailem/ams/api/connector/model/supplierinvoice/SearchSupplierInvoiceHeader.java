package com.almailem.ams.api.connector.model.supplierinvoice;


import lombok.Data;

import java.util.List;

@Data
public class SearchSupplierInvoiceHeader {

    private List<Long> supplierInvoiceHeaderId;

    private List<String> companyCode;

    private List<String> branchCode;


    private List<String> supplierInvoiceNo;




}
