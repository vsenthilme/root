package com.mnrclara.api.management.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class Dropdown {

	private List<KeyValuePair> matterList;
	private List<ClassKeyValuePair> classList;
	private List<KeyValuePair> clientNameList;
	private List<KeyValuePair> caseCategoryList;
	private List<KeyValuePair> subCaseCategoryList;
	private List<KeyValuePair> statusIdList;
}
