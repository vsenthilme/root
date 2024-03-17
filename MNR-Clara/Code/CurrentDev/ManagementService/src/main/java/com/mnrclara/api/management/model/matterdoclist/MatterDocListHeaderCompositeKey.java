package com.mnrclara.api.management.model.matterdoclist;

import lombok.Data;

import java.io.Serializable;

@Data
public class MatterDocListHeaderCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `CHK_LIST_NO`, `MATTER_NO`, `CLIENT_ID`
	 */
	private String languageId;
	private Long classId;
	private Long checkListNo;
	private String matterNumber;
	private String clientId;
}
