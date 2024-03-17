package com.mnrclara.api.management.model.clientnote;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblclientnoteid")
public class ClientNote {

	@Id
	@Column(name = "NOTE_NO")
	private String notesNumber;

	@Column(name = "LANG_ID")
	private String languageId;

	@Column(name = "CLASS_ID")
	private Long classId;

	@Column(name = "CLIENT_ID")
	private String clientId;

	@Column(name = "NOTE_TEXT")
	private String noteText;

	@Column(name = "MATTER_NO")
	private String matterNumber;

	@Column(name = "NOTE_TYP_ID")
	private Long noteTypeId;

	@Column(name = "STATUS_ID")
	private Long statusId;

	@Column(name = "IS_DELETED")
	private Long deletionIndicator = 0L;

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
	
	/*-------------------ADD_FIELDS------------------------------*/
	@Column(name = "ADD_FIELDS_1")
	private String additionalFields1;
	
	@Column(name = "ADD_FIELDS_2")
	private String additionalFields2;
	
	@Column(name = "ADD_FIELDS_3")
	private String additionalFields3;
	
	@Column(name = "ADD_FIELDS_4")
	private String additionalFields4;
	
	@Column(name = "ADD_FIELDS_5")
	private String additionalFields5;
	
	@Column(name = "ADD_FIELDS_6")
	private String additionalFields6;
	
	@Column(name = "ADD_FIELDS_7")
	private String additionalFields7;
}
