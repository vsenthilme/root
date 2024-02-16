package com.mnrclara.api.management.model.clientemail;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblclientemailid")
public class ClientEmail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "EMAIL_ID")
	private Long emailId;

	@Column(name = "EMAIL_DATE_TIME")
	private Date emailDateTime = new Date();

	@Column(name = "LANG_ID")
	private String languageId;

	@Column(name = "CLASS_ID")
	private Long classId;

	@Column(name = "CLIENT_ID")
	private String clientId;

	@Column(name = "MATTER_NO")
	private String matterNumber;

	@Column(name = "MAIL_TYP")
	private String mailType;

	@Column(name = "FROM_ADD")
	private String fromMailId;

	@Column(name = "TO_ADD")
	private String toMailId;

	@Column(name = "SUBJECT")
	private String subject;

	@Column(name = "CC")
	private String cc;

//	@Column(name = "BCC")
//	private String bcc;

	@Column(name = "BODY")
	private String body;

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
	private String UpdatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
}
