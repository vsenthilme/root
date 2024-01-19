package com.tekclover.wms.core.model.Connector;

import lombok.Data;

import java.util.List;

@Data
public class FindPerpetualHeader {

    private List<Long> perpetualHeaderId;
    private List<String> companyCode;
    private List<String> branchCode;
    private List<String> cycleCountNo;

}
