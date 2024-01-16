
import { Component, OnInit, Inject, OnDestroy, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from "@angular/material/dialog";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DialogExampleComponent, DialogData } from "src/app/common-field/dialog-example/dialog-example.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { CaseCategoryElement } from "../../../idmaster/language/language.service";
import { ControlgroupService } from "../controlgroup.service";
import { SetupServiceService } from "src/app/common-service/setup-service.service";

@Component({
  selector: 'app-controlgroup-new',
  templateUrl: './controlgroup-new.component.html',
  styleUrls: ['./controlgroup-new.component.scss']
})
export class ControlgroupNewComponent implements OnInit {
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
    companyIdAndDescription: [],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    groupId: [,],
    groupName: [, [Validators.required]],
    groupTypeId: [, [Validators.required]],
    groupTypeName: [],
    languageId: [this.auth.languageId],
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
    statusId: [],
    versionNumber: [],
    updatedBy: [],
    updatedOn: [],
    createdOn_to: [],
    createdOn_from: [],
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
  formgr: CaseCategoryElement | undefined;
  panelOpenState = false;
  constructor(public dialog: MatDialog,
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: ControlgroupService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private setupService: SetupServiceService,
    private cs: CommonService,
  ) { }
  ngOnInit(): void {

    if (this.data.pageflow1 == 'TransactionCreate') {
      this.form.controls.languageId.patchValue(this.data.element.languageId);
      this.form.controls.companyId.patchValue(this.data.element.companyId);
      this.form.controls.groupTypeId.patchValue(this.data.element.groupTypeId);
    }

    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.languageId.disable();
    this.form.controls.companyId.disable();
    this.auth.isuserdata();
    this.dropdownlist();
    if (this.data.pageflow != 'New' && this.data.pageflow1 != 'TransactionCreate') {
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
      this.form.controls.languageId.disable();
      this.form.controls.companyId.disable();
      this.form.controls.groupTypeId.disable();
      this.form.controls.groupId.disable();
    }
  }


  countryList: any[] = [];
  controltypeList: any[] = [];
  dropdownSelectcontroltypeID: any[] = [];
  languageIdList: any[] = [];
  companyList: any[] = [];
  dropdownSelectcompanyID: any[] = [];
  dropdownSelectLanguageID: any[] = [];
  dropdownSelectcountryID: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.cgsetup.language.url,
      this.cas.dropdownlist.cgsetup.company.url,
      this.cas.dropdownlist.cgsetup.controlgrouptype.url,
    ]).subscribe((results) => {
      this.languageIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.cgsetup.language.key);
      this.languageIdList.forEach((x: { key: string; value: string; }) => this.dropdownSelectLanguageID.push({ value: x.key, label: x.value }));
      this.setupService.searchCompany({ languageId: [this.auth.languageId] }).subscribe(res => {
        this.dropdownSelectcompanyID = [];
        res.forEach(element => {
          this.dropdownSelectcompanyID.push({ value: element.companyId, label: element.companyId + '-' + element.description });
        });
        if (this.data.pageflow1 == 'TransactionCreate') {
          this.form.controls.languageId.patchValue(this.data.element.languageId);
          this.form.controls.companyId.patchValue(this.data.element.companyId);
          this.form.controls.groupTypeId.patchValue(this.data.element.groupTypeId);
        }
      });
      this.setupService.searchControlType({ languageId: [this.auth.languageId], companyId: ["1000"], statusId: [0] }).subscribe(res => {
        this.dropdownSelectcontroltypeID = [];
        res.forEach(element => {
          this.dropdownSelectcontroltypeID.push({ value: element.groupTypeId, label: element.groupTypeId + '-' + element.groupTypeName });
        });
      });
      this.form.controls.languageId.patchValue(this.auth.languageId);
      this.form.controls.companyId.patchValue("1000")
      this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
 
 
  onsubgroupChange(value) {
    this.setupService.searchControlType({
      companyId: [this.form.controls.companyId.value],
      languageId: [this.form.controls.languageId.value], groupTypeId: [value.value], statusId: [0]
    }).subscribe(res => {
      this.form.controls.groupTypeName.patchValue(res[0].groupTypeName);

    });
  }
  onclientChange(value) {
    this.setupService.searchControlType({
      companyId: [this.form.controls.companyId.value],
      languageId: [this.form.controls.languageId.value], groupTypeId: [value.value]
    }).subscribe(res => {
      this.form.controls.groupTypeName.patchValue(res[0].groupTypeName);

    });
  }
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code, this.data.languageId, this.data.companyId, this.data.groupTypeId, this.data.versionNumber).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
      this.form.controls.statusId.patchValue(res.statusId != null ? res.statusId.toString() : '');
      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.dropdownlist();
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
    this.form.patchValue({ updatedby: this.auth.username });
    if (this.data.code) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code, this.data.languageId, this.data.companyId, this.data.groupTypeId, this.data.versionNumber).subscribe(res => {
        this.toastr.success(res.groupId + " Group Type Id updated successfully!", "Notification", {
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
        this.toastr.success(res.groupId + " GroupId saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        if (this.data.pageflow1 == 'TransactionCreate') {
          this.dialogRef.close(res);
        } else {
          this.dialogRef.close();
          window.location.reload();
        }

        

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




