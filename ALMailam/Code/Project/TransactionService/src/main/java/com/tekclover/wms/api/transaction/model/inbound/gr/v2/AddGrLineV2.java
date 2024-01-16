package com.tekclover.wms.api.transaction.model.inbound.gr.v2;

import com.tekclover.wms.api.transaction.model.inbound.gr.AddGrLine;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AddGrLineV2 extends AddGrLine {

    private Double inventoryQuantity;
    private String barcodeId;
    private Double cbm;
    private String cbmUnit;
    private String manufacturerCode;
    private String manufacturerName;
    private String origin;
    private String brand;
    private String rejectType;
    private String rejectReason;
    private Double cbmQuantity;
    private String companyDescription;
    private String plantDescription;
    private String warehouseDescription;
    private String interimStorageBin;
}
