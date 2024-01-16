import { SelectionModel } from '@angular/cdk/collections';
import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ReportsService } from '../reports.service';

export interface variant {
  
  
  no: string;
  actions: string;
  status: string;
  warehouseno: string;
  preinboundno: string;
  countno: string;
  by: string;
  damage: string;
  available: string;
  hold: string;
}
const ELEMENT_DATA: variant[] = [
  { no: "1", warehouseno: 'readonly', by: 'readonly', countno: 'readonly', damage: 'readonly', hold: 'readonly', available: 'readonly', preinboundno: 'readonly', status: 'readonly', actions: 's' },

];

@Component({
  selector: 'app-payment-due',
  templateUrl: './payment-due.component.html',
  styleUrls: ['./payment-due.component.scss']
})
export class PaymentDueComponent implements OnInit {

 form = this.fb.group({
  agreementNumber: [],
  civilId: [],
  customerCode: [],
 // endDate: [this.cs.todayapi()],
  phoneNumber: [],
  secondaryNumber: [],
  //startDate: [],
  storeNumber:[],
  dueStatus:[],
});
  isShowDiv = false;
  table = true;
  fullscreen = false;
  search = true;
  back = false;
  div1Function() {
    this.table = !this.table;
  }
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
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

  statusList: any[] = [];
  showFiller = false;
  displayedColumns: string[] = ['customerName','civilId',  'storeNumber', 'size', 'storageType','phase','rentPeriod' ,'dueStatus','lastPaidDate','dueDate','dueDays','lastPaidVoucherAmount','dueAmount','modeOfPayment','agreementStatus','startDate','endDate','phoneNumber'];
  sub = new Subscription();
  ELEMENT_DATA: variant[] = [];
  constructor(
    private router: Router, public toastr: ToastrService, private spin: NgxSpinnerService,
   
    private cas: CommonApiService,
    private fb: FormBuilder,
    public cs: CommonService,
    private service: ReportsService,
    public datepipe: DatePipe,
    ) { 


      
    this.statusList = [{
      label: 'Pending Due',
      value: 'Pending Due'
    },
    {
      label: 'No Dues',
      value: 'No Dues'
    }
  ];
    }
  animal: string | undefined;
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  RA: any = {};
  startDate: any;
  currentDate: Date;
  ngOnInit(): void {

    this.dropdownlist();
  //   this.currentDate = new Date();
  //   let yesterdayDate = new Date();
  //   let currentMonthStartDate = new Date();
  //   yesterdayDate.setDate(this.currentDate.getDate() - 1);
  //   this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
  //  currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
  // this.form.controls.startDate.patchValue(currentMonthStartDate);

   }

  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);

  customerIdList : any[] = [];
  agreementList : any[] = [];
  storenoList:any[]=[];

  dropdownlist() {

    this.cas.getalldropdownlist([
      this.cas.dropdownlist.trans.customerID.url,
      this.cas.dropdownlist.trans.agreement.url,
      this.cas.dropdownlist.trans.storageDropdown.url,
      
    ]).subscribe((results : any) => {
      this.customerIdList = this.cas.foreachlist2(results[0], this.cas.dropdownlist.trans.customerID.key);
      this.agreementList = this.cas.foreachlist1(results[1], this.cas.dropdownlist.trans.agreement.key);
      
      results[2].storageDropDown.forEach((x: any) => {
        this.storenoList.push({ value: x.storeNumber, label: x.storeId });
      })
     
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  @ViewChild(MatSort)
  sort!: MatSort;
  @ViewChild(MatPaginator)
  paginator!: MatPaginator; // Pagination
  // Pagination





  // filtersearch() {
  //   //  this.spin.show();
  //     this.table = true;
  //     this.search = true;
  //     this.fullscreen = true;


  // }
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
  checkboxLabel(row?: variant): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.warehouseno + 1}`;
  }



  clearselection(row: any) {

    this.selection.clear();
    this.selection.toggle(row);
  }

  onItemSelect(item: any) {
    console.log(item);
  }

  onSelectAll(items: any) {
    console.log(items);
  }
  submitted = false;
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
    this.spin.show();
    // this.form.controls.endDate.patchValue(this.cs.endDate(this.form.controls.endDate.value));
    // this.form.controls.startDate.patchValue(this.cs.startDate(this.form.controls.startDate.value));
    this.sub.add(this.service.getpaymentdue(this.form.getRawValue()).subscribe(res => {
      this.dataSource.data = res.agreementDetails;
      this.spin.hide()
      this.dataSource.paginator = this.paginator;
       this.dataSource.sort = this.sort;
      this.spin.hide();

      this.spin.hide();
      this.table = true;
      this.search = false;
      this.back = true;
      this.fullscreen = false;
    },
      err => {
        this.cs.commonerror1(err);
        this.spin.hide();
      }));
  }

  reset(){
    this.form.reset();
  }
  downloadexcel() {
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Customer Name": x.customerName,
        "Civil ID No": x.civilIdNumber,
        "Secondary No": x.secondaryNumber,
        'Start Date':this.cs.dateapi(x.startDate),
        'Location': x.location ,
        ' Store Number': x.storeNumber,
        "Size": x.size,
        "Storage type":x.storeType,
        "Phase":x.phase,
        "Rent":x.rent,
        "Rent Period":x.rentPeriod,
        "Total Rent":x.totalRent,
        "Last Paid Date":this.cs.dateapi(x.lastPaidDate),
        "Due Date":this.cs.dateapi(x.dueDate),
        "Due Days":x.dueDays,
        "Total Due Amount":x.totalDueAmount,
        "Next Due Amount":x.nextDueAmount,
        "Payment Terms":x.paymentTerms,
        "Discount if any":x.agreementDiscount,
        "Total after Discount":x.totalAfterDiscount,
       
      });

    })
    this.cs.exportAsExcel(res,"Payment-Due");
  }



}

