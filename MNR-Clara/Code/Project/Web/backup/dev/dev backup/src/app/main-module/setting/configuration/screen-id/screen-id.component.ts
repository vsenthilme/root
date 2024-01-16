

// const ELEMENT_DATA: PeriodicElement[] = [
//   { screen: "1001", language: 'AP-Noneditable', subscreen: '2001', description: 'Lorem Ipsum', subdescription: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { screen: "1002", language: 'AP-Noneditable', subscreen: '2001', description: 'Lorem Ipsum', subdescription: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { screen: "1003", language: 'AP-Noneditable', subscreen: '2001', description: 'Lorem Ipsum', subdescription: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { screen: "1004", language: 'AP-Noneditable', subscreen: '2001', description: 'Lorem Ipsum', subdescription: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { screen: "1005", language: 'AP-Noneditable', subscreen: '2001', description: 'Lorem Ipsum', subdescription: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { screen: "1006", language: 'AP-Noneditable', subscreen: '2001', description: 'Lorem Ipsum', subdescription: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { screen: "1007", language: 'AP-Noneditable', subscreen: '2001', description: 'Lorem Ipsum', subdescription: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { screen: "1008", language: 'AP-Noneditable', subscreen: '2001', description: 'Lorem Ipsum', subdescription: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { screen: "1009", language: 'AP-Noneditable', subscreen: '2001', description: 'Lorem Ipsum', subdescription: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },

// ];
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
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { ScreenEditComponent } from "./screen-edit/screen-edit.component";
import { ScreenElement, ScreenService } from "./screen.service";



@Component({
  selector: 'app-screen-id',
  templateUrl: './screen-id.component.html',
  styleUrls: ['./screen-id.component.scss']
})
export class ScreenIdComponent implements OnInit, OnDestroy {
  screenid = 1004;
  sub = new Subscription();
  ELEMENT_DATA: ScreenElement[] = [];
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
    private service: ScreenService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private excel: ExcelService,
    private auth: AuthService,
    private fb: FormBuilder) { }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  open_new_update(data: any = 'new'): void {
    if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    const dialogRef = this.dialog.open(ScreenEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].screenId : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      //  this.getallationslist();
      window.location.reload();
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
  downloadexcel() {
    this.spin.show();
    this.sub.add(this.service.Getall().subscribe((res: ScreenElement[]) => {
      this.ELEMENT_DATA = res;

      this.excel.exportAsExcel(res, "Screen ID");
      this.dataSource = new MatTableDataSource<ScreenElement>(this.ELEMENT_DATA);
      this.selection = new SelectionModel<ScreenElement>(true, []);
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));

  }
  getallationslist() {
    this.spin.show();
    this.sub.add(this.service.Getall().subscribe((res: ScreenElement[]) => {
      this.ELEMENT_DATA = res;

      this.dataSource = new MatTableDataSource<ScreenElement>(this.ELEMENT_DATA);
      this.selection = new SelectionModel<ScreenElement>(true, []);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
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
      data: this.selection.selected[0].screenId,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].screenId);

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
      // this.getallationslist();
      window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }


  displayedColumns: string[] = ['select', 'languageId', 'screenId', 'subScreenId', 'screenText', 'subScreenName', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource<ScreenElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<ScreenElement>(true, []);

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
  checkboxLabel(row?: ScreenElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.screenId + 1}`;
  }


  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination
  searhform = this.fb.group({
    screenId: [],
    subScreenId: [],
    screenText: [],
    subScreenName: [],


    createdBy: [],
    createdOn_from: [],
    createdOn_to: [],

  });

  search() {

    let data = this.cs.filterArray(this.ELEMENT_DATA, this.searhform.getRawValue())

    this.dataSource = new MatTableDataSource<ScreenElement>(data);

    this.selection = new SelectionModel<ScreenElement>(true, []);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

  }
  Cancel() {
    this.reset();

    this.dataSource = new MatTableDataSource<ScreenElement>(this.ELEMENT_DATA);

    this.selection = new SelectionModel<ScreenElement>(true, []);
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

