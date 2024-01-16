import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormBuilder, Validators } from '@angular/forms';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { TimekeeperService } from 'src/app/main-module/setting/business/timekeeper/timekeeper.service';
import { GeneralMatterService } from '../../General/general-matter.service';
import { MatternotesNewComponent } from '../../matters-notes/matternotes-new/matternotes-new.component';
import { MatterRateService } from '../../rate-list/matter-rate.service';
import { TimeTicketsService } from '../time-tickets.service';
interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-time-new',
  templateUrl: './time-new.component.html',
  styleUrls: ['./time-new.component.scss']
})
export class TimeNewComponent implements OnInit {
  screenid: 1117 | undefined;

  isMatterAllChecked = true;

  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({
    classId: [this.auth.classId],
    createdBy: [this.auth.userID],
    deletionIndicator: [0],
    languageId: [this.auth.languageId],
    matterNumber: [,],
    statusId: [33],
    updatedBy: [this.auth.userID],


    activityCode: [],
    activityCodeFE: [],
    approvedBillableAmount: [],
    approvedBillableTimeInHours: [],
    approvedOn: [],
    assignedOn: [],
    clientName: [],
    assignedPartner: [],
    billType: ['billable', Validators.required],
    caseCategoryId: [],
    caseSubCategoryId: [],
    clientId: [],
    screatedOn: [],
    defaultRatePerHour: [],
    defaultRate: [, Validators.required],
    rateUnit: [],
    allmatters: [],
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
    taskCode: [],
    taskCodeFE: [],
    timeKeeperCode: [, Validators.required],
    timeTicketAmount: [, Validators.required],
    stimeTicketDate: [],
    stimeTicketDateFE: [this.cs.todayapi(), Validators.required],
    timeTicketDescription: [, Validators.required],
    timeTicketHours: [0, Validators.required],
    timeTicketNumber: [],
    updatedOn: [],
    timerValidation: [false]
  });


  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }

  disabled = false;
  step = 0;

  panelOpenState = false;
  newPage = true;
  ttbSelected = false;
  _start: boolean = false;
  matter: any;
  btntext = "Save"
  isbtntext = true;

  statusIdlist: any[] = [];
  taskCodelist: any[] = [];
  timeKeeperCodelist: any[] = [];
  activityCodelist: any[] = [];

  selectedItems: any[] = [];
  multiselectactivityList: any[] = [];
  multiactivityList: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselecttaskList: any[] = [];
  multitaskList: any[] = [];

  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };

  multimatterList: any[] = [];
  multiMatterSelectList: any[] = [];

  constructor(
    public dialogRef: MatDialogRef<MatternotesNewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: TimeTicketsService, private matterService: GeneralMatterService,
    public toastr: ToastrService, private TimekeeperServiceService: TimekeeperService,
    private cas: CommonApiService, private rateservice: MatterRateService,
    private spin: NgxSpinnerService, public datePipe: DatePipe,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService,    private decimalPipe: DecimalPipe,
  ) { }

  onChange(ob: MatCheckboxChange) {
    this.form.controls.clientName.patchValue(null)
    this.form.controls.defaultRate.patchValue(null)
    this.form.controls.matterNumber.patchValue(null)
    this.dropdownlist();
  }

  ngOnInit(): void {
    console.log(this.data.pageflow)
    this.form.controls.screatedOn.disable();
    this.form.controls.createdBy.disable();
    this.form.controls.updatedBy.disable();
    this.form.controls.updatedOn.disable();
    this.form.controls.clientName.disable();
    this.form.controls.allmatters.patchValue(false)

    this.auth.isuserdata();

    this.dropdownlist();
    this.form.controls.defaultRate.disable();
    this.form.controls.defaultRatePerHour.disable();
    this.form.controls.timeTicketAmount.disable();
    //this.form.controls.timeKeeperCode.disable();
    this.form.controls.matterNumber.valueChanges.subscribe(x => {
      if (x) {
        this.getMatterDetails();
        this.gettimeKeeperCodeDetails_amountcalculation();
      }
    });
    this.form.controls.timeKeeperCode.valueChanges.subscribe(x => {
      this.form.controls.defaultRate.patchValue(0);
      this.form.controls.timeTicketAmount.patchValue(0);
      if (x) {
        this.gettimeKeeperCodeDetails_amountcalculation();
      }
    });
    this.form.controls.timeTicketHours.valueChanges.subscribe(x => {
      this.form.controls.timeTicketAmount.patchValue(0);
      if (x) {
        if (this.form.controls.billType.value == 'non-billable') {
          console.log(this.form.controls.defaultRatePerHour.value)
          console.log(this.form.controls.defaultRatePerHour)
          let value: any = Math.round((this.form.controls.defaultRatePerHour.value as number *
            this.form.controls.timeTicketHours.value as number) * 100) / 100;
            this.form.controls.timeTicketAmount.patchValue(value);


        } else {
          this.totalAmountCalculate();
        }
      }
    });
    this.form.controls.billType.valueChanges.subscribe(x => {
      this.form.controls.timeTicketAmount.patchValue(0);
      if (x == "non-billable") {
        let value: any = (this.form.controls.defaultRatePerHour.value as number *
          this.form.controls.timeTicketHours.value as number);
     
          let nonBillableAmount = value.toFixed(2)
          this.form.controls.timeTicketAmount.patchValue(nonBillableAmount)


      } else {
        this.totalAmountCalculate();
      }
    });
    if (this.data.matter) {
      this.matter = '' + this.data.matter + ' / ' + this.data.matterdesc + ' - ';
      this.form.controls.matterNumber.patchValue(this.data.matter);
    }
    if (this.data.pageflow != 'New') {
      if (this.data.pageflow == 'Display') {
        this.form.disable();
        this.isbtntext = false;
      }

      // this.form.controls.billType.disable();
      this.newPage = false;
      this.btntext = "Update";
      if (!this.newPage) {
        this.form.controls.stimeTicketDateFE.patchValue('')
        this.form.controls.stimeTicketDate.patchValue('')
        this.form.controls.billType.patchValue('')
      }
    }
    if (this.data.pageflow == 'Copy') {
      this.btntext = "Save";
    }
    if (this.data.pageflow == 'New') {
    this.form.controls.timeKeeperCode.patchValue(this.auth.userID);
    this.sub.add(this.TimekeeperServiceService.Get(this.form.controls.timeKeeperCode.value).subscribe(res => {
      this.form.controls.defaultRatePerHour.patchValue(res.defaultRate);
    }, err => {
      this.cs.commonerror(err);
    }));
  }
  }

  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.taskbasedCode.url,
    this.cas.dropdownlist.setup.timeKeeperCode.url,
    this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.setup.activityCode.url,
    this.cas.dropdownlist.matter.matterNumberList.url,
    this.cas.dropdownlist.matter.matterNumberOpenList.url,
    ]).subscribe((results: any) => {
      this.spin.hide();
      this.taskCodelist = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.taskbasedCode.key);
      this.taskCodelist.forEach((x: { key: string; value: string; }) => this.multitaskList.push({ value: x.key, label: x.value }))
      this.multiselecttaskList = this.multitaskList;

      this.timeKeeperCodelist = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.statusIdlist = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.statusId.key);
      this.activityCodelist = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.activityCode.key);
      this.activityCodelist.forEach((x: { key: string; value: string; }) => this.multiactivityList.push({ value: x.key, label: x.value }))
      this.multiselectactivityList = this.multiactivityList;

      if (this.form.controls.allmatters.value == true) {
        this.multiMatterSelectList = [];
        this.multimatterList = results[4].matterDropDown;

        this.multimatterList.forEach(matter => {
          this.multiMatterSelectList.push({ value: matter.matterNumber, label: (matter.matterNumber + "-" + matter.matterDescription) });
          if ((this.data.matter != null) && matter.matterNumber == this.data.matter) {
            this.form.controls.clientName.patchValue(matter.clientName);
            this.form.controls.clientId.patchValue(matter.clientId);
          }
        })
      }
      if (this.form.controls.allmatters.value == false) {

        this.multiMatterSelectList = [];
        this.multimatterList = results[5].matterDropDown;
        this.multimatterList.forEach(matter => {
          this.multiMatterSelectList.push({ value: matter.matterNumber, label: (matter.matterNumber + "-" + matter.matterDescription) });
          if ((this.data.matter != null) && matter.matterNumber == this.data.matter) {
            this.form.controls.clientName.patchValue(matter.clientName);
            this.form.controls.clientId.patchValue(matter.clientId);
          }
        })
      }


      if (!this.newPage) {
        this.fill();
      }
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.form.controls.languageId.patchValue("EN");
  }

  fill() {
    this.sub.add(this.service.Get(this.data.code).subscribe(res => { 
      res.defaultRate = this.form.controls.defaultRate.value
      res.billType = res.billType.toLowerCase()
      this.form.patchValue(res, { emitEvent: false });
      let createdon = res.screatedOn;
      let updatedOn = res.updatedOn;
      console.log(createdon)
      if(this.form.controls.timeKeeperCode.value){
        this.sub.add(this.rateservice.Get(this.form.controls.timeKeeperCode.value, this.form.controls.matterNumber.value).subscribe(res => {
          let decimalConvert = res.assignedRatePerHour.toFixed(2);
          this.form.controls.defaultRate.patchValue(decimalConvert as number);
       
          this.sub.add(this.TimekeeperServiceService.Get(this.form.controls.timeKeeperCode.value).subscribe(res => {
            this.form.controls.defaultRatePerHour.patchValue(res.defaultRate);
          }, err => {
            this.cs.commonerror(err);
          }));
        }, err => {
          this.cs.commonerror(err);
          //this.spin.hide();
        }));
      }
      if (res.stimeTicketDate != null && res.stimeTicketDate) {
       // this.form.controls.stimeTicketDate.patchValue(res.stimeTicketDate.split('+')[0]);
       this.form.controls.stimeTicketDateFE.patchValue(this.cs.day_callapiSearch(this.form.controls.stimeTicketDate.value));
      }
      // this.multiactivityList.forEach(element => {
      //   if (element.id == res.activityCode) {
      //     this.form.controls.activityCodeFE.patchValue([element]);
      //   }
      // });
      // this.multitaskList.forEach(element => {
      //   if (element.id == res.taskCode) {
      //     this.form.controls.taskCodeFE.patchValue([element]);
      //   }
      // });
      this.multimatterList.forEach(element => {
        if (element.id == res.matterNumber) {
          this.form.controls.matterNumber.patchValue([element]);
        }
      });
      this.form.controls.screatedOn.patchValue(this.cs.excel_date(createdon));
      this.form.controls.updatedOn.patchValue(this.cs.excel_date(updatedOn));
    }, err => {
      //this.spin.hide();
      this.cs.commonerror(err);
    }));
  }
  gettimeKeeperCodeDetails_amountcalculation() {
  
    if (this.form.controls.matterNumber.value && this.form.controls.timeKeeperCode.value) {
      //this.spin.show();
      this.sub.add(this.rateservice.Get(this.form.controls.timeKeeperCode.value, this.form.controls.matterNumber.value).subscribe(res => {
        let decimalConvert = res.assignedRatePerHour.toFixed(2);
        this.form.controls.defaultRate.patchValue(decimalConvert as number);
        //this.form.controls.defaultRatePerHour.patchValue(res.defaultRatePerHour as number);
        if (this.form.controls.timeTicketHours.value)
          this.form.controls.timeTicketAmount.patchValue(this.form.controls.defaultRate.value as number *
            this.form.controls.timeTicketHours.value as number);

            this.form.controls.timeTicketAmount.patchValue(this.decimalPipe.transform(this.form.controls.timeTicketAmount.value, "1.2-2"))
        //this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        //this.spin.hide();
      }));
    }
  }

  totalAmountCalculate() {
    if (this.form.controls.defaultRate.value) {
      let value: any = (this.form.controls.defaultRate.value as number *
        this.form.controls.timeTicketHours.value as number);

      let decimalConvert = value.toFixed(2)
      this.form.controls.timeTicketAmount.patchValue(decimalConvert)
    }
  }

  getMatterDetails() {
    if (this.form.controls.matterNumber.value) {
      this.sub.add(this.matterService.Get(this.form.controls.matterNumber.value).subscribe(res => {
        this.form.controls.clientId.patchValue(res.clientId);
        this.multimatterList.forEach(matter => {
          if (matter.matterNumber == this.form.controls.matterNumber.value) {
            this.form.controls.clientName.patchValue(matter.clientName);
          }
        })
        if (res.billingFormatId == 10 || res.billingFormatId == 12) {
          this.ttbSelected = true
        } else {
          this.ttbSelected = false
        }

      }, err => {
        this.cs.commonerror(err);
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
    if (this.form.controls.timeTicketAmount.value == 0 && !this.form.controls.timeTicketAmount.value) {
      this.toastr.error(
        "Please fill time ticket amount to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      return;
    }

    if (this.form.controls.timerValidation.value == true) {
      this.toastr.error(
        "Please pause the timer before saving the time ticket",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      return;
    }

    if (this.ttbSelected == true && this.form.controls.activityCode.value == null && !this.form.controls.activityCode.value) {
      this.toastr.error(
        "Please fill activity code to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      return;
    }

    if (this.ttbSelected == true && this.form.controls.taskCode.value == null && !this.form.controls.taskCode.value) {
      this.toastr.error(
        "Please fill task code to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      return;
    }

    if(this.form.controls.timeTicketHours.value == 0){
      this.toastr.error(
        "Please enter time to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      return;
    }
    
    if (this.form.controls.billType.value == 'non-billable') {
      this.form.controls.defaultRate.patchValue(this.form.controls.defaultRatePerHour.value);

    } else {
      this.form.controls.defaultRatePerHour.patchValue(this.form.controls.defaultRate.value);
    }

    this.form.controls.stimeTicketDate.patchValue(this.cs.dateNewFormat(this.form.controls.stimeTicketDateFE.value));
    
   // this.form.controls.approvedBillableAmount.patchValue(this.form.controls.timeTicketAmount.value);
    this.cs.notifyOther(false);

    this.form.removeControl('updatedOn');
    this.form.removeControl('screatedOn');

    this.form.patchValue({ updatedby: this.auth.username });
    console.log(this.data.code)
    if (this.data.code && this.data.pageflow != 'Copy') {
      this.sub.add(this.service.Update(this.form.getRawValue(), this.data.code).subscribe(res => {
        this.toastr.success(this.data.code + " updated successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.dialogRef.close();
      }, err => {
        this.cs.commonerror(err);
      }));
    }
    else {
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.timeTicketNumber + " saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.dialogRef.close();

      }, err => {
        this.cs.commonerror(err);
      }));
    }
  };

  start() {
    this._start = true;
  }
  clear() {
    this._start = false;
  }

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }
  }
}

