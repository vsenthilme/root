package com.mnrclara.spark.core.model.wmscore;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchOutboundHeader {
    private List<String> warehouseId;
    private List<String> companyCodeId;
    private List<String> languageId;
    private List<String> plantId;
    private List<String> refDocNumber;
    private List<String> partnerCode;
    private List<Long> outboundOrderTypeId;
    private List<Long> statusId;
    private List<String> soType; //referenceField1;

    private Date startRequiredDeliveryDate;
    private Date endRequiredDeliveryDate;

    private Date startDeliveryConfirmedOn;
    private Date endDeliveryConfirmedOn;

    private Date startOrderDate;
    private Date endOrderDate;

}
