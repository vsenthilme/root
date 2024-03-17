package com.mnrclara.wrapper.core.batch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MatterNote { 
	private String languageId;
	private Long classId;
	private String clientId;
	private String matterNumber;	
	private String notesNumber;
	private String notesDescription;
	private Long statusId;
	private Long noteTypeId;
	private Long deletionIndicator;
	private String createdBy;
    private Date createdOn = new Date();
    private String UpdatedBy;
	private Date updatedOn = new Date();
	
	public MatterNote (String languageId, Long classId, String clientId, String matterNumber, String notesNumber,  
			String notesDescription, Long statusId, Long noteTypeId, Long deletionIndicator, String createdBy, 
			Date createdOn, String UpdatedBy, Date updatedOn) {
		this.notesNumber = notesNumber;
		this.languageId = languageId;
		this.classId = classId;
		this.matterNumber = matterNumber;
		this.clientId = clientId;
		this.notesDescription = notesDescription;
		this.statusId = statusId;
		this.noteTypeId = noteTypeId;
		this.deletionIndicator = deletionIndicator;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.UpdatedBy = UpdatedBy;
		this.updatedOn = updatedOn;
	}
}
