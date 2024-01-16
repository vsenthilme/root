package com.mnrclara.api.cg.transaction.model.bscontrollinginterest;

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
        name = "tblbscontrollinginterest",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_key_bscontrollinginterest",
                        columnNames = {"LANG_ID", "C_ID", "VALIDATION_ID"})
        }
)
@IdClass(BSControllingInterestCompositeKey.class)
public class BSControllingInterest {

    @Id
    @Column(name = "LANG_ID", columnDefinition = "nvarchar(5)")
    private String languageId;

    @Id
    @Column(name = "C_ID", columnDefinition = "nvarchar(10)")
    private String companyId;

    @Id
    @Column(name = "VALIDATION_ID")
    private Long validationId;


    @Column(name = "STORE_ID", columnDefinition = "nvarchar(10)")
    private String storeId;

    @Column(name = "STORE_NM", columnDefinition = "nvarchar(100)")
    private String storeName;

    @Column(name = "SUB_GRP_ID", columnDefinition = "nvarchar(10)")
    private String subGroupId;

    @Column(name = "SUB_GRP_NM", columnDefinition = "nvarchar(100)")
    private String subGroupName;

    @Column(name = "GRP_ID", columnDefinition = "nvarchar(10)")
    private String groupId;

    @Column(name = "GRP_NM", columnDefinition = "nvarchar(100)")
    private String groupName;

    @Column(name = "CO_OWNER_ID_1")
    private Long coOwnerId1;

    @Column(name = "CO_OWNER_ID_2")
    private Long coOwnerId2;

    @Column(name = "CO_OWNER_ID_3")
    private Long coOwnerId3;

    @Column(name = "CO_OWNER_ID_4")
    private Long coOwnerId4;

    @Column(name = "CO_OWNER_NM_1", columnDefinition = "nvarchar(200)")
    private String coOwnerName1;

    @Column(name = "CO_OWNER_NM_2", columnDefinition = "nvarchar(200)")
    private String coOwnerName2;

    @Column(name = "CO_OWNER_NM_3", columnDefinition = "nvarchar(200)")
    private String coOwnerName3;

    @Column(name = "CO_OWNER_NM_4", columnDefinition = "nvarchar(200)")
    private String coOwnerName4;

    @Column(name = "CO_OWNER_NM_5", columnDefinition = "nvarchar(200)")
    private String coOwnerName5;

    @Column(name = "CO_OWNER_PER_1")
    private Double coOwnerPercentage1;

    @Column(name = "CO_OWNER_PER_2")
    private Double coOwnerPercentage2;

    @Column(name = "CO_OWNER_PER_3")
    private Double coOwnerPercentage3;

    @Column(name = "CO_OWNER_PER_4")
    private Double coOwnerPercentage4;

    @Column(name = "CO_OWNER_PER_5")
    private Double coOwnerPercentage5;

    @Column(name = "CI_PER")
    private Double controlInterestPercentage;

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
