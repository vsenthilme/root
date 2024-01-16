package com.mnrclara.api.management.model.matteritform;

import lombok.Data;

@Data
public class ClientAddress {

	private int number;
	private String streetAddress;
	private String city;
	private String state;
	private String zipCode;
}
