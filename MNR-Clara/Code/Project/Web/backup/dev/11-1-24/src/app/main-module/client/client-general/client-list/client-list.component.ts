import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { ClientGeneralElement, ClientGeneralService } from "../client-general.service";


interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-client-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.scss']
})
export class ClientListComponent implements OnInit {
  screenid = 1084;
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  showFiller = false;
  animal: string | undefined;
  id: string | undefined;

  ELEMENT_DATA: ClientGeneralElement[] = [];
  displayedColumns: string[] = ['select', 'classID', 'clientId', 'firstName', 'emailId', 'contactNumber', 'addressLine1', 'form', 'createdOnString', 'statusId', 'corporationClientId',];
  dataSource = new MatTableDataSource<ClientGeneralElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<ClientGeneralElement>(true, []);

  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination


  RA: any = {};
  sub = new Subscription();


  classList: any[] = [];
  statusList: any[] = [];
  clientList: any[] = [];

  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  pageNumber = 0;
  pageSize = 10;
  totalRecords = 0;
  showListByFilter = false;

  searchStatusList = { statusId: [18, 21] };
  intakeFormNumberList: dropdownelement[] = [];
  searhform = this.fb.group({
    classId: [],
    addressLine1: [],
    clientId: [],
    contactNumber: [],
    emailId: [],
    endCreatedOn: [],
    endCreatedOnFE: [],
    startCreatedOnFE: [],
    firstNameLastName: [],
    intakeFormNumber: [],
    startCreatedOn: [],
    statusId: [],
    endCreatedOnString: [],
    startCreatedOnString: [],
  });

  classObj: any[] = [];

  constructor(
    public dialog: MatDialog,
    private service: ClientGeneralService,
    private router: Router,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService
  ) { }

  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];
  multiselectClassList: any[] = [];
  filterClassList: any[] = [];
  StatusList: dropdownelement[] = [{ key: '18', value: 'Active' }, { key: '21', value: 'Inactive' }]

  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    if (this.auth.classId != '3') {
      this.classObj.push(this.auth.classId);
    } else {
      this.classObj.push(1, 2);
    }
    this.getSearchDropDown();
    this.getAllDropdownList();

    this.StatusList.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
    this.multiselectstatusList = this.multistatusList;

  }

  getAllDropdownList() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url
    ]).subscribe((results) => {
      this.classList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      //CLARA/AMS/2022/021
      this.classList.forEach((x: { key: string; value: string; }) => this.filterClassList.push({ value: x.key, label: x.value }));
      this.multiselectClassList = this.filterClassList;
      //end of CLARA/AMS/2022/021
      this.getAllListData(false);
    }, (err) => {
      this.toastr.error(err, "");
    });
    this.spin.hide();
  }

  getSearchDropDown() {
    this.sub.add(this.service.getAllSearchDropDown().subscribe((data: any) => {
      if (data) {
        data.forEach(client => {
          this.clientList.push({ value: client.key, label:  client.key + '-' + client.value });
        });
      }
    }, (err) => {
      this.toastr.error(err, "");
    }));
  }

  getAllListData(excel: boolean = false) {
    this.spin.show();
    // this.sub.add(this.service.getAllByPagination(this.pageNumber, this.pageSize, this.classObj).subscribe((res: any) => {
      this.sub.add(this.service.SearchNew(this.searhform.getRawValue()).subscribe((res: any[]) => {
      this.showListByFilter = false;
      this.spin.hide();
 
      this.ELEMENT_DATA = res;
      this.ELEMENT_DATA.forEach((x) => {
       // x.classId = this.classList.find(y => y.key == x.classId)?.value;
        x.corporationClientId = this.clientList.find(y => y.value == x.corporationClientId)?.label;
      })
      if (excel)
        this.excel.exportAsExcel(this.ELEMENT_DATA.sort((a, b) => (a.clientId > b.clientId) ? -1 : 1));
      this.dataSource = new MatTableDataSource<ClientGeneralElement>(this.ELEMENT_DATA);
      this.selection = new SelectionModel<ClientGeneralElement>(true, []);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  pageHandler($event: PageEvent) {
    this.pageNumber = $event.pageIndex;
    this.pageSize = $event.pageSize;
    this.getAllListData(false);
  }

  search() {
    this.searhform.controls.endCreatedOnString.patchValue(this.cs.dateNewFormat1(this.searhform.controls.endCreatedOnFE.value));
    this.searhform.controls.startCreatedOnString.patchValue(this.cs.dateNewFormat1(this.searhform.controls.startCreatedOnFE.value));
    this.spin.show();

    this.sub.add(this.service.SearchNew(this.searhform.getRawValue()).subscribe((res: ClientGeneralElement[]) => {
      this.showListByFilter = true;
      if (this.auth.classId != '3')
        this.ELEMENT_DATA = res.filter(x => x.classId === Number(this.auth.classId));
      else
        this.ELEMENT_DATA = res;
  //     this.ELEMENT_DATA.forEach((x) => {
  // //      x.classId = this.classList.find(y => y.key == x.classId)?.value;
  //     })

      this.dataSource = new MatTableDataSource<ClientGeneralElement>(this.ELEMENT_DATA.sort((a, b) => (a.clientId > b.clientId) ? -1 : 1));
      this.selection = new SelectionModel<ClientGeneralElement>(true, []);
      this.dataSource.sort = this.sort;
      this.totalRecords = this.ELEMENT_DATA.length;
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.spin.show();
   //    this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((data) => {
    //   data.forEach(x => {
  this.dataSource.data.forEach(x => {
      res.push({
        "Class": x.classId,
        "Client ID": x.clientId,
        'Name': x.firstNameLastName,
        "Email  ": x.emailId,
        "Phone ": x.contactNumber,
        'Address': x.addressLine1,
        "Status  ": x.statusId,
        'Created Date': this.cs.dateapi(x.createdOnString),
        'Corporation': x.corporationClientId,
      });
    })
    this.spin.hide();
    this.excel.exportAsExcel(res, "Client");
  //  }))
    
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
  checkboxLabel(row?: ClientGeneralElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.potentialClientId + 1}`;
  }

  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
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
      data: this.selection.selected[0].clientId,
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleterecord(this.selection.selected[0].clientId);
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
      paramdata = this.cs.encrypt({ code: this.selection.selected[0].clientId, code1: this.selection.selected[0].firstNameLastName, pageflow: data });
      sessionStorage.setItem('client', paramdata);
   //this.router.navigate(['/main/client/clientupdate/' + paramdata]);
          const url = this.router.serializeUrl(
          this.router.createUrlTree(['/main/client/clientupdate/' + paramdata])
        );
        
        window.open('#' + url, '_blank');
    }
    else {
      console.log(data)
      paramdata = this.cs.encrypt({ pageflow: data });

      sessionStorage.setItem('client', paramdata);
      this.router.navigate(['/main/client/clientNew/' + paramdata]);
      // const url = this.router.serializeUrl(
      //   this.router.createUrlTree(['/main/client/clientNew/' + paramdata])
      // );
      
      // window.open('#' + url, '_blank');
    }
  }

  Clear() {
    this.reset();
  };

  open_Intake(data: any, type = 'Display') {
    let formname = this.cs.customerformname(data.intakeFormId);
    if (formname == '') {
      this.toastr.error(
        "Intake form is not available for Direct Clients", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.cs.notifyOther(true);
      return;
    }
    this.cs.notifyOther(false);
    data.pageflow = type;
    this.router.navigate(['/main/crm/' + formname + '/' + this.cs.encrypt(data)]);

  }
  reset() {
    this.searhform.reset();
    this.getAllListData();
  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }
  }
}
