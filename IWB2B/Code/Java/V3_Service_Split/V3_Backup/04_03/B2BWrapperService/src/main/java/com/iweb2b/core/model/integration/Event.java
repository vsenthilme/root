package com.iweb2b.core.model.integration;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Event {

	private String type;
    private Date event_time;
    private String worker_name;
    private String worker_code;
    private String employee_code;
    private String employee_name;
    private String hub_name;
    private String hub_code;
    private String lat;
    private String lng;
    
    private String failure_reason;
    private String customer_update;
    private String execution_status;
    private String status_external;
    private String event_description;
    private String carrier_location_code;
    private String pop_image;
    private String poc_image;
    
    private String notes;
    private String signature_image;
    
    private Pop_image_list pop_image_list;
    private String pop_signature_image;
    private List proof_image_list;
    private String[] signature_image_list;
    private String[] proof_video_list;
    private String vehicle_number;
    private Poc_image_list poc_image_list;
}


