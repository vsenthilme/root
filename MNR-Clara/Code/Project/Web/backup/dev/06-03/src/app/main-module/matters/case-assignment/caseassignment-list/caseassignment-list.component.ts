import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { CaseAssignmentElement, CaseAssignmentService } from "../case-assignment.service";

interface SelectItem {
  id: string;
  itemName: string;
}


@Component({
  selector: 'app-caseassignment-list',
  templateUrl: './caseassignment-list.component.html',
  styleUrls: ['./caseassignment-list.component.scss']
})
export class CaseassignmentListComponent implements OnInit {
  screenid = 1118;
  openAssign(pageflow: any = 'Assign'): void {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    let paramdata = this.cs.encrypt({ code: this.selection.selected[0].matterNumber, pageflow: pageflow });
    this.router.navigate(['/main/matters/case-assignment/resource-assigment/' + paramdata]);


  }
  displayedColumns: string[] = ['select', 'matterNumber', 'clientId', 'name', 'partner', 'legalAssistant', 'caseCategoryId', 'originatingTimeKeeper', 'assignedTimeKeeper', 'responsibleTimeKeeper','referenceField2', 'referenceField1', 'statusIddes',];
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  ClientFilter: any;
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
  id: string | undefined;
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
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
  checkboxLabel(row?: any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.matterNumber + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  ELEMENT_DATA: CaseAssignmentElement[] = [];
  // displayedColumns: string[] = ['select', 'taskno', 'type', 'creation', 'deadline', 'remainder', 'originatting', 'responsible', 'legal', 'status',];

  dataSource = new MatTableDataSource<CaseAssignmentElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<CaseAssignmentElement>(true, []);

  constructor(public dialog: MatDialog,
    private service: CaseAssignmentService, private router: Router,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
  matterno: any = "";
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllListData();

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
      data: this.selection.selected[0].matterNumber,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].matterNumber);

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
  sub = new Subscription();
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }

  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination
  statuslist: any[] = [];
  caseCategoryIdList: any[] = [];
  clientIdList: any[] = [];
  matterlist: any[] = [];
  
  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  selectedItems3: SelectItem[] = [];
multiselectcasecatList: any[] = [];
multicasecatList: any[] = [];

selectedItems4: SelectItem[] = [];
multiselectclientList: any[] = [];
multiclientList: any[] = [];

selectedItems5: SelectItem[] = [];
multiselectmatternoList: any[] = [];
multimatternoList: any[] = [];

selectedItems6: SelectItem[] = [];
multiselectrespattornerList: any[] = [];
multirespattornerList: any[] = [];

selectedItems7: SelectItem[] = [];
multiselectassignattornerList: any[] = [];
multiassignattornerList: any[] = [];

selectedItems9: SelectItem[] = [];
multiselectpartnerList: any[] = [];
multipartnerList: any[] = [];

selectedItems8: SelectItem[] = [];
multiselectproginattornerList: any[] = [];
multiproginattornerList: any[] = [];
  
  dropdownSettings = {
    singleSelection: false,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };


  getall(excel: boolean = false) {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.caseCategoryId.url,
      this.cas.dropdownlist.client.clientId.url,
      this.cas.dropdownlist.matter.matterNumber.url,
    ]).subscribe((results) => {
    this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key).filter(s => [39].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label: x.value}))
      this.multiselectstatusList = this.multistatusList;

      this.caseCategoryIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.caseCategoryId.key);
      this.caseCategoryIdList.forEach((x: { key: string; value: string; }) => this.multicasecatList.push({value: x.key, label: x.key + '-' + x.value}))
      this.multiselectcasecatList = this.multicasecatList;

      this.clientIdList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.client.clientId.key);
      this.clientIdList.forEach((x: { key: string; value: string; }) => this.multiclientList.push({value: x.key, label: x.key + '-' + x.value}))
      this.multiselectclientList = this.multiclientList;

      this.matterlist = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.matter.matterNumber.key);
      this.matterlist.forEach((x: { key: string; value: string; }) => this.multimatternoList.push({value: x.key, label: x.key + '-' + x.value}))
      this.multiselectmatternoList = this.multimatternoList;
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: any[]) => {
        res.forEach((x: { responsibleTimeKeeper: string;}) => this.multirespattornerList.push({value: x.responsibleTimeKeeper, label:  x.responsibleTimeKeeper}))
        this.multiselectrespattornerList = this.multirespattornerList;
        this.multiselectrespattornerList = this.cas.removeDuplicatesFromArrayNew(this.multiselectrespattornerList);

        res.forEach((x: { originatingTimeKeeper: string;}) => this.multiproginattornerList.push({value: x.originatingTimeKeeper, label:  x.originatingTimeKeeper}))
        this.multiselectproginattornerList = this.multiproginattornerList;
        this.multiselectproginattornerList = this.cas.removeDuplicatesFromArrayNew(this.multiselectproginattornerList);

        res.forEach((x: { assignedTimeKeeper: string;}) => this.multiassignattornerList.push({value: x.assignedTimeKeeper, label:  x.assignedTimeKeeper}))
        this.multiselectassignattornerList = this.multiassignattornerList;
        this.multiselectassignattornerList = this.cas.removeDuplicatesFromArrayNew(this.multiselectassignattornerList);

        res.forEach((x: { partner: string;}) => this.multipartnerList.push({value: x.partner, label:  x.partner}))
        this.multiselectpartnerList = this.multipartnerList;
        this.multiselectpartnerList = this.cas.removeDuplicatesFromArrayNew(this.multiselectpartnerList);

        res.forEach((x) => {
          // x.matterNumber  = this.matterNumber list.find(y => y.key == x.noteTypeId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.caseCategoryId = this.caseCategoryIdList.find(y => y.key == x.caseCategoryId)?.value;
          x.name = this.clientIdList.find(y => y.key == x.clientId)?.value;
        })
        this.ELEMENT_DATA = res;

        if (excel)
          this.excel.exportAsExcel(res, "Case Assignment");
        this.dataSource = new MatTableDataSource<CaseAssignmentElement>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
        this.selection = new SelectionModel<CaseAssignmentElement>(true, []);
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
        "Matter ID ": x.matterNumber,
        'Client ID': x.clientId,
        "Legal Assistant": x.legalAssistant,
        "Partner  ": x.partner,
        'Client Name': x.name,
        'Case Category': x.caseCategoryId,
        'Paralegal ': x.referenceField2,
        'Originating Time Keeper ': x.originatingTimeKeeper,
        ' Assigned Time Keeper ': x.assignedTimeKeeper,
        'Responsible Time Keeper  ': x.responsibleTimeKeeper,
        'Law Clerk  ': x.referenceField1,
        'Status ': x.statusIddes,
      });

    })
    this.excel.exportAsExcel(res, "Case Assignment");
  }
  getAllListData() {
    this.getall();
  }

  searchStatusList = {
    statusId: [38, 39]
  };
  searhform = this.fb.group({
    assignedTimeKeeper: [],
    assignedTimeKeeperFE: [],
    caseCategoryId: [],
    caseCategoryIdFE: [],
    caseSubCategoryId: [],
    clientId: [],
    clientIdFE: [],
    legalAssistant: [],     
    matterNumber: [],
    matterNumberFE: [],
    originatingTimeKeeper: [],
    originatingTimeKeeperFE: [],
    partner: [],
    partnerFE: [],
    responsibleTimeKeeper: [],
    responsibleTimeKeeperFE: [],
    statusId: [],
    statusIdFE: []
  });
  Clear() {
    this.reset();
  };

  search() {
    
    // if (this.selectedItems2 && this.selectedItems2.length > 0){
    //   let multistatusList: any[]=[]
    //   this.selectedItems2.forEach((a: any)=> multistatusList.push(a.id))
    //   this.searhform.patchValue({statusId: this.selectedItems2 });
    // }
    // if (this.selectedItems9 && this.selectedItems9.length > 0){
    //   let multipartnerList: any[]=[]
    //   this.selectedItems9.forEach((a: any)=> multipartnerList.push(a.id))
    //   this.searhform.patchValue({partner: multipartnerList });
    // }
    // if (this.selectedItems3 && this.selectedItems3.length > 0){
    //   let multicasecatList: any[]=[]
    //   this.selectedItems3.forEach((a: any)=> multicasecatList.push(a.id))
    //   this.searhform.patchValue({caseCategoryId: multicasecatList });
    // }
    // if (this.selectedItems4 && this.selectedItems4.length > 0){
    //   let multiclientList: any[]=[]
    //   this.selectedItems4.forEach((a: any)=> multiclientList.push(a.id))
    //   this.searhform.patchValue({clientId: multiclientList });
    // }
    // if (this.selectedItems5 && this.selectedItems5.length > 0){
    //   let multimatternoList: any[]=[]
    //   this.selectedItems5.forEach((a: any)=> multimatternoList.push(a.id))
    //   this.searhform.patchValue({matterNumber: multimatternoList });
    // }
    // if (this.selectedItems6 && this.selectedItems6.length > 0){
    //   let multirespattornerList: any[]=[]
    //   this.selectedItems6.forEach((a: any)=> multirespattornerList.push(a.id))
    //   this.searhform.patchValue({responsibleTimeKeeper: multirespattornerList });
    // }

    // if (this.selectedItems7 && this.selectedItems7.length > 0){
    //   let multiassignattornerList: any[]=[]
    //   this.selectedItems7.forEach((a: any)=> multiassignattornerList.push(a.id))
    //   this.searhform.patchValue({assignedTimeKeeper: multiassignattornerList });
    // }

    // if (this.selectedItems8 && this.selectedItems8.length > 0){
    //   let multiproginattornerList: any[]=[]
    //   this.selectedItems8.forEach((a: any)=> multiproginattornerList.push(a.id))
    //   this.searhform.patchValue({originatingTimeKeeper: multiproginattornerList });
    // }

    this.spin.show();
    this.cas.getalldropdownlist([
      // this.cas.dropdownlist.setup.matterNumber .url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      // this.matterNumber list = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.matterNumber .key);
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {

        res.forEach((x) => {
          // x.noteTypeId = this.noteTypeIdlist.find(y => y.key == x.noteTypeId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.caseCategoryId = this.caseCategoryIdList.find(y => y.key == x.caseCategoryId)?.value;
          x.name = this.clientIdList.find(y => y.key == x.clientId)?.value;
        })
        this.ELEMENT_DATA = res;
        this.dataSource = new MatTableDataSource<CaseAssignmentElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<CaseAssignmentElement>(true, []);
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
  reset() {
    this.searhform.reset();
  }
}