package com.tekclover.wms.core.model.Connector;

import lombok.Data;

import java.util.List;

@Data
public class FindPurchaseReturnHeader {

    private List<Long> purchaseReturnHeaderId;
    private List<String> companyCode;
    private List<String> branchCode;
    private List<String> returnOrderNo;

}
