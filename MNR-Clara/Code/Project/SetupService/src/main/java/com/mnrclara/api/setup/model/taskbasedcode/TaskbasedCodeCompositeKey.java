package com.mnrclara.api.setup.model.taskbasedcode;

import java.io.Serializable;

import lombok.Data;

@Data
public class TaskbasedCodeCompositeKey implements Serializable {

	private static final long serialVersionUID = -7617672247680004647L;
	
	/*
	 * `LANG_ID`, `CLASS_ID`, `TASK_CODE`
	 */
	private String languageId;
	private Long classId;
	private String taskCode;
}
