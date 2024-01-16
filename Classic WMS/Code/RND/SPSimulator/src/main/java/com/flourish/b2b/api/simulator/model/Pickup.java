package com.flourish.b2b.api.simulator.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "obs_pickup")
public class Pickup {

    @Id
    @Column(name = "shipper_code")
    private String shipperCode;
    
    @Column(name = "so_type")
    private String shipmentOrdertype;
    
    @Column(name = "so_no")
    private String shipmentOrderNo;
    
    @Column(name = "ob_tr_no")
    private String outboundTrackingNo;
    
    @Column(name = "del_typ")
    private String deliveryType;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "item_code")
    private String itemCode;
    
    @Column(name = "inv_no")
    private String invoiceNo;
    
    @Column(name = "ob_do_no")
    private String outboundDeliveryOrderNo;
    
    @Column(name = "consignee_code")
    private String consigneCode;
    
    @Column(name = "ob_forward_code")
    private String outboundForwarderCode;
      
    @Column(name = "do_date")
    private Date deliveryOrderDate = new Date();
    
    @Column(name = "ob_tr_date")
    private Date trackingNoDate = new Date();
    
    @Column(name = "item_text")
    private String itemDesc;
    
    @Column(name = "hs_code")
    private String hsnCode;
    
    @Column(name = "item_qty")
    private Double itemQuantity;
    
    @Column(name = "var_name")
    private String varaintName;
    
    @Column(name = "ord_uom")
    private String orderedUnitOfMeasure;
    
    @Column(name = "item_len")
    private Long itemLength;
    
    @Column(name = "item_wid")
    private Long itemWidth;
    
    @Column(name = "item_ht")
    private Long itemHeight;
    
    @Column(name = "dim_uom")
    private String dimensionUnitOfMeasure;
    
    @Column(name = "item_wt")
    private Long itemWeight;
    
    @Column(name = "wt_uom")
    private String weightUnitOfMeasure;
    
    @Column(name = "unt_price")
    private String unitPrice;
    
    @Column(name = "curr")
    private String currency;
    
    @Column(name = "payment_mode")
    private String paymentMode;
    
    @Column(name = "tot_value")
    private Double totalValue;
    
    @Column(name = "tot_curr")
    private String totalCurrency;
    
    @Column(name = "shipper_nm")
    private String shipperName;
    
    @Column(name = "consignee_nm")
    private String consigneeName;
    
    @Column(name = "shipper_add")
    private String shipperAddress;
        
    @Column(name = "consignee_add")
    private String consigneeAddress;
    
    @Column(name = "ob_forward_nm")
    private String outboundForwarderName;
    
    @Column(name = "pick_dt")
    private Date pickedDate = new Date();
    
    @Column(name = "pop_detail")
    private String proofOfPickingDetail; // This is Image
    
    @Column(name = "vehicle_id")
    private String vehicleNumber;
    
    @Column(name = "driver_nm")
    private String driverName;
    
    @Column(name = "ctd_by")
    private String createdBy;
    
    @Column(name = "ctd_on")
    private Date createdOn = new Date();
    
    @Column(name = "utd_by")
    private String updatedBy;
    
    @Column(name = "utd_on")
    private Date updatedOn = new Date();
}
