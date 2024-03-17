package com.mnrclara.wrapper.core.model.management;

import java.util.Date;

import lombok.Data;

@Data
public class SearchCaseSheetParams {

	/**
	 * CLIENT_ID FIRST_LAST_NM CASEINFO_NO MATTER_NO CTD_BY CTD_ON
	 * STATUS_ID/STATUS_TEXT
	 * ----------------------------
	 */
	private String clientId;
	private String firstNameLastName;
	private String caseInformationId;
	private String matterNumber;
	private Long statusId;
	private String createdBy;
	private Date startCreatedOn;
	private Date endCreatedOn;
}
