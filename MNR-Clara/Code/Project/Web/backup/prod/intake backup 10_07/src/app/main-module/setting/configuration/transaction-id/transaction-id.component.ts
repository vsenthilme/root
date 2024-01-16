
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
import { TransactionEditComponent } from "./transaction-edit/transaction-edit.component";
import { TransactionElement, TransactionService } from "./transaction.service";


//   class: string;
//   language: string;
//   transaction: string;
//   description: string;
//   by: string;
//   on: string;
// }
// const ELEMENT_DATA: PeriodicElement[] = [
//   { class: "1001", language: 'AP-Noneditable', transaction: '2001', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1002", language: 'AP-Noneditable', transaction: '2001', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1003", language: 'AP-Noneditable', transaction: '2001', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1004", language: 'AP-Noneditable', transaction: '2001', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1005", language: 'AP-Noneditable', transaction: '2001', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1006", language: 'AP-Noneditable', transaction: '2001', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1007", language: 'AP-Noneditable', transaction: '2001', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1008", language: 'AP-Noneditable', transaction: '2001', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1009", language: 'AP-Noneditable', transaction: '2001', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },



// ];
@Component({
  selector: 'app-transaction-id',
  templateUrl: './transaction-id.component.html',
  styleUrls: ['./transaction-id.component.scss']
})
export class TransactionIdComponent implements OnInit, OnDestroy {
  screenid = 1006;
  sub = new Subscription();
  ELEMENT_DATA: TransactionElement[] = [];
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
    private service: TransactionService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
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
    const dialogRef = this.dialog.open(TransactionEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].transactionId : null }
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
        "Class": x.classId,
        'Language ID  ': x.languageId,
        "Transaction ID": x.transactionId,
        "Description": x.transactionDescription,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Transaction ID");
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
      data: this.selection.selected[0].transactionId,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].transactionId);

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
      //  this.getallationslist();
      window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  openDialog(): void {

    const dialogRef = this.dialog.open(TransactionEditComponent, {
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
  multitransactionIdList: any[] = [];
  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];
  multiselectclassList: any[] = [];   

  getallationslist() {
    let classIdList: any[] = [];
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url]).subscribe((results) => {
      classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      classIdList.forEach((x: { key: string; value: string; }) => this.multiselectclassList.push({value: x.value, label: x.value}))

      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: TransactionElement[]) => {
        res.forEach((x) => {
          x.classId = classIdList.find(y => y.key == x.classId)?.value;
        })

        
 res.forEach((x: { transactionId: any; transactionDescription: string;}) => this.multitransactionIdList.push({value: x.transactionId, label: x.transactionId + '-' + x.transactionDescription}))
 this.multitransactionIdList = this.cas.removeDuplicatesFromArrayNew(this.multitransactionIdList);

        res.forEach((x: { createdBy: string;}) => this.multiyseridList.push({value: x.createdBy, label: x.createdBy}))
        this.multiselectyseridList = this.multiyseridList;
          this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);

        this.ELEMENT_DATA = res;

        this.dataSource = new MatTableDataSource<TransactionElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<TransactionElement>(true, []);
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

  displayedColumns: string[] = ['select', 'classId', 'languageId', 'transactionId', 'transactionDescription', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource<TransactionElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<TransactionElement>(true, []);

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
  checkboxLabel(row?: TransactionElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.classId + 1}`;
  }

  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination
  searhform = this.fb.group({
    classId: [],
    transactionId: [],
    transactionDescription: [],


    createdBy: [],

    createdOn_from: [],
    createdOn_to: [],

  });

  search() {
    let data = this.cs.filterArray(this.ELEMENT_DATA, this.searhform.getRawValue())

    this.dataSource = new MatTableDataSource<TransactionElement>(data);

    this.selection = new SelectionModel<TransactionElement>(true, []);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

  }
  Cancel() {
    this.reset();

    this.dataSource = new MatTableDataSource<TransactionElement>(this.ELEMENT_DATA);

    this.selection = new SelectionModel<TransactionElement>(true, []);
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