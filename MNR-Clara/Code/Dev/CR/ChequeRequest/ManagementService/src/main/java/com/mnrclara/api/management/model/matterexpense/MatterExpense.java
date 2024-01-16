package com.mnrclara.api.management.model.matterexpense;

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
@Table(name = "tblmatterexpenseid")
public class MatterExpense { 
	
	@Id
	@Column(name = "MATTER_EXP_ID") 
	private Long matterExpenseId;
	
	@Column(name = "EXP_CODE") 
	private String expenseCode;
	
	@Column(name = "LANG_ID") 
	private String languageId;
	
	@Column(name = "CLASS_ID") 
	private Long classId;
	
	@Column(name = "MATTER_NO") 
	private String matterNumber;
	
	@Column(name = "CASEINFO_NO")
	private String caseInformationNo;
	
	@Column(name = "CLIENT_ID") 
	private String clientId;	
	
	@Column(name = "CASE_CATEGORY_ID") 
	private Long caseCategoryId;
	
	@Column(name = "CASE_SUB_CATEGORY_ID") 
	private Long caseSubCategoryId;
	
	@Column(name = "COST_ITEM")
	private Double costPerItem;
	
	@Column(name = "NO_ITEMS") 
	private Double numberofItems;
	
	@Column(name = "EXP_AMOUNT") 
	private Double expenseAmount;
	
	@Column(name = "RATE_UNIT") 
	private String rateUnit;
	
	@Column(name = "EXP_TEXT") 
	private String expenseDescription;
	
	@Column(name = "EXP_TYPE") 
	private String expenseType;
	
	@Column(name = "BILL_TYPE") 
	private String billType;
	
	@Column(name = "WRITE_OFF") 
	private Boolean writeOff;
	
	@Column(name = "EXP_ACCOUNT_NO") 
	private String expenseAccountNumber;
	
	@Column(name = "STATUS_ID") 
	private Long statusId;
	
	@Column(name = "IS_DELETED") 
	private Long deletionIndicator = 0L;
	
	@Column(name = "REF_FIELD_1") 
	private String referenceField1;
	
	@Column(name = "REF_FIELD_2") 
	private Date referenceField2;
	
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

//Check and Credit Request Form Fields

	@Column(name = "PAYABLE_TO", columnDefinition = "nvarchar(255)")
	private String payableTo;

	@Column(name = "VENDOR_MAILING_ADDRESS", columnDefinition = "nvarchar(500)")
	private String vendorMailingAddress;

	@Column(name = "VENDOR_PHONE", columnDefinition = "nvarchar(255)")
	private String vendorPhone;

	@Column(name = "VENDOR_FAX", columnDefinition = "nvarchar(255)")
	private String vendorFax;

	@Column(name = "VENDOR_NOTES", columnDefinition = "nvarchar(5000)")
	private String vendorNotes;

	@Column(name = "BILLABLE_TO_CLIENT", columnDefinition = "nvarchar(255)")
	private String billableToClient;

	@Column(name = "CARD_NOT_ACCEPTED", columnDefinition = "nvarchar(255)")
	private String cardNotAccepted;

	@Column(name = "PAYMENT_MODE", columnDefinition = "nvarchar(255)")
	private String paymentMode;

	@Column(name = "USER_NAME", columnDefinition = "nvarchar(255)")
	private String userName;

	@Column(name = "QB_DEPT", columnDefinition = "nvarchar(255)")
	private String qbDepartment;

	@Column(name = "DOCUMENT_NAME", columnDefinition = "nvarchar(255)")
	private String documentName;

	@Column(name = "PHYSICAL_CARD", columnDefinition = "nvarchar(100)")
	private String physicalCard;

	@Column(name = "CHECK_REQUEST_STATUS")
	private Long checkRequestStatus;

	@Column(name = "REQUIRED_DATE")
	private Date requiredDate;

	//Additional Fields added as requested by user

	@Column(name = "NAME_ON_CARD_CHECK", columnDefinition = "nvarchar(255)")
	private String nameOnCardOrCheck;

	@Column(name = "TYPE_OF_CREDIT_CARD", columnDefinition = "nvarchar(100)")
	private String typeOfCreditCard;

	@Column(name = "CREDIT_CARD", columnDefinition = "nvarchar(100)")
	private String creditCard;

	@Column(name = "SECURITY_CODE", columnDefinition = "nvarchar(100)")
	private String securityCode;

	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;

	@Column(name = "CHECK_REQUEST_CTD_BY", columnDefinition = "nvarchar(100)")
	private String checkRequestCreatedBy;

	@Column(name = "CHECK_REQUEST_CTD_ON")
	private Date checkRequestCreatedOn;
}
