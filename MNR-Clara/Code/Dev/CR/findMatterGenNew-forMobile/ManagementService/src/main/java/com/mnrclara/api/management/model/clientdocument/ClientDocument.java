package com.mnrclara.api.management.model.clientdocument;

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
@Table(name = "tblclientdocumentid")
public class ClientDocument {

	@Id
	@Column(name = "CLIENT_DOC_ID")
	private Long clientDocumentId;
	
	@Column(name = "DOC_NO")
	private String documentNo;

	@Column(name = "LANG_ID")
	private String languageId;

	@Column(name = "CLASS_ID")
	private Long classId;

	@Column(name = "CLIENT_ID")
	private String clientId;

	@Column(name = "DOC_URL")
	private String documentUrl;

	@Column(name = "DOC_DATE_TIME")
	private Date documentDate = new Date();

	@Column(name = "DOC_URL_VER")
	private String documentUrlVersion;

	@Column(name = "CLIENT_USR_ID")
	private String clientUserId;

	@Column(name = "MATTER_NO")
	private String matterNumber;

	@Column(name = "STATUS_ID")
	private Long statusId;

	@Column(name = "SENT_BY")
	private String sentBy;

	@Column(name = "SENT_ON")
	private Date sentOn;

	@Column(name = "RECEIVED_ON")
	private Date receivedOn;

	@Column(name = "RESENT_ON")
	private Date resentOn;

	@Column(name = "APPROVED_ON")
	private Date approvedOn;

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
	private String UpdatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn = new Date();
}
