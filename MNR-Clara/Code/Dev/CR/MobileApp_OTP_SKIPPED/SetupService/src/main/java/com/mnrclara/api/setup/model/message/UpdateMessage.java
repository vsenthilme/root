package com.mnrclara.api.setup.model.message;

import lombok.Data;

@Data
public class UpdateMessage {

	private String languageId;
	
	private Long classId;
	
	private String messageType;
	
	private String messageDescription;
	
	private Long deletionIndicator;
	
	private String updatedBy;
}
