package com.mnrclara.api.crm.model.itform;


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
