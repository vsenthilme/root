import {
  DatePipe,
  DecimalPipe
} from '@angular/common';
import {
  Component,
  Inject,
  OnInit
} from '@angular/core';
import {
  FormControl,
  FormBuilder,
  Validators
} from '@angular/forms';
import {
  MatDialogRef,
  MAT_DIALOG_DATA
} from '@angular/material/dialog';
import {
  NgxSpinnerService
} from 'ngx-spinner';
import {
  ToastrService
} from 'ngx-toastr';
import {
  Subscription
} from 'rxjs';
import {
  CommonApiService
} from 'src/app/common-service/common-api.service';
import {
  CommonService
} from 'src/app/common-service/common-service.service';
import {
  AuthService
} from 'src/app/core/core';
import {
  GeneralMatterService
} from 'src/app/main-module/matters/case-management/General/general-matter.service';
import {
  MatterRateService
} from 'src/app/main-module/matters/case-management/rate-list/matter-rate.service';
import {
  TimeTicketsService
} from 'src/app/main-module/matters/case-management/time-tickets/time-tickets.service';
import {
  TimekeeperService
} from 'src/app/main-module/setting/business/timekeeper/timekeeper.service';

@Component({
  selector: 'app-time-ticket-new',
  templateUrl: './time-ticket-new.component.html',
  styleUrls: ['./time-ticket-new.component.scss']
})
export class TimeTicketNewComponent implements OnInit {


  sub = new Subscription();
  email = new FormControl('', [Validators.required, Validators.email]);
  form = this.fb.group({
    classId: [this.auth.classId],
    createdBy: [this.auth.userID],
    deletionIndicator: [0],
    languageId: [this.auth.languageId],
    matterNumber: [, ],
    statusId: [],
    updatedBy: [this.auth.userID],
    activityCode: [],
    activityCodeFE: [],
    assignedRatePerHour: [],
    approvedBillableAmount: [,Validators.required],
    approvedBillableTimeInHours: [,Validators.required],
    approvedOn: [],
    assignedOn: [],
    clientName: [],
    assignedPartner: [],
    billType: ['billable', Validators.required],
    caseCategoryId: [],
    caseSubCategoryId: [],
    clientId: [],
    screatedOn: [],
    defaultRate: [, ],
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
    timeTicketAmount: [0, ],
    stimeTicketDate: [],
    stimeTicketDateFE: [],
    timeTicketDescription: [],
    timeTicketHours: [0,],
    timeTicketNumber: [],
    updatedOn: [],
  });


  submitted = false;
  public errorHandling = (control: string, error: string = "required") => {
    return this.form.controls[control].hasError(error) && this.submitted;
  }

  disabled = false;
  step = 0;

  panelOpenState = false;
  matter: any;

  statusIdlist: any[] = [];
  taskCodelist: any[] = [];
  timeKeeperCodelist: any[] = [];
  activityCodelist: any[] = [];

  selectedItems: any[] = [];
  multiselectactivityList: any[] = [];
  multiactivityList: any[] = [];

  selectedItems2: any[] = [];
  multiselecttaskList: any[] = [];
  multitaskList: any[] = [];


  multimatterList: any[] = [];
  multiMatterSelectList: any[] = [];

  constructor(
    public dialogRef: MatDialogRef < TimeTicketNewComponent > ,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private service: TimeTicketsService, private matterService: GeneralMatterService,
    public toastr: ToastrService, private TimekeeperServiceService: TimekeeperService,
    private cas: CommonApiService, private rateservice: MatterRateService,
    private spin: NgxSpinnerService, public datePipe: DatePipe,
    private auth: AuthService, private fb: FormBuilder,
    private cs: CommonService, private decimalPipe: DecimalPipe,
  ) {}



  ngOnInit(): void {
    console.log(this.data)
    this.dropdownlist();
  }
  multiTimeKeeperCodelist: any[] = [];
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
      this.taskCodelist.forEach((x: {
        key: string;value: string;
      }) => this.multitaskList.push({
        value: x.key,
        label: x.value
      }))
      this.multiselecttaskList = this.multitaskList;

      this.timeKeeperCodelist = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.timeKeeperCodelist.forEach((x: {
        key: string;value: string;
      }) => this.multiTimeKeeperCodelist.push({
        value: x.key,
        label: x.value
      }));
      this.statusIdlist = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.statusId.key);
      this.activityCodelist = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.activityCode.key);
      this.activityCodelist.forEach((x: {
        key: string;value: string;
      }) => this.multiactivityList.push({
        value: x.key,
        label: x.value
      }))
      this.multiselectactivityList = this.multiactivityList;

      if (this.form.controls.allmatters.value == true) {
        this.multiMatterSelectList = [];
        this.multimatterList = results[4].matterDropDown;

        this.multimatterList.forEach(matter => {
          this.multiMatterSelectList.push({
            value: matter.matterNumber,
            label: (matter.matterNumber + "-" + matter.matterDescription)
          });
          if ((this.data.approval.code.matter != null) && matter.matterNumber == this.data.approval.code.matter) {
            this.form.controls.clientName.patchValue(matter.clientName);
            this.form.controls.clientId.patchValue(matter.clientId);
          }
        })
      }
      if (this.form.controls.allmatters.value == false) {

        this.multiMatterSelectList = [];
        this.multimatterList = results[5].matterDropDown;
        this.multimatterList.forEach(matter => {
          this.multiMatterSelectList.push({
            value: matter.matterNumber,
            label: (matter.matterNumber + "-" + matter.matterDescription)
          });
          if ((this.data.approval.code.matter != null) && matter.matterNumber == this.data.approval.code.matter) {
            this.form.controls.clientName.patchValue(matter.clientName);
            this.form.controls.clientId.patchValue(matter.clientId);
          }
        })
      }
      this.form.controls.matterNumber.patchValue(this.data.approval.code.matterNumber);
      this.form.controls.approvedBillableAmount.disable();
      console.log(this.data.lines.timeKeeperCode)
      this.form.controls.timeKeeperCode.patchValue(this.data.lines.timeKeeperCode);
      this.form.controls.statusId.patchValue(this.data.lines.statusId);
      this.form.controls.stimeTicketDate.patchValue(this.cs.dateNewFormat(this.data.approval.code.preBillDate));
      this.form.controls.referenceField1.patchValue(this.data.lines.referenceField1);
      
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.form.controls.languageId.patchValue("EN");
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
    this.spin.show();
      this.sub.add(this.service.Create(this.form.getRawValue()).subscribe(res => {
        this.toastr.success(res.timeTicketNumber + " saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.dialogRef.close(res);
this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
      }));
  };


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


  calculateApprovedAmount() {
    if (this.form.controls.matterNumber.value && this.form.controls.timeKeeperCode.value) {
      //this.spin.show();
      this.sub.add(this.rateservice.Get(this.form.controls.timeKeeperCode.value, this.form.controls.matterNumber.value).subscribe(res => {
        let decimalConvert = res.assignedRatePerHour;
        this.form.controls.defaultRate.patchValue(res.assignedRatePerHour as number);
        this.form.controls.assignedRatePerHour.patchValue(res.assignedRatePerHour as number);
        this.form.controls.clientId.patchValue(res.clientId);
        if (this.form.controls.approvedBillableTimeInHours.value)
          this.form.controls.approvedBillableAmount.patchValue(this.form.controls.defaultRate.value as number *
            this.form.controls.approvedBillableTimeInHours.value as number);
     //   this.form.controls.approvedBillableAmount.patchValue(this.decimalPipe.transform(this.form.controls.approvedBillableAmount.value, "1.2-2"));
        //this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        //this.spin.hide();
      }));
    }
  }

  billtypeChange() {
    if (this.form.controls.billType.value == "non-billable" || this.form.controls.billType.value == "nocharge") {
      this.form.controls.approvedBillableAmount.patchValue(0);
    }
    if (this.form.controls.billType.value == "billable") {
      this.form.controls.approvedBillableAmount.patchValue((this.form.controls.assignedRatePerHour.value != null ? this.form.controls.assignedRatePerHour.value : 0) * this.form.controls.approvedBillableTimeInHours.value);
     // this.form.controls.approvedBillableAmount.patchValue(this.decimalPipe.transform(this.form.controls.approvedBillableAmount.value, "1.2-2"));
    }
  }
}
