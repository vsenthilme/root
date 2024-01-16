import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService, dropdownelement } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { CoaService } from '../../chart-of-accounts/coa.service';
import { GlMappingService } from '../gl-mapping.service';

@Component({
  selector: 'app-glmapping-new',
  templateUrl: './glmapping-new.component.html',
  styleUrls: ['./glmapping-new.component.scss']
})
export class GlmappingNewComponent implements OnInit {
  screenid: 1139 | undefined;

  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({

    languageId: [,],
    itemNumber: [],
    itemDescription: [, [Validators.required]],
    glAccount: [, [Validators.required]],
    glDescription: [, [Validators.required]],
    glAccountType: [, [Validators.required]],
    status: ["1", [Validators.required]],



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
    public dialogRef: MatDialogRef<GlmappingNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: GlMappingService,
    public toastr: ToastrService, private coaService: CoaService,
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
    this.form.controls.glDescription.disable();
    this.form.controls.glAccountType.disable();
    this.auth.isuserdata();
    this.dropdownlist();
    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
      this.form.controls.itemNumber.disable();


    }


    this.form.controls.glAccount.valueChanges.subscribe(x => {
      if (x)
        this.fillgl(x);
    }
    )
  }
  fillgl(x: any) {

    this.spin.show();
    this.sub.add(this.coaService.Get(x).subscribe(res => {

      this.form.controls.glDescription.patchValue(res.accountDescription);
      this.form.controls.glAccountType.patchValue(res.accountType);
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  languageIdList: any[] = [];
  coaList: any[] = [];
  statusIdList: dropdownelement[] = [{ key: "1", value: 'Active' }, { key: "0", value: 'Inactive' }];


  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.languageId.url,
    this.cas.dropdownlist.setup.coa.url,
    ]).subscribe((results) => {
      this.languageIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.languageId.key);
      this.coaList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.coa.key);
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code.itemNumber, this.data.code.languageId).subscribe(res => {
      this.form.patchValue(res, { emitEvent: false });
      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.form.controls.status.patchValue(res.status.toString());
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
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code.itemNumber, this.data.code.itemNumber).subscribe(res => {
        this.toastr.success(this.data.code.itemNumber + " updated successfully!","Notification",{
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
        this.toastr.success(res.itemNumber + " saved successfully!", "Notification", {
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