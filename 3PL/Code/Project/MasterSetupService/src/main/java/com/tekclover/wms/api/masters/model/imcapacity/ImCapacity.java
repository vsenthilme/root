package com.tekclover.wms.api.masters.model.imcapacity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tblimcapacity",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_key_imcapacity",
                        columnNames = {"LANG_ID", "C_ID", "PLANT_ID", "WH_ID", "ITM_CODE"})
        }
)
@IdClass(ImCapacityCompositeKey.class)
public class ImCapacity {

    @Id
    @Column(name="LANG_ID")
    private String languageId;

    @Id
    @Column(name = "C_ID")
    private String companyCodeId;

    @Id
    @Column(name = "PLANT_ID")
    private String plantId;

    @Id
    @Column(name = "WH_ID")
    private String warehouseId;

    @Id
    @Column(name = "ITM_CODE")
    private String itemCode;

    @Column(name="ST_BIN_TYP_ID")
    private Long storageBinTypeId;

    @Column(name="UOM_ID")
    private String uomId;

    @Column(name="CAPACITY")
    private String storageCapacity;

    @Column(name="STATUS_ID")
    private String statusId;

    @Column(name = "REF_FIELD_1")
    private String referenceField1;

    @Column(name = "REF_FIELD_2")
    private String referenceField2;

    @Column(name = "REF_FIELD_3")
    private String referenceField3;

    @Column(name = "REF_FIELD_4")
    private String referenceField4;

    @Column(name = "REF_FIELD_5")
    private String referenceField5;

    @Column(name = "REF_FIELD_6")
    private String referenceField6;

    @Column(name = "REF_FIELD_7")
    private String referenceField7;

    @Column(name = "REF_FIELD_8")
    private String referenceField8;

    @Column(name = "REF_FIELD_9")
    private String referenceField9;

    @Column(name = "REF_FIELD_10")
    private String referenceField10;

    @Column(name = "IS_DELETED")
    private Long deletionIndicator;

    @Column(name = "CTD_BY")
    private String createdBy;

    @Column(name = "CTD_ON")
    private Date createdOn = new Date();

    @Column(name = "UTD_BY")
    private String updatedBy;

    @Column(name = "UTD_ON")
    private Date updatedOn = new Date();
}
