package com.mnrclara.api.cg.transaction.model.Requestid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblrequestid")
public class RequestId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "REQUEST_ID")
    private Long requestId;

    @Column(name = "FILE_NM")
    private String fileName;

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

    @Column(name = "UP_LOADED_BY")
    private String uploadedBy;

    @Column(name = "RE_UPLOADED_BY")
    private String reUploadedBy;

    @Column(name = "UP_LOAD_ON")
    private Date uploadedOn;

    @Column(name = "RE_UP_LOAD_ON")
    private Date reUploadedOn;
}
