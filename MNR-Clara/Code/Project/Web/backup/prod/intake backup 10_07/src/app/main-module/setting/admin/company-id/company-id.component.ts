import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { CompanyEditComponent } from "./company-edit/company-edit.component";
import { ToastrService } from "ngx-toastr";
import { NgxSpinnerService } from "ngx-spinner";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { CompanyElement, CompanyService } from "./company.service";
import { Subscription } from "rxjs";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { FormBuilder } from "@angular/forms";
import { CommonApiService } from "src/app/common-service/common-api.service";

interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-company-id',
  templateUrl: './company-id.component.html',
  styleUrls: ['./company-id.component.scss']
})
export class CompanyIdComponent implements OnInit {
  screenid = 1082;
  public icon = 'expand_more';
  sub = new Subscription();
  ELEMENT_DATA: CompanyElement[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  constructor(public dialog: MatDialog,
    private service: CompanyService,
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
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);

    this.getAllListData();
  }

  displayedColumns: string[] = ['select', 'companyId', 'languageId', 'companyName', 'companyAddress1', 'companyAddress2', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource<CompanyElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<CompanyElement>(true, []);



  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Company ID": x.companyId,
        'Language ID ': x.languageId,
        " Address 1": x.companyAddress1,
        'Address 2': x.companyAddress2,
        'Company Name': x.companyName,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Company");
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
      data: this.selection.selected[0].companyId,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].companyId);

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

      this.spin.hide(); //this.getAllListData();
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
    const dialogRef = this.dialog.open(CompanyEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].companyId : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      //this.getAllListData();
      window.location.reload();
    });
  }
  openDialog2(data: any = 'new'): void {

    const dialogRef = this.dialog.open(CompanyEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {

      // this.getAllListData();
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

  dropdownSettings = {
    singleSelection: false,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  
  selectedItems7: SelectItem[] = [];
  multiselectcreatedList: any[] = [];
  multicreatedList: any[] = [];

  multiselectcompanyList: any[] = [];
  multicompanyList: any[] = [];


  getAllListData() {
    this.spin.show();
    this.sub.add(this.service.Getall().subscribe((res: CompanyElement[]) => {

      res.forEach((x: { createdBy: string;}) => this.multicreatedList.push({value: x.createdBy, label:  x.createdBy}))
      this.multiselectcreatedList = this.multicreatedList;
      this.multiselectcreatedList = this.cas.removeDuplicatesFromArrayNew(this.multiselectcreatedList);

      res.forEach((x: { companyId: string; companyName: string;}) => this.multicompanyList.push({value: x.companyId, label:   x.companyId + '-' + x.companyName}))
      this.multiselectcompanyList = this.multicompanyList;
      this.multiselectcompanyList = this.cas.removeDuplicatesFromArrayNew(this.multiselectcompanyList);


      this.ELEMENT_DATA = res;
      console.log(this.ELEMENT_DATA);
      this.dataSource = new MatTableDataSource<CompanyElement>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
      this.selection = new SelectionModel<CompanyElement>(true, []);
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
  checkboxLabel(row?: CompanyElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.companyId + 1}`;
  }




  searhform = this.fb.group({
    companyId: [],
    companyName: [],
    createdByFE : [],
    createdBy: [],
    createdOn_from: [],
    createdOn_to: [],

  });

  search() {

    if (this.selectedItems7 && this.selectedItems7.length > 0){
      let multicreatedList: any[]=[]
      this.selectedItems7.forEach((a: any)=> multicreatedList.push(a.id))
      this.searhform.patchValue({createdBy: this.selectedItems7 });
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
