package com.mnrclara.api.accounting.model.invoice;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(	name = "tblpaymentupdate")
public class PaymentUpdate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PAYMENTID") 
	private Long paymentId;
	
	@Column(name = "INVOICE_NO") 
	private String invoiceNumber;
	
	@Column(name = "CLASS_ID") 
	private Long classId;
	
	@Column(name = "CLIENT_ID") 
	private String clientId;
	
	@Column(name = "LANG_ID") 
	private String languageId;
	
	@Column(name = "MATTER_NO") 
	private String matterNumber;
	
	@Column(name = "PAYMENT_AMOUNT")
	private Double paymentAmount;
	
	@Column(name = "PAYMENT_DATE")
	private Date paymentDate;
	
	@Column(name = "PAYMENT_NUMBER")
	private String paymentNumber;
	
	@Column(name = "PAYMENT_TEXT")
	private String paymentText;
	
	@Column(name = "POSTING_DATE")
	private Date postingDate;
	
	@Column(name = "TRANSACTION_TYPE")
	private String transactionType;
	
	@Column(name = "PAYMENT_CODE")
	private String paymentCode;
	
	@Column(name = "STATUS_ID") 
	private String statusId;
	
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
}
