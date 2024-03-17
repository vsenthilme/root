package com.mnrclara.wrapper.core.model.report;

import java.util.Date;

import lombok.Data;

@Data
public class MatterLNEReport {

	private String clientId;
	private String matterNumber;
	private String status;
	private String caseCategoryId;
	private String caseSubCategoryId;
	private String matterDescription;
	private String billModeText;
	private Date matterOpenedDate;
	private Date matterClosedDate;

	private String sMatterOpenedDate;
	private String sMatterClosedDate;
	private String createdBy;	
	private String updatedBy;	
	private String referredBy;	
	private String corporateName;
	private String originatingTimeKeeper;
	private String assignedTimeKeeper;
	private String responsibleTimeKeeper;
	private String notesText;	
	
	// LNE
	private String typeOfMatter;
	private String reference;
	private String locationOfFile;
	private String defendants;
	private String causeNo;
	private String plaintiffs;
	private String advParty1Name;
	private String advParty1Email;
	private String advParty1CellPhone;
	private String advParty2Name;
	private String advParty2Email;
	private String advParty2CellPhone;
	private String judgeName;
	private String email;			// Court Email
	private String officeTelephone;	// Court Phone
	private String agencyName;
	private String agentName;
	private String agentEmail;
	private String agentOfficeTelephone;
	private Date scheduleDate;
	private String tasksToDo;
	private Date taskDate;
	private String partner;
	private String legalAssitant;
	private String lawClerk;
}
