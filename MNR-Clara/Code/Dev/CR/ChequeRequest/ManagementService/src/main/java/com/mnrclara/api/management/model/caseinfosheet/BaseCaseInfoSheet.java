package com.mnrclara.api.management.model.caseinfosheet;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class BaseCaseInfoSheet {

	@Id
	private String id;

	public String getId() {
//		this.id = languageId + ":" + classId + ":" + caseInformationId + ":" + clientId;
		return this.id;
	}
}