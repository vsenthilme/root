package com.tekclover.wms.core.model.Connector;


import lombok.Data;

import java.util.List;

@Data
public class SearchSupplierInvoiceHeader {

    private List<Long> supplierInvoiceHeaderId;

    private List<String> companyCode;

    private List<String> branchCode;


    private List<String> supplierInvoiceNo;




}
