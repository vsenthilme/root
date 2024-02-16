package com.mnrclara.api.management.model.matterdoclist;

import java.io.Serializable;

import lombok.Data;

@Data
public class MatterDocListCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `CHK_LIST_NO`, `MATTER_NO`, `CLIENT_ID`, `SEQ_NO`
	 */
	private String languageId;
	private Long classId;
	private Long checkListNo;
	private String matterNumber;
	private String clientId;
	private Long sequenceNumber;
}
