package com.iweb2b.api.integration.model.consignment.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity (name = "ConsignmentEntity")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblconsignment")
public class ConsignmentEntity implements Serializable {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONSIGNMENT_ID")
    private Long consignmentId = 0L;
    
    @Column(name="REFERENCE_NUMBER", columnDefinition = "nvarchar(100)" )
    private String reference_number;
    
    @Column(name="COD_AMOUNT", columnDefinition = "nvarchar(100)")
	private String cod_amount;

    @Column(name="COD_COLLECTION_MODE", columnDefinition = "nvarchar(100)")
	private String cod_collection_mode;

    @Column(name="CUSTOMER_CODE", columnDefinition = "nvarchar(100)")
	private String customer_code;

    @Column(name="SERVICE_TYPE_ID", columnDefinition = "nvarchar(100)")
	private String service_type_id;

    @Column(name="CONSIGNMENT_TYPE", columnDefinition = "nvarchar(100)")
	private String consignment_type;

    @Column(name="LOAD_TYPE", columnDefinition = "nvarchar(100)")
	private String load_type;

    @Column(name="DESCRIPTION", columnDefinition = "nvarchar(255)")
	private String description;

    @Column(name="COD_FAVOR_OF", columnDefinition = "nvarchar(100)")
	private String cod_favor_of;

    @Column(name="DIMENSION_UNIT", columnDefinition = "nvarchar(100)")
	private String dimension_unit;

    @Column(name="LENGTH", columnDefinition = "nvarchar(100)")
	private String length;

    @Column(name="WIDTH", columnDefinition = "nvarchar(100)")
    private String width;

    @Column(name="HEIGHT", columnDefinition = "nvarchar(100)")
    private String height;

    @Column(name="WEIGHT_UNIT", columnDefinition = "nvarchar(100)")
    private String weight_unit;

    @Column(name="WEIGHT")
    private Double weight;

    @Column(name="DECLARED_VALUE")
    private Double declared_value;

    @Column(name="NUM_PIECES")
    private Long num_pieces;

    @Column(name="NOTES", columnDefinition = "nvarchar(500)")
    private String notes;

    @Column(name="CUSTOMER_REFERENCE_NUMBER", columnDefinition = "nvarchar(100)")
    private String customer_reference_number;

    @Column(name="IS_RISK_SURCHARGE_APPLICABLE")
    private Boolean is_risk_surcharge_applicable;

    @Column(name = "CREATED_AT")
    private Date created_at;

    @Column(name = "STATUS_DESCRIPTION", columnDefinition = "nvarchar(100)")
    private String status_description;
    
    @Column(name = "CUSTOMER_CIVIL_ID")
    private String customer_civil_id;
    
    @Column(name = "RECEIVER_CIVIL_ID")
    private String receiver_civil_id;
    
    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "AWB_3RD_PARTY", columnDefinition = "nvarchar(150)")
    private String awb_3rd_Party;
    
    @Column(name = "SCANTYPE_3RD_PARTY", columnDefinition = "nvarchar(150)")
    private String scanType_3rd_Party;
    
    @Column(name = "HUBCODE_3RD_PARTY", columnDefinition = "nvarchar(150)")
    private String hubCode_3rd_Party;

    @Column(name = "ORDER_TYPE", columnDefinition = "nvarchar(50)")
    private String orderType;

    @Column(name = "JNT_PUSH_STATUS", columnDefinition = "nvarchar(50)")
    private String jntPushStatus;

    @Column(name = "BOUTIQAAT_PUSH_STATUS", columnDefinition = "nvarchar(50)")
    private String boutiqaatPushStatus;
    
    @Column(name = "IS_AWB_PRINTED")
    private Boolean is_awb_printed;

    @Column(name = "INCO_TERMS", columnDefinition = "nvarchar(100)")
    private String inco_terms;
    
    @Column(name = "QP_WH_STATUS")
    private String qp_webhook_status;
    
    @Column(name = "IS_CANCELLED")
    private Long cancelStatus;
}