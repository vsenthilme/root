package com.mnrclara.api.accounting.model.prebill;

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
 * `PRE_BILL_BATCH_NO` , `PRE_BILL_NO` , `PRE_BILL_DATE` , `MATTER_NO`
 */
@Table(
		name = "tblprebilldetails", 
		uniqueConstraints = { 
				@UniqueConstraint (
						name = "unique_key_prebilldetails", 
						columnNames = {"PRE_BILL_BATCH_NO" , "PRE_BILL_NO" , "PRE_BILL_DATE" , "MATTER_NO"})
				}
		)
@IdClass(PreBillDetailsCompositeKey.class)
public class PreBillDetails { 
	
	@Id
	@Column(name = "PRE_BILL_BATCH_NO") 
	private String preBillBatchNumber;
	
	@Id
	@Column(name = "PRE_BILL_NO")
	private String preBillNumber;
	
	@Id
	@Column(name = "PRE_BILL_DATE") 
	private Date preBillDate;
	
	@Id
	@Column(name = "MATTER_NO") 
	private String matterNumber;
	
	@Column(name = "CLIENT_ID")
	private String clientId;
	
	@Column(name = "LANG_ID") 
	private String languageId;	
	
	@Column(name = "CLASS_ID") 
	private Long classId;
	
	@Column(name = "PARTNER_ASSIGNED") 
	private String partnerAssigned;
	
	@Column(name = "NO_OF_TICKETS") 
	private Long numberOfTimeTickets;
	
	@Column(name = "TOTAL_AMOUNT") 
	private Double totalAmount;
	
	@Column(name = "START_DATE") 
	private Date startDateForPreBill;
	
	@Column(name = "FEES_CUTOFF")
	private Date feesCostCutoffDate;
	
	@Column(name = "PAYMENT_CUTOFF") 
	private Date paymentCutoffDate;
	
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
