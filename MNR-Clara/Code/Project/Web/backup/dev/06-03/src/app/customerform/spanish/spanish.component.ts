import { HttpClient } from "@angular/common/http";
import { Component, OnDestroy, OnInit } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";

import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { InquiresService, inquiryElement } from "src/app/main-module/crm/inquiries/inquires.service";
import { IntakeService } from "src/app/main-module/crm/intake-snap-main/intake.service";
import { environment } from "src/environments/environment";
import { FormService } from "../form.service";
import { ImmigrationService } from "../immigration-form/immigration.service";
import { Location } from "@angular/common";
import { MatTableDataSource } from "@angular/material/table";
import { SpanishService } from "./spanish.service";


interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-spanish',
  templateUrl: './spanish.component.html',
  styleUrls: ['./spanish.component.scss']
})
export class SpanishComponent implements OnInit, OnDestroy {
  public myModel = ''     
  public myModel2= ''  
  public myModel3 = '' 
  public mask = [/\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]
  // public mask = [ '+','1',  ' ', /\d/, /\d/, /\d/, '-',/\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]
  //public mask = [ '+','(', /[1-9]/,')', ' ', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]
  screenid: 1068 | undefined;
  todaydate = new Date();
  displayedColumns: string[] = ['no', 'enter', 'exited'];
  dataSource = new MatTableDataSource<inquiryElement>();

  languageIdList = { languageId: ['SP'] };



  form = this.fb.group({
    address: [, [Validators.required]],
    attorney: [, []],
    attorneyDate: [, []],
    attorneyNotes: [, []],
    attorneyNotesForOthers: [, []],
    attorneyNotesForRemoval: [, []],
    classID: [, []],
    clientGeneralInfo: this.fb.group({

             //new fields added
             spouseCrimeNotes: [, []],
             visaPetitionNotes: [, []],
             victimOfACrimeNotes: [, []],
             guiltyNotes: [, []],
             entryExitNotes: [, []],
             deportedNotes: [, []],
             childNotes: [, []],
             deadlineDate: [, []],

             
             zipcode: [, []],
             state: [, []],
             city: [, []],

             whichCityAndCountryDidYouMarryIn: [, []],
             whatIsYourOccupation: [, []],
             areYouCurrentlyWorking: [, [Validators.required]],
             //areYouCurrentlyWorking_na:[],
             haveYouEverAskedForAnyPubilcAssistance: [, [Validators.required]],
             //haveYouEverAskedForAnyPubilcAssistance_na:[],
             
      ageOfClient: [, []],
      americanCitizenshipObtainedThrough: [, []],
      child1: this.fb.group({
        age: [, []],
        cityAndCountryOfBirth: [, []],
        dateOfBirth: [, []],
        doesChildLiveWithYou: [, []],
        nameOfChild: [, []],
      }),
      child2: this.fb.group({
        age: [, []],
        cityAndCountryOfBirth: [, []],
        dateOfBirth: [, []],
        doesChildLiveWithYou: [, []],
        nameOfChild: [, []],
      }),
      child3: this.fb.group({
        age: [, []],
        cityAndCountryOfBirth: [, []],
        dateOfBirth: [, []],
        doesChildLiveWithYou: [, []],
        nameOfChild: [, []],
      }),
      child4: this.fb.group({
        age: [, []],
        cityAndCountryOfBirth: [, []],
        dateOfBirth: [, []],
        doesChildLiveWithYou: [, []],
        nameOfChild: [, []],
      }),
      child5: this.fb.group({
        age: [, []],
        cityAndCountryOfBirth: [, []],
        dateOfBirth: [, []],
        doesChildLiveWithYou: [, []],
        nameOfChild: [, []],
      }),
      child6: this.fb.group({
        age: [, []],
        cityAndCountryOfBirth: [, []],
        dateOfBirth: [, []],
        doesChildLiveWithYou: [, []],
        nameOfChild: [, []],
      }),
      child7: this.fb.group({
        age: [, []],
        cityAndCountryOfBirth: [, []],
        dateOfBirth: [, []],
        doesChildLiveWithYou: [, []],
        nameOfChild: [, []],
      }),
      child8: this.fb.group({
        age: [, []],
        cityAndCountryOfBirth: [, []],
        dateOfBirth: [, []],
        doesChildLiveWithYou: [, []],
        nameOfChild: [, []],
      }),
      clientPersonalInfo: this.fb.group({
        cityAndCountryOfBirth: [, [Validators.required]],
        countryOfCitizenship: [, [Validators.required]],
        dateOfBirth: [, [Validators.required]],
        fullName: [, [Validators.required]],
        otherNamesUsed: [, [Validators.required]],
      }),
      clientReceivedBenefits: this.fb.group({
        anyHelpFromFederalStateOrLocalGovernment: [, ],
        areYouPayingAnyProperty: [, []],
        dateOfCrimeOrViolentActCommitted: [, []],
        doYouOwnYourHome: [,[Validators.required] ],
        //doYouOwnYourHome_na:[],
        foodStamps: [, []],
        haveYouOrAnyFamilyMemberEverbeenAVictimOfACrimeOrAViolentAct: [,[Validators.required] ],
        //haveYouOrAnyFamilyMemberEverbeenAVictimOfACrimeOrAViolentAct_na:[],
        medicaid: [, ],
        propertyType: [, []],
        publicHousing: [, ],
        sinceWhenFoodStamps: [, []],
        sinceWhenHelpOfferedFromFederalStateOrLocalGovernment: [, []],
        sinceWhenMedicaid: [, []],
        sinceWhenPublicHousing: [, []],
        summaryOfWhatHappened: [, []],
        whatType: [, []],
        wasItReported: [, []],
        //wasItReported_na:[],

 
      }),
     
      date: [, [Validators.required]],
      dateOfMarriage: [, []],
      dateOfMarriageOfDivorcedOrWidowed: [, []],
      datePetitionFiled: [, []],
      divorcedOrWidowedPersonalInfo: this.fb.group({
        cityAndCountryOfBirth: [, []],
        countryOfCitizenship: [, []],
        dateOfBirth: [, []],
        fullName: [, []],
        otherNamesUsed: [, []],
      }),

      enteredDates: this.fb.group({
        enteredDate1: [, []],
        enteredDate2: [, []],
        enteredDate3: [, []],
        enteredDate4: [, []],
        enteredDate5: [, []],
        lastEenteredDate: [, []],
      }),
      exitedDates: this.fb.group({
        exitedDate1: [, []],
        exitedDate2: [, []],
        exitedDate3: [, []],
        exitedDate4: [, []],
        exitedDate5: [, []],
        lastExitedDate: [, []],
      }),
      visaEntry: this.fb.group({
        didYouComeEnterWithVISA1: [, []],
        didYouComeEnterWithVISA2: [, []],
        didYouComeEnterWithVISA3: [, []],
        didYouComeEnterWithVISA4: [, []],
        didYouComeEnterWithVISA5: [, []],
        didYouComeEnterWithVISA6: [, []],
      }),
      firstDateOfEntry: this.fb.group({
        dateOfEntry: [, [Validators.required]],
        hiddenLocation: [, []],
        kindOfDocuments: [, []],
        placeOfArrival: [, [Validators.required]],
        unitedStatesEnteredThrough: [, ],
        wereYouInspectedByAnImmigrationOfficer: [,[Validators.required] ],
        //wereYouInspectedByAnImmigrationOfficer_na:[],
      }),
      hasAnyoneEverFiledImmigrantVisaPetition: [, [Validators.required]],
      hasSpouseEverbeenConvictedOfACrime: [, []],
    //  isPermanentResident: [, [Validators.required]],
      // isThisPermanentResident: [],
     // isThisPermanentResidentOrUSC: [, []],
     permanentResidentOrUSC: [, []],
      lastDateOfEntry: this.fb.group({
        dateOfEntry: [, [Validators.required]],
        hiddenLocation: [, []],
        kindOfDocuments: [, []],
        placeOfArrival: [, [Validators.required]],
        unitedStatesEnteredThrough: [, ],
        wereYouInspectedByAnImmigrationOfficer: [,[Validators.required] ],
        //wereYouInspectedByAnImmigrationOfficer_na:[],
      }),
      maritalStatus: [, [Validators.required]],
      nameOfFiledPersonForParents: [, []],
       permanentResident:  [, [Validators.required]],
      placeOfResidence: [, []],
      questions: this.fb.group({
        areYouCurrentlyEmployedUsingUSSSN: [,[Validators.required] ],
        //areYouCurrentlyEmployedUsingUSSSN_na:[],
        areYouCurrentlyLivingInUSA: [, [Validators.required]],
        //areYouCurrentlyLivingInUSA_na:[],
        areYouCurrentlyWorking: [, ],
        areYouReceivingUnemployment: [, []],
        certicateDetails: [, []],
        consulateDenyingVISAIssued: [, []],
        consulateThatIssuedVISA: [, []],
        crimeCharged: [, []],
        currentLocation: [, []],
        dateOfCrimeCharged: [, []],
        dateOfDeniedEntry: [, []],
        dateOfDeportation: [, []],
        dateOfDeportationProceedings: [, []],
        dateOfDriverLicense: [, []],
        dateOfFoundGuiltyCrime: [, []],
        dateOfStop: [, []],
        dateOfStoppedOrQuestioned: [, []],
        deniedEntryMoreThanOnce: [, []],
        deniedMoreThanOnce: [, []],
        //deniedMoreThanOnce_na:[],
        didYouGotoCourt: [, []],
        //didYouGotoCourt_na:[],
        doYouHaveEmploymentAuthorization: [,[Validators.required] ],
        //doYouHaveEmploymentAuthorization_na:[],
        doYouHaveValidDriverLicense: [, [Validators.required]],
        //doYouHaveValidDriverLicense_na:[],
        doYouPayTaxesInUS: [,[Validators.required] ],
        //doYouPayTaxesInUS_na:[],
        education: [, [Validators.required]],
        educationOther: [, []],
        employmentAuthorizationValidStatus: [, []],
        endResultOfDeportationProceedings: [, []],
        englishLanguageProficiency: [, [Validators.required]],
        expirationDateOFVISA: [, []],
        foundGuiltyCrimeCharged: [, []],
        foundGuiltyLocation: [, []],
        haveYouEverHadUSVisa: [, [Validators.required]],
        //haveYouEverHadUSVisa_na:[],
        haveYouEverbeenChargedWWithCommittingACrime: [,[Validators.required] ],
        //haveYouEverbeenChargedWWithCommittingACrime_na:[],
        haveYouEverbeenDeniedEntryIntoUS: [,[Validators.required] ],
        //haveYouEverbeenDeniedEntryIntoUS_na:[],
        haveYouEverbeenFoundGuiltyOfCommittingACrime: [, [Validators.required]],
        //haveYouEverbeenFoundGuiltyOfCommittingACrime_na:[],
        haveYouEverbeenDeportedOrRemovedFromUS: [, [Validators.required]],
        //haveYouEverbeenDeportedOrRemovedFromUS_na:[],
        haveYouEverbeenInDeportationProceedings: [, [Validators.required]],
        //haveYouEverbeenInDeportationProceedings_na:[],
        haveYouEverbeenRefusedUSVisa: [,[Validators.required]],
        //haveYouEverbeenRefusedUSVisa_na:[],
        haveYouEverbeenStoppedOrQuestionedByAnImmigOfficer: [,[Validators.required] ],
        //haveYouEverbeenStoppedOrQuestionedByAnImmigOfficer_na:[],
        haveYouEverbeenStoppedOrQuestionedByThePolice: [,[Validators.required] ],
        //haveYouEverbeenStoppedOrQuestionedByThePolice_na:[],
        haveYouFiledTaxesDuringLast3Years: [, [Validators.required]],
        //haveYouFiledTaxesDuringLast3Years_na:[],
        issueDateOFVISA: [, []],
        ssnValidStatus: [, []],
        locationOfCrimeCharged: [, []],
        locationOfDeniedEntry: [, []],
        locationOfDeportation: [, []],
        locationOfDeportedOrRemovedFromUS: [, []],
        locationOfStoppedByPolice: [, []],
        locationOfStoppedOrQuestioned: [, []],
        occupation: [, []],
        owningAnyCertications: [,[Validators.required]],
        //owningAnyCertications_na:[],
        penalty: [, []],
        reasonForDeportation: [, []],
        reasonForNonTaxFiling: [, []],
        reasonForStop: [, []],
        reasonForUnemployment: [, []],
        sinceWhen: [, []],
        stateOfDriverLicense: [, []],
        stayingSinceMonth: [, []],
        stayingSinceYear: [, []],
        stoppedMoreThanOnce: [, []],
        //stoppedMoreThanOnce_na:[],
        wereYouArrested: [, []],
       // wereYouArrested_na:[],
      }),
      relationshipOfFiledPersonForParents: [, []],
      residencyObtainedThrough: [, []],
      resultOfFiledPetition: [, []],
      spouseOrFiancePersonalInfo: this.fb.group({
        cityAndCountryOfBirth: [, []],
        countryOfCitizenship: [, []],
        dateOfBirth: [, []],
        fullName: [, []],
        otherNamesUsed: [, []],
      }),
      yearOfCitizenship: [, []],
      yearOfResidency: [, []],
    }),
    confRoomNo: [, []],
    contactNumber: this.fb.group({
      cellNo: [, [Validators.required]],
      faxNo: [, ],
      homeNo: [, ],
      workNo: [, ],
    }),

    contactPersonName: [, [Validators.required]],
    doHaveImmigrationAttorney: [, [Validators.required]],
    doNeedImmigrationUpdates: [, [Validators.required]],
    doYouLiveAtThisAddress: [, [Validators.required]],
    emailAddress: [, [, Validators.email]],
    feeInDollar: [, []],
    inquiryNo: [, []],
    itFormID: [, []],
    itFormNo: [, []],
    language: [, []],
    legalAssistant: [, []],
    name: [, [Validators.required]],
    nameOfAttorney: [, []],
    notes: [, []],
    purposeOfVisit: [, [Validators.required]],
    referenceMedium: this.fb.group({
      knownByRadio: [, []],
      listOfMediumAboutFirm: [, []],
      mediumAboutFirmFilter: [, []],
      referredByOthers: [, []],
      referredByOurClient: [, []],
    }),
    casePlan: this.fb.group({
      additonalFee: [],
      adminCost1: [],
      adminCost2: [],
      adminCost3: [],
      attorney: [],
      casePlan: [],
      checkOrCash: [],
      governmentFilingAmount1: [],
      governmentFilingAmount2: [],
      governmentFilingAmount3: [],
      governmentFilingAmount4: [],
      governmentFilingAmount5: [],
      governmentFilingAmount6: [],
      governmentFilngType1: [],
      governmentFilngType2: [],
      governmentFilngType3: [],
      governmentFilngType4: [],
      governmentFilngType5: [],
      governmentFilngType6: [],
      intialDepositAmount: [],
      legalAssistant: [],
      legalFee1: [],
      legalFee2: [],
      legalFee3: [],
      legalFeeType1: [],
      legalFeeType2: [],
      legalFeeType3: [],
      notes: [],
      paralegal: [],
      totalCost: [],
     }),
    thisConsultationIsFor: [, [Validators.required]],
    createdOn: [this.cs.todayapi()],
    createdBy: [this.auth.userID, []],
    updatedBy: [this.auth.userID, []],
    updatedOn: [this.cs.todayapi()]



  });


  intakefg = this.fb.group({
    alternateEmailId: [],
    approvedBy: [],
    approvedDate: [],
    classId: [],
    emailId: [],
    inquiryNumber: [],
    intakeFormId: [],
    intakeNotesNumber: [],
    receivedDate: [],
    referenceField1: [this.cs.todayapi(),  [Validators.required]],
    referenceField10: [],
    referenceField2: [,  [Validators.required]],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    sentDate: [],
    statusId: [],
    updatedBy: [],
    createdBy: [],
    createdOn: [],
    intakeNumber: [],
    intakeFormNumber: [],


  })

  sub = new Subscription();

  btntext = 'Submit';
  attorneyNotes: any[];
  disabled = false;
  step = 0;
  filterreferralList: any;
  filterreferralList1: any;
  filterClassId: any;
  production: boolean;

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
    private serviceIntake: IntakeService, private inquiresService: InquiresService,
    private service: SpanishService,
    private fservice: FormService,
    private router: Router,
    private spin: NgxSpinnerService, public toastr: ToastrService,
    private http: HttpClient,
    private cas: CommonApiService,) { 
      this.attorneyNotes = [
        {value: 'N-400', label: 'N-400'},
        {value: 'I-130', label: 'I-130'},
        {value: '485', label: '485'},
        {value: 'I-765', label: 'I-765'},
        {value: '601A', label: '601A'},
        {value: 'FOIA', label: 'FOIA'},
        {value: 'USCIS', label: 'USCIS'},
        {value: 'DACA Extension', label: 'DACA Extension'},
        {value: 'Travel Doc.', label: 'Travel Doc.'},
        {value: 'FBI', label: 'FBI'},
        {value: 'CP', label: 'CP'},
        {value: 'ICE', label: 'ICE'},
        {value: 'Jail Visit', label: 'Jail Visit'},
        {value: 'Bond Hearing', label: 'Bond Hearing'},
        {value: 'Affirmative Asylum', label: 'Affirmative Asylum'},
        {value: 'CBP', label: 'CBP'},
        {value: 'U Certification', label: 'U Certification'},
        {value: 'U Visa', label: 'U Visa'},
        {value: 'VAWA', label: 'VAWA'},
        {value: 'EOIR', label: 'EOIR'},
        {value: 'TN', label: 'TN'},
        {value: 'E-1', label: 'E-1'},
        {value: 'E-2', label: 'E-2'},
        {value: 'LIA', label: 'LIA'},
    ];
    }

  ngOnInit(): void {
    //for all itform
    let code = this.route.snapshot.params.code;
    let js = this.cs.decrypt(code);
    this.webemailpage = js.pageflow
    this.pageflow = js.pageflow.replace('newweb', 'New');
    if (this.webemailpage == 'newweb')
      this.sub.add(
        this.http.get<any>(`/mnr-setup-service/login?userId=` + environment.hyperlink_userid + `&password=` + environment.hyperlink_pasword).subscribe(
          (res) => {

            this.spin.hide();
            let menu = [1000, 1001, 1004, 2101, 2102, 2202, 2203];
            sessionStorage.setItem('menu', menu.toString());
            sessionStorage.setItem("user", JSON.stringify(res))
            this.onload(js);

          }
          ,
          (rej) => {
            this.spin.hide();
            this.toastr.error("", rej);
            this.form.disable();
            this.intakefg.disable();
          }
        )
      );
    else
      this.onload(js);

  }

  onload(js: any) {


    this.fill(js);
    this.dropdownlist();
    if (js.pageflow == 'newweb') {
      this.btncancel = true;
      this.form.controls.recommendations.clearValidators();
      this.form.controls.billingDepartmentNotes.clearValidators();
      this.form.controls.attorney.clearValidators();
      this.form.controls.notes.clearValidators();
      this.form.controls.issues.clearValidators();
    }
    else if (js.pageflow == 'validate')
      this.btntext = "Save";
    else if (this.pageflow == 'Display') {
      this.isbtntext = false;
      this.form.disable();
      this.intakefg.disable();
    }
    else if (js.pageflow == 'update')
      this.btntext = "Update & Assign";
  }
  fill(data: any) {

    if (data.intakeFormNumber) {
      this.spin.show();
      this.itformno = data.intakeFormNumber;
      this.sub.add(this.fservice.Get(data.intakeFormNumber).subscribe(res => {
        this.intakefg.patchValue(res, { emitEvent: false });
        this.filterClassId = res.classId;

        // this.multiselectreferralList = [];
        // this.multireferralList = [];
        // this.filterreferralList1 = [];
        //   this.filterClassId = res.classId;
        //   this.filterreferralList1 = this.filterreferralList.filter((element: { languageId: any; classId: any;}) => {
        //     return element.classId === this.filterClassId;
  
        //   });
        //   this.filterreferralList1.forEach((x: { referralId: string; referralDescription: string; }) => this.multireferralList.push({id: x.referralId, itemName: x.referralId + '-' + x.referralDescription}))
        //   this.multiselectreferralList = this.multireferralList;

        this.intakefg.controls.createdOn.patchValue(this.cs.dateapi(this.intakefg.controls.createdOn.value));
        if (this.pageflow == 'New') {
          this.form.controls.itFormNo.patchValue(res.intakeFormNumber);
          this.form.controls.itFormID.patchValue(res.intakeFormId);
          this.form.controls.inquiryNo.patchValue(res.inquiryNumber);
          this.form.controls.classID.patchValue(res.classId);
          this.form.controls.language.patchValue(res.languageId);

          if (res.statusId != 7) {
            this.router.navigate(['/mr/received']);
          }

        }
        else
          this.sub.add(this.service.Get(
            res.classId, res.inquiryNumber, res.intakeFormId, res.intakeFormNumber, res.languageId
          ).subscribe(res => {
            console.log(res)
            this.form.patchValue(res, { emitEvent: false });
          //   let pushData: SelectItem[] = [];
          //   if(res.referenceMedium.listOfMediumAboutFirm != null){
          //   res.referenceMedium.listOfMediumAboutFirm.forEach((data:any) => {
          //     this.multiselectreferralList.forEach((element: any) => {
          //       if(element.id == +data) {
          //         pushData.push(element);
          //       }
          //     });
          //   });
          // }
          //   this.form.get('referenceMedium')?.get('mediumAboutFirmFilter')?.patchValue(pushData);
            this.spin.hide();
         //   this.form.controls.doNeedImmigrationUpdates.setValue(res.doNeedImmigrationUpdates ? 'true' : 'false');
          }, err => {

            this.cs.commonerror(err);
            this.spin.hide();

          }));
        this.spin.hide();

      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
  }


  email = new FormControl('', [Validators.required, Validators.email]);
  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    if (control.includes('.')) {
      const controls = this.form.get(control);
      return controls ? controls.hasError(error) : false && this.submitted;

    }
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  public errorHandling_admin = (control: string, error: string = "required") => {
    if (control.includes('.')) {
      const controls = this.form.get(control);
      return controls ? controls.hasError(error) : false && this.submitted;
    }
    return this.intakefg.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (!this.form.valid && this.submitted && this.pageflow != 'update' && this.pageflow != 'validate') {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : this.email.hasError('email') ? ' Field should not be blank' : '';
  }


  errorList: any[] = [];
  submit() {
    this.errorList = [];
    let any = this.cs.findInvalidControlsRecursive(this.form);
    any.forEach(x => {
      if(x != "clientGeneralInfo" && x != "clientPersonalInfo" && x != "questions" && x != "clientReceivedBenefits" && x != " firstDateOfEntry"){
        this.errorList.push(x)
      }
    })
    this.production = this.cs.prodInstance();
    // if (this.selectedItems && this.selectedItems.length > 0){
    //   let referralList: any[]=[]
    //   this.selectedItems.forEach((a: any)=> referralList.push(a.id))
    //   this.form.patchValue({listOfMediumAboutFirm: referralList });
    // }
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.submitted = true;
    // if (this.form.invalid && this.pageflow != 'update' && this.pageflow != 'validate') {
    //      this.toastr.error(
    //     "Please fill the required fields to continue",
    //     "Notification",{
    //       timeOut: 2000,
    //       progressBar: false,
    //     }
    //   );

    //   this.cs.notifyOther(true);
    //   return;
    // }

    if (this.form.invalid && this.pageflow != 'update' && this.pageflow != 'validate') {
      this.errorList.forEach((x)=>{
        this.toastr.error(
          "Please fill the "+ x + " field to continue",
          "Notification", {
          timeOut: 10000,
          progressBar: false,
        }
        );
      })

      this.cs.notifyOther(true);
      return;
    }


    this.cs.notifyOther(false);

    if(this.form.get('clientGeneralInfo')?.get('lastDateOfEntry')?.get('wereYouInspectedByAnImmigrationOfficer')?.value == false && !this.form.get('clientGeneralInfo')?.get('lastDateOfEntry')?.get('unitedStatesEnteredThrough')?.value  && this.pageflow != 'update' && this.pageflow != 'validate'){
      this.toastr.error(
        "Please fill how did you enter the United States to continue",
        "Notification",{
          timeOut: 2000,
          progressBar: false,
        }
      );
    }

    if(this.form.get('clientGeneralInfo')?.get('firstDateOfEntry')?.get('wereYouInspectedByAnImmigrationOfficer')?.value == false && !this.form.get('clientGeneralInfo')?.get('firstDateOfEntry')?.get('unitedStatesEnteredThrough')?.value  && this.pageflow != 'update' && this.pageflow != 'validate'){
      this.toastr.error(
        "Please fill how did you enter the United States to continue",
        "Notification",{
          timeOut: 2000,
          progressBar: false,
        }
      );
    }

    if (this.pageflow == 'update' || this.pageflow == 'validate')
      if (!this.intakefg.controls.referenceField2.value) {
        this.toastr.error(
          "Please fill the required Assigned Attorney Field to continue",
          "Notification",{
            timeOut: 2000,
            progressBar: false,
          }
        );

        this.cs.notifyOther(true);
        return;
      }


    if (this.pageflow == 'validate')
    if (this.intakefg.controls.statusId.value != 5 && this.intakefg.controls.statusId.value != 6 && this.intakefg.controls.statusId.value != 10) {
         this.toastr.error(
          "Please fill the required status Field to continue",
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
    if (this.pageflow == 'New')
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {

        this.intakefg.controls.statusId.patchValue(9);
        this.sub.add(this.fservice.Update(this.intakefg.controls.intakeFormNumber.value, this.intakefg.getRawValue()).subscribe(res => {
          this.spin.hide();
          if (this.webemailpage != 'newweb')
            this.toastr.success(res.intakeFormNumber + ' ' + this.btntext + " successfully","Notification",{
              timeOut: 2000,
              progressBar: false,
            });
          this.spin.hide();
          if (this.webemailpage == 'newweb')
            this.router.navigate(['/mr/thanks']);
          else
            this.router.navigate(['/main/crm/inquiryvalidate']);

        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();

        }));
        // this.sub.add(this.fservice.Receive(this.intakefg.controls.inquiryNumber.value, this.intakefg.controls.intakeFormNumber.value).subscribe(res => {



        // }, err => {

        //   this.cs.commonerror(err);
        //   this.spin.hide();

        // }));

      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));

    else {

      this.sub.add(this.service.Update(this.form.getRawValue()).subscribe(res => {
        if (this.pageflow == 'update') {
          this.intakefg.controls.statusId.patchValue(8);
        }
        this.spin.show();
        this.sub.add(this.fservice.Update(this.intakefg.controls.intakeFormNumber.value, this.intakefg.getRawValue()).subscribe(res => {
          this.spin.hide();
          if (this.intakefg.controls.statusId.value == 10)
            this.toastr.success("Prospective Client " + res.referenceField7 + " created successfully","Notification",{
              timeOut: 2000,
              progressBar: false,
            });
          else this.toastr.success(this.intakefg.controls.intakeFormNumber.value + ' ' + this.btntext + " successfully","Notification",{
              timeOut: 2000,
              progressBar: false,
            });
          this.router.navigate(['/main/crm/inquiryform']);

        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();

        }));


      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
  }

selectedItems: any[] = [];
  multiselectreferralList: any[] = [];
  multireferralList: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectuseridList: SelectItem[] = [];
  multiuseridList: SelectItem[] = [];

  dropdownSettings = {
    singleSelection: false, 
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };
  referralList: any = [];
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
        this.useridList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.userId.key, { userTypeId: [1, 2] });
        this.statusIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [5, 6, 10].includes(s.key));

      }, (err) => {
        this.toastr.error(err, "");
      });
      this.sub.add(this.service.getreferral().subscribe(res => {
        this.referralList = res;
        this.filterreferralList = this.referralList.filter((element: { languageId: any; }) => {
          return element.languageId === 'SP';

        });
        this.filterreferralList1 = this.filterreferralList;
        this.filterreferralList1.forEach((x: { referralId: string; referralDescription: string; }) => this.multireferralList.push({value: (x.referralId).toString(), label: x.referralId + '-' + x.referralDescription}))
        this.multiselectreferralList = this.multireferralList;
        
        this.spin.hide();
        
      }, 
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    this.spin.hide();

  }

  back() {
    if (this.pageflow == 'New') {

      this.spin.show();
      this.sub.add(this.serviceIntake.Delete(this.intakefg.controls.intakeFormNumber.value, this.intakefg.controls.inquiryNumber.value).subscribe(res => {
        this.spin.hide();
        this.spin.show();
        this.sub.add(this.inquiresService.Assign(this.intakefg.getRawValue(), this.intakefg.controls.inquiryNumber.value).subscribe(res => {
          this.location.back();

          this.spin.hide();

        }, err => {


          this.cs.commonerror(err);
          this.spin.hide();

        }));
      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
    this.location.back();

  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

  onItemSelect(item:any){
    console.log(item);
    console.log(this.selectedItems);
}
OnItemDeSelect(item:any){
}
onSelectAll(items: any){
}
onDeSelectAll(items: any){
    console.log(items);
}
}