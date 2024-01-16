package com.mnrclara.wrapper.core.model.crm;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SearchPotentialClientInput {
	
	/*
	 * POT_CLIENT_ID
	 * INQ_NO
	 * IT_FORM_ID/IT_FORM_TEXT
	 * FIRST_LAST_NM
	 * EMAIL_ID
	 * CONT_NO
	 * CTD_ON
	 * CTD_BY
	 * REF_FIELD_1 -- Consultation date
	 * REF_FIELD_2 -- Assigned attorney
	 * STATUS_ID/STATUS_TEXT
	 * -----------------------------------
	 * multi search
	 * ------------
	 * Prospective Client
	 * Inquiry
	 * Intake Form
	 * Created by
	 * Status 
	 * 
	 */
	private List<String> potentialClientId;
	private List<String> inquiryNumber;
	private List<Long> intakeFormId;
	private List<String> createdBy;
	private List<Long> statusId;
	
	private String firstNameLastName;
	private String emailId;
	private String contactNumber;
	private Date sCreatedOn;
	private Date eCreatedOn;	
	private Date sConsultationDate;
	private Date eConsultationDate;
	private String assignedAttorney;
}
