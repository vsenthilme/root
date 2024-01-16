import { HttpClient } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { MatterIntakeService } from '../matter-intake.service';

import { Location } from "@angular/common";
import { Form010Service } from './form010.service';
@Component({
  selector: 'app-form010-english',
  templateUrl: './form010-english.component.html',
  styleUrls: ['./form010-english.component.scss']
})
export class Form010EnglishComponent implements OnInit, OnDestroy {
  screenid: 1124 | undefined;
  btntext = 'Submit';

  Charges1 = this.fb.group({
    dateArrestedCitiedDetainedOrCharged: [],
    outcomeOrDispositionOfTheArrestCitationDetentionOrCharge: [],
    whereWereYouArrestedCitiedDetainedOrCharged: [],
    whyWhereYouArrestedCitiedDetainedOrCharged: [],
  }

  ); Charges2 = this.fb.group({
    dateArrestedCitiedDetainedOrCharged: [],
    outcomeOrDispositionOfTheArrestCitationDetentionOrCharge: [],
    whereWereYouArrestedCitiedDetainedOrCharged: [],
    whyWhereYouArrestedCitiedDetainedOrCharged: [],
  }

  ); Charges3 = this.fb.group({
    dateArrestedCitiedDetainedOrCharged: [],
    outcomeOrDispositionOfTheArrestCitationDetentionOrCharge: [],
    whereWereYouArrestedCitiedDetainedOrCharged: [],
    whyWhereYouArrestedCitiedDetainedOrCharged: [],
  }

  ); Charges4 = this.fb.group({
    dateArrestedCitiedDetainedOrCharged: [],
    outcomeOrDispositionOfTheArrestCitationDetentionOrCharge: [],
    whereWereYouArrestedCitiedDetainedOrCharged: [],
    whyWhereYouArrestedCitiedDetainedOrCharged: [],
  }

  ); Charges5 = this.fb.group({
    dateArrestedCitiedDetainedOrCharged: [],
    outcomeOrDispositionOfTheArrestCitationDetentionOrCharge: [],
    whereWereYouArrestedCitiedDetainedOrCharged: [],
    whyWhereYouArrestedCitiedDetainedOrCharged: [],
  }

  ); Charges6 = this.fb.group({
    dateArrestedCitiedDetainedOrCharged: [],
    outcomeOrDispositionOfTheArrestCitationDetentionOrCharge: [],
    whereWereYouArrestedCitiedDetainedOrCharged: [],
    whyWhereYouArrestedCitiedDetainedOrCharged: [],
  }

  ); MemberGroupDetail1 = this.fb.group({
    membershipFrom: [],
    membershipTo: [],
    nameOfGroup: [],
    purposeOfGroup: [],
  }

  ); MemberGroupDetail2 = this.fb.group({
    membershipFrom: [],
    membershipTo: [],
    nameOfGroup: [],
    purposeOfGroup: [],
  }

  ); MemberGroupDetail3 = this.fb.group({
    membershipFrom: [],
    membershipTo: [],
    nameOfGroup: [],
    purposeOfGroup: [],
  }

  ); MemberGroupDetail4 = this.fb.group({
    membershipFrom: [],
    membershipTo: [],
    nameOfGroup: [],
    purposeOfGroup: [],
  }

  ); MemberGroupDetail5 = this.fb.group({
    membershipFrom: [],
    membershipTo: [],
    nameOfGroup: [],
    purposeOfGroup: [],
  }

  ); MemberGroupDetail6 = this.fb.group({
    membershipFrom: [],
    membershipTo: [],
    nameOfGroup: [],
    purposeOfGroup: [],
  }

  ); AdditionalQuestions = this.fb.group({
    areRemovalExclusionRescissionOrDeportationProceedingsCurrentlyPendingAgainstYou: [],
    areYouAMaleWhoLivedInTheUSAtAnyTimeBetweenYour18thAnd26thBirthdays: [],
    areYouCurrentlyAMemberOfTheUSArmedForces: [],
    areYouCurrentlyStationedOverseas: [],
    areYouScheduledToDeployOverseasWithinTheNext3Months: [],
    charges1: this.Charges1,
    charges2: this.Charges2,
    charges3: this.Charges3,
    charges4: this.Charges4,
    charges5: this.Charges5,
    charges6: this.Charges6,
    didYouConsiderYourselfANonUSResident: [],
    didYouEverHaveAHereditaryTitleOrAnOrderOfNobilityInAnyForeignCountry: [],
    didYouEverReceiveAnyTypeOfMilitaryParamilitaryOrWeaponsTraining: [],
    didYouEverRecruitEnlistConscriptOrUseAnyPersonUnderAge15ToServeOrHelpAnArmedForcesGroup: [],
    didYouEverSellGiveProvideOrHelpWithWeaponsToAnyPerson: [],
    didYouEverUseAnyPersonUnderAge15ToDoAnythingThatHelpedOrSupportedPeopleInCombat: [],
    didYouEverUsedOrHelpedTheGroupUsingAWeaponAgainstAnyPerson: [],
    didYouTellAnyOtherPersonThatYouWouldUseAWeaponAgainstThatPerson: [],
    didYouWorkOrAssociateWithAnyGermanNaziOrSSMilitaryUnitBetweenMarch231933AndMay81945: [],
    didYouWorkOrAssociateWithAnyGovernmentInAnyAreaBetweenMarch231933AndMay81945: [],
    didYouWorkOrAssociateWithNaziGovernmentOfGermanyBetweenMarch231933AndMay81945: [],
    doYouKnowThatThisPersonIsGoingToSellOrGiveWeaponsToAnotherPerson: [],
    doYouKnowThatThisPersonIsGoingToUseWeaponsAgainstAnotherPerson: [],
    doYouOweAnyFederalStateOrLocalTaxesThatAreOverdue: [],
    haveYouCalledYourselfANonUSResidentSinceYouBecameAPermanentResident: [],
    haveYouCompletedTheProbationOrParole: [],
    haveYouEverAdvocatedTheOverthrowOfAnyGovernmentByForceOrViolence: [],
    haveYouEverBeenAHabitualDrunkard: [],
    haveYouEverBeenAMemberOfATerroristOrganization: [],
    haveYouEverBeenAMemberOfAnyOtherTotalitarianParty: [],
    haveYouEverBeenAMemberOfOrInAnyWayAssociatedWithAnyOrganizationInTheUSOrOtherCountries: [],
    haveYouEverBeenAMemberOfTheCommunistParty: [],
    haveYouEverBeenAProstituteOrProcuredAnyoneForProstitution: [],
    haveYouEverBeenArrestedCitedOrDetainedByAnyLawEnforcementOfficer: [],
    haveYouEverBeenChargedWithCommittedAttemptingOrAssistingInCommittingAcrimeOrOffense: [],
    haveYouEverBeenConvictedOfAcrimeOrOffense: [],
    haveYouEverBeenCourtMartialedAdministrativelySeparatedOrDisciplinedInTheUSArmedForces: [],
    haveYouEverBeenDeclaredLegallyIncompetentOrBeenConfinedToAMentalInstitution: [],
    haveYouEverBeenDischargedFromTrainingOrServiceInTheUSArmedForcesBecauseYouWereAnAlien: [],
    haveYouEverBeenFailedToSupportYourDependentsOrToPayAlimony: [],
    haveYouEverBeenGambledIllegallyOrReceivedIncomeFromIllegalGambling: [],
    haveYouEverBeenHelpedAnyoneEnterOrTryToEnterTheUSIllegally: [],
    haveYouEverBeenInJailOrPrison: [],
    haveYouEverBeenMadeAnyMisrepresentationToObtainAnyPublicBenefitInTheUS: [],
    haveYouEverBeenMarriedSomeoneInOrderToObtainAnImmigrationBenefit: [],
    haveYouEverBeenMarriedToMorethanOnePersonAttheSameTime: [],
    haveYouEverBeenOrderedRemovedExcludedOrDeportedFromTheUS: [],
    haveYouEverBeenPlacedInAnAlternativeSentencingOrARehabilitativeProgram: [],
    haveYouEverBeenPlacedInRemovalExclusionRescissionOrDeportationProceedings: [],
    haveYouEverBeenRemovedExcludedOrDeportedFromTheUS: [],
    haveYouEverBeenSoldOrSmuggledControlledSubstancesIllegalDrugsOrNarcotics: [],
    haveYouEverClaimedToBeAUSCitizen: [],
    haveYouEverCommittedAssistedInCommittingOrAttemptedToCommitACrimeOrOffenseForWhichYouWereNotArrested: [],
    haveYouEverDesertedFromTheUSArmedForces: [],
    haveYouEverGivenFalseOrMisleadedInformationAboutUSGovernmentOfficials: [],
    haveYouEverLeftTheUSToAvoidBeingDraftedIntoTheUSArmedForces: [],
    haveYouEverLiedToAnyUSGovernmentOfficialToGainEntryOrAdmissionIntoTheUS: [],
    haveYouEverNotFiledAFederalStateOrLocalTaxReturnSinceYouBecameAPermanentResident: [],
    haveYouEverReceivedASuspendedSentenceBeenPlacedOnProbationOrBeenParoled: [],
    haveYouEverRegisteredToVoteInAnyFederalStateOrLocalElectionInTheUS: [],
    haveYouEverServedInTheUSArmedForces: [],
    haveYouEverVotedInAnyFederalStateOrLocalElectionInTheUS: [],
    haveYouEverappliedForAnyKindOfExemptionFromMilitaryServiceInTheUSArmedForces: [],
    haveYouEverpersecutedAnyPersonBecauseOfRaceReligion: [],
    howLongWereYouInJailOrPrison: [],
    memberGroupDetail1: this.MemberGroupDetail1,
    memberGroupDetail2: this.MemberGroupDetail2,
    memberGroupDetail3: this.MemberGroupDetail3,
    memberGroupDetail4: this.MemberGroupDetail4,
    memberGroupDetail5: this.MemberGroupDetail5,
    memberGroupDetail6: this.MemberGroupDetail6,
    reasonForNotRegisteringSelectiveServiceSystem: [],
    selectiveServiceNumber: [],
    selectiveServiceSystemRegisteredDate: [],
    wereYouEverInvolvedInBadlyHurtingOrTryingToHurtApersonOnPurpose: [],
    wereYouEverInvolvedInForcingOrTryingToForceSomeoneToHavAnyKindOfSexualContactOrRelations: [],
    wereYouEverInvolvedInGenocide: [],
    wereYouEverInvolvedInKillingOrTryingToKillSomeone: [],
    wereYouEverInvolvedInNotLettingSomeonePracticeHisOrHerReligion: [],
    wereYouEverInvolvedInTorture: [],
    wereYouEverMemberOrServeInGuerillaGroup: [],
    wereYouEverMemberOrServeInInsurgentOrganization: [],
    wereYouEverMemberOrServeInMilitaryUnit: [],
    wereYouEverMemberOrServeInMilitia: [],
    wereYouEverMemberOrServeInParamilitaryUnit: [],
    wereYouEverMemberOrServeInPoliceUnit: [],
    wereYouEverMemberOrServeInRebelGroup: [],
    wereYouEverMemberOrServeInSelfDefenseUnit: [],
    wereYouEverMemberOrServeInVigilanteUnit: [],
    wereYouEverPartOrHelpedOfAnyGroupOrOrganizationUsedAWeaponAgainstAnyPerson: [],
    wereYouEverWorkerVolunteerSoldierServeInAnyOtherPlaceWherePeopleWereForcedToStay: [],
    wereYouEverWorkerVolunteerSoldierServeInDetentionFacility: [],
    wereYouEverWorkerVolunteerSoldierServeInLaborCamp: [],
    wereYouEverWorkerVolunteerSoldierServeInPrisonCamp: [],
    wereYouEverWorkerVolunteerSoldierServeInPrisonOrJail: [],
  }

  ); Child1 = this.fb.group({
    countryOfBirth: [],
    dateOfBirth: [],
    fullName: [],
    location: [],
    relationshipToYou: [],
    uscis: [],
  }

  ); Child2 = this.fb.group({
    countryOfBirth: [],
    dateOfBirth: [],
    fullName: [],
    location: [],
    relationshipToYou: [],
    uscis: [],
  }

  ); Child3 = this.fb.group({
    countryOfBirth: [],
    dateOfBirth: [],
    fullName: [],
    location: [],
    relationshipToYou: [],
    uscis: [],
  }

  ); Child4 = this.fb.group({
    countryOfBirth: [],
    dateOfBirth: [],
    fullName: [],
    location: [],
    relationshipToYou: [],
    uscis: [],
  }

  ); Child5 = this.fb.group({
    countryOfBirth: [],
    dateOfBirth: [],
    fullName: [],
    location: [],
    relationshipToYou: [],
    uscis: [],
  }

  ); Child6 = this.fb.group({
    countryOfBirth: [],
    dateOfBirth: [],
    fullName: [],
    location: [],
    relationshipToYou: [],
    uscis: [],
  }

  ); Child7 = this.fb.group({
    countryOfBirth: [],
    dateOfBirth: [],
    fullName: [],
    location: [],
    relationshipToYou: [],
    uscis: [],
  }

  ); Child8 = this.fb.group({
    countryOfBirth: [],
    dateOfBirth: [],
    fullName: [],
    location: [],
    relationshipToYou: [],
    uscis: [],
  }

  ); Children = this.fb.group({
    child1: this.Child1,
    child2: this.Child2,
    child3: this.Child3,
    child4: this.Child4,
    child5: this.Child5,
    child6: this.Child6,
    child7: this.Child7,
    child8: this.Child8,
    totalNoOfChildren: [],
  }

  ); ContactDetails = this.fb.group({
    aliasOrNickname: [],
    currentLegalFirstName: [],
    currentLegalLastName: [],
    currentLegalMiddleName: [],
    daytimePhone: [],
    emailAddress: [],
    eveningPhone: [],
    exactFirstName: [],
    exactLastName: [],
    exactMiddleName: [],
    haveYouUsedAnyotherNamesFromAPreviousMarriage: [],
    maidenName: [],
    maidenNameNotApplicable: [],
    middleNameNotApplicable: [],
    mobilePhone: [],
    provideAllOtherNamesUsed: [],
    suffix: [],
    workPhone: [],
    yourNameListedDifferentlyOnYourPermanentResidentCard: [],
  }

  ); MailingAddress = this.fb.group({
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

  ); CurrentResidenceAndMailingAddress = this.fb.group({
    dateFrom: [],
    mailingAddress: this.MailingAddress,
    phoneNumber: [],
    residentAddress: this.ResidentAddress,
    thisTheSameAddressAsYourCurrentMailingAddress: [],
    untilPresent: [],
  }

  ); Eligibility = this.fb.group({
    explain: [],
    iamApplyingOnTheBasisOfQualifyingMilitaryService: [],
    iamPermanentResident: [],
    ihaveBeenAPermanentResident3Years: [],
    ihaveBeenAPermanentResidentFor5Years: [],
    other: [],
  }

  ); ForeignAddress = this.fb.group({
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

  ); EmploymentsAndSchools = this.fb.group({
    datesFrom: [],
    datesTo: [],
    employerOrSchoolName: [],
    foreignAddress: this.ForeignAddress,
    residentAddress: this.ResidentAddress2,
    titleOrOccupation: [],
  }

  ); LegalNameOfCurrentSpouse = this.fb.group({
    familyOrLastName: [],
    givenOrFirstName: [],
    middleName: [],
  }

  ); MyCurrentMaritalStatusIs = this.fb.group({
    divorced: [],
    marriageAnnulled: [],
    married: [],
    separated: [],
    singleOrNeverMarrried: [],
    widowed: [],
  }

  ); OtherNamesUsedByCurrentSpouse = this.fb.group({
    familyOrLastName: [],
    givenOrFirstName: [],
    middleName: [],
  }

  ); PreviousLegalNameOfCurrentSpouse = this.fb.group({
    familyOrLastName: [],
    givenOrFirstName: [],
    middleName: [],
  }

  ); HowMarriageEnded = this.fb.group({
    annulled: [],
    divorced: [],
    other: [],
    spouseDeceased: [],
  }

  ); SpousesUSImmigrationStatus = this.fb.group({
    citizenOfUS: [],
    other: [],
    permanentResident: [],
  }

  ); PriorSpouseMarriageDetails = this.fb.group({
    countryOfBirth: [],
    countryOfCitizenshipOrNationality: [],
    dateOfBirth: [],
    dateOfEnded: [],
    dateOfMarriage: [],
    familyOrLastName: [],
    givenOrFirstName: [],
    howMarriageEnded: this.HowMarriageEnded,
    middleName: [],
    spousesUSImmigrationStatus: this.SpousesUSImmigrationStatus,
  }

  ); IfNotAppicableIsMarked = this.fb.group({
    spouseCountryOfCitizenship: [],
    spouseImmigrationStatusOfOther: [],
    spouseImmigrationStatusOfPermanentResident: [],
    spouseUSCISNumber: [],
  }

  ); IfOtherIsMarked = this.fb.group({
    placeWhereSpouseBecameAUSCitizen: [],
    whenDidYoursSpouseBecameAUSCitizen: [],
  }

  ); IfSpouseNotAUSCitizen = this.fb.group({
    anumber: [],
    currentImmigrationStatus: [],
    spouseCountryOfCitizenshipOrNaionality: [],
  }

  ); SpouseCompleteAddress = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); WhenDidYourSpouseBecomeUSCitizen = this.fb.group({
    atBirth: [],
    notApplicable: [],
    other: [],
  }

  ); SpouseInformation = this.fb.group({
    alienRegistrationNumber: [],
    dateHeOrSheWasNaturalized: [],
    dateOfArrival: [],
    dateOfBirth: [],
    dateOfMarriage: [],
    doesYourSpouseHaveTheSameHomeAddressAsYou: [],
    earingsPerWeek: [],
    employersAddress: [],
    employersName: [],
    howManyTimesHasYourCurrentSpouseBeenMarried: [],
    ifNotAppicableIsMarked: this.IfNotAppicableIsMarked,
    ifOtherIsMarked: this.IfOtherIsMarked,
    ifSpouseNotAUSCitizen: this.IfSpouseNotAUSCitizen,
    immigrationStatus: [],
    name: [],
    namebeforeMarriage: [],
    placeOfArrivalIntoTheUS: [],
    placeOfBirth: [],
    placeOfMarriage: [],
    spouseAddress: [],
    spouseCompleteAddress: this.SpouseCompleteAddress,
    spouseDateOfBirth: [],
    spousePresentEmployer: [],
    spouseUSSocialSecurityNumber: [],
    whenDidYourSpouseBecomeUSCitizen: this.WhenDidYourSpouseBecomeUSCitizen,
    yourCurrentSpouseAUSCitizen: [],
  }

  ); HowMarriageEnded2 = this.fb.group({
    annulled: [],
    divorced: [],
    other: [],
    spouseDeceased: [],
  }

  ); SpousesUSImmigrationStatus2 = this.fb.group({
    citizenOfUS: [],
    other: [],
    permanentResident: [],
  }

  ); SpousePreviousMarriageDetailsIfMarriedBefore = this.fb.group({
    countryOfBirth: [],
    countryOfCitizenshipOrNationality: [],
    dateOfBirth: [],
    dateOfEnded: [],
    dateOfMarriage: [],
    familyOrLastName: [],
    givenOrFirstName: [],
    howMarriageEnded: this.HowMarriageEnded2,
    middleName: [],
    spousesUSImmigrationStatus: this.SpousesUSImmigrationStatus2,
  }

  ); MaritalHistory = this.fb.group({
    dateOfBirthOfCurrentSpouse: [],
    howManyTimesHaveYouBeenMarried: [],
    legalNameOfCurrentSpouse: this.LegalNameOfCurrentSpouse,
    myCurrentMaritalStatusIs: this.MyCurrentMaritalStatusIs,
    otherNamesUsedByCurrentSpouse: this.OtherNamesUsedByCurrentSpouse,
    previousLegalNameOfCurrentSpouse: this.PreviousLegalNameOfCurrentSpouse,
    priorSpouseMarriageDetails: this.PriorSpouseMarriageDetails,
    spouseInformation: this.SpouseInformation,
    spousePreviousMarriageDetailsIfMarriedBefore: this.SpousePreviousMarriageDetailsIfMarriedBefore,
    yourSpouseACurrentMemberOfTheUSArmedForces: [],
  }

  ); NameChange = this.fb.group({
    newFirstName: [],
    newLastName: [],
    newMiddleName: [],
    wouldYouLikeToLegallyChangeYourName: [],
  }

  ); Parents = this.fb.group({
    fathersCountryOfBirth: [],
    fathersDateOfBirth: [],
    fathersFirstName: [],
    fathersLastName: [],
    fathersMiddleName: [],
    mothersCountryOfBirth: [],
    mothersDateOfBirth: [],
    mothersFirstName: [],
    mothersLastName: [],
    mothersMiddleName: [],
    wereYourParentsMarriedBeforeYour18thBirthday: [],
    yourFatherUSCitizen: [],
    yourMotherUSCitizen: [],
  }

  ); Race = this.fb.group({
    americanIndianOrAlaskaNative: [],
    asian: [],
    blackOrAfricanAmerican: [],
    hawaiianNativeOrOtherPacificIsland: [],
    nativeHawaiianOrOtherPacificIslander: [],
    white: [],
  }

  ); SelectIfAnyApplicable = this.fb.group({
    iamBlindOrSightImpaired: [],
    iamDeafOrHearingImpaired: [],
    irequireAnotherTypeOfAaccommodation: [],
    iuseAWheelchairOrOtherDevice: [],
  }

  ); PersonalDetails = this.fb.group({
    alienNumber: [],
    alienNumberNotApplicable: [],
    areYou50YearsOfAgeAndLvedInUSAsPermanentResidentForAtleast20Years: [],
    areYou55YearsOfAgeAndLvedInUSAsPermanentResidentForAtleast15Years: [],
    areYou60YearsOfAgeAndLvedInUSAsPermanentResidentForAtleast20Years: [],
    areYouHispanicOrLatino: [],
    countryOfBirth: [],
    countryOfCitizenship: [],
    countryWhereMostRecentCitizenshipOrPrmanentResidencyOrLandedImmigrantStatusGranted: [],
    dateOfBirth: [],
    dateYouBecameUSPermanentResident: [],
    doYouAaveAPhysicalOrDevelopmentalImpairment: [],
    eyeColor: [],
    femaleGender: [],
    hairColor: [],
    heightInFeet: [],
    heightInInches: [],
    ihaveNotBeenIssuedUSASocialSecurityNumber: [],
    maleGender: [],
    race: this.Race,
    selectIfAnyApplicable: this.SelectIfAnyApplicable,
    socialSecurityNumber: [],
    weightInPounds: [],
    wouldYouLikeToLegallyChangeYourName: [],
  }

  ); FullAddress = this.fb.group({
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

  ); ResidentAddress1 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress3,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress2 = this.fb.group({
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

  ); ResidentAddress22 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress2,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress4,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress3 = this.fb.group({
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

  ); ResidentAddress32 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress3,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress5,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress4 = this.fb.group({
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

  ); ResidentAddress42 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress4,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress6,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); FullAddress5 = this.fb.group({
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

  ); ResidentAddress52 = this.fb.group({
    dateFrom: [],
    dateTo: [],
    fullAddress: this.FullAddress5,
    livingFrom: [],
    livingTill: [],
    residentAddress: this.ResidentAddress7,
    wasTheResidenceUnderYourName: [],
    whoDidYouLiveWith: [],
  }

  ); ResidenceHistory = this.fb.group({
    residenceInformationForPast5YearsWithMostRecentAddressFirst: [],
    residentAddress1: this.ResidentAddress1,
    residentAddress2: this.ResidentAddress22,
    residentAddress3: this.ResidentAddress32,
    residentAddress4: this.ResidentAddress42,
    residentAddress5: this.ResidentAddress52,
  }

  ); TextOfTheOath = this.fb.group({
    areYouWillingToBearArmsOnBehalfOfTheUS: [],
    areYouWillingToGiveUpAnyInheritedTitleOrorderOfNobilityThatYouHaveInAForeignCountry: [],
    areYouWillingToTakeTheFullOathOfAllegianceToTheUS: [],
    areYouWillingToperformNoncombatantServicesInTheUSArmedForces: [],
    areYouWillingToperformWorkOfNationalImportanceUnderCivilianDirection: [],
    doYouUnderstandTheFullOathOfAllegianceToTheUS: [],
    doYousupporttheConstitutionandformofgovernmentOfTheUS: [],
  }

  ); TripSpentDetails1 = this.fb.group({
    countriesToWhichYouTraveled: [],
    dateYouLeftTheUS: [],
    dateYouReturnedToTheUS: [],
    didTripLastMoreThan6Months: [],
    totalDaysOutsideTheUS: [],
  }

  ); TripSpentDetails2 = this.fb.group({
    countriesToWhichYouTraveled: [],
    dateYouLeftTheUS: [],
    dateYouReturnedToTheUS: [],
    didTripLastMoreThan6Months: [],
    totalDaysOutsideTheUS: [],
  }

  ); TripSpentDetails3 = this.fb.group({
    countriesToWhichYouTraveled: [],
    dateYouLeftTheUS: [],
    dateYouReturnedToTheUS: [],
    didTripLastMoreThan6Months: [],
    totalDaysOutsideTheUS: [],
  }

  ); TripSpentDetails4 = this.fb.group({
    countriesToWhichYouTraveled: [],
    dateYouLeftTheUS: [],
    dateYouReturnedToTheUS: [],
    didTripLastMoreThan6Months: [],
    totalDaysOutsideTheUS: [],
  }

  ); TripSpentDetails5 = this.fb.group({
    countriesToWhichYouTraveled: [],
    dateYouLeftTheUS: [],
    dateYouReturnedToTheUS: [],
    didTripLastMoreThan6Months: [],
    totalDaysOutsideTheUS: [],
  }

  ); TripSpentDetails6 = this.fb.group({
    countriesToWhichYouTraveled: [],
    dateYouLeftTheUS: [],
    dateYouReturnedToTheUS: [],
    didTripLastMoreThan6Months: [],
    totalDaysOutsideTheUS: [],
  }

  ); TripSpentDetails7 = this.fb.group({
    countriesToWhichYouTraveled: [],
    dateYouLeftTheUS: [],
    dateYouReturnedToTheUS: [],
    didTripLastMoreThan6Months: [],
    totalDaysOutsideTheUS: [],
  });
  TimeSpentOutsideTheUS = this.fb.group({
    daysSpentOutsideUSForPast5Years: [],
    listAllTheTripsTakenOutsideUSForPast5YearsWithMostRecentFirst: [],
    noOfTripsTakenOutsideUSForPast5Years: [],
    tripSpentDetails1: this.TripSpentDetails1,
    tripSpentDetails2: this.TripSpentDetails2,
    tripSpentDetails3: this.TripSpentDetails3,
    tripSpentDetails4: this.TripSpentDetails4,
    tripSpentDetails5: this.TripSpentDetails5,
    tripSpentDetails6: this.TripSpentDetails6,
    tripSpentDetails7: this.TripSpentDetails7,
  });


  form = this.fb.group({
    additionalQuestions: this.AdditionalQuestions,
    children: this.Children,
    classID: [],
    clientId: [],
    contactDetails: this.ContactDetails,
    createdBy: [],
    createdOn: [],
    currentResidenceAndMailingAddress: this.CurrentResidenceAndMailingAddress,
    eligibility: this.Eligibility,
    employmentsAndSchools: this.EmploymentsAndSchools,
    itFormID: [],
    itFormNo: [],
    language: [],
    maritalHistory: this.MaritalHistory,
    matterNumber: [],
    nameChange: this.NameChange,
    parents: this.Parents,
    personalDetails: this.PersonalDetails,
    residenceHistory: this.ResidenceHistory,
    textOfTheOath: this.TextOfTheOath,
    timeSpentOutsideTheUS: this.TimeSpentOutsideTheUS,
    updatedBy: [],
    updatedOn: [],
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


  })

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
    private service: Form010Service,
    private fservice: MatterIntakeService,
    private router: Router,
    private spin: NgxSpinnerService, public toastr: ToastrService,
    private http: HttpClient,
    private cas: CommonApiService) { }

  ngOnInit(): void {
    //for all itform

    let code = this.route.snapshot.params.code;
    let js = this.cs.decrypt(code);
    this.webemailpage = js.pageflow
    this.pageflow = js.pageflow.replace('newweb', 'New');

    this.onload(js);
  }

  onload(js: any) {


    this.fill(js);
    this.dropdownlist();
    if (js.pageflow == 'validate')
      this.btntext = "Save";
    else if (this.pageflow == 'Display') {
      this.isbtntext = false;
      this.form.disable();
      this.intakefg.disable();
    }
    else if (js.pageflow == 'update')
      this.btntext = "Submit";
  }
  fill(data: any) {

    if (data.intakeFormNumber) {
      this.spin.show();
      this.itformno = data.intakeFormNumber;
      this.sub.add(this.fservice.Get(data.intakeFormNumber).subscribe(res => {
        this.intakefg.patchValue(res);
        this.intakefg.controls.createdOn.patchValue(this.cs.dateapi(this.intakefg.controls.createdOn.value));

        this.spin.hide();

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

      }, (err) => {
        this.spin.hide();

        this.toastr.error(err, "");
      });
    this.spin.hide();

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

