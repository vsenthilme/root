import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { documentElement } from "src/app/main-module/setting/business/document-template/document-template.service";
import { GeneralMatterService } from "../General/general-matter.service";
import { TaskMatterElement, TaskMatterService } from "./task-matter.service";
import { TaskNewComponent } from "./task-new/task-new.component";

interface SelectItem {
  id: string;
  itemName: string;
}




@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.scss']
})
export class TaskComponent implements OnInit {
  screenid = 1114;
  public icon = 'expand_more';

  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  matterdesc: any;
  statuslist1: dropdownelement[];
  code: any;
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
  animal: string | undefined;
  id: string | undefined;
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
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
  checkboxLabel(row?: TaskMatterElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.taskNumber + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  ELEMENT_DATA: TaskMatterElement[] = [];
  // displayedColumns: string[] = ['select', 'taskno', 'type', 'creation', 'deadline', 'remainder', 'originatting', 'responsible', 'legal', 'status',];
  displayedColumns: string[] = ['select', 'taskNumber','taskName', 'taskAssignedTo','createdBy', 'createdOn', 'deadlineDate', 'reminderDate', 'statusIddes',];

  dataSource = new MatTableDataSource<TaskMatterElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<TaskMatterElement>(true, []);

  constructor(public dialog: MatDialog,
    private service: TaskMatterService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService, private serviceMatter: GeneralMatterService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
  matterno: any;
  ClientFilter: any;
  RA: any = {};
  ngOnInit(): void {
    this.code = (this.cs.decrypt(this.route.snapshot.params.code));
    this.RA = this.auth.getRoleAccess(this.screenid);
    sessionStorage.setItem('matter', this.route.snapshot.params.code);
    this.matterdesc = this.cs.decrypt(sessionStorage.getItem('matter')).code1;
    this.matterno = this.cs.decrypt(sessionStorage.getItem('matter')).code;
    this.ClientFilter = { matterNumber: this.matterno };
    this.getAllListData();
  }
  deleteDialog() {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    if (this.selection.selected[0].statusId == 32) {
      this.toastr.error("Task can't be Deleted.", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: this.selection.selected[0].taskNumber,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].taskNumber);

      }
    });
  }
  deleterecord(id: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id).subscribe((res) => {
      this.toastr.success(id + " deleted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.spin.hide();
      this.getAllListData();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
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
          data: { pageflow: data, matter: this.cs.decrypt(this.route.snapshot.params.code).code, matterdesc: this.matterdesc, code: data != 'New' ? this.selection.selected[0].taskNumber : null }
        });

        dialogRef.afterClosed().subscribe(result => {

          this.getAllListData();
          //    window.location.reload();
        });


      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));


    }
    else {
      const dialogRef = this.dialog.open(TaskNewComponent, {
        disableClose: true,
        width: '70%',
        maxWidth: '80%',
        position: { top: '6.5%' },
        data: { pageflow: data, matter: this.cs.decrypt(this.route.snapshot.params.code).code, matterdesc: this.matterdesc, code: data != 'New' ? this.selection.selected[0].taskNumber : null }
      });

      dialogRef.afterClosed().subscribe(result => {

        this.getAllListData();
        //  window.location.reload();
      });

    }


  }
  sub = new Subscription();
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination
  taskTypeCodelist: any[] = [];
  statuslist: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  multitasktypeList: any[] = [];

    dropdownSettings = {
    singleSelection: false,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };


  getall(excel: boolean = false) {
    this.spin.show();
    this.cas.getalldropdownlist([
      // this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.taskTypeCode.url,
    ]).subscribe((results) => {
      // this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.statuslist1 = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key).filter(s => [31, 32].includes(s.key));
      this.statuslist1.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
    this.multiselectstatusList = this.multistatusList;
      this.taskTypeCodelist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.taskTypeCode.key);
      this.taskTypeCodelist.forEach((x: { key: string; value: string; }) => this.multitasktypeList.push({value: x.key, label:  x.key + '-' + x.value}))

      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: TaskMatterElement[]) => {
        res.forEach((x) => {
          // x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.taskTypeCode = this.taskTypeCodelist.find(y => y.key == x.taskTypeCode)?.value;
        })
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno && x.deletionIndicator == 0);


        this.assignedUserIdList = [];
        const categories = this.ELEMENT_DATA.map(person => ({
          taskAssignedTo: person.taskAssignedTo,
        }));
        const distinctThings = categories.filter(
          (thing, i, arr) => arr.findIndex(t => t.taskAssignedTo === thing.taskAssignedTo) === i
        );
        distinctThings.forEach(x => {

          this.assignedUserIdList.push({ key: x.taskAssignedTo, value: x.taskAssignedTo });
        });

        if (excel)
          this.excel.exportAsExcel(res, "Task");
        this.dataSource = new MatTableDataSource<TaskMatterElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<TaskMatterElement>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();

  }
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Task Number": x.taskNumber,
        'Task Type ': x.taskTypeCode,
        "Status  ": x.statusIddes,
        'Reminder Date ': this.cs.dateapi(x.reminderDate),
        'Deadline Date ': this.cs.dateapi(x.deadlineDate),
        //   'Creation Date ': this.cs.dateapi(x.createdOn),     //error throwing
      });

    })
    this.excel.exportAsExcel(res, "Task");
  }
  getAllListData() {
    this.getall();
  }
  assignedUserIdList: dropdownelement[] = [];
  searchStatusList = {
    statusId: [31, 32,]
  };
  searhform = this.fb.group({
    ecreatedOn: [],
    edeadlineDate: [],
    ereminderDate: [],
    screatedOn: [],
    sdeadlineDate: [],
    sreminderDate: [],
    statusId: [],
    statusIdFE: [],
    taskAssignedTo: [],
    taskName: [],
    taskNumber: [],
    taskTypeCode: [],
  });
  Clear() {
    this.reset();
  };

  search() {

    // if (this.selectedItems2 && this.selectedItems2.length > 0){
    //   let multistatusList: any[]=[]
    //   this.selectedItems2.forEach((a: any)=> multistatusList.push(a.id))
    //   this.searhform.patchValue({statusId: multistatusList });
    // }

    this.searhform.controls.ecreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.ecreatedOn.value));
    this.searhform.controls.edeadlineDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.edeadlineDate.value));
    this.searhform.controls.ereminderDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.ereminderDate.value));
    this.searhform.controls.screatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.screatedOn.value));
    this.searhform.controls.sdeadlineDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.sdeadlineDate.value));
    this.searhform.controls.sreminderDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.sreminderDate.value));


    this.spin.show();
    this.cas.getalldropdownlist([
      // this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      // this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);

      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: TaskMatterElement[]) => {
        res.forEach((x) => {
          // x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.taskTypeCode = this.taskTypeCodelist.find(y => y.key == x.taskTypeCode)?.value;
        })
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno && x.deletionIndicator == 0);


        this.dataSource = new MatTableDataSource<TaskMatterElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<TaskMatterElement>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();

  }
  reset() {
    this.searhform.reset();
  }
}