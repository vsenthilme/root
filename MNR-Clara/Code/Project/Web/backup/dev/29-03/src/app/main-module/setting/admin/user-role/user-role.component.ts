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
import { CommonService, dropdownelement } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { LanguageEditComponent } from "../../configuration/language-id/language-edit/language-edit.component";
import { UserTypeService } from "../usertype-id/user-type.service";
import { UsertypeEditComponent } from "../usertype-id/usertype-edit/usertype-edit.component";
import { UserRoleService } from "./user-role.service";
interface SelectItem {
  id: string;
  itemName: string;
}


export interface userrole {


  role: string;
  name: string;
  status: string;
  by: string;
  on: string;
}
const ELEMENT_DATA: userrole[] = [
  { role: "1001", name: 'AP-Noneditable', status: 'text', by: 'Admin', on: '08-05-2021' },


];
@Component({
  selector: 'app-user-role',
  templateUrl: './user-role.component.html',
  styleUrls: ['./user-role.component.scss']
})
export class UserRoleComponent implements OnInit {
  screenid = 1013;



  displayedColumns: string[] = ['select', 'role', 'name', 'status', 'by', 'on'];

  public icon = 'expand_more';
  sub = new Subscription();
  ELEMENT_DATA: any[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  constructor(public dialog: MatDialog,
    private service: UserRoleService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private router: Router,
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

    const dialogRef = this.dialog.open(UsertypeEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {


    });
  }
  userdisplay() {
    this.router.navigate(["/main/setting/admin/userroleedit"]);

  }
  RA: any = {};
  
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);

    this.getAllListData();


    this.StatusList.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
    this.multiselectstatusList = this.multistatusList;

  }
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);


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
      data: this.selection.selected[0].userRoleId,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].userRoleId);

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
    //  window.location.reload();
    this.getAllListData();
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
    let paramdata;
    if (this.selection.selected.length > 0) {

      paramdata = this.cs.encrypt({ code: this.selection.selected[0].userRoleId, pageflow: data });

      this.router.navigate(['/main/setting/admin/userroleedit/' + paramdata]);
    }
    else {
      paramdata = this.cs.encrypt({ pageflow: data });

      this.router.navigate(['/main/setting/admin/userroleedit/' + paramdata]);
    }
  }
  openDialog2(data: any = 'new'): void {

    const dialogRef = this.dialog.open(UsertypeEditComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe(result => {

    //  window.location.reload();
      this.getAllListData();
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
  multiselectuserroleList: any[] = [];
  multiuserroleList: any[] = [];

  selectedItems3: SelectItem[] = [];
  multiselectusernameList: any[] = [];
  multiusernameList: any[] = [];

  selectedItems4: SelectItem[] = [];
  multiselectcreatedbyList: any[] = [];
  multicreatedbyList: any[] = [];

  selectedItems5: SelectItem[] = [];
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


  getall(excel: boolean = false) {
    let classIdList: any[] = [];
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    ]).subscribe((results) => {
      classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: any[]) => {
        this.ELEMENT_DATA = [];
        // res = [...new Set(res.map(item => item.userRoleId, item.userRoleName, item.roleStatus))];
        res.forEach((x) => {
          x.classIddes = classIdList.find(y => y.key == x.classId)?.value;
          x.statusId = x.statusId == true ? 'Active' : 'InActive';
          let filter = this.cs.filterArray(this.ELEMENT_DATA, { userRoleId: x.userRoleId })
          if (filter.length == 0)
            this.ELEMENT_DATA.push(x);
        })

        res.forEach((x: { userRoleId: string;}) => this.multiuserroleList.push({value: x.userRoleId, label:  x.userRoleId}))
        this.multiselectuserroleList = this.multiuserroleList;
        this.multiselectuserroleList = this.cas.removeDuplicatesFromArrayNew(this.multiselectuserroleList);


        res.forEach((x: { userRoleName: string;}) => this.multiusernameList.push({value: x.userRoleName, label:  x.userRoleName}))
        this.multiselectusernameList = this.multiusernameList;
        this.multiselectusernameList = this.cas.removeDuplicatesFromArrayNew(this.multiselectusernameList);

        res.forEach((x: { createdBy: string;}) => this.multicreatedbyList.push({value: x.createdBy, label:  x.createdBy}))
        this.multiselectcreatedbyList = this.multicreatedbyList;
        this.multiselectcreatedbyList = this.cas.removeDuplicatesFromArrayNew(this.multiselectcreatedbyList);



        if (excel)
          this.excel.exportAsExcel(this.ELEMENT_DATA, "User Role");
        this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<any>(true, []);
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
        " User Role ID": x.userRoleId,
        ' Role Name  ': x.userRoleName,
        "Status": x.roleStatus,
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn)
      });

    })
    this.excel.exportAsExcel(res, "User Role");
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
  checkboxLabel(row?: any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.userTypeId + 1}`;
  }
  StatusList: any[] = [{ key: 'ACTIVE', value: 'ACTIVE' }, { key: 'INACTIVE', value: 'INACTIVE' }]

  searhform = this.fb.group({
    userRoleName: [],
    userRoleId: [],
    roleStatus: [],
    createdBy: [],
    userRoleNameFE: [],
    userRoleIdFE: [],
    roleStatusFE: [],
    createdByFE: [],
    createdOn_from: [],
    createdOn_to: [],

  });

  search() {

    // if (this.selectedItems2 && this.selectedItems2.length > 0){
    //   let multiuserroleList: any[]=[]
    //   this.selectedItems2.forEach((a: any)=> multiuserroleList.push(a.id))
    //   this.searhform.patchValue({userRoleId: multiuserroleList });
    // }
    // if (this.selectedItems3 && this.selectedItems3.length > 0){
    //   let multiusernameList: any[]=[]
    //   this.selectedItems3.forEach((a: any)=> multiusernameList.push(a.id))
    //   this.searhform.patchValue({userRoleName: multiusernameList });
    // }   
    // if (this.selectedItems4 && this.selectedItems4.length > 0){
    //   let multicreatedbyList: any[]=[]
    //   this.selectedItems4.forEach((a: any)=> multicreatedbyList.push(a.id))
    //   this.searhform.patchValue({createdBy: this.selectedItems4 });
    // }
    // if (this.selectedItems5 && this.selectedItems5.length > 0){
    //   let multistatusList: any[]=[]
    //   this.selectedItems5.forEach((a: any)=> multistatusList.push(a.id))
    //   this.searhform.patchValue({roleStatus: this.selectedItems5 });
    // }

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