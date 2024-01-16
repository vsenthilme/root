import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { CasesubDisplayComponent } from "./casesub-display/casesub-display.component";

import { ToastrService } from "ngx-toastr";
import { NgxSpinnerService } from "ngx-spinner";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { Subscription } from "rxjs";
import { CaseSubCategoryElement, CaseSubCategoryService } from "./case-sub-category.service";
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { FormBuilder } from "@angular/forms";

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-casesub-category',
  templateUrl: './casesub-category.component.html',
  styleUrls: ['./casesub-category.component.scss']
})
export class CasesubCategoryComponent implements OnInit {

  screenid = 1023;

  public icon = 'expand_more';
  sub = new Subscription();
  ELEMENT_DATA: CaseSubCategoryElement[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  StatusList: dropdownelement[] = [{ key: 'Active', value: 'Active' }, { key: 'Inactive', value: 'Inactive' }]


  constructor(public dialog: MatDialog,
    private service: CaseSubCategoryService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
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
  display(): void {

    const dialogRef = this.dialog.open(CasesubDisplayComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {


    });
  }
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);

    this.getAllListData();
    this.StatusList.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
    this.multiselectstatusList = this.multistatusList;

  }
  displayedColumns: string[] = ['select', 'classId', 'caseSubcategoryId', 'subCategory', 'caseCategoryId', 'taxType', 'categoryStatus', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource<CaseSubCategoryElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<CaseSubCategoryElement>(true, []);


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
    this.sub.add(this.service.Delete(id.caseSubcategoryId, id.classId, id.languageId, id.caseCategoryId).subscribe((res) => {
      this.toastr.success(id.caseSubcategoryid + " deleted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.spin.hide(); //this.getAllListData();
      window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  openDialog(data: any = 'new'): void {
    if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    const dialogRef = this.dialog.open(CasesubDisplayComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0] : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      // this.getAllListData();
      window.location.reload();
    }
    );
  }
  openDialog2(data: any = 'new'): void {

    const dialogRef = this.dialog.open(CasesubDisplayComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {

      //  this.getAllListData();
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

  selectedItems3: SelectItem[] = [];
  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];
  multicasesubcatList: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];


dropdownSettings = {
  singleSelection: true,
  text:"Select",
  selectAllText:'Select All',
  unSelectAllText:'UnSelect All',
  enableSearchFilter: true,
  badgeShowLimit: 2,
  disabled: false
};


multiselectclassList: any[] = [];  
multiselectcasecategoryList: any[] = [];      

  getall(excel: boolean = false) {
    let classIdList: any[] = [];
    let caseCategoryIdList: any[] = [];
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.caseCategoryId.url,
    ]).subscribe((results) => {
      classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      classIdList.forEach((x: { key: string; value: string; }) => this.multiselectclassList.push({value: x.value, label: x.value}))

      caseCategoryIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.caseCategoryId.key);
      caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multiselectcasecategoryList.push({value: x.value, label: x.key + '-' + x.value}))

      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: CaseSubCategoryElement[]) => {
        res.forEach((x: { createdBy: string;}) => this.multiyseridList.push({value: x.createdBy, label: x.createdBy}))
        this.multiselectyseridList = this.multiyseridList;
          this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);

          res.forEach((x: { caseSubcategoryId: any; subCategory: any;}) => this.multicasesubcatList.push({value: x.caseSubcategoryId, label: x.caseSubcategoryId + '-' + x.subCategory}))
          this.multicasesubcatList = this.cas.removeDuplicatesFromArrayNew(this.multicasesubcatList);


        res.forEach((x) => {
          x.classIddes = classIdList.find(y => y.key == x.classId)?.value;
          x.caseCategoryIddes = caseCategoryIdList.find(y => y.key == x.caseCategoryId)?.value;
        })
        this.ELEMENT_DATA = res;

        if (excel)
          this.excel.exportAsExcel(res, "Case Sub Category");
        this.dataSource = new MatTableDataSource<CaseSubCategoryElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<CaseSubCategoryElement>(true, []);
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
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Class": x.classIddes,
        'Case Sub Category ID': x.caseSubcategoryId,
        "Case Sub Category ": x.subCategory,
        "Case Category ": x.caseCategoryIddes,
        "Tax Type ": x.taxType,
        'Status': x.categoryStatus,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Case Sub Category");
  }
  getAllListData() {
    this.getall();
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
  checkboxLabel(row?: CaseSubCategoryElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.caseSubcategoryId + 1}`;
  }

  searhform = this.fb.group({
    classIddes: [],
    caseCategoryIddes: [],
    subCategory: [],
    categoryStatus: [],
    categoryStatusFE: [],
    createdBy: [],
    createdByFE: [],
    createdOn_from: [],
    caseSubcategoryId: [],
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
      this.searhform.patchValue({categoryStatus: this.selectedItems2 });
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

