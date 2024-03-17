package com.mnrclara.wrapper.core.model.crm.itform;

import lombok.Data;

@Data
public class ClientAddress {

	private int number;
	private String streetAddress;
	private String city;
	private String state;
	private String zipCode;
}
