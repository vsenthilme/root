package com.mnrclara.spark.core.model.Almailem;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindPreInboundHeaderV2 {

    private List<String> warehouseId;
    private List<String> preInboundNo;
    private List<Long> inboundOrderTypeId;
    private List<String> refDocNumber;
    private List<Long> statusId;

    private Date startRefDocDate;
    private Date endRefDocDate;

    private Date startCreatedOn;
    private Date endCreatedOn;

    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> plantId;
}
