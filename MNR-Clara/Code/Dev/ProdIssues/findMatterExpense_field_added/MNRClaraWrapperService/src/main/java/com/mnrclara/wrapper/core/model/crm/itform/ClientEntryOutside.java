package com.mnrclara.wrapper.core.model.crm.itform;


import java.util.Date;

import lombok.Data;

@Data
public class ClientEntryOutside {

	private Date dateOfEntered;
	private Date dateOfExited;
	private String place;
}
