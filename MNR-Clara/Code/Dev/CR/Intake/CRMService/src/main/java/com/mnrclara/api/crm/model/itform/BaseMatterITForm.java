package com.mnrclara.api.crm.model.itform;

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