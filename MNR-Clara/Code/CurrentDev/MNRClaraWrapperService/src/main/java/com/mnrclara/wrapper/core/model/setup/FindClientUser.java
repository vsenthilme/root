package com.mnrclara.wrapper.core.model.setup;

import lombok.Data;

import java.util.List;

@Data
public class FindClientUser {
	
	/*
	 * CLASS_ID
	 * CLIENT_USR_ID
	 * CLIENT_ID
	 * CONT_NO
	 * FIRST_LAST_NM
	 * STATUS_ID
	 * EMAIL_ID
	 * CTD_ON
	 */
	private List<Long> classId;
	//private List<String> clientUserId;
	private List<String> clientId;
	private List<String> contactNumber;
	private List<String> fullName;
	private List<String> emailId;
	
	//private Date startCreatedOn;
	//private Date endCreatedOn;
	
}
