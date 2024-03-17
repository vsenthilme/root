package com.mnrclara.api.accounting.model.quotation;

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
 * `QUTOE_NO` , `QUOTE_REV_NO`
 */
@Table(
		name = "tblquotationheader", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_quotationheader", 
						columnNames = { "QUOTE_NO" , "QUOTE_REV_NO"})
				}
		)
@IdClass(QuotationHeaderCompositeKey.class)
public class QuotationHeaderEntity { 
	
	@Id
	@Column(name = "QUOTE_NO") 
	private String quotationNo;	
	
	@Id
	@Column(name = "QUOTE_REV_NO") 
	private Long quotationRevisionNo;

	@Column(name = "LANG_ID") 
	private String languageId;
	
	@Column(name = "CLASS_ID")
	private Long classId;
	
	@Column(name = "MATTER_NO") 
	private String matterNumber;
	
	@Column(name = "CLIENT_ID") 
	private String clientId;
	
	@Column(name = "CASE_CATEGORY_ID") 
	private Long caseCategoryId;
	
	@Column(name = "CASE_SUB_CATEGORY_ID") 
	private Long caseSubCategoryId;	
	
	@Column(name = "FIRST_LAST_NM") 
	private String firstNameLastName;
	
	@Column(name = "CORPORATION") 
	private String corporation;
	
	@Column(name = "QUOTE_DATE") 
	private Date quotationDate;
	
	@Column(name = "QUOTE_AMT") 
	private Double quotationAmount;
	
	@Column(name = "CURRENCY") 
	private String currency;
	
	@Column(name = "DUE_DATE") 
	private Date dueDate;
	
	@Column(name = "PAYMENT_PLAN_NO") 
	private String paymentPlanNumber;
	
	@Column(name = "TERM_TEXT") 
	private String termDetails;
	
	@Column(name = "SENT_ON")
	private Date sentDate;
	
	@Column(name = "APPROVED_ON") 
	private Date approvedDate;
	
	@Column(name = "STATUS_ID") 
	private Long statusId;
	
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
