package com.tekclover.wms.core.model.spark;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindStorageBin {
    private List<String> warehouseId;
    private List<String> storageBin;
    private List<Long> floorId;
    private List<String> storageSectionId;
    private List<String> rowId;
    private List<String> aisleNumber;
    private List<String>languageId;
    private List<String> companyCodeId;
    private List<String> plantId;
    private List<String> spanId;
    private List<String> shelfId;
    private List<String> createdBy;
    private List<String> updatedBy;
    private List<Long> statusId;

    private Date startCreatedOn;
    private Date endCreatedOn;

    private Date startUpdatedOn;
    private Date endUpdatedOn;
}
