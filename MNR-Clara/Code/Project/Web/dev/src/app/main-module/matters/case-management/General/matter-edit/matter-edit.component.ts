import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { Location } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { GeneralMatterService } from '../general-matter.service';


interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-matter-edit',
  templateUrl: './matter-edit.component.html',
  styleUrls: ['./matter-edit.component.scss']
})
export class MatterEditComponent implements OnInit {
  screenid: 1099 | undefined;
  input: any;
  public icon = 'expand_more';
  todaydate = new Date();

  sub = new Subscription();
  email = new FormControl('', [ Validators.email, Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")]);
  form = this.fb.group({

    administrativeCost: [,],
    approvalDate: [,],
    arAccountNumber: [,],
    billingFormatId: [,],
    billingFrequencyId: [,],
    billingModeId: [,],
    billingRemarks: [,],
    caseCategoryId: [,],
    caseCategoryIdFE: [,  ],
    caseClosedDate: [,],
    caseFileNumber: [,],
    caseFiledDate: [,],
    caseInformationNo: [,],
    caseOpenedDate: [,],
    caseSubCategoryId: [,],
    caseSubCategoryIdFE: [,  ],
    classId: [, Validators.required],
    clientId: [,],
    contigencyFeeAmount: [,],
    courtDate: [,],
    createdBy: [,],
    createdOn: [,],
    deletionIndicator: [,],
    directPhoneNumber: [,[Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    expirationDate: [,],
    fileNumber: [,],
    flatFeeAmount: [,],
    languageId: [,],
    matterDescription: [, Validators.required],
    matterNumber: [,],
    priorityDate: [,],
    rateUnit: [,],
    receiptDate: [,],
    receiptNoticeNo: [,],
    referenceField1: [,],
    referenceField10: [,],
    referenceField11: [,],
    referenceField12: [,],
    referenceField13: [,],
    referenceField14: [,],
    referenceField15: [,],
    referenceField16: [,],
    referenceField17: [,],
    referenceField18: [,],
    referenceField19: [,],
    referenceField2: [,],
    referenceField20: [,],
    referenceField3: [,],
    referenceField4: [,],
    referenceField5: [,],
    referenceField6: [,],
    referenceField7: [,],
    referenceField8: [,],
    referenceField9: [,],
    referenceField29: [,],
    referenceField30: [,],
    referenceField31: [,],
    statusId: [,],
    transactionId: [,],
    trustDepositNo: [,],
    updatedBy: [,],
    updatedOn: [,],

    
    sapprovalDate: [,],
    scaseClosedDate: [,],
    scaseFiledDate: [,],
    scaseOpenedDate: [,],
    scourtDate: [,],
    sexpirationDate: [,],
    spriorityDate: [,],
    sreceiptDate: [,],
  });
  formcliet = this.fb.group({
    addressLine1: [,],
    addressLine2: [,],
    addressLine3: [,],
    alternateEmailId: [,Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")],
    city: [,],
    classId: [, ],
    clientCategoryId: [,],
    clientId: [],
    consultationDate: [,],
    contactNumber: [,[Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    corporationClientId: [,],
    country: [,],
    createdBy: [,],
    createdOn: [,],
    deletionIndicator: [,],
    emailId: [,Validators.pattern("[a-zA-Z0-9.-_]{1,}@[a-zA-Z.-]{2,}[.]{1}[a-zA-Z]{2,}")],
    fax: [,],
    firstName: [,],
    firstNameLastName: [,],
    homeNo: [,[Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    inquiryNumber: [,],
    intakeFormId: [,],
    intakeFormNumber: [,],
    isMailingAddressSame: [false,],
    languageId: [this.auth.languageId,],
    lastName: [,],
    lastNameFirstName: [,],
    mailingAddress: [,],
    occupation: [,],
    potentialClientId: ['999999',],
    referenceField1: [,],
    referenceField10: [,],
    referenceField2: [,],
    referenceField3: [,],
    referenceField4: [,],
    referenceField5: [,],
    referenceField6: [,],
    referenceField7: [,],
    referenceField8: [,],
    referenceField9: [,],
    referralId: [,],
    socialSecurityNo: [,],
    state: [,],
    statusId: [18,],
    suiteDoorNo: [,],
    transactionId: [,],
    updatedBy: [,],
    updatedOn: [,],
    workNo: [,[Validators.minLength(12),Validators.maxLength(12),Validators.pattern('[0-9 -]+$')]],
    zipCode: [,],


  });
  formTimekeeper = this.fb.group({
    partner: [,],
    originatingTimeKeeper: [,],
    responsibleTimeKeeper: [,],
    legalAssistant: [,],
    referenceField1: [],
    referenceField2: [],
    referec: [],
    assignedTimeKeeper:[]

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


  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  toggleFloat() {

    this.isShowDiv = !this.isShowDiv;
    this.toggle = !this.toggle;

    if (this.icon === 'expand_more') {
      this.icon = 'chevron_left';
    } else {
      this.icon = 'expand_more'
    }
    this.showFloatingButtons = !this.showFloatingButtons;

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
  newPage = true;


  isbtntext = true;
  constructor(private fb: FormBuilder,
    private auth: AuthService,
    private service: GeneralMatterService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private location: Location,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,) { }

  ngOnInit(): void {
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.receiptNoticeNo.disable();
    this.form.controls.caseInformationNo.disable();
    //this.form.controls.caseOpenedDate.disable();

    //newly added
    this.form.controls.referenceField9.disable();
    this.form.controls.referenceField4.disable();
    this.form.controls.priorityDate.disable();
    this.form.controls.referenceField2.disable();

    this.form.controls.caseCategoryId.valueChanges.subscribe(x => {

        if (x)
          this.getsubcaseCategoryId(x);
      });

    sessionStorage.setItem('matter', this.route.snapshot.params.code);
    this.auth.isuserdata();

    let code = this.route.snapshot.params.code;
    let js: any = {}
    // let js = this.cs.decrypt(code);
    // this.fill(js);
    this.newPage = false;
    js = this.cs.decrypt(code);
     this.dropdownlist(js);
    this.formcliet.disable();
    this.formTimekeeper.disable();
  }

  getsubcaseCategoryId(code: string) {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.caseSubcategoryId.url,
    ]).subscribe((results:any) => {



      this.caseSubCategoryIdList = results[0];
      console.log(this.caseSubCategoryIdList)
      this.multicasesubList= [];
          this.caseSubCategoryIdList.forEach((element:any) => {
            if(element.caseCategoryId == +code ){
             this.multicasesubList.push({value: element.caseSubcategoryId, label:  element.caseSubcategoryId + ' - ' + element.subCategory})
        
            }
          })
           console.log(this.multicasesubList)
       this.multiselectcasesubList = this.multicasesubList;
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();

  }


  
  clientIdList: any[] = [];
  caseSubCategoryIdList: any[] = [];
  caseCategoryIdList: any[] = [];
  statusIdList: any[] = [];

  selectedItems: SelectItem[] = [];
  multiselectcasesubList: any[] = [];
  multicasesubList: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectcaseList: any[] = [];
  multicaseList: any[] = [];

  dropdownSettings = {
    singleSelection: true,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  dropdownlist(js?:any) {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.client.clientId.url,
    this.cas.dropdownlist.setup.caseCategoryId.url,
    this.cas.dropdownlist.setup.caseSubcategoryId.url,
    this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.clientIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.client.clientId.key);
      this.caseCategoryIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.caseCategoryId.key);
      this.caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicaseList.push({value: x.key, label:  x.value}))
      this.multiselectcaseList = this.multicaseList;

      this.caseSubCategoryIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.caseSubcategoryId.key);
      this.caseSubCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicasesubList.push({value: x.key, label:  x.key+ ' - ' +x.value}))
      this.multiselectcasesubList = this.multicasesubList;

      this.statusIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.statusId.key, { statusId: [26, 27, 28, 29, 30, 36] });
      if(!this.newPage){
        this.fill(js);
      } else {
        this.spin.hide();
      }
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
  btntext = "Save";
  fill(data: any) {

    if (data.pageflow != 'New') {
      this.btntext = 'Update';
      if (data.pageflow == 'Display') {
        this.form.disable();
        this.dropdownSettings.disabled = true;
        this.isbtntext = false;
      }
      this.form.controls.matterNumber.disable();

      this.spin.show();
      this.sub.add(this.service.Get(data.code).subscribe(res => {
        this.form.patchValue(res, { emitEvent: false });
        this.form.controls.caseOpenedDate.patchValue(this.cs.day_callapiSearch(this.form.controls.scaseOpenedDate.value));
        this.form.controls.caseFiledDate.patchValue(this.cs.day_callapiSearch(this.form.controls.scaseFiledDate.value));
        this.form.controls.caseClosedDate.patchValue(this.cs.day_callapiSearch(this.form.controls.scaseClosedDate.value));
        this.form.controls.approvalDate.patchValue(this.cs.day_callapiSearch(this.form.controls.sapprovalDate.value));
        this.form.controls.expirationDate.patchValue(this.cs.day_callapiSearch(this.form.controls.sexpirationDate.value));
        this.form.controls.receiptDate.patchValue(this.cs.day_callapiSearch(this.form.controls.sreceiptDate.value));
        this.form.controls.courtDate.patchValue(this.cs.day_callapiSearch(this.form.controls.scourtDate.value));
        this.form.controls.priorityDate.patchValue(this.cs.day_callapiSearch(this.form.controls.spriorityDate.value));


        this.getsubcaseCategoryId(res.caseCategoryId);
        // this.multicaseList.forEach(element => {
        //   if(element.id == res.caseCategoryId){
        //     this.form.controls.caseCategoryId.patchValue([element]);
        //   }
        // });
        // this.multicasesubList.forEach(element => {
        //   if(element.id == res.caseSubCategoryId){
        
        //     this.form.controls.caseSubCategoryIdFE.patchValue([element]);
        //   }
        // });
        this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
        this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));

        this.sub.add(this.service.GetClient(res.clientId).subscribe(res => {
          this.formcliet.patchValue(res);
          this.formcliet.controls.referenceField2.patchValue(res.firstNameLastName);

          if(res.clientCategoryId == 4){
            this.formcliet.controls.referenceField1.patchValue(res.referenceField2);
          }
          else if(res.clientCategoryId == 3 && res.corporationClientId){
            this.sub.add(this.service.GetClient(res.corporationClientId).subscribe(res => {
              this.formcliet.controls.referenceField1.patchValue(res.firstNameLastName);
            }));
          }
          else{
            this.formcliet.controls.referenceField1.patchValue(null);
          }
          this.spin.hide();
          //this.getsubcaseCategoryId(res.caseCategoryId);
        }, err => {
          // this.cs.commonerror(err);
          this.spin.hide();
        }));

        this.sub.add(this.service.GetTimeKeper(data.code).subscribe(res => {
          this.formTimekeeper.patchValue(res);
          this.spin.hide();
        }, err => {
          // this.cs.commonerror(err);
          this.spin.hide();
        }));
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
  }

  submit() {

    //   this.form.controls['matterDescription'].setValidators([Validators.pattern('[a-zA-Z0-9 \S]+')]);
    //   this.form.controls['matterDescription'].updateValueAndValidity();
  
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
    // if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
    //   this.form.patchValue({caseSubCategoryId: this.selectedItems[0].id });
    // }
    // if (this.selectedItems2 != null && this.selectedItems2 != undefined && this.selectedItems2.length > 0) {
    //   this.form.patchValue({caseCategoryId: this.selectedItems2[0].id });
    // }
    this.form.controls.scaseOpenedDate.patchValue(this.cs.dateNewFormat(this.form.controls.caseOpenedDate.value));
    this.form.controls.scaseFiledDate.patchValue(this.cs.dateNewFormat(this.form.controls.caseFiledDate.value));
    this.form.controls.scaseClosedDate.patchValue(this.cs.dateNewFormat(this.form.controls.caseClosedDate.value));
    this.form.controls.sapprovalDate.patchValue(this.cs.dateNewFormat(this.form.controls.approvalDate.value));
    this.form.controls.sexpirationDate.patchValue(this.cs.dateNewFormat(this.form.controls.expirationDate.value));
    this.form.controls.sreceiptDate.patchValue(this.cs.dateNewFormat(this.form.controls.receiptDate.value));
    this.form.controls.scourtDate.patchValue(this.cs.dateNewFormat(this.form.controls.courtDate.value));
    this.form.controls.spriorityDate.patchValue(this.cs.dateNewFormat(this.form.controls.priorityDate.value));

    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.form.patchValue({ updatedby: this.auth.username });
    if (this.form.controls.matterNumber.value) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.form.controls.matterNumber.value).subscribe(res => {
        this.toastr.success(this.form.controls.matterNumber.value + " updated successfully!","Notification",{
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();

        this.router.navigate(['/main/matters/case-management/accounting/' + this.route.snapshot.params.code]);


      }, err => {

        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
    else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.matterNumber + " saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        //this.router.navigate(['/main/matters/case-management/general']);
        this.router.navigate(['/main/matters/case-management/accounting/' + this.route.snapshot.params.code]);
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
  };
  back() {
    this.location.back();
}
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

  open_caseInfo() {

    let paramdata = this.cs.encrypt({ code: this.form.controls.caseInformationNo.value, pageflow: 'Display' });
    if (this.form.controls.caseInformationNo.value.charAt(0) == '5')
      this.router.navigate(['/main/matters/case-info/immigrationnew/' + paramdata]);
    else
      this.router.navigate(['/main/matters/case-info/landenew/' + paramdata]);
  }

  getUpdateImmigration() {
    this.spin.show();

    this.sub.add(this.service.getUpdateImmigration(this.form.controls.matterNumber.value, this.form.controls.referenceField9.value).subscribe(res => {
      this.toastr.success(res.matterNumber + " Immigration updated successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      let code = this.route.snapshot.params.code;
      let js = this.cs.decrypt(code);
      this.fill(js);

    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();

    }));
  }

  onItemSelect(item:any){
    console.log(item.value)
  if (item)
  // res.forEach((x) => {
  //   // x.receiptNo = this.receiptNolist.find(y => y.key == x.noteTypeId)?.value;
  //   x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;

  // })
  this.getsubcaseCategoryId(item.value)
}

}