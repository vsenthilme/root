package com.flourish.b2b.api.simulator.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class NewPickup {

	@NotBlank(message = "Shipper Code is mandatory")
    private String shipperCode;
    
	@NotBlank(message = "Shipment Order type is mandatory")
    private String shipmentOrdertype;
    
	@NotBlank(message = "Shipment Order No is mandatory")
    private String shipmentOrderNo;
    
	@NotBlank(message = "Outbound Tracking No is mandatory")
    private String outboundTrackingNo;
    
	@NotBlank(message = "Delivery Type is mandatory")
    private String deliveryType;
    
	@NotBlank(message = "Status is mandatory")
    private String status;
    
    private String itemCode;
    
    private String invoiceNo;
    
    private String outboundDeliveryOrderNo;
    
    private String consigneCode;
    
    private String outboundForwarderCode;
      
    private Date deliveryOrderDate = new Date();
    
    private Date trackingNoDate = new Date();
    
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
    
    private String paymentMode;
    
    private Double totalValue;
    
    private String totalCurrency;
    
    private String shipperName;
    
    private String consigneeName;
    
    private String shipperAddress;
        
    private String consigneeAddress;
    
    private String outboundForwarderName;
    
    private Date pickedDate = new Date();
    
    private String proofOfPickingDetail; // This is Image
    
    private String vehicleNumber;
    
    private String driverName;
    
    private String createdBy;
    
    private Date createdOn = new Date();
}
