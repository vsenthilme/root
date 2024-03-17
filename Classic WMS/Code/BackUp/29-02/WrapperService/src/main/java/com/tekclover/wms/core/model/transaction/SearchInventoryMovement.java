package com.tekclover.wms.core.model.transaction;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchInventoryMovement {
    /*
     * WH_ID
     * MVT_TYP_ID
     * SUB_MVT_TYP_ID
     * PACK_BARCODE
     * ITM_CODE
     * STR_NO
     * MVT_DOC_NO
     */

    private List<String> languageId;
    private List<String> companyCodeId;
    private List<String> plantId;
    private List<String> warehouseId;
    private List<Long> movementType;
    private List<Long> submovementType;
    private List<String> packBarcodes;
    private List<String> itemCode;
    private List<String> batchSerialNumber;
    private List<String> movementDocumentNo;

    private Date fromCreatedOn;
    private Date toCreatedOn;
}
