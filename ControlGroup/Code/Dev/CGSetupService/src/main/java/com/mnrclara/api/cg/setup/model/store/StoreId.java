package com.mnrclara.api.cg.setup.model.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
/*
 * `STATE_ID`,`C_ID`, `LANG_ID`
 */
@Table(
        name = "tblstoreid",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_key_storeid",
                        columnNames = {"STORE_ID","C_ID","LANG_ID"})
        }
)
@IdClass(StoreIdCompositeKey.class)
public class StoreId {

    @Id
    @Column(name = "STORE_ID")
    private Long storeId;

    @Id
    @Column(name="C_ID",columnDefinition = "nvarchar(50)")
    private String companyId;

    @Id
    @Column(name = "LANG_ID",columnDefinition = "nvarchar(5)")
    private String languageId;

    @Column(name = "STORE_NAME",columnDefinition = "nvarchar(100)")
    private String storeName;

    @Column(name = "GRP_TYP_ID")
    private Long groupTypeId;

    @Column(name = "SUB_GRP_TYP_ID")
    private Long subGroupTypeId;

    @Column(name = "GRP_TYP_NM",columnDefinition = "nvarchar(100)")
    private String groupTypeName;

    @Column(name = "SUB_GRP_TYP_NM",columnDefinition = "nvarchar(100)")
    private String subGroupTypeName;

    @Column(name = "PH_NO",columnDefinition = "nvarchar(20)")
    private String phoneNo;

    @Column(name = "ADDRESS",columnDefinition = "nvarchar(500)")
    private String address;

    @Column(name = "CITY",columnDefinition = "nvarchar(50)")
    private String city;

    @Column(name = "STATE",columnDefinition = "nvarchar(50)")
    private String state;

    @Column(name = "COUNTRY",columnDefinition = "nvarchar(50)")
    private String country;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "IS_DELETED")
    private Long deletionIndicator;

    @Column(name = "C_ID_DESC",columnDefinition = "nvarchar(500)")
    private String companyIdAndDescription;

    @Column(name = "CITY_ID_DESC",columnDefinition = "nvarchar(500)")
    private String cityIdAndDescription;

    @Column(name = "COUNTRY_ID_DESC",columnDefinition = "nvarchar(500)")
    private String countryIdAndDescription;

    @Column(name = "STATE_ID_DESC",columnDefinition = "nvarchar(500)")
    private String stateIdAndDescription;

    @Column(name = "REF_FIELD_1",columnDefinition = "nvarchar(200)")
    private String referenceField1;

    @Column(name = "REF_FIELD_2",columnDefinition = "nvarchar(200)")
    private String referenceField2;

    @Column(name = "REF_FIELD_3",columnDefinition = "nvarchar(200)")
    private String referenceField3;

    @Column(name = "REF_FIELD_4",columnDefinition = "nvarchar(200)")
    private String referenceField4;

    @Column(name = "REF_FIELD_5",columnDefinition = "nvarchar(200)")
    private String referenceField5;

    @Column(name = "REF_FIELD_6",columnDefinition = "nvarchar(200)")
    private String referenceField6;

    @Column(name = "REF_FIELD_7",columnDefinition = "nvarchar(200)")
    private String referenceField7;

    @Column(name = "REF_FIELD_8",columnDefinition = "nvarchar(200)")
    private String referenceField8;

    @Column(name = "REF_FIELD_9",columnDefinition = "nvarchar(200)")
    private String referenceField9;

    @Column(name = "REF_FIELD_10",columnDefinition = "nvarchar(200)")
    private String referenceField10;

    @Column(name = "CTD_BY",columnDefinition = "nvarchar(50)")
    private String createdBy;

    @Column(name = "CTD_ON")
    private Date createdOn = new Date();

    @Column(name = "UTD_BY",columnDefinition = "nvarchar(50)")
    private String updatedBy;

    @Column(name = "UTD_ON")
    private Date updatedOn = new Date();


}

