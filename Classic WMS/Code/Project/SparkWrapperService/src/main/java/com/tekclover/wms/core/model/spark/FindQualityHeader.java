package com.tekclover.wms.core.model.spark;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindQualityHeader {

    private List<String> refDocNumber;
    private List<String> partnerCode;
    private List<String> qualityInspectionNo;
    private List<String> actualHeNo;
    private List<Long> outboundOrderTypeId;
    private List<Long> statusId;
    private List<String> soType; //referenceField1;
    private Date startQualityCreatedOn;
    private Date endQualityCreatedOn;

    private List<String> warehouseId;
}
