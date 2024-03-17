package com.mnrclara.api.crm.model.itform;


import java.util.Date;

import lombok.Data;

@Data
public class ImmigrationHistoryQuestions {

	/*
	 * IMMIGRATION HISTORY 
	 */
	/*----------Section-1-------------------------*/
	private String passportIssuedCountryNames;
	private PassportStolenOrLost passportStolenOrLostDetails;
	
	/*----------Section-2-------------------------*/
	private LivingLocation livingLocationDetails;

	/*----------Section-3-------------------------*/
	private VisaIssuance visaIssuanceDetails;
	
	/*----------Section-4-------------------------*/
	private VisaDeniedOrRevoked visaDeniedOrRevoked;
	
	/*----------Section-5-------------------------*/
	private GreenCardInfo greenCardInfo;
	
	/*----------Section-6-------------------------*/
	private DetainedOrInterrogatedByAnImmigrationOfficer detainedOrInterrogatedByAnImmigrationOfficer;
	
	/*----------Section-7-------------------------*/
	private USAEntryDenied usaEntryDeniedDetails;
	
	/*----------Section-8-------------------------*/
	private DeportationProcess deportationProcessDetails;
	
	/*----------Section-9-------------------------*/
	private DeportedOrExpelled deportedOrExpelledDetails;
	
	/*----------Section-10-------------------------*/
	private DriverLicenses driverLicensesDetails;
	
	/*----------Section-11-------------------------*/
	private TrafficTickets trafficTicketsDetails;
	
	/*----------Section-12-------------------------*/
	private GovernmentAssistance governmentAssistanceDetails;
}

//Has your passport ever been stolen or lost?
@Data
class PassportStolenOrLost {
	private boolean HasYourPassportEverbeenStolenOrLost;
	private Date stolenDate;
	private boolean wasRepotedToPolice;
}

@Data 
class LivingLocation {
	private boolean areYouCurrentlyLivingInUSA;
	private String stayingSinceMonth;
	private String stayingSinceYear;
	private String nameOflocation;
}

@Data
class VisaIssuance {
	private boolean haveYouEverHadUSVisa;
	private String consulateThatIssuedVISA;
	private Date dateOfIssuance;
	private Date dateOfExpiration;
}

@Data
class VisaDeniedOrRevoked {
	private boolean haveYouEverbeenDeniedOrRevokedAmericanVisa;
	private String whereDidYourVisaWasDenied;
	private boolean wasDeniedMoreThanOnce;
	private int howManyTimesGotDenied;
	private String visaClassification;
}

@Data
class GreenCardInfo {
	private boolean haveYouEverAppliedForGreenCardBefore;
	private Date whenDidYouApply;
	private String result;
}

@Data
class DetainedOrInterrogatedByAnImmigrationOfficer {
	private boolean haveYouEverBeenDetainedOrInterrogated;
	private String whereDidDetainOrInterrogate;
	private String month;
	private String year;
}

@Data
class USAEntryDenied {
	private boolean haveYouBeenDeniedToEnterTheUSA;
	private String whereDidItDenied;
	private String month;
	private String year;
	private boolean wereYouDeniedMorethanOnce;
	private int howManyTimes;
}

@Data
class DeportationProcess {
	private boolean haveYouEverBeenInDeportationProcess;
	private String whereDidItHappened;
	private String month;
	private String year;
	private String finalResult;
	private boolean haveYouBeenDeportedMorethanOnce;
	private int howManyTimes;
}

@Data
class DeportedOrExpelled {
	private boolean haveYouEverBeenDeportedOrExpelled;
	private String whereDidItHappened;
	private String month;
	private String year;
	private String reasonOfDetention;
	private boolean wasArrested;
	private int howManydays;
}

@Data
class DetainedAccusedOrConvicted  {
	private boolean haveYouEverbeenDetainedAccusedOrConvictedForFelony;
	private String whereDidItHappened;
	private String month;
	private String year;
	private String whatKindOfFelony;
	private String finalResult;
	private boolean didYouGoToTrial;
}
		
@Data
class DriverLicenses {
	private boolean doYouHaveDriverLicenses;
	private String state;
	private String month;
	private String monthOfExpire;
	private String yearOfExpire;
}

@Data
class TrafficTickets {
	private boolean haveYouEverHadTrafficTickets;
	private String when;
	private String where;
	private Double finePaid;
} 

@Data
class GovernmentAssistance {
	private boolean haveYouEverReceivedGovernmentAssistance;
	private String sinceWhen;
} 