package com.tekclover.wms.api.transaction.model.inbound.inventory;

import java.util.Date;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
/*
 * `LANG_ID`, `C_ID`, `PLANT_ID`, `WH_ID`, `PACK_BARCODE`, `ITM_CODE`, `ST_BIN`, `STCK_TYP_ID`, `SP_ST_IND_ID`
 */
@Table(
        name = "tblinventory",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_key_inventory",
                        columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "PACK_BARCODE", "ITM_CODE", "ST_BIN", "SP_ST_IND_ID", "MFR_CODE"})
        }
)
@IdClass(InventoryCompositeKey.class)
public class Inventory {

    @Id
    @Column(name = "LANG_ID")
    protected String languageId;

    @Id
    @Column(name = "C_ID")
    protected String companyCodeId;

    @Id
    @Column(name = "PLANT_ID")
    protected String plantId;

    @Id
    @Column(name = "WH_ID")
    protected String warehouseId;

    @Column(name = "PAL_CODE")
    protected String palletCode;

    @Column(name = "CASE_CODE")
    protected String caseCode;

    @Id
    @Column(name = "PACK_BARCODE")
    protected String packBarcodes;

    @Id
    @Column(name = "ITM_CODE")
    protected String itemCode;

    @Column(name = "VAR_ID")
    protected Long variantCode;

    @Column(name = "VAR_SUB_ID")
    protected String variantSubCode;

    @Column(name = "STR_NO")
    protected String batchSerialNumber;

    @Id
    @Column(name = "ST_BIN")
    protected String storageBin;

    //	@Id
    @Column(name = "STCK_TYP_ID")
    protected Long stockTypeId;

    @Id
    @Column(name = "SP_ST_IND_ID")
    protected Long specialStockIndicatorId;

    @Column(name = "REF_ORD_NO")
    protected String referenceOrderNo;

    @Column(name = "STR_MTD")
    protected String storageMethod;

    @Column(name = "BIN_CL_ID")
    protected Long binClassId;

    @Column(name = "TEXT")
    protected String description;

    @Column(name = "INV_QTY")
    protected Double inventoryQuantity;

    @Column(name = "ALLOC_QTY")
    protected Double allocatedQuantity;

    @Column(name = "INV_UOM")
    protected String inventoryUom;

    @Column(name = "MFR_DATE")
    protected Date manufacturerDate;

    @Column(name = "EXP_DATE")
    protected Date expiryDate;

    @Column(name = "IS_DELETED")
    protected Long deletionIndicator;

    @Column(name = "REF_FIELD_1")
    protected String referenceField1;

    @Column(name = "REF_FIELD_2")
    protected String referenceField2;

    @Column(name = "REF_FIELD_3")
    protected String referenceField3;

    @Column(name = "REF_FIELD_4")
    protected Double referenceField4;

    @Column(name = "REF_FIELD_5")
    protected String referenceField5;

    @Column(name = "REF_FIELD_6")
    protected String referenceField6;

    @Column(name = "REF_FIELD_7")
    protected String referenceField7;

    @Column(name = "REF_FIELD_8")
    protected String referenceField8;

    @Column(name = "REF_FIELD_9")
    protected String referenceField9;

    @Column(name = "REF_FIELD_10")
    protected String referenceField10;

    @Column(name = "IU_CTD_BY")
    protected String createdBy;

    @Column(name = "IU_CTD_ON")
    protected Date createdOn = new Date();

    @Column(name = "UTD_BY")
    protected String updatedBy;

    @Column(name = "UTD_ON")
    protected Date updatedOn;

    //V2 field
    @Id
    @Column(name = "MFR_CODE", columnDefinition = "nvarchar(255)")
    protected String manufacturerCode;
}
