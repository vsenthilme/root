import { Component, Inject, OnDestroy, OnInit } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CancelComponent } from "src/app/common-field/dialog_modules/cancel/cancel.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { InquiresService } from "../../inquiries/inquires.service";
import { InquiryUpdate3Component } from "../../inquiries/inquiry-update3/inquiry-update3.component";
import { PotentialService, prospectiveClienlistelement } from "../potential.service";

interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-display',
  templateUrl: './display.component.html',
  styleUrls: ['./display.component.scss']
})
export class DisplayComponent implements OnInit, OnDestroy {
  screenid: 1076 | undefined;

  showFiller = false;
  animal: string | undefined;
  name: string | undefined;
  prospectiveClienlist: prospectiveClienlistelement[] = [];
  // openDialog3(): void {

  //   const dialogRef = this.dialog.open(CancelComponent, {
  //     disableClose: true,
  //     width: '30%',
  //     maxWidth: '80%',
  //   });

  //   dialogRef.afterClosed().subscribe(result => {


  //   });
  // }
  sub = new Subscription();
  form = this.fb.group({
    addressLine1: [, [Validators.required]],
    addressLine2: [],
    addressLine3: [],
    agreementCode: [],
    alternateTelephone1: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    alternateTelephone2: [, [Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    caseCategoryId: [],
    //caseCategoryIdFE: [],
    city: [],
    classId: [],
    consultationDate: [],
    contactNumber: [],
    country: [],
    deletionIndicator: [0],
    emailId: [, [Validators.email ]],//alteranateemail
    firstName: [,  Validators.pattern('[a-zA-Z0-9 \S]+')],
    firstNameLastName: [, [Validators.required, Validators.pattern('[a-zA-Z0-9 \S]+')]],
    inquiryNumber: [, [Validators.required]],
    intakeFormId: [],
    intakeFormNumber: [],
    languageId: [],
    lastName: [,  Validators.pattern('[a-zA-Z0-9 \S]+')],
    lastNameFirstName: [],
    mailingAddress: [],
    occupation: [],
    pcNotesNumber: [],
    potentialClientId: [],
    referenceField1: [],//followupBy
    referenceField10: [],
    referenceField2: [],//assignedAttorney

    referenceField3: [], //prospectiveClientStatus
    referenceField4: [],
    referenceField5: [, [ Validators.email ]],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    referralId: [],
    referralIdFE: [],
    socialSecurityNo: [],
    state: [],
    statusId: [],//onboardingStatus
    statusId_des: [],//onboardingStatus
    transactionId: [],
    zipCode: [],

    createdOn: [this.cs.today()],
    createdBy: [this.auth.userID, [Validators.required]],
    updatedBy: [this.auth.userID, [Validators.required]],
    updatedOn: [this.cs.today()],
    clientCategoryId:  [, [Validators.required]],
    //Client Category


  });

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


  email = new FormControl('', [Validators.required, Validators.email]);
  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    // if (this.email.hasError('required')) {
    //   return ' Field should not be blank';
    // }
    return this.email.hasError('email') ? 'Not a valid email' : this.email.hasError('required') ? ' Field should not be blank' : '';
  }

  constructor(public dialogRef: MatDialogRef<InquiryUpdate3Component>,
    @Inject(MAT_DIALOG_DATA,) public data: any,
    private fb: FormBuilder, private auth: AuthService,
    private service: PotentialService,

    private spin: NgxSpinnerService, public toastr: ToastrService,
    private cas: CommonApiService,
    private cs: CommonService,

  ) { }

  ngOnInit(): void {
    this.form.controls.inquiryNumber.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.statusId_des.disable();
    this.auth.isuserdata();
    if (this.data.pageflow == 'display') {
      this.form.disable();
      this.dropdownSettings.disabled=true;
    }
    this.dropdownlist();
    //this.form.controls.firstNameLastName.disable();


  }

  Fullname(){
    this.form.patchValue({
      firstName: this.form.value.firstName,
      lastName: this.form.value.lastName,
      firstNameLastName: this.form.value.firstName +' '+ this.form.value.lastName
    });
  }

  referralIdList: any[] = [];
  subReferalIdList: any[] = [];
  clientCategoryIdList: any[] = [];
  prospectiveClientStatusList: any[] = [];
  onboardingStatusList: any[] = [];
  classIdList: any[] = [];
  useridList: any[] = [];
  statusIdList: any[] = [];
  caseCategoryIdList: any[] = [];


  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.referralId.url,
      this.cas.dropdownlist.setup.subReferalId.url,
      this.cas.dropdownlist.setup.clientCategoryId.url,
      this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.setup.userId.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.caseCategoryId.url
    ]).subscribe((results) => {
      this.referralIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.referralId.key);
      this.referralIdList.forEach((x: { key: string; value: string; }) => this.multireferralList.push({ value: x.key, label:  x.value }))
      this.multiselectreferralList = this.multireferralList;
      this.subReferalIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.subReferalId.key);
      this.clientCategoryIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.clientCategoryId.key);
      this.classIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.classId.key);
      this.useridList = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.userId.key);
      this.statusIdList = this.cas.foreachlist(results[5], this.cas.dropdownlist.setup.statusId.key);
      this.caseCategoryIdList = this.cas.foreachlist(results[6], this.cas.dropdownlist.setup.caseCategoryId.key);
      this.caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicaseCategoryList.push({ value: x.key, label:  x.value }))
      this.multiselectcaseCategoryList = this.multicaseCategoryList;
      this.fill();

    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();

  }

  fill() {
    console.log(this.multiselectcaseCategoryList)
    this.spin.show();
    this.sub.add(this.service.Get(this.data.data.potentialClientId).subscribe(res => {
      //  res.consultationDate = null;
      console.log(res)
      this.form.patchValue(res, { emitEvent: false });

      // this.multireferralList.forEach(element => {
      //   console.log(res.referralId)
      //   console.log(this.multireferralList)
      //   if(element.id == res.referralId){
      
      //     this.form.controls.referralIdFE.patchValue([element]);
      //   }
      // });
      // let multiinquiryList1: any[]=[]
      // if(res.referralId){
      //   multiinquiryList1.push(res.referralId);
      // console.log(multiinquiryList1)
      // this.form.patchValue({referralId: multiinquiryList1 });
      // }

      // this.multicaseCategoryList.forEach(element => {
      //   if(element.id == res.caseCategoryId){
      
      //     this.form.controls.caseCategoryIdFE.patchValue([element]);
      //   }
      // });
      // let multiinquiryList: any[]=[]
      // multiinquiryList.push(res.caseCategoryId);
      // console.log(multiinquiryList)
      // this.form.patchValue({caseCategoryId: multiinquiryList });

      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      // if (res.statusId == 15)
      //   this.form.controls.referenceField3.disable();
      if (this.statusIdList.length > 0)
        this.form.controls.statusId_des.patchValue(this.statusIdList.filter(x => x.key == res.statusId)[0].value);


      this.prospectiveClienlist.push({ status: 'Created On', date: res.createdOn });
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Get_agreement(res.agreementCode).subscribe(res => {

        if (res.sentOn)
          this.prospectiveClienlist.push({ status: 'Sent On', date: res.sentOn });
        if (res.approvedOn)

          this.prospectiveClienlist.push({ status: 'Approved On', date: res.approvedOn });
        if (res.receivedOn)

          this.prospectiveClienlist.push({ status: 'Received On', date: res.receivedOn });
        // if (res.resentOn)

        //   this.prospectiveClienlist.push({ status: 'Resent On', date: res.resentOn });

        this.spin.hide();
      }, err => {
        //  this.cs.commonerror(err);
        this.spin.hide();
      }));

    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));


  }


  submit() {
    //    this.form.controls.contactNumber.patchValue(this.form.controls.contactNumber.value.e164Number)
         this.form.patchValue({ updatedBy: this.auth.userID, updatedOn: this.cs.todayapi() });
    console.log(this.auth.userID)
    let any = this.cs.findInvalidControls(this.form);
    console.log(any)
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
    // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
    //   this.form.patchValue({ referralId: this.selectedItems[0].id });
    // }
    // if (this.selectedItems2 != null && this.selectedItems2 != undefined && this.selectedItems2.length > 0) {
    //   this.form.patchValue({ caseCategoryId: this.selectedItems2[0] });
    // }
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');



    this.sub.add(this.service.Update(this.form.getRawValue(), this.data.data.potentialClientId).subscribe(res => {
      this.toastr.success(this.data.data.potentialClientId + " updated successfully","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      this.dialogRef.close();

    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    }));
  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }
  }

  selectedItems: SelectItem[] = [];
  multiselectreferralList: any[] = [];
  multireferralList: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectcaseCategoryList: any[] = [];
  multicaseCategoryList: any[] = [];



  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

}


