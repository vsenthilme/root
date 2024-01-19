package com.tekclover.wms.core.model.Connector;

import lombok.Data;

import java.util.List;

@Data
public class FindCustomerMaster {

    private List<Long> customerMasterId;
    private List<String> companyCode;
    private List<String> branchCode;
    private List<String> customerCode;

}
