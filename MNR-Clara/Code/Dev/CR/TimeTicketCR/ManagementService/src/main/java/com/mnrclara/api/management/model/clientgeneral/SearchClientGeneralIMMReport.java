package com.mnrclara.api.management.model.clientgeneral;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchClientGeneralIMMReport {
	/*
	 * CLASS_ID
	 * CLIENT_ID
	 * REFERRAL_ID
	 * CTD_ON
	 * STATUS_ID
	 * CLIENT_CAT_ID
	 * REF_FIELD_9
	 * REF_FIELD_4
	 */
	private Long classId;
	private List<String> clientId;
	private List<Long> referralId;
	private Date fromCreatedOn;
	private Date toCreatedOn;
	private List<Long> statusId;
	private List<Long> clientCategoryId;
	private Date fromDateClosed; 			// REF_FIELD_4
	private Date toDateClosed;				// REF_FIELD_4
	private List<String> docketwiseId;		// REF_FIELD_9	
	
}
