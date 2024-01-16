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
import { MessageEditComponent } from "./message-edit/message-edit.component";
import { MessageElement, MessageService } from "./message.service";

interface SelectItem {
  id: string;
  itemName: string;
}

// export interface PeriodicElement {


//   class: string;
//   Message: string;
//   message: string;
//   type: string;
//   by: string;
//   on: string;
//   description: string;
// }
// const ELEMENT_DATA: PeriodicElement[] = [
//   { class: "1001", Message: 'AP-Noneditable', message: '2001', type: 'Lorem Ipsum', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1002", Message: 'AP-Noneditable', message: '2001', type: 'Lorem Ipsum', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1003", Message: 'AP-Noneditable', message: '2001', type: 'Lorem Ipsum', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1004", Message: 'AP-Noneditable', message: '2001', type: 'Lorem Ipsum', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1005", Message: 'AP-Noneditable', message: '2001', type: 'Lorem Ipsum', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1006", Message: 'AP-Noneditable', message: '2001', type: 'Lorem Ipsum', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1007", Message: 'AP-Noneditable', message: '2001', type: 'Lorem Ipsum', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1008", Message: 'AP-Noneditable', message: '2001', type: 'Lorem Ipsum', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },
//   { class: "1009", Message: 'AP-Noneditable', message: '2001', type: 'Lorem Ipsum', description: 'Lorem Ipsum', by: 'Admin', on: '08-05-2021' },

// ];
@Component({
  selector: 'app-message-id',
  templateUrl: './message-id.component.html',
  styleUrls: ['./message-id.component.scss']
})
export class MessageIdComponent implements OnInit, OnDestroy {
  screenid = 1009;
  sub = new Subscription();
  ELEMENT_DATA: MessageElement[] = [];
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
    private service: MessageService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private fb: FormBuilder,
    private excel: ExcelService,
    private auth: AuthService,) { }
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
    const dialogRef = this.dialog.open(MessageEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].messageId : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      //  this.getallationslist();
      window.location.reload();
    });
  }
  
  selectedItems3: SelectItem[] = [];
  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];
  downloadexcel() {
    let classIdList: any[] = [];
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url]).subscribe((results) => {
      classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);

    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();

    this.spin.show();
    this.sub.add(this.service.Getall().subscribe((res: MessageElement[]) => {
      res.forEach((x) => {
        x.classId = classIdList.find(y => y.key == x.classId)?.value;
      })

      res.forEach((x: { createdBy: string;}) => this.multiyseridList.push({value: x.createdBy, label: x.createdBy}))
      this.multiselectyseridList = this.multiyseridList;
        this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);

      this.ELEMENT_DATA = res;

      this.excel.exportAsExcel(res, "Message ID");
      this.dataSource = new MatTableDataSource<MessageElement>(this.ELEMENT_DATA);
      this.selection = new SelectionModel<MessageElement>(true, []);
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
      data: this.selection.selected[0].messageId,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].messageId);

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

    const dialogRef = this.dialog.open(MessageEditComponent, {
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

  getallationslist() {
    let classIdList: any[] = [];
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url]).subscribe((results) => {
      classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);

    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();

    this.spin.show();
    this.sub.add(this.service.Getall().subscribe((res: MessageElement[]) => {
      res.forEach((x) => {
        x.classId = classIdList.find(y => y.key == x.classId)?.value;
      })

      res.forEach((x: { createdBy: string;}) => this.multiyseridList.push({value: x.createdBy, label: x.createdBy}))
      this.multiselectyseridList = this.multiyseridList;
        this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);

      this.ELEMENT_DATA = res;

      this.dataSource = new MatTableDataSource<MessageElement>(this.ELEMENT_DATA);
      this.selection = new SelectionModel<MessageElement>(true, []);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  displayedColumns: string[] = ['select', 'classId', 'languageId', 'messageId', 'messageType', 'messageDescription', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource<MessageElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<MessageElement>(true, []);

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
  checkboxLabel(row?: MessageElement): string {
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
    createdBy: [],
    createdByFE: [],
    messageType: [],
    messageId: [],
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

    this.dataSource = new MatTableDataSource<MessageElement>(data);

    this.selection = new SelectionModel<MessageElement>(true, []);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

  }
  Cancel() {
    this.reset();

    this.dataSource = new MatTableDataSource<MessageElement>(this.ELEMENT_DATA);

    this.selection = new SelectionModel<MessageElement>(true, []);
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



