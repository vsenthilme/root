package com.iweb2b.core.model.integration.asyad;

import lombok.Data;

@Data
public class DestinationDetails {
	
	/*
	 * 		{
               "name": "Mohammad Alsmaim",
               "phone": "96597990399",
               "alternate_phone": "",
               "address_line_1": "house 61 Block 5, street 517",
               "address_line_2": "",
               "pincode": "91710",
               "city": "Jaber Alahmad",
               "state": "",
               "country": "KW"
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
