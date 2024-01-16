import {
  Component,
  OnInit,
  Inject,
  OnDestroy,
  ViewChild
} from "@angular/core";
import {
  FormBuilder,
  FormControl,
  Validators
} from "@angular/forms";
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialog
} from "@angular/material/dialog";
import {
  NgxSpinnerService
} from "ngx-spinner";
import {
  ToastrService
} from "ngx-toastr";
import {
  Subscription
} from "rxjs";
import {
  DialogExampleComponent,
  DialogData
} from "src/app/common-field/dialog-example/dialog-example.component";
import {
  CommonApiService
} from "src/app/common-service/common-api.service";
import {
  CommonService
} from "src/app/common-service/common-service.service";
import {
  AuthService
} from "src/app/core/core";
import {
  CaseCategoryElement
} from "../../language/language.service";
import {
  ControlgrouptypeService
} from "../controlgrouptype.service";
import {
  SetupServiceService
} from "src/app/common-service/setup-service.service";
import {
  ConfirmComponent
} from "src/app/common-field/dialog_modules/confirm/confirm.component";

@Component({
  selector: 'app-controlgrouptype-new',
  templateUrl: './controlgrouptype-new.component.html',
  styleUrls: ['./controlgrouptype-new.component.scss']
})
export class ControlgrouptypeNewComponent implements OnInit {
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
    groupTypeId: [],
    groupTypeName: [, [Validators.required]],
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
    statusId: [0, ],
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
    public dialogRef: MatDialogRef < DialogExampleComponent > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: ControlgrouptypeService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private auth: AuthService, private fb: FormBuilder,
    private setupService: SetupServiceService,
    private cs: CommonService,
  ) {}
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
      this.form.controls.groupTypeId.disable();
    }
  }
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
        key: string;value: string;
      }) => this.dropdownSelectLanguageID.push({
        value: x.key,
        label: x.value
      }));
      this.companyList = this.cas.foreachlist(results[1], this.cas.dropdownlist.cgsetup.company.key);
      this.companyList.forEach((x: {
        key: string;value: string;
      }) => this.dropdownSelectcompanyID.push({
        value: x.key,
        label: x.value
      }))
      this.form.controls.languageId.patchValue(this.auth.languageId);
      this.form.controls.companyId.patchValue("1000");
      this.spin.hide();
        }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
 
  multilanguageList: any[] = [];
  multicompanyList: any[] = [];
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code, this.data.languageId, this.data.companyId, this.data.versionNumber).subscribe(res => {

      this.form.patchValue(res, {
        emitEvent: false
      });
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
    this.form.patchValue({
      updatedby: this.auth.username
    });

    if (this.data.code) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code, this.data.languageId, this.data.companyId, this.data.versionNumber).subscribe(res => {
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
    } else {
      let obj: any = {}
      obj.groupTypeId = [this.form.controls.groupTypeId.value];
      obj.statusId = [0];
      obj.companyId = [this.form.controls.companyId.value];
      obj.languageId = [this.form.controls.languageId.value];
      this.spin.show();
      this.sub.add(this.service.search(obj).subscribe(res => {
        if (res.length == 1) {
          const dialogRef1 = this.dialog.open(ConfirmComponent, {
            disableClose: true,
            width: '50%',
            maxWidth: '80%',
            position: {
              top: '6.5%'
            },
            data: {
              title: "Confirm",
              message: "This Entry already Exist!,Do you wish to Continue?"
            }

          });
          this.spin.hide();
          dialogRef1.afterClosed().subscribe(result => {
            if (result) {
              this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
                this.toastr.success(res.groupTypeId + " Updated Successfully!", "Notification", {
                  timeOut: 2000,
                  progressBar: false,
                }, );
                this.spin.hide();
                this.dialogRef.close();

              }, err => {
                this.cs.commonerror(err);
                this.spin.hide();

              }, ));
            } else {
              this.toastr.error("Data Already Exists", 'Notification', {
                timeOut: 2000,
                progressBar: false,
              });
            }
          });
        } else {
          this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
            this.toastr.success(res.groupTypeId + " Saved Successfully!", "Notification", {
              timeOut: 2000,
              progressBar: false,
            }, );
            this.spin.hide();

          }, err => {
            this.cs.commonerror(err);
            this.spin.hide();

          }, ));
          this.dialogRef.close();
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
  // openDialog(): void {
  //   if(this.data.pageflow == "Edit"){
  //     const dialogRef2 = this.dialog.open(ControlgrouptypeEditComponent, {
  //       disableClose: true,
  //       width: '50%',
  //       maxWidth: '80%',
  //       position: {
  //         top: '6.5%'
  //       },

  //     });
  //     dialogRef2.afterClosed().subscribe(result => {
  //       if (result == "Yes") {
  //         console.log(2);
  //         this.submit();
  //       } 
  //       else {
  //         this.toastr.error("No Changes Made!", 'Notification', {
  //           timeOut: 2000,
  //           progressBar: false,
  //         });
  //       }
  //     });
  //   }
  //   else{
  //     this.submit();
  //   }
  //   }
}
