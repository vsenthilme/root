import { HttpClient } from "@angular/common/http";
import { Component, OnDestroy, OnInit } from "@angular/core";
import { FormArray, FormBuilder, FormControl, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { InquiresService } from "src/app/main-module/crm/inquiries/inquires.service";
import { IntakeService } from "src/app/main-module/crm/intake-snap-main/intake.service";
import { environment } from "src/environments/environment";
import { FormService } from "../form.service";
import { Location } from "@angular/common";
import { LAndEService } from "./l-and-e.service";
interface SelectItem {
  id: string;
  itemName: string;
}
@Component({
  selector: 'app-l-and-e',
  templateUrl: './l-and-e.component.html',
  styleUrls: ['./l-and-e.component.scss']
})

export class LAndEComponent implements OnInit, OnDestroy {
  screenid: 1072 | undefined;
  btntext = 'Submit';
  languageIdList = { languageId: ['EN'] };
  todaydate = new Date();
  Address = this.fb.group({
    city: [, [Validators.required]],
    state: [, [Validators.required]],
    streetAddress: [, [Validators.required]],
    zipCode: [, [Validators.required]],
  });

  BillingAddress = this.fb.group({
    city: [, [Validators.required]],
    state: [, [Validators.required]],
    streetAddress: [, [Validators.required]],
    zipCode: [, [Validators.required]],
  });

  ClientReferenceMedium = this.fb.group({
    knownByRadio: [,],
    listOfMediumAboutFirm: [[], []],
    mediumAboutFirmFilter: [[], [Validators.required]],
    referredByOthers: [,],
    referredByOurClient: [,],
  });
  ContactNumber = this.fb.group({
    cellNo: [, [Validators.required]],
    faxNo: [, ],
    homeNo: [, [Validators.required]],
    workNo: [, [Validators.required]],
  });


  form = this.fb.group({
    address: this.Address,
    addressOfThirdPartyBilling: [],
    agreementSigned: [,],
    attorney: [,],
    authorizedAgent: [, [Validators.required]],
    authorizedAgentOfThirdPartyBilling: [,],
    billingAddress: this.BillingAddress,
    billingType: [,],
    classID: [,],
    clientReferenceMedium: this.ClientReferenceMedium,
    consulationFor: [, [Validators.required]],
    contactNumber: this.ContactNumber,
    date: [, [Validators.required]],
    doYouAlreadyHaveAnAttorney: [, [Validators.required]],
    emailAddress: [, [Validators.email, Validators.required]],
    inquiryNo: [,],
    itFormID: [,],
    itFormNo: [,],
    language: [,],
    legalAssistant: [,],
    name: [, [Validators.required]],
    nameOfAttorney: [,],
    nameOfThirdPartyBilling: [,],
    noOfEmployeesInCaseType: [0,],
    othersInCaseType: [,],
    othersInCaseTypeFE: [,],
    partnerInCharge: [,],
    purposeOfVisit: [, [Validators.required]],
    telephone: [,],
    typeOfCase: [,],
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
    referenceField2:  [,  ],
    referenceField2FE: [,  [Validators.required]],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    sentDate: [],
    statusId: [, [Validators.required]],
    updatedBy: [],
    createdBy: [],
    createdOn: [],
    intakeNumber: [],
    intakeFormNumber: [],


  })

  sub = new Subscription();



  disabled = false;
  step = 0;
  filterreferralList: any;
  caseCategoryIdList: any[] = [];
  filterClassId: any;
  filterreferralList1: any;
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
    private service: LAndEService,
    private fservice: FormService,
    private router: Router,
    private spin: NgxSpinnerService, public toastr: ToastrService,
    private http: HttpClient,
    private cas: CommonApiService,) { }

  ngOnInit(): void {
    //for all itform
    //  this.spin.show();
    //  this.form.controls.doYouAlreadyHaveAnAttorney.valueChanges.subscribe(val => {
    //    this.form.controls.nameOfAttorney.clearValidators();
    //    this.form.controls.nameOfAttorney.disable();
    //    if (val == 'true') {
    //      this.form.controls.nameOfAttorney.setValidators();
    //      this.form.controls.nameOfAttorney.enable();
    //    }
    //  });
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
  //  this.fill(js);
    this.dropdownlist(js);
   
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
        this.filterClassId = res.classId;
      this.multiselectreferralList = [];
      this.multireferralList = [];
      this.filterreferralList1 = [];
        this.filterreferralList1 = this.referralList.filter((element: {classId: any;}) => {
          return element.classId  == this.filterClassId;
        });
        this.filterreferralList1.forEach((x: { referralId: string; referralDescription: string; }) => this.multireferralList.push({ id: x.referralId, itemName: x.referralId + '-' +  x.referralDescription }))
        this.multiselectreferralList = this.multireferralList;
        console.log(this.multiselectreferralList)

        this.intakefg.patchValue(res, { emitEvent: false });
        this.multiuseridList.forEach(element => {
          if (element.id == res.referenceField2) {
            //  this.intakefg.controls.referenceField2FE.patchValue([element]);
            this.intakefg.patchValue({ referenceField2FE: [element] });
          }
        });
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
            this.form.patchValue(res, { emitEvent: false });
            console.log(res.clientReferenceMedium.listOfMediumAboutFirm)
            let pushData: SelectItem[] = [];
            if( res.clientReferenceMedium.listOfMediumAboutFirm != null){
              res.clientReferenceMedium.listOfMediumAboutFirm.forEach((data: any) => {
                this.multiselectreferralList.forEach((element: any) => {
                  if (element.id == +data) {
                    pushData.push(element);
                  }
                });
              });
            }
        
          this.form.get('clientReferenceMedium')?.get('mediumAboutFirmFilter')?.patchValue(pushData);


            this.multicasesubList.forEach(element => {
              if (element.id == res.othersInCaseType) {
                //  this.intakefg.controls.referenceField2FE.patchValue([element]);
                this.form.patchValue({ othersInCaseTypeFE: [element] });
              }
            });




            this.spin.hide();

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


  submit() {
    let any = this.cs.findInvalidControls(this.form);


 

    if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
      let referralList: any[] = []
      this.selectedItems.forEach((a: any) => referralList.push(a.id))
      this.form.get('clientReferenceMedium')?.get('listOfMediumAboutFirm')?.patchValue(referralList);
    }
    // if (this.selectedItems2 != null && this.selectedItems2 != undefined && this.selectedItems2.length > 0) {
    //   let multiuseridList: any[]=[]
    //   this.selectedItems2.forEach((a: any)=> multiuseridList.push(a.id))
    //   this.intakefg.get('referenceField2')?.patchValue(multiuseridList);
    // }

    if (this.selectedItems2 != null && this.selectedItems2 != undefined && this.selectedItems2.length > 0) {
      this.intakefg.patchValue({ referenceField2: this.selectedItems2[0].id });
    }

    if (this.selectedItems3 != null && this.selectedItems3 != undefined && this.selectedItems3.length > 0) {
      this.form.patchValue({ othersInCaseType: this.selectedItems3[0].id });
    }

    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.submitted = true;
    console.log(this.pageflow)
    console.log(this.form.invalid)
    // if (this.form.invalid && this.pageflow != 'update' && this.pageflow != 'validate') {
    //   this.toastr.error(
    //     "Please fill the required fields to continue",
    //     "Notification", {
    //     timeOut: 2000,
    //     progressBar: false,
    //   }
    //   );

    //   this.cs.notifyOther(true);
    //   return;
    // }


    if (this.form.invalid && this.pageflow != 'update' && this.pageflow != 'validate') {
      any.forEach((x)=>{
        this.toastr.error(
          "Please fill the "+ x + " fields to continue",
          "Notification", {
          timeOut: 10000,
          progressBar: false,
        }
        );
      })

      this.cs.notifyOther(true);
      return;
    }
    // if (this.form.invalid && this.pageflow != 'update' && this.pageflow != 'validate') {
    //   any.forEach((x)=>{
    //     this.toastr.error(
    //       "Please fill the "+ x + " fields to continue",
    //       "Notification", {
    //       timeOut: 2000,
    //       progressBar: false,
    //     }
    //     );
    //     this.cs.notifyOther(true);
    //     return;
    //   })
    // }

    this.cs.notifyOther(false);

    if (this.pageflow == 'update' || this.pageflow == 'validate')
      if (!this.intakefg.controls.referenceField2.value) {
        this.toastr.error(
          "Please fill the required Assigned Attorney Field to continue",
          "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
        );

        this.cs.notifyOther(true);
        return;
      }


    if (this.pageflow == 'validate')
      if (this.intakefg.controls.statusId.value != 5 && this.intakefg.controls.statusId.value != 6 && this.intakefg.controls.statusId.value != 10 && this.intakefg.controls.statusId.value != 70 && this.intakefg.controls.statusId.value != 71 && this.intakefg.controls.statusId.value != 72 && this.intakefg.controls.statusId.value != 73 && this.intakefg.controls.statusId.value != 74) {
        this.toastr.error(
          "Please fill the required status Field to continue",
          "Notification", {
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
        if (this.selectedItems && this.selectedItems.length > 0) {
          let referralList: any[] = []
          this.selectedItems.forEach((a: any) => referralList.push(a.id))
          this.form.patchValue({ listOfMediumAboutFirm: referralList });
        }
        if (this.selectedItems2 && this.selectedItems2.length > 0) {
          let multiuseridList: any[] = []
          this.selectedItems2.forEach((b: any) => multiuseridList.push(b.id))
          this.form.patchValue({ referenceField2: multiuseridList });
        }
        this.intakefg.controls.statusId.patchValue(9);
        this.sub.add(this.fservice.Update(this.intakefg.controls.intakeFormNumber.value, this.intakefg.getRawValue()).subscribe(res => {
          this.spin.hide();
          if (this.webemailpage != 'newweb')
            this.toastr.success(res.intakeFormNumber + ' ' + this.btntext + " Successfully", "Notification", {
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
          else this.toastr.success(this.intakefg.controls.intakeFormNumber.value + ' ' + this.btntext + " Successfully", "Notification", {
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

  selectedItems: SelectItem[] = [];
  multiselectreferralList: SelectItem[] = [];
  multireferralList: SelectItem[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectuseridList: SelectItem[] = [];
  multiuseridList: SelectItem[] = [];

  selectedItems3: SelectItem[] = [];
  multiselectcasesubList: SelectItem[] = [];
  multicasesubList: SelectItem[] = [];

  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };
  dropdownSettings1 = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };
  referralList: any = [];
  useridList: any[] = [];
  statusIdList: any[] = [];
  referenceList: any[] = [];
  casesubList: any[] = [];

  dropdownlist(js?:any) {
    //this.fill(js);
    this.spin.show();
    this.cas.getalldropdownlist([
      //   this.cas.dropdownlist.setup.inquiryModeId.url,
      // this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.setup.userId.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.referralId.url,
      this.cas.dropdownlist.setup.caseSubcategoryId.url,
    ]).subscribe((results) => {
      // this.inquiryModeIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.inquiryModeId.key);
      // this.classIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.classId.key);
      this.useridList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.userId.key, { userTypeId: [1, 2] });
      this.useridList.forEach((x: { key: string; value: string; }) => this.multiuseridList.push({ id: x.key, itemName: x.value }))
      this.multiselectuseridList = this.multiuseridList;
      this.statusIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [5, 6, 10,70,71,72,73,74].includes(s.key));
      //this.referralList = this.cas.foreachlist1(results[2], this.cas.dropdownlist.setup.referralId.key, { languageId: 'EN' });
      this.referralList = results[2];
      this.filterreferralList1 = this.referralList
      this.filterreferralList1.forEach((x: { referralId: string; referralDescription: string; }) => this.multireferralList.push({ id: x.referralId, itemName: x.referralId + '-' +  x.referralDescription }))
      this.multiselectreferralList = this.multireferralList;
      

      this.caseCategoryIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.caseSubcategoryId.key);
      this.caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicasesubList.push({ id: x.key, itemName: x.value }))
      this.multiselectcasesubList = this.multicasesubList;
      this.fill(js);
    }, (err) => {
      this.toastr.error(err, "");
    });


    this.spin.hide();

  }

  back() {
    if (this.pageflow == 'New') {

      this.spin.show();
      this.sub.add(this.serviceIntake.Delete(this.intakefg.controls.intakeFormNumber.value, this.intakefg.controls.inquiryNumber.value).subscribe(res => {
        this.sub.add(this.inquiresService.Assign({statusId: 4}, this.intakefg.controls.inquiryNumber.value).subscribe(updateres => {                        //this.intakefg.getRawValue()
        if(updateres){
          this.location.back();
          this.spin.hide();
        }
        }, err => {


          this.cs.commonerror(err);
          this.spin.hide();

        }));
    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    }));
    }else{
      this.location.back();
    }

  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

}

