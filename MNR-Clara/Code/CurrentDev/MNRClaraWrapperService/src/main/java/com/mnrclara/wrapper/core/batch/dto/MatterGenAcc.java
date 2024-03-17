package com.mnrclara.wrapper.core.batch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MatterGenAcc { 
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
	private String caseInformationNo;
	private Long transactionId;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private String billingModeId;
	private String billingFormatId;
	private String billingFrequencyId;
	private String billingRemarks;
	private String matterDescription;
	private Date caseOpenedDate;
	private String arAccountNumber;
	private String trustDepositNo;
	private Double flatFeeAmount;
	private Double administrativeCost;
	private Long statusId;
	private Long deletionIndicator = 0L;
	private String createdBy;
	private Date createdOn;
	private String UpdatedBy;
	private String referenceField25;	
	
	public MatterGenAcc (String languageId,Long classId,String matterNumber,String clientId,String caseInformationNo,Long transactionId,
			Long caseCategoryId,Long caseSubCategoryId,String billingModeId,String billingFormatId,String billingFrequencyId,
			String billingRemarks,String matterDescription,Date caseOpenedDate,String arAccountNumber,String trustDepositNo,
			Double flatFeeAmount,Double administrativeCost,Long statusId,Long deletionIndicator,String createdBy,Date createdOn,
			String UpdatedBy, String referenceField25) {
		
		this.matterNumber = matterNumber;
		this.languageId = languageId;
		this.classId = classId;
		this.caseInformationNo = caseInformationNo;
		this.clientId = clientId;
		this.transactionId = transactionId;
		this.caseCategoryId = caseCategoryId;
		this.caseSubCategoryId = caseSubCategoryId;
		this.matterDescription = matterDescription;
		this.caseOpenedDate = caseOpenedDate;
		this.billingModeId = billingModeId;
		this.billingFrequencyId = billingFrequencyId;
		this.billingFormatId = billingFormatId;
		this.billingRemarks = billingRemarks;
		this.arAccountNumber = arAccountNumber;
		this.trustDepositNo = trustDepositNo;
		this.flatFeeAmount = flatFeeAmount;
		this.administrativeCost = administrativeCost;
		this.statusId = statusId;
		this.deletionIndicator = deletionIndicator;
		this.referenceField25 = referenceField25;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.UpdatedBy = UpdatedBy;
	}
}
