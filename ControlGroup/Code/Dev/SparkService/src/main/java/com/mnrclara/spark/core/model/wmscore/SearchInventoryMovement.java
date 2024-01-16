package com.mnrclara.spark.core.model.wmscore;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchInventoryMovement {

    private List<String> warehouseId;
    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> plantId;
    private List<String> itemCode;
    private List<Long> movementType;
    private List<Long> submovementType;
    private List<String> packBarcodes;
    private List<String> batchSerialNumber;
    private List<String> movementDocumentNo;
    private Date fromCreatedOn;
    private Date toCreatedOn;

}
