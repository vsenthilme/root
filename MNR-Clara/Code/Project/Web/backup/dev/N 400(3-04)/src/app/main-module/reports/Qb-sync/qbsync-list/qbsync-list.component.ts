
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
import { PrebillService } from 'src/app/main-module/accounts/prebill/prebill.service';
import { ReportServiceService } from '../../report-service.service';
import { UpdateQbComponent } from './update-qb/update-qb.component';
export interface PeriodicElement {
  id: string;
  objectName: string;
  createdOn: string;
  error: string;
  inquiry: string;
  date: string;
  by: string;
  followup: string;
  notes: string;
}

@Component({
  selector: 'app-qbsync-list',
  templateUrl: './qbsync-list.component.html',
  styleUrls: ['./qbsync-list.component.scss']
})
export class QbsyncListComponent implements OnInit {


  screenid = 1170;
  public icon = 'expand_more';
  isShowDiv = false;
  table = true;
  fullscreen = false;
  search = true;
  back = false;
  showFloatingButtons: any;
  toggle = true;
  disableExecute: boolean;
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
  selectedClassId: any[] = [];

  multiSelectStatusList: any[] = [];
  multiStatusList: any[] = [];
  selectedStatusId: any[] = [];

  multiSelectInquiryAssignedList: any[] = [];
  multiInquiryAssignedList: any[] = [];
  selectedInquiryAssignedId: any[] = [];

  multiSelectAttorneyList: any[] = [];
  multiAttorneyList: any[] = [];
  selectedAttorneyId: any[] = [];

  pcIntakeFormList: any[] = [];

  submitted = false;

  statusIdList: any[] = [];
  inquiryList: any[] = [];
  userIdList: any[] = [];

  form = this.fb.group({
    endCreatedOn: [new Date(), [Validators.required]],
    startCreatedOn: [new Date("01/01/1900 00:00:00"), [Validators.required]],
    objectId: [,],
    
  });



  displayedColumns: string[] = ['select', 'action', 'createdOn', 'id', 'objectName', 'error'];
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

  // getAllDropDown() {
  //   this.cas.getalldropdownlist([
  //     this.cas.dropdownlist.setup.classId.url,
  //     this.cas.dropdownlist.setup.statusId.url,
  //     this.cas.dropdownlist.crm.pcIntakeForm.url,
  //     this.cas.dropdownlist.setup.userId.url,
  //   ]).subscribe((results: any) => {
  //     console.log(results[0])
  //     results[0].forEach((classData: any) => {
  //       this.multiClassList.push({ value: classData.classId, label: classData.classId + ' - ' + classData.classDescription });
  //       console.log(this.multiClassList)
  //       if (this.authService.classId == 3) {
  //         this.multiClassList = this.multiClassList.filter(classData => classData.value == 1 || classData.value == 2)
  //       } else {
  //         this.multiClassList = this.multiClassList.filter(classData => classData.value == this.authService.classId)
  //       }
  //     })
  //     this.multiSelectClassList = this.multiClassList;

  //     this.statusIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.statusId.key);
  //     this.pcIntakeFormList = results[2];
  //     this.userIdList = results[3];
  //     this.statusIdList.forEach(status => {
  //       //status
  //       for (let i = 0; i < this.pcIntakeFormList.length; i++) {
  //         if (status.key == this.pcIntakeFormList[i].statusId) {
  //           this.multiStatusList.push({ value: status.key, label: status.value })
  //           break;
  //         }
  //       }
  //     })
  //     this.multiSelectStatusList = this.multiStatusList;


  //     this.userIdList.forEach(user => {
  //       //consulting-attorney
  //       for (let i = 0; i < this.pcIntakeFormList.length; i++) {
  //         if (user.userId == this.pcIntakeFormList[i].referenceField2) {
  //           this.multiAttorneyList.push({ value: user.userId, label: user.fullName, classId: user.classId })
  //           break;
  //         }
  //       }

  //       //inquiry-assigned
  //       for (let i = 0; i < this.pcIntakeFormList.length; i++) {
  //         if (user.userId == this.pcIntakeFormList[i].referenceField4) {
  //           this.multiAttorneyList.push({ value: user.userId, label: user.fullName, classId: user.classId })
  //           break;
  //         }
  //       }
  //     })
  //     this.multiSelectAttorneyList = this.multiAttorneyList;
  //     this.multiSelectInquiryAssignedList = this.multiInquiryAssignedList;
  //     console.log(this.userIdList)

  //   }, (err) => {
  //     this.toastr.error(err, "");
  //   });
  // }
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
    this.form.controls.endCreatedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.endCreatedOn.value));
    this.form.controls.startCreatedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.startCreatedOn.value));
    this.spin.show();
    this.sub.add(this.service.getAllGbSync(this.form.getRawValue()).subscribe(res => {
   if(res){
    this.dataSource.data = res;
    this.disableExecute = true;
    this.totalRecords = this.dataSource.data.length;
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.spin.hide();
    this.table = true;
    this.search = false;
    this.back = true;

   }
   else{
    this.toastr.error("No message from QB", "Notification", {
      timeOut: 2000,
      progressBar: false,
    });
   }
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
  onItemSelect(item: any, type?: any) {
    if (type == 'CLASS') {
      this.filterBasedOnClass();
    }
  }
  filterBasedOnClass() {

  }


  reset() {
    this.form.reset();
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
    if(this.selection.selected[0].objectName == "CLIENT"){
      this.sub.add(this.service.reRunClient(this.selection.selected[0].id, '').subscribe(ress => { 
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
   if(this.selection.selected[0].objectName == "MATTER"){
    this.sub.add(this.service.reRunMatter(this.selection.selected[0].id, '').subscribe(ress => { 
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
