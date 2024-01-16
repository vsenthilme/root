




  import { SelectionModel } from "@angular/cdk/collections";
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
  selector: 'app-expiration-report',
  templateUrl: './expiration-report.component.html',
  styleUrls: ['./expiration-report.component.scss']
})
export class ExpirationReportComponent implements OnInit {

  screenid = 1172;
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

  multiSelectClassList: any[] = [];
  multiClassList: any[] = [];

  multiSelectDocumentType: any[] = [];
  multiDocumentType: any[] = [];
thisDocumentType: any[] = [];

  selectedClassId: any[] = [];

  multiClientList: any[] = [];
  multiReferralList: any[] = [];
  selectedReferralId: any[] = [];
  submitted = false;

  multMatterList: any[] = [];


  form = this.fb.group({
    caseCategoryId: [[],],
    caseSubCategoryId: [[],],
    fromEligibilityDate: [],
    fromEligibilityDateFE: [,],
    documentType: [],
    fromExpirationDate: [, ],
    fromExpirationDateFE: [,],
    timeKeeperCode: [[],],
    toEligibilityDate: [],
    toEligibilityDateFE: [,],
    toExpirationDate: [, ],
    toExpirationDateFE: [],
    receiptNumber: [[],],
  });

  displayedColumns: string[] = [
    'select',
    'clientName', 
    'matterText',
    'employerPetitionerName',
    'docType',
    'eligibilityDate',
      'approvalDate', 
   'expirationDate',
   'originatingTk',
   'responsibleTk',
     'assignedTk', 
  'paralegal',
  'legalAssistant',
  'receiptNumber'
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

    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllDropDown();
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort)
  sort: MatSort;
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
    this.form.controls.fromExpirationDate.patchValue(this.cs.dateNewFormat(this.form.controls.fromExpirationDateFE.value));
    this.form.controls.fromEligibilityDate.patchValue(this.cs.dateNewFormat(this.form.controls.fromEligibilityDateFE.value));
    this.form.controls.toEligibilityDate.patchValue(this.cs.dateNewFormat(this.form.controls.toEligibilityDateFE.value));
    this.form.controls.toExpirationDate.patchValue(this.cs.dateNewFormat(this.form.controls.toExpirationDateFE.value));
    this.spin.show();
    this.sub.add(this.service.getExpiration(this.form.getRawValue()).subscribe(res => {
      this.dataSource.data = res;
      this.spin.hide()
       this.dataSource.paginator = this.paginator;
       this.dataSource.sort = this.sort;
      this.spin.hide();
     // this.dataSource.data.forEach((data: any) => {
     //   data.potentialClientId = this.multMatterList.find(y => y.value == data.potentialClientId)?.label;
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
  multiSelectCaseCategoryList: any[] = [];
  multiSelectCaseSubCategoryList: any[] = [];
  timekeeperCodelist: any[] = [];
  multitimekeeperList: any[] = [];
  multiselecttimekeeperList: any[] = [];

  
  ReceiptNumberlist: any[] = [];
  multiReceiptNumber: any[] = [];
  multiSelectReceiptNumber: any[] = [];

  getAllDropDown() {
    console.log(2)
    this.cas.getalldropdownlist([     
      this.cas.dropdownlist.matter.dropdown.url,
      this.cas.dropdownlist.setup.documentType.url,
      this.cas.dropdownlist.setup.timeKeeperCode.url,
      this.cas.dropdownlist.matter.receiptappnotice.url,
    ]).subscribe((results: any) => {
      results[0].caseCategoryList.forEach((x: any) => {
        this.multiSelectCaseCategoryList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      results[0].subCaseCategoryList.forEach((x: any) => {
        this.multiSelectCaseSubCategoryList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      this.thisDocumentType = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.documentType.key);
      this.thisDocumentType.forEach((x: { key: string; value: string; }) => this.multiDocumentType.push({value: x.key, label: x.key + ' / ' + x.value}))
      this.multiSelectDocumentType = this.multiDocumentType;

      this.timekeeperCodelist = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.timekeeperCodelist.forEach((x: { key: string; value: string; }) => this.multitimekeeperList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselecttimekeeperList = this.multitimekeeperList;

      this.ReceiptNumberlist = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.matter.receiptappnotice.key);
      this.ReceiptNumberlist.forEach((x: { key: string; value: string; }) => this.multiReceiptNumber.push({ value: x.key, label: x.key}))
      this.multiSelectReceiptNumber = this.multiReceiptNumber;

      
      console.log(this.multiSelectDocumentType)
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
        'Client Name': x.clientName,
        'Matter Name': x.matterText,
        'Employer Name/Petitioner Name': x.employerPetitionerName,
        'Document type': (x.docType),
        'Eligibility date' : x.eligibilityDate,
        "Expiration Date ": x.expirationDate,
        "Originating Time Keeper": x.originatingTk,
        "Responsible Time Keeper": x.responsibleTk,
        "Assigned Time Keeper": x.assignedTk,
        "Paralegal": x.paralegal,
        "Legal Assistant":x.legalAssistant,
        "Receipt No": x.receiptNumber

      });

    })
    this.excel.exportAsExcel(res, "Expiration");
  }

}


