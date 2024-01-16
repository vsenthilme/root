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
import { LAndECaseElement, LAndECaseService } from "../l-and-ecase.service";

interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-land-e-list',
  templateUrl: './land-e-list.component.html',
  styleUrls: ['./land-e-list.component.scss']
})
export class LandEListComponent implements OnInit {
  screenid = 1094;
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
  checkboxLabel(row?: LAndECaseElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  ELEMENT_DATA: LAndECaseElement[] = [];

  displayedColumns: string[] = ['select', 'actions', 'id', 'clientId', 'firstNameLastName', 'matterNumber', 'matterDescription', 'createdBy', 'createdOn', 'statusIddes'];
  dataSource = new MatTableDataSource<LAndECaseElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<LAndECaseElement>(true, []);

  constructor(public dialog: MatDialog,
    private service: LAndECaseService, private router: Router,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
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
      data: this.selection.selected[0].id,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].id);

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
  openDialog(data: any = 'new'): void {
    if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    let paramdata = "";

    if (this.selection.selected.length > 0) {
      paramdata = this.cs.encrypt({ code: this.selection.selected[0].id, clientId: this.selection.selected[0].clientId, pageflow: data });
      this.router.navigate(['/main/matters/case-info/landenew/' + paramdata]);
    }
    else {
      paramdata = this.cs.encrypt({ pageflow: data });
      this.router.navigate(['/main/matters/case-info/landenew/' + paramdata]);
    }

    // const dialogRef = this.dialog.open(CasecategoryDisplayComponent, {
    //   disableClose: true,
    //   width: '50%',
    //   maxWidth: '80%',
    //   position: { top: '6.5%' },
    //   data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].clientId : null }
    // });

    // dialogRef.afterClosed().subscribe(result => {

    //   this.getAllListData();
    // });
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
  classIdList: any[] = [];
  statuslist: any[] = [];
  clientlist: any[] = [];

  getall(excel: boolean = false) {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.client.clientId.url,
    this.cas.dropdownlist.matter.matterNumber.url,
    ]).subscribe((results) => {
      this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);

      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [1, 26, 2].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
      this.multiselectstatusList = this.multistatusList;
      console.log(this.multiselectstatusList)
    
      this.clientlist = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.client.clientId.key);
      this.clientlist.forEach((x: { key: string; value: string; }) => this.multiclientList.push({value: x.key, label: x.key + '-' + x.value}))
      this.multiselectclientList = this.multiclientList;

      this.matterlist = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.matter.matterNumber.key, { classId: 1 });
      this.matterlist.forEach((x: { key: string; value: string; }) => this.multimatternoList.push({value: x.key, label: x.key + '-' + x.value}))
      this.multiselectmatternoList = this.multimatternoList;

      this.sub.add(this.service.Getall().subscribe((res: LAndECaseElement[]) => {
        console.log(res)
        if (this.auth.classId != '3')
          this.ELEMENT_DATA = res.filter(x => x.classId === Number(this.auth.classId));
        else
          this.ELEMENT_DATA = res;
        this.ELEMENT_DATA.forEach((x) => {
          x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.firstNameLastName = this.clientlist.find(y => y.key == x.clientId)?.value;
        })

        this.caseinfolist = this.cas.foreachlist_searchpage(this.ELEMENT_DATA, this.cas.dropdownlist.caseinfo.caseinfoId.key);
        this.caseinfolist.forEach((x: { key: string; value: string; }) => this.multicaseinfoList.push({value: x.key, label:  x.value}))
        this.multiselectcaseinfoList = this.multicaseinfoList;
        this.spin.hide();
        if (excel)
          this.excel.exportAsExcel(this.ELEMENT_DATA);
        this.dataSource = new MatTableDataSource<LAndECaseElement>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
        this.selection = new SelectionModel<LAndECaseElement>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });

  }
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Case Info No": x.id,
        'Client No': x.clientId,
        "Client Name ": x.firstNameLastName,
        "Case No ": x.matterNumber,
        'Matter Description': x.matterDescription,
        "Status  ": x.statusId,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn),
      });

    })
    this.excel.exportAsExcel(res, "L&E");
  }
  getAllListData() {
    this.getall();
  }



  searhform = this.fb.group({
    caseInformationId: [,],
    caseInformationIdFE: [,],
    clientId: [,], 
    clientIdFE: [,],
  //  createdBy: [],
   endCreatedOn: [,],
    //firstNameLastName: [],
    matterNumber: [,],   
     matterNumberFE: [,],
    startCreatedOn: [,],
  statusId: [,],
  statusIdFE: [,],
  });
  Clear() {
    this.reset();
  };
caseinfolist: any[] = [];
matterlist: any[] = [];

selectedItems: SelectItem[] = [];
multiselectclientList: any[] = [];
multiclientList: any[] = [];

selectedItems2: SelectItem[] = [];
multiselectmatternoList: any[] = [];
multimatternoList: any[] = [];

selectedItems3: SelectItem[] = [];
multiselectcaseinfoList: any[] = [];
multicaseinfoList: any[] = [];

selectedItems4: any[] = [];
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


  search() {
    this.searhform.controls.startCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.startCreatedOn.value));
    this.searhform.controls.endCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endCreatedOn.value));
    // if (this.selectedItems3 && this.selectedItems3.length > 0){
    //   let multicaseinfoList: any[]=[]
    //   this.multicaseinfoList.forEach((a: any)=> multicaseinfoList.push(a.id))
    //   this.searhform.patchValue({caseInformationId: multicaseinfoList });
    // }
    
    // if (this.selectedItems3 && this.selectedItems3.length > 0){
    //   let multicaseinfoList: any
    //   this.searhform.patchValue({caseInformationId: this.selectedItems3[0].id });
    // }
    // else{
    //   this.searhform.patchValue({caseInformationId: null});
    // }

    // if (this.selectedItems && this.selectedItems.length > 0){
    //   this.searhform.patchValue({clientId: this.selectedItems[0].id });
    // }else{
    //   this.searhform.patchValue({clientId: null});
    // }

    // if (this.selectedItems2 && this.selectedItems2.length > 0){
    //   this.searhform.patchValue({matterNumber: this.selectedItems2[0].id });
    // }else{
    //   this.searhform.patchValue({matterNumber: null});
    // }
    // if (this.selectedItems4 && this.selectedItems4.length > 0){
    //   this.searhform.patchValue({statusId: this.selectedItems4[0] });
    // }else{
    //   this.searhform.patchValue({statusId: null});
    // }

      this.spin.show();
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {

        res.forEach((x) => {
          x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.firstNameLastName = this.clientlist.find(y => y.key == x.clientId)?.value;
        })

        this.dataSource = new MatTableDataSource<any>(res);
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.spin.hide();
      }, (err: any) => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    this.spin.hide();
  }
  creatematter(code: any) {


    if (!code.matterDescription) {
      this.toastr.error("Kindly fill the Matter Description", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    this.spin.show();

    this.sub.add(this.service.CreateMatter(code.id).subscribe((res: any) => {
      this.toastr.success(res.matterNumber + " created successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.spin.hide();
      this.getall();

    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  reset() {
    this.searhform.reset();
  }
}



