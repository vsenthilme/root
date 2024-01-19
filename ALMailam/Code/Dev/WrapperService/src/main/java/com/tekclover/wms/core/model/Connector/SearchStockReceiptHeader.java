package com.tekclover.wms.core.model.Connector;

import lombok.Data;

import java.util.List;

@Data
public class SearchStockReceiptHeader {

    private List<Long> stockReceiptHeaderId;
    private List<String> companyCode;
    private List<String> branchCode;
    private List<String> receiptNo;

}
