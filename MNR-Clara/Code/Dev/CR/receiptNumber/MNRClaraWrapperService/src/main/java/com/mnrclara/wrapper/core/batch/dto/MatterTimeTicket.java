package com.mnrclara.wrapper.core.batch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MatterTimeTicket { 
//	LANG_ID	CLASS_ID	MATTER_NO	CLIENT_ID	CASE_CATEGORY_ID	CASE_SUB_CATEGORY_ID	
//	TIME_TICKET_NO	TIME_TICKET_HRS	TIME_TICKET_DATE	TIME_TICKET_AMOUNT	BILL_TYPE	
//	TIME_TICKET_TEXT	STATUS_ID	Isdeleted	CTD_BY	CTD_ON
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String clientId;
//	private String timeKeeperCode;
	private Long caseCategoryId;
	private Long caseSubCategoryId;
	private String timeTicketNumber;
	private Double timeTicketHours;
	private Date timeTicketDate;
	private Double timeTicketAmount;
	private String billType;
	private String timeTicketDescription;
	private Long statusId;
	private Long deletionIndicator;
	private String createdBy;
    private Date createdOn = new Date();
	
    /**
     * 
     * @param timeTicketNumber
     * @param languageId
     * @param classId
     * @param matterNumber
     * @param clientId
     * @param caseCategoryId
     * @param caseSubCategoryId
     * @param timeTicketHours
     * @param timeTicketDate
     * @param timeTicketAmount
     * @param billType
     * @param timeTicketDescription
     * @param statusId
     * @param deletionIndicator
     * @param createdBy
     * @param createdOn
     */
	public MatterTimeTicket (String languageId, Long classId, String matterNumber, String clientId, 
			Long caseCategoryId, Long caseSubCategoryId, String timeTicketNumber, Double timeTicketHours, Date timeTicketDate,
			Double timeTicketAmount, String billType, String timeTicketDescription, Long statusId, Long deletionIndicator, 
			String createdBy, Date createdOn) {
		this.timeTicketNumber = timeTicketNumber;
		this.languageId = languageId;
		this.classId = classId;
		this.matterNumber = matterNumber;
		this.clientId = clientId;
		this.caseCategoryId = caseCategoryId;
		this.caseSubCategoryId = caseSubCategoryId;
		this.timeTicketHours = timeTicketHours;
		this.timeTicketDate = timeTicketDate;
		this.timeTicketAmount = timeTicketAmount;
		this.billType = billType;
		this.timeTicketDescription = timeTicketDescription;
		this.statusId = statusId;
		this.deletionIndicator = deletionIndicator;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}
}
