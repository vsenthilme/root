import { SelectionModel } from '@angular/cdk/collections';
import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
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

@Component({
  selector: 'app-matter-pl',
  templateUrl: './matter-pl.component.html',
  styleUrls: ['./matter-pl.component.scss']
})
export class MatterPLComponent implements OnInit {
  screenid = 1190;
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
    caseCategoryId:    [],
    caseSubCategoryId: [],
    classId:           [, Validators.required],
    clientNumber:      [],
    fromInvoiceDate:   [],
    matterNumber:      [],
    partner:           [],
    toInvoiceDate:     [],
    fromDateFE:     [],
    toDateFE:       [this.cs.todayapi(), Validators.required],
  });

  displayedColumns: string[] = [
    'clientId', 
    'clientName',
    'matterNumber',
    'matterDescription',
      'partnerAssigned', 
      'invoiceDate', 
      'invoiceNumber', 
      'timeticketCaptured', 
      'flatFeeBilled', 
      'costCaptured', 
      'costBilled', 
      'totalBilled', 
      'matterPandL', 
  ];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

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
    private auth: AuthService) { }
  RA: any = {};
  startDate: any;
  currentDate: Date;
    
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
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
    this.form.controls.fromInvoiceDate.patchValue(this.cs.dateNewFormat1(this.form.controls.fromDateFE.value));
    this.form.controls.toInvoiceDate.patchValue(this.cs.dateNewFormat1(this.form.controls.toDateFE.value));

    this.spin.show();
    this.sub.add(this.service.getMatterPL(this.form.getRawValue()).subscribe(res => {
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

  multiSelectClassList: any[] = [];
  multiSelectMatterList: any[] = [];
  multiSelectCaseCatList: any[] = [];
  multiSelectCaseSubCatList: any[] = [];
  userIdList: any[] = [];
  matterAssignmentIdList: any[] = [];
  originatingTimeList: any[] = [];
  partnerList: any[] = [];
  respTimeList: any[] = [];
  getAllDropDown() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.matter.dropdown.url,
      this.cas.dropdownlist.matter.matterAssignment.url,
      this.cas.dropdownlist.setup.userId.url,
    ]).subscribe((results: any) => {
      
      results[0].classList.forEach((x: any) => {
        this.multiSelectClassList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      results[0].matterList.forEach((x: any) => {
        this.multiSelectMatterList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      results[0].caseCategoryList.forEach((x: any) => {
        this.multiSelectCaseCatList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      results[0].subCaseCategoryList.forEach((x: any) => {
        this.multiSelectCaseSubCatList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      this.matterAssignmentIdList = results[1];
      this.userIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.userId.key);
      this.userIdList.forEach(user => {
      //partner
      for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
        if (user.key == this.matterAssignmentIdList[i].partner) {
          this.partnerList.push({ value: user.key, label: user.value })
          break;
        }
      }
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
        'Client Name': x.clientName,
        'Matter No': x.matterNumber,
        'Matter Description': x.matterDescription,
        'Partner': x.partnerAssigned,
        'Date': this.cs.dateapi(x.invoiceDate),
        'Invoice No ': x.invoiceNumber,
        'Value of Time Tickets Captured' : x.timeticketCaptured,
        'Flat Fee Billed' : x.flatFeeBilled,
        'Cost Captured' : x.costCaptured,
        'Cost Billed': x.costBilled,
        'Total Billed': x.totalBilled,
        'Matter P&L': x.matterPandL
      });

    })
    this.excel.exportAsExcel(res, "Attorney Productivity");
  }




}




