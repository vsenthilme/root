package com.mnrclara.api.management.model.expirationdate;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SearchExpirationDate {

	/*
	 * NOTE_NO, NOTE_TYP_ID/NOTE_TYP_TEXT, NOTE_TEXT, MATTER_NO, CTD_ON,
	 * STATUS_ID/STATUS_TEXT
	 * ------------------------
	 * multi search
	 * ~~~~~~~~~~~~~~~
	 * Note Number
	 * Type
	 * Case No (matterNumber)
	 * Status 
	 */
	private List<String> matterNumber;
	private List<String> documentType;
	private List<Long> classId;
	private List<String> clientId;

	private Date creationFromDate;
	private Date creationToDate;
	private Date expirationFromDate;
	private Date expirationToDate;
}
