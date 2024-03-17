package com.mnrclara.wrapper.core.model.report;

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
