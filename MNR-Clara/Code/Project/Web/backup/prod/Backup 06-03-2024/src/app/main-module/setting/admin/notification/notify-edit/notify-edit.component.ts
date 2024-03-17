import { Component, OnInit, Inject, OnDestroy } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DialogExampleComponent, DialogData } from "src/app/common-field/dialog-example/dialog-example.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { NotificationService } from "../notification.service";

@Component({
  selector: 'app-notify-edit',
  templateUrl: './notify-edit.component.html',
  styleUrls: ['./notify-edit.component.scss']
})
export class NotifyEditComponent implements OnInit {


  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({
    createdBy: [this.auth.userID, [Validators.required]],
    languageId: ['EN', [Validators.required]],
    notificationId: [,],
    classId: [3, [Validators.required]],
    notificationDescription: [,],
    transactionId: [, [Validators.required]],
    userId: [, [Validators.required]],
    createdOn: [],
    updatedOn: [],
    updatedBy: [this.auth.userID, [Validators.required]],
    deletionIndicator: [0]
  });




  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
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
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: NotificationService,
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
    this.form.controls.classId.disable();
    this.form.controls.languageId.disable();

    this.auth.isuserdata();
    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
      this.form.controls.notificationId.disable();

      this.form.controls.classId.disable();
      this.form.controls.languageId.disable();
      this.form.controls.userId.disable();
      this.form.controls.transactionId.disable();

    }
    this.dropdownlist();
  }

  languageIdList: any[] = [];
  userIdList: any[] = [];
  classIdList: any[] = [];
  transactionIdList: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.languageId.url,
    this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.userId.url,
    this.cas.dropdownlist.setup.transactionId.url]).subscribe((results) => {
      this.languageIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.languageId.key);
      this.classIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.classId.key);
      this.userIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.userId.key);
      this.transactionIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.transactionId.key);

    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code.notificationId, this.data.code.classId, this.data.code.languageId, this.data.code.transactionId, this.data.code.userId).subscribe(res => {
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

    this.form.patchValue({ updatedby: this.auth.userID, updatedOn: this.cs.todayapi() });
    if (this.data.code) {
      this.form.patchValue({ updatedby: this.auth.userID, updatedOn: this.cs.todayapi() });
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code.notificationId,
        this.form.controls.classId.value, this.form.controls.languageId.value, this.form.controls.transactionId.value,
        this.form.controls.userId.value
      ).subscribe(res => {
        this.toastr.success(res.notificationId + " updated successfully!","Notification",{
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
      this.form.patchValue({ createdOn: this.cs.todayapi() });
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.notificationId + " saved successfully!", "Notification", {
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



