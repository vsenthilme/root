
import { Component, OnInit, Inject, OnDestroy, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DialogExampleComponent, DialogData } from "src/app/common-field/dialog-example/dialog-example.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { CaseCategoryElement } from "../../../idmaster/language/language.service";
import { CliententityassignementService } from "../cliententityassignement.service";
import { SetupServiceService } from "src/app/common-service/setup-service.service";

@Component({
  selector: 'app-cliententityassignment-new',
  templateUrl: './cliententityassignment-new.component.html',
  styleUrls: ['./cliententityassignment-new.component.scss']
})
export class CliententityassignmentNewComponent implements OnInit {
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
    companyId: [, [Validators.required]],
    createdBy: [],
    createdOn: [],
    deletionIndicator: [],
    languageId: [, [Validators.required]],
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
    storeId: [, [Validators.required]],
    storeName: [],
    updatedBy: [],
    updatedOn: [],
    validityDateFrom: [],
    startCreatedOn:[],
    endCreatedOn:[],
    validityDateTo: [],
    versionNumber: [],
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
    public dialogRef: MatDialogRef<DialogExampleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: CliententityassignementService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private setupService:SetupServiceService,
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
      this.form.controls.languageId.disable();
      this.form.controls.companyId.disable();
      this.form.controls.clientId.disable();
      this.form.controls.storeId.disable();
     
    }
  }


  caseCategoryIdList: any[] = [];
  classIdList: any[] = [];
  languageIdList: any[] = [];
  countryList:any[]=[];
  controltypeList: any[]=[];
  dropdownSelectclientID:any[]=[];
  companyList: any[]=[];
  storeList:any[]=[];
  dropdownSelectcompanyID: any[] = [];
  dropdownSelectLanguageID: any[] = [];
  dropdownSelectcountryID: any[]=[];
  dropdownSelectgroupID:any[]=[];
  dropdownSelectstoreID:any[]=[];
  groupList:any[]=[];
  clientList:any[]=[];
  dropdownlist(){
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.cgsetup.language.url,
      this.cas.dropdownlist.cgsetup.company.url,
      this.cas.dropdownlist.cgsetup.controlgrouptype.url,
      this.cas.dropdownlist.cgsetup.client.url,
      this.cas.dropdownlist.cgsetup.store.url,
    ]).subscribe((results) => {
    this.languageIdList = this.cas.foreachlist(results[0],this.cas.dropdownlist.cgsetup.language.key);
   this.languageIdList.forEach((x: { key: string; value: string; }) => this.dropdownSelectLanguageID.push({value: x.key, label:  x.value}));
   this.dropdownSelectLanguageID = this.cas.removeDuplicatesFromArrayNew(this.dropdownSelectLanguageID);
   this.setupService.searchCompany({languageId: [this.form.controls.languageId.value]}).subscribe(res => {
    this.dropdownSelectcompanyID = [];
    res.forEach(element => {
      this.dropdownSelectcompanyID.push({value: element.companyId, label: element.companyId + '-' + element.description});
    });
  });
  this.setupService.searchClient({languageId: [this.form.controls.languageId.value],companyId:[this.form.controls.companyId.value]}).subscribe(res => {
    this.dropdownSelectclientID = [];
    res.forEach(element => {
      this.dropdownSelectclientID.push({value: element.clientId, label: element.clientId + '-' + element.clientName});
    });
  });
 
 
    this.setupService.searchStore({languageId: [this.form.controls.languageId.value],companyId:[this.form.controls.companyId.value]}).subscribe(res => {
      this.dropdownSelectstoreID = [];
      res.forEach(element => {
        this.dropdownSelectstoreID.push({value: element.storeId, label: element.storeId + '-' + element.storeName});
      });
    });
    this.spin.hide();
    console.log(this.dropdownSelectLanguageID);
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }

   fill() {
     this.spin.show();
    this.sub.add(this.service.Get(this.data.code,this.data.languageId,this.data.companyId,this.data.storeId,this.data.versionNumber).subscribe(res => {
     this.form.patchValue(res, { emitEvent: false });
      this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
      this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
       this.spin.hide();
      console.log(this.data.companyId);
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
       this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code,this.data.languageId,this.data.companyId,this.data.storeId,this.data.versionNumber).subscribe(res => {
         this.toastr.success(this.data.code + " updated successfully!","Notification",{
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
         this.toastr.success(res.clientId + " saved successfully!", "Notification", {
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

  
   dropdownSelectcontroltypeID:any[]=[];
;  onLanguageChange(value){
    this.setupService.searchCompany({languageId: [value.value]}).subscribe(res => {
      this.dropdownSelectcompanyID = [];
      res.forEach(element => {
        this.dropdownSelectcompanyID.push({value: element.companyId, label: element.companyId + '-' + element.description});
      });
    });
    this.setupService.searchClient({languageId: [value.value],companyId:[this.form.controls.companyId.value]}).subscribe(res => {
      this.dropdownSelectclientID = [];
      res.forEach(element => {
        this.dropdownSelectclientID.push({value: element.clientId, label: element.clientId + '-' + element.clientName});
      });
    });
   
      this.setupService.searchStore({languageId: [value.value],companyId:[this.form.controls.companyId.value]}).subscribe(res => {
        this.dropdownSelectstoreID = [];
        res.forEach(element => {
          this.dropdownSelectstoreID.push({value: element.storeId, label: element.storeId + '-' + element.storeName});
        });
      });
  }
  onCompanyChange(value){
    this.setupService.searchClient({languageId: [this.form.controls.languageId.value],companyId:[value.value]}).subscribe(res => {
      this.dropdownSelectclientID = [];
      res.forEach(element => {
        this.dropdownSelectclientID.push({value: element.clientId, label: element.clientId + '-' + element.clientName});
      });
    });
    this.setupService.searchStore({languageId: [this.form.controls.languageId.value],companyId:[value.value]}).subscribe(res => {
      this.dropdownSelectstoreID = [];
      res.forEach(element => {
        this.dropdownSelectstoreID.push({value: element.storeId, label: element.storeId + '-' + element.storeName});
      });
    });
  }


  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
}





