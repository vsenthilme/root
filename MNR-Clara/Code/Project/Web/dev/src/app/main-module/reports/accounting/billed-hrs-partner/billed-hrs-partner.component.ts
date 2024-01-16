import { SelectionModel } from '@angular/cdk/collections';
import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { ReportServiceService } from '../../report-service.service';
import { PeriodicElement } from '../ar-report/ar-report.component';
import { TimekeeperService } from 'src/app/main-module/setting/business/timekeeper/timekeeper.service';

@Component({
  selector: 'app-billed-hrs-partner',
  templateUrl: './billed-hrs-partner.component.html',
  styleUrls: ['./billed-hrs-partner.component.scss']
})
export class BilledHrsPartnerComponent implements OnInit {

  
  screenid = 1193;
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

  multiSelectClientList: any[] = [];
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
      clientId: [[], ],
      partner: [[], Validators.required],
      fromPostingDate: [],
      fromPostingDateFE: [new Date("01/01/00 00:00:00")],
      fromTimeTicketDate: [],
   //   fromTimeTicketDateFE: [new Date("01/01/00 00:00:00")],
      fromTimeTicketDateFE: [new Date("01/01/00 00:00:00")],
      matterNumber: [[],],
      timeKeeperCode: [[],],
      toPostingDate:[, ],
      toPostingDateFE:[this.cs.todayapi(), Validators.required],
      toTimeTicketDate: [],
      toTimeTicketDateFE: [this.cs.todayapi()],
     // toTimeTicketDateFE: [this.cs.todayapi()],
  });

  displayedColumns: string[] = [
    'matterNumber', 
    'partner',
    'attorney',
    'invoiceNumber',
    'dateOfBill',
      'hoursBilled', 
      'amountBilled', 
      'approxHoursPaid', 
      'approxAmountReceived', 
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
    private auth: AuthService,
    private partner: TimekeeperService) { }
  RA: any = {};
  startDate: any;
  currentDate: Date;
    
  ngOnInit(): void {

    //this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllDropDown();
    this.currentDate = new Date();
    let yesterdayDate = new Date();
    let currentMonthStartDate = new Date();
    yesterdayDate.setDate(this.currentDate.getDate() - 1);
    this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
   currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
//  this.form.controls.fromTimeTicketDateFE.patchValue(currentMonthStartDate);
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
    this.form.controls.fromPostingDate.patchValue(this.cs.dateNewFormat(this.form.controls.fromPostingDateFE.value));
    this.form.controls.toPostingDate.patchValue(this.cs.dateNewFormat(this.form.controls.toPostingDateFE.value));
    this.form.controls.fromTimeTicketDate.patchValue(this.cs.dateNewFormat(this.form.controls.fromTimeTicketDateFE.value));
    this.form.controls.toTimeTicketDate.patchValue(this.cs.dateNewFormat(this.form.controls.toTimeTicketDateFE.value));
    console.log(this.form)
    this.spin.show();
    this.sub.add(this.service.getBilledPaidHoursPartner(this.form.getRawValue()).subscribe(res => {
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
  multiSelectMatterList: any[] = [];
  multitimekeeperList: any[] = [];
  timekeeperCodelist: any[] = [];
  
  multiPartnerList: any[] = [];
  
  getAllDropDown() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.matter.dropdown.url,
      this.cas.dropdownlist.setup.timeKeeperCode.url,
    ]).subscribe((results: any) => {
      results[0].clientNameList.forEach((x: any) => {
        this.multiSelectClientList.push({ value: x.key, label: x.key + '-' + x.value });
      }) 
      results[0].matterList.forEach((x: any) => {
        this.multiSelectMatterList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      this.timekeeperCodelist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.timekeeperCodelist.forEach((x: { key: string; value: string; }) => this.multitimekeeperList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselecttimekeeperList = this.multitimekeeperList;

      let filterPartner = results[1].filter(x => x.userTypeId == 1);
      filterPartner.forEach(x => this.multiPartnerList.push({ value: x.timekeeperCode, label: x.timekeeperCode + '-' + x.timekeeperName }))
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
    this.form.controls.clientId.patchValue([]);
    this.form.controls.matterNumber.patchValue([]);
    this.form.controls.timeKeeper.patchValue([]);
    this.form.controls.partner.patchValue([]);
  }

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        'Matter No': (x.matterNumber),
        'Attorney': x.attorney,
        'Partner': x.partner,
        'Invoice No': x.invoiceNumber,
        "Date of Bill": this.cs.excel_date(x.dateOfBill),
        'Hours Billed ': x.hoursBilled,
        'Amount Billed' : x.amountBilled,
        'Approximate Hours Paid' : x.approxHoursPaid,
        'Amount Received' : x.approxAmountReceived,
        
      });
    
    })
    res.push({
      'Matter No': '',
      'Attorney': '',
      'Invoice No': '',
      "Date of Bill": '',
      'Hours Billed ': this.hrbilled(),
      'Amount Billed' : this.amtbilled(),
      'Approximate Hours Paid' : this.apphrbilled(),
      'Amount Received' : this.totalamtbilled(),
      
    });
    this.excel.exportAsExcel(res, "Billed Hours Paid - Partner");
  }
  hrbilled() {
    let total = 0;
   this.dataSource.data.forEach(element => {
     total = total + (element.hoursBilled != null ? element.hoursBilled : 0);
   })
   return total;
    }
  amtbilled() {
    let total = 0;
   this.dataSource.data.forEach(element => {
     total = total + (element.amountBilled != null ? element.amountBilled : 0);
   })
   return total;
    }
  apphrbilled() {
    let total = 0;
   this.dataSource.data.forEach(element => {
     total = total + (element.approxHoursPaid != null ? element.approxHoursPaid : 0);
   })
   return total;
    }
  totalamtbilled() {
    let total = 0;
   this.dataSource.data.forEach(element => {
     total = total + (element.approxAmountReceived != null ? element.approxAmountReceived : 0);
   })
   return total;
    }


  fromPostingDate(fromValue){
this.form.controls.fromTimeTicketDateFE.patchValue(fromValue.value);
  }
  toPostingDate(toValue){
    this.form.controls.toTimeTicketDateFE.patchValue(toValue.value);
  }
}




