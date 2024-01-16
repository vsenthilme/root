package com.mnrclara.spark.core.model.wmscore;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchPreOutboundHeader {
    private List<String> warehouseId;
    private List<String> companyCodeId;
    private List<String> plantId;
    private List<String> languageId;
    private List<String> preOutboundNo;
    private List<Long> outboundOrderTypeId;
    private List<String> soType; 			// SO type - Ref.Field1
    private List<String> soNumber; 			// refDocNumber - SO Number
    private List<String> partnerCode;
    private List<Long> statusId;
    private List<String> createdBy;

    private Date startRequiredDeliveryDate;
    private Date endRequiredDeliveryDate;
    private Date startOrderDate;
    private Date endOrderDate;
    private Date startCreatedOn;
    private Date endCreatedOn;
}
