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
} from "../../language/language.service";
import {
  StoreService
} from "../store.service";
import {
  SetupServiceService
} from "src/app/common-service/setup-service.service";
@Component({
  selector: 'app-store-new',
  templateUrl: './store-new.component.html',
  styleUrls: ['./store-new.component.scss']
})
export class StoreNewComponent implements OnInit {
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
    address: [],
    city: [,[Validators.required]],
    cityIdAndDescription: [],
    companyId: ["1000"],
    companyIdAndDescription: [],
    country: [,[Validators.required]],
    countryIdAndDescription: [],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    languageId: [this.auth.languageId],
    phoneNo: [],
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
    groupTypeId:[,[Validators.required]],
    groupTypeName:[],
    subGroupTypeName:[],
    subGroupTypeId:[,[Validators.required]],
    state: [,[Validators.required]],
    stateIdAndDescription: [],
    status: [0,],
    storeId: [],
    storeName: [, [Validators.required]],
    createdOn_to: [],
    createdOn_from: [],
    updatedBy: [],
   updatedOn: [],
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
    private service: StoreService,
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
    this.form.controls.state.disable();
    this.form.controls.country.disable();
    if (this.data.pageflow1 == 'TransactionCreate') {
      this.form.controls.languageId.patchValue(this.data.element.languageId);
      this.form.controls.companyId.patchValue(this.data.element.companyId);
     
    }
    this.auth.isuserdata();
    this.dropdownlist();
    if (this.data.pageflow != 'New') {
      this.form.controls.languageId.disable();
      this.form.controls.companyId.disable();
      this.form.controls.country.disable();
      this.form.controls.state.disable();
      this.form.controls.city.disable();
      if (this.data.pageflow != 'New' && this.data.pageflow1 != 'TransactionCreate') {
      if (this.data.pageflow == 'Display')
        //this.form.controls.dropdownSelectstateID.patchValue(this.form.controls.state.value);
      this.form.disable();
      this.fill();
    }
    }
  }
  countryList: any[] = [];
  cityList: any[] = [];
  languageIdList: any[] = [];
  dropdownSelectstateID: any[] = [];
  companyList: any[] = [];
  stateList: any[] = [];
  dropdownSelectcompanyID: any[] = [];
  dropdownSelectLanguageID: any[] = [];
  dropdownSelectcountryID: any[] = [];
  dropdownSelectcityID: any[] = [];
  dropdownSelectsubgroupID:any[]=[];
  dropdownSelectgroupTypeID:any[]=[];
  groupList:any[]=[];
  subgroupList:any[]=[];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.cgsetup.language.url,
      this.cas.dropdownlist.cgsetup.company.url,
      this.cas.dropdownlist.cgsetup.country.url,
      this.cas.dropdownlist.cgsetup.state.url,
      this.cas.dropdownlist.cgsetup.city.url,
      this.cas.dropdownlist.cgsetup.controlgrouptype.url,
      this.cas.dropdownlist.cgsetup.subgrouptype.url,
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
      this.countryList = this.cas.foreachlist(results[2], this.cas.dropdownlist.cgsetup.country.key);
      this.countryList.forEach((x: {
        key: string;value: string;
      }) => this.dropdownSelectcountryID.push({
        value: x.key,
        label: x.value
      }))
      this.dropdownSelectcountryID = this.cs.removeDuplicateInArray(this.dropdownSelectcountryID);
      this.stateList = this.cas.foreachlist(results[3], this.cas.dropdownlist.cgsetup.state.key);
      this.stateList.forEach((x: {
        key: string;value: string;
      }) => this.dropdownSelectstateID.push({
        value: x.key,
        label: x.value
      }))
      this.cityList = this.cas.foreachlist(results[4], this.cas.dropdownlist.cgsetup.city.key);
      this.cityList.forEach((x: {
        key: string;value: string;
      }) => this.dropdownSelectcityID.push({
        value: x.key,
        label: x.value
      }))
      this.groupList = this.cas.foreachlist(results[5], this.cas.dropdownlist.cgsetup.controlgrouptype.key);
      this.groupList.forEach((x: {
        key: string;value: string;
      }) => this.dropdownSelectgroupTypeID.push({
        value: x.key,
        label: x.value,
        description:x.value,
      }))
      this.subgroupList = this.cas.foreachlist(results[4], this.cas.dropdownlist.cgsetup.subgrouptype.key);
      this.subgroupList.forEach((x: {
        key: string;value: string;
      }) => this.dropdownSelectsubgroupID.push({
        value: x.key,
        label: x.value
      }))
      if (this.data.pageflow1 == 'TransactionCreate') {
        this.form.controls.languageId.patchValue(this.data.element.languageId);
        this.form.controls.companyId.patchValue(this.data.element.companyId);
       // this.form.controls.groupTypeId.patchValue(this.data.element.groupTypeId);
      }
      this.dropdownSelectcityID = this.cs.removeDuplicateInArray(this.dropdownSelectcityID);
      this.form.controls.languageId.patchValue(this.auth.languageId);
      this.form.controls.companyId.patchValue("1000");
      this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
 
  onCompanyChange(value) {
    this.setupService.searchCity({
      languageId: [this.auth.languageId],
      companyId: ["1000"]
    }).subscribe(res => {
      this.dropdownSelectcityID = [];
      res.forEach(element => {
        this.dropdownSelectcityID.push({
          value: element.cityId,
          label: element.cityId + '-' + element.cityName
        });
      });
    });
  }
  ongroupTypeChange(value) {
    console.log(value);
    this.setupService.searchSubGroupType({
      languageId: [this.auth.languageId],
      companyId: ["1000"],
      groupTypeId: [value.value]
    }).subscribe(res => {
      this.dropdownSelectsubgroupID = [];
      res.forEach(element => {
        this.dropdownSelectsubgroupID.push({
          value: element.subGroupTypeId,
          label: element.subGroupTypeId + '-' + element.subGroupTypeName,
          description:element.subGroupTypeName
        });
      });
   this.form.controls.groupTypeName.patchValue(value.description);
    });
  }
  onsubgroupTypeChange(value) {
  
   this.form.controls.subGroupTypeName.patchValue(value.description);
 
  }

  onCityChange(value) {
    this.setupService.searchCity({
      languageId: [this.auth.languageId],
      companyId: ["1000"],
      cityId: [value.value]
    }).subscribe(res => {
      this.dropdownSelectstateID = [];
      res.forEach(element => {
        this.dropdownSelectstateID.push({
          value: element.stateId,
          label: element.stateId + '-' + element.stateIdAndDescription
        });
      });
      this.form.controls.state.patchValue(res[0].stateId)
      this.dropdownSelectcountryID = [];
      res.forEach(element => {
        this.dropdownSelectcountryID.push({
          value: element.countryId,
          label: element.countryId + '-' + element.countryIdAndDescription
        });
      });
      this.form.controls.country.patchValue(res[0].countryId);
    });
  }
  multilanguageList: any[] = [];
  multicompanyList: any[] = [];
  fill() {
    this.spin.show();
    this.sub.add(this.service.Get(this.data.code, this.data.languageId, this.data.companyId).subscribe(res => {
      this.form.patchValue(res, {
        emitEvent: false
      });
      this.form.controls.status.patchValue(res.status != null ? res.status.toString() : '');
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
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code, this.data.languageId, this.data.companyId).subscribe(res => {
        this.toastr.success(res.storeId + " Store Id updated successfully!", "Notification", {
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
        this.toastr.success(res.storeId + " Store Id saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        if (this.data.pageflow1 == 'TransactionCreate') {
          this.dialogRef.close(res);
        }
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
