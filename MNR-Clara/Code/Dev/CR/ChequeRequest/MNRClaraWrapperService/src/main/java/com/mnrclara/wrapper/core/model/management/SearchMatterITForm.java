package com.mnrclara.wrapper.core.model.management;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchMatterITForm {
	/*
	 * IT_FORM_NO				- Multiple Search field values
	 * IT_FORM_ID/IT_FORM_TEXT 	- Multiple Search field values
	 * MATTER_NO				- Multiple Search field values
	 * STATUS_ID/STATUS_TEXT	- Multiple Search field values
	 * SENT_ON
	 * RECEIVED_ON
	 * APPROVED_ON
	 */
	private List<String> intakeFormNumber;;
	private List<Long> intakeFormId;
	private List<String> matterNumber;
	private List<Long> statusId;
	
	private Date sSentOn;
	private Date eSentOn;
	private Date sReceivedOn;
	private Date eReceivedOn;
	private Date sApprovedOn;
	private Date eApprovedOn;
}
