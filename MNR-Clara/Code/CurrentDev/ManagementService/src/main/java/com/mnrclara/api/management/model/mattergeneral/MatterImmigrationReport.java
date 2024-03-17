package com.mnrclara.api.management.model.mattergeneral;

import java.util.Date;

import lombok.Data;

@Data
public class MatterImmigrationReport {

	private String clientId;
	private String clientName;
	private String matterNumber;
	private String status;
	private String petitionerName;				// Client General
	private String firstNameLastName;			// Client General
	private String petitionerEmailId;			// Client General
	private String petitionerContactNumber;		// Client General
	private String beneficiaryContactNumber;	// Client General
	private String benerficiaryEmailId;			// Client General
	private String newMatterExistingClient;		// MATTERGENACC
	private String referredBy;					// REFERRAL_ID
	private String caseCategoryId;
	private String caseSubCategoryId;
	private String matterDescription;
	private String billModeText;
	private Date clientOpenedDate;
	private Date matterOpenedDate;
	private String originatingTimeKeeper;
	private String assignedTimeKeeper;
	private String responsibleTimeKeeper;
	private Double flatFeeAmount;
	private Double retainerPaid;
	private Double expensesFee;
	private String attorneysNotes; 				// NOTE_TEXT
	private Date matterClosedDate;
	private String matterNotes;
	private Date caseFiledDate;
	private String paralegal;
	private String partner;
	private String legalAssistant;
	private String createdBy;
	private String lawClerk;
}
