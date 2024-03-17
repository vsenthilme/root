package com.mnrclara.api.accounting.model.prebill;

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
	private Date referenceField2;	// Expense Date
	
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
