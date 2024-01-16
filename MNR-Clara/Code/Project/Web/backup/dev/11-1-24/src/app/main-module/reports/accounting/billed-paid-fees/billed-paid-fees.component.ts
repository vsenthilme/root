
  import { SelectionModel } from "@angular/cdk/collections";
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
    selector: 'app-billed-paid-fees',
    templateUrl: './billed-paid-fees.component.html',
    styleUrls: ['./billed-paid-fees.component.scss']
  })
  export class BilledPaidFeesComponent implements OnInit {
  
  
    screenid = 1177;
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
        month: [new Date().getMonth() + 1, Validators.required],
        caseCategoryId: [[],  Validators.required],
        timeKeeperCode: [[],],
        year:[new Date().getFullYear(), Validators.required],
    });
  
    displayedColumns: string[] = [
      'select',
      'caseCategory', 
      'caseSubCategory',
      'timekeeperCode',
      'monthlyBilledAmount',
        'monthlyBilledPercentage', 
        'monthlyPaidAmount', 
        'monthlyPaidPercentage', 
        'yearlyBilledAmount', 
        'yearlyBilledPercentage', 
        'yearlyPaidAmount',
        'yearlyPaidPercentage',
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
      private auth: AuthService) { }
    RA: any = {};
      
    ngOnInit(): void {
    this.months();
    this.yearValue();
      this.RA = this.auth.getRoleAccess(this.screenid);
      this.getAllDropDown();
      
    }

    monthsList: any[] = [];
    yearValueList: any[] = [];
    months() {
      let monthData = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
      monthData.forEach((element, index) => {
        this.monthsList.push({value: index + 1, label: element })
      })
      return this.monthsList;
    }
   yearValue() {
      for (let i = 2010; i < new Date().getFullYear() + 1; i++) {
        this.yearValueList.push({value: i, label: i })
      }
     
      this.yearValueList.sort((a, b) => (a.value < b.value) ? 1 : -1);
    }

    // yearValue() {
    //   for (let i = new Date().getFullYear(); i > 2010; i--) {
    //     this.yearValueList.push({value: i, label: i })
    //   }
    //   console.log(this.yearValueList)
    //   this.yearValueList.sort((a, b) => (a.value > b.value) ? 1 : -1);
    // }

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
      this.spin.show();
      this.sub.add(this.service.getBilledFeesPaid(this.form.getRawValue()).subscribe(res => {
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
    multiSelectCaseCategoryList: any[] = [];
    multitimekeeperList: any[] = [];
    timekeeperCodelist: any[] = [];
    
    getAllDropDown() {
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.matter.dropdown.url,
        this.cas.dropdownlist.setup.timeKeeperCode.url,
      ]).subscribe((results: any) => {
        results[0].caseCategoryList.forEach((x: any) => {
          this.multiSelectCaseCategoryList.push({ value: x.key, label: x.key + '-' + x.value });
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
      this.form.controls.month.patchValue(new Date().getMonth() + 1);
      this.form.controls.year.patchValue(new Date().getFullYear());
      this.form.controls.clientId.patchValue([]);
      this.form.controls.matterNumber.patchValue([]);
      this.form.controls.timeKeeper.patchValue([]);
    }
  
    downloadexcel() {
      // if (excel)
      var res: any = [];
      this.dataSource.data.forEach(x => {
        res.push({
          'Class': (x.classId == 1 ? 'L&E' : 'Immigration'),
          'Case Category': x.caseCategory,
          'Case Sub Category': x.caseSubCategory,
          'Timekeeper ': x.timekeeperCode,
          ' Billed' : x.monthlyBilledAmount,
          'Billed %' : x.monthlyBilledPercentage,
          'Paid' : x.monthlyPaidAmount,
          'Paid %' : x.monthlyPaidPercentage,
          "Billed Year":x.yearlyBilledAmount,
          "Billed % Year":x.yearlyBilledPercentage,
          "Paid Year":x.yearlyPaidAmount,
          "Paid % Year":(x.yearlyPaidPercentage),
        });
  
      })
      this.excel.exportAsExcel(res, "Billed Paid fees");
    }
  
  }
  
  
  
  

