package com.flourish.b2b.api.simulator.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class NewConsignmentOutbound {
	
	@NotBlank(message = "ShipperCode is mandatory")
    private String shipperCode;
	
    private String consigneCode;
    private String outboundForwarderCode;
	
	@NotBlank(message = "Shipment Order Type is mandatory")
    private String shipmentOrdertype;
    
	@NotBlank(message = "Shipment Order No is mandatory")
    private String shipmentOrderNo;
	
    private String invoiceNo;
    private String outboundDeliveryOrderNo;
    private String itemCode;
	
	@NotBlank(message = "Delivery Type is mandatory")
	private String deliveryType;
	
    private String warehouseCode;
    private Date shipmentOrderDate = new Date();
    private Date requiredDeliveryDate = new Date();
    private Date deliveryOrderDate = new Date();
    private String itemDesc;
    private String hsnCode;
    
    private Double itemQuantity;
    
    private String varaintName;
    private String orderedUnitOfMeasure;
    private Long itemLength;
    private Long itemWidth;
    private Long itemHeight;
    private String dimensionUnitOfMeasure;
    private Long itemWeight;
    private String weightUnitOfMeasure;
    private String unitPrice;
    private String currency;
    private Double totalValue;
    private String totalCurrency;
    private String shipperName;
    private String consigneeName;
    private String shipperAddress;
    private String consigneeAddress;
    private String outboundForwarderName;
    private String customerName;
    private String deliveryAddress;
    
    @NotNull(message = "Status is mandatory")
    private String status;
    
    @NotNull(message = "Consignement Type is mandatory")
    private String consignementType;
    
    private String itemGroup;
    private String eWayBillNumber;
    private Date invoiceDate = new Date();
    private String paymentMode;
    private Double codAmount;
    private String codMode;
    
    @JsonIgnore
    private String createdBy = "Service Provider";
    
    @JsonIgnore
    private Date createdOn = new Date();
    
    @JsonIgnore
    private String updatedBy = "Service Provider";
    
    @JsonIgnore
    private Date updatedOn = new Date();
}
