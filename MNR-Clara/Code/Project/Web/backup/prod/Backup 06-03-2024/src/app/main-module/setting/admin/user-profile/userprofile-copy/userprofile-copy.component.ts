import { Component, OnInit, Inject } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DialogExampleComponent, DialogData } from "src/app/common-field/dialog-example/dialog-example.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { TimekeeperService } from "../../../business/timekeeper/timekeeper.service";
import { UserProfileService } from "../user-profile.service";

@Component({
  selector: 'app-userprofile-copy',
  templateUrl: './userprofile-copy.component.html',
  styleUrls: ['./userprofile-copy.component.scss']
})
export class UserprofileCopyComponent implements OnInit {

  screenid: 1016 | undefined;
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
    userId: [, Validators.required],

    classId: [, [Validators.required]],
    languageId: [this.auth.languageId, [Validators.required]],
    createdOn: [],
    updatedOn: [],
    createdBy: [this.auth.userID, [Validators.required]],
    updatedBy: [this.auth.userID, [Validators.required]],
    deletionIndicator: [0],



    emailId: [, [Validators.required, Validators.email]],
    firstName: [, [Validators.required]],
    fullName: [],
    lastName:  [, [Validators.required]],
    otpRequired: [],
    password: [, ],
    passwordFE: [, ],
    phoneNumber: [, [Validators.minLength(12), Validators.maxLength(12), Validators.pattern('[0-9 -]+$')]],
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
    userRoleId: [, [Validators.required]],
    userStatus: ["Active", [Validators.required]],
    userTypeId: [, [Validators.required]],


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
    public dialogRef: MatDialogRef<UserprofileCopyComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: UserProfileService,
    private timeKeeperService: TimekeeperService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService,
  ) { }
  ngOnInit(): void {
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    this.auth.isuserdata();
    this.dropdownlist();
    if (this.data.pageflow != 'New') {
      this.form.controls.userId.disable();
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
      if (this.data.pageflow != 'Copy') {
       // this.form.controls.classId.disable();
        this.form.controls.languageId.disable();
        //this.form.controls.userTypeId.disable();
      }
    }
  }


  caseCategoryIdList: any[] = [];
  classIdList: any[] = [];
  languageIdList: any[] = [];
  userTypeIdList: any[] = [];
  userRoleIdList: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.languageId.url,
    this.cas.dropdownlist.setup.userTypeId.url,
    this.cas.dropdownlist.setup.userRoleId.url
    ]).subscribe((results) => {

      this.classIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.classId.key);
      this.languageIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.languageId.key);
      this.userTypeIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.userTypeId.key);
      this.userRoleIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.userRoleId.key);

    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }

  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });

      this.form.controls.passwordFE.patchValue(this.form.controls.password.value);
      this.form.controls.password.patchValue(null);
   //   this.form.get('password')?.reset();
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
        this.sub.add(this.timeKeeperService.Update({timekeeperName: this.form.controls.fullName.value}, this.form.controls.userId.value).subscribe(timeKeeperRes => {

        }))
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

