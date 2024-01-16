package com.iweb2b.api.integration.model.consignment.dto.qp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class QPOrder implements Serializable {

	private String orderID;
	private String subOrderID;
    private String trackingNumber;
    private String customerName;
    private String customerMobile;
    
    @JsonProperty ("MerchantName")
    private String merchantName;
    
    @JsonProperty ("MerchantStore")
    private String merchantStore;
    
    @JsonProperty ("MerchantPhone")
    private String merchantPhone;
    
    private String delivery_Zone;
    private String delivery_Street;
    private String delivery_BuildingNo;
    private String delivery_UnitNo;
    private String pickup_Zone;
    private String pickup_Street;
    private String pickup_Building;
    private String pickup_Unitno;
    private String location_Details;
    private String deliveryType;
    private String deliveryScheduletype;
    private String zoneType;
    private String productDiscription;
    private Double weight;
    private Long quantity;
    
    @JsonProperty ("TransectionType")
    private Long transectionType;
    
    private Long currentStatus;
    private String createdDate;
}