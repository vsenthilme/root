package com.mnrclara.api.management.model.matteritform;

import java.util.Date;

import lombok.Data;

@Data
public class ITForm009 extends BaseMatterITForm {
	
	// Personal Information
	private PersonalInformation personalInfo;
	private ResidentialHistory residentialHistory;
	private EmploymentHistory employmentHistory;
	private PublicBenefits publicBenefits;
	private SpouseInformation spouseInformation;
	private ChildrenInformation childrenInformation;
	private FamilyInformation familyInformation;
	
	//----------------------------------------------------
	private Long status;
	private Date sentOn = new Date();
	private String createdBy;
	private String updatedBy;
	private Date createdOn = new Date();
	private Date updatedOn = new Date();
	
	//----------------------------------------------------
	
	@Data
	class PersonalInformation {
		private Name name;
		private boolean male;
		private boolean female;
		private double height;
		private double weight;
		private String eyeColor;
		private String hairColor;
		private Date DateOfBirth;
		private String nationality;		
		private String socialSecurityNumber;
		private PlaceOfBirth placeOfBirth;
		private Passport passport;
		private PhoneNumber phoneNumber;
		private EntryInfo entryInfo;
		private VisaDenied visaDenied;
		private VisaImmigrant visaImmigrant;
		
		@Data
		class Name {
			private String name;
			private String middleName;
			private String lastName;
			private String otherNamesUsed;
			private String legalNameAsItAppearsInPassportOrBirthCertificate;
		}
			
		@Data 
		class PlaceOfBirth {
			private String city;
			private String state;
			private String country;
		}
		
		@Data 
		class Passport {
			private String passportNumber;
			private String cityOfIssuance;
			private String countryOfIssuance;
			private Date DateOfIssuance;
			private Date expirationDate;
		}
			
		@Data 
		class PhoneNumber {
			private String telephoneNumber;
			private String cellPhone;
			private String Work;
		}
		
		@Data 
		class EntryInfo	{
			private String monthOfFirstVisit;
			private int yearOfFirstVisit;
			private String placeOfFirstVisit;
			private String monthOfLastVisit;
			private int yearOfLastVisit;
			private String placeOfLastVisit;
		}
			
		@Data 
		class VisaDenied {
			private boolean haveYouEverbeenDeniedVisa;
			private Date when;
			private String where;
			private String typeOfVisa;
		}
		
		@Data 
		class VisaImmigrant {
			private boolean hasAnyoneEverFiledImmigrantVisa;
			private Date when;
			private boolean wasVisaGranted;
			private String typeOfVisa;
			private String reasonForVisaDenial;
		}
	}
		
	/*
	 * Residential History: FOR THE LAST 10 YEARS
	 */
	@Data 
	class ResidentialHistory {
		private AddressDetails currentAddress;
		private AddressDetails previousAddress1;
		private AddressDetails previousAddress2;
		private AddressDetails previousAddress3;
		private AddressDetails previousAddress4;		
	
		@Data 
		class AddressDetails {
			private String address;
			private String fromMonthYear;
			private String toMonthYear;
		}
	}
		
	@Data 
	class EmploymentHistory {
		private EmploymentDetails currentEmployment;
		private EmploymentDetails previousEmployment1;
		private EmploymentDetails previousEmployment2;
		private EmploymentDetails previousEmployment3;
		private EmploymentDetails previousEmployment4;			
			
		@Data 
		class EmploymentDetails {
			private String companyAndWagesPerHour;
			private String address;
			private String telephone;
			private String occupation;
			private Date employmentDateFrom;
			private Date employmentDateTo;
		}
	}
			
	/*
	 * Use of Public Benefits: 
	 */
	@Data 
	class PublicBenefits {
		private BenefitDetails benefit1;
		private BenefitDetails benefit2;			
			
		@Data 
		class BenefitDetails {
			private String typeOfPublicBenefit;
			private Date DatesPublicBenefitReceived;
			private String nameOfPersonReceivingBenefit;
		}	
	}
			
	/*
	 * Information About your Spouse
	 */
	@Data 
	class SpouseInformation {
		private String name;
		private String namebeforeMarriage;
		private String placeOfMarriage;
		private Date dateOfMarriage;
		private String spouseAddress;
		private Date dateOfBirth;
		private String placeOfBirth;
		private String alienRegistrationNumber;
		private String immigrationStatus;
		private String placeOfArrivalIntoTheUS;
		private Date dateOfArrival;
		private Date dateHeOrSheWasNaturalized;
		private String employersName;
		private String employersAddress;
		private String earingsPerWeek;
	}
	
	/*
	 * Information About your Children:
	 */
	@Data 
	class ChildrenInformation {
		private PersonDetails childrenInfo;
		private String estimatedTotalAssets;
		private String estimatedAverageWeeklyEarnings;
		
		@Data 
		class PersonDetails {
			private String name;
			private Date dateOfBirth;
			private String placeOfBirth;
			private String placeOfResidence;
			private String alienRegistrationNumber;
			private String immigrationStatus;
		}
	}
	
	@Data 
	class FamilyInformation {
		private Family mother;
		private Family father;
		private Family brotherOrSister1;
		private Family brotherOrSister2;
		private Family brotherOrSister3;
		private Family brotherOrSister4;
		
		private Family grandParents1;
		private Family grandParents2;
		private Family grandParents3;
		private Family grandParents4;
		
		private Family auntOrUncle1;
		private Family auntOrUncle2;
		private Family auntOrUncle3;
		private Family auntOrUncle4;
		
		@Data 
		class Family {
			private PersonDetails personDetails;
			private Deceased deceased;
			
			@Data 
			class PersonDetails {
				private String name;
				private Date dateOfBirth;
				private String placeOfBirth;
				private String placeOfResidence;
				private String alienRegistrationNumber;
				private String immigrationStatus;
			}
			
			@Data 
			class Deceased {
				private Date deceasedDate;
				private String placeOfDeath;
			}
		}
	}
}