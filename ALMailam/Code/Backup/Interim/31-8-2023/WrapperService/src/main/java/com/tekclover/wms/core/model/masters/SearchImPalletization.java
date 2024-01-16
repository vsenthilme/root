package com.tekclover.wms.core.model.masters;


import lombok.Data;

import java.util.List;

@Data
public class SearchImPalletization {

    private List<String> itemCode;
    private List<String> plantId;
    private List<String> warehouseId;
    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> palletizationLevel;
}
