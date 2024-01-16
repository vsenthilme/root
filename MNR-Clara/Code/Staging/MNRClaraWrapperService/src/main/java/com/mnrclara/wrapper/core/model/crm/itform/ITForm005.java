package com.mnrclara.wrapper.core.model.crm.itform;

import java.util.Date;

import lombok.Data;

@Data
public class ITForm005 extends BaseITForm {
    
    private String confRoomNo;
    private String attorney;
    private String legalAssistant;
    private String date;
    private String clientNumber;
    private String clientPGT;
    private String bgt;
    private boolean hasYourContactInfoChanged;
    
    /*------------------------------------------------*/
    private String name;
    private ClientContactNumberInfo contactNumber;
    private String address;
    private boolean doYouLiveAtThisAddress;
	private String emailAddress;
	private String contactPersonName;
	private boolean doYouNeedImmigrationUpdates;
	private String purposeOfVisit;
	
	/*------------------------------------------------*/
	private boolean hasYourMaritalStatusInfoChanged;
	private String maritalStatus;
	private String nameOfSpouseOrFiance; 
	private ClientPersonalInfo clientPersonalInfo;
	private Date dateOfMarriage;
	private String placeOfResidence;
	private boolean hasSpouseEverbeenConvictedOfACrime;
	
	// IF YOU HAVE CHILDREN, PLEASE COMPLETE THIS SECTION
	private ClientChildrenInfo child1;
	private ClientChildrenInfo child2;
	private ClientChildrenInfo child3;
	private ClientChildrenInfo child4;
		
	// HAVE YOU TRAVELED OUTSIDE THE UNITED STATES?
	private ClientEntryOutside clientEntryOutside1;
	private ClientEntryOutside clientEntryOutside2;
	private ClientEntryOutside clientEntryOutside3;
	private ClientEntryOutside clientEntryOutside4;
	private ClientEntryOutside clientEntryOutside5;
	private ClientEntryOutside lastClientEntryOutside;
	
	private String notes;
	private String issues;
	private String recommendations;
	private String billingDepartmentNotes;
	
	private String createdBy;
	private String updatedBy;
	private Date createdOn = new Date();
	private Date updatedOn = new Date();
}