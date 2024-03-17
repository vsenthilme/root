package com.mnrclara.wrapper.core.model.crm.itform;

import java.util.Date;

import lombok.Data;

@Data
public class MatterITForm000 extends BaseMatterITForm {
 
	// STATUS_ID
	private Long statusId;
	
	// CTD_BY
	private String createdBy;
	
	// CTD_ON
	private Date createdOn = new Date();
	
	// UTD_BY
	private String updatedBy;
	
	// UTD_ON
	private Date updatedOn = new Date();
	
	// SENT_ON
	private Date sentOn = new Date();

}