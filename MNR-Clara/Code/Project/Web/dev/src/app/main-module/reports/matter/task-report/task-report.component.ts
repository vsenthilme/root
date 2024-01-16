
import { SelectionModel } from "@angular/cdk/collections";
import { DatePipe } from "@angular/common";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { GeneralMatterService } from "src/app/main-module/matters/case-management/General/general-matter.service";
import { TaskMatterService } from "src/app/main-module/matters/case-management/task/task-matter.service";
import { TaskNewComponent } from "src/app/main-module/matters/case-management/task/task-new/task-new.component";
import { TaskComponent } from "src/app/main-module/matters/case-management/task/task.component";
import { ReportServiceService } from "../../report-service.service";

@Component({
  selector: 'app-task-report',
  templateUrl: './task-report.component.html',
  styleUrls: ['./task-report.component.scss']
})
export class TaskReportComponent implements OnInit {

  screenid = 1187;
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

    classId: [, Validators.required],
    ecreatedOn: [],
    edeadlineDate: [],
    sdeadlineDate: [],
    edeadlineDateFE: [this.cs.todayapi(),],
    ereminderDate: [],
    screatedOn: [],
    matterNumber: [],
    sdeadlineDateFE: [new Date("01/01/00 00:00:00"),],
    sreminderDate: [],
    statusId: [],
    statusIdFE: [],
    taskAssignedTo: [],
    createdBy: [],
    taskName: [],
    taskNumber: [],
    taskTypeCode: [],

  });

  displayedColumns: string[] = [
    'select',
    'classId',
    'matterNumber',
    'taskName',
    'taskDescription',
    'createdBy',
    'taskAssignedTo',
    'createdOn',
    'deadlineDate',
    'reminderDate',
    'statusId',
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
    private service: TaskMatterService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    private excel: ExcelService,
    private fb: FormBuilder,
    public toastr: ToastrService,
    public datepipe: DatePipe,
    private cas: CommonApiService,
    private auth: AuthService,
    private serviceMatter: GeneralMatterService,
    private route: Router) { }
  RA: any = {};
  startDate: any;
  currentDate: Date;


  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllDropDown();
    //   this.currentDate = new Date();
    //   let yesterdayDate = new Date();
    //   let currentMonthStartDate = new Date();
    //   yesterdayDate.setDate(this.currentDate.getDate() - 1);
    //   this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
    //  currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
    // this.form.controls.sdeadlineDateFE.patchValue(currentMonthStartDate);

    var date = new Date();
    var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
    var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);

    this.form.controls.sdeadlineDateFE.patchValue(firstDay);
    this.form.controls.edeadlineDateFE.patchValue(lastDay);
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openDialog(data: any = 'new'): void {
    if (data != 'New') {
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
      if (data != 'Display') {
        if (this.selection.selected[0].statusId == 32) {
          this.toastr.error("Task is already completed.", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
          return;
        }
      }
      this.spin.show();
      this.sub.add(this.serviceMatter.Get(this.selection.selected[0].matterNumber).subscribe((res) => {
        this.spin.hide();
        let statusList = [30, 36];
        if (statusList.includes(res.statusId)) {
          this.toastr.error("Task can't be edited.", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
          return;
        }
        const dialogRef = this.dialog.open(TaskNewComponent, {
          disableClose: true,
          width: '70%',
          maxWidth: '80%',
          position: { top: '6.5%' },
          data: { pageflow: data, matter: this.selection.selected[0].matterNumber, matterdesc: this.selection.selected[0].matterNumber, code: data != 'New' ? this.selection.selected[0].taskNumber : null }
        });

        dialogRef.afterClosed().subscribe(result => {

          this.filtersearch();
          //    window.location.reload();
        });


      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));


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
    console.log(this.statuslist)
    this.form.controls.sdeadlineDate.patchValue(this.cs.day_callapiSearch(this.form.controls.sdeadlineDateFE.value));
    this.form.controls.edeadlineDate.patchValue(this.cs.day_callapiSearch(this.form.controls.edeadlineDateFE.value));
    this.spin.show();
    this.sub.add(this.service.Search(this.form.getRawValue()).subscribe(res => {
      res.forEach((x) => {
        // x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
        x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
      })
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

  multiselectassignedbyList: any[] = [];
  multiselectassignedtoList: any[] = [];
  multiSelectCaseCatList: any[] = [];
  multiSelectCaseSubCatList: any[] = [];
  multitimekeeperList: any[] = [];
  timekeeperCodelist: any[] = [];
  statuslist: any[] = [];
  getAllDropDown() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.matter.dropdown.url,
      this.cas.dropdownlist.setup.timeKeeperCode.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results: any) => {
      results[0].classList.forEach((x: any) => {
        this.multiSelectClassList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      results[0].matterList.forEach((x: any) => {
        this.multiMatterList.push({ value: x.key, label: x.key + '-' + x.value });
      })
      this.timekeeperCodelist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.timekeeperCodelist.forEach((x: { key: string; value: string; }) => this.multitimekeeperList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselectassignedbyList = this.multitimekeeperList;
      this.multiselectassignedtoList = this.multitimekeeperList;


      this.statuslist = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.setup.statusId.key);
      console.log(this.statuslist)
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
    this.form.controls.classId.patchValue([]);
    this.form.controls.taskAssignedTo.patchValue([]);
    this.form.controls.createdBy.patchValue([]);
    this.form.controls.matterNumber.patchValue([]);
  }

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        'Class': (x.classId == 1 ? 'L&E' : x.classId == 2 ? 'Immigration' : ''),
        'Matter Name': x.matterNumber,
        'Task Name': x.taskName,
        'Task Description ': x.taskDescription,
        'Assigned By': x.createdBy,
        'Assigned On': this.cs.dateapi(x.createdOn),
        'Deadline': this.cs.dateapi(x.deadlineDate),
        'Reminder': this.cs.dateapi(x.reminderDate),
        'Status': x.statusIddes,
      });

    });
    this.excel.exportAsExcel(res, "Task");
  }
}




