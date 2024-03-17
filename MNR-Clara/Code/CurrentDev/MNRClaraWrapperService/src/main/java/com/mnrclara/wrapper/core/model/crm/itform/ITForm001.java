package com.mnrclara.wrapper.core.model.crm.itform;

import java.util.Date;

import lombok.Data;

@Data
public class ITForm001 extends BaseITForm {
    private String confRoomNo;
    private String attorney;
    private String legalAssistant;
    private String date;
    private String clientNumber;
    private String clientPGT;
    private String bgt;
    private String name;
    private ClientContactNumberInfo contactNumber;
    private String address;
    private boolean doYouLiveAtThisAddress;
	private String emailAddress;
	private String contactPersonName;
	private boolean doYouNeedImmigrationUpdates;
	private String purposeOfVisit;
	private String issues;
	private String recommendations;
	private String billingDepartmentNotes;
	private String notes;
	
	private String createdBy;
	private String updatedBy;
	private Date createdOn = new Date();
	private Date updatedOn = new Date();
}