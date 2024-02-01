package com.iweb2b.api.integration.model.tracking;

import java.util.List;

import lombok.Data;

@Data
public class ConsignmentTracking {

	private String reference_number;
	private String customer_reference_number;
	private String service_type_id;
	private String attempt_count;
	private String status;
	private String delivery_kyc_type;
	private String delivery_kyc_number;
	private Boolean is_cod;
	private String cod_amount;
	private String weight;
	private String creation_date;
	private String receiver_name;
	private String receiver_relation;
	private String hub_code;
	private String courier_partner_reference_number;
	private String courier_partner;
	private String courier_account;
	private String num_pieces;
	private String customer_code;
	private String customer_name;
	private String billing_type;
	private String origin_location_code;
	private String destination_location_code;
	private String drs_number;
	
	private String rescheduled_date;
	private String rescheduled_timeslot_start;
	private String rescheduled_timeslot_end;
	
	private Long pickup_attempt_count;
	private String carrier_payment_details;
	private String pop_image;
	private String pop_signature_image;
	private String description;
	private String shipment_type;
	private String delivery_date;
	private String raven_link;
	private String delivery_time;
	private Long  status_timestamp;
	
	private String signature_image;
	private String location;
	private String trip_reference_number;
	private String courier_event_code;
	private String sub_type;
	private String consignment_movement_type;
	private String leg_type;
	private String carrier_bag_id;
	private String event_time_utc;
	private Boolean is_otp_verified;

	private List<PiecesDetail> pieces_detail;
	private OriginDetail origin_details;
	private DestinationDetail destination_details;
	private List<Event> events;
}
