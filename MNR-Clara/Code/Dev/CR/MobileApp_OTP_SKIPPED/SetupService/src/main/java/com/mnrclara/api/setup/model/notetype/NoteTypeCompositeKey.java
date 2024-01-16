package com.mnrclara.api.setup.model.notetype;

import java.io.Serializable;

import lombok.Data;

@Data
public class NoteTypeCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `NOTE_TYP_ID`
	 */
	private String languageId;
	private Long classId;
	private Long noteTypeId;
}
