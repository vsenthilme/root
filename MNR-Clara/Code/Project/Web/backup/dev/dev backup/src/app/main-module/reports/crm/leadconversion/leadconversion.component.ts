import { SelectionModel } from '@angular/cdk/collections';
import { DatePipe } from '@angular/common';
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
  selector: 'app-leadconversion',
  templateUrl: './leadconversion.component.html',
  styleUrls: ['./leadconversion.component.scss']
})
export class LeadconversionComponent implements OnInit {


  screenid = 1159;
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

  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 1
  };

  dropdownSettings1 = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };
  form = this.fb.group({
    classId: [, [Validators.required]],
   // classIdFE: [, [Validators.required]],
    fromCreatedOn: [new Date("01/01/00 00:00:00"), [Validators.required]],
    toCreatedOn: [this.cs.todayapi(), [Validators.required]],
    inquiryAssignedToRefField4: [,],
    inquiryAssignedToRefField4FE: [,],
    consultingAttorneyRefField2: [,],
    consultingAttorneyRefField2FE: [,],
    statusId: [, [Validators.required]],
   // statusIdFE: [, [Validators.required]],
  });



  displayedColumns: string[] = ['select', 'inquiryNumber', 'inquiyDate', 'inquiryAssignedToRefField4', 'firstNameLastName', 'contactNumber', 'email', 'modeOfInquiry', 'intakeFormReceived', 'consultationDate', 'consultingAttorney', 'statusId', 'orginalInquiryObjective', 'createdOn'];
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
    public authService: AuthService,
    private service: PrebillService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    private fb: FormBuilder,
    public toastr: ToastrService,
    private excel: ExcelService,
    public datepipe: DatePipe,
    private cas: CommonApiService,
  ) { }

  startDate: any;
  currentDate: Date;
  RA: any = {};
    
  ngOnInit(): void {

    this.RA = this.authService.getRoleAccess(this.screenid);
    this.getAllDropDown();
    this.currentDate = new Date();
    let yesterdayDate = new Date();
    let currentMonthStartDate = new Date();
    yesterdayDate.setDate(this.currentDate.getDate() - 1);
    this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
   currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
  this.form.controls.fromCreatedOn.patchValue(currentMonthStartDate);
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort,)
  sort: MatSort;

  getAllDropDown() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.crm.pcIntakeForm.url,
      this.cas.dropdownlist.setup.userId.url,
    ]).subscribe((results: any) => {
      console.log(results[0])
      results[0].forEach((classData: any) => {
        this.multiClassList.push({ value: classData.classId, label: classData.classId + ' - ' + classData.classDescription });
        console.log(this.multiClassList)
        if (this.authService.classId == 3) {
          this.multiClassList = this.multiClassList.filter(classData => classData.value == 1 || classData.value == 2)
        } else {
          this.multiClassList = this.multiClassList.filter(classData => classData.value == this.authService.classId)
        }
      })
      this.multiSelectClassList = this.multiClassList;

      this.statusIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.statusId.key);
      this.pcIntakeFormList = results[2];
      this.userIdList = results[3];
      this.statusIdList.forEach(status => {
        //status
        for (let i = 0; i < this.pcIntakeFormList.length; i++) {
          if (status.key == this.pcIntakeFormList[i].statusId) {
            this.multiStatusList.push({ value: status.key, label: status.value })
            break;
          }
        }
      })
      this.multiSelectStatusList = this.multiStatusList;


      this.userIdList.forEach(user => {
        //consulting-attorney
        for (let i = 0; i < this.pcIntakeFormList.length; i++) {
          if (user.userId == this.pcIntakeFormList[i].referenceField2) {
            this.multiAttorneyList.push({ value: user.userId, label: user.fullName, classId: user.classId })
            break;
          }
        }

        //inquiry-assigned
        for (let i = 0; i < this.pcIntakeFormList.length; i++) {
          if (user.userId == this.pcIntakeFormList[i].referenceField4) {
            this.multiAttorneyList.push({ value: user.userId, label: user.fullName, classId: user.classId })
            break;
          }
        }
      })
      this.multiSelectAttorneyList = this.multiAttorneyList;
      this.multiSelectInquiryAssignedList = this.multiInquiryAssignedList;
      console.log(this.userIdList)

    }, (err) => {
      this.toastr.error(err, "");
    });
  }
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
    // if (this.selectedClassId && this.selectedClassId.length > 0) {
    //   this.form.patchValue({ classId: this.selectedClassId[0].id });
    // }
    // if (this.selectedStatusId && this.selectedStatusId.length > 0) {
    //   let data: any[] = []
    //   this.selectedStatusId.forEach((a: any) => data.push(a.id))
    //   this.form.patchValue({ statusId: data });
    // }
    // if (this.selectedInquiryAssignedId && this.selectedInquiryAssignedId.length > 0) {
    //   let data: any[] = []
    //   this.selectedInquiryAssignedId.forEach((a: any) => data.push(a.id))
    //   this.form.patchValue({ inquiryAssignedToRefField4: data });
    // }
    // if (this.selectedAttorneyId && this.selectedAttorneyId.length > 0) {
    //   let data: any[] = []
    //   this.selectedAttorneyId.forEach((a: any) => data.push(a.id))
    //   this.form.patchValue({ consultingAttorneyRefField2: data });
    // }
    this.form.controls.fromCreatedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.fromCreatedOn.value));
    this.form.controls.toCreatedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.toCreatedOn.value));
    this.spin.show();
    this.sub.add(this.service.getLeadConversionReport(this.form.getRawValue()).subscribe(res => {
      this.dataSource.data = res;
console.log(this.statusIdList)
      this.dataSource.data.forEach((data: any) => {
        data.statusId = this.statusIdList.find(y => y.key == data.statusId)?.value;
      })
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
  onItemSelect(item: any, type?: any) {
    if (type == 'CLASS') {
      this.filterBasedOnClass();
    }
  }
  filterBasedOnClass() {
    // this.multiSelectInquiryAssignedList = [];
    // this.selectedInquiryAssignedId = [];
    // this.form.controls.inquiryAssignedToRefField4FE.setValue([]);
    // this.multiInquiryAssignedList.forEach(element => {
    //   if (element['classId'] == this.selectedClassId[0].id) {
    //     this.multiSelectInquiryAssignedList.push(element)
    //   }
    // });

    // this.multiSelectAttorneyList = [];
    // this.selectedAttorneyId = [];
    // this.form.controls.consultingAttorneyRefField2FE.setValue([]);
    // this.multiAttorneyList.forEach(element => {
    //   if (element['classId'] == this.selectedClassId[0].id) {
    //     this.multiSelectAttorneyList.push(element)
    //   }
    // });

  }


  reset() {
    this.form.reset();
  }


  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        'InquiryNo' : x.inquiryNumber,
        'Inquiry Date' :this.cs.dateapi(x.inquiyDate),
        'Inquiry Assigned To' : x.inquiryAssignedToRefField4, 
        'Name' : x.firstNameLastName, 
        'Phone Number' : x.contactNumber, 
        'Email' : x.email, 
        'Mode Of Inquiry' : x.modeOfInquiry,
         'Date IntakeForm Received' : this.cs.dateapi(x.intakeFormReceived),
          'Date of Consultation' : this.cs.dateapi(x.consultationDate),
           'Consulting Attorney' : x.consultingAttorney,
           'Status' : x.statusId, 
           'Objective Of Original Inquiry' : x.orginalInquiryObjective, 
           'Prospective File Date' : this.cs.dateapi(x.createdOn),
      });

    })
    this.excel.exportAsExcel(res, "Lead Conversion");
  }
}
