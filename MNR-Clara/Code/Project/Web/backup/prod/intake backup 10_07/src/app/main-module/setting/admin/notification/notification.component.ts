
import { NotificationElement, NotificationService } from "./notification.service";
import { NotifyEditComponent } from "./notify-edit/notify-edit.component";
import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { FormBuilder } from "@angular/forms";

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {

  screenid = 1059;
  sub = new Subscription();
  ELEMENT_DATA: NotificationElement[] = [];
  public icon = 'expand_more';

  isShowDiv = false;
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
  showFiller = false;
  animal: string | undefined;
  name: string | undefined;
  constructor(public dialog: MatDialog,
    private service: NotificationService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,) { }



  open_new_update(data: any = 'new'): void {
    if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    const dialogRef = this.dialog.open(NotifyEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0] : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      // this.getallationslist();
      window.location.reload();
    });
  }

  open_new(data: any = 'new'): void {

    const dialogRef = this.dialog.open(NotifyEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.7%', }
    });

    dialogRef.afterClosed().subscribe(result => {

      // this.getallationslist();
      window.location.reload();
    });
  }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getallationslist();
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination

  getall(excel: boolean = false) {
    let classIdList: any[] = [];
    let transactionIdList: any[] = [];
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.transactionId.url,

    ]).subscribe((results) => {
      classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      transactionIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.transactionId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: NotificationElement[]) => {
        res.forEach((x) => {
          x.classIddes = classIdList.find(y => y.key == x.classId)?.value;
          x.transactionIddes = transactionIdList.find(y => y.key == x.transactionId)?.value;
        })
        this.ELEMENT_DATA = res;

        if (excel)
          this.excel.exportAsExcel(res, "Intake");
        this.dataSource = new MatTableDataSource<NotificationElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<NotificationElement>(true, []);
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
        "Class": x.classIddes,
        'Language ID  ': x.languageId,
        "Transaction": x.transactionIddes,
        " User ID": x.userId,
        'Notification ID': x.notificationId,
        'Notification Description': x.notificationDescription,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Intake");
  }
  getallationslist() {
    this.getall();
  }
  deleteDialog() {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
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
      data: this.selection.selected[0],
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0]);

      }
    });
  }
  deleterecord(id: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id.notificationId, id.classId, id.languageId, id.transactionId, id.userId).subscribe((res) => {
      this.toastr.success(id.notificationid + " deleted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
      // this.getallationslist();
      window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  displayedColumns: string[] = ['select', 'classIddes', 'languageId', 'transactionIddes', 'userId', 'notificationId', 'notificationDescription', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource<NotificationElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<NotificationElement>(true, []);

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
  checkboxLabel(row?: NotificationElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.notificationId + 1}`;
  }
  searhform = this.fb.group({
    classIddes: [],
    languageId: [],
    transactionIddes: [],
    userId: [],
    notificationId: [],
    notificationDescription: [],

    createdBy: [],
    createdOn_from: [],
    createdOn_to: [],

  });

  search() {
    let data = this.cs.filterArray(this.ELEMENT_DATA, this.searhform.getRawValue())

    this.dataSource = new MatTableDataSource<any>(data);

    this.selection = new SelectionModel<any>(true, []);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

  }
  Cancel() {
    this.reset();

    this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);

    this.selection = new SelectionModel<any>(true, []);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  reset() {
    this.searhform.reset();
  }
}

