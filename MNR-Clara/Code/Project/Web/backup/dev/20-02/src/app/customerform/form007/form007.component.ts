
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { MatterIntakeService } from '../matter-intake.service';
import { Location } from "@angular/common";
import { Form007Service } from './form007.service';

@Component({
  selector: 'app-form007',
  templateUrl: './form007.component.html',
  styleUrls: ['./form007.component.scss'],
})
export class Form007Component implements OnInit, OnDestroy {
  screenid: 1121 | undefined;
  btntext = 'Submit';
  todaydate = new Date();
  AdditionalInfo = this.fb.group({
    country: [],
    haveYouEverbeenAMemberOfAnyOrgOrGroup: [],
    haveYouEverbeenPartOfTheMilitary: [],
    militaryPeriodUntil: [],
    militaryPeriodsinceFrom: [],
    nameOfTheGroup: [],
    purposeOfTheGroup: [],
    range: [],
    sinceFrom: [],
    until: [],
  }

  ); Arrivals = this.fb.group({
    date1: [],
    date2: [],
    date3: [],
    date4: [],
    date5: [],
  }

  ); Departures = this.fb.group({
    date1: [],
    date2: [],
    date3: [],
    date4: [],
    date5: [],
  }

  ); FirstArrival = this.fb.group({
    arrivalMonth: [],
    arrivalYear: [],
    placeOfArrival: [],
    wereYouInspectedByAnImmigrationOfficer: [],
  }

  ); LastArrival = this.fb.group({
    arrivalMonth: [],
    arrivalYear: [],
    placeOfArrival: [],
    wereYouInspectedByAnImmigrationOfficer: [],
  }

  ); ArrivalAndDepartureDetails = this.fb.group({
    arrivals: this.Arrivals,
    departures: this.Departures,
    firstArrival: this.FirstArrival,
    lastArrival: this.LastArrival,
    whenDidYouComeToLiveToUSA: [],
    whereDidYouArriveInCityOfUSA: [],
    whereDidYouArriveInMexicanTown: [],
  }

  ); Child1 = this.fb.group({
    age: [],
    cityAndCountryOfBirth: [],
    dateOfBirth: [],
    doesChildLiveWithYou: [],
    nameOfChild: [],
  }

  ); Child2 = this.fb.group({
    age: [],
    cityAndCountryOfBirth: [],
    dateOfBirth: [],
    doesChildLiveWithYou: [],
    nameOfChild: [],
  }

  ); Child3 = this.fb.group({
    age: [],
    cityAndCountryOfBirth: [],
    dateOfBirth: [],
    doesChildLiveWithYou: [],
    nameOfChild: [],
  }

  ); Child4 = this.fb.group({
    age: [],
    cityAndCountryOfBirth: [],
    dateOfBirth: [],
    doesChildLiveWithYou: [],
    nameOfChild: [],
  }

  ); Child5 = this.fb.group({
    age: [],
    cityAndCountryOfBirth: [],
    dateOfBirth: [],
    doesChildLiveWithYou: [],
    nameOfChild: [],
  }

  ); Child6 = this.fb.group({
    age: [],
    cityAndCountryOfBirth: [],
    dateOfBirth: [],
    doesChildLiveWithYou: [],
    nameOfChild: [],
  }

  ); FullAddress = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); CurrentLocationResidentAddress = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); DiovercedOrWidowerMaritalStatus = this.fb.group({
    cityAndCountryOfBirth: [],
    countryOfCitizenship: [],
    dateOfMarriage: [],
    dateOfMarriageTermination: [],
    nameOfExspouse: [],
    reasonForMarriageEnd: [],
  }

  ); CurrentEmployment = this.fb.group({
    address: [],
    companyAndWagesPerHour: [],
    employmentDateFrom: [],
    employmentDateTo: [],
    occupation: [],
    telephone: [],
  }

  ); FullAddress2 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); EmploymentDetails1 = this.fb.group({
    doYouHaveAnyEvidenceOfEmployment: [],
    employmentFrom: [],
    employmentTill: [],
    fullAddress: this.FullAddress2,
    nameAndSurnameOfYourSupervisor: [],
    nameOfTheCompany: [],
    telephoneNumber: [],
  }

  ); FullAddress3 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); EmploymentDetails10 = this.fb.group({
    doYouHaveAnyEvidenceOfEmployment: [],
    employmentFrom: [],
    employmentTill: [],
    fullAddress: this.FullAddress3,
    nameAndSurnameOfYourSupervisor: [],
    nameOfTheCompany: [],
    telephoneNumber: [],
  }

  ); FullAddress4 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); EmploymentDetails2 = this.fb.group({
    doYouHaveAnyEvidenceOfEmployment: [],
    employmentFrom: [],
    employmentTill: [],
    fullAddress: this.FullAddress4,
    nameAndSurnameOfYourSupervisor: [],
    nameOfTheCompany: [],
    telephoneNumber: [],
  }

  ); FullAddress5 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); EmploymentDetails3 = this.fb.group({
    doYouHaveAnyEvidenceOfEmployment: [],
    employmentFrom: [],
    employmentTill: [],
    fullAddress: this.FullAddress5,
    nameAndSurnameOfYourSupervisor: [],
    nameOfTheCompany: [],
    telephoneNumber: [],
  }

  ); FullAddress6 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); EmploymentDetails4 = this.fb.group({
    doYouHaveAnyEvidenceOfEmployment: [],
    employmentFrom: [],
    employmentTill: [],
    fullAddress: this.FullAddress6,
    nameAndSurnameOfYourSupervisor: [],
    nameOfTheCompany: [],
    telephoneNumber: [],
  }

  ); FullAddress7 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); EmploymentDetails5 = this.fb.group({
    doYouHaveAnyEvidenceOfEmployment: [],
    employmentFrom: [],
    employmentTill: [],
    fullAddress: this.FullAddress7,
    nameAndSurnameOfYourSupervisor: [],
    nameOfTheCompany: [],
    telephoneNumber: [],
  }

  ); FullAddress8 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); EmploymentDetails6 = this.fb.group({
    doYouHaveAnyEvidenceOfEmployment: [],
    employmentFrom: [],
    employmentTill: [],
    fullAddress: this.FullAddress8,
    nameAndSurnameOfYourSupervisor: [],
    nameOfTheCompany: [],
    telephoneNumber: [],
  }

  ); FullAddress9 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); EmploymentDetails7 = this.fb.group({
    doYouHaveAnyEvidenceOfEmployment: [],
    employmentFrom: [],
    employmentTill: [],
    fullAddress: this.FullAddress9,
    nameAndSurnameOfYourSupervisor: [],
    nameOfTheCompany: [],
    telephoneNumber: [],
  }

  ); FullAddress10 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); EmploymentDetails8 = this.fb.group({
    doYouHaveAnyEvidenceOfEmployment: [],
    employmentFrom: [],
    employmentTill: [],
    fullAddress: this.FullAddress10,
    nameAndSurnameOfYourSupervisor: [],
    nameOfTheCompany: [],
    telephoneNumber: [],
  }

  ); FullAddress11 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); EmploymentDetails9 = this.fb.group({
    doYouHaveAnyEvidenceOfEmployment: [],
    employmentFrom: [],
    employmentTill: [],
    fullAddress: this.FullAddress11,
    nameAndSurnameOfYourSupervisor: [],
    nameOfTheCompany: [],
    telephoneNumber: [],
  }

  ); PreviousEmployment1 = this.fb.group({
    address: [],
    companyAndWagesPerHour: [],
    employmentDateFrom: [],
    employmentDateTo: [],
    occupation: [],
    telephone: [],
  }

  ); PreviousEmployment2 = this.fb.group({
    address: [],
    companyAndWagesPerHour: [],
    employmentDateFrom: [],
    employmentDateTo: [],
    occupation: [],
    telephone: [],
  }

  ); PreviousEmployment3 = this.fb.group({
    address: [],
    companyAndWagesPerHour: [],
    employmentDateFrom: [],
    employmentDateTo: [],
    occupation: [],
    telephone: [],
  }

  ); PreviousEmployment4 = this.fb.group({
    address: [],
    companyAndWagesPerHour: [],
    employmentDateFrom: [],
    employmentDateTo: [],
    occupation: [],
    telephone: [],
  }

  ); EmploymentHistoryDetails = this.fb.group({
    areYouCurrentlyWorkingInUSAWithSSN: [],
    currentEmployment: this.CurrentEmployment,
    dpYouPayTaxes: [],
    employmentDetails1: this.EmploymentDetails1,
    employmentDetails10: this.EmploymentDetails10,
    employmentDetails2: this.EmploymentDetails2,
    employmentDetails3: this.EmploymentDetails3,
    employmentDetails4: this.EmploymentDetails4,
    employmentDetails5: this.EmploymentDetails5,
    employmentDetails6: this.EmploymentDetails6,
    employmentDetails7: this.EmploymentDetails7,
    employmentDetails8: this.EmploymentDetails8,
    employmentDetails9: this.EmploymentDetails9,
    occupation: [],
    previousEmployment1: this.PreviousEmployment1,
    previousEmployment2: this.PreviousEmployment2,
    previousEmployment3: this.PreviousEmployment3,
    previousEmployment4: this.PreviousEmployment4,
    sinceWhen: [],
    ssnvalid: [],
  }

  ); DeportationProcessDetails = this.fb.group({
    finalResult: [],
    haveYouBeenDeportedMorethanOnce: [],
    haveYouEverBeenInDeportationProcess: [],
    howManyTimes: [],
    month: [],
    whereDidItHappened: [],
    year: [],
  }

  ); DeportedOrExpelledDetails = this.fb.group({
    haveYouEverBeenDeportedOrExpelled: [],
    howManydays: [],
    month: [],
    reasonOfDetention: [],
    wasArrested: [],
    whereDidItHappened: [],
    year: [],
  }

  ); DetainedOrInterrogatedByAnImmigrationOfficer = this.fb.group({
    haveYouEverBeenDetainedOrInterrogated: [],
    month: [],
    whereDidDetainOrInterrogate: [],
    year: [],
  }

  ); DriverLicensesDetails = this.fb.group({
    doYouHaveDriverLicenses: [],
    month: [],
    monthOfExpire: [],
    state: [],
    yearOfExpire: [],
  }

  ); GovernmentAssistanceDetails = this.fb.group({
    haveYouEverReceivedGovernmentAssistance: [],
    sinceWhen: [],
  }

  ); GreenCardInfo = this.fb.group({
    haveYouEverAppliedForGreenCardBefore: [],
    result: [],
    whenDidYouApply: [],
  }

  ); LivingLocationDetails = this.fb.group({
    areYouCurrentlyLivingInUSA: [],
    nameOflocation: [],
    stayingSinceMonth: [],
    stayingSinceYear: [],
  }

  ); PassportStolenOrLostDetails = this.fb.group({
    hasYourPassportEverbeenStolenOrLost: [],
    stolenDate: [],
    wasRepotedToPolice: [],
  }

  ); TrafficTicketsDetails = this.fb.group({
    finePaid: [],
    haveYouEverHadTrafficTickets: [],
    when: [],
    where: [],
  }

  ); UsaEntryDeniedDetails = this.fb.group({
    haveYouBeenDeniedToEnterTheUSA: [],
    howManyTimes: [],
    month: [],
    wereYouDeniedMorethanOnce: [],
    whereDidItDenied: [],
    year: [],
  }

  ); VisaDeniedOrRevoked = this.fb.group({
    haveYouEverbeenDeniedOrRevokedAmericanVisa: [],
    howManyTimesGotDenied: [],
    visaClassification: [],
    wasDeniedMoreThanOnce: [],
    whereDidYourVisaWasDenied: [],
  }

  ); VisaIssuanceDetails = this.fb.group({
    consulateThatIssuedVISA: [],
    dateOfExpiration: [],
    dateOfIssuance: [],
    haveYouEverHadUSVisa: [],
  }

  ); ImmigrationHistoryQuestions = this.fb.group({
    deportationProcessDetails: this.DeportationProcessDetails,
    deportedOrExpelledDetails: this.DeportedOrExpelledDetails,
    detainedOrInterrogatedByAnImmigrationOfficer: this.DetainedOrInterrogatedByAnImmigrationOfficer,
    driverLicensesDetails: this.DriverLicensesDetails,
    governmentAssistanceDetails: this.GovernmentAssistanceDetails,
    greenCardInfo: this.GreenCardInfo,
    livingLocationDetails: this.LivingLocationDetails,
    passportIssuedCountryNames: [],
    passportStolenOrLostDetails: this.PassportStolenOrLostDetails,
    trafficTicketsDetails: this.TrafficTicketsDetails,
    usaEntryDeniedDetails: this.UsaEntryDeniedDetails,
    visaDeniedOrRevoked: this.VisaDeniedOrRevoked,
    visaIssuanceDetails: this.VisaIssuanceDetails,
  }

  ); AmericanResidenceOrAmericanCitizenshipDetails = this.fb.group({
    hasBoyfriendOrGirlfriendHold: [false],
    hasBrotherOrSisterHold: [false],
    hasFatherOrMotherHold: [false],
    hasHusbandOrWifeHold: [false],
    hasSonOrDaughterHold: [false],
  }

  ); FatherInformation = this.fb.group({
    cityAndCountryOfBirth: [],
    countryOfCitizenship: [],
    dateOfBirth: [],
    fullName: [],
    otherNamesUsed: [],
  }

  ); MotherInformation = this.fb.group({
    cityAndCountryOfBirth: [],
    countryOfCitizenship: [],
    dateOfBirth: [],
    fullName: [],
    otherNamesUsed: [],
  }

  ); ParentsAndSiblingsInfoDetails = this.fb.group({
    ageOfClient: [],
    americanCitizenshipObtainedThrough: [],
    americanResidenceOrAmericanCitizenshipDetails: this.AmericanResidenceOrAmericanCitizenshipDetails,
    americanResidentObtainedThrough: [],
    fatherInformation: this.FatherInformation,
    motherInformation: this.MotherInformation,
    siblingsObtainedAmericanCitizenshipOrResidentCardThrough: [],
    yearOfCitizenship: [],
    yearOfResidence: [],
  }

  ); ClientPersonalInfo = this.fb.group({
    cityAndCountryOfBirth: [],
    countryOfCitizenship: [],
    dateOfBirth: [],
    fullName: [],
    otherNamesUsed: [],
  }

  ); HeightDetails = this.fb.group({
    eyeColor: [],
    feet: [],
    hairColor: [],
    inches: [],
  }

  ); RaceDetails = this.fb.group({
    americanIndianOrAlaskaNative: [],
    asian: [],
    blackOrAfricanAmerican: [],
    hawaiianNativeOrOtherPacificIsland: [],
    nativeHawaiianOrOtherPacificIslander: [],
    white: [],
  }

  ); PersonalInfoDetails = this.fb.group({
    clientPersonalInfo: this.ClientPersonalInfo,
    heightDetails: this.HeightDetails,
    maritalStatus: [],
    placeOfResidence: [],
    raceDetails: this.RaceDetails,
    weightInPounds: [],
  }

  ); PetitionDetails = this.fb.group({
    doYouHaveAnyRelativesWhoBelongToTheArmyOrAreVeteran: [],
    hasAnyoneFiledPetition: [],
    month: [],
    petitionFileForYouOrParentsORSpouse: [],
    result: [],
    underWhoseName: [],
    whatIsTheRelation: [],
    when: [],
    whoFiledPetition: [],
    year: [],
  }

  ); FullAddress12 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress2 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); PreviousResidentAddress1 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress12,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress2,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress13 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress3 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); PreviousResidentAddress10 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress13,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress3,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress14 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress4 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); PreviousResidentAddress11 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress14,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress4,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress15 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress5 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); PreviousResidentAddress12 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress15,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress5,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress16 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress6 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); PreviousResidentAddress13 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress16,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress6,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress17 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress7 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); PreviousResidentAddress2 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress17,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress7,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress18 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress8 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); PreviousResidentAddress3 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress18,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress8,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress19 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress9 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); PreviousResidentAddress4 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress19,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress9,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress20 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress10 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); PreviousResidentAddress5 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress20,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress10,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress21 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress11 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); PreviousResidentAddress6 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress21,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress11,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress22 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress12 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); PreviousResidentAddress7 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress22,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress12,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress23 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress13 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); PreviousResidentAddress8 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress23,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress13,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress24 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); ResidentAddress14 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); PreviousResidentAddress9 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress24,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress14,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); SpouseOrFiancePersonalInfo = this.fb.group({
    cityAndCountryOfBirth: [],
    countryOfCitizenship: [],
    dateOfBirth: [],
    dateOfMarriage: [],
    fullName: [],
    otherNamesUsed: [],
    placeOfMarriage: [],
    placeOfResidence: [],
  }

  );
  VictimOfCrimeDetails = this.fb.group({
    dateOfOccurred: [],
    haveYouOrParentsOrSpouseEverbeenVicimOfCrime: [],
    summaryOfTheEvent: [],
    wasItReported: [],
  });


  form = this.fb.group({
    additionalInfo: this.AdditionalInfo,
    arrivalAndDepartureDetails: this.ArrivalAndDepartureDetails,
    child1: this.Child1,
    child2: this.Child2,
    child3: this.Child3,
    child4: this.Child4,
    child5: this.Child5,
    child6: this.Child6,
    clientId: [],
    createdBy: [],
    createdOn: [],
    currentLocationResidentAddress: this.CurrentLocationResidentAddress,
    diovercedOrWidowerMaritalStatus: this.DiovercedOrWidowerMaritalStatus,
    employmentHistoryDetails: this.EmploymentHistoryDetails,
    immigrationHistoryQuestions: this.ImmigrationHistoryQuestions,
    classID: [],
    matterNumber: [],
    itFormID: [],
    itFormNo: [],
    language: [],
    parentsAndSiblingsInfoDetails: this.ParentsAndSiblingsInfoDetails,
    personalInfoDetails: this.PersonalInfoDetails,
    petitionDetails: this.PetitionDetails,
    previousResidentAddress1: this.PreviousResidentAddress1,
    previousResidentAddress10: this.PreviousResidentAddress10,
    previousResidentAddress11: this.PreviousResidentAddress11,
    previousResidentAddress12: this.PreviousResidentAddress12,
    previousResidentAddress13: this.PreviousResidentAddress13,
    previousResidentAddress2: this.PreviousResidentAddress2,
    previousResidentAddress3: this.PreviousResidentAddress3,
    previousResidentAddress4: this.PreviousResidentAddress4,
    previousResidentAddress5: this.PreviousResidentAddress5,
    previousResidentAddress6: this.PreviousResidentAddress6,
    previousResidentAddress7: this.PreviousResidentAddress7,
    previousResidentAddress8: this.PreviousResidentAddress8,
    previousResidentAddress9: this.PreviousResidentAddress9,
    spouseOrFiancePersonalInfo: this.SpouseOrFiancePersonalInfo,
    updatedBy: [],
    updatedOn: [],
    victimOfCrimeDetails: this.VictimOfCrimeDetails,
  });

  intakefg = this.fb.group({
    approvedOn: [],
    classId: [],
    clientId: [],
    clientUserId: [],
    createdBy: [this.auth.userID],
    createdOn: [],
    deletionIndicator: [0],
    intakeFormId: [, Validators.required],
    intakeFormNumber: [],
    languageId: [],
    matterNumber: [, Validators.required],
    notesNumber: [],
    receivedOn: [],
    referenceField1: [],
    referenceField10: [],
    referenceField2: [],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    resentOn: [],
    sentOn: [],
    statusId: [],
    updatedBy: [this.auth.userID],
    updatedOn: [],


  });

  sub = new Subscription();



  disabled = false;
  step = 0;

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  panelOpenState = false;


  itformno = "";
  webemailpage: string | undefined;
  isbtntext = true;
  btncancel = false;
  // Consultation Date & admin tab check with raj
  pageflow: string = 'New';
  constructor(private fb: FormBuilder, private auth: AuthService, private cs: CommonService, private route: ActivatedRoute, private location: Location,
    private service: Form007Service,
    private fservice: MatterIntakeService,

    private spin: NgxSpinnerService, public toastr: ToastrService,

    private cas: CommonApiService) {

  }
  ngOnInit(): void {
    //for all itform

    let code = this.route.snapshot.params.code;
    let js = this.cs.decrypt(code);
    this.webemailpage = js.pageflow
    this.pageflow = js.pageflow.replace('newweb', 'New');

    this.onload(js);
  }

  onload(js: any) {


    this.dropdownlist();
    if (js.pageflow == 'validate')
      this.btntext = "Approve";
    else if (this.pageflow == 'Display') {
      this.isbtntext = false;
      this.form.disable();
      this.intakefg.disable();
    }
    else if (js.pageflow == 'update')
      this.btntext = "Submit";
  }
  fill() {
    let code = this.route.snapshot.params.code;
    let data = this.cs.decrypt(code);
    if (data.intakeFormNumber) {
      this.spin.show();
      this.itformno = data.intakeFormNumber;
      this.sub.add(this.fservice.Get(data.intakeFormNumber).subscribe(res => {
        this.spin.hide(); this.intakefg.patchValue(res, { emitEvent: false });
        this.intakefg.controls.createdOn.patchValue(this.cs.dateapi(this.intakefg.controls.createdOn.value));



        this.spin.show();

        this.sub.add(this.service.Get(
          res.classId, res.matterNumber, res.intakeFormId, res.intakeFormNumber, res.languageId, res.clientId
        ).subscribe(res => {
          this.spin.hide();

          this.form.patchValue(res, { emitEvent: false });

        }, err => {
          this.spin.hide();

          this.cs.commonerror(err);

        }));

      }, err => {
        this.spin.hide();

        this.cs.commonerror(err);

      }));

    }
  }


  email = new FormControl('', [Validators.required, Validators.email]);
  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return false;
    // if (control.includes('.')) {
    //   const controls = this.form.get(control);
    //   return controls ? controls.hasError(error) : false && this.submitted;

    // }
    // return this.form.controls[control].hasError(error) && this.submitted;
  }
  public errorHandling_admin = (control: string, error: string = "required") => {
    return false;
    // if (control.includes('.')) {
    //   const controls = this.form.get(control);
    //   return controls ? controls.hasError(error) : false && this.submitted;
    // }
    // return this.intakefg.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    return '';
    // if (this.email.hasError('required')) {
    //   return ' Field should not be blank';
    // }
    // return this.email.hasError('email') ? 'Not a valid email' : this.email.hasError('email') ? ' Field should not be blank' : '';
  }


  submit() {
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.intakefg.removeControl('updatedOn');
    this.intakefg.removeControl('createdOn');
    this.submitted = true;
    if (this.form.invalid) {
         this.toastr.error(
        "Please fill the required fields to continue",
        "Notification",{
          timeOut: 2000,
          progressBar: false,
        }
      );

      this.cs.notifyOther(true);
      return;
    }

    this.cs.notifyOther(false);
    this.spin.show();
    this.sub.add(this.service.Update(this.form.getRawValue()).subscribe(res => {
      this.spin.hide();
      this.spin.show();
      if (this.pageflow == 'validate') {

        this.sub.add(this.fservice.Update(this.intakefg.controls.intakeFormNumber.value, this.intakefg.getRawValue()).subscribe(res => {
          this.spin.hide();


          this.toastr.success(" " + this.form.controls.itFormNo.value + " validated successfully","Notification",{
            timeOut: 2000,
            progressBar: false,
          });

          this.location.back();

        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();

        }));

        // this.intakefg.controls.statusId.patchValue(10);
      }
      else {
        this.toastr.success(" " + this.form.controls.itFormNo.value + " updated successfully","Notification",{
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();

        this.location.back();
      }
      // this.location.back();


      //  }
      // else
      // this.sub.add(this.fservice.Update(this.intakefg.controls.intakeFormNumber.value, this.intakefg.getRawValue()).subscribe(res => {
      //   this.spin.hide();

      //   this.toastr.success(" " + res.intakeFormNumber + " updated successfully","Notification",{
        //   timeOut: 2000,
        //   progressBar: false,
        // });
      //   this.location.back();



      // }, err => {
      //   this.cs.commonerror(err);
      //   this.spin.hide();

      // }));


    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    }));
  }


  useridList: any[] = [];
  statusIdList: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      //   this.cas.dropdownlist.setup.inquiryModeId.url,
      // this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.setup.userId.url,
      this.cas.dropdownlist.setup.statusId.url]).subscribe((results) => {
        // this.inquiryModeIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.inquiryModeId.key);
        // this.classIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.classId.key);
        this.useridList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.userId.key);
        this.statusIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [5, 6, 10].includes(s.key));
        this.spin.hide();

        this.fill();
      }, (err) => {
        this.spin.hide();

        this.toastr.error(err, "");
      });
    // this.spin.hide();

  }

  back() {
    this.location.back();

  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
}

