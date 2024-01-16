package com.iwmvp.api.master.model.orderdetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tblmvporderdetailsheader")
public class OrderDetailsHeader {
    @Column(name = "LANG_ID", columnDefinition = "nvarchar(5)")
    private String languageId;
    @Column(name = "COMP_ID", columnDefinition = "nvarchar(50)")
    private String companyId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ORDER_ID")
    private Long orderId;

    @Column(name="REF_NO",columnDefinition = "nvarchar(50)")
    private String referenceNo;
    @Column(name="SHIPSY_ORD_NO",columnDefinition = "nvarchar(50)")
    private String shipsyOrderNo;
    @Column(name="CUST_ID")
    private Long customerId;
    @Column(name="LOAD_TYPE",columnDefinition = "nvarchar(30)")
    private String loadType;
    @Column(name="TYPE_OF_DELIVERY",columnDefinition = "nvarchar(10)")
    private String typeOfDelivery;
    @Column(name="DELIVERY_CHARGE")
    private Double deliveryCharge;
    @Column(name="ORIGIN_DETAILS_NAME",columnDefinition = "nvarchar(100)")
    private String originDetailsName;
    @Column(name="ORIGIN_DETAILS_PHONE",columnDefinition = "nvarchar(30)")
    private String originDetailsPhone;
    @Column(name="ORIGIN_DETAILS_ADDRESS_LINE_1",columnDefinition = "nvarchar(500)")
    private String originDetailsAddressLine1;
    @Column(name="ORIGIN_DETAILS_ADDRESS_LINE_2",columnDefinition = "nvarchar(500)")
    private String originDetailsAddressLine2;
    @Column(name="ORIGIN_DETAILS_PINCODE",columnDefinition = "nvarchar(10)")
    private String originDetailsPincode;
    @Column(name="DESTINATION_DETAILS_NAME",columnDefinition = "nvarchar(100)")
    private String destinationDetailsName;
    @Column(name="DESTINATION_DETAILS_PHONE",columnDefinition = "nvarchar(30)")
    private String destinationDetailsPhone;
    @Column(name="DESTINATION_DETAILS_ADDRESS_LINE_1",columnDefinition = "nvarchar(500)")
    private String destinationDetailsAddressLine1;
    @Column(name="DESTINATION_DETAILS_ADDRESS_LINE_2",columnDefinition = "nvarchar(500)")
    private String destinationDetailsAddressLine2;
    @Column(name="DESTINATION_DETAILS_PINCODE",columnDefinition = "nvarchar(10)")
    private String destinationDetailsPincode;
    @Column(name="SERVICE_TYPE_ID",columnDefinition = "nvarchar(30)")
    private String serviceTypeId;
    @Column(name="LOYALTY_POINT")
    private Double loyaltyPoint;
    @Column(name="LOYALTY_AMT")
    private Double loyaltyAmount;
    @Column(name="ORIGIN_CITY",columnDefinition = "nvarchar(50)")
    private String originCity;
    @Column(name="ORIGIN_STATE",columnDefinition = "nvarchar(50)")
    private String originState;
    @Column(name="ORIGIN_COUNTRY",columnDefinition = "nvarchar(50)")
    private String originCountry;
    @Column(name="DESTINATION_CITY",columnDefinition = "nvarchar(50)")
    private String destinationCity;
    @Column(name="DESTINATION_STATE",columnDefinition = "nvarchar(50)")
    private String destinationState;
    @Column(name="DESTINATION_COUNTRY",columnDefinition = "nvarchar(50)")
    private String destinationCountry;
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
    @Column(name="APD_BY",columnDefinition = "nvarchar(50)")
    private String approvedBy;
    @Column(name="APD_ON")
    private Date approvedOn = new Date();
    @Column(name="UTD_BY",columnDefinition = "nvarchar(50)")
    private String updatedBy;
    @Column(name="UTD_ON")
    private Date updatedOn = new Date();
}
