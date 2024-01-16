import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { ToastrService } from "ngx-toastr";
import { NgxSpinnerService } from "ngx-spinner";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { Subscription } from "rxjs";
import { AgreementService } from "src/app/main-module/crm/agreement-snap/agreement.service";
import { AgreementElement, AgreementTemplateService } from "./agreement-template.service";
import { AgreementtemplateDisplayComponent } from "./agreementtemplate-display/agreementtemplate-display.component";
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { FormBuilder } from "@angular/forms";
import { Router } from "@angular/router";



interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-agreeement-template',
  templateUrl: './agreeement-template.component.html',
  styleUrls: ['./agreeement-template.component.scss']
})
export class AgreeementTemplateComponent implements OnInit {

  StatusList: dropdownelement[] = [{ key: 'Active', value: 'Active' }, { key: 'Inactive', value: 'Inactive' }]


  screenid = 1047;
  public icon = 'expand_more';
  sub = new Subscription();
  ELEMENT_DATA: AgreementElement[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  constructor(public dialog: MatDialog,
    private service: AgreementTemplateService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService, private router: Router,
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

    const dialogRef = this.dialog.open(AgreementtemplateDisplayComponent, {
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
  displayedColumns: string[] = ['select', 'classId', 'agreementCode', 'mailme','url', 'agreementFileDescription', 'agreementStatus', 'createdBy', 'createdOn'];
  dataSource = new MatTableDataSource<AgreementElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<AgreementElement>(true, []);


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
      data: this.selection.selected[0].agreementCode,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].agreementCode);

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
      this.getAllListData();
   //   window.location.reload();
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
    const dialogRef = this.dialog.open(AgreementtemplateDisplayComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].agreementCode : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      this.getAllListData();
    // window.location.reload();
    });
  }
  openDialog2(data: any = 'new'): void {

    const dialogRef = this.dialog.open(AgreementtemplateDisplayComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {

      this.getAllListData();
   //   window.location.reload();
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
  multiselectcasecatList: any[] = [];  
  multiselectagreementList: any[] = [];  

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
      caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multiselectcasecatList.push({value: x.value, label: x.value}))

      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: AgreementElement[]) => {
        res.forEach((x: { createdBy: string;}) => this.multiyseridList.push({value: x.createdBy, label: x.createdBy}))
        this.multiselectyseridList = this.multiyseridList;
          this.multiselectyseridList = this.cas.removeDuplicatesFromArrayNew(this.multiyseridList);
          
          res.forEach((x: { agreementCode: string; agreementFileDescription: string;}) => this.multiselectagreementList.push({value: x.agreementCode, label: x.agreementFileDescription}))
          this.multiselectagreementList = this.cas.removeDuplicatesFromArrayNew(this.multiselectagreementList);

        res.forEach((x) => {
          x.classId = classIdList.find(y => y.key == x.classId)?.value;
          x.caseCategoryId = caseCategoryIdList.find(y => y.key == x.caseCategoryId)?.value;
        })

        this.ELEMENT_DATA = res;

        if (excel)
          this.excel.exportAsExcel(res, "Agreement Template");
        this.dataSource = new MatTableDataSource<AgreementElement>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
        this.selection = new SelectionModel<AgreementElement>(true, []);
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
        "Class": x.classId,
        "Agreement Code ": x.agreementCode,
        "Mail Merge": (x.mailMerge == true ? 'Yes' : 'No'),
        "Agreement File Description ": x.agreementFileDescription,
        'Status': x.agreementStatus,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "Agreement Template");
  }
  getAllListData() {
    this.getall();
  }
  open_agreement(data: any): void {
   // window.open('main/setting/business/agreementtemplateview/' + this.cs.encrypt(data));
    this.router.navigate(['main/setting/business/agreementtemplateview/' + this.cs.encrypt(data)]);

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
  checkboxLabel(row?: AgreementElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.agreementCode + 1}`;
  }
  searhform = this.fb.group({
    classId: [],
    caseCategoryId: [],
    agreementCode: [],
    agreementStatus: [],
    agreementFileDescription: [],
    classStatus: [],
    classStatusFE:[],
    createdBy: [],
    createdByFE: [],
    createdOn_from: [],
    createdOn_to: [],

  });

  search() {

    // if (this.selectedItems2 && this.selectedItems2.length > 0){
    //   let multistatusList: any[]=[]
    //   this.selectedItems2.forEach((a: any)=> multistatusList.push(a.id))
    //   this.searhform.patchValue({classStatus: this.selectedItems2 });
    // }

    // if (this.selectedItems3 && this.selectedItems3.length > 0){
    //   let multiyseridList: any[]=[]
    //   this.selectedItems3.forEach((a: any)=> multiyseridList.push(a.id))
    //   this.searhform.patchValue({createdBy: this.selectedItems3 });
    // }

    let data = this.cs.filterArray(this.ELEMENT_DATA, this.searhform.getRawValue())
console.log(this.searhform.getRawValue())
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