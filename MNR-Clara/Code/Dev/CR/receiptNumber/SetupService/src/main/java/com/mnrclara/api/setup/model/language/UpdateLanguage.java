package com.mnrclara.api.setup.model.language;

import lombok.Data;

@Data
public class UpdateLanguage {
	
	private Long classId;
	
	private String languageDescription;
	
	private Long deletionIndicator;

	private String updatedBy;
}
