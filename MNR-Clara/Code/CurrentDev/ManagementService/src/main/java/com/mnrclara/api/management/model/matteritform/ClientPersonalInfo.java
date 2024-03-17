package com.mnrclara.api.management.model.matteritform;


import java.util.Date;

import lombok.Data;

@Data
public class ClientPersonalInfo {

	private String fullName;
	private String otherNamesUsed;
	private Date dateOfBirth;
	private String cityAndCountryOfBirth;
	private String countryOfCitizenship;
}
