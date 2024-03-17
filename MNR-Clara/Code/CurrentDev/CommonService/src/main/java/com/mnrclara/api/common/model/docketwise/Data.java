package com.mnrclara.api.common.model.docketwise;

@lombok.Data
public class Data {
	private Long id;
	private String street_number_and_name;
	private String apartment_number;
	private String city;
	private String state;
	private String county;
	private String postal_code;
	private String province;
	private String zip_code;
	private String country;
	private boolean physical;
	private String number;
	private boolean dayTime;
}
