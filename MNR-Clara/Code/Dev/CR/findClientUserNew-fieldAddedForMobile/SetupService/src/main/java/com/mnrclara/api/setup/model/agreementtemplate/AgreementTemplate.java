package com.mnrclara.api.setup.model.agreementtemplate;

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
/*
 * `LANG_ID`, `CLASS_ID`, `CASE_CATEGORY_ID`, `AGREEMENT_CODE`, `AGREEMENT_URL`
 */
@Table(name = "tblagreementtemplateid")
public class AgreementTemplate { 
	
	@Id
	@Column(name = "AGREEMENT_CODE")
	private String agreementCode;
	
	@Column(name = "LANG_ID")
	private String languageId;
	
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Column(name = "CASE_CATEGORY_ID")
	private Long caseCategoryId;
	
	@Column(name = "AGREEMENT_URL")
	private String agreementUrl;
	
	@Column(name = "AGREEMENT_TEXT")
	private String agreementFileDescription;
	
	@Column(name = "AGREEMENT_STATUS")
	private String agreementStatus;
	
	@Column(name = "MAIL_MERGE")
	private Boolean mailMerge;
	
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
