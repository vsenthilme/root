


// export interface PeriodicElement {

import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { StatusEditComponent } from "./status-edit/status-edit.component";
import { StatusElement, StatusService } from "./status.service";


//   language: string;
//   statusid: string;
//   by: string;
//   on: string;
//   status: string;
// }
// const ELEMENT_DATA: PeriodicElement[] = [
//   {   language: 'AP-Noneditable',   statusid: 'Lorem Ipsum', status: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { language: 'AP-Noneditable',   statusid: 'Lorem Ipsum', status: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { language: 'AP-Noneditable',   statusid: 'Lorem Ipsum', status: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { language: 'AP-Noneditable',   statusid: 'Lorem Ipsum', status: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { language: 'AP-Noneditable',   statusid: 'Lorem Ipsum', status: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { language: 'AP-Noneditable',   statusid: 'Lorem Ipsum', status: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { language: 'AP-Noneditable',   statusid: 'Lorem Ipsum', status: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { language: 'AP-Noneditable',   statusid: 'Lorem Ipsum', status: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { language: 'AP-Noneditable',   statusid: 'Lorem Ipsum', status: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },

// ];
@Component({
  selector: 'app-status-id',
  templateUrl: './status-id.component.html',
  styleUrls: ['./status-id.component.scss']
})
export class StatusIdComponent implements OnInit, OnDestroy {
  screenid = 1008;
  sub = new Subscription();
  ELEMENT_DATA: StatusElement[] = [];
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
    private service: StatusService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cas: CommonApiService,
    private cs: CommonService,
    private excel: ExcelService,
    private auth: AuthService, private fb: FormBuilder) { }


  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openDialog2(data: any = 'new'): void {
    if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    const dialogRef = this.dialog.open(StatusEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].statusId : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      // this.getallationslist();
      window.location.reload();
    });
  }
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Language ID": x.languageId,
        'Status ID  ': x.statusId,
        "Description": x.status,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Status ID");
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
      data: this.selection.selected[0].statusId,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].statusId);

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
      //this.getallationslist();
      window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  openDialog(): void {

    const dialogRef = this.dialog.open(StatusEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },

    });

    dialogRef.afterClosed().subscribe(result => {

      this.getallationslist();
    });
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
  selectedItems3: any[] = [];
  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];
  multilanguageIdList: any[] = [];
  multistatusList: any[] = [];

  getallationslist() {
    this.spin.show();
    this.sub.add(this.service.Getall().subscribe((res: StatusElement[]) => {

      res.forEach((x: { createdBy: string;}) => this.multiyseridList.push({value: x.createdBy, label: x.createdBy}))
      this.multiselectyseridList = this.multiyseridList;
        this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);


        res.forEach((x: { statusId: any; status: string;}) => this.multistatusList.push({value: x.statusId, label: x.statusId + '-' + x.status}))
        this.multistatusList = this.cas.removeDuplicatesFromArrayNew(this.multistatusList);

        res.forEach((x: { languageId: string;}) => this.multilanguageIdList.push({value: x.languageId, label: x.languageId}))
        this.multilanguageIdList = this.cas.removeDuplicatesFromArrayNew(this.multilanguageIdList);

      this.ELEMENT_DATA = res;

      this.dataSource = new MatTableDataSource<StatusElement>(this.ELEMENT_DATA);
      this.selection = new SelectionModel<StatusElement>(true, []);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  displayedColumns: string[] = ['select', 'languageId', 'statusId', 'status', 'createdBy', 'createdOn',];
  dataSource = new MatTableDataSource<StatusElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<StatusElement>(true, []);

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
  checkboxLabel(row?: StatusElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.statusId + 1}`;
  }

  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination
  searhform = this.fb.group({
    languageId: [],
    statusId: [],
    status: [],
    createdByFE: [],
    createdBy: [],

    createdOn_from: [],
    createdOn_to: [],

  });

  search() {

    
 if (this.selectedItems3 && this.selectedItems3.length > 0){
  let multiyseridList: any[]=[]
  this.selectedItems3.forEach((a: any)=> multiyseridList.push(a.id))
  this.searhform.patchValue({createdBy: this.selectedItems3 });
}

    let data = this.cs.filterArray(this.ELEMENT_DATA, this.searhform.getRawValue())

    this.dataSource = new MatTableDataSource<StatusElement>(data);

    this.selection = new SelectionModel<StatusElement>(true, []);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

  }
  Cancel() {
    this.reset();

    this.dataSource = new MatTableDataSource<StatusElement>(this.ELEMENT_DATA);

    this.selection = new SelectionModel<StatusElement>(true, []);
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

