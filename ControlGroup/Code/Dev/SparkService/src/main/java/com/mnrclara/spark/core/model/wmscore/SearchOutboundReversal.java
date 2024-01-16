package com.mnrclara.spark.core.model.wmscore;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchOutboundReversal {
    private List<String> outboundReversalNo;
    private List<String> companyCodeId;
    private List<String> plantId;
    private List<String> languageId;
    private List<String> warehouseId;
    private List<String> reversalType;
    private List<String> refDocNumber;
    private List<String> partnerCode;
    private List<String> itemCode;
    private List<String> packBarcode;
    private List<Long> statusId;
    private List<String> reversedBy;

    private Date startReversedOn;
    private Date endReversedOn;
}
