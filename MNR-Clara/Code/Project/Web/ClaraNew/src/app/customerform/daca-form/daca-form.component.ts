import { HttpClient } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
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
import { EnglishN400Service } from "../english-n400/english-n400.service";
import { FormService } from "../form.service";
import { Location } from "@angular/common";
import { DacaFormService } from "./daca-form.service";
import { date } from "@rxweb/reactive-form-validators";
@Component({
  selector: 'app-daca-form',
  templateUrl: './daca-form.component.html',
  styleUrls: ['./daca-form.component.scss']
})
export class DacaFormComponent implements OnInit {
  screenid: 1071 | undefined;
  todaydate = new Date();
  foods: any[] = [];


  displayedColumns: string[] = ['no', 'enter', 'exited', 'place'];
  dataSource = [];


  Child1 = this.fb.group({
    age: [],
    cityAndCountryOfBirth: [],
    dateOfBirth: [],
    doesChildLiveWithYou: [, []],
    nameOfChild: [],
  });

  Child2 = this.fb.group({
    age: [],
    cityAndCountryOfBirth: [],
    dateOfBirth: [],
    doesChildLiveWithYou: [, []],
    nameOfChild: [],
  });

  Child3 = this.fb.group({
    age: [],
    cityAndCountryOfBirth: [],
    dateOfBirth: [],
    doesChildLiveWithYou: [, []],
    nameOfChild: [],
  });

  Child4 = this.fb.group({
    age: [],
    cityAndCountryOfBirth: [],
    dateOfBirth: [],
    doesChildLiveWithYou: [, []],
    nameOfChild: [],
  });

  ClientEntryOutside1 = this.fb.group({
    dateOfEntered: [],
    dateOfExited: [],
    place: [],
  });

  ClientEntryOutside2 = this.fb.group({
    dateOfEntered: [],
    dateOfExited: [],
    place: [],
  });

  ClientEntryOutside3 = this.fb.group({
    dateOfEntered: [],
    dateOfExited: [],
    place: [],
  });

  ClientEntryOutside4 = this.fb.group({
    dateOfEntered: [],
    dateOfExited: [],
    place: [],
  });

  ClientEntryOutside5 = this.fb.group({
    dateOfEntered: [],
    dateOfExited: [],
    place: [],
  });

  ClientPersonalInfo = this.fb.group({
    cityAndCountryOfBirth: [],
    countryOfCitizenship: [],
    dateOfBirth: [],
    fullName: [],
    otherNamesUsed: [],
  });

  ContactNumber = this.fb.group({
    cellNo: [,[Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    faxNo: [,],
    homeNo: [,[Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    workNo: [,[Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
  });

  LastClientEntryOutside = this.fb.group({
    dateOfEntered: [],
    dateOfExited: [],
    place: [],
  });

  form = this.fb.group({
    address: [],
    attorney: [],
    bgt: [],
    billingDepartmentNotes: [],
    child1: this.Child1,
    child2: this.Child2,
    child3: this.Child3,
    child4: this.Child4,
    classID: [, Validators.required],
    clientEntryOutside1: this.ClientEntryOutside1,
    clientEntryOutside2: this.ClientEntryOutside2,
    clientEntryOutside3: this.ClientEntryOutside3,
    clientEntryOutside4: this.ClientEntryOutside4,
    clientEntryOutside5: this.ClientEntryOutside5,
    clientNumber: [],
    clientPGT: [],
    clientPersonalInfo: this.ClientPersonalInfo,
    confRoomNo: [],
    contactNumber: this.ContactNumber,
    contactPersonName: [],
    createdBy: [],
    createdOn: [],
    date: [],
    dateOfMarriage: [],
    doYouLiveAtThisAddress: [, Validators.required],
    doYouNeedImmigrationUpdates: [, Validators.required],
    emailAddress: [],
    hasSpouseEverbeenConvictedOfACrime: [, []],
    hasYourContactInfoChanged: [],
    hasYourMaritalStatusInfoChanged: [, []],
    inquiryNo: [],
    issues: [],
    itFormID: [, Validators.required],
    itFormNo: [, Validators.required],
    language: [, Validators.required],
    lastClientEntryOutside: this.LastClientEntryOutside,
    legalAssistant: [],
    maritalStatus: [],
    name: [, Validators.required],
    nameOfSpouseOrFiance: [],
    notes: [],
    placeOfResidence: [],
    purposeOfVisit: [, Validators.required],
    recommendations: [],
    updatedBy: [],
    updatedOn: [],
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
    referenceField2: [],
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



  disabled = false;
  step = 0;
  btntext = 'Submit';
  production: any;

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
    private service: DacaFormService,
    private fservice: FormService,
    private router: Router,
    private spin: NgxSpinnerService, public toastr: ToastrService,
    private http: HttpClient,
    private cas: CommonApiService) { }

  ngOnInit(): void {
    //for all itform

    this.production = this.cs.prodInstance();
    this.spin.show();

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
    
    this.cs.notifyOther(false);

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

