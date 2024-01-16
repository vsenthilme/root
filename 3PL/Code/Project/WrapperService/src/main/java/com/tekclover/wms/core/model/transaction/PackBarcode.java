package com.tekclover.wms.core.model.transaction;

import lombok.Data;

@Data
public class PackBarcode {

    private String quantityType;
    private String barcode;

    //	V2
    private Double cbm;
    private Double cbmQuantity;
}
