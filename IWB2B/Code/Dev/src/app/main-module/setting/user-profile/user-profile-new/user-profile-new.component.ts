import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { UserprofileNewComponent } from 'src/app/main-module/admin/user-profile/userprofile-new/userprofile-new.component';
import { SettingsService } from '../../settings.service';

@Component({
  selector: 'app-user-profile-new',
  templateUrl: './user-profile-new.component.html',
  styleUrls: ['./user-profile-new.component.scss']
})
export class UserProfileNewComponent implements OnInit {

  email = new FormControl('', [Validators.required, Validators.email]);
  input: any;

  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }

    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  hide = true;
  sub = new Subscription();

  form = this.fb.group({
    companyCode: ["1000",],
    createdBy: [],
    createdOn: [],
    currencyDecimal: [],
    dateFormatId: [],
    deletionIndicator: [],
    emailId: [],
    firstName: [],
    isLoggedIn: [true,],
    languageId: ['EN',],
    lastName: [],
    password: [],
    passwordFE: [],
    resetPassword: [true,],
    statusId: [],
    timeZone: [],
    updatedBy: [],
    updatedOn: [],
    userId: [, Validators.required],
    userName: [],
    userRoleId: [1,],
    userTypeId: [1,],
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
    public dialogRef: MatDialogRef<UserprofileNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService, private service: SettingsService
  ) { }
  ngOnInit(): void {
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    if (this.data.pageflow != 'New') {
      this.form.controls.userId.disable();
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
    }
  }



  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
   //   this.form.get('password')?.reset();
   this.form.controls.statusId.patchValue(parseInt(res.statusId));
   this.form.controls.passwordFE.patchValue(this.form.controls.password.value);
   this.form.controls.password.patchValue(null);
      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }


  passwordChange(e){
    console.log(this.form.controls.passwordFE.value);
    this.form.controls.password.patchValue(this.form.controls.passwordFE.value);
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
    this.form.patchValue({ updatedby: this.auth.username });
    if (this.data.code && this.data.pageflow != 'Copy') {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code).subscribe(res => {
        this.toastr.success(this.data.code + " updated successfully!", "Notification", {
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
        this.toastr.success(res.userId + " saved successfully!", "Notification", {
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

  onNoClick(): void {
    this.dialogRef.close();
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
}

