import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { UserprofileService } from '../../userprofile/userprofile.service';
import { HhtuserService } from '../hhtuser.service';

@Component({
  selector: 'app-hhtuser-new',
  templateUrl: './hhtuser-new.component.html',
  styleUrls: ['./hhtuser-new.component.scss']
})
export class HhtuserNewComponent implements OnInit {

  disabled = false;
  step = 0;
 // dialogRef: any;

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }
  form = this.fb.group({
    languageId: [this.auth.languageId, ],
    companyCodeId: [this.auth.companyId, ],
    plantId: [this.auth.plantId, ],
    warehouseId: [this.auth.warehouseId, ],
    userId:[,Validators.required],
    password: [],
    passwordFE: [,Validators.required],
    userName: [,Validators.required],
    statusId: [],
    caseReceipts: [],
    itemReceipts: [],
    putaway: [],
    transfer: [],
    picking: [],
    quality: [],
    inventory: [],
    customerReturn: [],
    supplierReturn: [],
    referenceField1: [],
    referenceField2: [],
    referenceField3: [],
    referenceField4: [],
    referenceField5: [],
    referenceField6: [],
    referenceField7: [],
    referenceField8: [],
    referenceField9: [],
    referenceField10: [],
    deletionIndicator: [],
    createdBy: [],
    createdOn: [],
    updatedBy: [],
    updatedOn: [],
    createdOnFE: [],
    updatedOnFE: [],
  });
  panelOpenState = false;
  hide = true; 

  constructor(
    public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    private fb: FormBuilder,
    public cs: CommonService,
    private service: HhtuserService ,
    
  ) { }
  ngOnInit(): void {
    this.form.controls.companyCodeId.disable();
    this.form.controls.languageId.disable();
    this.form.controls.plantId.disable();
    this.form.controls.warehouseId.disable();


    this.form.controls.createdOnFE.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOnFE.disable();


    if (this.data.pageflow != 'New') {
      this.form.controls.userId.disable();
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
  }
}
fill() {
  this.spin.show();
  this.sub.add(this.service.Get(this.data.code, this.auth.warehouseId).subscribe(res => {
    this.form.patchValue(res, { emitEvent: false });
    this.form.controls.passwordFE.patchValue(this.form.controls.password.value);
    this.form.controls.password.patchValue(null);
    this.form.controls.statusId.patchValue(res.statusId.toString());
  this.form.controls.createdOnFE.patchValue(this.cs.dateapiutc0(this.form.controls.createdOn.value));
  this.form.controls.updatedOnFE.patchValue(this.cs.dateapiutc0(this.form.controls.updatedOn.value));
    this.spin.hide();
  },
   err => {
  this.cs.commonerror(err);
    this.spin.hide();
  }
  ));
}
sub = new Subscription();
submitted = false;

submit(){
this.submitted = true;
if (this.form.invalid) {
  this.toastr.error(
    "Please fill required fields to continue",
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

// this.form.controls.createdOn.patchValue(this.cs.day_callapi(this.form.controls.createdOnFE.value));
// this.form.controls.updatedOn.patchValue(this.cs.day_callapi(this.form.controls.updatedOnFE.value));

if (this.data.code) {
this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code, this.auth.warehouseId).subscribe(res => {
  this.toastr.success(this.data.code + " updated successfully!","Notification",{
    timeOut: 2000,
    progressBar: false,
  });
  this.spin.hide();
  this.dialogRef.close();

}, err => {

  this.cs.commonerror(err);
  this.spin.hide();

}));
}else{
this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
  this.toastr.success(res.userId + " Saved Successfully!","Notification",{
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

}


passwordChange(e){
  this.form.controls.password.patchValue(this.form.controls.passwordFE.value);
}

email = new FormControl('', [Validators.required, Validators.email]);
public errorHandling = (control: string, error: string = "required") => {
  return this.form.controls[control].hasError(error) && this.submitted;
}
getErrorMessage() {
  if (this.email.hasError('required')) {
    return ' Field should not be blank';
  }
  return this.email.hasError('email') ? 'Not a valid email' : '';
}
}
