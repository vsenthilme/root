package com.mnrclara.api.setup.model.userrole;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)

// `LANG_ID`, `USR_ROLE_ID`, `SCREEN_ID`, `SUB_SCREEN_ID`
@Table(
	name = "tbluserroleid", 
	uniqueConstraints = { 
		@UniqueConstraint (
			name = "unique_key_storagebin", 
			columnNames = {"LANG_ID", "USR_ROLE_ID", "SCREEN_ID", "SUB_SCREEN_ID"})
		}
	)
@IdClass(UserRoleCompositeKey.class)
public class UserRole { 
	
	@Id
	@Column(name = "USR_ROLE_ID")
	private Long userRoleId;
	
	@Id
	@Column(name = "LANG_ID")
	private String languageId;
	
	@Id
	@Column(name = "SCREEN_ID")
	private Long screenId;
	
	@Id
	@Column(name = "SUB_SCREEN_ID")
	private Long subScreenId;
	
	@Column(name = "USR_ROLE_NM")
	private String userRoleName;

	@Column(name = "CREATE_UPDATE")
	private Boolean createUpdate;
	
	@Column(name = "`VIEW`")
	private Boolean view;
	
	@Column(name = "`DELETE`")
	private Boolean delete;
	
	@Column(name = "ROLE_STATUS")
	private Boolean roleStatus;
	
	@Column(name = "IS_DELETED")
	private Long deletionIndicator;

	@Column(name = "REF_FIELD_1")
	private String referenceField1;
	
	@Column(name = "REF_FIELD_2")
	private String referenceField2;
	
	@Column(name = "REF_FIELD_3")
	private String referenceField3;
	
	@Column(name = "REF_FIELD_4")
	private String referenceField4;
	
	@Column(name = "REF_FIELD_5")
	private String referenceField5;
	
	@Column(name = "REF_FIELD_6")
	private String referenceField6;
	
	@Column(name = "REF_FIELD_7")
	private String referenceField7;
	
	@Column(name = "REF_FIELD_8")
	private String referenceField8;
	
	@Column(name = "REF_FIELD_9")
	private String referenceField9;
	
	@Column(name = "REF_FIELD_10")
	private String referenceField10;

	@Column(name = "CTD_BY")
	private String createdBy;

	@Column(name = "CTD_ON")
    private Date createdOn = new Date();

	@Column(name = "UTD_BY")
    private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
}
