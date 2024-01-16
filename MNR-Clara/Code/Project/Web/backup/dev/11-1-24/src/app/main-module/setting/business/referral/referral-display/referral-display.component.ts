import { Component, OnInit, Inject } from "@angular/core";
import { FormControl, Validators, FormBuilder } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { DialogExampleComponent, DialogData } from "src/app/common-field/dialog-example/dialog-example.component";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { ReferralElement, ReferralService } from "../referral.service";

@Component({
  selector: 'app-referral-display',
  templateUrl: './referral-display.component.html',
  styleUrls: ['./referral-display.component.scss']
})
export class ReferralDisplayComponent implements OnInit {
  screenid: 1056 | undefined;
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

    createdBy: [this.auth.userID, [Validators.required]],
    referralDescription:  [, [Validators.required]],
    classId: [, [Validators.required]],
    languageId: [, [Validators.required]],
    referralStatus: ["Active", [Validators.required]],
    updatedBy: [this.auth.userID, [Validators.required]],
    deletionIndicator: [0],
    referralId: [],
    subReferralDescription: [],
    subReferralId: [,],
    taxType: [,],
    updatedOn: [],
    createdOn: []
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
  formgr: ReferralElement | undefined;

  panelOpenState = false;
  constructor(
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: ReferralService,
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
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
      this.form.controls.referralId.disable();
    this.form.controls.classId.disable();
      this.form.controls.subReferralId.disable();
      this.form.controls.languageId.disable();

    }
  }


  caseCategoryIdList: any[] = [];
  classIdList: any[] = [];
  languageIdList: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url, this.cas.dropdownlist.setup.languageId.url
    ]).subscribe((results) => {

      this.classIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.classId.key);
      this.languageIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.languageId.key);

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
    this.form.patchValue({ updatedby: this.auth.username });
    if (this.data.code) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code).subscribe(res => {
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
    }
    else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.referralId + " saved successfully!", "Notification", {
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




