package com.mnrclara.spark.core.model.wmscore;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindStagingHeaderV2 {

    private List<Long> inboundOrderTypeId;
    private List<String> stagingNo;
    private List<String> preInboundNo;
    private List<String> companyCode;
    private List<String> plantId;
    private List<String> languageId;
    private List<String> refDocNumber;
    private List<Long> statusId;
    private List<String> warehouseId;
    private List<String> createdBy;

    private Date startCreatedOn;
    private Date endCreatedOn;
}
