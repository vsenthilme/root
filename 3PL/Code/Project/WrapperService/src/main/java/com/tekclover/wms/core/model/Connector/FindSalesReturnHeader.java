package com.tekclover.wms.core.model.Connector;

import lombok.Data;

import java.util.List;

@Data
public class FindSalesReturnHeader {

    private List<Long> salesReturnHeaderId;

    private List<String> companyCode;

    private List<String> branchCodeOfReceivingWarehouse;

    private List<String> returnOrderNo;

}
