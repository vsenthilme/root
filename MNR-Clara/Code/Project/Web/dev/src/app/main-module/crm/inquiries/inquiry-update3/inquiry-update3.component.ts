
import { Component, OnInit, Inject } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { InquiresService } from "../inquires.service";
import {ErrorStateMatcher} from '@angular/material/core';

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-inquiry-update3',
  templateUrl: './inquiry-update3.component.html',
  styleUrls: ['./inquiry-update3.component.scss']
})
export class InquiryUpdate3Component implements OnInit {
  public myModel = ''
    public mask = [/\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]
 // public mask = [ '+','1',  ' ', /\d/, /\d/, /\d/, '-',/\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]
  //public mask = [ '+','(', /[1-9]/,')', ' ', /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/]
  today = this.cs.today();
  maxDate = new Date();
  screenid: 1065 | undefined;
  showFiller = false;
  code: string = '';
  title: string = 'New';
  btntxt = 'Save';
  assigif = false;
  validationif = false;
  sub = new Subscription();
  form = this.fb.group({
    assignedUserId: [],
    assignedUserIdFE: [],
    classId: [],
    //contactNumber: [, [Validators.required,Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    contactNumber: [, [Validators.required,Validators.pattern('[0-9 -]+$')]],
    deletionIndicator: [0],
    email: [, [Validators.required, Validators.email ]],
    firstName: [, [Validators.required,Validators.maxLength(20), Validators.pattern('[a-zA-Z \S.-]+')]],
    inquiryDate: [this.cs.today()],//meed 
    inquiryModeId: [, [Validators.required]],
    //inquiryNotesNumber: [],
    inquiryNumber: [],
    intakeFormId: [],
    //intakeNotesNumber: [],
    // languageId: [, [Validators.required]],
    lastName: [, [Validators.required,Validators.maxLength(20), Validators.pattern('[a-zA-Z \S]+')]],
    // /referenceField9: [, Validators.pattern('[A-Za-z0-9]+$')],
    referenceField9: [],
    statusId: [],
    // transactionId: [],
    createdOn: [this.today],
    createdBy: [this.auth.userID, [Validators.required]],
    updatedBy: [this.auth.userID, [Validators.required]],
    updatedOn: [this.today],
    referenceField4: [],
    // assindby and assindon check with raj
  });

  email = new FormControl('', [Validators.required, Validators.email]);
  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    // if (this.email.hasError('required')) {
    //   return ' Field should not be blank';
    // }
    return this.email.hasError('required') ? ' Field should not be blank' : '';
      
  }

 // maxLength = 10;
  separateDialCode = true;
  constructor(public dialogRef: MatDialogRef<InquiryUpdate3Component>,
    @Inject(MAT_DIALOG_DATA,) public data: any,
    private fb: FormBuilder, private auth: AuthService,
    private service: InquiresService,
    private router: Router,
    private spin: NgxSpinnerService, public toastr: ToastrService,
    private cas: CommonApiService,
    private cs: CommonService,

  ) { }

  ngOnInit(): void {
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.inquiryDate.disable();
    this.auth.isuserdata();
    this.title = this.data.pageflow.replace(' - H', '');
    this.dropdownlist();

    if (this.data.code) {
      this.title = this.data.pageflow + ' - ' + this.data.code;
      this.fill();
      this.btntxt = 'Update';
    }

    if (this.data.pageflow == 'Inquiry Assign') {
      this.assigif = true;
      this.form.controls.assignedUserId.setValidators(Validators.required);
      this.form.controls.classId.setValidators(Validators.required);// not working
      this.btntxt = 'Assign';
    }

    else if (this.data.pageflow == 'Inquiry Validation') {
      this.assigif = true;
      this.validationif = true;
      this.form.controls.classId.setValidators([Validators.required]);

      this.form.controls.assignedUserId.setValidators([Validators.required]);
      this.form.controls.statusId.setValidators([Validators.required]);
      this.btntxt = 'Update & Validation';

    }
    this.form.updateValueAndValidity();

  }

  inquiryModeIdList: any[] = [];
  classIdList: any[] = [];
  useridList: any[] = [];
  statusIdList: any[] = [];

  selectedItems: SelectItem[] = [];
  multiselecttimekeeperList: SelectItem[] = [];
  multitimekeeperList: SelectItem[] = [];


  
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
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.inquiryModeId.url,
    this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.userId.url,
    this.cas.dropdownlist.setup.statusId.url]).subscribe((results) => {
      if (this.data.pageflow == "Inquiry New - H") {
        this.inquiryModeIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.inquiryModeId.key)
            .filter(s => s.key != 1);
    } else {
        this.inquiryModeIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.inquiryModeId.key);
    }
     // this.inquiryModeIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.inquiryModeId.key).filter(s => s.key != 1);
      this.classIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.classId.key).filter(s => s.key != 3);
      this.useridList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.userId.key);
      this.useridList.forEach((x: { key: string; value: string; }) => this.multitimekeeperList.push({id: x.key, itemName:  x.value}))
      this.multiselecttimekeeperList = this.multitimekeeperList;
      this.statusIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.statusId.key).filter(s => [4, 5, 6, 63, 64, 65, this.form.controls.statusId.value].includes(s.key));

    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();

  }
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
      res.inquiryDate = this.cs.dateapi(res.inquiryDate);
      this.form.patchValue(res, { emitEvent: false });
      // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
      //   this.form.controls.assignedUserId.patchValue([{id: res.assignedUserId,itemName: res.assignedUserId}]);
      // }
      if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
        this.form.controls.assignedUserId.patchValue([{id: res.assignedUserId,itemName: res.assignedUserId}]);
      }
      // console.log(this.multitimekeeperList)
      // this.multitimekeeperList.forEach(element => {
      //   if(element.id == res.assignedUserId){
      //     console.log(this.form)
      //     this.form.controls.assignedUserId.patchValue([element]);
      //   }
      // });
      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));

      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));


  }
  submit() {
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
    
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.form.removeControl('inquiryDate');
    if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
      this.form.patchValue({assignedUserId: this.selectedItems[0].id });
    }

    // this.form.patchValue({ updatedby: this.auth.userID, updatedOn: this.cs.todayapi() });
    if (this.data.code) {
      if (this.data.pageflow == 'Inquiry Assign')
        this.sub.add(this.service.Assign(this.form.getRawValue(), this.data.code).subscribe(res => {
          this.toastr.success(this.data.code + ' ' + this.btntxt + " successfully","Notification",{
              timeOut: 2000,
              progressBar: false,
            });
          this.spin.hide();
          this.dialogRef.close();

        }, err => {

          this.cs.commonerror(err);
          this.spin.hide();

        }));
      else if (this.data.pageflow == 'Inquiry Validation') {
        this.sub.add(this.service.updateValiationStatus
          (this.form.getRawValue(), this.data.code, this.form.controls.statusId.value).subscribe(res => {
            this.toastr.success(this.data.code + ' ' + this.btntxt + " successfully","Notification",{
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
      else
        this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code).subscribe(res => {
          this.toastr.success(res.inquiryNumber + ' ' + this.btntxt + " successfully","Notification",{
              timeOut: 2000,
              progressBar: false,
            });


          // window.reload();

          this.spin.hide();
          this.dialogRef.close();
        }, err => {

          this.cs.commonerror(err);
          this.spin.hide();

        }));


    }
    else {
      // this.form.patchValue({ createdOn: this.cs.todayapi(), inquiryDate: this.cs.todayapi() });
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {

        this.toastr.success(res.inquiryNumber + " saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.dialogRef.close();

        if (res.referenceField7 == 'true')
          this.toastr.success("Text Message Sent Successfully to the client", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
        else
          this.toastr.error("Text Message failed to sent", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });


        let url: string = this.router.url;
        if (this.data.pageflow == 'Inquiry New - H')
          this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
            this.router.navigate([url]);
          })
      }, err => {


        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
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

}

