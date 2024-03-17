package com.mnrclara.wrapper.core.model.report;

import lombok.Data;

@Data
public class ReferralReport {
	private String classId;					// POTENTIALCLIENT
	private String potentialClientId;		// POTENTIALCLIENT
	private Long referralId;				// POTENTIALCLIENT
	private String referralDesc;				// POTENTIALCLIENT
	private String emailId;					// POTENTIALCLIENT
	
	private String inquiryFirstNameLastName;	// INQUIRY
	
	private String clientId;				// CLIENTGENERAL
	private String clientFirstNameLastName;	// CLIENTGENERAL
}
