package com.iwmvp.api.master.model.loyaltysetup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tblmvployaltysetup")
public class LoyaltySetup {
    @Column(name = "LANG_ID", columnDefinition = "nvarchar(5)")
    private String languageId;
    @Column(name = "COMP_ID", columnDefinition = "nvarchar(50)")
    private String companyId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LOYALTY_ID")
    private Long loyaltyId;
    @Column(name="CATEGORY_ID",columnDefinition = "nvarchar(50)")
    private String categoryId;
    @Column(name="TV_FROM")
    private Double transactionValueFrom;
    @Column(name="TV_TO")
    private Double transactionValueTo;
    @Column(name="LOYALTY_POINT")
    private Double loyaltyPoint;
    @Column(name="STATUS",columnDefinition = "nvarchar(20)")
    private String status;
    @Column(name = "IS_DELETED")
    private Long deletionIndicator;
    @Column(name="REF_FIELD_1",columnDefinition = "nvarchar(255)")
    private String referenceField1;
    @Column(name="REF_FIELD_2",columnDefinition = "nvarchar(255)")
    private String referenceField2;
    @Column(name="REF_FIELD_3",columnDefinition = "nvarchar(255)")
    private String referenceField3;
    @Column(name="REF_FIELD_4",columnDefinition = "nvarchar(255)")
    private String referenceField4;
    @Column(name="REF_FIELD_5",columnDefinition = "nvarchar(255)")
    private String referenceField5;
    @Column(name="REF_FIELD_6",columnDefinition = "nvarchar(255)")
    private String referenceField6;
    @Column(name="REF_FIELD_7",columnDefinition = "nvarchar(255)")
    private String referenceField7;
    @Column(name="REF_FIELD_8",columnDefinition = "nvarchar(255)")
    private String referenceField8;
    @Column(name="REF_FIELD_9",columnDefinition = "nvarchar(255)")
    private String referenceField9;
    @Column(name="REF_FIELD_10",columnDefinition = "nvarchar(255)")
    private String referenceField10;
    @Column(name="CTD_BY",columnDefinition = "nvarchar(50)")
    private String createdBy;
    @Column(name="CTD_ON")
    private Date createdOn = new Date();
    @Column(name="UTD_BY",columnDefinition = "nvarchar(50)")
    private String updatedBy;
    @Column(name="UTD_ON")
    private Date updatedOn = new Date();
}
