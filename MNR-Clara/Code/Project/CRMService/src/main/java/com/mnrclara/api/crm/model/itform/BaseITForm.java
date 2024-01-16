package com.mnrclara.api.crm.model.itform;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class BaseITForm {
	
	@JsonIgnore
	@Id
    private String id;
    
    private String inquiryNo;
    private Long classID;
	private String language;
    private String itFormNo;
	private Long itFormID;
	
	public String getId () {
		log.info(inquiryNo + ":" + classID + ":" + language + ":" + itFormNo + ":" + itFormID);
		this.id = inquiryNo + ":" + classID + ":" + language + ":" + itFormNo + ":" + itFormID;
		return this.id;
	}
	
	public String getId (String uk) {
		log.info(inquiryNo + ":" + classID + ":" + language + ":" + itFormNo + ":" + itFormID + ":" + uk);
		this.id = inquiryNo + ":" + classID + ":" + language + ":" + itFormNo + ":" + itFormID + ":" + uk;
		return this.id;
	}
}