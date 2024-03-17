package com.mnrclara.wrapper.core.model.crm.itform;


import java.util.Date;

import lombok.Data;

@Data
public class ClientGeneralInfo {

	private ClientPersonalInfo clientPersonalInfo;
	private String maritalStatus;
	
	// IF MARRIED OR ENGAGED, COMPLETE THIS SECTION:
	private ClientPersonalInfo spouseOrFiancePersonalInfo;
	
	private boolean permanentResidentOrUSC;
	private Date dateOfMarriage;
	private String placeOfResidence;
	private boolean hasSpouseEverbeenConvictedOfACrime;

	//IF DIVORCED OR WIDOWED, PLEASE COMPLETE THIS SECTION
	private ClientPersonalInfo divorcedOrWidowedPersonalInfo;
	private Date dateOfMarriageOfDivorcedOrWidowed;
	private boolean permanentResident;
	private int yearOfResidency;
	private String residencyObtainedThrough;
	
	// DO YOU HAVE A FATHER OR MOTHER WITH AMERICAN CITIZENSHIP
	private String americanCitizenshipObtainedThrough;
	private int yearOfCitizenship;
	private int ageOfClient;
	private boolean hasAnyoneEverFiledImmigrantVisaPetition;
	private String relationshipOfFiledPersonForParents;
	private Date datePetitionFiled;
	private String nameOfFiledPersonForParents;
	private String resultOfFiledPetition;
	
	// IF YOU HAVE CHILDREN, PLEASE COMPLETE THIS SECTION
	private ClientChildrenInfo child1;
	private ClientChildrenInfo child2;
	private ClientChildrenInfo child3;
	private ClientChildrenInfo child4;
	private ClientChildrenInfo child5;
	private ClientChildrenInfo child6;
	private ClientChildrenInfo child7;
	private ClientChildrenInfo child8;
	
	// Dates of entries and exits to the United States:
	private ClientEnteredDates enteredDates;
	private ClientExitedDates exitedDates;
	private ClientEntry firstDateOfEntry;
	private ClientEntry lastDateOfEntry;
	private VisaEntry visaEntry;
	
	// Client General Questions
	private ClientGeneralQuestions questions;
	private ClientReceivedBenefits clientReceivedBenefits;
	
	/*------------------------------------------*/
	private Date date;
	
	/* 
	 * New Fields
	 */
	private String childNotes;
	private String entryExitNotes;
	private String visaPetitionNotes;
	private String spouseCrimeNotes;
	private String deportedNotes;
	private String guiltyNotes;
	private String victimOfACrimeNotes;
	private String attorneyNotesAsArray;
	private String deadlineDate;
	
	private String city;
	private String State;
	private String Zipcode;
	private String whichCityAndCountryDidYouMarryIn;
	
	private String whatIsYourOccupation;
	private String areYouCurrentlyWorking;
	private String haveYouEverAskedForAnyPubilcAssistance;
}
