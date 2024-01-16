package com.mnrclara.api.management.model.matteritform;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class BaseMatterITForm {
	
	@JsonIgnore
	@Id
    private String id;
    
	/*
	 * Unique Key
	 * --------------
	 * LANG_ID, CLASS_ID, MATTER_NO, CLIENT_ID, IT_FORM_NO, IT_FORM_ID
	 */
	private String language;
	private Long classID;
	private String matterNumber;
    private String clientId;
    private String itFormNo;
	private Long itFormID;
	
	public String getId () {
		this.id = language + ":" + classID + ":" + matterNumber + ":" + clientId + ":" + itFormNo + ":" + itFormID;
		return this.id;
	}
}