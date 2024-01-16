package com.flourish.b2b.api.simulator.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "obs_consignment_outbound")
public class ConsignmentOutbound implements Persistable<String> {

//    @Id
    @Column(name = "shipper_code")
    private String shipperCode;
    
    @Column(name = "consignee_code")
    private String consigneCode;
    
    @Column(name = "ob_forward_code")
    private String outboundForwarderCode;
    
    @Column(name = "so_type")
    private String shipmentOrdertype;
    
    @Id
    @Column(name = "so_no")
    private String shipmentOrderNo;
    
    @Column(name = "inv_no")
    private String invoiceNo;
    
    @Column(name = "ob_do_no")
    private String outboundDeliveryOrderNo;
    
    @Column(name = "item_code")
    private String itemCode;
    
    @Column(name = "del_typ")
    private String deliveryType;
    
    @Column(name = "wh_code")
    private String warehouseCode;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "so_date")
    private Date shipmentOrderDate = new Date();
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "req_del_date")
    private Date requiredDeliveryDate = new Date();
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "do_date")
    private Date deliveryOrderDate = new Date();
    
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
    
    @Column(name = "customer_nm")
    private String customerName;
    
    @Column(name = "delivery_add")
    private String deliveryAddress;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "consignment_type")
    private String consignementType;
    
    @Column(name = "item_group")
    private String itemGroup;
    
    @Column(name = "eway_bill")
    private String eWayBillNumber;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "invoice_dt")
    private Date invoiceDate = new Date();
    
    @Column(name = "payment_mode")
    private String paymentMode;
    
    @Column(name = "cod_amount")
    private Double codAmount;
    
    @Column(name = "cod_collection_mode")
    private String codMode;
    
    @Column(name = "ctd_by")
    private String createdBy;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "ctd_on")
    private Date createdOn = new Date();
    
    @Column(name = "utd_by")
    private String updatedBy;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "utd_on")
    private Date updatedOn = new Date();

	@Override
	public String getId() {
		return null;
	}

	@Override
	public boolean isNew() {
		return true;
	}
}
