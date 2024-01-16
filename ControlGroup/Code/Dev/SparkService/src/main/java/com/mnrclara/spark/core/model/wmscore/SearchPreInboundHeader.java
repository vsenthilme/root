package com.mnrclara.spark.core.model.wmscore;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchPreInboundHeader {
    private List<String> warehouseId;
    private List<String> companyCode;
    private List<String> languageId;
    private List<String> plantId;
    private List<String> preInboundNo;
    private List<Long> inboundOrderTypeId;
    private List<String> refDocNumber;
    private List<Long> statusId;

    private Date startRefDocDate;
    private Date endRefDocDate;

    private Date startCreatedOn;
    private Date endCreatedOn;
}
