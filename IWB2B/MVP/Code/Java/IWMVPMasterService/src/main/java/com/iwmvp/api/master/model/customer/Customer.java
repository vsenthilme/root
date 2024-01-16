package com.iwmvp.api.master.model.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tblmvpcustomer")
public class Customer {
    @Column(name = "LANG_ID", columnDefinition = "nvarchar(5)")
    private String languageId;
    @Column(name = "COMP_ID", columnDefinition = "nvarchar(25)")
    private String companyId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CUST_ID")
    private Long customerId;
    @Column(name="CUST_TYPE",columnDefinition = "nvarchar(50)")
    private String customerType;
    @Column(name="CUST_NAME",columnDefinition = "nvarchar(100)")
    private String customerName;
    @Column(name="CUST_CATEGORY",columnDefinition = "nvarchar(20)")
    private String customerCategory;
    @Column(name="PHONE_NO",columnDefinition = "nvarchar(20)")
    private String phoneNo;
    @Column(name="ALT_PHONE_NO",columnDefinition = "nvarchar(20)")
    private String alternatePhoneNo;
    @Column(name="CIVIL_ID",columnDefinition = "nvarchar(20)")
    private String civilId;
    @Column(name="EMAIL_ID",columnDefinition = "nvarchar(50)")
    private String emailId;
    @Column(name="ADDRESS",columnDefinition = "nvarchar(1000)")
    private String address;
    @Column(name="CITY",columnDefinition = "nvarchar(50)")
    private String city;
    @Column(name="COUNTRY",columnDefinition = "nvarchar(50)")
    private String country;
    @Column(name="USER_NM",columnDefinition = "nvarchar(50)")
    private String userName;
    @Column(name="PASSWORD",columnDefinition = "nvarchar(50)")
    private String password;
    @Column(name="STATUS",columnDefinition = "nvarchar(20)")
    private String status;
    @Column(name="LOYALTY_POINT")
    private Double loyaltyPoint;
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
    @Column(name="APD_BY",columnDefinition = "nvarchar(50)")
    private String approvedBy;
    @Column(name="APD_ON")
    private Date approvedOn = new Date();
    @Column(name="UTD_BY",columnDefinition = "nvarchar(50)")
    private String updatedBy;
    @Column(name="UTD_ON")
    private Date updatedOn = new Date();
}
