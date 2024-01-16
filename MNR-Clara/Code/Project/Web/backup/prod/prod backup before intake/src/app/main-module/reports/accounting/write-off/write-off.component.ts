

  


  
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
    selector: 'app-write-off',
    templateUrl: './write-off.component.html',
    styleUrls: ['./write-off.component.scss']
  })
  export class WriteOffComponent implements OnInit {
  
  
    screenid = 1182;
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
        clientId: [[], Validators.required],
        fromDate: [],
        fromDateFE: [new Date("01/01/00 00:00:00")],
        matterNumber: [[],],
        timeKeeperCode: [[],],
        toDate: [, ],
        toDateFE: [this.cs.todayapi(), Validators.required],
    });
  
    displayedColumns: string[] = [
      'select',
      'clientId', 
      'clientName',
      'matterNumber',
      'matterText',
        'date', 
        'responsibleTimeKeeper', 
        'fees', 
        'hardCosts', 
        'softCosts', 
        'taxes', 
        'lateCharges', 
        'total', 
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
      this.sub.add(this.service.getWriteOff(this.form.getRawValue()).subscribe(res => {
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
    }
  
    downloadexcel() {
      // if (excel)
      var res: any = [];
      this.dataSource.data.forEach(x => {
        res.push({
          'Client ID': x.clientId,
          'Client Name': x.clientName,
          'Matter No': x.matterNumber,
          'Matter Description': x.matterText,
          'Date' : this.cs.dateapi(x.date),
          'Responsible Time Keeper' : x.responsibleTimeKeeper,
          'Fees' : x.fees,
          'Hard Costs' : x.hardCosts,
          "Soft Costs":x.softCosts,
          "Taxes": x.taxes,
          "Late Charges":x.lateCharges,
          "Total":x.total,
        });
  
      })
      res.push({
        'Client ID': '',
        'Client Name': '',
        'Matter No': '',
        'Matter Description': '',
        'Date' : '',
        'Responsible Time Keeper' : '',
        'Fees' : this.fees(),
        'Hard Costs' : this.hardcost(),
        "Soft Costs": this.softcost(),
        "Taxes": this.taxes(),
        "Late Charges": this.latecharges(),
        "Total": this.tamt(),
      });
      this.excel.exportAsExcel(res, "Write Off");
    }
    fees() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.fees != null ? element.fees : 0);
     })
     return total;
      }
    hardcost() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.hardCosts != null ? element.hardCosts : 0);
     })
     return total;
      }
    softcost() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.softCosts != null ? element.softCosts : 0);
     })
     return total;
      }
    taxes() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.taxes != null ? element.taxes : 0);
     })
     return total;
      }
    latecharges() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.lateCharges != null ? element.lateCharges : 0);
     })
     return total;
      }
   tamt() {
      let total = 0;
     this.dataSource.data.forEach(element => {
       total = total + (element.total != null ? element.total : 0);
     })
     return total;
      }
  }
  
  
  
  
