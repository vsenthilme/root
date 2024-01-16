package com.tekclover.wms.core.model.spark;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchInboundLine {
    private List<String> warehouseId;
    private List<String> companyCode;
    private List<String> plantId;
    private List<String> languageId;
    private List<String> referenceField1;
    private Date startConfirmedOn;
    private Date endConfirmedOn;

    private List<Long> statusId;
}
