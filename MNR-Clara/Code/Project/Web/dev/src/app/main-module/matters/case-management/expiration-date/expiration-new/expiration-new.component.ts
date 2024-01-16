import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ExpenseService } from 'src/app/main-module/setting/business/expense-code/expense.service';
import { GeneralMatterService } from '../../General/general-matter.service';
import { ExpirationDateService } from '../expiration-date.service';

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-expiration-new',
  templateUrl: './expiration-new.component.html',
  styleUrls: ['./expiration-new.component.scss']
})
export class ExpirationNewComponent implements OnInit {
  screenid: 1131 | undefined;

  todaydate = new Date();
  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);


  form = this.fb.group({

    approvalDate: [],
    classId: [, Validators.required],
    clientId: [, Validators.required],
    createdBy: [this.auth.userID],
    createdOn: [],
    deletionIndicator: [0],
    documentType: [, Validators.required],
    eligibilityDate: [,],
    expirationDate: [],
    languageId: [, Validators.required],
    matterNumber: [, Validators.required],
    referenceField1: [],
    toggleNotification: [ , ],
    referenceField10: [],
    referenceField2: [],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    reminderDate: [],
    reminderDays: [, [Validators.pattern('[0-9 -]+$')]],
    reminderDescription: [, ],
    statusId: [],
    updatedBy: [this.auth.userID],
    updatedOn: [],
  });


  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  approvalDate: any;
  expirationDate: any;
  eligibilityDate: any;
  reminderDate: any;
  createdOn: any;
  updatedOn: any;
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }
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


  constructor(
    public dialogRef: MatDialogRef<ExpirationNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: ExpirationDateService, private matterService: GeneralMatterService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService, private mexpenseservice: ExpenseService,
  ) { }
  matter: any;
  btntext = "Save"
  ngOnInit(): void {

    this.form.controls.reminderDate.disable();

    this.form.controls.reminderDays.valueChanges.subscribe(x => { this.calculatdate(); });
this.form.controls.createdOn.disable();
this.form.controls.createdBy.disable();
this.form.controls.updatedOn.disable();
this.form.controls.updatedBy.disable();

    this.form.controls.eligibilityDate.valueChanges.subscribe(x => {
      if (x) {
        this.calculatdate();
      }
    });



    this.auth.isuserdata();
    this.dropdownlist();

    if (this.data.matter) {
      // this.matter = ' Matter - (' + this.data.matter + ') - ';
      this.matter = '' + this.data.matter + ' / ' + this.data.matterdesc + ' - ';
      this.form.controls.matterNumber.patchValue(this.data.matter);
      this.form.controls.matterNumber.disable();

    }
    this.getMatterDetails();
 
    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display') {
        this.form.disable();
        this.isbtntext = false;

      }
      this.fill();
      this.btntext = "Update";
      //this.form.controls.documentType.disable();

    }
  }
  calculatdate() {

    let reminderDate: Date = new Date(this.form.controls.eligibilityDate.value);
    reminderDate.setDate(reminderDate.getDate() - Number(this.form.controls.reminderDays.value));

    this.form.controls.reminderDate.patchValue(reminderDate);


  };
  documentTypelist: any[] = [];
  statusIdlist: any[] = [];

  selectedItems: SelectItem[] = [];
  multiselectdocumentList: any[] = [];
  multidocumentList: any[] = [];

  dropdownSettings = {
    singleSelection: true,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };


  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.documentType.url,

    // this.cas.dropdownlist.setup.noteText.url,
    this.cas.dropdownlist.setup.statusId.url,
      // this.cas.dropdownlist.matter.matterNumber.url,
    ]).subscribe((results) => {
      this.documentTypelist = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.documentType.key);
      this.documentTypelist.forEach((x: { key: string; value: string; }) => this.multidocumentList.push({value: x.key, label:  x.value}))
      this.multiselectdocumentList = this.multidocumentList;
      console.log(this.multiselectdocumentList)
      // this.taskTypeList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.taskTypeCode.key);
      this.statusIdlist = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.statusId.key).filter(x => [18, 21].includes(x.key));
      // this.matterNumberList = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.matter.matterNumber.key);
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }
  isbtntext = true;
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code.matterNumber, this.data.code.classId, this.data.code.clientId, this.data.code.documentType, this.data.code.languageId).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });

      this.form.controls.approvalDate.patchValue(this.cs.day_callapiSearch(res.approvalDate));
      this.form.controls.expirationDate.patchValue(this.cs.day_callapiSearch(res.expirationDate));
      this.form.controls.eligibilityDate.patchValue(this.cs.day_callapiSearch(res.eligibilityDate));
      this.form.controls.reminderDate.patchValue(this.cs.day_callapiSearch(res.reminderDate));

      this.approvalDate = res.approvalDate;
      this.expirationDate = res.expirationDate;
      this.eligibilityDate = res.eligibilityDate;
      this.reminderDate = res.reminderDate;
      this.createdOn = res.createdOn;
      this.updatedOn = res.updatedOn;

      // this.multidocumentList.forEach(element => {
      //   if(element.value == res.documentType){
      //     console.log(this.form)
      //     this.form.controls.documentType.patchValue([element.value]);
      //   }
      // });

      // let multiinquiryList: any[]=[]
      // multiinquiryList.push(res.documentType);
      // console.log(multiinquiryList)
      // this.form.patchValue({documentType: multiinquiryList });

      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  getMatterDetails() {
    this.spin.show();
    this.sub.add(this.matterService.Get(this.form.controls.matterNumber.value).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
      if (this.data.pageflow == 'New') {
        this.form.controls.approvalDate.patchValue(null);
        this.form.controls.expirationDate.patchValue(null);
      }
      this.form.controls.statusId.patchValue(38);
      // this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.spin.hide();

      this.form.controls.approvalDate.patchValue(this.cs.day_callapiSearch(this.approvalDate));
      this.form.controls.expirationDate.patchValue(this.cs.day_callapiSearch(this.expirationDate));
      this.form.controls.eligibilityDate.patchValue(this.cs.day_callapiSearch(this.eligibilityDate));
      this.form.controls.reminderDate.patchValue(this.cs.day_callapiSearch(this.reminderDate));
      this.form.controls.createdOn.patchValue(this.cs.excel_date(this.createdOn));
      this.form.controls.updatedOn.patchValue(this.cs.excel_date(this.updatedOn));

    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  submit() {
    this.submitted = true;
    let any = this.cs.findInvalidControls(this.form);
    console.log(any);
    if(this.form.controls.toggleNotification.value == true){
      this.form.controls['reminderDescription'].setValidators([Validators.required, Validators.maxLength(160),]);
      this.form.controls['reminderDescription'].updateValueAndValidity();
    }

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
console.log(this.form.controls.documentType.value)
    if(this.form.controls.documentType.value != "FOIAS"  && this.form.controls.documentType.value != "I-140s" && !this.form.controls.eligibilityDate.value){

      this.toastr.error(
        "Please Fill Eligibility date to Continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      this.cs.notifyOther(false);
      return;
    }
    if(this.form.controls.documentType.value != "FOIAS" && this.form.controls.documentType.value != "I-140s" && !this.form.controls.reminderDays.value){

      this.toastr.error(
        "Please Fill Reminder days to Continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      this.cs.notifyOther(false);
      return;
    }


    if (this.form.controls.reminderDescription.value && this.form.controls.reminderDescription.value.includes('&')) {
      this.toastr.error(
        "Invalid Charecter '&'",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
  }

    this.cs.notifyOther(false);
    this.spin.show();
    
    // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
    //   this.form.patchValue({documentType: this.selectedItems[0]});
    // }
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');


    this.form.controls.eligibilityDate.patchValue(this.cs.dateNewFormat(this.form.controls.eligibilityDate.value));
    this.form.controls.approvalDate.patchValue(this.cs.dateNewFormat(this.form.controls.approvalDate.value));
    this.form.controls.reminderDate.patchValue(this.cs.dateNewFormat(this.form.controls.reminderDate.value));
    this.form.controls.expirationDate.patchValue(this.cs.dateNewFormat(this.form.controls.expirationDate.value));


    this.form.patchValue({ updatedby: this.auth.username });
    if (this.data.code) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code.matterNumber).subscribe(res => {
        this.toastr.success(res.documentType + " updated successfully!","Notification",{
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
    else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.documentType + " saved successfully!", "Notification", {
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
  };
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }


  omit_special_char(e){
    let value = e.target.value
    if (value.includes('&')) {
      this.toastr.error(
        "Invalid Charecter '&'",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
  }
  }
}