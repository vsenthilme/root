package com.iweb2b.core.model.integration;

import lombok.Data;

import java.util.Date;
@Data
public class Customer {
    private String languageId;
    private String companyId;
    private Long customerId;
    private String customerType;
    private String customerName;
    private String customerCategory;
    private String phoneNo;
    private String alternatePhoneNo;
    private String civilId;
    private String emailId;
    private String address;
    private String city;
    private String country;
    private String userName;
    private String password;
    private String status;
    private Double loyaltyPoint;
    private Long deletionIndicator;
    private String referenceField1;
    private String referenceField2;
    private String referenceField3;
    private String referenceField4;
    private String referenceField5;
    private String referenceField6;
    private String referenceField7;
    private String referenceField8;
    private String referenceField9;
    private String referenceField10;
    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;
}
