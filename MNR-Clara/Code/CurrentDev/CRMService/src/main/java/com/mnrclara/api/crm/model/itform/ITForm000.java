package com.mnrclara.api.crm.model.itform;

import java.util.Date;

import lombok.Data;

@Data
public class ITForm000 extends BaseITForm {
 
	// STATUS_ID
	private Long statusId;
	
	// CTD_BY
	private String createdBy;
	
	// CTD_ON
	private Date createdOn = new Date();
	
	// SENT_ON
	private Date sentOn = new Date();
}