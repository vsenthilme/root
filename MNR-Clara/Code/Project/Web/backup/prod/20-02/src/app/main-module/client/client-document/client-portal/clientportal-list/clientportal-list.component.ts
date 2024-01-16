import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { ClientDocumentService } from "../../client-document.service";
import { SendIntakeComponent } from "../../send-intake-popup/send-intake.component";
import { ClientportalDisplayComponent } from "./clientportal-display/clientportal-display.component";

@Component({
  selector: 'app-clientportal-list',
  templateUrl: './clientportal-list.component.html',
  styleUrls: ['./clientportal-list.component.scss']
})
export class ClientportalListComponent implements OnInit {
  ELEMENT_DATA: any[] = [];
  screenid = 1156;
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  sub = new Subscription;

  showFiller = false;
  animal: string | undefined;
  id: string | undefined;

  displayedColumns: string[] = ['select', 'action', 'clientId', 'clientUserId', 'eligibility', 'contactNumber', 'emailId', 'createdOn', 'createdBy'];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);
  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination

  RA: any = {};

  constructor(public dialog: MatDialog, private auth: AuthService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private fb: FormBuilder,
    private excel: ExcelService,
    private service: ClientDocumentService
  ) { }

  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllClientList();
  }

  openClientDetails(type): void {
    if (this.selection.selected.length == 0) {
      this.toastr.error(
        "Please choose a client to view",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );
      this.cs.notifyOther(true);
      return;
    }
    const dialogRef = this.dialog.open(ClientportalDisplayComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.7%', },
      data: { type: type, clientDetails: this.selection.selected[0] }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.getAllClientList();
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
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
    console.log('show:' + this.showFloatingButtons);
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.documenttype + 1}`;
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

  getAllClientList() {
    this.spin.show();

    this.sub.add(this.service.getAllClientList().subscribe((res: any[]) => {
      console.log(res)
      if (this.auth.classId != '3')
        this.ELEMENT_DATA = res.filter(x => x.classId == +this.auth.classId);
      else
        this.ELEMENT_DATA = res;

      this.ELEMENT_DATA.forEach((x: { clientId: string; }) => this.multiclientnoList.push({ value: x.clientId, label: x.clientId }))
      this.multiselectclientnoList = this.multiclientnoList;
      this.multiselectclientnoList = this.cas.removeDuplicatesFromArrayNew(this.multiselectclientnoList);


      this.ELEMENT_DATA.forEach((x: { clientUserId: string; }) => this.multiclientportalList.push({ value: x.clientUserId, label: x.clientUserId }))
      this.multiselectclientportalList = this.multiclientportalList;
      this.multiselectclientportalList = this.cas.removeDuplicatesFromArrayNew(this.multiselectclientportalList);

      this.ELEMENT_DATA.forEach((x: { lastName: string; firstName: string; }) => this.multifullNameList.push({ value: x.firstName, label: x.firstName + x.lastName }))
      this.multiselectfullNameList = this.multifullNameList;
      this.multiselectfullNameList = this.cas.removeDuplicatesFromArrayNew(this.multiselectfullNameList);


      // this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
      this.dataSource.data = this.ELEMENT_DATA;
      this.selection = new SelectionModel<any>(true, []);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));

    this.spin.hide();
  }

  sendMail(data: any) {
    console.log(data);
    const dialogRef = this.dialog.open(SendIntakeComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      data: data

    });
    console.log(data)
    dialogRef.afterClosed().subscribe(result => {
    });
  }


  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Client ID": x.clientId,
        'Client Portal ID': x.clientUserId,
        "Client Name": x.firstName + x.firstName,
        'Phone No': x.contactNumber,
        "Email ID ": x.emailId,
        "Created Date  ": this.cs.dateapi(x.createdOn),
        "Created By  ": x.createdBy,
        //   'Creation Date ': this.cs.dateapi(x.createdOn),     //error throwing
      });

    })
    this.excel.exportAsExcel(res, "Client Portal");
  }


  searhform = this.fb.group({
    clientId: [],
    classId: [],
    clientUserIdFE: [],
    clientUserId: [],
    fullNameFE: [],
    fullName: [],
    startCreatedOn: [],
    endCreatedOn: [],

  });

  submitted = false;



  selectedItems2: any[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  selectedItems3: any[] = [];
  multiselectfullNameList: any[] = [];
  multifullNameList: any[] = [];


  selectedItems4: any[] = [];
  multiselectclientportalList: any[] = [];
  multiclientportalList: any[] = [];


  selectedItems5: any[] = [];
  multiselectclientnoList: any[] = [];
  multiclientnoList: any[] = [];


  filtersearch() {
    this.submitted = true;
    if (this.searhform.invalid) {
      this.toastr.error(
        "Please fill the required fields to continue",
        "Notification", {
        timeOut: 2000,
        progressBar: false,
      }
      );

      this.cs.notifyOther(true);
      return;
    }

    // if (this.selectedItems2 && this.selectedItems2.length > 0) {
    //   let multistatusList: any[] = []
    //   this.selectedItems2.forEach((a: any) => multistatusList.push(a.id))
    //   this.searhform.patchValue({ statusId: multistatusList });
    // }

    // if (this.selectedItems5 && this.selectedItems5.length > 0) {
    //   let multiclientnoList: any[] = []
    //   this.selectedItems5.forEach((a: any) => multiclientnoList.push(a.id))
    //   this.searhform.patchValue({ clientId: this.selectedItems5 });
    // }


    // if (this.selectedItems3 && this.selectedItems3.length > 0) {
    //   let multifullNameList: any[] = []
    //   this.selectedItems3.forEach((a: any) => multifullNameList.push(a.id))
    //   this.searhform.patchValue({ fullName: this.selectedItems3 });
    // }

    // if (this.selectedItems4 && this.selectedItems4.length > 0) {
    //   let multiclientportalList: any[] = []
    //   this.selectedItems4.forEach((a: any) => multiclientportalList.push(a.id))
    //   this.searhform.patchValue({ clientUserId: this.selectedItems4 });
    // }


    this.searhform.controls.endCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endCreatedOn.value));
    this.searhform.controls.startCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.startCreatedOn.value));


    this.spin.show();


    this.spin.show();
    this.sub.add(this.service.searchClientList(this.searhform.getRawValue()).subscribe((res: any[]) => {
      console.log(this.searhform.getRawValue())
      //  this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno);
      this.ELEMENT_DATA =  res.filter(x => x.deletionIndicator == 0);
      this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
      this.selection = new SelectionModel<any>(true, []);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
}