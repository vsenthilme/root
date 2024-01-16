package com.tekclover.wms.core.model.spark;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindInboundHeader {
    private List<String> warehouseId;
    private List<String> refDocNumber;
    private List<Long> inboundOrderTypeId;
    private List<String> containerNo;
    private List<Long> statusId;

    private Date startCreatedOn;
    private Date endCreatedOn;
    private Date startConfirmedOn;
    private Date endConfirmedOn;
}
