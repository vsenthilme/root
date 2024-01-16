
  import { SelectionModel } from "@angular/cdk/collections";
import { DatePipe } from "@angular/common";
  import { Component, OnInit, ViewChild } from "@angular/core";
  import { FormBuilder, Validators } from "@angular/forms";
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
  import { PrebillService } from "src/app/main-module/accounts/prebill/prebill.service";
import { ReportServiceService } from "../../report-service.service";
  
  export interface PeriodicElement {
    name: string;
    email: string;
    attorney: string;
    clientno: string;
    inquiry: string;
    date: string;
    by: string;
    followup: string;
    notes: string;
  }

  @Component({
    selector: 'app-client-cash-recipt',
    templateUrl: './client-cash-recipt.component.html',
    styleUrls: ['./client-cash-recipt.component.scss']
  })
  export class ClientCashReciptComponent implements OnInit {
  
    screenid = 1180;
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
      clientId:  [[],],
     fromDate: [, ],
     toDate: [,],
     fromDateFE: [new Date("01/01/00 00:00:00"), ],
     toDateFE: [this.cs.todayapi(), Validators.required],
     matterNumber: [[], ],
    });
  
    displayedColumns: string[] = [
      'select',
      'clientId', 
      'clientName',
      'matterNumber',
      'matterText',
        'paymentDate', 
     'paymentAmount',
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
      private cas: CommonApiService,
      public datepipe: DatePipe,
      private auth: AuthService) { }
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
      this.form.controls.fromDate.patchValue(this.cs.dateNewFormat(this.form.controls.fromDateFE.value));
      this.form.controls.toDate.patchValue(this.cs.dateNewFormat(this.form.controls.toDateFE.value));
      this.spin.show();
      this.sub.add(this.service.getClientCash(this.form.getRawValue()).subscribe(res => {
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
    multitimekeeperList: any[] = [];
    timekeeperCodelist: any[] = [];
    
    getAllDropDown() {
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.matter.dropdown.url,
      ]).subscribe((results: any) => {
        results[0].clientNameList.forEach((x: any) => {
          this.multiSelectClientList.push({ value: x.key, label: x.key + '-' + x.value });
        })
        results[0].matterList.forEach((x: any) => {
          this.multiMatterList.push({ value: x.key, label: x.key + '-' + x.value });
        })
  
      }, (err) => {
        this.toastr.error(err, "");
      });
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
    reset() {
      this.form.reset();
    }
  
    downloadexcel() {
      // if (excel)
      var res: any = [];
      this.dataSource.data.forEach(x => {
        res.push({
          'Client Id': x.clientId,
          'Client Name': x.clientName,
          'Matter No': x.matterNumber,
          'Matter Description': x.matterText,
          'Date' : this.cs.dateapiutc0(x.paymentDate),
          "Amount ": x.paymentAmount,
  
        });
  
      })
      res.push({
        'Client Id': '',
        'Client Name': '',
        'Matter No': '',
        'Matter Description': '',
        'Date' : '',
        "Amount ": this.totalamt(),

      });

      this.excel.exportAsExcel(res, "Client Cash Receipt");
    }
    totalamt() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.paymentAmount != null ? element.paymentAmount : 0);
     })
     return total;
      }
  
  }
  
  
  
  