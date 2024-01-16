package com.mnrclara.api.crm.model.potentialclient;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchReferralReport {
	
	/*
	 * CLASS_ID
	 * REFERRAL_ID
	 * CTD_ON
	 */
	private Long classId;
	private List<Long> referralId;	
	private Date fromCreatedOn;
	private Date toCreatedOn;
}
