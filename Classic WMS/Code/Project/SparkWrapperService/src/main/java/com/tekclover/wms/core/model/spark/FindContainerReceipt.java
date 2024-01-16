package com.tekclover.wms.core.model.spark;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindContainerReceipt {

    private List<String> warehouseId;
    private List<String> containerReceiptNo;

    private Date startContainerReceivedDate;
    private Date endContainerReceivedDate;

    private List<String> containerNo;

    private List<String> partnerCode;

    // CNT_UL_BY
    private List<String> unloadedBy;
    private List<Long> statusId;

    private Date fromCreatedOn;
    private Date toCreatedOn;
}
