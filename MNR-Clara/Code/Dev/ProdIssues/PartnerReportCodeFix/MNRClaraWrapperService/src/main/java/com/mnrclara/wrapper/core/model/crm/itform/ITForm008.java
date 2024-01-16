package com.mnrclara.wrapper.core.model.crm.itform;

import java.util.Date;

import lombok.Data;

@Data
public class ITForm008 extends BaseMatterITForm {
	
	private PersonalInfo personalInfoDetails;
	
	// IF MARRIED OR ENGAGED, COMPLETE THIS SECTION:
	private SpouseOrFiancePersonalInfo spouseOrFiancePersonalInfo;
	
	// IF YOU ARE DIVORCED OR A WIDOWER
	private DiovercedOrWidowerMaritalStatus diovercedOrWidowerMaritalStatus;
	
	//----------PARENTS/SIBLINGS INFORMATION-------------------------
	private ParentsAndSiblingsInfo parentsAndSiblingsInfoDetails;
	
	// INFORMATION ABOUT YOUR CHILDREN
	private ClientChildrenInfo child1;
	private ClientChildrenInfo child2;
	private ClientChildrenInfo child3;
	private ClientChildrenInfo child4;
	private ClientChildrenInfo child5;
	private ClientChildrenInfo child6;
	
	// IMMIGRATION HISTORY 
	private ImmigrationHistoryQuestions immigrationHistoryQuestions;
	
	// HAS ANYONE EVER FILED A PETITION FOR YOU, YOUR PARENTS OR YOUR SPOUSE? 
	private Petition PetitionDetails;
	
	// INFORMATION OF THE ARRIVALS/DEPARTURES TO THE U.S.A
	private ArrivalAndDeparture arrivalAndDepartureDetails;
	
	// VictimOfCrime
	private VictimOfCrime victimOfCrimeDetails;
	
	// HISTORY OF RESIDENCE IN THE UNITED STATES
	private ResidentAddress currentLocationResidentAddress;
	
	// PLEASE LIST THE ADDRESSES YOU RESIDED IN THE US WITHIN THE PAST 5 YEARS:
	private ResidentAddress previousResidentAddress1;
	private ResidentAddress previousResidentAddress2;
	private ResidentAddress previousResidentAddress3;
	private ResidentAddress previousResidentAddress4;
	private ResidentAddress previousResidentAddress5;
	private ResidentAddress previousResidentAddress6;
	private ResidentAddress previousResidentAddress7;
	private ResidentAddress previousResidentAddress8;
	private ResidentAddress previousResidentAddress9;
	private ResidentAddress previousResidentAddress10;
	private ResidentAddress previousResidentAddress11;
	private ResidentAddress previousResidentAddress12;
	private ResidentAddress previousResidentAddress13;
	
	// EMPLOYMENT HISTORY
	private EmploymentHistory employmentHistoryDetails;
	
	// ADDITIONAL INFORMATION 
	private AdditionalInfo additionalInfo;
	//----------------------------------------------------
	
	private String createdBy;
	private String updatedBy;
	private Date createdOn = new Date();
	private Date updatedOn = new Date();

	//------------------------------------------------------------
	
	@Data
	class SpouseOrFiancePersonalInfo extends ClientPersonalInfo {
		private Date dateOfMarriage;
		private String placeOfMarriage;
		private String placeOfResidence;
	}
	
	@Data
	class PersonalInfo {
		private ClientPersonalInfo clientPersonalInfo;
		private String placeOfResidence;
		private Height heightDetails;
		private long weightInPounds;
		private Race raceDetails;
		private String maritalStatus;
	}
	
	@Data 
	class ParentsAndSiblingsInfo {
		// DO YOU HAVE A FATHER OR MOTHER WITH AMERICAN CITIZENSHIP
		private String americanCitizenshipObtainedThrough;
		private int yearOfCitizenship;
		private int ageOfClient; // How old were you?
		
		// IS YOUR FATHER OR MOTHER AN AMERICAN RESIDENT?
		private String americanResidentObtainedThrough;
		private int yearOfResidence;
		
		// INFORMATION ABOUT YOUR FATHER AND MOTHER
		private ClientPersonalInfo fatherInformation;
		private ClientPersonalInfo motherInformation;
		
		/*
		 * Are any of them currently in the U.S.A or do they have an American Residence, or American Citizenship
		 */
		private AmericanResidenceOrAmericanCitizenship americanResidenceOrAmericanCitizenshipDetails;
		
		// IF YOU HAVE ANY SIBLINGS WITH AMERICAN CITIZENSHIP OR RESIDENT CARD,
		private String siblingsObtainedAmericanCitizenshipOrResidentCardThrough;
	}
	
	@Data
	class Height {
		private int feet;
		private float inches;
		private String eyeColor;
		private String hairColor;
	}
	
	@Data
	class Race {
		private boolean white;
		private boolean asian;
		private boolean blackOrAfricanAmerican;
		private boolean americanIndianOrAlaskaNative;
		private boolean hawaiianNativeOrOtherPacificIsland;
	}
	
	@Data
	class AmericanResidenceOrAmericanCitizenship {
		private boolean hasHusbandOrWifeHold;
		private boolean hasFatherOrMotherHold;
		private boolean hasBoyfriendOrGirlfriendHold;
		private boolean hasSonOrDaughterHold;
		private boolean hasBrotherOrSisterHold;
	}
	
	@Data
	class Petition {
		private boolean hasAnyoneFiledPetition;
		private String petitionFileForYouOrParentsORSpouse;
		private String when;
		private String month;
		private String year;
		private String underWhoseName;
		private String whoFiledPetition;
		private String result;
		private boolean doYouHaveAnyRelativesWhoBelongToTheArmyOrAreVeteran;
		private String whatIsTheRelation;
	}
	
	@Data
	class ArrivalAndDeparture {
		
		private Arrival firstArrival;
		private Arrival lastArrival;
		private String whenDidYouComeToLiveToUSA;
		private String whereDidYouArriveInMexicanTown;
		private String whereDidYouArriveInCityOfUSA;
		private Dates arrivals;
		private Dates departures;
		
		@Data
		class Arrival {
			private String arrivalMonth;
			private String arrivalYear;
			private String placeOfArrival;
			private boolean wereYouInspectedByAnImmigrationOfficer;
		}
		
		@Data
		class Dates {
			private Date date1;
			private Date date2;
			private Date date3;
			private Date date4;
			private Date date5;
		}
	}
	
	@Data
	class VictimOfCrime {
		private String haveYouOrParentsOrSpouseEverbeenVicimOfCrime;
		private Date dateOfOccurred;
		private boolean wasItReported;
		private String summaryOfTheEvent;
	}
	
	@Data
	class ResidentAddress {
		private ClientAddress fullAddress;
		private Date livingFrom;
		private Date livingTill;
		private boolean wasTheResidenceUnderYourName;
		private String whoDidYouLiveWith;
	}
	
	@Data
	class EmploymentHistory {
		private boolean areYouCurrentlyWorkingInUSAWithSSN;
		private boolean isSSNvalid;
		private String occupation;
		private boolean dpYouPayTaxes;
		private Date sinceWhen;
		
		// List all the employments you have worked at within the past 5 years. Starting with your current employment
		private Employment employmentDetails1;
		private Employment employmentDetails2;
		private Employment employmentDetails3;
		private Employment employmentDetails4;
		private Employment employmentDetails5;
		private Employment employmentDetails6;
		private Employment employmentDetails7;
		private Employment employmentDetails8;
		private Employment employmentDetails9;
		private Employment employmentDetails10;
		
		@Data
		class Employment {
			private String nameOfTheCompany;
			private ClientAddress fullAddress;
			private String telephoneNumber;
			private String nameAndSurnameOfYourSupervisor;
			private Date employmentFrom;
			private Date employmentTill;
			private boolean doYouHaveAnyEvidenceOfEmployment;
		}
	}
	
	@Data
	class AdditionalInfo {
		private boolean haveYouEverbeenAMemberOfAnyOrgOrGroup;
		private String nameOfTheGroup;
		private String purposeOfTheGroup;
		private Date sinceFrom;
		private Date until;
		
		private boolean haveYouEverbeenPartOfTheMilitary;
		private String country;
		private String range;
		private Date militaryPeriodsinceFrom;
		private Date militaryPeriodUntil;
	}
}