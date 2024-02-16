package com.mnrclara.wrapper.core.model.management;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchMatterNote {
	/*
	 * NOTE_NO
	 * NOTE_TYP_ID/NOTE_TYP_TEXT
	 * CTD_ON
	 * STATUS_ID/STATUS_TEXT
	 * --------------------------
	 * multisearch
	 * -----------
	 * Note Number
	 * Type
	 * Status 
	 */
	private List<String> notesNumber;
	private List<Long> noteTypeId;
	private List<Long> statusId;
	
	private Date startCreatedOn;
	private Date endCreatedOn;
}
