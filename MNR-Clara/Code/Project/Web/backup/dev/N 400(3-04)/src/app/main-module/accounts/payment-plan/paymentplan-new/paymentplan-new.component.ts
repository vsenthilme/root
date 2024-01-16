import { Component, OnInit, ViewChild } from "@angular/core";
import { FormArray, FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { GeneralMatterService } from "src/app/main-module/matters/case-management/General/general-matter.service";
import { QuotationService } from "../../quotation/quotations-list/quotation.service";
import { PaymentPlanService } from "../payment-plan.service";

interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-paymentplan-new',
  templateUrl: './paymentplan-new.component.html',
  styleUrls: ['./paymentplan-new.component.scss']
})
export class PaymentplanNewComponent implements OnInit {


  screenid: 1137 | undefined;
  displayedColumns: string[] = ['itemNumber', 'dueDate', 'dueAmount', 'remainingDueNow','paymentReminderDays','reminderDate'];
  input: any;
  isbtntext = true;
  path = "New"
  public icon = 'expand_more';

  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({

    approvedOn: [,],
    classId: [, [Validators.required]],
    clientId: [[], [Validators.required]],
    //clientIdFE: [[], [Validators.required]],
    createdBy: [this.auth.userID],
    createdOn: [,],
    currency: [],
    deletionIndicator: [0],
    dueAmount: [, [Validators.required]],
    endDate: [,],
    installmentAmount: [],
    languageId: [, [Validators.required]],
    matterNumber: [[], [Validators.required]],
  //  matterNumberFE: [[], [Validators.required]],
    noOfInstallment: [],
    paymentCalculationDayDate: [, [Validators.required]],
    paymentPlanDate: [, [Validators.required]],
    paymentPlanLines: [, []],
    paymentPlanNumber: [],
    paymentPlanRevisionNo: [],
    paymentPlanStartDate: [, [Validators.required]],
    paymentPlanText: [, [Validators.required]],
    paymentPlanTotalAmount: [, [Validators.required]],
    quotationNo: [,],
    reminderDate: [,],
    paymentReminderDays: [,],
    quotationNoFE: [[],],
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
    sentOn: [,],
    statusId: [],
    updatedBy: [this.auth.userID],
    updatedOn: [,],
  });

  apiClientfileter: any = { clientId: null };
  apiquotefileter: any = { clientId: null, matterNumber: null };

  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  matterList: any;
  statusearchList: any;
  quotationList: any;
  duedatevalue: any;
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
  dataSource = new MatTableDataSource<any>([]);


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

  constructor(private fb: FormBuilder,
    private auth: AuthService,
    private service: PaymentPlanService,
    private serviceQ: QuotationService,
    private serviceatter: GeneralMatterService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private cs: CommonService,) { }

  ngOnInit(): void {

    this.form.controls.createdBy.disable();
    this.form.controls.createdOn.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();

    this.form.controls.clientId.valueChanges.subscribe(x => {
      this.apiClientfileter = { clientId: x };
    });
    this.form.controls.matterNumber.valueChanges.subscribe(x => {
      if (x && x.length > 0) {
        this.apiquotefileter = { clientId: this.form.controls.clientId.value, matterNumber: x };
        ;
        this.getmatter(x);
      }
    });

    this.dropdownlist();

    this.form.controls.quotationNo.valueChanges.subscribe(x => {
      if (x && x.length > 0)
        this.get_quotation(x);
    });

    this.auth.isuserdata();

    let code = this.route.snapshot.params.code;
    if (code != 'new') {

      let js = this.cs.decrypt(code);
      this.fill(js);
    }

 

  }


  datecalculate(element : any) {
    if (element.paymentReminderDays != 0) {
      let Newdate: Date = new Date(element.dueDate);
      Newdate.setDate(Newdate.getDate() - Number(element.paymentReminderDays));
      let reminderDate = new Date(Newdate);
      element.reminderDate = this.cs.dateNewFormat(reminderDate);
    //  this.form.controls.paymentReminderDays.patchValue(this.cs.day_callapiSearch(reminderDate))
    this.dataSource._updateChangeSubscription();
    }else{
      element.reminderDate = element.dueDate
    }
  }
  
  statusIdList: any[] = [];

  selectedItems: SelectItem[] = [];
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];
  selectedItems2: SelectItem[] = [];
  multiselectmatterList: any[] = [];
  multimatterList: any[] = [];
  selectedItems3: SelectItem[] = [];
  multiselectquotationList: any[] = [];
  multiquotationList: any[] = [];

  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };
  referralList: any = [];

  onItemSelect(item: any, type: string) {
    if (type == 'CLIENT') {
      this.multiselectmatterList = [];
   //   this.form.controls.matterNumber.patchValue('');
      this.multimatterList.forEach((data: any) => {
        if (data['clientId'].includes(this.form.controls.clientId.value)) {
          this.multiselectmatterList.push(data)
        }
      })
    } else if (type == 'MATTER') {
      this.multiselectquotationList = [];
    //  this.form.controls.quotationNo.patchValue('');
      this.multiquotationList.forEach((data: any) => {
        if (data['matterNumber'].includes(this.form.controls.matterNumber.value)) {
          this.multiselectquotationList.push(data)
        }
      })
      console.log(this.multiselectquotationList)
    }

  }
  OnItemDeSelect(item: any) {
  }
  onSelectAll(items: any) {

  }
  onDeSelectAll(items: any) {
  }
  matterfilterList: any = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.statusId.url,


    ]).subscribe((results) => {
     
      let statusL = [12, 43, 1];
      if (this.form.controls.statusId.value == 1)
        statusL = [12, 43, 1];
      else if (this.form.controls.statusId.value == 12)
        statusL = [43, 12];
      this.statusIdList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.statusId.key, { statusId: statusL });


      this.spin.hide();
    }, (err) => {
      this.toastr.error(err, "");
    });

    // this.spin.hide();
    this.sub.add(this.service.GetClientdetails().subscribe(res => {
      this.spin.show();
      this.referralList = res;
      this.referralList.forEach((x: { clientId: string; firstNameLastName: string; }) => this.multiclientList.push({ value: x.clientId, label: x.clientId + '-' + x.firstNameLastName }))
      this.multiselectclientList = this.multiclientList;
      this.sub.add(this.service.getmatter().subscribe(res => {
        this.matterList = res;
        this.matterfilterList = this.matterList.filter((element: { matterNumber: any; }) => {
          return element.matterNumber === this.apiClientfileter

        })
        this.matterList.forEach((x: any) => this.multimatterList.push({ value: x.matterNumber, label: x.matterNumber + '-' + x.matterDescription, clientId: x.clientId }))
        this.multiselectmatterList = this.multimatterList;
        this.sub.add(this.service.getquotation().subscribe(res => {
          this.quotationList = res;
          this.quotationList.forEach((x: any) => this.multiquotationList.push({ value: x.quotationNo, label: x.quotationNo, matterNumber: x.matterNumber }))
          this.multiselectquotationList = this.multiquotationList;

          // this.spin.hide();

        },
          err => {
            this.cs.commonerror(err);
            // this.spin.hide();
          }));
        // this.spin.hide();

      },

        err => {
          this.cs.commonerror(err);
          // this.spin.hide();
        }));

   this.spin.hide();

    },
      err => {
        this.cs.commonerror(err);
        // this.spin.hide();
      }));
  }

  getmatter(code: any) {
     this.spin.show();
    console.log(code)
    this.sub.add(this.serviceatter.Get(this.form.controls.matterNumber.value).subscribe(res => {

      this.form.controls.classId.patchValue(res.classId);
      this.form.controls.languageId.patchValue(res.languageId);
       this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      // this.spin.hide();
    }));
  }

  get_quotation(code: any) {
     this.spin.show();
    this.sub.add(this.serviceQ.Get(this.form.controls.quotationNo.value, 1).subscribe(res => {

      this.form.controls.classId.patchValue(res.classId);
      this.form.controls.languageId.patchValue(res.languageId);
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      // this.spin.hide();
    }));
  }
  calcdues = false;
  isbtntexts = false;
  btntext = "Save";
  pageflow = "New";
  fill(data: any) {
    this.pageflow = data.pageflow;

    if (data.pageflow != 'New') {
      console.log(this.form)
      this.path = "Edit"
      this.form.controls.clientId.disable();
      this.form.controls.matterNumber.disable();
      this.form.controls.quotationNo.disable();
      this.dropdownSettings.disabled = true;
      console.log(this.form)
      this.btntext = "Update";
      this.pageflow = data.code.paymentPlanNumber;
      if (data.pageflow == 'Display') {
        this.path = "Display"
        this.form.disable();
        this.dropdownSettings.disabled = true;
        this.calcdues = true;
        this.isbtntext = false;
      }
      ;
       this.spin.show();
      this.sub.add(this.service.Get(data.code.paymentPlanNumber, data.code.paymentPlanRevisionNo).subscribe(res => {
        this.form.patchValue(res);
        this.form.controls.clientId.patchValue(res.clientId);
        this.form.controls.matterNumber.patchValue(res.matterNumber);
        this.form.controls.quotationNo.patchValue(res.quotationNo);

        this.form.controls.paymentPlanDate.patchValue(this.cs.day_callapiSearch(this.form.controls.paymentPlanDate.value));
        this.form.controls.paymentPlanStartDate.patchValue(this.cs.day_callapiSearch(this.form.controls.paymentPlanStartDate.value));

        this.form.controls.createdOn.patchValue(this.cs.dateapi(this.form.controls.createdOn.value));
        this.form.controls.updatedOn.patchValue(this.cs.dateapi(this.form.controls.updatedOn.value));
        this.dataSource = new MatTableDataSource<any>(res.paymentPlanLines);
        if (res.statusId != 12) { this.isbtntexts = true; }
         this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        // this.spin.hide();
      }));
    }
  }

  submit(x: any = null) {




    this.submitted = true; this.form.updateValueAndValidity;

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
  //  this.calculate();


  if(this.dataSource.data.length <= 0){
    this.toastr.error(
      "Please calculate dues to continue",
      "Notification", {
      timeOut: 2000,
      progressBar: false,
    }
    );

    this.cs.notifyOther(true);
    return;
  }
    this.form.controls.paymentPlanLines.patchValue([]);
    console.log(this.form)
    console.log(this.dataSource.data)
    this.form.controls.paymentPlanLines.patchValue(this.dataSource.data)
    console.log(this.form)

    this.form.controls.paymentPlanDate.patchValue(this.cs.dateNewFormat(this.form.controls.paymentPlanDate.value));
    this.form.controls.paymentPlanStartDate.patchValue(this.cs.dateNewFormat(this.form.controls.paymentPlanStartDate.value));


    this.cs.notifyOther(false);
     this.spin.show();
    this.form.removeControl('updatedOn');
    this.form.removeControl('createdOn');
    this.form.patchValue({ updatedby: this.auth.username });
    if (this.form.controls.paymentPlanNumber.value) {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.form.controls.paymentPlanNumber.value, this.form.controls.paymentPlanRevisionNo.value).subscribe(res => {
        this.toastr.success(this.form.controls.paymentPlanNumber.value + " updated successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
         this.spin.hide();
        this.router.navigate(['/main/accounts/paymentplanlist']);



      }, err => {

        this.cs.commonerror(err);
        // this.spin.hide();

      }));
    }
    else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.paymentPlanNumber + " saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
         this.spin.hide();
        this.router.navigate(['/main/accounts/paymentplanlist']);

      }, err => {
        this.cs.commonerror(err);
        // this.spin.hide();

      }));
    }
  };
  back() {
    this.router.navigate(['/main/accounts/paymentplanlist']);
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  calculate() {
 
    //this.spin.show();
    this.spin.show("Calculating");
    setTimeout(() => {
      this.spin.hide("Calculating");
    }, 3000);
    this.panelOpenState = !this.panelOpenState
    let result: any[] = [];
    let interval: number = this.form.controls.paymentCalculationDayDate.value;

    let i = 0;
    do {
      let reminderDate: Date = (i == 0) ? new Date(this.form.controls.paymentPlanStartDate.value) : new Date(result[i - 1].dueDate);
     
     
      reminderDate.setMonth(reminderDate.getMonth() + 1);
      this.duedatevalue = (reminderDate)
      this.form.controls.paymentReminderDays.patchValue(0)
      if (i == 0)
        reminderDate.setDate(interval);
        console.log(this.form.controls.dueAmount.value)
      
      let value = {
        'itemNumber': i + 1,
        'dueDate': this.cs.dateNewFormat(reminderDate),
        'dueAmount': this.form.controls.dueAmount.value,
        'remainingDueNow': (i == 0 ? this.form.controls.paymentPlanTotalAmount.value - this.form.controls.dueAmount.value :
          result[i - 1].remainingDueNow - this.form.controls.dueAmount.value),
        'reminderDays': this.form.controls.paymentReminderDays.value,
        'reminderDate': this.form.controls.paymentReminderDays.value == 0 ? this.cs.dateNewFormat(reminderDate) : this.form.controls.reminderDate.value,
      };
  
      if (value.remainingDueNow < 0) {
        value.dueAmount = result[i - 1].remainingDueNow;
        value.remainingDueNow = 0;
      }
  
      result.push(value);
      i++;
    }
    
    while (result[i - 1].remainingDueNow != 0)
    this.form.controls.noOfInstallment.patchValue(result.length);
    console.log(result)



    this.dataSource = new MatTableDataSource<any>(result);
    this.form.controls.paymentPlanLines.patchValue(result);
    //this.spin.hide();
  }

  change(item: any) {
    console.log(item)
  }
 
}

