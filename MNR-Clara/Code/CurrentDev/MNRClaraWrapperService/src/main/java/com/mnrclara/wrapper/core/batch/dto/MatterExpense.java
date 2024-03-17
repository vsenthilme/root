package com.mnrclara.wrapper.core.batch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MatterExpense { 
	
	private Long matterExpenseId;
	private String languageId;
	private Long classId;
	private String clientId;
	private String matterNumber;
	private String expenseCode;
	private Double expenseAmount;
	private String expenseDescription;
	private String expenseType;
	private String billType;
	private Long statusId;
	private Long deletionIndicator = 0L;
	private String createdBy;
    private Date createdOn;
    private String referenceField2;
   
    /**
     * 
     * @param languageId
     * @param classId
     * @param clientId
     * @param matterNumber
     * @param expenseCode
     * @param expenseAmount
     * @param expenseDescription
     * @param expenseType
     * @param billType
     * @param statusId
     * @param deletionIndicator
     * @param createdBy
     * @param createdOn
     */
	public MatterExpense (Long matterExpenseId, String languageId, Long classId, String clientId, String matterNumber, String expenseCode,
			Double expenseAmount, String expenseDescription, String expenseType, String billType, Long statusId,
			Long deletionIndicator, String createdBy, Date createdOn, String referenceField2) {
		this.matterExpenseId = matterExpenseId;
		this.expenseCode = expenseCode;
		this.languageId = languageId;
		this.classId = classId;
		this.matterNumber = matterNumber;
		this.clientId = clientId;
		this.expenseAmount = expenseAmount;
		this.expenseDescription = expenseDescription;
		this.expenseType = expenseType;
		this.billType = billType;
		this.statusId = statusId;
		this.deletionIndicator = deletionIndicator;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.referenceField2 = referenceField2;
	}
}
