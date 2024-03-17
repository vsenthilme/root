package com.mnrclara.wrapper.core.model.management;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchClientDocument {
	/*
	 * DOC_NO
	 * DOC_TEXT --> Removed from FS
	 * SENT_BY
	 * STATUS_ID/STATUS_TEXT
	 * MATTER_NO
	 * SENT_ON
	 * RECEIVED_ON
	 * ------------------
	 * multi search
	 * ------------
	 * Number (DOC_NO)
	 * Status
	 * Case No (MATTER_NO)
	 */
	private List<String> documentNo;	
	private List<String> matterNumber;
	private List<Long> statusId;
	
	private String sentBy;
	private Date sSentOn;
	private Date eSentOn;
	private Date sReceivedOn;
	private Date eReceivedOn;
}
