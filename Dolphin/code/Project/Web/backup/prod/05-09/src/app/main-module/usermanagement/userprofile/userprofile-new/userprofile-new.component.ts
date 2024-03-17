import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { UserprofileService } from '../userprofile.service';
import { HhtuserService } from '../../hhtuser/hhtuser.service';

@Component({
  selector: 'app-userprofile-new',
  templateUrl: './userprofile-new.component.html',
  styleUrls: ['./userprofile-new.component.scss']
})
export class UserprofileNewComponent implements OnInit {
 

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
  form = this.fb.group({
    companyCode: [this.auth.companyId],
    createdBy: [],
    createdOn: [],
    currencyDecimal: [],
    dateFormatId: [],
    deletionIndicator: [],
    emailId: [],
    firstName: [,Validators.required],
    languageId: [this.auth.languageId,],
    lastName: [],
    plantId: [this.auth.plantId,],
    statusId: [],
    timeZone: [],
    updatedBy: [],
    updatedOn: [],
    userId: [,Validators.required],
    userName: [,Validators.required],
    userRoleId: [1,],
    userTypeId: [,Validators.required],
    warehouseId: [this.auth.warehouseId, ],
    password:[,],
    passwordFE:[,Validators.required],
    createdOnFE: [],
    updatedOnFE: [],
  });

  hide = true;

  panelOpenState = false;
  constructor(
    public dialogRef: MatDialogRef<any>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    public auth: AuthService,
    private fb: FormBuilder,
    private HhtuserService: HhtuserService,
    public cs: CommonService,
    private service: UserprofileService,
  ) { }
  ngOnInit(): void {
    this.form.controls.companyCode.disable();
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
sub = new Subscription();
submitted = false;


fill() {
  this.spin.show();
  this.sub.add(this.service.Get(this.data.code, this.auth.warehouseId).subscribe(res => {
    this.form.patchValue(res, { emitEvent: false });
    this.form.controls.passwordFE.patchValue(this.form.controls.password.value);
    this.form.controls.password.patchValue(null);
    this.form.controls.statusId.patchValue(res.statusId ? res.statusId.toString() : '');
    this.form.controls.userTypeId.patchValue(res.userTypeId ? res.userTypeId.toString() : '');
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


passwordChange(e){
  this.form.controls.password.patchValue(this.form.controls.passwordFE.value);
}

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


  if(this.form.controls.userTypeId.value == 7){
    this.sub.add(this.HhtuserService.Create(this.form.getRawValue()).subscribe(res => {
      this.toastr.success(res.userId + " HHT Saved Successfully!","Notification",{
        timeOut: 2000,
        progressBar: false,
      });
    }))
  }
  
  this.spin.hide();
  this.dialogRef.close();

}, err => {
  this.cs.commonerror(err);
  this.spin.hide();

}));
}

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