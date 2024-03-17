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
	private String pickup_attempt_count;
	private String carrier_payment_details;

	private List<PiecesDetail> pieces_detail;
	private OriginDetail origin_details;
	private DestinationDetail destination_details;
	private List<Event> events;
}
