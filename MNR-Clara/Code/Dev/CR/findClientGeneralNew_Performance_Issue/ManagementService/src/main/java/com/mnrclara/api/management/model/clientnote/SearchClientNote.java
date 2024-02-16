package com.mnrclara.api.management.model.clientnote;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchClientNote {

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
	private List<String> notesNumber;
	private List<String> matterNumber;
	private List<Long> noteTypeId;
	private List<Long> statusId;
	
	private String noteText;
	private Date startCreatedOn;
	private Date endCreatedOn;
	
	private Long deletionIndicator = 0L;	
}
