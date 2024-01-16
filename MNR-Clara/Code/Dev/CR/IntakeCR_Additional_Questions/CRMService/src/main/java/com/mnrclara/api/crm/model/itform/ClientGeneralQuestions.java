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
	
	/*
	 * CR - Extra fields
	 */
	private String howManyPeopleLiveWithYou;
	private String doAnyOfTheIndividualPayTaxesAlone;
	private String numberOfIndividualPayTaxesAlone;
	private String annualHouseholdIncome;
	private String totalValueOfAssest;
	private String highestDegreeOrLevelOfSchool;
	private String listOfCertificateOrLicenses;
	private String haveYouReceivedAnySupplementaryIncomeOrTemporaryAssisstance;
	
	private String benefitReceived1;
	private String benefitReceived2;
	private String benefitReceived3;
	private String benefitReceived4;
	
	private Date benefitStartDate1;
	private Date benefitStartDate2;
	private Date benefitStartDate3;
	private Date benefitStartDate4;
	private Date benefitEndDate1;
	private Date benefitEndDate2;
	private Date benefitEndDate3;
	private Date benefitEndDate4;
	
	private String benefitAmount1;
	private String benefitAmount2;
	private String benefitAmount3;
	private String benefitAmount4;
	private String benefitAmount5;
	private String haveYouReceivedLongTermInstitutionalizationAtGovtExpense;
	private String institutionalizationBenefitReceived1;
	private String institutionalizationBenefitReceived2;
	private String institutionalizationBenefitReceived3;
	private String institutionalizationBenefitReceived4;
	
	private Date institutionalizationBenefitReceivedStartDate1;
	private Date institutionalizationBenefitReceivedStartDate2;
	private Date institutionalizationBenefitReceivedStartDate3;
	private Date institutionalizationBenefitReceivedStartDate4;
	private Date institutionalizationBenefitReceivedEndDate1;
	private Date institutionalizationBenefitReceivedEndDate2;
	private Date institutionalizationBenefitReceivedEndDate3;
	private Date institutionalizationBenefitReceivedEndDate4;

	private String institutionalizationBenefitAmount1;
	private String institutionalizationBenefitAmount2;
	private String institutionalizationBenefitAmount3;
	private String institutionalizationBenefitAmount4;
	
	private String nameOfInstitution1;
	private String nameOfInstitution2;
	private String nameOfInstitution3;
	private String cityAndState1;
	private String cityAndState2;
	private String cityAndState3;
}
