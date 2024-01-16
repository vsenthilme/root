
  import { SelectionModel } from "@angular/cdk/collections";
import { DatePipe } from "@angular/common";
  import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators } from "@angular/forms";
  import { MatDialog } from "@angular/material/dialog";
  import { MatPaginator } from "@angular/material/paginator";
  import { MatSort } from "@angular/material/sort";
  import { MatTableDataSource } from "@angular/material/table";
  import { Router } from "@angular/router";
  import { IDropdownSettings } from "ng-multiselect-dropdown";
  import { NgxSpinnerService } from "ngx-spinner";
  import { ToastrService } from "ngx-toastr";
  import { forkJoin, of, Subscription } from "rxjs";
  import { catchError } from "rxjs/operators";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
  import { MasterService } from "src/app/shared/master.service";
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
    selector: 'app-lead-time',
    templateUrl: './lead-time.component.html',
    styleUrls: ['./lead-time.component.scss']
  })
  export class LeadTimeComponent implements OnInit {

    form = this.fb.group({
      jobCard: [],
      jobCardType: [],
      leadTime: [],
      plannedHours: [],
      processedBy: [],
      processedTime: [],
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
      remarks: [],
      startDate: [,Validators.required],
      startTime: [],
      status: [['WIP'],],
      updatedBy: [],
      updatedOn: [],
      workOrderDate: [],
      workOrderId: [],
      workOrderNumber: [],
      workOrderSbu: [],
      codeId: [],
      created: [],
      createdBy: [],
      createdOn: [],
      customerId: [],
      deletionIndicator: [],
      endDate: [this.cs.todayapi(),Validators.required],
      endTime: [, ],

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
    displayedColumns: string[] = ['workOrderDate','workOrderId','customerId','customerName','status', 'jobCard', 'jobCardType', 'startDate', 'endDate', 'processedBy','created','leadTime'];
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
     public datepipe: DatePipe,
      ) { }
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
      this.currentDate = new Date();
      let yesterdayDate = new Date();
      let currentMonthStartDate = new Date();
      yesterdayDate.setDate(this.currentDate.getDate() - 1);
      this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
     currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
    this.form.controls.startDate.patchValue(currentMonthStartDate);
     }

   workordercreatedbyList : any[] = [];
   workorderprocessedbyList : any[]=[];
   workOrderSbuList: any[] = [];
  dropdownlist() {

    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.workordercreatedby.url,
      this.cas.dropdownlist.setup.workorderprocessedby.url,
      this.cas.dropdownlist.setup.workorderstatus.url,
    ]).subscribe((results) => {
      this.workordercreatedbyList = this.cas.foreachlist(results[0], this.cas.dropdownlist.setup.workordercreatedby.key);
      this.workOrderSbuList = this.cas.foreachlist1(results[2], this.cas.dropdownlist.setup.workorderstatus.key);
      this.workorderprocessedbyList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.workorderprocessedby.key)
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
      
      this.form.controls.endDate.patchValue(this.cs.endDate(this.form.controls.endDate.value));
      this.form.controls.startDate.patchValue(this.cs.startDate(this.form.controls.startDate.value));
      this.spin.show();
      this.sub.add(this.service.getWorkOrderLeadTime(this.form.getRawValue()).subscribe(res => {
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
          "WO. Date":  this.cs.dateapiutc0(x.workOrderDate),
          "Job Card": x.jobCard,
          "Job card Type": x.jobCardType,
          'Start Date Time': this.cs.dateapiutc0(x.startDate) + ' ' +x.startTime,
          'End Date Time':this.cs.dateapiutc0(x.endDate)+ ' '+x.endTime,
          'Processed By': x.processedBy,
          "Created By": x.created,
          "Lead Time":x.leadTime,
          
         
        });
  
      })
      this.cs.exportAsExcel(res,"Lead-Time");
    }
  }
