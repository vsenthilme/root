package com.mnrclara.api.setup.model.userrole;

import lombok.Data;

@Data
public class UpdateUserRole {

	private String languageId;
	private Long screenId;
	private Long subScreenId;
	private String userRoleName;
	private Boolean createUpdate;
	private Boolean view;
	private Boolean delete;
	private Boolean roleStatus;
	private Long deletionIndicator;
	private String referenceField1;
	private String referenceField2;
	private String referenceField3;
	private String referenceField4;
	private String referenceField5;
	private String referenceField6;
	private String referenceField7;
	private String referenceField8;
	private String referenceField9;
	private String referenceField10;
	private String updatedBy;
}
