import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { ClientGeneralService } from 'src/app/main-module/client/client-general/client-general.service';
import { GeneralMatterService } from '../../General/general-matter.service';
import { MatterRateService } from '../../rate-list/matter-rate.service';
import { MatterIntakeService } from '../matter-intake.service';

@Component({
  selector: 'app-intake-selection',
  templateUrl: './intake-selection.component.html',
  styleUrls: ['./intake-selection.component.scss']
})
export class IntakeSelectionComponent implements OnInit {
  screenid: 1120 | undefined;



  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);


  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  clientname: any;
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }
  disabled = false;
  step = 0;
  form = this.fb.group({
    approvedOn: [],
    classId: [],
    clientId: [],
    clientUserId: [],
    createdBy: [this.auth.userID],
    createdOn: [],
    deletionIndicator: [0],
    intakeFormId: [, Validators.required],
    intakeFormNumber: [],
    languageId: [],
    matterNumber: [,],
    notesNumber: [],
    receivedOn: [],
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
    resentOn: [],
    sentOn: [],
    statusId: [],
    updatedBy: [this.auth.userID],
    updatedOn: [],
    firstNameLastName: [],

    caseCategoryId: [],
    caseSubCategoryId: [],

  });



  constructor(
    public dialogRef: MatDialogRef<IntakeSelectionComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: MatterIntakeService, private matterService: GeneralMatterService,
    public toastr: ToastrService,
    private cas: CommonApiService, private clientService: ClientGeneralService,
    private spin: NgxSpinnerService, private router: Router,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService,
  ) { }
  matter: any;
  ngOnInit(): void {

    this.auth.isuserdata();
    if (this.data.matter) {
      // this.matter = ' Matter - (' + this.data.matter + ') - ';
      this.matter = '' + this.data.matter + ' / ' + this.data.matterdesc + ' - ';
      this.form.controls.matterNumber.patchValue(this.data.matter);
      // this.form.controls.matterNumber.disable();

    }
    this.dropdownlist();


  }

  intakeFormIdList: any[] = [];
  caseCategoryIdList: any[] = [];
  caseSubCategoryIdList: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.intakeFormId.url,
    this.cas.dropdownlist.setup.caseCategoryId.url,
    this.cas.dropdownlist.setup.caseSubcategoryId.url,
    ]).subscribe((results) => {
      this.intakeFormIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.intakeFormId.key, { clientTypeId: 2 });
      this.caseCategoryIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.caseCategoryId.key,);
      this.caseSubCategoryIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.caseSubcategoryId.key,);

      this.getMatterDetails();
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
    this.form.controls.languageId.patchValue("EN");
  }

  getMatterDetails() {
    this.spin.show();
    this.sub.add(this.matterService.Get(this.form.controls.matterNumber.value).subscribe(res => {

      this.form.patchValue(res, { emitEvent: false });
      this.spin.hide();
      this.form.controls.caseCategoryId.patchValue(this.caseCategoryIdList.find(y => y.key == res.caseCategoryId)?.value);
      this.form.controls.caseSubCategoryId.patchValue(this.caseSubCategoryIdList.find(y => y.key == res.caseSubCategoryId)?.value);
      this.form.controls.caseCategoryId.disable();
      this.form.controls.caseSubCategoryId.disable();
      this.spin.show();
      this.sub.add(this.clientService.Get(this.form.controls.clientId.value).subscribe(res => {
        this.clientname = res.clientId + '-' + res.firstNameLastName
      console.log(this.clientname)
        this.form.controls.firstNameLastName.patchValue(this.clientname);
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
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



    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.spin.show();
    this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {

      this.toastr.success(res.intakeFormNumber + " saved successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      // this.form.patchValue(res, { emitEvent: false });
      this.spin.hide();
      this.dialogRef.close();

      let formname = this.cs.customerformname(this.form.controls.intakeFormId.value);
      if (formname == '') {
        this.toastr.error(
          "Select from is invalid.",
          ""
        );

        return;
      }


      this.router.navigate(['/main/matters/case-management/' + formname + '/' + this.cs.encrypt({ intakeFormNumber: res.intakeFormNumber, pageflow: 'update' })]);





    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();

    }));

  }


  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
}
