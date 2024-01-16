package com.mnrclara.api.crm.model.pcitform;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class LeadConversionReport {

	private String classId;						// CLASS_ID
	private String inquiryNumber;				// Inquiry Number
	private String inquiryAssignedToRefField4;	// Inquiry Assigned to
	private Date intakeFormReceived;			// RECEIVED_ON	- PCINTAKEFORM
	private String ConsultationDate;			// Date of Consultation - REF_FIELD_1
	private String consultingAttorney;			// Consulting Attorney - REF_FIELD_2
	private Long statusId;						// STATUS_ID

	
	//---------Inquiry Fields----------------------------------------------
	private Date inquiyDate;				// CTD_ON - INQUIRY
	private String firstNameLastName;		// FIRST_LAST_NM - INQUIRY
	private String contactNumber;			// CONT_NO - INQUIRY
	private String email;					// EMAIL_ID	- INQUIRY
	private String modeOfInquiry;				// INQ_MODE_ID - INQUIRY
	private String orginalInquiryObjective;	// INQ_NOTE_NO
	private String listOfMediumAboutFirm;

	//---------POTENTIALCLIENT---------------------------------------------
	private Date createdOn;
}
