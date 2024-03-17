package com.mnrclara.api.management.model.clientgeneral;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchClientGeneral {
	/*
	 * CLIENT_ID, 
	 * FIRST_LAST_NM, 
	 * EMAIL_ID, 
	 * CONT_NO, 
	 * ADDRESS_LINE1, 
	 * IT_FORM_NO,
	 * CTD_ON 
	 * STATUS_ID/STATUS_TEXT
	 * ---------------------------------
	 * 
	 * multi search
	 * --------------
	 * Client ID
	 * IT_FORM_NO
	 * STATUS_ID/STATUS_TEXT
	 */
	private List<String> clientId;
	private List<String> intakeFormNumber;
	private List<Long> statusId;
	
	private String firstNameLastName;
	private String emailId;
	private String contactNumber;
	private String addressLine1;
	private Date startCreatedOn;
	private Date endCreatedOn;

	private List<Long> classId;
}
