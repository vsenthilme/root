package com.iweb2b.core.model.integration.asyad;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ConsignmentWebhook{

    private String reference_number;
    private String customer_reference_number;
    private String description;
    private String courier_partner_reference_number;
    private String courier_partner;
    private String courier_account;
    private String drs_number;
    private String shipper_phone;
    private String hub_name;
    private String hub_code;
    private String destination_hub_code;
    private String employee_code;
    private String destination_name;
    private String destination_phone;
    private String destination_address_line_1;
    private String destination_address_line_2;
    private String destination_city;
    private Double cod_amount;
    private String customer_code;
    private String created_at;
    private String worker_code;
    private String worker_name;
    private Long attempt_count;
    private String poc_image;

    private List<String> poc_image_list;

    private List<String> quality_check_image_list;

    private String signature_image;
    private String failure_reason;
    private String type;
    private Date event_time;
    private String origin_name;
    private String service_type;
    private String hub_arrival_time;
    private String pickup_time;
    private String first_inscan_at_hub_time;
    private String receiver_phone;
    private String delivery_kyc_type;
    private String delivery_kyc_number;
    private Boolean delivery_status;
    private Long delivery_attempts;
    private String delivery_date;
    private String delivery_time;
    private Boolean is_cod;
    private String creation_date;
    private String receiver_name;
    private String receiver_relation;
    private String failure_reason_code;
    private Double lat;
    private Double lng;
    private String rider_phone;
    private String trip_reference_number;
    private String raven_link;
    private String movement_type;
    private String carrier_location;
    private String consignment_movement_type;
    private String shipment_type;
    private String carrier_name;
    private String leg_type;
    private String carrier_location_code;
    private String carrier_bag_id;
    private Long pickup_time_epoch;
    private Long hub_arrival_time_epoch;
    private Long created_at_epoch;
    private Long event_time_epoch;
    private String courier_event_code;
    private String sub_type;
    private String rescheduled_date;
    private String rescheduled_timeslot_start;
    private String rescheduled_timeslot_end;

    private String status_description;
}