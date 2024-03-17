package com.mnrclara.api.management.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AddNotes {

	private String notesNumber;
	private Long classId;
	private String languageId;
	private Long transactionId;
	private String transactionNo;
	private Long noteTypeId;
	private String notesDescription;
	private String matterNumber;
	private Long deletionIndicator = 0L;
	private String createdBy;
	private Date createdOn = new Date();
	private String updatedBy;
	private Date updatedOn = new Date();
}
