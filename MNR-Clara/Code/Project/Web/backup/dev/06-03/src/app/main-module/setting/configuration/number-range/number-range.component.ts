
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
import { NumberRangeElement, NumberRangeService } from "./number-range.service";
import { NumberrangeDisplayComponent } from "./numberrange-display/numberrange-display.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { FormBuilder } from "@angular/forms";

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-number-range',
  templateUrl: './number-range.component.html',
  styleUrls: ['./number-range.component.scss']
})
export class NumberRangeComponent implements OnInit, OnDestroy {
  sub = new Subscription();
  ELEMENT_DATA: NumberRangeElement[] = [];
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
    private service: NumberRangeService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,) { }

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
    const dialogRef = this.dialog.open(NumberrangeDisplayComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].numberRangeCode : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      this.getallationslist();
    });
  }
  open_new(): void {

    const dialogRef = this.dialog.open(NumberrangeDisplayComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {

      //this.getallationslist();
      window.location.reload();
    });
  }
  screenid = 1045;
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);

    this.getallationslist();

    this.statusIdList.forEach((x: { key: string; value: string; }) => this.multiselectstatusList.push({value: x.key, label:  x.value}))
    this.multiselectstatusList = this.multiselectstatusList;

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
  statusIdList: any[] = [
    { key: "Active", value: 'Active' },
    { key: "InActive", value: 'InActive' }];

    
    selectedItems3: SelectItem[] = [];
    multiselectyseridList: any[] = [];
    multiyseridList: any[] = [];


    multiselectnumberRangeList: any[] = [];
    multiselectclassList: any[] = [];   

 multiselectstatusList: any[] = [];  

  getall(excel: boolean = false) {
    let classIdList: any[] = [];
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    ]).subscribe((results) => {
      classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      classIdList.forEach((x: { key: string; value: string; }) => this.multiselectclassList.push({value: x.value, label: x.value}))
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: NumberRangeElement[]) => {
        res.forEach((x) => {
          x.classId = classIdList.find(y => y.key == x.classId)?.value;
        })
        
 res.forEach((x: { createdBy: string;}) => this.multiyseridList.push({value: x.createdBy, label: x.createdBy}))
 this.multiselectyseridList = this.multiyseridList;
   this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);

   
 res.forEach((x: { numberRangeCode: any; numberRangeObject: any;}) => this.multiselectnumberRangeList.push({value: x.numberRangeCode, label: x.numberRangeCode + '-' + x.numberRangeObject}))
 this.multiselectnumberRangeList = this.cas.removeDuplicatesFromArrayNew(this.multiselectnumberRangeList);

        this.ELEMENT_DATA = res;

        if (excel)
          this.excel.exportAsExcel(res, "Number Range");
        this.dataSource = new MatTableDataSource<NumberRangeElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<NumberRangeElement>(true, []);
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
        'Number Range Code': x.numberRangeCode,
        "Number Range Object": x.numberRangeObject,
        "Number Range From": x.numberRangeFrom,
        "Number Range To": x.numberRangeTo,
        "Current Number Range": x.numberRangeCurrent,
        "Status": x.numberRangeStatus,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Number Range");
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
      data: this.selection.selected[0].numberRangeCode,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].numberRangeCode);

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


  displayedColumns: string[] = ['select', 'classId', 'numberRangeObject', 'numberrangecode', 'numberRangeFrom', 'numberRangeTo', 'numberRangeCurrent', 'numberRangeStatus', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource<NumberRangeElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<NumberRangeElement>(true, []);

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
  checkboxLabel(row?: NumberRangeElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.numberRangeCode + 1}`;
  }
  searhform = this.fb.group({
    classId: [],
    numberRangeCode: [],
    numberRangeObject: [],
    numberRangeStatus: [],
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
