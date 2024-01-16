package com.mnrclara.qb.ws.services.model;

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
	private Long caseSubCategoryId;
	private String matterDescription;
	private String fileNumber;
	private Long caseFileNumber;
	private Date caseOpenedDate;
	private Date caseClosedDate;
	private Date caseFiledDate;
	private Date priorityDate;
	private String receiptNoticeNo;
	private Date receiptDate;
	private Date expirationDate;
	private Date courtDate;
	private Date approvalDate;
	private String billingModeId;
	private String billingFrequencyId;
	private String billingFormatId;
	private String billingRemarks;
	private String arAccountNumber;
	private String trustDepositNo;
	private Double flatFeeAmount;
	private Double administrativeCost;
	private Double contigencyFeeAmount;
	private String rateUnit;
	private String directPhoneNumber;
	private Long sentToQB = 0L;
}
