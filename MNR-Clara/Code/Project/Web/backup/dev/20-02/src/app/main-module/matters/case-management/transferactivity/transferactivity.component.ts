
import { SelectionModel } from '@angular/cdk/collections';
import { DecimalPipe, DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Validators } from 'ngx-editor';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { defaultStyle } from 'src/app/config/customStyles';
import { AuthService } from 'src/app/core/core';
import { GeneralMatterService } from '../General/general-matter.service';

@Component({
  selector: 'app-transferactivity',
  templateUrl: './transferactivity.component.html',
  styleUrls: ['./transferactivity.component.scss']
})
export class TransferactivityComponent implements OnInit {

  screenid = 1186;
  public icon = 'expand_more';
  isShowDiv = false;
  fullscreen = false;
  search = true;
  back = false;
  showFloatingButtons: any;
  toggle = true;
  arBalance1: any;
  plusOneDate: Date;
  matterDescription: any;
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

  form = this.fb.group({
    toMatterNumber: [, [Validators.required]],
    fromMatterNumber: [, [Validators.required]],
    fromClientName: [, ],
    toClientName: [, ],
    fromDateRange: [],
    toDateRange: [],
  });


  displayedColumns1: string[] = ['paymentDate', 'transactionType', 'paymentNumber', 'paymentCode', 'paymentAmount',];
  dataSource1 = new MatTableDataSource<any>([]);
  displayedColumns: string[] = [ 'invoiceNumber', 'preBillNumber', 'matterNumber', 'invoiceDate','partnerAssigned','invoiceAmount', 'totalPaidAmount', 'remainingBalance'];
  dataSource = new MatTableDataSource<any>([]);
  selection = new SelectionModel<any>(true, []);


  RA: any = {};
  submitted = false;
  sub = new Subscription();
  matterClientList: any[] = [];
  matterSelectList: any[] = [];

  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination
  

  userId = this.auth.userID;

  constructor(
    public dialog: MatDialog,
    private auth: AuthService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    private fb: FormBuilder,
    private decimalPipe: DecimalPipe,
    private excel: ExcelService,
    public toastr: ToastrService,
    public service: GeneralMatterService,
    private cas: CommonApiService,
    private datePipe: DatePipe
  ) { }

  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllDropDown();

  }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  getAllDropDown() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.matter.matterNumberList.url,
    ]).subscribe((results: any) => {
      this.spin.hide();
      this.matterClientList = results[0].matterDropDown;
      this.matterClientList.forEach(matter => {
        this.matterSelectList.push({ value: matter.matterNumber, label: (matter.matterNumber + "-" + matter.matterDescription), client: matter.clientName, });
      });
    });
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
    // this.plusOneDate = new Date(this.form.controls.toPostingDate.value);
    // this.plusOneDate.setDate(this.plusOneDate.getDate() + 1);

    // this.form.controls.toPostingDate1.patchValue(this.plusOneDate)

    let obj: any = {};

    obj.fromDateRange = this.cs.dateNewFormat(this.form.controls.fromDateRange.value);
    obj.toDateRange = this.cs.dateNewFormat(this.form.controls.toDateRange.value);
    obj.fromMatterNumber = this.form.controls.fromMatterNumber.value;
    obj.toMatterNumber = this.form.controls.toMatterNumber.value;

    this.spin.show();
    this.sub.add(this.service.getTransferMatterBillingActivityReport(obj).subscribe(res => {
      this.spin.hide();
      this.dataSource.data = res.invoiceHeaders;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;

      
      this.dataSource1.data = res.paymentUpdates;
      this.dataSource1.paginator = this.paginator;
      this.dataSource1.sort = this.sort;

      this.search = false;
      this.back = true;
    },
      err => {
        this.submitted = false;
        this.cs.commonerror(err);
        this.spin.hide();
      }));
  }

  public errorHandling = (control: string, error: string = "required") => {

    if (control.includes('.')) {
      const controls = this.form.get(control);
      return controls ? controls.hasError(error) : false;

    }
    return this.form.controls[control].hasError(error);
  }
  getErrorMessage(type: string) {
    if (!this.form.valid && this.submitted) {
      if (this.form.controls[type].hasError('required')) {
        return 'Field should not be blank';
      } else {
        return '';
      }
    } else {
      return '';
    }
  }

  togglesearch() {
    this.search = false;
    this.fullscreen = false;
    this.back = true;
  }
  backsearch() {
    this.search = true;
    this.fullscreen = true;
    this.back = false;
 
  }
  reset() {
    this.form.reset();
  }

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
  checkboxLabel(row?: any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.name + 1}`;
  }

  onItemSelect(item) {
    this.matterSelectList.forEach(x => {
      if(x.value == item.value){
        this.form.controls.fromClientName.patchValue(x.client);
      }
    })
  }
  onItemSelect1(item) {
    this.matterSelectList.forEach(x => {
      if(x.value == item.value){
        this.form.controls.toClientName.patchValue(x.client);
      }
    })
  }

 


  downloadexcel() {
  }

}

