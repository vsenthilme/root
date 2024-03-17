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
@Table(name = "tblmattertimeticketid")
public class MatterTimeTicket {
	
	@Id
	@Column(name = "TIME_TICKET_NO") 
	private String timeTicketNumber;
	
	@Column(name = "LANG_ID") 
	private String languageId;
	
	@Column(name = "CLASS_ID") 
	private Long classId;
	
	@Column(name = "MATTER_NO") 
	private String matterNumber;
	
	@Column(name = "CLIENT_ID") 
	private String clientId;
	
	@Column(name = "TK_CODE") 
	private String timeKeeperCode;
	
	@Column(name = "CASE_CATEGORY_ID") 
	private Long caseCategoryId;
	
	@Column(name = "CASE_SUB_CATEGORY_ID") 
	private Long caseSubCategoryId;
	
	@Column(name = "TIME_TICKET_HRS") 
	private Double timeTicketHours;
	
	@Column(name = "APP_TIME_TICKET_HRS") 
	private Double appTimeTicketHours;
	
	@Column(name = "TIME_TICKET_DATE") 
	private Date timeTicketDate;
	
	@Column(name = "ACTIVITY_CODE") 
	private String activityCode;
	
	@Column(name = "TASK_CODE") 
	private String taskCode;
	
	@Column(name = "DEF_RATE") 
	private Double defaultRate;
	
	@Column(name = "RATE_UNIT") 
	private String rateUnit;
	
	@Column(name = "TIME_TICKET_AMOUNT") 
	private Double timeTicketAmount;
	
	@Column(name = "BILL_TYPE") 
	private String billType;
	
	@Column(name = "TIME_TICKET_TEXT") 
	private String timeTicketDescription;
	
	@Column(name = "ASS_PARTNER") 
	private String assignedPartner;
	
	@Column(name = "ASS_ON") 
	private Date assignedOn;
	
	@Column(name = "APP_BILL_TIME") 
	private Double approvedBillableTimeInHours;
	
	@Column(name = "APP_BILL_AMOUNT") 
	private Double approvedBillableAmount;
	
	@Column(name = "APP_ON") 
	private Date approvedOn;
	
	@Column(name = "STATUS_ID") 
	private Long statusId;
	
	@Column(name = "IS_DELETED") 
	private Long deletionIndicator = 0L;
	
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
