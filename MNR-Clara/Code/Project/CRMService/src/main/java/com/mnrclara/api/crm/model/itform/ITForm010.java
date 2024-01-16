package com.mnrclara.api.crm.model.itform;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ITForm010 extends BaseMatterITForm {
	
	// Eligibility
	private Eligibility eligibility;
	
	// ContactDetails
	private ContactDetails contactDetails;
	
	// Name Change
	private NameChange nameChange;
	
	// Personal Details
	private PersonalDetails personalDetails;
	
	// Current Residence and Mailing Address
	private CurrentResidenceAndMailingAddress currentResidenceAndMailingAddress;
	
	// Residence History
	private ResidenceHistory residenceHistory;
	
	// Parents
	private Parents parents;
	
	// Employment and Schools
	private EmploymentsAndSchools employmentsAndSchools;
	
	// Time Spent Outside the U.S.
	private TimeSpentOutsideTheUS timeSpentOutsideTheUS;
	
	// Marital History
	private MaritalHistory maritalHistory;
	
	// Children
	private Children children;
	
	// Additional Questions
	private AdditionalQuestions additionalQuestions;
	
	// TextOfTheOath
	private TextOfTheOath textOfTheOath;
	
	//----------------------------------------------------
	private String createdBy;
	private String updatedBy;
	private Date createdOn = new Date();
	private Date updatedOn = new Date();
	
	//----------------------------------------------------
	
	// Eligibility
	@Data
	class Eligibility {
		private boolean iHaveBeenAPermanentResidentFor5Years;
		private boolean iHaveBeenAPermanentResident3Years;
		private boolean iAmPermanentResident;
		private boolean iAmApplyingOnTheBasisOfQualifyingMilitaryService;
		private boolean other;
		private String explain;
	}
	
	// Contact Details
	@Data
	class ContactDetails {
		private String currentLegalLastName;
		private String currentLegalFirstName;
		private String currentLegalMiddleName;
		private boolean middleNameNotApplicable;
		private String aliasOrNickname;
		private String suffix;
		private boolean yourNameListedDifferentlyOnYourPermanentResidentCard;
		private String exactFirstName;
		private String exactMiddleName;
		private String exactLastName;
		private String maidenName;
		private boolean maidenNameNotApplicable;
		private boolean haveYouUsedAnyotherNamesFromAPreviousMarriage;
		private List<String> provideAllOtherNamesUsed;
		private String eMailAddress;
		private String daytimePhone;
		private String workPhone;
		private String eveningPhone;
		private String mobilePhone;
	}
	
	// Name Change
	@Data
	class NameChange {
		private boolean wouldYouLikeToLegallyChangeYourName;
		private String newLastName;
		private String newFirstName;
		private String newMiddleName;
	}
	
	// Personal Details
	@Data
	class PersonalDetails {
		private boolean wouldYouLikeToLegallyChangeYourName;
		private boolean maleGender;
		private boolean femaleGender;
		private String socialSecurityNumber;
		private boolean iHaveNotBeenIssuedUSASocialSecurityNumber;
		private String alienNumber;
		private boolean alienNumberNotApplicable;
		private Date dateOfBirth;
		private Date dateYouBecameUSPermanentResident;
		private String countryOfBirth;
		private List<String> countryOfCitizenship;
		private String countryWhereMostRecentCitizenshipOrPrmanentResidencyOrLandedImmigrantStatusGranted;
		private int heightInFeet;
		private int heightInInches;
		private int weightInPounds;
		private boolean areYouHispanicOrLatino;
		private Race race;
		private String hairColor;
		private String eyeColor;
		private PhysicalBodyQuestions selectIfAnyApplicable;
		private boolean doYouAaveAPhysicalOrDevelopmentalImpairment;
		private boolean areYou50YearsOfAgeAndLvedInUSAsPermanentResidentForAtleast20Years;
		private boolean areYou55YearsOfAgeAndLvedInUSAsPermanentResidentForAtleast15Years;
		private boolean areYou60YearsOfAgeAndLvedInUSAsPermanentResidentForAtleast20Years;
		
		@Data
		class Race {
			private boolean white;
			private boolean asian;
			private boolean blackOrAfricanAmerican;
			private boolean americanIndianOrAlaskaNative;
			private boolean nativeHawaiianOrOtherPacificIslander;
		}

		@Data
		class PhysicalBodyQuestions {
			private boolean iAmDeafOrHearingImpaired;
			private boolean iUseAWheelchairOrOtherDevice;
			private boolean iAmBlindOrSightImpaired;
			private boolean iRequireAnotherTypeOfAaccommodation;
		}
	}
	
	@Data
	class CurrentResidenceAndMailingAddress {
		private Date dateFrom;
		private Date untilPresent;
		private ClientAddress residentAddress;
		private boolean thisTheSameAddressAsYourCurrentMailingAddress;
		private ClientAddress mailingAddress;
		private String phoneNumber;
	}
	
	@Data
	class ResidenceHistory {
		private String residenceInformationForPast5YearsWithMostRecentAddressFirst;
		private ResidentAddress residentAddress1; 
		private ResidentAddress residentAddress2; 
		private ResidentAddress residentAddress3; 
		private ResidentAddress residentAddress4; 
		private ResidentAddress residentAddress5; 
		
		@Data
		class ResidentAddress {
			private Date dateFrom;
			private Date dateTo;
			private ClientAddress residentAddress;
		}
	}

	@Data
	class Parents {
		private boolean wereYourParentsMarriedBeforeYour18thBirthday;
		private boolean yourMotherUSCitizen;
		private String mothersLastName;
		private String mothersFirstName;
		private String mothersMiddleName;
		private String mothersCountryOfBirth;
		private Date mothersDateOfBirth;
		private boolean yourFatherUSCitizen;
		private String fathersLastName;
		private String fathersFirstName;
		private String fathersMiddleName;
		private String fathersCountryOfBirth;
		private Date fathersDateOfBirth;
	}
	
	@Data
	class EmploymentsAndSchools {
		private String employerOrSchoolName;
		private ClientAddress residentAddress;
		private ClientAddress foreignAddress;
		private Date datesFrom;
		private Date datesTo;
		private String titleOrOccupation;
	}
	
	@Data
	class TimeSpentOutsideTheUS {
		private int daysSpentOutsideUSForPast5Years;
		private int noOfTripsTakenOutsideUSForPast5Years;
		private String listAllTheTripsTakenOutsideUSForPast5YearsWithMostRecentFirst;
		private TripSpent tripSpentDetails1;
		private TripSpent tripSpentDetails2;
		private TripSpent tripSpentDetails3;
		private TripSpent tripSpentDetails4;
		private TripSpent tripSpentDetails5;
		private TripSpent tripSpentDetails6;
		private TripSpent tripSpentDetails7;	
		
		@Data
		class TripSpent {
			private Date dateYouLeftTheUS;
			private Date dateYouReturnedToTheUS;
			private boolean didTripLastMoreThan6Months;
			private String countriesToWhichYouTraveled;
			private int totalDaysOutsideTheUS;
		}
	}
	
	@Data
	class MaritalHistory {
		private MaritalStatus myCurrentMaritalStatusIs;
		private boolean yourSpouseACurrentMemberOfTheUSArmedForces;
		private int howManyTimesHaveYouBeenMarried;
		private SpouseDetails legalNameOfCurrentSpouse;
		private SpouseDetails previousLegalNameOfCurrentSpouse;
		private Date dateOfBirthOfCurrentSpouse;
		private SpouseDetails otherNamesUsedByCurrentSpouse;
		private SpouseInformation spouseInformation;
		private MarriageDetails spousePreviousMarriageDetailsIfMarriedBefore;	
		private MarriageDetails priorSpouseMarriageDetails;	
		
		@Data
		class SpouseInformation {
			private Date spouseDateOfBirth;
			private Date dateOfMarriage;
			private String spouseUSSocialSecurityNumber;
			private boolean doesYourSpouseHaveTheSameHomeAddressAsYou;
			private ClientAddress spouseCompleteAddress;
			private String spousePresentEmployer;
			private boolean yourCurrentSpouseAUSCitizen;
			private SpouseCitizen whenDidYourSpouseBecomeUSCitizen;
			private SpouseCitizenOther ifOtherIsMarked;
			private SpouseCitizenNotApplicable ifNotAppicableIsMarked;
			private SpouseCitizenNo ifSpouseNotAUSCitizen;
			private int howManyTimesHasYourCurrentSpouseBeenMarried;
			
			@Data
			class SpouseCitizen {
				private boolean atBirth;
				private boolean other;
				private boolean notApplicable;
			}
			
			@Data
			class SpouseCitizenOther {
				private Date whenDidYoursSpouseBecameAUSCitizen;
				private String placeWhereSpouseBecameAUSCitizen;
			}			
			
			@Data
			class SpouseCitizenNotApplicable {
				private String spouseCountryOfCitizenship;
				private String spouseUSCISNumber;
				private String spouseImmigrationStatusOfPermanentResident;
				private String spouseImmigrationStatusOfOther;
			}
			
			@Data
			class SpouseCitizenNo {
				private String spouseCountryOfCitizenshipOrNaionality;
				private String aNumber;
				private String currentImmigrationStatus;
			}
		}

		@Data
		class MaritalStatus {
			private boolean singleOrNeverMarrried;
			private boolean married;
			private boolean separated;
			private boolean divorced;
			private boolean widowed;
			private boolean marriageAnnulled;
		}
			
		@Data
		class SpouseDetails {
			private String familyOrLastName;
			private String givenOrFirstName;
			private String middleName;
		}

		@Data
		class MarriageDetails {
			private String familyOrLastName;
			private String givenOrFirstName;
			private String middleName;
			private ImmigrationStatus spousesUSImmigrationStatus;
			private Date dateOfBirth;
			private String countryOfBirth;
			private String countryOfCitizenshipOrNationality;
			private Date dateOfMarriage;
			private Date dateOfEnded;
			private MarriageEnded howMarriageEnded;	
			
			@Data
			class MarriageEnded {
				private String annulled;
				private String divorced;
				private String spouseDeceased;
				private String other;
			}
			
			@Data
			class ImmigrationStatus {
				private String citizenOfUS;
				private String permanentResident;
				private String other;
			}
		}
	}
	
	@Data
	class Children {
		private int totalNoOfChildren;
		private ChildrenDetails child1;
		private ChildrenDetails child2;
		private ChildrenDetails child3;
		private ChildrenDetails child4;
		private ChildrenDetails child5;
		private ChildrenDetails child6;
		private ChildrenDetails child7;
		private ChildrenDetails child8;
		
		@Data
		class ChildrenDetails {
			private String fullName;
			private Date dateOfBirth;
			private String uscis;
			private String countryOfBirth;
			private String location;
			private String relationshipToYou;
		}
	}
	
	@Data
	class AdditionalQuestions {
		private boolean haveYouEverClaimedToBeAUSCitizen;
		private boolean haveYouEverRegisteredToVoteInAnyFederalStateOrLocalElectionInTheUS;
		private boolean haveYouEverVotedInAnyFederalStateOrLocalElectionInTheUS;
		private boolean didYouEverHaveAHereditaryTitleOrAnOrderOfNobilityInAnyForeignCountry;
		private boolean haveYouEverBeenDeclaredLegallyIncompetentOrBeenConfinedToAMentalInstitution;
		private boolean doYouOweAnyFederalStateOrLocalTaxesThatAreOverdue;
		private boolean haveYouEverNotFiledAFederalStateOrLocalTaxReturnSinceYouBecameAPermanentResident;
		private boolean didYouConsiderYourselfANonUSResident;
		private boolean haveYouCalledYourselfANonUSResidentSinceYouBecameAPermanentResident;
		private boolean haveYouEverBeenAMemberOfOrInAnyWayAssociatedWithAnyOrganizationInTheUSOrOtherCountries;
		private GroupDetails memberGroupDetail1;
		private GroupDetails memberGroupDetail2;
		private GroupDetails memberGroupDetail3;
		private GroupDetails memberGroupDetail4;
		private GroupDetails memberGroupDetail5;
		private GroupDetails memberGroupDetail6;			
		private boolean haveYouEverBeenAMemberOfTheCommunistParty;
		private boolean haveYouEverBeenAMemberOfAnyOtherTotalitarianParty;
		private boolean haveYouEverBeenAMemberOfATerroristOrganization;
		private boolean haveYouEverAdvocatedTheOverthrowOfAnyGovernmentByForceOrViolence;
		private boolean haveYouEverpersecutedAnyPersonBecauseOfRaceReligion;
		private boolean didYouWorkOrAssociateWithNaziGovernmentOfGermanyBetweenMarch231933AndMay81945;
		private boolean didYouWorkOrAssociateWithAnyGovernmentInAnyAreaBetweenMarch231933AndMay81945;
		private boolean didYouWorkOrAssociateWithAnyGermanNaziOrSSMilitaryUnitBetweenMarch231933AndMay81945;
		private boolean wereYouEverInvolvedInGenocide;
		private boolean wereYouEverInvolvedInTorture;
		private boolean wereYouEverInvolvedInKillingOrTryingToKillSomeone;
		private boolean wereYouEverInvolvedInBadlyHurtingOrTryingToHurtApersonOnPurpose;
		private boolean wereYouEverInvolvedInForcingOrTryingToForceSomeoneToHavAnyKindOfSexualContactOrRelations;
		private boolean wereYouEverInvolvedInNotLettingSomeonePracticeHisOrHerReligion;
		
		private boolean wereYouEverMemberOrServeInMilitaryUnit;
		private boolean wereYouEverMemberOrServeInParamilitaryUnit;
		private boolean wereYouEverMemberOrServeInPoliceUnit;
		private boolean wereYouEverMemberOrServeInSelfDefenseUnit;
		private boolean wereYouEverMemberOrServeInVigilanteUnit;
		private boolean wereYouEverMemberOrServeInRebelGroup;
		private boolean wereYouEverMemberOrServeInGuerillaGroup;
		private boolean wereYouEverMemberOrServeInMilitia;
		private boolean wereYouEverMemberOrServeInInsurgentOrganization;
		
		private boolean wereYouEverWorkerVolunteerSoldierServeInPrisonOrJail;
		private boolean wereYouEverWorkerVolunteerSoldierServeInPrisonCamp;
		private boolean wereYouEverWorkerVolunteerSoldierServeInDetentionFacility;
		private boolean wereYouEverWorkerVolunteerSoldierServeInLaborCamp;
		private boolean wereYouEverWorkerVolunteerSoldierServeInAnyOtherPlaceWherePeopleWereForcedToStay;
		
		private boolean wereYouEverPartOrHelpedOfAnyGroupOrOrganizationUsedAWeaponAgainstAnyPerson;
		private boolean didYouEverUsedOrHelpedTheGroupUsingAWeaponAgainstAnyPerson;
		private boolean didYouTellAnyOtherPersonThatYouWouldUseAWeaponAgainstThatPerson;
		
		private boolean didYouEverSellGiveProvideOrHelpWithWeaponsToAnyPerson;
		private boolean doYouKnowThatThisPersonIsGoingToUseWeaponsAgainstAnotherPerson;
		private boolean doYouKnowThatThisPersonIsGoingToSellOrGiveWeaponsToAnotherPerson;
		
		private boolean didYouEverReceiveAnyTypeOfMilitaryParamilitaryOrWeaponsTraining;
		private boolean didYouEverRecruitEnlistConscriptOrUseAnyPersonUnderAge15ToServeOrHelpAnArmedForcesGroup;
		private boolean didYouEverUseAnyPersonUnderAge15ToDoAnythingThatHelpedOrSupportedPeopleInCombat;
		
		private boolean haveYouEverCommittedAssistedInCommittingOrAttemptedToCommitACrimeOrOffenseForWhichYouWereNotArrested;
		private boolean haveYouEverBeenArrestedCitedOrDetainedByAnyLawEnforcementOfficer;
		private boolean haveYouEverBeenChargedWithCommittedAttemptingOrAssistingInCommittingAcrimeOrOffense;
		private boolean haveYouEverBeenConvictedOfAcrimeOrOffense;
		private boolean haveYouEverBeenPlacedInAnAlternativeSentencingOrARehabilitativeProgram; 
		private boolean haveYouEverReceivedASuspendedSentenceBeenPlacedOnProbationOrBeenParoled;
		private boolean haveYouCompletedTheProbationOrParole;
		private boolean haveYouEverBeenInJailOrPrison;
		private boolean howLongWereYouInJailOrPrison;
		
		private ArrestDetails charges1;
		private ArrestDetails charges2;
		private ArrestDetails charges3;
		private ArrestDetails charges4;
		private ArrestDetails charges5;
		private ArrestDetails charges6;
		
		private boolean haveYouEverBeenAHabitualDrunkard;
		private boolean haveYouEverBeenAProstituteOrProcuredAnyoneForProstitution;
		private boolean haveYouEverBeenSoldOrSmuggledControlledSubstancesIllegalDrugsOrNarcotics;
		private boolean haveYouEverBeenMarriedToMorethanOnePersonAttheSameTime;
		private boolean haveYouEverBeenMarriedSomeoneInOrderToObtainAnImmigrationBenefit;
		private boolean haveYouEverBeenHelpedAnyoneEnterOrTryToEnterTheUSIllegally;
		private boolean haveYouEverBeenGambledIllegallyOrReceivedIncomeFromIllegalGambling;
		private boolean haveYouEverBeenFailedToSupportYourDependentsOrToPayAlimony;
		private boolean haveYouEverBeenMadeAnyMisrepresentationToObtainAnyPublicBenefitInTheUS;
		
		private boolean haveYouEverGivenFalseOrMisleadedInformationAboutUSGovernmentOfficials;
		private boolean haveYouEverLiedToAnyUSGovernmentOfficialToGainEntryOrAdmissionIntoTheUS;
		private boolean haveYouEverBeenRemovedExcludedOrDeportedFromTheUS;
		private boolean haveYouEverBeenOrderedRemovedExcludedOrDeportedFromTheUS;
		private boolean haveYouEverBeenPlacedInRemovalExclusionRescissionOrDeportationProceedings;
		private boolean areRemovalExclusionRescissionOrDeportationProceedingsCurrentlyPendingAgainstYou;
		private boolean haveYouEverServedInTheUSArmedForces;
		private boolean areYouCurrentlyAMemberOfTheUSArmedForces;
		private boolean areYouScheduledToDeployOverseasWithinTheNext3Months;;
		private boolean areYouCurrentlyStationedOverseas;
		private boolean haveYouEverBeenCourtMartialedAdministrativelySeparatedOrDisciplinedInTheUSArmedForces;
		private boolean haveYouEverBeenDischargedFromTrainingOrServiceInTheUSArmedForcesBecauseYouWereAnAlien;
		private boolean haveYouEverLeftTheUSToAvoidBeingDraftedIntoTheUSArmedForces;
		private boolean haveYouEverappliedForAnyKindOfExemptionFromMilitaryServiceInTheUSArmedForces;
		private boolean haveYouEverDesertedFromTheUSArmedForces;
		private boolean areYouAMaleWhoLivedInTheUSAtAnyTimeBetweenYour18thAnd26thBirthdays;
		private Date selectiveServiceSystemRegisteredDate;
		private String selectiveServiceNumber;
		private String reasonForNotRegisteringSelectiveServiceSystem;
		
		@Data
		class GroupDetails {
			private String nameOfGroup;
			private String purposeOfGroup;
			private Date membershipFrom;
			private Date membershipTo;
		}
			
		@Data
		class ArrestDetails {
			private String whyWhereYouArrestedCitiedDetainedOrCharged;
			private Date dateArrestedCitiedDetainedOrCharged;
			private String whereWereYouArrestedCitiedDetainedOrCharged;
			private String outcomeOrDispositionOfTheArrestCitationDetentionOrCharge;
		}
	}
		
	@Data
	class TextOfTheOath {
		private boolean doYousupporttheConstitutionandformofgovernmentOfTheUS;
		private boolean doYouUnderstandTheFullOathOfAllegianceToTheUS;
		private boolean areYouWillingToTakeTheFullOathOfAllegianceToTheUS;
		private boolean areYouWillingToBearArmsOnBehalfOfTheUS;
		private boolean areYouWillingToperformNoncombatantServicesInTheUSArmedForces;
		private boolean areYouWillingToperformWorkOfNationalImportanceUnderCivilianDirection;
		private boolean areYouWillingToGiveUpAnyInheritedTitleOrorderOfNobilityThatYouHaveInAForeignCountry;
	}	
}