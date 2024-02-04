package com.tekclover.wms.core.model.Connector;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchTransferInHeader {

    private List<Long> transferInHeaderId;

    private List<String> sourceCompanyCode;

    private List<String> targetCompanyCode;

    private List<String> transferOrderNo;

    private List<String> sourceBranchCode;

    private List<String> targetBranchCode;

    private Date fromTransferOrderDate;

    private Date toTransferOrderDate;

}
