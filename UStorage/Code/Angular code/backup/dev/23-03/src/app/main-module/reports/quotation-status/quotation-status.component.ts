import { SelectionModel } from '@angular/cdk/collections';
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
  selector: 'app-quotation-status',
  templateUrl: './quotation-status.component.html',
  styleUrls: ['./quotation-status.component.scss']
})
export class QuotationStatusComponent implements OnInit {


  form = this.fb.group({
    quoteId: [],
     requirementType: [],
     sbu:[,],
     status: [['Open'],],
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
  statusList: any[] = []
  showFiller = false;
  displayedColumns: string[] = ['quotationDate','sbu', 'requirementType', 'enquiryReferenceNumber', 'quoteId', 'customerCode', 'customerName', 'mobileNumber','rent', 'status','notes'];
  sub = new Subscription();
  ELEMENT_DATA: variant[] = [];
  constructor(
    private router: Router, 
    public toastr: ToastrService, 
    private spin: NgxSpinnerService,
    private cas: CommonApiService,
    private fb: FormBuilder,
    public cs: CommonService,
    private service: ReportsService,
    ) {
      this.statusList = [
        {label: 'Open', value: 'Open'},
        {label: 'Agreement Created', value: 'Agreement Created'},
        {label: 'Rejected', value: 'Rejected '},
       
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
  ngOnInit(): void {
    this.dropdownlist();
   }

  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);

  quoteIdByList : any[] = [];
  sbuList : any[] = [];
  requirementList : any[] = [];

  dropdownlist() {

    this.cas.getalldropdownlist([
      this.cas.dropdownlist.trans.quoteIdBy.url,
      this.cas.dropdownlist.setup.requirementtype.url,
      this.cas.dropdownlist.setup.sbu.url,
      
    ]).subscribe((results) => {
      this.quoteIdByList = this.cas.foreachlist1(results[0], this.cas.dropdownlist.trans.quoteIdBy.key);
      this.requirementList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.requirementtype.key);
     this.sbuList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.sbu.key);
     
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
      this.sub.add(this.service.getquoteIdTime(this.form.getRawValue()).subscribe(res => {
        this.dataSource.data = res;
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
          this.cs.commonerror(err);
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
          "Date": this.cs.dateapiutc0(x.quotationDate),
          "SBU": x.sbu,
          "Requirement Type": x.requirementType,
          'Enquiry No': x.enquiryReferenceNumber,
          'Quote ID': x.quoteId,
          'Customer ID': x.customerCode,
          "Customer Name": x.customerName,
          "Phone No":x.mobileNumber,
          "Value":x.rent,
          "Status":x.status,
          "Notes":x.notes,
          
        });
  
      })
      this.cs.exportAsExcel(res,"Quotation-Status");
    }


}


