import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DialogExampleComponent } from 'src/app/common-field/dialog-example/dialog-example.component';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { SetupServiceService } from 'src/app/common-service/setup-service.service';
import { AuthService } from 'src/app/core/core';
import { ClientService } from '../../../master/client/client.service';
import { CaseCategoryElement } from '../../language/language.service';
import { EntityService } from '../entity.service';

@Component({
  selector: 'app-entity-new',
  templateUrl: './entity-new.component.html',
  styleUrls: ['./entity-new.component.scss']
})
export class EntityNewComponent implements OnInit {
  screenid: 1022 | undefined;
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
    clientId: [],
    clientName: [],
    companyId: ["1000"],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    entityId: [],
    companyIdAndDescription: ["Monty & Ramirez LLP"],
    entityName: [],
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
    statusId: [0,],
    updatedBy: [],
    updatedOn: [],
  });


  clientForm = this.fb.group({
    clientId: [],
    clientName: [, [Validators.required]],
    companyId: ["1000"],
    companyIdAndDescription: [],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
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
    statusId: ["0",],
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
    private service: EntityService,
    private clientService: ClientService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private setupService: SetupServiceService,
    private cs: CommonService,
  ) { }
  ngOnInit(): void {
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.languageId.disable();
    this.form.controls.companyId.disable();
    this.auth.isuserdata();
    this.dropdownlist();
    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
      this.form.controls.languageId.disable();
      this.form.controls.companyId.disable();
      this.form.controls.clientId.disable();
    }
  }
  caseCategoryIdList: any[] = [];
  classIdList: any[] = [];
  languageIdList: any[] = [];
  companyList: any[] = [];
  dropdownSelectcompanyID: any[] = [];
  dropdownSelectLanguageID: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.cgsetup.language.url,
      this.cas.dropdownlist.cgsetup.company.url,
    ]).subscribe((results) => {
      this.languageIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.cgsetup.language.key);
      this.languageIdList.forEach((x: {
        key: string; value: string;
      }) => this.dropdownSelectLanguageID.push({
        value: x.key,
        label: x.value
      }));

      this.setupService.searchCompany({
        languageId: [this.form.controls.languageId.value]
      }).subscribe(res => {
        this.dropdownSelectcompanyID = [];
        res.forEach(element => {
          this.dropdownSelectcompanyID.push({
            value: element.companyId,
            label: element.companyId + '-' + element.description
          });
        });
      });
      this.setupService.searchClient({
        languageId: [this.auth.languageId],
        companyId: ["1000"]
      }).subscribe(res => {
        this.dropdownSelectclientID = [];
        res.forEach(element => {
          this.dropdownSelectclientID.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName
          });
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
  dropdownSelectclientID: any[] = [];


  multilanguageList: any[] = [];
  multicompanyList: any[] = [];
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code, this.data.languageId, this.data.companyId, this.data.clientId).subscribe(res => {
      this.form.patchValue(res, {
        emitEvent: false
      });
      this.form.controls.statusId.patchValue(res.statusId != null ? res.statusId.toString() : '');
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
    this.form.patchValue({
      updatedby: this.auth.username
    });

    if (this.data.code) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code, this.data.languageId, this.data.companyId, this.data.clientId).subscribe(res => {
        this.toastr.success(this.data.code + " Entity Id updated successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,

        });
        this.spin.hide();
        this.dialogRef.close();

      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    } else {

      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.clientForm.controls.clientName.patchValue(this.form.controls.entityName.value);
        this.clientService.Create(this.clientForm.getRawValue()).subscribe(clientcreatedRes => {
          this.form.patchValue(res, {
            emitEvent: false
          });
          this.form.controls.referenceField1.patchValue(clientcreatedRes.clientId);
          this.service.Update(this.form.getRawValue(), res.entityId, res.languageId, res.companyId, res.clientId).subscribe(clientcreatedRes => {
        
            this.toastr.success(res.clientId + " Entity Id saved successfully!", "Notification", {
              timeOut: 2000,
              progressBar: false,
            });

          }, err => {
            this.cs.commonerror(err);
            this.spin.hide();

          });

        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();

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
  // openDialog(): void {
  //   if (this.data.pageflow == "Edit") {
  //     const dialogRef = this.dialog.open(ClientpopupComponent, {
  //       disableClose: true,
  //       width: '50%',
  //       maxWidth: '80%',
  //       position: {
  //         top: '6.5%'
  //       },

  //     });
  //     dialogRef.afterClosed().subscribe(result => {
  //       if (result == "Yes") {
  //         this.submit();
  //       } else {
  //         this.toastr.error("No Changes Made!", 'Notification', {
  //           timeOut: 2000,
  //           progressBar: false,
  //         });
  //       }
  //     });

  //   } else {
  //     this.submit();
  //   }
  // }
}

