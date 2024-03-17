package com.mnrclara.api.management.model.matterdoclist;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchMatterDocList {
	
	/*
	 * CLASS_ID
	 * CHK_LIST_NO
	 * MATTER_NO
	 * CLIENT_ID
	 * STATUS_ID
	 * CTD_BY
	 * CTD_ON
	 */
	 
	private List<Long> classId;
	private List<Long> checkListNo;
	private List<String> matterNumber;
	private List<String> clientId;
	private List<Long> statusId;
	private List<String> createdBy;

	private Date startCreatedOn;
	private Date endCreatedOn;	
}
