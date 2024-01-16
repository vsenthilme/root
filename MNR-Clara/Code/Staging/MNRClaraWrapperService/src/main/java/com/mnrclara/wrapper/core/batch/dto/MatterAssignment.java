package com.mnrclara.wrapper.core.batch.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MatterAssignment { 
	
	/*
	 * LANG_ID	CLASS_ID	MATTER_NO	CASEINFO_NO	CLIENT_ID	CASE_OPEN_DATE	
	 * PARTNER	ORIGINATING_TK	RESPONSIBLE_TK
	 * 	ASSIGNED_TK	LEGAL_ASSIST	STATUS_ID	Isdeleted	CTD_BY	CTD_ON
	 */
	
	private String languageId;
	private Long classId;
	private String matterNumber;
	private String caseInformationNo;
	private String clientId;
	private Date caseOpenedDate;
	private String partner;
	private String originatingTimeKeeper;
	private String responsibleTimeKeeper;
	private String assignedTimeKeeper;
	private String legalAssistant;
	private Long statusId;
	private Long deletionIndicator;
	private String createdBy;
	private Date createdOn;
	
//	private String referenceField1;
//	private String referenceField2;
//	private String referenceField3;
//	private String referenceField4;
//	private String referenceField5;
//	private String referenceField6;
//	private String referenceField7;
//	private String referenceField8;
//	private String referenceField9;
//	private String referenceField10;
//	private Long caseCategoryId;
//	private Long caseSubCategoryId;
//	private String updatedBy;
//	private Date updatedOn;
	
	/**
	 * 
	* @param caseInformationNo
	* @param languageId
	* @param classId
	* @param matterNumber
	* @param clientId
	* @param caseCategoryId
	* @param caseSubCategoryId
	* @param caseOpenedDate
	* @param partner
	* @param originatingTimeKeeper
	* @param responsibleTimeKeeper
	* @param assignedTimeKeeper
	* @param legalAssistant
	* @param statusId
	* @param deletionIndicator
	* @param referenceField1
	* @param referenceField2
	* @param referenceField3
	* @param referenceField4
	* @param referenceField5
	* @param referenceField6
	* @param referenceField7
	* @param referenceField8
	* @param referenceField9
	* @param referenceField10
	* @param createdBy
	* @param createdOn
	* @param updatedBy
	* @param updatedOn
	 */
//	public MatterAssignment (String caseInformationNo, String languageId, Long classId, String matterNumber, String clientId, 
//			Long caseCategoryId, Long caseSubCategoryId, Date caseOpenedDate, String partner, String originatingTimeKeeper, 
//			String responsibleTimeKeeper, String assignedTimeKeeper, String legalAssistant, Long statusId, Long deletionIndicator,
//			String referenceField1, String referenceField2, String referenceField3, String referenceField4, String referenceField5, 
//			String referenceField6, String referenceField7, String referenceField8, String referenceField9, String referenceField10,
//			String createdBy, Date createdOn, String updatedBy, Date updatedOn) {
	
	public MatterAssignment (String languageId,Long classId,String matterNumber,String caseInformationNo,String clientId,Date caseOpenedDate,String partner,
	String originatingTimeKeeper,String responsibleTimeKeeper,String assignedTimeKeeper,String legalAssistant,Long statusId,
	Long deletionIndicator,String createdBy,Date createdOn) {
		this.caseInformationNo = caseInformationNo;
		this.languageId = languageId;
		this.classId = classId;
		this.matterNumber = matterNumber;
		this.clientId = clientId;
		this.caseOpenedDate = caseOpenedDate;
		this.partner = partner;
		this.originatingTimeKeeper = originatingTimeKeeper;
		this.responsibleTimeKeeper = responsibleTimeKeeper;
		this.assignedTimeKeeper = assignedTimeKeeper;
		this.legalAssistant = legalAssistant;
		this.statusId = statusId;
		this.deletionIndicator = deletionIndicator;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}
}
