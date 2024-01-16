package com.mnrclara.api.cg.transaction.model.storepartnerlisting;


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
 * `LANG_ID`,`C_ID`, `STORE_ID`,'VERSION_NO'
 */
@Table(
        name = "tblstorepartnerlisting",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_key_storepartnerlisiting",
                        columnNames = {"LANG_ID", "C_ID", "STORE_ID", "VERSION_NO"})
        }
)
@IdClass(StorePartnerListingCompositeKey.class)
public class StorePartnerListing {

    @Id
    @Column(name = "LANG_ID", columnDefinition = "nvarchar(10)")
    private String languageId;

    @Id
    @Column(name = "C_ID", columnDefinition = "nvarchar(50)")
    private String companyId;

    @Id
    @Column(name = "STORE_ID", columnDefinition = "nvarchar(10)")
    private String storeId;

    @Id
    @Column(name = "VERSION_NO")
    private Long versionNumber;

    @Column(name = "STORE_NM", columnDefinition = "nvarchar(100)")
    private String storeName;

    @Column(name = "VALID_DATE_FROM")
    private Date validityDateFrom;

    @Column(name = "VALID_DATE_TO")
    private Date validityDateTo;

    @Column(name = "GRP_ID")
    private Long groupTypeId;

    @Column(name = "GRP_TYP_NM", columnDefinition = "nvarchar(100)")
    private String groupTypeName;

    @Column(name = "SUB_GRP_ID")
    private Long subGroupId;

    @Column(name = "SUB_GRP_NM", columnDefinition = "nvarchar(100)")
    private String subGroupName;

    @Column(name = "GROUP_ID")
    private Long groupId;

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

    @Column(name = "CO_OWNER_ID_5")
    private Long coOwnerId5;

    @Column(name = "CO_OWNER_ID_6")
    private Long coOwnerId6;

    @Column(name = "CO_OWNER_ID_7")
    private Long coOwnerId7;

    @Column(name = "CO_OWNER_ID_8")
    private Long coOwnerId8;

    @Column(name = "CO_OWNER_ID_9")
    private Long coOwnerId9;

    @Column(name = "CO_OWNER_ID_10")
    private Long coOwnerId10;

    @Column(name = "CO_OWNER_NAME_1", columnDefinition = "nvarchar(200)")
    private String coOwnerName1;

    @Column(name = "CO_OWNER_NAME_2", columnDefinition = "nvarchar(200)")
    private String coOwnerName2;

    @Column(name = "CO_OWNER_NAME_3", columnDefinition = "nvarchar(200)")
    private String coOwnerName3;

    @Column(name = "CO_OWNER_NAME_4", columnDefinition = "nvarchar(200)")
    private String coOwnerName4;

    @Column(name = "CO_OWNER_NAME_5", columnDefinition = "nvarchar(200)")
    private String coOwnerName5;

    @Column(name = "CO_OWNER_NAME_6", columnDefinition = "nvarchar(200)")
    private String coOwnerName6;

    @Column(name = "CO_OWNER_NAME_7", columnDefinition = "nvarchar(200)")
    private String coOwnerName7;

    @Column(name = "CO_OWNER_NAME_8", columnDefinition = "nvarchar(200)")
    private String coOwnerName8;

    @Column(name = "CO_OWNER_NAME_9", columnDefinition = "nvarchar(200)")
    private String coOwnerName9;

    @Column(name = "CO_OWNER_NAME_10", columnDefinition = "nvarchar(200)")
    private String coOwnerName10;

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

    @Column(name = "CO_OWNER_PER_6")
    private Double coOwnerPercentage6;

    @Column(name = "CO_OWNER_PER_7")
    private Double coOwnerPercentage7;

    @Column(name = "CO_OWNER_PER_8")
    private Double coOwnerPercentage8;

    @Column(name = "CO_OWNER_PER_9")
    private Double coOwnerPercentage9;

    @Column(name = "CO_OWNER_PER_10")
    private Double coOwnerPercentage10;

    @Column(name = "R_SHIP_ID_1")
    private Long relationShipId1;

    @Column(name = "R_SHIP_ID_2")
    private Long relationShipId2;

    @Column(name = "R_SHIP_ID_3")
    private Long relationShipId3;

    @Column(name = "R_SHIP_ID_4")
    private Long relationShipId4;

    @Column(name = "R_SHIP_ID_5")
    private Long relationShipId5;

    @Column(name = "R_SHIP_ID_6")
    private Long relationShipId6;

    @Column(name = "R_SHIP_ID_7")
    private Long relationShipId7;

    @Column(name = "R_SHIP_ID_8")
    private Long relationShipId8;

    @Column(name = "R_SHIP_ID_9")
    private Long relationShipId9;

    @Column(name = "R_SHIP_ID_10")
    private Long relationShipId10;

    @Column(name = "R_SHIP_NAME_1", columnDefinition = "nvarchar(200)")
    private String relationShipName1;

    @Column(name = "R_SHIP_NAME_2", columnDefinition = "nvarchar(200)")
    private String relationShipName2;

    @Column(name = "R_SHIP_NAME_3", columnDefinition = "nvarchar(200)")
    private String relationShipName3;

    @Column(name = "R_SHIP_NAME_4", columnDefinition = "nvarchar(200)")
    private String relationShipName4;

    @Column(name = "R_SHIP_NAME_5", columnDefinition = "nvarchar(200)")
    private String relationShipName5;

    @Column(name = "R_SHIP_NAME_6", columnDefinition = "nvarchar(200)")
    private String relationShipName6;

    @Column(name = "R_SHIP_NAME_7", columnDefinition = "nvarchar(200)")
    private String relationShipName7;

    @Column(name = "R_SHIP_NAME_8", columnDefinition = "nvarchar(200)")
    private String relationShipName8;

    @Column(name = "R_SHIP_NAME_9", columnDefinition = "nvarchar(200)")
    private String relationShipName9;

    @Column(name = "R_SHIP_NAME_10", columnDefinition = "nvarchar(200)")
    private String relationShipName10;

    @Column(name = "STATUS_ID")
    private Long statusId;

    @Column(name = "STATUS_ID_2")
    private Long statusId2;

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