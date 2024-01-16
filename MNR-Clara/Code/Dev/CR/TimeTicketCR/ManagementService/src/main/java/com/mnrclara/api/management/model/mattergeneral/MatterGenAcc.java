package com.mnrclara.api.management.model.mattergeneral;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblmattergenaccid")
public class MatterGenAcc {

	@Id
	@Column(name = "MATTER_NO")
	private String matterNumber;
	
	@JsonIgnore
	@Column(name = "SENT_TO_QB")
	private Long sentToQB = 0L;
	
	@JsonIgnore
	@Column(name = "SENT_TO_DKTW")
	private Long sentToDocketwise = 0L;
	
	@Column(name = "LANG_ID")
	private String languageId;

	@Column(name = "CLASS_ID")
	private Long classId;

	@Column(name = "CASEINFO_NO")
	private String caseInformationNo;

	@Column(name = "CLIENT_ID")
	private String clientId;

	@Column(name = "TRANS_ID")
	private Long transactionId;

	@Column(name = "CASE_CATEGORY_ID")
	private Long caseCategoryId;

	@Column(name = "CASE_SUB_CATEGORY_ID")
	private Long caseSubCategoryId;

	@Column(name = "MATTER_TEXT")
	private String matterDescription;

	@Column(name = "FILE_NO")
	private String fileNumber;

	@Column(name = "CASE_FILE_NO")
	private Long caseFileNumber;

	@Column(name = "CASE_OPEN_DATE")
	private Date caseOpenedDate;

	@Column(name = "CASE_CLOSED_DATE")
	private Date caseClosedDate;

	@Column(name = "CASE_FILED_DATE")
	private Date caseFiledDate;

	@Column(name = "PRIORITY_DATE")
	private Date priorityDate;

	@Column(name = "RECEIPT_NOT_NO")
	private String receiptNoticeNo;

	@Column(name = "RECEIPT_DATE")
	private Date receiptDate;

	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;

	@Column(name = "COURT_DATE")
	private Date courtDate;

	@Column(name = "APPROVAL_DATE")
	private Date approvalDate;

	@Column(name = "BILL_MODE_ID")
	private String billingModeId;

	@Column(name = "BILL_FREQ_ID")
	private String billingFrequencyId;

	@Column(name = "BILL_FORMAT_ID")
	private String billingFormatId;

	@Column(name = "BILL_REMARK")
	private String billingRemarks;

	@Column(name = "AR_ACCOUNT_NO")
	private String arAccountNumber;

	@Column(name = "TRUST_DEPOSIT_NO")
	private String trustDepositNo;

	@Column(name = "FLAT_FEE")
	private Double flatFeeAmount;
	
	@Column(name = "REMAIN_AMOUNT")
	private Double remainingAmount;

	@Column(name = "ADMIN_COST")
	private Double administrativeCost;

	@Column(name = "CONTIG_FEE")
	private Double contigencyFeeAmount;

	@Column(name = "RATE_UNIT")
	private String rateUnit;

	@Column(name = "DIR_PHONE_NO")
	private String directPhoneNumber;

	@Column(name = "STATUS_ID")
	private Long statusId;

	@Column(name = "ACCOUNTANT_NAME")
	private String accountantName;

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

	@Column(name = "REF_FIELD_11")
	private String referenceField11;

	@Column(name = "REF_FIELD_12")
	private String referenceField12;

	@Column(name = "REF_FIELD_13")
	private String referenceField13;

	@Column(name = "REF_FIELD_14")
	private String referenceField14;

	@Column(name = "REF_FIELD_15")
	private String referenceField15;

	@Column(name = "REF_FIELD_16")
	private String referenceField16;

	@Column(name = "REF_FIELD_17")
	private String referenceField17;

	@Column(name = "REF_FIELD_18")
	private String referenceField18;

	@Column(name = "REF_FIELD_19")
	private String referenceField19;

	@Column(name = "REF_FIELD_20")
	private String referenceField20;
	
	@Column(name = "REF_FIELD_21")
	private String referenceField21;
	
	@Column(name = "REF_FIELD_22")
	private String referenceField22;
	
	@Column(name = "REF_FIELD_23")
	private String referenceField23;
	
	@Column(name = "REF_FIELD_24")
	private String referenceField24;	
	
	@Column(name = "REF_FIELD_25")
	private String referenceField25;
	
	@Column(name = "REF_FIELD_26")
	private String referenceField26;	// Client Name
	
	@Column(name = "REF_FIELD_27")
	private String referenceField27;	// Class
	
	@Column(name = "REF_FIELD_28")
	private String referenceField28;	// CaseCategory
	
	//-----------------------For Docketwise----------------------------------------------------
	@Column(name = "REF_FIELD_29")
	private String referenceField29;
	
	@Column(name = "REF_FIELD_30")
	private String referenceField30;
	
	@Column(name = "CTD_BY")
	private String createdBy;

	@Column(name = "CTD_ON")
	private Date createdOn;

	@Column(name = "UTD_BY")
	private String updatedBy;

	@Column(name = "UTD_ON")
	private Date updatedOn;
}
