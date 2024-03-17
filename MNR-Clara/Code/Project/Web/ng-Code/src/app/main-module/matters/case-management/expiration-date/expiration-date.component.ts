import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router, ActivatedRoute } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { dropdownelement, CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { IntakeSelectionComponent } from "../matter-intake/intake-selection/intake-selection.component";
import { ExpirationDateService } from "./expiration-date.service";
import { ExpirationNewComponent } from "./expiration-new/expiration-new.component";

@Component({
  selector: 'app-expiration-date',
  templateUrl: './expiration-date.component.html',
  styleUrls: ['./expiration-date.component.scss']
})
export class ExpirationDateComponent implements OnInit {

  screenid = 1130;

  displayedColumns: string[] = ['select', 'documentType', 'approvalDate', 'expirationDate', 'eligibilityDate', 'reminderDays', 'reminderDate', 'createdBy', 'createdOn', 'statusId',];

  expenseTypeList: dropdownelement[] = [{ key: 'Debit', value: 'Debit' }, { key: 'Credit', value: 'Credit' }]

  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  ClientFilter: any;
  matterdesc: any;
  code: any;
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.documentType + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  ELEMENT_DATA: any[] = [];
  // displayedColumns: string[] = ['select', 'taskno', 'type', 'creation', 'deadline', 'remainder', 'originatting', 'responsible', 'legal', 'status',];

  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);

  constructor(public dialog: MatDialog,
    private service: ExpirationDateService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
  matterno: any = "";
  RA: any = {};
  ngOnInit(): void {
    this.code = (this.cs.decrypt(this.route.snapshot.params.code));
    this.RA = this.auth.getRoleAccess(this.screenid);
    sessionStorage.setItem('matter', this.route.snapshot.params.code);
    this.matterdesc = this.cs.decrypt(sessionStorage.getItem('matter')).code1;
    this.matterno = this.cs.decrypt(sessionStorage.getItem('matter')).code;
    this.ClientFilter = { matterNumber: this.matterno };

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
      data: this.selection.selected[0],
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0]);

      }
    });
  }
  deleterecord(data: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(data.matterNumber, data.classId, data.clientId, data.documentType, data.languageId).subscribe((res) => {
      this.toastr.success(data.documentType + " deleted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      this.spin.hide(); //this.getAllListData();
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
    const dialogRef = this.dialog.open(ExpirationNewComponent, {
      disableClose: true,
      width: '80%',
      maxWidth: '80%',
      //position: { top: '6.5%' },
      data: { pageflow: data, matter: this.cs.decrypt(this.route.snapshot.params.code).code, matterdesc: this.matterdesc, code: data != 'New' ? this.selection.selected[0] : null }
    });

    dialogRef.afterClosed().subscribe(result => {

      // this.getAllListData();
      this.getAllListData();
    });
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
  documentTypelist: any[] = [];
  statuslist: any[] = [];

  getall(excel: boolean = false) {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.documentType.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.documentTypelist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.documentType.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: any[]) => {
        res.forEach((x) => {
          // x.documentType = this.documentTypelist.find(y => y.key == x.noteTypeId)?.value;
          x.statusId = this.statuslist.find(y => y.key == x.statusId)?.value;

        })
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno);

        if (excel)
          this.excel.exportAsExcel(res, "Exipiration");
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
        "Document Type": x.documentType,
        'Approval Date': this.cs.dateapi(x.approvalDate),
        "Expiration Date": this.cs.dateapi(x.expirationDate),
        "Eligibility Date  ": this.cs.dateapi(x.eligibilityDate),
        "Reminder Days ": x.reminderDays,
        'Reminder Date': this.cs.dateapi(x.reminderDate),
        'Created By': x.createdBy,
        'Created On': this.cs.dateapi(x.createdOn),
        'Status': x.statusId,
      });

    })
    this.excel.exportAsExcel(res, "Expiration Date");
  }
  getAllListData() {
    this.getall();
  }

  searchStatusList = {
    statusId: [38, 39]
  };
  searhform = this.fb.group({
    endCreatedOn: [],
    documentType: [],
    expenseType: [],
    matterNumber: [this.matterno],
    createdBy: [],
    startCreatedOn: [],
    statusId: [],
  });
  Clear() {
    this.reset();
  };

  search() {
    // this.searhform.controls.endCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endCreatedOn.value));
    // this.searhform.controls.startCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.startCreatedOn.value));


    // this.spin.show();
    // this.cas.getalldropdownlist([
    //   // this.cas.dropdownlist.setup.documentType.url,
    //   this.cas.dropdownlist.setup.statusId.url,
    // ]).subscribe((results) => {
    //   // this.documentTypelist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.documentType.key);
    //   this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
    //   this.spin.hide();

    //   this.spin.show();
    //   this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {

    //     res.forEach((x) => {
    //       // x.noteTypeId = this.noteTypeIdlist.find(y => y.key == x.noteTypeId)?.value;
    //       x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
    //     })
    //     this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno);
    //     this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
    //     this.selection = new SelectionModel<any>(true, []);
    //     this.dataSource.sort = this.sort;
    //     this.dataSource.paginator = this.paginator;
    //     this.spin.hide();
    //   }, err => {
    //     this.cs.commonerror(err);
    //     this.spin.hide();
    //   }));
    // }, (err) => {
    //   this.toastr.error(err, "");
    // });
    // this.spin.hide();

  }
  reset() {
    this.searhform.reset();
  }

}