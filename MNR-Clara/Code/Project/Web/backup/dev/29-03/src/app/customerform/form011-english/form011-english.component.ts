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
import { Form011Service } from './form011.service';
import { Location } from "@angular/common";
@Component({
  selector: 'app-form011-english',
  templateUrl: './form011-english.component.html',
  styleUrls: ['./form011-english.component.scss']
})
export class Form011EnglishComponent implements OnInit, OnDestroy {
  screenid: 1125 | undefined;
  btntext = 'Submit';

  ChildrenInfo = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); ChildrenInformation = this.fb.group({
    childrenInfo: this.ChildrenInfo,
    estimatedAverageWeeklyEarnings: [],
    estimatedTotalAssets: [],
  }

  ); CurrentEmployment = this.fb.group({
    address: [],
    companyAndWagesPerHour: [],
    employmentDateFrom: [],
    employmentDateTo: [],
    occupation: [],
    telephone: [],
  }

  ); FullAddress = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); EmploymentDetails1 = this.fb.group({
    doYouHaveAnyEvidenceOfEmployment: [],
    employmentFrom: [],
    employmentTill: [],
    fullAddress: this.FullAddress,
    nameAndSurnameOfYourSupervisor: [],
    nameOfTheCompany: [],
    telephoneNumber: [],
  }

  ); FullAddress2 = this.fb.group({
    city: [],
    state: [],
    streetAddress: [],
    zipCode: [],
  }

  ); EmploymentDetails10 = this.fb.group({
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

  ); EmploymentDetails2 = this.fb.group({
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

  ); EmploymentDetails3 = this.fb.group({
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

  ); EmploymentDetails4 = this.fb.group({
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

  ); EmploymentDetails5 = this.fb.group({
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

  ); EmploymentDetails6 = this.fb.group({
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

  ); EmploymentDetails7 = this.fb.group({
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

  ); EmploymentDetails8 = this.fb.group({
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

  ); EmploymentDetails9 = this.fb.group({
    doYouHaveAnyEvidenceOfEmployment: [],
    employmentFrom: [],
    employmentTill: [],
    fullAddress: this.FullAddress10,
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

  ); EmploymentHistory = this.fb.group({
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

  ); Deceased = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); AuntOrUncle1 = this.fb.group({
    deceased: this.Deceased,
    personDetails: this.PersonDetails,
  }

  ); Deceased2 = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails2 = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); AuntOrUncle2 = this.fb.group({
    deceased: this.Deceased2,
    personDetails: this.PersonDetails2,
  }

  ); Deceased3 = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails3 = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); AuntOrUncle3 = this.fb.group({
    deceased: this.Deceased3,
    personDetails: this.PersonDetails3,
  }

  ); Deceased4 = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails4 = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); AuntOrUncle4 = this.fb.group({
    deceased: this.Deceased4,
    personDetails: this.PersonDetails4,
  }

  ); Deceased5 = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails5 = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); BrotherOrSister1 = this.fb.group({
    deceased: this.Deceased5,
    personDetails: this.PersonDetails5,
  }

  ); Deceased6 = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails6 = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); BrotherOrSister2 = this.fb.group({
    deceased: this.Deceased6,
    personDetails: this.PersonDetails6,
  }

  ); Deceased7 = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails7 = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); BrotherOrSister3 = this.fb.group({
    deceased: this.Deceased7,
    personDetails: this.PersonDetails7,
  }

  ); Deceased8 = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails8 = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); BrotherOrSister4 = this.fb.group({
    deceased: this.Deceased8,
    personDetails: this.PersonDetails8,
  }

  ); Deceased9 = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails9 = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); Father = this.fb.group({
    deceased: this.Deceased9,
    personDetails: this.PersonDetails9,
  }

  ); Deceased10 = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails10 = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); GrandParents1 = this.fb.group({
    deceased: this.Deceased10,
    personDetails: this.PersonDetails10,
  }

  ); Deceased11 = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails11 = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); GrandParents2 = this.fb.group({
    deceased: this.Deceased11,
    personDetails: this.PersonDetails11,
  }

  ); Deceased12 = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails12 = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); GrandParents3 = this.fb.group({
    deceased: this.Deceased12,
    personDetails: this.PersonDetails12,
  }

  ); Deceased13 = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails13 = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); GrandParents4 = this.fb.group({
    deceased: this.Deceased13,
    personDetails: this.PersonDetails13,
  }

  ); Deceased14 = this.fb.group({
    deceasedDate: [],
    placeOfDeath: [],
  }

  ); PersonDetails14 = this.fb.group({
    alienRegistrationNumber: [],
    dateOfBirth: [],
    immigrationStatus: [],
    name: [],
    placeOfBirth: [],
    placeOfResidence: [],
  }

  ); Mother = this.fb.group({
    deceased: this.Deceased14,
    personDetails: this.PersonDetails14,
  }

  ); FamilyInformation = this.fb.group({
    auntOrUncle1: this.AuntOrUncle1,
    auntOrUncle2: this.AuntOrUncle2,
    auntOrUncle3: this.AuntOrUncle3,
    auntOrUncle4: this.AuntOrUncle4,
    brotherOrSister1: this.BrotherOrSister1,
    brotherOrSister2: this.BrotherOrSister2,
    brotherOrSister3: this.BrotherOrSister3,
    brotherOrSister4: this.BrotherOrSister4,
    father: this.Father,
    grandParents1: this.GrandParents1,
    grandParents2: this.GrandParents2,
    grandParents3: this.GrandParents3,
    grandParents4: this.GrandParents4,
    mother: this.Mother,
  }

  ); EntryInfo = this.fb.group({
    monthOfFirstVisit: [],
    monthOfLastVisit: [],
    placeOfFirstVisit: [],
    placeOfLastVisit: [],
    yearOfFirstVisit: [],
    yearOfLastVisit: [],
  }

  ); Name = this.fb.group({
    lastName: [],
    legalNameAsItAppearsInPassportOrBirthCertificate: [],
    middleName: [],
    name: [],
    otherNamesUsed: [],
  }

  ); Passport = this.fb.group({
    cityOfIssuance: [],
    countryOfIssuance: [],
    dateOfIssuance: [],
    expirationDate: [],
    passportNumber: [],
  }

  ); PhoneNumber = this.fb.group({
    cellPhone: [],
    telephoneNumber: [],
    work: [],
  }

  ); PlaceOfBirth = this.fb.group({
    city: [],
    country: [],
    state: [],
  }

  ); VisaDenied = this.fb.group({
    haveYouEverbeenDeniedVisa: [],
    typeOfVisa: [],
    when: [],
    where: [],
  }

  ); VisaImmigrant = this.fb.group({
    hasAnyoneEverFiledImmigrantVisa: [],
    reasonForVisaDenial: [],
    typeOfVisa: [],
    wasVisaGranted: [],
    when: [],
  }

  ); PersonalInfo = this.fb.group({
    dateOfBirth: [],
    entryInfo: this.EntryInfo,
    eyeColor: [],
    female: [],
    hairColor: [],
    height: [],
    male: [],
    name: this.Name,
    nationality: [],
    passport: this.Passport,
    phoneNumber: this.PhoneNumber,
    placeOfBirth: this.PlaceOfBirth,
    socialSecurityNumber: [],
    visaDenied: this.VisaDenied,
    visaImmigrant: this.VisaImmigrant,
    weight: [],
  }

  ); Benefit1 = this.fb.group({
    datesPublicBenefitReceived: [],
    nameOfPersonReceivingBenefit: [],
    typeOfPublicBenefit: [],
  }

  ); Benefit2 = this.fb.group({
    datesPublicBenefitReceived: [],
    nameOfPersonReceivingBenefit: [],
    typeOfPublicBenefit: [],
  }

  ); PublicBenefits = this.fb.group({
    benefit1: this.Benefit1,
    benefit2: this.Benefit2,
  }

  ); CurrentAddress = this.fb.group({
    address: [],
    fromMonthYear: [],
    toMonthYear: [],
  }

  ); PreviousAddress1 = this.fb.group({
    address: [],
    fromMonthYear: [],
    toMonthYear: [],
  }

  ); PreviousAddress2 = this.fb.group({
    address: [],
    fromMonthYear: [],
    toMonthYear: [],
  }

  ); PreviousAddress3 = this.fb.group({
    address: [],
    fromMonthYear: [],
    toMonthYear: [],
  }

  ); PreviousAddress4 = this.fb.group({
    address: [],
    fromMonthYear: [],
    toMonthYear: [],
  }

  ); ResidentialHistory = this.fb.group({
    currentAddress: this.CurrentAddress,
    previousAddress1: this.PreviousAddress1,
    previousAddress2: this.PreviousAddress2,
    previousAddress3: this.PreviousAddress3,
    previousAddress4: this.PreviousAddress4,
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

  );
  form = this.fb.group({
    childrenInformation: this.ChildrenInformation,
    classID: [],
    clientId: [],
    createdBy: [],
    createdOn: [],
    employmentHistory: this.EmploymentHistory,
    familyInformation: this.FamilyInformation,
    itFormID: [],
    itFormNo: [],
    language: [],
    matterNumber: [],
    personalInfo: this.PersonalInfo,
    publicBenefits: this.PublicBenefits,
    residentialHistory: this.ResidentialHistory,
    spouseInformation: this.SpouseInformation,
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
    private service: Form011Service,
    private fservice: MatterIntakeService,

    private spin: NgxSpinnerService, public toastr: ToastrService,

    private cas: CommonApiService,) { }

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
      this.btntext = "Save";
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

