package com.mnrclara.api.management.model.matteritform;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

/*
 * `LANG_ID`, `CLASS_ID`,`MATTER_NO`, `CLIENT_ID`,  `IT_FORM_ID`,  `IT_FORM_NO`
 */
@Table(
		name = "tblmatteritformid", 
		uniqueConstraints = { 
				@UniqueConstraint(name = "unique_key_matteritform", 
						columnNames = { 
										"LANG_ID",
										"CLASS_ID",
										"MATTER_NO",
										"CLIENT_ID",
										"IT_FORM_ID",
										"IT_FORM_NO"
									}
						) 
				}
		)
@IdClass(MatterITFormCompositeKey.class)
public class MatterITForm { 
	
	@Id
	@Column(name = "IT_FORM_ID")
	private Long intakeFormId;
	
	@Id
	@Column(name = "LANG_ID")
	private String languageId;
	
	@Id
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Id
	@Column(name = "MATTER_NO")
	private String matterNumber;
	
	@Id
	@Column(name = "CLIENT_ID")
	private String clientId;
	
	@Id
	@Column(name = "IT_FORM_NO")
	private String intakeFormNumber;
	
	@Column(name = "CLIENT_USR_ID")
	private String clientUserId;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
	
	@Column(name = "NOTE_NO")
	private String notesNumber;
	
	@Column(name = "SENT_ON")
	private Date sentOn;
	
	@Column(name = "RECEIVED_ON")
	private Date receivedOn;
	
	@Column(name = "RESENT_ON")
	private Date resentOn;
	
	@Column(name = "APPROVED_ON")
	private Date approvedOn;
	
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
    private Date createdOn;

	@Column(name = "UTD_BY")
    private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn;
}
