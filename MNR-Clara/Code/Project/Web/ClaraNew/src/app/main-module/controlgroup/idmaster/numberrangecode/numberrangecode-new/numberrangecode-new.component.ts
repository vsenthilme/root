import { Component, Inject, OnInit } from '@angular/core';
import { DialogExampleComponent } from 'src/app/common-field/dialog-example/dialog-example.component';
import { CommonService } from 'src/app/common-service/common-service.service';
import { NumberrangecodeService } from '../numberrangecode.service';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { AuthService } from 'src/app/core/core';

@Component({
  selector: 'app-numberrangecode-new',
  templateUrl: './numberrangecode-new.component.html',
  styleUrls: ['./numberrangecode-new.component.scss']
})
export class NumberrangecodeNewComponent implements OnInit {

 
  screenid: 1022 | undefined;
  email = new FormControl('', [Validators.required, Validators.email]);
  input: any;
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }
  sub = new Subscription();
  form = this.fb.group({
    companyId: ["1000"],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    languageId: [this.auth.languageId],
    numberRangeCode: [, [Validators.required]],
    numberRangeCurrent: [],
    numberRangeFrom: [],
    numberRangeObject: [, [Validators.required]],
    numberRangeStatus: [0,],
    numberRangeTo: [],
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
    updatedBy: [],
    updatedOn: [],
  });
  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
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
    public dialogRef: MatDialogRef < DialogExampleComponent > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: NumberrangecodeService ,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService,
  ) {}
  ngOnInit(): void {
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.languageId.disable();
    this.form.controls.companyId.disable();
    this.auth.isuserdata();
    this.dropdownlist();
    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
      this.form.controls.languageId.disable();
      this.form.controls.numberRangeCode.disable();
      this.form.controls.numberRangeObject.disable();
    }
  }
  languageIdList: any[] = [];
  dropdownSelectLanguageID: any[] = [];
  companyList: any[] = [];
  dropdownSelectcompanyID: any[] = [];
 
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.cgsetup.language.url,
      this.cas.dropdownlist.cgsetup.company.url,
    ]).subscribe((results) => {
      this.languageIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.cgsetup.language.key);
      this.languageIdList.forEach((x: {
        key: string;value: string;
      }) => this.dropdownSelectLanguageID.push({
        value: x.key,
        label: x.value
      }));
      this.companyList = this.cas.foreachlist(results[1], this.cas.dropdownlist.cgsetup.company.key);
      this.companyList.forEach((x: {
        key: string;value: string;
      }) => this.dropdownSelectcompanyID.push({
        value: x.key,
        label: x.value
      }))
      this.form.controls.languageId.patchValue(this.auth.languageId);
      this.form.controls.companyId.patchValue("1000");
      this.spin.hide();
        }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code, this.data.languageId,this.data.companyId,this.data.numberRangeObject).subscribe(res => {
      this.form.patchValue(res, {
        emitEvent: false
      });
      this.form.controls.numberRangeStatus.patchValue(res.numberRangeStatus != null ? res.numberRangeStatus.toString() : '');
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
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.form.patchValue({
      updatedby: this.auth.username
    });
    if (this.data.code) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code, this.data.languageId,this.data.companyId,this.data.numberRangeObject).subscribe(res => {
        this.toastr.success(res.numberRangeCode + " Number Range Code  updated successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.dialogRef.close();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    } else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.numberRangeCode + " Number Range Code  saved successfully!", "Notification", {
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
}


