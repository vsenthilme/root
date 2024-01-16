import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, HostListener, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { ClassDisplayComponent } from "./class-display/class-display.component";
import { ToastrService } from "ngx-toastr";
import { NgxSpinnerService } from "ngx-spinner";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { ClassElement, ClassService } from "./class.service";
import { Subscription } from "rxjs";
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { FormBuilder } from "@angular/forms";


interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-class',
  templateUrl: './class.component.html',
  styleUrls: ['./class.component.scss']
})
export class ClassComponent implements OnInit {
  screenid = 1019;

  public icon = 'expand_more';
  StatusList: dropdownelement[] = [{ key: 'Active', value: 'Active' }, { key: 'Inactive', value: 'Inactive' }]

  sub = new Subscription();
  ELEMENT_DATA: ClassElement[] = [];
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
    private service: ClassService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }

  }
  openDialog2(): void {

    const dialogRef = this.dialog.open(ClassDisplayComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.7%', }

    });

    dialogRef.afterClosed().subscribe(result => {

      this.getAllClass();
    });
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

    const dialogRef = this.dialog.open(ClassDisplayComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].classId : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      //  this.getAllClass();
      window.location.reload();
    });
  }
  class_display(): void {

    const dialogRef = this.dialog.open(ClassDisplayComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {


    });
  }

  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];


    dropdownSettings = {
    singleSelection: false,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };


  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllClass();
    this.StatusList.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
    this.multiselectstatusList = this.multistatusList;

  }


  displayedColumns: string[] = ['select', 'company', 'classId', 'classDescription', 'classStatus', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource<ClassElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<ClassElement>(true, []);

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Company ID": x.companyId,
        'Class ID ': x.classId,
        " Class": x.classDescription,
        'Status': x.classStatus,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Class");
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
      data: this.selection.selected[0].classId,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].classId);

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

      this.spin.hide(); //this.getAllClass();
      window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination

  selectedItems3: SelectItem[] = [];
  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];
  multiselectclassList: any[] = [];

  multiselectcompanyList: any[] = [];

  getAllClass() {
    this.spin.show();
    this.sub.add(this.service.Getall().subscribe((res: ClassElement[]) => {

      res.forEach((x: { createdBy: string;}) => this.multiyseridList.push({value: x.createdBy, label: x.createdBy}))
      this.multiselectyseridList = this.multiyseridList;
        this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);

        
      res.forEach((x: { classId: string; classDescription: string;}) => this.multiselectclassList.push({value: x.classId, label:   x.classId + '-' + x.classDescription}))
      this.multiselectclassList = this.cas.removeDuplicatesFromArrayNew(this.multiselectclassList);

      res.forEach((x: { companyId: string; classDescription: string;}) => this.multiselectcompanyList.push({value: x.companyId, label:   x.companyId}))
      this.multiselectcompanyList = this.cas.removeDuplicatesFromArrayNew(this.multiselectcompanyList);



      this.ELEMENT_DATA = res;
      console.log(this.ELEMENT_DATA);
      this.dataSource = new MatTableDataSource<ClassElement>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
      this.selection = new SelectionModel<ClassElement>(true, []);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
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
  checkboxLabel(row?: ClassElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.classId + 1}`;
  }


  public lastScrolledHeight: number = 0;
  public showAddButton: boolean = false;

  @HostListener('window:scroll', ['$event']) onScroll(event: { path: any[]; }) {
    const window = event.path[1];
    const currentScrollHeight = window.scrollY;


    if (currentScrollHeight > this.lastScrolledHeight) {
      this.showAddButton = true;

    } else {
      this.showAddButton = false;

    }
    this.lastScrolledHeight = currentScrollHeight;
  }

  scrollToTop(el: { scrollTop: number; }) {
    el.scrollTop = 0;
  }

  searhform = this.fb.group({
    companyId: [],

    classId: [],
    classStatus: [],
    createdByFE: [],
    classStatusFE:[],
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

    
  if (this.selectedItems2 && this.selectedItems2.length > 0){
    let multistatusList: any[]=[]
    this.selectedItems2.forEach((a: any)=> multistatusList.push(a.id))
    this.searhform.patchValue({classStatus: this.selectedItems2 });
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