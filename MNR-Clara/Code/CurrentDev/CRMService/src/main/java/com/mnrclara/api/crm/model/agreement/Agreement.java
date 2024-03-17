package com.mnrclara.api.crm.model.agreement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mnrclara.api.crm.repository.AgreementCompositeKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@IdClass(AgreementCompositeKey.class)
@Table(
		name = "tblagreementid", 
		uniqueConstraints = { 
				@UniqueConstraint(name = "unique_key_agreement", 
						columnNames = { "AGREEMENT_CODE", 
										"POT_CLIENT_ID", 
										"INQ_NO", 
										"AGREEMENT_URL", 
										"AGREEMENT_URL_VER",
										"LANG_ID",
										"CLASS_ID"}
						) 
				}
		)
public class Agreement {

	@Id
	@Column(name = "AGREEMENT_CODE")
	private String agreementCode;
	
	@Id
	@Column(name = "POT_CLIENT_ID")
	private String potentialClientId;
	
	@Id
	@Column(name = "INQ_NO")
	private String inquiryNumber;
	
	@Id
	@Column(name = "AGREEMENT_URL")
	private String agreementURL;
	
	@Id
	@Column(name = "AGREEMENT_URL_VER")
	private String agreementURLVersion;
	
	@Id
	@Column(name = "LANG_ID")
	private String languageId = "EN";
	
	@Id
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Column(name = "CLIENT_ID")
	private String clientId;
	
	@Column(name = "CASE_CATEGORY_ID")
	private Long caseCategoryId;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
	
	@Column(name = "TRANS_ID")
	private Long transactionId;
	
	@Column(name = "EMail_ID")
	private String emailId;
	
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
    private Date createdOn;

	@Column(name = "UTD_BY")
    private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn;
}
