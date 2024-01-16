package com.mnrclara.wrapper.core.model.spark;


import lombok.Data;

@Data
public class FindInventory {
    private String languageId;
    private String companyCodeId;
    private String plantId;
    private String warehouseId;
    private String palletCode;
    private String caseCode;
}
