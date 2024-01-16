package com.tekclover.wms.core.model.spark;

import lombok.Data;

@Data
public class FindInventoryMovement {
    private String languageId;
    private String companyCodeId;
    private String plantId;
    private String warehouseId;
}
