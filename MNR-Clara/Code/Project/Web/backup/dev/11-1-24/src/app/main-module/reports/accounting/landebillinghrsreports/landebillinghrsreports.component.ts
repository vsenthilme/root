import { SelectionModel } from "@angular/cdk/collections";
import { DatePipe } from "@angular/common";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { ReportServiceService } from "../../report-service.service";
import { PeriodicElement } from "../ar-report/ar-report.component";

@Component({
  selector: 'app-landebillinghrsreports',
  templateUrl: './landebillinghrsreports.component.html',
  styleUrls: ['./landebillinghrsreports.component.scss']
})
export class LandebillinghrsreportsComponent implements OnInit {

 
  screenid = 1192;
    public icon = 'expand_more';
    isShowDiv = false;
    table = true;
    fullscreen = false;
    search = true;
    back = false;
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
  
    sub = new Subscription();
  
    multiSelectClassList: any[] = [];
    multiClientList: any[] = [];
  
    multiSelectDocumentType: any[] = [];
    multiDocumentType: any[] = [];
  thisDocumentType: any[] = [];
  
    selectedClassId: any[] = [];

    multiReferralList: any[] = [];
    selectedReferralId: any[] = [];
    submitted = false;
  
    multiMatterList: any[] = [];
    multiCaseSubList: any[] = [];
  
    form = this.fb.group({
      classId: [[1],],
  endDate: [],
  fromDate: [],
  startDate: [],
  timekeeperCode:[],
  toDate: [],
  toDateFE:[this.cs.todayapi(),],
  fromDateFE:[],
    });
  
    displayedColumns: string[] = [
      'timekeeperName',
      'expectedBillableHours',
      'billableHours', 
      'ratio',
      'billedHours',
      'nonBillableHours',
        'averageBillingRate', 
        'actualRates', 
        'expectedBillingBasedOnExpectedBillableHours', 
        'expectedBillingBasedOnLoggedBillableHours', 
        'billedFees',
        'expectedFeeAndActualFeeRatio'
    ];
    dataSource = new MatTableDataSource<any>();
    selection = new SelectionModel<PeriodicElement>(true, []);
  
    /** Whether the number of selected elements matches the total number of rows. */
    isAllSelected() {
      const numSelected = this.selection.selected.length;
      const numRows = this.dataSource.data.length;
      return numSelected === numRows;
    }
  
    /** Selects all rows if they are not all selected; otherwise clear selection. */
    masterToggle() {
      if (this.isAllSelected()) {
        this.selection.clear();
        return;
      }
  
      this.selection.select(...this.dataSource.data);
    }
  
    /** The label for the checkbox on the passed row */
    checkboxLabel(row?: PeriodicElement): string {
      if (!row) {
        return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
      }
      return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.name + 1}`;
    }
    constructor(
      public dialog: MatDialog,
      private service: ReportServiceService,
      private cs: CommonService,
      private spin: NgxSpinnerService,
      private excel: ExcelService,
      private fb: FormBuilder,
      public toastr: ToastrService,
      public datepipe: DatePipe,
      private cas: CommonApiService,
      private auth: AuthService) { }
    RA: any = {};
    startDate: any;
    currentDate: Date;
      
    ngOnInit(): void {
  
      this.RA = this.auth.getRoleAccess(this.screenid);
      this.getAllDropDown();
      this.currentDate = new Date();
      let yesterdayDate = new Date();
      let currentMonthStartDate = new Date();
      yesterdayDate.setDate(this.currentDate.getDate() - 1);
      this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
     currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
    this.form.controls.fromDateFE.patchValue(currentMonthStartDate);
    }
    ngAfterViewInit() {
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
    @ViewChild(MatPaginator) paginator: MatPaginator;
   @ViewChild(MatSort) sort: MatSort;
    applyFilter(event: Event) {
      const filterValue = (event.target as HTMLInputElement).value;
      this.dataSource.filter = filterValue.trim().toLowerCase();
  
      if (this.dataSource.paginator) {
        this.dataSource.paginator.firstPage();
      }
    }
    filtersearch() {
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
      this.form.controls.fromDate.patchValue(this.cs.dateNewFormat1(this.form.controls.fromDateFE.value));
      this.form.controls.toDate.patchValue(this.cs.dateNewFormat1(this.form.controls.toDateFE.value));
      this.spin.show();
      this.sub.add(this.service.getlandebillinghr(this.form.getRawValue()).subscribe(res => {
        this.dataSource.data = res;
        this.spin.hide()
        this.dataSource.paginator = this.paginator;
         this.dataSource.sort = this.sort;
        this.spin.hide();
       // this.dataSource.data.forEach((data: any) => {
       //   data.potentialClientId = this.multiMatterList.find(y => y.value == data.potentialClientId)?.label;
       // })
        this.spin.hide();
        this.table = true;
        this.search = false;
        this.back = true;
      },
        err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));
    }
    togglesearch() {
      this.search = false;
      this.table = true;
      this.fullscreen = false;
      this.back = true;
    }
    backsearch() {
      this.table = true;
      this.search = true;
      this.fullscreen = true;
      this.back = false;
    }
  
    multiselecttimekeeperList: any[] = [];
    multiSelectCaseCatList: any[] = [];
    multiSelectCaseSubCatList: any[] = [];
    multitimekeeperList: any[] = [];
    timekeeperCodelist: any[] = [];
    
    getAllDropDown() {
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.setup.timeKeeperCode.url,
      ]).subscribe((results: any) => {
        this.timekeeperCodelist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.timeKeeperCode.key, { classId: [1] });
        this.timekeeperCodelist.forEach((x: { key: string; value: string; }) => this.multitimekeeperList.push({ value: x.key, label: x.key + '-' + x.value }))
        this.multiselecttimekeeperList = this.multitimekeeperList;
      }, (err) => {
        this.toastr.error(err, "");
      });
    }
  
  
    public errorHandling = (control: string, error: string = "required") => {
      return this.form.controls[control].hasError(error) && this.submitted;
    }
    
  email = new FormControl('', [Validators.required, Validators.email]);
    getErrorMessage() {
      if (this.email.hasError('required')) {
        return ' Field should not be blank';
      }
      return this.email.hasError('email') ? 'Not a valid email' : '';
    }
    reset() {
      this.form.reset();
      this.form.controls.classId.patchValue([1]);
      this.form.controls.timekeeperCode.patchValue([]);
    }
  
    downloadexcel() {
      // if (excel)
      var res: any = [];
      this.dataSource.data.forEach(x => {
        res.push({
          'TimeKeeper Name':x.timekeeperName,
          'Expected Billable Hours':x.expectedBillableHours,
          'Billable Hours': x.billableHours,
          'Ratio': x.ratio,
          'Billed Hours ': x.billedHours,
          'Non-Billable Hours' : x.nonBillableHours,
          'Default Billing Rate' : x.averageBillingRate,
          'Average Billing Rate' : x.actualRates,
          'Expected Billing Based on the Exp Billable hrs' : x.expectedBillingBasedOnExpectedBillableHours,
          'Expected Billing Based on the Logged Billable hrs' : x.expectedBillingBasedOnLoggedBillableHours,
          'Billed Fees':x.billedFees,
          'Ration of Billed and Actual Fees':x.expectedFeeAndActualFeeRatio
  
        });
  
      })
      res.push({
        'TimeKeeper Name':'',
        'Expected Billable Hours':this.expectedbillablehrs( ),
        'Billable Hours': this.billablehrs(),
        'Ratio': '',
        'Billed Hours ': this.billhrs(),
        'Non-Billable Hours' : this.nonbillhrs(),
        'Default Billing Rate' : '',
        'Average Billing Rate' : '',
        'EXP' : this.exp(),
        'EBP' : this.ebp(),
        'Billed Fees':this.billedfee(),
        'Ration of Billed and Actual Fees':'',

      });
      this.excel.exportAsExcel(res, "L&E Billing Hrs");
    }
    billhrs() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.billedHours != null ? element.billedHours : 0);
     })
     return total;
      }
    nonbillhrs() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.nonBillableHours != null ? element.nonBillableHours : 0);
     })
     return total;
      }
    billablehrs() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.billableHours != null ? element.billableHours : 0);
     })
     return total;
      }
    expectedbillablehrs() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.expectedBillableHours != null ? element.expectedBillableHours : 0);
     })
     return total;
      }
    nocharhrs() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.noCharge != null ? element.noCharge : 0);
     })
     return total;
      }
    totalhrs() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.totalHours != null ? element.totalHours : 0);
     })
     return total;
      }
      defaultrate() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.actualRates != null ? element.actualRates : 0);
     })
     return total;
      }
    avgbillingrate() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.averageBillingRate != null ? element.averageBillingRate : 0);
     })
     return total;
      }
    exp() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.expectedBillingBasedOnExpectedBillableHours != null ? element.expectedBillingBasedOnExpectedBillableHours : 0);
     })
     return total;
      }
    ebp() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.expectedBillingBasedOnLoggedBillableHours != null ? element.expectedBillingBasedOnLoggedBillableHours : 0);
     })
     return total;
      }
    billedfee() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.billedFees != null ? element.billedFees : 0);
     })
     return total;
      }
  }
  
  
  
  
