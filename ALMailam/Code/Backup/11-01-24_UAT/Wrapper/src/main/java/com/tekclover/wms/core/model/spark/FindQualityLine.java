package com.tekclover.wms.core.model.spark;


import lombok.Data;

import java.util.List;

@Data
public class FindQualityLine {

    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> plantId;
    private List<String> warehouseId;
    private List<String> refDocNumber;
    private List<String> partnerCode;
    private List<String> qualityInspectionNo;
    private List<Long> statusId;
    private List<String> preOutboundNo;
    private List<Long> lineNumber;
    private List<String> itemCode;
}
