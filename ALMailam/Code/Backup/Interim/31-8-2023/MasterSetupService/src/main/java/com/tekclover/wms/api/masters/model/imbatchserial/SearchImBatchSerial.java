package com.tekclover.wms.api.masters.model.imbatchserial;

import lombok.Data;

import java.util.List;

@Data
public class SearchImBatchSerial {

    private List<String> companyCodeId;
    private List<String>plantId;
    private List<String>languageId;
    private List<String>warehouseId;
    private List<String> itemCode;
    private List<String>storageMethod;
}
