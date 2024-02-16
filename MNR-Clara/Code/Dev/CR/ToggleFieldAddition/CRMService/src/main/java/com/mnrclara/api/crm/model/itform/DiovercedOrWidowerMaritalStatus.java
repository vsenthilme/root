package com.mnrclara.api.crm.model.itform;


import java.util.Date;

import lombok.Data;

@Data
public class DiovercedOrWidowerMaritalStatus {

	private String nameOfExspouse;
	private String cityAndCountryOfBirth;
	private String countryOfCitizenship;
	private Date dateOfMarriage;
	private Date dateOfMarriageTermination;
	private String reasonForMarriageEnd;
}
