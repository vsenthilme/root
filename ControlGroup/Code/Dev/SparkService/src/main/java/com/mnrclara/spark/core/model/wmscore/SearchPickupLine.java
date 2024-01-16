package com.mnrclara.spark.core.model.wmscore;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class SearchPickupLine {
    private List<String> warehouseId;
    private List<String> companyCodeId;
    private List<String> plantId;
    private List<String> languageId;
    private List<String> preOutboundNo;
    private List<String> refDocNumber;
    private List<String> partnerCode;
    private List<Long> lineNumber;
    private List<String> pickupNumber;
    private List<String> itemCode;
    private List<String> actualHeNo;
    private List<String> pickedStorageBin;
    private List<String> pickedPackCode;

    private List<Long> statusId;
    private Date fromPickConfirmedOn;
    private Date toPickConfirmedOn;
}
