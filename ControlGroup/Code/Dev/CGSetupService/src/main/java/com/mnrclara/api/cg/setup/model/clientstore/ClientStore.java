package com.mnrclara.api.cg.setup.model.clientstore;

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
 * 'CLIENT_ID', 'STORE_ID', 'COMPANY_ID', 'LANG_ID', 'VERSION_NO'
 */
@Table(
        name = "tblclientstore",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_key_clientstore",
                        columnNames = {"CLIENT_ID", "STORE_ID", "C_ID", "LANG_ID", "VERSION_NO"}
                )
        }
)
@IdClass(ClientStoreCompositeKey.class)
public class ClientStore {

    @Id
    @Column(name = "VERSION_NO")
    private Long versionNumber;

    @Id
    @Column(name = "CLIENT_ID")
    private Long clientId;

    @Id
    @Column(name = "STORE_ID")
    private Long storeId;

    @Id
    @Column(name = "C_ID", columnDefinition = "nvarchar(50)")
    private String companyId;

    @Id
    @Column(name = "LANG_ID", columnDefinition = "nvarchar(10)")
    private String languageId;

    @Column(name = "CLIENT_NM", columnDefinition = "nvarchar(100)")
    private String clientName;

    @Column(name = "STORE_NM", columnDefinition = "nvarchar(100)")
    private String storeName;

    @Column(name = "STATUS_ID")
    private Long statusId;

    @Column(name = "IS_DELETED")
    private Long deletionIndicator;

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

    @Column(name = "VALID_DATE_FROM")
    private Date validityDateFrom;

    @Column(name = "VALID_DATE_TO")
    private Date validityDateTo;

    @Column(name = "CTD_BY",columnDefinition = "nvarchar(50)")
    private String createdBy;

    @Column(name = "CTD_ON")
    private Date createdOn = new Date();

    @Column(name = "UTD_BY",columnDefinition = "nvarchar(50)")
    private String updatedBy;

    @Column(name = "UTD_ON")
    private Date updatedOn = new Date();
}