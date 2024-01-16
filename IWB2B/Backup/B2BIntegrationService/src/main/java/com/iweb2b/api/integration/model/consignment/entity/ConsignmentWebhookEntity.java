package com.iweb2b.api.integration.model.consignment.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity (name = "ConsignmentWebhookEntity")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblconsignmentwebhook")
public class ConsignmentWebhookEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONSIGNMENT_WEBHOOK_ID")
    private Long consignment_webhook_id;

    @Column(name = "REFERENCE_NUMBER", columnDefinition = "nvarchar(100)")
    private String reference_number;
    
    @Column(name = "CUSTOMER_REFERENCE_NUMBER", columnDefinition = "nvarchar(100)")
    private String customer_reference_number;
    
    @Column(name = "DESCRIPTION", columnDefinition = "nvarchar(5000)")
    private String description;
    
    @Column(name = "COURIER_PARTNER_REFERENCE_NUMBER", columnDefinition = "nvarchar(255)")
    private String courier_partner_reference_number;
    
    @Column(name = "COURIER_PARTNER", columnDefinition = "nvarchar(255)")
    private String courier_partner;
    
    @Column(name = "COURIER_ACCOUNT", columnDefinition = "nvarchar(100)")
    private String courier_account;
    
    @Column(name = "DRS_NUMBER", columnDefinition = "nvarchar(100)")
    private String drs_number;
    
    @Column(name = "SHIPPER_PHONE", columnDefinition = "nvarchar(100)")
    private String shipper_phone;
    
    @Column(name = "HUB_NAME", columnDefinition = "nvarchar(255)")
    private String hub_name;
    
    @Column(name = "HUB_CODE", columnDefinition = "nvarchar(100)")
    private String hub_code;
    
    @Column(name = "DESTINATION_HUB_CODE", columnDefinition = "nvarchar(100)")
    private String destination_hub_code;
    
    @Column(name = "EMPLOYEE_CODE", columnDefinition = "nvarchar(100)")
    private String employee_code;
    
    @Column(name = "DESTINATION_NAME", columnDefinition = "nvarchar(255)")
    private String destination_name;
    
    @Column(name = "DESTINATION_PHONE", columnDefinition = "nvarchar(100)")
    private String destination_phone;
    
    @Column(name = "DESTINATION_ADDRESS_LINE_1", columnDefinition = "nvarchar(500)")
    private String destination_address_line_1;
    
    @Column(name = "DESTINATION_ADDRESS_LINE_2", columnDefinition = "nvarchar(500)")
    private String destination_address_line_2;
    
    @Column(name = "DESTINATION_CITY", columnDefinition = "nvarchar(100)")
    private String destination_city;
    
    @Column(name = "COD_AMOUNT")
    private Double cod_amount;
    
    @Column(name = "CUSTOMER_CODE", columnDefinition = "nvarchar(100)")
    private String customer_code;
    
    @Column(name = "CREATED_AT", columnDefinition = "nvarchar(100)")
    private String created_at;
    
    @Column(name = "WORKER_CODE", columnDefinition = "nvarchar(100)")
    private String worker_code;
    
    @Column(name = "WORKER_NAME", columnDefinition = "nvarchar(100)")
    private String worker_name;
    
    @Column(name = "ATTEMPT_COUNT")
    private Long attempt_count;
    
    @Column(name = "POC_IMAGE", columnDefinition = "nvarchar(255)")
    private String poc_image;
    
    @Column(name = "SIGNATURE_IMAGE", columnDefinition = "nvarchar(255)")
    private String signature_image;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consignment_webhook_id",fetch = FetchType.EAGER)
    private Set<PocImageListEntity> poc_image_list;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consignment_webhook_id",fetch = FetchType.EAGER)
    private Set<QualityCheckImageListEntity> quality_check_image_list;

    @Column(name = "FAILURE_REASON", columnDefinition = "nvarchar(255)")
    private String failure_reason;
    
    @Column(name = "TYPE", columnDefinition = "nvarchar(100)")
    private String type;
    
    @Column(name = "EVENT_TIME")
    private Date event_time;
    
    @Column(name = "ORIGIN_NAME", columnDefinition = "nvarchar(255)")
    private String origin_name;
    
    @Column(name = "SERVICE_TYPE", columnDefinition = "nvarchar(100)")
    private String service_type;
    
    @Column(name = "HUB_ARRIVAL_TIME", columnDefinition = "nvarchar(100)")
    private String hub_arrival_time;
    
    @Column(name = "PICKUP_TIME", columnDefinition = "nvarchar(100)")
    private String pickup_time;
    
    @Column(name = "FIRST_INSCAN_AT_HUB_TIME", columnDefinition = "nvarchar(100)")
    private String first_inscan_at_hub_time;
    
    @Column(name = "RECEIVER_PHONE", columnDefinition = "nvarchar(100)")
    private String receiver_phone;
    
    @Column(name = "DELIVERY_KYC_TYPE", columnDefinition = "nvarchar(100)")
    private String delivery_kyc_type;
    
    @Column(name = "DELIVERY_KYC_NUMBER", columnDefinition = "nvarchar(100)")
    private String delivery_kyc_number;
    
    @Column(name = "DELIVERY_STATUS")
    private Boolean delivery_status;
    
    @Column(name = "DELIVERY_ATTEMPTS")
    private Long delivery_attempts;
    
    @Column(name = "DELIVERY_DATE", columnDefinition = "nvarchar(100)")
    private String delivery_date;
    
    @Column(name = "DELIVERY_TIME", columnDefinition = "nvarchar(100)")
    private String delivery_time;
    
    @Column(name = "IS_COD")
    private Boolean is_cod;
    
    @Column(name = "CREATION_DATE", columnDefinition = "nvarchar(100)")
    private String creation_date;
    
    @Column(name = "RECEIVER_NAME", columnDefinition = "nvarchar(255)")
    private String receiver_name;
    
    @Column(name = "RECEIVER_RELATION", columnDefinition = "nvarchar(255)")
    private String receiver_relation;
    
    @Column(name = "FAILURE_REASON_CODE", columnDefinition = "nvarchar(100)")
    private String failure_reason_code;
    
    @Column(name = "LAT")
    private Double lat;
    
    @Column(name = "LNG")
    private Double lng;
    
    @Column(name = "RIDER_PHONE", columnDefinition = "nvarchar(100)")
    private String rider_phone;
    
    @Column(name = "TRIP_REFERENCE_NUMBER", columnDefinition = "nvarchar(100)")
    private String trip_reference_number;
    
    @Column(name = "RAVEN_LINK", columnDefinition = "nvarchar(255)")
    private String raven_link;
    
    @Column(name = "MOVEMENT_TYPE", columnDefinition = "nvarchar(100)")
    private String movement_type;
    
    @Column(name = "CARRIER_LOCATION", columnDefinition = "nvarchar(100)")
    private String carrier_location;
    
    @Column(name = "CONSIGNMENT_MOVEMENT_TYPE", columnDefinition = "nvarchar(100)")
    private String consignment_movement_type;
    
    @Column(name = "SHIPMENT_TYPE", columnDefinition = "nvarchar(100)")
    private String shipment_type;
    
    @Column(name = "CARRIER_NAME", columnDefinition = "nvarchar(255)")
    private String carrier_name;
    
    @Column(name = "LEG_TYPE", columnDefinition = "nvarchar(100)")
    private String leg_type;
    
    @Column(name = "CARRIER_LOCATION_CODE", columnDefinition = "nvarchar(100)")
    private String carrier_location_code;
    
    @Column(name = "CARRIER_BAG_ID", columnDefinition = "nvarchar(100)")
    private String carrier_bag_id;
    
    @Column(name = "PICKUP_TIME_EPOCH")
    private Long pickup_time_epoch;
    
    @Column(name = "HUB_ARRIVAL_TIME_EPOCH")
    private Long hub_arrival_time_epoch;
    
    @Column(name = "CREATED_AT_EPOCH")
    private Long created_at_epoch;
    
    @Column(name = "EVENT_TIME_EPOCH")
    private Long event_time_epoch;
    
    @Column(name = "COURIER_EVENT_CODE", columnDefinition = "nvarchar(100)")
    private String courier_event_code;
    
    @Column(name = "SUB_TYPE", columnDefinition = "nvarchar(100)")
    private String sub_type;
    
    @Column(name = "RESCHEDULED_DATE", columnDefinition = "nvarchar(100)")
    private String rescheduled_date;

    @Column(name = "RESCHEDULED_TIMESLOT_START", columnDefinition = "nvarchar(100)")
    private String rescheduled_timeslot_start;

    @Column(name = "RESCHEDULED_TIMESLOT_END", columnDefinition = "nvarchar(100)")
    private String rescheduled_timeslot_end;

    @Column(name = "STATUS_DESCRIPTION", columnDefinition = "nvarchar(255)")
    private String status_description;
}