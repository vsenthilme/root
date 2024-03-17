package com.mnrclara.api.accounting.model.invoice;

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
 * `LANG_ID` , `CLASS_ID` , `MATTER_NO` , `CLIENT_ID` , `INVOICE_NO` , `INVOICE_FISCAL_YEAR` , 
 * `INVOICE_PERIOD` , `ITEM_NO`
 */
@Table(
		name = "tblinvoicelinetransfer", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_invoiceline_transfer", 
						columnNames = { "LANG_ID" , "CLASS_ID" , "MATTER_NO" , "CLIENT_ID" , "INVOICE_NO" , "ITEM_NO"})
				}
		)
@IdClass(InvoiceLineCompositeKey.class)
public class InvoiceLineTransfer { 
	
	@Id
	@Column(name = "LANG_ID") 
	private String languageId;
	
	@Id
	@Column(name = "CLASS_ID") 
	private Long classId;
	
	@Id
	@Column(name = "MATTER_NO") 
	private String matterNumber;
	
	@Id
	@Column(name = "CLIENT_ID")	
	private String clientId;
	
	@Id
	@Column(name = "INVOICE_NO")
	private String invoiceNumber;
	
	@Id
	@Column(name = "ITEM_NO") 
	private Long itemNumber;
	
	//////////////////////////////////////////////////////////
	
	@Column(name = "INVOICE_FISCAL_YEAR") 
	private String invoiceFiscalYear;
	
	@Column(name = "SERIAL_NO")
	private Long serialNumber;
	
	@Column(name = "INVOICE_PERIOD") 
	private String invoicePeriod;
	
	@Column(name = "ACTIVITY_CODE") 
	private String activityCode;
	
	@Column(name = "INVOICE_TEXT") 
	private String invoiceText;
	
	@Column(name = "TASK_CODE") 
	private String taskCode;
	
	@Column(name = "TIME_TICKET_DATE") 
	private Date timeTicketDate;
	
	@Column(name = "TK_CODE") 
	private String timeKeeperCode;
	
	@Column(name = "BILL_TYPE")
	private String billType;
	
	@Column(name = "BILL_TIME") 
	private Double billableTimeinHours;
	
	@Column(name = "BILL_AMOUNT") 
	private Double billableAmount;
	
	@Column(name = "CURRENCY") 
	private String currency;
	
	@Column(name = "GL_ACCOUNT") 
	private String glAccount;
	
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
