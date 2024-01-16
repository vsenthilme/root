package com.mnrclara.spark.core.model.wmscore;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchPerpetualHeader {

    private List<String> warehouseId;
    private List<Long> cycleCountTypeId;
    private List<String> cycleCountNo;
    private List<Long> movementTypeId;
    private List<Long> subMovementTypeId;
    private List<Long> headerStatusId;
    private List<Long> lineStatusId;
    private List<String> createdBy;

    private Date startCreatedOn;
    private Date endCreatedOn;
}
