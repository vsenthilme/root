package com.mnrclara.api.accounting.model.paymentplan;

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
 * `PAYMENT_PLAN_NO` , `PLAN_REV_NO` , `ITEM_NO`
 */
@Table(
		name = "tblpaymentplanline", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_paymentplanline", 
						columnNames = { "PAYMENT_PLAN_NO" , "PLAN_REV_NO" , "ITEM_NO"})
				}
		)
@IdClass(PaymentPlanLineCompositeKey.class)
public class PaymentPlanLineEntity { 
	
	@Id
	@Column(name = "PAYMENT_PLAN_NO") 
	private String paymentPlanNumber;	
	
	@Id
	@Column(name = "PLAN_REV_NO") 
	private Long paymentPlanRevisionNo;
	
	@Id
	@Column(name = "ITEM_NO") 
	private Long itemNumber;

	@Column(name = "LANG_ID") 
	private String languageId;
	
	@Column(name = "CLASS_ID") 
	private Long classId;
	
	@Column(name = "MATTER_NO") 
	private String matterNumber;
	
	@Column(name = "CLIENT_ID") 
	private String clientId;
	
	@Column(name = "QUOTE_NO") 
	private String quotationNo;
	
	@Column(name = "DUE_DATE") 
	private Date dueDate;
	
	@Column(name = "DUE_AMOUNT") 
	private Double dueAmount;
	
	@Column(name = "REMAIN_DUE_NOW") 
	private Double remainingDueNow;
	
	@Column(name = "CURRENCY") 
	private String currency;
	
	@Column(name = "REMINDER_DAYS") 
	private Long paymentReminderDays;
	
	@Column(name = "REMINDER_DATE") 
	private Date reminderDate;
	
	@Column(name = "REMINDER_STATUS") 
	private String reminderStatus;
	
	@Column(name = "STATUS_ID") 
	private Long statusId;
	
	@Column(name = "PAID_AMOUNT")
	private Double paidAmount;
	
	@Column(name = "BALANCE_AMOUNT")
	private Double balanceAmount;
	
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
