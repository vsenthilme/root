import { Component, OnInit, ViewChild } from '@angular/core';
import { InvoiceComponent } from './invoice/invoice.component';
import { SelectionModel } from '@angular/cdk/collections';
import { DatePipe } from '@angular/common';
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

@Component({
  selector: 'app-immigration-payment',
  templateUrl: './immigration-payment.component.html',
  styleUrls: ['./immigration-payment.component.scss']
})
export class ImmigrationPaymentComponent implements OnInit {

screenid = 1189;
public icon = 'expand_more';
isShowDiv = false;
table = true;
fullscreen = false;
search = true;
back = false;
showFloatingButtons: any;
toggle = true;
submitted: boolean;
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


form = this.fb.group({
 fromDueDateFE:             [new Date("01/01/00 00:00:00"), ],
 toDueDateFE:               [this.cs.todayapi(), Validators.required],
 clientNumber: [],
 fromDueDate: [],
 fromRemainderDate: [],
 fromRemainderDateFE: [],
 matterNumber: [],
 toDueDate: [],
 toRemainderDate: [],
 toRemainderDateFE: [],

});

displayedColumns1: string[] = [
  'clientId', 
  'matterNumber',
  'quoteNumber',
    'paymentPlanAmount', 
    'instalmentAmount', 
    'invoice', 
    'startDate', 
    'paymentPlanDate', 
    'status', 
    'dueDate', 
    'remainderDate', 
    'clientPhoneNumber', 
    'clientWorkNumber', 
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
dispList: any[] = [];

selectedDisplay: any[] = [];
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
  private auth: AuthService) {

    this.dispList = [
    {label: 'Client ID', value: 'clientId'  },  
    {label: 'Matter No', value: 'matterNumber' },
    {label: 'Quote No', value: 'quoteNumber'  },
    {label: 'Payment Plan Amount',   value: 'paymentPlanAmount' },
    {label: 'Instalment Amount', value: 'instalmentAmount'  },
    {label: 'Invoice', value: 'invoice'  },
    {label: 'Start Date', value: 'startDate'  },
    {label: 'Payment Plan Date', value: 'paymentPlanDate'  },
    {label: 'Status', value: 'status'  },
    {label: 'Upcoming Due Date', value: 'dueDate'  },
    {label: 'Upcoming Reminder Date', value: 'remainderDate'  },
    {label: 'Client Cell No', value: 'clientPhoneNumber'  },
    {label: 'Client Work No', value: 'clientWorkNumber'  },
  ];
  this.dispList.map((value) => this.selectedDisplay.push(value.value));

   }

RA: any = {};
startDate: any;
currentDate: Date;
displayedColumns: any[] = [];
ngOnInit(): void {

  this.RA = this.auth.getRoleAccess(this.screenid);
  this.getAllDropDown();
  this.currentDate = new Date();
  let yesterdayDate = new Date();
  let currentMonthStartDate = new Date();
  yesterdayDate.setDate(this.currentDate.getDate() - 1);
  this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
 currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
this.form.controls.fromDueDateFE.patchValue(currentMonthStartDate);
console.log(this.selectedDisplay)
this.displayedColumns = this.selectedDisplay;
}

displayChange(e){
  console.log(e)
  this.displayedColumns = [];
  this.displayedColumns = (e.value);
  console.log(this.displayedColumns)
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
  this.form.controls.fromDueDate.patchValue(this.cs.dateNewFormat1(this.form.controls.fromDueDateFE.value));
  this.form.controls.toDueDate.patchValue(this.cs.dateNewFormat1(this.form.controls.toDueDateFE.value));

  this.spin.show();
  this.sub.add(this.service.getImmigtationPayment(this.form.getRawValue()).subscribe(res => {
    this.dataSource.data = res;
    this.spin.hide()
    this.dataSource.paginator = this.paginator;
     this.dataSource.sort = this.sort;
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

multiSelectClientList: any[] = [];
multiSelectMatterList: any[] = [];
getAllDropDown() {
  this.cas.getalldropdownlist([
    this.cas.dropdownlist.matter.dropdown.url,
  ]).subscribe((results: any) => {
    results[0].clientNameList.forEach((x: any) => {
      this.multiSelectClientList.push({ value: x.key, label: x.key + '-' + x.value });
    })
    results[0].matterList.forEach((x: any) => {
      this.multiSelectMatterList.push({ value: x.key, label: x.key + '-' + x.value });
    })
   

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
}

downloadexcel() {
  // if (excel)
  var res: any = [];
  this.dataSource.data.forEach(x => {
    res.push({
      'Client ID': x.clientId,
      'Matter No': x.matterNumber,
      'Quote No': x.quoteNumber,
      'Pyament Plan Amount': x.paymentPlanAmount,
      'Instalment  Amount': x.instalmentAmount,
      'Start Date   ': this.cs.dateapi(x.startDate),
      'Payment Plan Date' :  this.cs.dateapi(x.paymentPlanDate),
      'Status ' : x.status,
      'Upcoming Due Date' : this.cs.dateapi(x.dueDate),
      'Upcoming Reminder Date': this.cs.dateapi(x.remainderDate),
      'Client Phone No ' : x.clientPhoneNumber,
      'Client Work No ' : x.clientWorkNumber,
    });

  })
  this.excel.exportAsExcel(res, "Immigration Payment Plan");
}



openDialog(element): void {
  const dialogRef = this.dialog.open(InvoiceComponent, {
    disableClose: true,
    width: '70%',
    maxWidth: '80%',
    position: { top: '6.5%' },
    data: element
  });

  dialogRef.afterClosed().subscribe(result => {
  });
}

}





