package com.mnrclara.api.accounting.model.management;

import java.io.Serializable;

import lombok.Data;

@Data
public class MatterTimeTicketCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `MATTER_NO`, `CLIENT_ID`, `TIME_TICKET_NO`, `TK_CODE`
	 */
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private String timeTicketNumber;
	private String timeKeeperCode;
}
