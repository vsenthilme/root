package com.tekclover.wms.api.transaction.model.inbound.inventory.v2;

import com.tekclover.wms.api.transaction.model.inbound.inventory.Inventory;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
public class InventoryV2 extends Inventory {

    @Column(name = "ST_IND")
    private Long stockIndicator;

    @Column(name = "PARTNER_ID", columnDefinition = "nvarchar(100)")
    private String partnerId;

    @Column(name = "GR_DATE")
    private Date grDate;

    @Column(name = "BARCODE_ID", columnDefinition = "nvarchar(255)")
    private String barcodeId;

    @Column(name = "CBM", columnDefinition = "nvarchar(255)")
    private String cbm;

    @Column(name = "CBM_UNIT", columnDefinition = "nvarchar(255)")
    private String cbmUnit;

    @Column(name = "CBM_PER_QTY", columnDefinition = "nvarchar(255)")
    private String cbmPerQuantity;
//    @Id
//    @Column(name = "MFR_CODE", columnDefinition = "nvarchar(255)")
//    private String manufacturerCode;

    @Column(name = "MFR_NAME", columnDefinition = "nvarchar(255)")
    private String manufacturerName;

    @Column(name = "ORIGIN", columnDefinition = "nvarchar(255)")
    private String origin;

    @Column(name = "BRAND", columnDefinition = "nvarchar(255)")
    private String brand;

    @Column(name = "REF_DOC_NO", columnDefinition = "nvarchar(255)")
    private String referenceDocumentNo;

    @Column(name = "C_TEXT", columnDefinition = "nvarchar(255)")
    private String companyDescription;

    @Column(name = "PLANT_TEXT", columnDefinition = "nvarchar(255)")
    private String plantDescription;

    @Column(name = "WH_TEXT", columnDefinition = "nvarchar(255)")
    private String warehouseDescription;

    @Column(name = "STATUS_TEXT", columnDefinition = "nvarchar(150)")
    private String statusDescription;
}