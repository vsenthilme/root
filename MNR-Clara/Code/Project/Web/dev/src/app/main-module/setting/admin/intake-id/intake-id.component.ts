import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { IntakeidEditComponent } from "./intakeid-edit/intakeid-edit.component";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { IntakeElement, IntakeService } from "./intake.service";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { FormBuilder } from "@angular/forms";


interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-intake-id',
  templateUrl: './intake-id.component.html',
  styleUrls: ['./intake-id.component.scss']
})
export class IntakeIdComponent implements OnInit {

  sub = new Subscription();
  ELEMENT_DATA: IntakeElement[] = [];
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
    private service: IntakeService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cas: CommonApiService,
    private cs: CommonService,
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

  open_new_update(data: any = 'new'): void {
    if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    const dialogRef = this.dialog.open(IntakeidEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].intakeFormId : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      // this.getallationslist();
      window.location.reload();
    });
  }

  open_new(data: any = 'new'): void {

    const dialogRef = this.dialog.open(IntakeidEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.7%', }

    });

    dialogRef.afterClosed().subscribe(result => {

      //this.getallationslist();
      window.location.reload();
    });
  }
  screenid = 1011;
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
  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination

  

  selectedItems3: SelectItem[] = [];
  multiselectyseridList: any[] = [];
  multiyseridList: any[] = [];
  multiselectclassList: any[] = [];
  selectedItems2: SelectItem[] = [];
  multiselectIntakeList: any[] = [];
  multiselectclientList: any[] = [];

  
  dropdownSettings = {
    singleSelection: true,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };


  getall(excel: boolean = false) {
    let classIdList: any[] = [];
    let clientTypeIdList: any[] = [];
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.clientTypeId.url,
    ]).subscribe((results) => {
      classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      classIdList.forEach((x: { key: string; value: string; }) => this.multiselectclassList.push({value: x.value, label: x.value}))

      clientTypeIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.clientTypeId.key);
      clientTypeIdList.forEach((x: { key: string; value: string; }) => this.multiselectclientList.push({value: x.value, label: x.value}))
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: IntakeElement[]) => {

        res.forEach((x: { createdBy: string;}) => this.multiyseridList.push({value: x.createdBy, label: x.createdBy}))
        this.multiselectyseridList = this.multiyseridList;
          this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);

          res.forEach((x: { intakeFormDescription: string; intakeFormId: string;}) => this.multiselectIntakeList.push({value: x.intakeFormId, label: x.intakeFormDescription}))
            this.multiselectIntakeList = this.cas.removeDuplicatesFromArrayNew(this.multiselectIntakeList);

            
      

        res.forEach((x) => {
          x.classId = classIdList.find(y => y.key == x.classId)?.value;
          x.clientTypeId = clientTypeIdList.find(y => y.key == x.clientTypeId)?.value;
        })
        
        this.ELEMENT_DATA = res;

        if (excel)
          this.excel.exportAsExcel(res, "Intake");
        this.dataSource = new MatTableDataSource<IntakeElement>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
        this.selection = new SelectionModel<IntakeElement>(true, []);
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
        'Language ': x.languageId,
        " Client type ID": x.clientTypeId,
        " Intake Form ID": x.intakeFormId,
        'Intake Form Description': x.intakeFormDescription,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Intake");
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
      data: this.selection.selected[0].intakeFormId,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].intakeFormId);

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


  displayedColumns: string[] = ['select', 'classId', 'languageId', 'clientTypeId', 'intakeFormId', 'intakeFormDescription', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource<IntakeElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<IntakeElement>(true, []);

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
  checkboxLabel(row?: IntakeElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.intakeFormId + 1}`;
  }

  searhform = this.fb.group({
    classId: [],
    intakeFormId: [],
    intakeFormDescription: [],
    clientTypeId: [],
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
    if (this.selectedItems2 && this.selectedItems2.length > 0){
      let multiyList: any[]=[]
      this.selectedItems2.forEach((a: any)=> multiyList.push(a.id))
      this.searhform.patchValue({classId: this.selectedItems2 });
    }
console.log(this.searhform.controls.classId.value)
    let data = this.cs.filterArray(this.ELEMENT_DATA, this.searhform.getRawValue())
console.log(data)
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