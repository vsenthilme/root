package com.mnrclara.api.cg.transaction.model.bseffectivecontrol;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
/*
 * `VALIDATION_ID`,`COUNTRY_ID`, `LANG_ID`
 */
@Table(
        name = "tblbseffectivecontrol",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_key_bseffectivecontrol",
                        columnNames = {"LANG_ID", "C_ID", "VALIDATION_ID"})
        }
)
@IdClass(BSEffectiveControlCompositeKey.class)
public class BSEffectiveControl {

    @Id
    @Column(name = "LANG_ID", columnDefinition = "nvarchar(5)")
    private String languageId;

    @Id
    @Column(name = "C_ID", columnDefinition = "nvarchar(10)")
    private String companyId;

    @Id
    @Column(name = "VALIDATION_ID")
    private Long validationId;

    @Column(name = "CLIENT_ID")
    private Long clientId;

    @Column(name = "CLIENT_NM", columnDefinition = "nvarchar(100)")
    private String clientName;

    @Column(name = "SUB_GROUP_ID", columnDefinition = "nvarchar(10)")
    private String subGroupId;

    @Column(name = "SUB_GROUP_NM", columnDefinition = "nvarchar(100)")
    private String subGroupName;

    @Column(name = "GROUP_ID", columnDefinition = "nvarchar(10)")
    private String groupId;

    @Column(name = "GROUP_NM", columnDefinition = "nvarchar(100)")
    private String groupName;

    @Column(name = "STORE_ID_1")
    private Long storeId1;

    @Column(name = "STORE_ID_2")
    private Long storeId2;

    @Column(name = "STORE_ID_3")
    private Long storeId3;

    @Column(name = "STORE_ID_4")
    private Long storeId4;

    @Column(name = "STORE_ID_5")
    private Long storeId5;

    @Column(name = "STORE_NM_1", columnDefinition = "nvarchar(200)")
    private String storeName1;

    @Column(name = "STORE_NM_2", columnDefinition = "nvarchar(200)")
    private String storeName2;

    @Column(name = "STORE_NM_3", columnDefinition = "nvarchar(200)")
    private String storeName3;

    @Column(name = "STORE_NM_4", columnDefinition = "nvarchar(200)")
    private String storeName4;

    @Column(name = "STORE_NM_5", columnDefinition = "nvarchar(200)")
    private String storeName5;

    @Column(name = "STORE_PER_1")
    private Double storePercentage1;

    @Column(name = "STORE_PER_2")
    private Double storePercentage2;

    @Column(name = "STORE_PER_3")
    private Double storePercentage3;

    @Column(name = "STORE_PER_4")
    private Double storePercentage4;

    @Column(name = "STORE_PER_5")
    private Double storePercentage5;

    @Column(name = "ECT_PER")
    private Double effectiveControlPercentage;

    @Column(name = "STATUS_ID")
    private Long statusId;

    @Column(name = "IS_DELETED")
    private Long deletionIndicator = 0L;

    @Column(name = "REF_FIELD_1", columnDefinition = "nvarchar(200)")
    private String referenceField1;

    @Column(name = "REF_FIELD_2", columnDefinition = "nvarchar(200)")
    private String referenceField2;

    @Column(name = "REF_FIELD_3", columnDefinition = "nvarchar(200)")
    private String referenceField3;

    @Column(name = "REF_FIELD_4", columnDefinition = "nvarchar(200)")
    private String referenceField4;

    @Column(name = "REF_FIELD_5", columnDefinition = "nvarchar(200)")
    private String referenceField5;

    @Column(name = "REF_FIELD_6", columnDefinition = "nvarchar(200)")
    private String referenceField6;

    @Column(name = "REF_FIELD_7", columnDefinition = "nvarchar(200)")
    private String referenceField7;

    @Column(name = "REF_FIELD_8", columnDefinition = "nvarchar(200)")
    private String referenceField8;

    @Column(name = "REF_FIELD_9", columnDefinition = "nvarchar(200)")
    private String referenceField9;

    @Column(name = "REF_FIELD_10", columnDefinition = "nvarchar(200)")
    private String referenceField10;

    @Column(name = "CTD_BY", columnDefinition = "nvarchar(50)")
    private String createdBy;

    @Column(name = "CTD_ON")
    private Date createdOn = new Date();

    @Column(name = "UTD_BY", columnDefinition = "nvarchar(50)")
    private String updatedBy;

    @Column(name = "UTD_ON")
    private Date updatedOn = new Date();

}
