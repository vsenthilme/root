


  


  
  import { SelectionModel } from '@angular/cdk/collections';
import { Component, ComponentFactoryResolver, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
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
import { ReportServiceService } from '../report-service.service';
// export interface PeriodicElement {
//   id: string;
//   objectName: string;
//   createdOn: string;
//   error: string;
//   inquiry: string;
//   date: string;
//   by: string;
//   followup: string;
//   notes: string;
// }



@Component({
  selector: 'app-docketwise-sync',
  templateUrl: './docketwise-sync.component.html',
  styleUrls: ['./docketwise-sync.component.scss']
})
export class DocketwiseSyncComponent implements OnInit {


  screenid = 1174;
  public icon = 'expand_more';
  isShowDiv = false;
  table = true;
  fullscreen = false;
  search = true;
  back = false;
  showFloatingButtons: any;
  toggle = true;
  matterNumber: any;
  todaydate = new Date();
  todaydate1: string | null;
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


  submitted = false;

  form = this.fb.group({
    matterNumber: [,],
    
  });



  displayedColumns: string[] = ['select','createdOn', 'clientId', 'matterNumber', 'caseOpenedDate','statusText'];
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
  }
  constructor(
    public dialog: MatDialog,
    public authService: AuthService,
    private service: ReportServiceService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    private fb: FormBuilder,
    public toastr: ToastrService,
    private excel: ExcelService,
    private cas: CommonApiService,
  ) { }

  
  RA: any = {};
    
  ngOnInit(): void {

    this.RA = this.authService.getRoleAccess(this.screenid);
   // this.getAllDropDown();
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true })
  sort: MatSort;

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }

  totalRecords = 0;
  filtersearch() {
    this.submitted = true;
    if (!this.matterNumber) {
      this.toastr.error(
        "Please fill matter no to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }
    this.spin.show();
     this.sub.add(this.service.docketwise(this.matterNumber).subscribe(res => {

   if(res.statusId){
    res['statusText'] = 'SUCCESS'
   }
   res.createdOn = this.cs.dateapiutc0(res.createdOn)
   res.caseOpenedDate = this.cs.dateapiutc0(res.caseOpenedDate)
    this.dataSource.data.push(res);
      this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
      this.spin.hide();
      this.table = true;
      this.search = false;
      //this.fullscreen = true;
      this.back = true;
    },
      err => {
        this.submitted = false;
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
    this.matterNumber = null;
  }


  downloadexcel() {
    // if (excel)
    // var res: any = [];
    // this.dataSource.data.forEach(x => {
    //   res.push({
    //     'Class ID': x.classId,
    //     "Inquiry No  ": x.inquiryNumber,
    //     "Invoice Date ": this.cs.dateapi(x.inquiyDate),
    //     'Inquiry Assigned To ': x.inquiryAssignedToRefField4,
    //     'Name': x.firstNameLastName,
    //     'Phone No': x.contactNumber,
    //     'Email': x.email,
    //     'Inquiry Mode': x.modeOfInquiry,
    //     'Date Intake from Received': this.cs.dateapi(x.intakeFormReceived),
    //     ' Date of Consultation ': this.cs.dateapi(x.consultationDate),
    //     ' Consulting Attorney': x.consultingAttorney,
    //     'Status': x.statusId,
    //     'Objective of Original Inquiry': x.orginalInquiryObjective,
    //     ' Prospective File Date': this.cs.dateapi(x.createdOn),
    //   });

    // })
    // this.excel.exportAsExcel(res, "Lead Conversion");
  }

  updateQb(data: any) {
    console.log(this.selection.selected[0].objectName)
   if(this.selection.selected[0].objectName == "INVOICE"){
    this.sub.add(this.service.reRunInvoice(this.selection.selected[0].id, '').subscribe(ress => { 
      this.toastr.success(this.selection.selected[0].id + " Sync Reinitiated!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this. filtersearch()
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
   }
  }
}


