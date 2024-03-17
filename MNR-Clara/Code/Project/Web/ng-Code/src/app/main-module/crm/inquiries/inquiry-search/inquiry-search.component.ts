import { SelectionModel } from "@angular/cdk/collections";
import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { NotesComponent } from "src/app/common-field/notes/notes.component";
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { environment } from "src/environments/environment";
import { inquiryElement, InquiresService } from "../inquires.service";
import { InquiryUpdate3Component } from "../inquiry-update3/inquiry-update3.component";
import { IntakePopupComponent } from "../intake-popup/intake-popup.component";

interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-inquiry-search',
  templateUrl: './inquiry-search.component.html',
  styleUrls: ['./inquiry-search.component.scss']
})
export class InquirySearchComponent implements OnInit, OnDestroy, AfterViewInit {

  screenid = 1060;
  public icon = 'expand_more';
  assignedUserIdList: dropdownelement[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  field: boolean;
  prod: boolean;
  uat: boolean;
  scrollToTop(el: { scrollTop: number; }) {
    el.scrollTop = 0;
  }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement)?.value;

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
  checkboxLabel(row?: inquiryElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.inquiryNumber + 1}`;
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


  displayedColumns: string[] = ['select', 'action', 'inquiryNumber', 'sinquiryDate',  'inquiryModeId', 'firstName', 'lastName', 'assignedUserId', 'screatedOn', 'classIddes', 'email', 'referenceField4', 'contactNumber', 'hearAboutUs','notes', 'sms', 'referenceField2', 'referenceField3', 'statusIddesc',];

  dataSource = new MatTableDataSource<inquiryElement>();
  selection = new SelectionModel<inquiryElement>(true, []);
  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination


  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  // Pagination
  // Pagination

  Update = 'Update';
  isnew = false;
  // 
  pageflow: string | undefined;
  columnsToDisplay: string[] = [];
  ELEMENT_DATA: inquiryElement[] = [];
  sub = new Subscription();

  currentEnv: string;

  constructor(public dialog: MatDialog,
    private fb: FormBuilder,
    private service: InquiresService,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private router: ActivatedRoute,
    private auth: AuthService) {
    this.currentEnv = environment.name;
  }


  openNotes(data: any): void {



    const dialogRef = this.dialog.open(NotesComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      data: data.intakeFormId == 1 ? data.referenceField4 : data.referenceField9
    });

    dialogRef.afterClosed().subscribe(result => {



    });
  }
  open_new_update(data: any = null): void {
    const dialogRef = this.dialog.open(InquiryUpdate3Component, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: { code: data, pageflow: data != null ?  'Inquiry Validation' : 'Inquiry Assign'}
    });

    dialogRef.afterClosed().subscribe(result => {

      this.getallationslist();
      // window.location.reload();
    });
  }
  RA: any = {};
  ngOnInit(): void {
    if (this.currentEnv == 'prod') {
      this.prod = true;
    } else {
      this.prod = false;
    }

    this.router.data.subscribe(x => {
      this.pageflow = x.title;
    });
    // debugger
    this.field = false;
    if (this.pageflow == 'Inquiry Validation') {
      this.field = true;
      this.RA = this.auth.getRoleAccess(1064);
      this.columnsToDisplay = ['select', 'action', 'statusIddesc', 'inquiryNumber', 'sinquiryDate', 'inquiryModeId', 'lastName', 'email', 'contactNumber', 'assignedUserId', 'hearAboutUs', 'notes', 'sms',];
      this.Update = 'Assign';
      this.columnsToDisplay = ['select', 'action', 'statusIddesc', 'inquiryNumber', 'sinquiryDate','inquiryModeId', 'firstName', 'lastName', 'email', 'contactNumber', 'assignedUserId', 'hearAboutUs', 'notes', 'sms', 'referenceField4'];
      this.Update = 'Validate & Reassign';
      this.isnew = false;
    }
    this.getallationslist();
  }


  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  inquiryMode: any[] = [];
  statuslist: any[] = [];
  InquiryNoList: any[] = [];
  downloadexcel() {

    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Inquiry No ": x.inquiryNumber,
        'Inquiry Date ': this.cs.dateapi(x.sinquiryDate),
        "Inquiry Mode": x.inquiryModeId,
        'First Name': x.firstName,
        'Last Name ': x.lastName,
        'Assigned To ': x.assignedUserId,
        'Assigned On': this.cs.dateapi(x.screatedOn),
        'Class': x.classIddes,
        'Email': x.email,
        'Phone': x.contactNumber,
        'Hear About Us ': x.howDidYouHearAboutUs == 'other' ? x.others : x.howDidYouHearAboutUs,
        'Inquiry Status': x.statusIddesc,
        'Communications': x.referenceField2,
        'Service Type': x.referenceField3,
      });

    })
    this.excel.exportAsExcel(res, "Inquiry");
  }


  selectedItems: SelectItem[] = [];
  multiselectassignAttorneyList: any[] = [];
  multiassignAttorneyList: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  selectedItems3: SelectItem[] = [];
  multiselectclassList: any[] = [];
  multiclassList: any[] = [];

  selectedItems4: SelectItem[] = [];
  multiselectInquirymodeList: any[] = [];
  multiInquirymodeList: any[] = [];

  selectedItems5: SelectItem[] = [];
  multiselectInquirynoList: any[] = [];
  multiInquirynoList: any[] = [];

  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  getallationslist() {


    let classIdList: any[] = []
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.setup.inquiryModeId.url,
    this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.crm.inquiryNumber.url]).subscribe((results) => {
      if (this.pageflow == 'Inquiry Validation') {
        this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key).filter(s => [1, 2, 3, 4, 5, 6, 7, 25, 63, 64, 65].includes(s.key));
        this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({ value: x.key, label: x.value }))
        this.multiselectstatusList = this.multistatusList;
        console.log(this.multiselectstatusList)
      }

      this.inquiryMode = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.inquiryModeId.key);
      this.inquiryMode.forEach((x: { key: string; value: string; }) => this.multiInquirymodeList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselectInquirymodeList = this.multiInquirymodeList;


      classIdList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.setup.classId.key);
      classIdList.forEach((x: { key: string; value: string; }) => this.multiclassList.push({ value: x.key, label: x.value }))
      this.multiselectclassList = this.multiclassList;

      this.InquiryNoList = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.crm.inquiryNumber.key);
      this.InquiryNoList.forEach((x: { key: string; value: string; }) => this.multiInquirynoList.push({ value: x.key, label: x.value }))
      this.multiselectInquirynoList = this.multiInquirynoList;

      let initalStatus: number[] = [];
    initalStatus = [1, 2, 3, 4, 5, 7, 25, 63, 64, 65];


      this.sub.add(this.service.Search({statusId: initalStatus}).subscribe((res: inquiryElement[]) => {
        let statuslistf: number[] = [];

          statuslistf = [1, 2, 3, 4, 5, 6, 7, 25, 63, 64, 65];


        this.ELEMENT_DATA = res.filter(x => statuslistf.includes(x.statusId) && x.deletionIndicator == 0)



        this.ELEMENT_DATA.forEach((x) => {
          x.statusIddesc = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.inquiryModeId = this.inquiryMode.find(y => y.key == x.inquiryModeId)?.value;
          x.classIddes = classIdList.find(y => y.key == x.classId)?.value;
        })

        this.assignedUserIdList = [];

        const categories = this.ELEMENT_DATA.map(person => ({
          assignedUserId: person.assignedUserId,
        }));
        const distinctThings = categories.filter(
          (thing, i, arr) => arr.findIndex(t => t.assignedUserId === thing.assignedUserId) === i
        );
        distinctThings.forEach(x => {

          this.assignedUserIdList.push({ key: x.assignedUserId, value: x.assignedUserId });
        });

        this.assignedUserIdList.forEach((x: { key: string; value: string; }) => this.multiassignAttorneyList.push({ value: x.key, label: x.value }))
        this.multiassignAttorneyList.forEach((element: any) => {
          if (element.value != undefined) {
            this.multiselectassignAttorneyList.push(element);
          }
        })



        this.dataSource = new MatTableDataSource<inquiryElement>(this.ELEMENT_DATA.sort((a, b) => (a.inquiryNumber > b.inquiryNumber) ? -1 : 1));
        this.selection = new SelectionModel<inquiryElement>(true, []);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;

        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.spin.hide();
      this.toastr.error(err, "");
    });


  }

  openInTake(code: any): void {

    const dialogRef = this.dialog.open(IntakePopupComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
     // position: { top: '6.5%' },
      data: code
    });

    dialogRef.afterClosed().subscribe(result => {


    });
  }

  Delete(code: any): void {

    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '30%',
      maxWidth: '80%',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.spin.show();

        this.sub.add(this.service.Delete(code).subscribe((res) => {
          this.toastr.success(code + ' deleted successfully.', "Notification", {
            timeOut: 2000,
            progressBar: false,
          });

          this.spin.hide();
          this.getallationslist();
        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));
      }
    });
  }
  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }




  searchStatusList = {
    statusId: [1, 2, 3, 4, 5, 6, 7, 25
    ]
  };


  searhform = this.fb.group({


    classId: [],

    classIdFE: [],
    statusId: [],

    statusIdFE: [],
    inquiryNumber: [],
    inquiryNumberFE: [],
    inqEndDate: [],
    inqStartDate: [],
    inquiryModeId: [],
    inquiryModeIdFE: [],
    sinqStartDate: [],
    sinqEndDate: [],
    firstName: [],
    lastName: [],
    email: [],
    contactNumber: [],
    assignedUserIdFE: [],
    assignedUserId: [],
    eassignedOn: [],
    sassignedOn: [],
    inqStartDateFE: [],
    inqEndDateFE: [],

  });
  Clear() {
    this.reset();
  };
  search() {

    if (this.selectedItems && this.selectedItems.length > 0) {
      let multiassignAttorneyList: any[] = []
      this.selectedItems.forEach((a: any) => multiassignAttorneyList.push(a.id))
      this.searhform.patchValue({ assignedUserId: this.selectedItems });
    }
    if (this.selectedItems2 && this.selectedItems2.length > 0) {
      let multistatusList: any[] = []
      this.selectedItems2.forEach((a: any) => multistatusList.push(a.id))
      this.searhform.patchValue({ statusId: this.selectedItems2 });
    }

    if (this.selectedItems3 && this.selectedItems3.length > 0) {
      let multiclassList: any[] = []
      this.selectedItems3.forEach((a: any) => multiclassList.push(a.id))
      this.searhform.patchValue({ classId: this.selectedItems3 });
    }

    if (this.selectedItems4 && this.selectedItems4.length > 0) {
      let multiInquirymodeList: any[] = []
      this.selectedItems4.forEach((a: any) => multiInquirymodeList.push(a.id))
      this.searhform.patchValue({ inquiryModeId: this.selectedItems4 });
    }

    if (this.selectedItems5 && this.selectedItems5.length > 0) {
      let multiInquirynoList: any[] = []
      this.selectedItems5.forEach((a: any) => multiInquirynoList.push(a.id))
      this.searhform.patchValue({ inquiryNumber: this.selectedItems5 });
    }

    this.searhform.controls.sinqEndDate.patchValue(this.cs.dateNewFormat(this.searhform.controls.inqEndDateFE.value));
    this.searhform.controls.sinqStartDate.patchValue(this.cs.dateNewFormat(this.searhform.controls.inqStartDateFE.value));
    let classIdList: any[] = []
    this.spin.show();

    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.setup.inquiryModeId.url,
    this.cas.dropdownlist.setup.classId.url]).subscribe((results) => {
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.inquiryMode = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.inquiryModeId.key);
      classIdList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.setup.classId.key);
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: inquiryElement[]) => {
        let statuslistf: number[] = [];

        if (this.pageflow == 'Inquiry New')
          statuslistf = [1, 2];

        else if (this.pageflow == 'Inquiry Assign')
          statuslistf = [1, 2, 3, 4];

        else if (this.pageflow == 'Inquiry Validation')
          statuslistf = [3, 4, 5, 6, 7, 25];


        this.ELEMENT_DATA = res.filter(x => statuslistf.includes(x.statusId) && x.deletionIndicator == 0)


        this.ELEMENT_DATA.forEach((x) => {
          x.statusIddesc = this.statuslist.find(y => y.key == x.statusId)?.value;
          x.inquiryModeId = this.inquiryMode.find(y => y.key == x.inquiryModeId)?.value;
          x.classIddes = classIdList.find(y => y.key == x.classId)?.value;
        })



        this.dataSource = new MatTableDataSource<inquiryElement>(this.ELEMENT_DATA.sort((a, b) => (a.inquiryNumber > b.inquiryNumber) ? -1 : 1));
        this.selection = new SelectionModel<inquiryElement>(true, []);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;

        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.spin.hide();
      this.toastr.error(err, "");
    });
  }
  reset() {
    this.searhform.reset();
  }

}



