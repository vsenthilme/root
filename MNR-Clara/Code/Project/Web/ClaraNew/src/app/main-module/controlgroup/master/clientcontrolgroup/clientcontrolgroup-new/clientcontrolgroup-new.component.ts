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
} from "../../../idmaster/language/language.service";
import {
  ClientcontrolgroupService
} from "../clientcontrolgroup.service";
import {
  SetupServiceService
} from "src/app/common-service/setup-service.service";
import {
  ConfirmComponent
} from "src/app/common-field/dialog_modules/confirm/confirm.component";
@Component({
  selector: 'app-clientcontrolgroup-new',
  templateUrl: './clientcontrolgroup-new.component.html',
  styleUrls: ['./clientcontrolgroup-new.component.scss']
})
export class ClientcontrolgroupNewComponent implements OnInit {

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
    clientId: [, [Validators.required]],
    clientName: [],
    companyId: ["1000"],
    companyIdAndDescription: [],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    groupTypeId: [, [Validators.required]],
    groupTypeName: [],
    languageId: [this.auth.languageId],
    relationshipDescription: [],
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
    relationship: [, [Validators.required]],
    statusId: [],
    subGroupTypeId: [, [Validators.required]],
    subGroupTypeName: [],
    updatedBy: [],
    updatedOn: [],
  });

  submitted = false;
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

  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  

  formgr: CaseCategoryElement | undefined;
  panelOpenState = false;
  constructor(public dialog: MatDialog,
    public dialogRef: MatDialogRef < DialogExampleComponent > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: ClientcontrolgroupService,
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
      this.form.controls.languageId.patchValue(this.data.languageId);
      this.form.controls.companyId.disable();
      this.form.controls.clientId.disable();
      this.form.controls.groupTypeId.disable();
      this.form.controls.subGroupTypeId.disable();
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
  groupList: any[] = [];
  clientList: any[] = [];
  dropdownRelationSelection: any[] = [];
  relationSelection: any;

  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.cgsetup.language.url,
      //this.cas.dropdownlist.cgsetup.relationshipid.url,
    ]).subscribe((results) => {

      this.languageIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.cgsetup.language.key);
      this.languageIdList.forEach((x: {
        key: string;value: string;
      }) => this.dropdownSelectLanguageID.push({
        value: x.key,
        label: x.value
      }));
      this.dropdownSelectLanguageID = this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectLanguageID);

      this.setupService.searchCompany({
        languageId: [this.auth.languageId]
      }).subscribe(res => {
        this.dropdownSelectcompanyID = [];
        res.forEach(element => {
          this.dropdownSelectcompanyID.push({
            value: element.companyId,
            label: element.companyId + '-' + element.description
          });
        });
      });
      this.setupService.searchrelationship({
        languageId: [this.auth.languageId],
        companyId: ["1000"],
        statusId: [0]
      }).subscribe(res => {
        this.dropdownRelationSelection = [];
        res.forEach(element => {
          this.dropdownRelationSelection.push({
            value: element.relationShipId,
            label: element.relationShipId + '-' + element.description,
            description: element.description
          });
          if(this.data.pageflow !== 'New'){
            this.form.controls.relationship.patchValue(this.data.relationship);
          }
        });
      });
      this.dropdownRelationSelection = this.cas.removeDuplicatesFromArrayNew(this.dropdownRelationSelection);
      this.setupService.searchControlType({
        languageId: [this.auth.languageId],
        companyId: ["1000"],
        statusId: [0]
      }).subscribe(res => {
        this.dropdownSelectcontroltypeID = [];
        res.forEach(element => {
          this.dropdownSelectcontroltypeID.push({
            value: element.groupTypeId,
            label: element.groupTypeId + '-' + element.groupTypeName
          });
        });
      });

      this.setupService.searchClient({
        languageId: [this.auth.languageId],
        companyId: ["1000"],
        statusId: [0]
      }).subscribe(res => {
        this.dropdownSelectclientID = [];
        res.forEach(element => {
          this.dropdownSelectclientID.push({
            value: element.clientId,
            label: element.clientId + '-' + element.clientName
          });
        });
      });

      this.setupService.searchSubGroupType({
        languageId: [this.auth.languageId],
        companyId: ["1000"],
        groupTypeId: [this.form.controls.groupTypeId.value],
        statusId: [0]
      }).subscribe(res => {
        this.dropdownSelectgroupID = [];
        res.forEach(element => {
          this.dropdownSelectgroupID.push({
            value: element.subGroupTypeId,
            label: element.subGroupTypeId + '-' + element.subGroupTypeName
          });
        });
      });
this.form.controls.languageId.patchValue(this.auth.languageId);
this.form.controls.companyId.patchValue("1000");
     
      this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
  dropdownSelectgroupID: any[] = [];
  dropdownSelectclientID: any[] = [];

 

  onCompanyChange(value) {
    this.setupService.searchControlType({
      languageId: [this.form.controls.languageId.value],
      companyId: [value.value],
      statusId: [0]
    }).subscribe(res => {

      this.dropdownSelectcontroltypeID = [];
      res.forEach(element => {
        this.dropdownSelectcontroltypeID.push({
          value: element.groupTypeId,
          label: element.groupTypeId + '-' + element.groupTypeName
        });
      }); 
      this.setupService.searchrelationship({
        languageId: [this.form.controls.languageId.value],
        companyId: [value.value],
        statusId: [0]
      }).subscribe(res => {
        this.dropdownRelationSelection = [];
        res.forEach(element => {
          this.dropdownRelationSelection.push({
            value: element.relationShipId,
            label: element.relationShipId + '-' + element.description,
            description: element.description
          });
        });
        if(this.data.pageflow !== 'New'){
          this.form.controls.relationship.patchValue(this.data.relationship);
        }
      });
    });
    // this.setupService.searchrelationship({
    //   languageId: [this.form.controls.languageId.value],
    //   companyId: [value.value],
    //   statusId: [0]
    // }).subscribe(res => {

    //   this.dropdownRelationSelection = [];
    //   res.forEach(element => {
    //     this.dropdownRelationSelection.push({
    //       value: element.relationshipId,
    //       label: element.relationship + '-' + element.relationship,
    //       relationship:element.relationship
    //     });
    //   }); 

    //});
    this.setupService.searchClient({
      languageId: [this.form.controls.languageId.value],
      companyId: [value.value],
      statusId: [0]
    }).subscribe(res => {

      this.dropdownSelectclientID = [];
      res.forEach(element => {
        this.dropdownSelectclientID.push({
          value: element.clientId,
          label: element.clientId + '-' + element.clientName
        });
      });
    });

    this.setupService.searchSubGroupType({
      languageId: [this.form.controls.languageId.value],
      companyId: [value.value],
      groupTypeId: [this.form.controls.groupTypeId.value],
      statusId: [0]
    }).subscribe(res => {
      this.dropdownSelectgroupID = [];
      res.forEach(element => {
        this.dropdownSelectgroupID.push({
          value: element.subGroupTypeId,
          label: element.subGroupTypeId + '-' + element.subGroupTypeName
        });
      });
    });
  }
  onGroupChange(value) {
    this.setupService.searchSubGroupType({
      languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      groupTypeId: [value.value],
      statusId: [0]
    }).subscribe(res => {
      this.dropdownSelectgroupID = [];
      res.forEach(element => {
        this.dropdownSelectgroupID.push({
          value: element.subGroupTypeId,
          label: element.subGroupTypeId + '-' + element.subGroupTypeName
        });
      });
    });

    this.setupService.searchControlType({
      companyId: [this.form.controls.companyId.value],
      languageId: [this.form.controls.languageId.value],
      groupTypeId: [value.value],
      statusId: [0]
    }).subscribe(res => {
      this.form.controls.groupTypeName.patchValue(res[0].groupTypeName);

    });
  }

  onGroupIdChange(value) {
    this.setupService.searchSubGroupType({
      languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      groupTypeId: [this.form.controls.groupTypeId.value],
      groupId: [value.value],
      statusId: [0]
    }).subscribe(res => {
      this.form.controls.groupTypeName.patchValue(res[0].groupTypeName);
    })
  }

  onClientChange(value) {
    this.setupService.searchClient({
      languageId: [this.form.controls.languageId.value],
      companyId: [this.form.controls.companyId.value],
      clientId: [value.value],
      statusId: [0]
    }).subscribe(res => {
      this.form.controls.clientName.patchValue(res[0].clientName);

    })
  }

  onrelationshipChange(value) {
   this.form.controls.relationship.patchValue(value.value);
   this.form.controls.relationshipDescription.patchValue(value.description);
  }
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code, this.data.languageId, this.data.companyId, this.data.groupTypeId, this.data.subGroupTypeId, this.data.versionNumber).subscribe(res => {
      this.form.patchValue(res, {
        emitEvent: false
      });
      console.log(res);
      this.data.relationship=res.relationship;
      console.log(this.data.relationship);
      this.form.controls.relationship.patchValue(res.relationship);
      this.form.controls.statusId.patchValue(res.statusId != null ? res.statusId.toString() : '');
      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.dropdownlist();
      console.log(this.data.languageId);

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
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code, this.data.languageId, this.data.companyId, this.data.groupTypeId, this.data.subGroupTypeId, this.data.versionNumber).subscribe(res => {
        this.toastr.success(this.data.code + "Co-OwnerId Mapping Updated successfully!", "Notification", {
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
      let obj: any = {}
      obj.groupTypeId = [this.form.controls.groupTypeId.value];
      obj.subGroupTypeId = [this.form.controls.subGroupTypeId.value];
      obj.clientId = [this.form.controls.clientId.value]
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
              title: "Update",
              message: "This Entry already Exist!,Do you wish to Continue?"
            }
          });
          this.spin.hide();
          dialogRef1.afterClosed().subscribe(result => {
            if (result == "Yes") {
              console.log(result);
              this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
                this.toastr.success(res.clientId + " Co-OwnerId Mapped Successfully!", "Notification", {
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
            this.toastr.success(res.clientId + " Co-Owner Id Mapped Successfully!", "Notification", {
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
}
