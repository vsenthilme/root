package com.mnrclara.api.crm.model.itform;


import java.util.Date;

import lombok.Data;

@Data
public class ClientEntryOutside {

	private Date dateOfEntered;
	private Date dateOfExited;
	private String place;
}
