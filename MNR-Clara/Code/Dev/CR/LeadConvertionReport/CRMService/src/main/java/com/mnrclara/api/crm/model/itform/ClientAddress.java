package com.mnrclara.api.crm.model.itform;

import lombok.Data;

@Data
public class ClientAddress {

	private int number;
	private String streetAddress;
	private String city;
	private String state;
	private String zipCode;
}
