package com.tekclover.wms.api.transaction.model.deliveryheader;

import lombok.Data;

import java.util.List;

@Data
public class SearchDeliveryHeader {

    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> plantId;
    private List<String> warehouseId;
    private List<Long> deliveryNo;
    private List<Long> statusId;
    private List<String> remarks;
    private List<String> refDocNo;
}
