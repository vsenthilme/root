package com.mnrclara.wrapper.core.model.datamigration;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;

@Data
public class MatterGenAcc {

	@CsvBindByPosition(position = 0)
	private String languageId;
	
	@CsvBindByPosition(position = 1)
	private Long classId;
	
	@CsvBindByPosition(position = 2) 
	private String matterNumber;
	
	@CsvBindByPosition(position = 3)
	private String clientId;
	
	@CsvBindByPosition(position = 4) 
	private String caseInformationNo;
	
	@CsvBindByPosition(position = 5) 
	private Long transactionId;
	
	@CsvBindByPosition(position = 6) 
	private Long caseCategoryId;
	
	@CsvBindByPosition(position = 7) 
	private Long caseSubCategoryId;
	
	@CsvBindByPosition(position = 8) 
	private String billingModeId;
	
	@CsvBindByPosition(position = 9) 
	private String billingFormatId;
	
	@CsvBindByPosition(position = 10) 
	private String billingFrequencyId;
	
	@CsvBindByPosition(position = 11)
	private String billingRemarks;
	
	@CsvBindByPosition(position = 12)
	private Double flatFeeAmount;
	
	@CsvBindByPosition(position = 13) 
	private Double administrativeCost;
	
	@CsvBindByPosition(position = 14) 
	private String matterDescription;
	
	@CsvBindByPosition(position = 15) 
	private String caseOpenedDate;
	
	@CsvBindByPosition(position = 16)
	private String caseClosedDate;
	
	@CsvBindByPosition(position = 17) 
	private String caseFiledDate;
	
	@CsvBindByPosition(position = 18) 
	private String priorityDate;
	
	@CsvBindByPosition(position = 19)
	private String receiptNoticeNo;
	
	@CsvBindByPosition(position = 20) 
	private String receiptDate;
	
	@CsvBindByPosition(position = 21) 
	private String expirationDate;
	
	@CsvBindByPosition(position = 22)
	private String courtDate;
	
	@CsvBindByPosition(position = 23) 
	private String approvalDate;
	
	@CsvBindByPosition(position = 24) 
	private String arAccountNumber;
	
	@CsvBindByPosition(position = 25)
	private String trustDepositNo;
	
	@CsvBindByPosition(position = 26) 
	private Double contigencyFeeAmount;
	
	@CsvBindByPosition(position = 27) 
	private String rateUnit;
	
	@CsvBindByPosition(position = 28)
	private String directPhoneNumber;
	
	@CsvBindByPosition(position = 29) 
	private Long statusId;
	
	@CsvBindByPosition(position = 30) 
	private Long deletionIndicator = 0L;
	
	@CsvBindByPosition(position = 31)
	private String createdBy;
	
	@CsvBindByPosition(position = 32) 
	private String createdOn;
	
	@CsvBindByPosition(position = 33) 
	private String UpdatedBy;
	
	@CsvBindByPosition(position = 34)
	private String updatedOn;
	
	@CsvBindByPosition(position = 35)
	private String referenceField25;
}
