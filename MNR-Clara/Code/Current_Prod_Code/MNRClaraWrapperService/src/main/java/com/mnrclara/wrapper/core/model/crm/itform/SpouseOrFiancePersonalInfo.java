package com.mnrclara.wrapper.core.model.crm.itform;


import java.util.Date;

import lombok.Data;

@Data
public class SpouseOrFiancePersonalInfo extends ClientPersonalInfo {

	private Date dateOfMarriage;
	private String placeOfMarriage;
	private String placeOfResidence;
}
