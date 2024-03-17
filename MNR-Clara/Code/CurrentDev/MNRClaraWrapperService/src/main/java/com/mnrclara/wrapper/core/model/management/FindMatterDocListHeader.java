package com.mnrclara.wrapper.core.model.management;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FindMatterDocListHeader {
	
	/*
	 * CLASS_ID
	 * CHK_LIST_NO
	 * MATTER_NO
	 * CLIENT_ID
	 * STATUS_ID
	 * CTD_BY
	 * CTD_ON
	 */
	private List<Long> matterHeaderId;
	private List<Long> caseCategoryId;
	private List<Long> caseSubCategoryId;
	private List<Long> classId;
	private List<Long> checkListNo;
	private List<String> matterNumber;
	private List<String> clientId;
	private List<Long> statusId;
	private List<String> createdBy;

	private Date startCreatedOn;
	private Date endCreatedOn;	
}
