import { SelectionModel } from "@angular/cdk/collections";
import { DatePipe } from "@angular/common";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators } from "@angular/forms";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ReportsService } from "../reports.service";

  
  
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
  selector: 'app-employee-efficiency',
  templateUrl: './employee-efficiency.component.html',
  styleUrls: ['./employee-efficiency.component.scss']
})
export class EmployeeEfficiencyComponent implements OnInit {

  form = this.fb.group({
    endDate: [this.cs.todayapi(),Validators.required],
    jobCardType: [],
    processedBy: [],
    startDate: [,Validators.required],
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
  showFiller = false;
  displayedColumns: string[] = ['workOrderDate', 'processedBy', 'processedTime', 'leadTime', 'startDate', 'endDate','jobCardType',];
  sub = new Subscription();
  ELEMENT_DATA: variant[] = [];

  jobCardTypeList: any[] = []
  constructor(
    private router: Router, 
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cas: CommonApiService,
    private fb: FormBuilder,
    private service: ReportsService,
      public cs: CommonService,
      public datepipe: DatePipe,
    ) {

      this.jobCardTypeList = [
        {label: 'Villa', value: 'Villa'},
        {label: 'Flat Apartment', value: 'Flat Apartment'},
        {label: 'Warehouse', value: 'Warehouse'},
        {label: 'Commercial Space', value: 'Commercial Space'},
        {label: 'Others', value: 'Others'}
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
    this.dropdownlist()
    this.currentDate = new Date();
    let yesterdayDate = new Date();
    let currentMonthStartDate = new Date();
    yesterdayDate.setDate(this.currentDate.getDate() - 1);
    this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
   currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
  this.form.controls.startDate.patchValue(currentMonthStartDate);
   }



  

   workorderprocessedbyList : any[]=[];
  dropdownlist() {

    this.cas.getalldropdownlist([

      this.cas.dropdownlist.setup.workorderprocessedby.url,
    ]).subscribe((results) => {

      this.workorderprocessedbyList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.workorderprocessedby.key)
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);

 


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
    this.form.controls.endDate.patchValue(this.cs.endDate(this.form.controls.endDate.value));
    this.form.controls.startDate.patchValue(this.cs.startDate(this.form.controls.startDate.value));
    this.sub.add(this.service. getEmployeeEfficiencyTime(this.form.getRawValue()).subscribe(res => {
      this.dataSource.data = res;
      this.spin.hide()
      this.dataSource.paginator = this.paginator;
       this.dataSource.sort = this.sort;
      this.spin.hide();

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
  reset(){
    this.form.reset();
  }
  downloadexcel() {
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "WO Date": this.cs.dateapi(x.workOrderDate),
        "Processed by": x.processedBy,
        "Processed Time": x.processedTime,
        'Lead Time': x.leadTime,
        'Start Date Time': this.cs.dateapi(x.startDate) ,
        'End Date Time': this.cs.dateapi(x.endDate),
        "Job card Type": x.jobCardType,
       
      });

    })
    this.cs.exportAsExcel(res,"Employee-Efficiency");
  }

}
