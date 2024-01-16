import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { TimekeeperDisplayComponent } from "./timekeeper-display/timekeeper-display.component";
import { TimekeeperElement, TimekeeperService } from "./timekeeper.service";

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-timekeeper',
  templateUrl: './timekeeper.component.html',
  styleUrls: ['./timekeeper.component.scss']
})
export class TimekeeperComponent implements OnInit {
  screenid = 1017;

  displayedColumns: string[] = ['select', 'classId', 'timekeeperCode', 'timekeeperName', 'userTypeIddesc', 'defaultRate', 'timekeeperStatus', 'createdBy', 'createdOn'];
  StatusList: dropdownelement[] = [{ key: 'Active', value: 'Active' }, { key: 'Inactive', value: 'Inactive' }]
  sub = new Subscription();
  ELEMENT_DATA: TimekeeperElement[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
  constructor(public dialog: MatDialog,
    private service: TimekeeperService,
    private cas: CommonApiService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
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

    this.getall();
    
    this.StatusList.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
this.multiselectstatusList = this.multistatusList;
  }

  dataSource = new MatTableDataSource<TimekeeperElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<TimekeeperElement>(true, []);


  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];


  selectedItems3: SelectItem[] = [];
  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];

    dropdownSettings = {
    singleSelection: false,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  multiselectclassList: any[] = [];    
  multiselectuserTypeIdList: any[] = [];
  multiselecttimekeeperList: any[] = [];


  getall(excel: boolean = false) {
    let classIdList: any[] = [];
    let userTypeIdList: any[] = [];
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.userTypeId.url,
    ]).subscribe((results) => {
      classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      classIdList.forEach((x: { key: string; value: string; }) => this.multiselectclassList.push({value: x.value, label: x.value}))

      userTypeIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.userTypeId.key);
      userTypeIdList.forEach((x: { key: string; value: string; }) => this.multiselectuserTypeIdList.push({value: x.value, label: x.value}))


      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: TimekeeperElement[]) => {
        res.forEach((x: { createdBy: string;}) => this.multiyseridList.push({value: x.createdBy, label: x.createdBy}))
        this.multiselectyseridList = this.multiyseridList;
        this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);


        res.forEach((x: { timekeeperCode: string; timekeeperName: string;}) => this.multiselecttimekeeperList.push({value: x.timekeeperCode, label: x.timekeeperCode + '-' + x.timekeeperName}))
        this.multiselecttimekeeperList = this.cas.removeDuplicatesFromArrayNew(this.multiselecttimekeeperList);


        res.forEach((x) => {
          x.classId = classIdList.find(y => y.key == x.classId)?.value;
          x.userTypeIddesc = userTypeIdList.find(y => y.key == x.userTypeId)?.value;
        })
        this.ELEMENT_DATA = res;

        if (excel)
          this.excel.exportAsExcel(res, "Time Keeper");
        this.dataSource = new MatTableDataSource<TimekeeperElement>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
        this.selection = new SelectionModel<TimekeeperElement>(true, []);
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
        "Class": x.classId,
        'Code': x.timekeeperCode,
        "Full Name": x.timekeeperName,
        "User Type": x.userTypeIddesc,
        "Default Rate/Hour": x.defaultRate,
        'Status': x.timekeeperStatus,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Time Keeper");
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
      data: this.selection.selected[0].timekeeperCode,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].timekeeperCode);

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

      this.spin.hide();// this.getall();
      window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  openDialog(data: any = 'New'): void {
    if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    const dialogRef = this.dialog.open(TimekeeperDisplayComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].timekeeperCode : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      //this.getall();
      window.location.reload();
    });
  }
  openDialog2(data: any = 'new'): void {

    const dialogRef = this.dialog.open(TimekeeperDisplayComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {

      // this.getall();
      window.location.reload();
    });
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
  checkboxLabel(row?: TimekeeperElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.timekeeperCode + 1}`;
  }




  searhform = this.fb.group({
    classId: [],

    timekeeperCode: [],
    timekeeperName: [],
    userTypeIddesc: [],

    rateUnit: [],
    timekeeperStatus: [],
    timekeeperStatusFE: [],
    createdBy: [],
    createdByFE: [],
    createdOn_from: [],
    createdOn_to: [],

  });

  search() {
    
 if (this.selectedItems3 && this.selectedItems3.length > 0){
  let multiyseridList: any[]=[]
  this.selectedItems3.forEach((a: any)=> multiyseridList.push(a.id))
  this.searhform.patchValue({createdBy: this.selectedItems3 });
}

    if (this.selectedItems2 && this.selectedItems2.length > 0){
      let multistatusList: any[]=[]
      this.selectedItems2.forEach((a: any)=> multistatusList.push(a.id))
      this.searhform.patchValue({timekeeperStatus: this.selectedItems2 });
    }

    let data = this.cs.filterArray(this.ELEMENT_DATA, this.searhform.getRawValue())
console.log( this.searhform.getRawValue())
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