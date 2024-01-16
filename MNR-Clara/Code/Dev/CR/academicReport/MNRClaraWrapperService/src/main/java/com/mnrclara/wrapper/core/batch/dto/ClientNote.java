package com.mnrclara.wrapper.core.batch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ClientNote { 
	
	private String notesNumber;
	private String languageId;
	private Long classId;
	private String clientId;
	private String noteText;
	private String matterNumber;
	private Long noteTypeId;
	private Long statusId;
	private Long deletionIndicator = 0L;
	private String referenceField1;
	private String referenceField2;
	private String referenceField3;
	private String referenceField4;
	private String referenceField5;
	private String referenceField6;
	private String referenceField7;
	private String referenceField8;
	private String referenceField9;
	private String referenceField10;
	private String createdBy;
	private Date createdOn = new Date();
	private String updatedBy;
	private Date updatedOn = new Date();
	private String additionalFields1;
	private String additionalFields2;
	private String additionalFields3;
	private String additionalFields4;
	private String additionalFields5;
	private String additionalFields6;
	private String additionalFields7;
	
	/**
	 * 
	* @param notesNumber
	* @param languageId
	* @param classId
	* @param clientId
	* @param noteText
	* @param matterNumber
	* @param noteTypeId
	* @param statusId
	* @param deletionIndicator
	* @param referenceField1
	* @param referenceField2
	* @param referenceField3
	* @param referenceField4
	* @param referenceField5
	* @param referenceField6
	* @param referenceField7
	* @param referenceField8
	* @param referenceField9
	* @param referenceField10
	* @param createdBy
	* @param createdOn
	* @param updatedBy
	* @param updatedOn
	* @param additionalFields1
	* @param additionalFields2
	* @param additionalFields3
	* @param additionalFields4
	* @param additionalFields5
	* @param additionalFields6
	* @param additionalFields7
	 */
	 
	public ClientNote (String notesNumber, String languageId, Long classId, String clientId, String noteText, String matterNumber, 
			Long noteTypeId, Long statusId, Long deletionIndicator, String referenceField1, String referenceField2, 
			String referenceField3, String referenceField4, String referenceField5, String referenceField6, String referenceField7, 
			String referenceField8, String referenceField9, String referenceField10, String createdBy, Date createdOn, String updatedBy, 
			Date updatedOn, String additionalFields1, String additionalFields2, String additionalFields3, String additionalFields4, 
			String additionalFields5, String additionalFields6, String additionalFields7) {
		this.notesNumber = notesNumber;
		this.languageId = languageId;
		this.classId = classId;
		this.clientId = clientId;
		this.noteText = noteText;
		this.matterNumber = matterNumber;
		this.noteTypeId = noteTypeId;
		this.statusId = statusId;
		this.deletionIndicator = deletionIndicator;
		this.referenceField1 = referenceField1;
		this.referenceField2 = referenceField2;
		this.referenceField3 = referenceField3;
		this.referenceField4 = referenceField4;
		this.referenceField5 = referenceField5;
		this.referenceField6 = referenceField6;
		this.referenceField7 = referenceField7;
		this.referenceField8 = referenceField8;
		this.referenceField9 = referenceField9;
		this.referenceField10 = referenceField10;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
		this.additionalFields1 = additionalFields1;
		this.additionalFields2 = additionalFields2;
		this.additionalFields3 = additionalFields3;
		this.additionalFields4 = additionalFields4;
		this.additionalFields5 = additionalFields5;
		this.additionalFields6 = additionalFields6;
		this.additionalFields7 = additionalFields7;
	}
}
