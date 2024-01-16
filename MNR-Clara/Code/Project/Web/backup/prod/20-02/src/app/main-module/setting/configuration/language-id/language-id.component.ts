import { SelectionModel } from "@angular/cdk/collections";
import { AfterViewInit, Component, OnInit, ViewChild } from "@angular/core";
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
import { LanguageEditComponent } from "./language-edit/language-edit.component";
import { LanguageElement, LanguageService } from "./language.service";



// const ELEMENT_DATA: LanguageElement[] = [
//   { class: "1001", language: 'AP-Noneditable', text: 'text', by: 'Admin', on: '08-05-2021' },
//   { class: "1002", language: 'AP-Noneditable', text: 'text', by: 'Admin', on: '08-05-2021' },
//   { class: "1003", language: 'AP-Noneditable', text: 'text', by: 'Admin', on: '08-05-2021' },
//   { class: "1004", language: 'AP-Noneditable', text: 'text', by: 'Admin', on: '08-05-2021' },
//   { class: "1005", language: 'AP-Noneditable', text: 'text', by: 'Admin', on: '08-05-2021' },
//   { class: "1006", language: 'AP-Noneditable', text: 'text', by: 'Admin', on: '08-05-2021' },
//   { class: "1007", language: 'AP-Noneditable', text: 'text', by: 'Admin', on: '08-05-2021' },
//   { class: "1008", language: 'AP-Noneditable', text: 'text', by: 'Admin', on: '08-05-2021' },
//   { class: "1009", language: 'AP-Noneditable', text: 'text', by: 'Admin', on: '08-05-2021' },
// ];
interface SelectItem {
  id: string;
  itemName: string;
}
@Component({
  selector: 'app-language-id',
  templateUrl: './language-id.component.html',
  styleUrls: ['./language-id.component.scss']
})
export class LanguageIdComponent implements OnInit, AfterViewInit {
  list = [{ key: 'text', value: 'text 1' }];
  screenid = 1003;
  sub = new Subscription();
  ELEMENT_DATA: LanguageElement[] = [];
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement)?.value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
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
    private service: LanguageService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,
    private cas: CommonApiService) { }

  openDialog2(data: any = 'new'): void {
    if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    const dialogRef = this.dialog.open(LanguageEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].languageId : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      // this.getallationslist();
      window.location.reload();
    });
  }
  classIdList: any[] = [];

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Class": x.classId,
        'Language ID  ': x.languageId,
        "Description": x.languageDescription,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Language");
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
      data: this.selection.selected[0].languageId,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].languageId);

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

      this.spin.hide();// this.getallationslist();
      window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  openDialog(): void {

    const dialogRef = this.dialog.open(LanguageEditComponent, {
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
  selectedItems3: SelectItem[] = [];
  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];


dropdownSettings = {
  singleSelection: true,
  text:"Select",
  selectAllText:'Select All',
  unSelectAllText:'UnSelect All',
  enableSearchFilter: true,
  badgeShowLimit: 2,
  disabled: false
};

multilanguageList: any[] = [];   
multiselectclassList: any[] = [];      
  getallationslist() {

    let classIdList: any[] = [];
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url]).subscribe((results) => {
      classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      classIdList.forEach((x: { key: string; value: string; }) => this.multiselectclassList.push({value: x.value, label: x.value}))
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: LanguageElement[]) => {
        res.forEach((x: { createdBy: string;}) => this.multiyseridList.push({value: x.createdBy, label: x.createdBy}))
        this.multiselectyseridList = this.multiyseridList;
          this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);
  
        res.forEach((x) => {
          x.classId = classIdList.find(y => y.key == x.classId)?.value;
        })

        res.forEach((x: { languageId: string;languageDescription: string}) => this.multilanguageList.push({value: x.languageId, label: x.languageId + '-' + x.languageDescription}))
        this.multilanguageList = this.cas.removeDuplicatesFromArrayNew(this.multilanguageList);

        this.ELEMENT_DATA = res;

        this.dataSource = new MatTableDataSource<LanguageElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<LanguageElement>(true, []);
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



  searhform = this.fb.group({
    classId: [],
    createdBy: [],
    createdByFE: [],
    languageId: [],

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

    this.dataSource = new MatTableDataSource<LanguageElement>(data);

    this.selection = new SelectionModel<LanguageElement>(true, []);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

  }
  Cancel() {
    this.reset();

    this.dataSource = new MatTableDataSource<LanguageElement>(this.ELEMENT_DATA);

    this.selection = new SelectionModel<LanguageElement>(true, []);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  displayedColumns: string[] = ['select', 'classId', 'languageId', 'languageDescription', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource<LanguageElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<LanguageElement>(true, []);

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
  checkboxLabel(row?: LanguageElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.classId + 1}`;
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
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
