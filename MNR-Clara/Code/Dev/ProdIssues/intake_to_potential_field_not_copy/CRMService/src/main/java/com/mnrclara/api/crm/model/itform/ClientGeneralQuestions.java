package com.mnrclara.api.crm.model.itform;


import java.util.Date;

import lombok.Data;

@Data
public class ClientGeneralQuestions {

	private String areYouCurrentlyLivingInUSA;
	private String stayingSinceMonth;
	private String stayingSinceYear;
	private String currentLocation;
	
	/*------------------------------------------*/
	private String haveYouEverHadUSVisa;
	private String consulateThatIssuedVISA;
	private Date issueDateOFVISA;
	private Date expirationDateOFVISA;
	
	/*------------------------------------------*/
	private String haveYouEverbeenRefusedUSVisa;
	private String consulateDenyingVISAIssued;
	private String deniedMoreThanOnce;
	
	/*------------------------------------------*/
	private String haveYouEverbeenStoppedOrQuestionedByAnImmigOfficer;
	private String locationOfStoppedOrQuestioned;
	private String stoppedMoreThanOnce;
	private Date dateOfStoppedOrQuestioned;

	/*------------------------------------------*/
	private String haveYouEverbeenDeniedEntryIntoUS;
	private String locationOfDeniedEntry;
	private String deniedEntryMoreThanOnce;
	private Date dateOfDeniedEntry;
	
	/*------------------------------------------*/
	private String haveYouEverbeenInDeportationProceedings;
	private String locationOfDeportation;
	private String endResultOfDeportationProceedings;
	private Date dateOfDeportationProceedings;
	
	/*------------------------------------------*/
	private String haveYouEverbeenDeportedOrRemovedFromUS;
	private String locationOfDeportedOrRemovedFromUS;
	private String reasonForDeportation;
	private Date dateOfDeportation;
	
	/*------------------------------------------*/
	private String haveYouEverbeenStoppedOrQuestionedByThePolice;
	private String locationOfStoppedByPolice;
	private String reasonForStop;
	private Date dateOfStop;
	private String wereYouArrested;
	
	/*------------------------------------------*/
	private String haveYouEverbeenChargedWWithCommittingACrime;
	private String locationOfCrimeCharged;
	private String crimeCharged;
	private Date dateOfCrimeCharged;
	private String didYouGotoCourt;
	
	/*------------------------------------------*/
	private String haveYouEverbeenFoundGuiltyOfCommittingACrime;
	private String foundGuiltyLocation;
	private String foundGuiltyCrimeCharged;
	private Date dateOfFoundGuiltyCrime;
	private String penalty;
	
	/*------------------------------------------*/
	private String doYouHaveValidDriverLicense;
	private String stateOfDriverLicense;
	private Date dateOfDriverLicense;
	
	/*------------------------------------------*/
	private String doYouPayTaxesInUS;
	private String sinceWhen;
	
	/*------------------------------------------*/
	private String areYouCurrentlyEmployedUsingUSSSN;
	private String ssnValidStatus;
	
	/*------------------------------------------*/
	private String doYouHaveEmploymentAuthorization;
	private String employmentAuthorizationValidStatus;
	
	/*------------------------------------------*/
	private String occupation;
	
	/*------------------------------------------*/
	private boolean areYouCurrentlyWorking;
	private String reasonForUnemployment;
	private String areYouReceivingUnemployment;
	
	/*------------------------------------------*/
	private String haveYouFiledTaxesDuringLast3Years;
	private String reasonForNonTaxFiling;
	
	/*------------------------------------------*/
	private String education;
	private String educationOther;
	
	/*------------------------------------------*/
	private String englishLanguageProficiency;
	
	/*------------------------------------------*/
	private String owningAnyCertications;
	private String certicateDetails;
}
