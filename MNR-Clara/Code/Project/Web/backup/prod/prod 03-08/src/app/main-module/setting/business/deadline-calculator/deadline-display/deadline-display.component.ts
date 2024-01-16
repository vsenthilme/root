import { Component, OnInit, Inject } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { DeadlineCalculatorService } from "../deadline-calculator.service";


@Component({
  selector: 'app-deadline-display',
  templateUrl: './deadline-display.component.html',
  styleUrls: ['./deadline-display.component.scss']
})
export class DeadlineDisplayComponent implements OnInit {
  screenid: 1044 | undefined;

  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({

    classId: [, [Validators.required]],
    languageId: [, [Validators.required]],

    deadLineCalculationDays: [, [Validators.required, Validators.pattern('[0-9 -]+$')]],
    deadLineDaysStatus: ['Active', [Validators.required]],
    caseCategoryId: [, [Validators.required]],
    taskTypeCode: [, [Validators.required]],

    deletionIndicator: [0],
    createdBy: [this.auth.userID, [Validators.required]],
    createdOn: [],
    updatedOn: [],
    updatedBy: [this.auth.userID, [Validators.required]],

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
    public dialogRef: MatDialogRef<DeadlineDisplayComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: DeadlineCalculatorService,
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
      // this.form.controls.deadLineCalculationDays.disable();
      this.form.controls.classId.disable();
      this.form.controls.taskTypeCode.disable();
    // this.form.controls.caseCategoryId.disable();

    }
    this.form.controls.classId.valueChanges.subscribe(x => { this.classdropdownlist(x) });

  }

  languageIdList: any[] = [];
  classIdList: any[] = [];
  taskTypeCodeList: any[] = [];
  caseCategoryIdList: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.languageId.url,
    this.cas.dropdownlist.setup.classId.url,

    this.cas.dropdownlist.setup.caseCategoryId.url,
    ]).subscribe((results) => {
      this.languageIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.languageId.key);
      this.classIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.classId.key);
      this.caseCategoryIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.caseCategoryId.key);
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }

  classdropdownlist(x: any) {
    console.log(1)
    this.taskTypeCodeList = [];
    if (x) {
      this.spin.show();
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.setup.taskTypeCode.url,
      ]).subscribe((results) => {

        this.taskTypeCodeList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.taskTypeCode.key, { classId: x });
        console.log(this.taskTypeCodeList)
      }, (err) => {
        this.toastr.error(err, "");
      });
      this.spin.hide();
    }
  }
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });

      this.classdropdownlist(this.form.controls.classId.value)
    
      this.form.controls.taskTypeCode.patchValue(Number(res.taskTypeCode));
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
        this.toastr.success(" updated successfully!","Notification",{
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
        this.toastr.success(" saved successfully!", "Notification", {
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