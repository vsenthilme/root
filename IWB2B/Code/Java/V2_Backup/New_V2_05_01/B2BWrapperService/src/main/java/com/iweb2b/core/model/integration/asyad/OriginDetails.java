package com.iweb2b.core.model.integration.asyad;

import lombok.Data;

@Data
public class OriginDetails {
	
	/*
	 * "origin_details": {
           "name": "The Hut Group",
           "phone": "97101606811863",
           "alternate_phone": "",
           "address_line_1": "Mohebi Logistics; Plot WT01 & WT04",
           "address_line_2": "LF Logistics/Mohebi Logistics, Plot",
           "pincode": "8005",
           "city": "Dubai",
           "state": "",
           "country": "AE"
       },
	 */

	private String name;
    private String phone;
    private String alternatePhone;
    private String addressLine1;
    private String addressLine2;
    private String pincode;
    private String city;
    private String state;
    private String country;
}
