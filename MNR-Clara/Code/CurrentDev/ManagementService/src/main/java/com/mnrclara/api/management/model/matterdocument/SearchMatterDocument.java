package com.mnrclara.api.management.model.matterdocument;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchMatterDocument {
	/*
	 * DOC_NO
	 * SENT_BY
	 * STATUS_ID/STATUS_TEXT
	 * SENT_ON
	 * RECEIVED_ON
	 * ----------------------
	 * multisearch
	 * ============
	 * DOC_NO
	 * Sent by
	 * Status
	 */
	private List<String> documentNo;
	private List<String> sentBy;
	private List<Long> statusId;
	private List<Long> classId;
	private List<String> matterNumber;
	private List<String> clientId;
	
	private Date sSentOn;
	private Date eSentOn;
	private Date sReceivedOn;
	private Date eReceivedOn;
}
