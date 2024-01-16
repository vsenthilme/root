package com.tekclover.wms.api.billing.model.invoiceline;

import lombok.Data;
import java.util.List;

@Data
public class FindInvoiceLine {
    private String companyCodeId;
    private String plantId;
    private String warehouseId;
    private List<String> invoiceNumber;
    private List<String> partnerCode;
    private List<Long> lineNumber;
    private List<Long> statusId;
}
