package com.mnrclara.api.cg.setup.model.client;

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
 * `C_ID`, `LANG_ID`,'CLIENT_ID'
 */
@Table(
        name = "tblclient",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_key_client",
                        columnNames = {"C_ID","CLIENT_ID","LANG_ID"})
        }
)
@IdClass(ClientCompositeKey.class)
public class Client {

    @Id
    @Column(name = "CLIENT_ID")
    private Long clientId;

    @Id
    @Column(name = "C_ID",columnDefinition = "nvarchar(50)")
    private String companyId;

    @Id
    @Column(name = "LANG_ID",columnDefinition = "nvarchar(5)")
    private String languageId;

    @Column(name = "CLIENT_NM",columnDefinition = "nvarchar(100)")
    private String clientName;

    @Column(name = "STATUS_ID")
    private Long statusId;

    @Column(name = "C_ID_DESC",columnDefinition = "nvarchar(100)")
    private String companyIdAndDescription;

    @Column(name = "IS_DELETED")
    private Long deletionIndicator = 0L;

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
