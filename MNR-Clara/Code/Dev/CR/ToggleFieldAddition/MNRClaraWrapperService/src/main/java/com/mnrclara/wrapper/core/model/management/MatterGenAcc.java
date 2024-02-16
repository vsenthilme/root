package com.mnrclara.wrapper.core.model.management;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MatterGenAcc {

	private String matterNumber;
	private String languageId;
	private Long classId;
	private String caseInformationNo;
	private String clientId;
	private Long transactionId;
	private Long caseCategoryId;
	private String caseCategory;
	private Long caseSubCategoryId;
	private String matterDescription;
	private String fileNumber;
	private Long caseFileNumber;
	
	private Date caseOpenedDate;
	private Date caseClosedDate;
	private Date caseFiledDate;
	private Date priorityDate;
	private Date receiptDate;
	private Date expirationDate;
	private Date courtDate;
	private Date approvalDate;
	
	/* String type Dates */
	private String sCaseOpenedDate;
	private String sCaseClosedDate;
	private String sCaseFiledDate;
	private String sPriorityDate;
	private String sReceiptDate;
	private String sExpirationDate;
	private String sCourtDate;
	private String sApprovalDate;
	
	private String receiptNoticeNo;
	private String billingModeId;
	private String billingFrequencyId;
	private String billingFormatId;
	private String billingRemarks;
	private String arAccountNumber;
	private String trustDepositNo;
	private Double flatFeeAmount;
	private Double remainingAmount;
	private Double administrativeCost;
	private Double contigencyFeeAmount;
	private String rateUnit;
	private String directPhoneNumber;
	private Long statusId;
	private String accountantName;
	private Long deletionIndicator = 0L;
	private String referenceField1;
	private String referenceField2;
	private String referenceField3;
	private String referenceField4;
	private String referenceField5;
	private String referenceField6;
	private String referenceField7;
	private String referenceField8;
	private String referenceField9;
	private String referenceField10;
	private String referenceField11;
	private String referenceField12;
	private String referenceField13;
	private String referenceField14;
	private String referenceField15;
	private String referenceField16;
	private String referenceField17;
	private String referenceField18;
	private String referenceField19;
	private String referenceField20;
	private Long sentToQB = 0L;
	
	
	/*
	 * Holds Conflict Seach Data
	 * first name, last name, first last name and email ID 
	 */
	private String referenceField21;
	private String referenceField22;
	private String referenceField25;
	
	private String referenceField23;	// Statue 
	private String referenceField24;	// Subcategory
	private String referenceField26;	// Client Name
	private String referenceField27;	// Class Name
	private String referenceField28;	// Case category
	
	private String referenceField29;	// preference_category_id
	private String referenceField30;	// priority_date
	private String referenceField31;	// priority_date_status
	
	private String createdBy;
	private Date createdOn;
	private String UpdatedBy;
	private Date updatedOn;	
}
