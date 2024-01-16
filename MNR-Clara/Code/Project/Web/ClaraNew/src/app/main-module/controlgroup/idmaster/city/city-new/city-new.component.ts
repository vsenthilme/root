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
  MAT_DIALOG_DATA
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
} from "../../language/language.service"
import {
  CityService
} from "../city.service";
import {
  SetupServiceService
} from "src/app/common-service/setup-service.service";

@Component({
  selector: 'app-city-new',
  templateUrl: './city-new.component.html',
  styleUrls: ['./city-new.component.scss']
})
export class CityNewComponent implements OnInit {
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
    cityId: [, [Validators.required]],
    cityName: [],
    companyId: ["1000", [Validators.required]],
    countryId: [, [Validators.required]],
    countryIdAndDescription: [],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    languageId: [this.auth.languageId, [Validators.required]],
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
    stateId: [, [Validators.required]],
    stateIdAndDescription: [],
    createdOn_to: [],
    createdOn_from: [],
    updatedBy: [],
    updatedOn: [],
    zipCode: [],
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
  constructor(
    public dialogRef: MatDialogRef < DialogExampleComponent > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: CityService,
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
      this.form.controls.languageId.disable();
      this.form.controls.companyId.disable();
      this.form.controls.countryId.disable();
      this.form.controls.stateId.disable();
      this.form.controls.cityId.disable();
      if (this.data.pageflow == 'Display')
        this.form.disable();
      this.fill();
    }
  }
  countryList: any[] = [];
  languageIdList: any[] = [];
  dropdownSelectstateID: any[] = [];
  companyList: any[] = [];
  stateList: any[] = [];
  dropdownSelectcompanyID: any[] = [];
  dropdownSelectLanguageID: any[] = [];
  dropdownSelectcountryID: any[] = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.cgsetup.language.url,
    //  this.cas.dropdownlist.cgsetup.company.url,
    //  this.cas.dropdownlist.cgsetup.country.url,
    //  this.cas.dropdownlist.cgsetup.state.url,
    ]).subscribe((results) => {
      this.languageIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.cgsetup.language.key);
      this.languageIdList.forEach((x: {
        key: string;value: string;
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
        this.setupService.searchCountry({
         languageId: [this.auth.languageId],
        companyId: ["1000"]
        }).subscribe(res => {
          this.dropdownSelectcountryID = [];
          res.forEach(element => {
            this.dropdownSelectcountryID.push({
              value: element.countryId,
          label: element.countryId + '-' + element.countryName
          });
          });
        });
        this.setupService.searchState({
          languageId: [this.form.controls.languageId.value],
         companyId: [this.form.controls.companyId.value],
          countryId: [this.form.controls.countryId.value]
        }).subscribe(res => {
          this.dropdownSelectstateID = [];
          res.forEach(element => {
            this.dropdownSelectstateID.push({
             value: element.stateId,
           label: element.stateId + '-' + element.stateName
          });
         });
        });
        this.dropdownSelectstateID = this.cs.removeDuplicateInArray(this.dropdownSelectstateID);
        this.form.controls.languageId.patchValue(this.auth.languageId);
        this.form.controls.companyId.patchValue("1000");
      this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
 
 
  onCountryChange(value) {
    this.setupService.searchState({
      languageId: [this.auth.languageId],
      companyId: ["1000"],
      countryId: [value.value]
    }).subscribe(res => {
      this.dropdownSelectstateID = [];
      res.forEach(element => {
        this.dropdownSelectstateID.push({
          value: element.stateId,
          label: element.stateId + '-' + element.stateName
        });
      });
    });
  }
  multilanguageList: any[] = [];
  multicompanyList: any[] = [];
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code, this.data.languageId, this.data.companyId, this.data.countryId, this.data.stateId).subscribe(res => {
      this.form.patchValue(res, {
        emitEvent: false
      });
      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
      this.spin.hide();
      this.dropdownlist();
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
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code, this.data.languageId, this.data.companyId, this.data.countryId, this.data.stateId).subscribe(res => {
        this.toastr.success(this.data.code + " City Id updated successfully!", "Notification", {
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
        this.toastr.success(res.cityId + " City Id saved successfully!", "Notification", {
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
