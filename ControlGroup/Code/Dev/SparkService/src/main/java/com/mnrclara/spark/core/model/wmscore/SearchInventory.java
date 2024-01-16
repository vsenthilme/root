package com.mnrclara.spark.core.model.wmscore;

import lombok.Data;

import java.util.List;

@Data
public class SearchInventory {

    private List<String> warehouseId;
    private List<String> companyCodeId;
    private List<String> plantId;
    private List<String> languageId;
    private List<String> packBarcodes;
    private List<String> itemCode;
    private List<String> storageBin;
    private List<String> storageSectionId;
    private List<Long> stockTypeId;
    private List<Long> specialStockIndicatorId;
    private List<Long> binClassId;
    private List<String> description;
}
