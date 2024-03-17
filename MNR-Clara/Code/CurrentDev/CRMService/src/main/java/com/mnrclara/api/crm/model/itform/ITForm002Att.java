package com.mnrclara.api.crm.model.itform;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("ITForm002Att")
public class ITForm002Att extends BaseITForm {
    private String confRoomNo;
    private String feeInDollar;
    private String name;
    private ClientContactNumberInfo contactNumber;
    private String address;
    private boolean doYouLiveAtThisAddress;
    private String emailAddress;
	private String contactPersonName;
	private boolean doNeedImmigrationUpdates;
	private ClientReferenceMedium referenceMedium;
	private String thisConsultationIsFor;
	private String purposeOfVisit;
	private boolean doHaveImmigrationAttorney;
	private String nameOfAttorney;
	private String attorney;
	private String legalAssistant;
	private Date attorneyDate;
	private List<String> attorneyNotes;
	private String attorneyNotesForRemoval;
	private String attorneyNotesForOthers;
	private String notes;
	private ClientGeneralInfo clientGeneralInfo;
	private CasePlan casePlan;
	private String createdBy;
	private String updatedBy;
	private Date createdOn = new Date();
	private Date updatedOn = new Date();
}