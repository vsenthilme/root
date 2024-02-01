package com.iweb2b.api.integration.model.tracking;

import java.util.Date;

import lombok.Data;

@Data
public class Event {

	 private String type;
	    private Date event_time;
	    private String event_description;
	    private String hub_name;
	    private String hub_code;
	    private String customer_update;
	    private String notes;
	    private String poc_image;
	    private String failure_reason;
	    private String signature_image;
	    private String pop_image;
	    private String worker_name;
	    private String worker_code;
	    private String employee_code;
	    private String employee_name;
	    private String latitude;
	    private String longitude;
	    private String execution_status;
	    private String status_external;
	    private String carrier_location_code;
   
}
