package com.mnrclara.api.setup.model.message;

import lombok.Data;

@Data
public class AddMessage {

    private String languageId;
	
	private Long classId;
	
	private Long messageId;
	
	private String messageType;
	
	private String messageDescription;
	
	private Long deletionIndicator;

	private String createdBy;
}
