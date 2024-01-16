package com.tekclover.wms.api.transaction.model.deliverymodule.deliveryheader;

import lombok.Data;

import java.util.List;

@Data
public class SearchDeliveryHeader {

    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> plantId;
    private List<String> warehouseId;
    private List<String> deliveryNo;
}
