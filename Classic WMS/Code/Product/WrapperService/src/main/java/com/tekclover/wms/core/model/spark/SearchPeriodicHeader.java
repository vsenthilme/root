package com.tekclover.wms.core.model.spark;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchPeriodicHeader {
    private List<String> warehouseId;
    private List<String> companyCodeId;
    private List<String> plantId;
    private List<String> languageId;
    private List<Long> cycleCountTypeId;
    private List<String> cycleCountNo;
    private List<Long> headerStatusId;
    private List<Long> lineStatusId;
    private List<String> createdBy;

    private Date startCreatedOn;
    private Date endCreatedOn;

    // From Line table
    private List<String> cycleCounterId;
}
