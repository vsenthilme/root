package com.iwmvp.core.model.master;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDetailsHeader {
    private String languageId;
    private String companyId;
    private String orderId;
    private String referenceNo;
    private String shipsyOrderNo;
    private Long customerId;
    private String loadType;
    private String typeOfDelivery;
    private String deliveryCharge;
    private String originDetailsName;
    private String originDetailsPhone;
    private String originDetailsAddressLine1;
    private String originDetailsAddressLine2;
    private String originDetailsPincode;
    private String destinationDetailsName;
    private String destinationDetailsPhone;
    private String destinationDetailsAddressLine1;
    private String destinationDetailsAddressLine2;
    private String destinationDetailsPincode;
    private String serviceTypeId;
    private String loyaltyPoint;
    private String loyaltyAmount;
    private String originCity;
    private String originState;
    private String originCountry;
    private String destinationCity;
    private String destinationState;
    private String destinationCountry;
    private String status;
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
    private Date createdOn = new Date();
    private String updatedBy;
    private Date updatedOn = new Date();
}
