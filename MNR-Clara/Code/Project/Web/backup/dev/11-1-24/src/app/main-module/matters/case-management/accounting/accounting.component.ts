import { Component, OnInit } from '@angular/core';
import { FormControl, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from "@angular/common";
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { GeneralMatterService } from '../General/general-matter.service';
import { AdminService } from 'src/app/main-module/setting/business/admin-cost/admin.service';
import { MatterExpensesService } from '../expenses/matter-expenses.service';

interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-accounting',
  templateUrl: './accounting.component.html',
  styleUrls: ['./accounting.component.scss']
})
export class AccountingComponent implements OnInit {

  screenid = 1107;
  input: any;
  public icon = 'expand_more';


  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({

    administrativeCost: [, ],
    approvalDate: [,],
    arAccountNumber: [, Validators.required],
    billingFormatId: [, Validators.required],
    billingFrequencyId: [, Validators.required],
    billingModeId: [, Validators.required],
    billingRemarks: [,],
    caseCategoryId: [,],
    caseClosedDate: [,],
    caseFileNumber: [,],
    caseFiledDate: [,],
    caseInformationNo: [,],
    caseOpenedDate: [,],
    caseSubCategoryId: [,],
    classId: [, Validators.required],
    clientId: [,],
    contigencyFeeAmount: [,],
    courtDate: [,],
    createdBy: [,],
    createdOn: [,],
    deletionIndicator: [,],
    directPhoneNumber: [,],
    expirationDate: [,],
    fileNumber: [,],
    flatFeeAmount: [, ],
    languageId: [,],
    matterDescription: [,],
    matterNumber: [,],
    priorityDate: [,],
    rateUnit: [,],
    receiptDate: [,],
    receiptNoticeNo: [,],
    referenceField1: [,],
    referenceField10: [, [Validators.email ]],
    referenceField11: [,],
    referenceField12: [, Validators.required],
    referenceField13: [,],
    referenceField14: [,],
    referenceField15: [,],
    referenceField16: [,],
    referenceField17: [,],
    referenceField18: [,],
    referenceField19: [,],
    referenceField2: [false,],
    remainingAmount: [,],
    referenceField20: [,],
    referenceField3: [,],
    referenceField4: [,],
    referenceField5: [,],
    referenceField6: [,],
    referenceField7: [,],
    referenceField8: [,],
    referenceField9: [,],
    referenceField25: [,],
    statusId: [,],
    transactionId: [,],
    trustDepositNo: ["4205",],
    updatedBy: [,],
    updatedOn: [,],

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



  isbtntext = true;
  constructor(private fb: FormBuilder,
    private auth: AuthService,
    private service: GeneralMatterService, private location: Location,
    public toastr: ToastrService, private admincostservice: AdminService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private expenseservice: MatterExpensesService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,) { }

  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.updatedBy.disable();

    this.form.controls.referenceField15.disable();
    this.form.controls.referenceField16.disable();
    this.form.controls.referenceField17.disable();
    this.form.controls.referenceField18.disable();
    this.form.controls.referenceField19.disable();
    this.form.controls.referenceField20.disable();


    this.form.controls.referenceField2.patchValue(true);



    // this.form.controls.referenceField2.valueChanges.subscribe(val => {
    //   if (val == true) {
    //     this.form.controls.referenceField3.patchValue(this.form.controls.referenceField15.value);
    //     this.form.controls.referenceField4.patchValue(this.form.controls.referenceField16.value);
    //     this.form.controls.referenceField5.patchValue(this.form.controls.referenceField17.value);
    //     this.form.controls.referenceField6.patchValue(this.form.controls.referenceField18.value);
    //     this.form.controls.referenceField7.patchValue(this.form.controls.referenceField19.value);
    //     this.form.controls.referenceField8.patchValue(this.form.controls.referenceField20.value);
    //   }


    //   else {
    //     this.form.controls.referenceField3.patchValue('');
    //     this.form.controls.referenceField4.patchValue('');
    //     this.form.controls.referenceField5.patchValue('');
    //     this.form.controls.referenceField6.patchValue('');
    //     this.form.controls.referenceField7.patchValue('');
    //     this.form.controls.referenceField8.patchValue('');
    //   }

    // });

    //this.form.controls.statusId.disable();

    this.form.controls.flatFeeAmount.valueChanges.subscribe((x: number) => {
      this.form.controls.administrativeCost.patchValue(0);

      // this.spin.show();
      this.sub.add(this.admincostservice.Getall().subscribe(res => {
        // this.spin.hide();
        let data = this.cs.filterArray(res, { classId: this.form.controls.classId.value });
        let filter = data.filter((f: any) => x >= (f.flatFeeRateFrom as number) && x <= (f.flatFeeRateTo as number));
        if (filter.length > 0)
          this.form.controls.administrativeCost.patchValue(filter[0].adminCost);
      }, err => {
        // this.cs.commonerror(err);
        // this.spin.hide();
      }));
    }

    );

    sessionStorage.setItem('matter', this.route.snapshot.params.code);

    this.dropdownlist();

    let code = this.route.snapshot.params.code;
    let js = this.cs.decrypt(code);
    this.fill(js);

  }

  matterBilling(){
    console.log(this.form.controls.referenceField2.value)
      if(this.form.controls.referenceField2.value == false){
        this.form.controls.referenceField3.patchValue(this.form.controls.referenceField15.value);
        this.form.controls.referenceField4.patchValue(this.form.controls.referenceField16.value);
        this.form.controls.referenceField5.patchValue(this.form.controls.referenceField17.value);
        this.form.controls.referenceField6.patchValue(this.form.controls.referenceField18.value);
        this.form.controls.referenceField7.patchValue(this.form.controls.referenceField19.value);
        this.form.controls.referenceField8.patchValue(this.form.controls.referenceField20.value);
      }
      else{
        this.form.controls.referenceField3.patchValue('');
        this.form.controls.referenceField4.patchValue('');
        this.form.controls.referenceField5.patchValue('');
        this.form.controls.referenceField6.patchValue('');
        this.form.controls.referenceField7.patchValue('');
        this.form.controls.referenceField8.patchValue('');
      }
  }
  billingModeIdList: any[] = [];
  billingFormatIdList: any[] = [];
  billingFrequencyIdList: any[] = [];
  coaList: any[] = [];
  statusIdList: any[] = [];
  timeKeeperList: any[] = [];

  selectedItems: SelectItem[] = [];
  multiselecttreferralList: any[] = [];
  multireferralList: any[] = [];

  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.billingModeId.url,
    this.cas.dropdownlist.setup.billingFormatId.url,
    this.cas.dropdownlist.setup.billingFrequencyId.url,
    this.cas.dropdownlist.setup.accountNumber.url,
    this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.setup.timeKeeperCode.url,  //inset by MK
    ]).subscribe((results) => {
      console.log(results)
      this.billingModeIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.billingModeId.key);
      this.billingFormatIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.billingFormatId.key);
      this.billingFrequencyIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.billingFrequencyId.key);
      this.coaList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.accountNumber.key);
      console.log( this.coaList)
      //inset my MK 7-3-22
      this.statusIdList = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.statusId.key, { statusId: [26, 27, 28, 29, 30, 36, 47, 48, 49, 50] });
      this.timeKeeperList = this.cas.foreachlist(results[5], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.timeKeeperList.forEach((x: { key: string; value: string; }) => this.multireferralList.push({ value: x.key, label: x.value }))
      this.multiselecttreferralList = this.multireferralList;
      //end if inset
    }, (err) => {

      this.toastr.error(err, "");
    });
    this.spin.hide();
  }
  btntext = "Save";
  fill(data: any) {

    if (data.pageflow != 'New') {
      this.btntext = 'Update';
      if (data.pageflow == 'Display') {
        this.form.disable();
        this.dropdownSettings.disabled=true;
        this.isbtntext = false;
      }
      this.form.controls.matterNumber.disable();

      this.spin.show();
      this.sub.add(this.service.Get(data.code).subscribe(res => {
        this.form.patchValue(res, { emitEvent: false });
        this.form.controls.referenceField2.patchValue(res.referenceField2);
        this.form.controls.referenceField3.patchValue(res.referenceField3);
       
        this.form.controls.referenceField4.patchValue(res.referenceField4);
        this.form.controls.referenceField5.patchValue(res.referenceField5);
        this.form.controls.referenceField6.patchValue(res.referenceField6);
        this.form.controls.referenceField7.patchValue(res.referenceField7);
        this.form.controls.referenceField8.patchValue(res.referenceField8);
         this.form.controls.arAccountNumber.patchValue("4200")
         this.form.controls.trustDepositNo.patchValue("4205")
        this.multireferralList.forEach(element => {
          if (element.id == res.referenceField12) {
            this.form.controls.referenceField12.patchValue([element]);
          }
        });
        this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));

        this.form.controls.billingModeId.patchValue(parseInt(res.billingModeId));
        this.form.controls.billingFrequencyId.patchValue(parseInt(res.billingFrequencyId));
        this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));

        this.sub.add(this.service.GetClient(res.clientId).subscribe(ress => {
          this.form.controls.referenceField15.patchValue(ress.addressLine3);
          this.form.controls.referenceField16.patchValue(ress.referenceField16);
          this.form.controls.referenceField17.patchValue(ress.referenceField17);
          this.form.controls.referenceField18.patchValue(ress.referenceField18);
          this.form.controls.referenceField19.patchValue(ress.referenceField19);
          this.form.controls.referenceField20.patchValue(ress.referenceField20);
          if(this.form.controls.referenceField2.value == true){
          this.form.controls.referenceField3.patchValue(ress.addressLine3);
          this.form.controls.referenceField4.patchValue(ress.referenceField16);
          this.form.controls.referenceField5.patchValue(ress.referenceField17);
          this.form.controls.referenceField6.patchValue(ress.referenceField18);
          this.form.controls.referenceField7.patchValue(ress.referenceField19);
          this.form.controls.referenceField8.patchValue(ress.referenceField20);
          }
          this.spin.hide();
  
        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));
        if(this.form.controls.referenceField2.value == 'false'){
          this.form.controls.referenceField2.patchValue(false);
        } else {
          this.form.controls.referenceField2.patchValue(true);
        }
        this.spin.hide();

      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));

   

    }
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

    if(this.form.controls.billingModeId.value == 2 && !this.form.controls.flatFeeAmount.value){
      this.toastr.error(
        "Please fill flat fees to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      return;
    }

    this.cs.notifyOther(false);
    this.spin.show();
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    if (this.selectedItems != null && this.selectedItems != undefined && this.selectedItems.length > 0) {
      this.form.patchValue({ referenceField12: this.selectedItems[0].id });
    }
    this.form.patchValue({ updatedby: this.auth.username });
    if (this.form.controls.matterNumber.value) {

      if(this.form.controls.remainingAmount.value == null){
        this.form.controls.remainingAmount.patchValue(this.form.controls.flatFeeAmount.value);
      }

      this.sub.add(this.service.Updateaccounting(this.form.getRawValue(), this.form.controls.matterNumber.value).subscribe(res => {
        this.toastr.success(this.form.controls.matterNumber.value + " updated successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });


        this.expenseservice.Search({expenseCode: ["ADM"], matterNumber: this.form.controls.matterNumber.value}).subscribe(res => {
          if(res.length == 0){
            let obj: any = {};
            obj.caseCategoryId = this.form.controls.caseCategoryId.value;
            obj.caseSubCategoryId = this.form.controls.caseSubCategoryId.value;
            obj.caseInformationNo = this.form.controls.caseInformationNo.value;
            obj.classId = this.form.controls.classId.value;
            obj.clientId = this.form.controls.clientId.value;
            obj.expenseAmount = this.form.controls.administrativeCost.value;
            obj.expenseCode = "ADM";
            obj.expenseDescription = "Administrative Cost";
            obj.expenseType = "Debit";
            obj.languageId = this.form.controls.languageId.value;
            obj.matterNumber = this.form.controls.matterNumber.value;
            obj.numberofItems = 1;
            obj.referenceField5 = "Soft cost";
            obj.statusId = 37;

            this.expenseservice.Create(obj).subscribe(res =>{
              this.toastr.success(res.expenseCode + " saved successfully!", "Notification", {
                timeOut: 2000,
                progressBar: false,
              });
              this.spin.hide();
              this.router.navigate(['/main/matters/case-management/expenses/' + this.route.snapshot.params.code]);
            })

          }
          if(res.length == 1){
            let obj: any = {};
            obj.caseCategoryId = this.form.controls.caseCategoryId.value;
            obj.caseSubCategoryId = this.form.controls.caseSubCategoryId.value;
            obj.caseInformationNo = this.form.controls.caseInformationNo.value;
            obj.classId = this.form.controls.classId.value;
            obj.clientId = this.form.controls.clientId.value;
            obj.expenseAmount = this.form.controls.administrativeCost.value;
            obj.expenseCode = "ADM";
            obj.expenseDescription = "Administrative Cost";
            obj.expenseType = "Debit";
            obj.languageId = this.form.controls.languageId.value;
            obj.matterNumber = this.form.controls.matterNumber.value;
            obj.numberofItems = 1;
            obj.referenceField5 = "Soft cost";
            obj.statusId = 37;

            this.expenseservice.Update(obj, res[0].matterExpenseId).subscribe(res =>{
              this.toastr.success(res.expenseCode + " updated successfully!", "Notification", {
                timeOut: 2000,
                progressBar: false,
              });
              this.spin.hide();
              this.router.navigate(['/main/matters/case-management/matter/' + this.route.snapshot.params.code]);
            })

          }
        })




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
       // this.router.navigate(['/main/matters/case-management/general']);
       this.router.navigate(['/main/matters/case-management/matter/' + this.route.snapshot.params.code]);

      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();

      }));
    }
  };
  back() {
    this.router.navigate(['/main/matters/case-management/general']);
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
}