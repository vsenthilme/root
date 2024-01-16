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
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { FeessharingNewComponent } from "./feessharing-new/feessharing-new.component";
import { MatterFeesSharingElement, MatterFeesSharingService } from "./matter-fees-sharing.service";

@Component({
  selector: 'app-feessharing-list',
  templateUrl: './feessharing-list.component.html',
  styleUrls: ['./feessharing-list.component.scss']
})
export class FeessharingListComponent implements OnInit {

  screenid = 1110;
  displayedColumns: string[] = ['select', 'timeKeeperCode', 'name', 'feeSharingPercentage', 'createdBy', 'createdOn', 'statusIddes',];

  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  ClientFilter: any;
  matterdesc: any;
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.timeKeeperCode + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  ELEMENT_DATA: MatterFeesSharingElement[] = [];
  // displayedColumns: string[] = ['select', 'taskno', 'type', 'creation', 'deadline', 'remainder', 'originatting', 'responsible', 'legal', 'status',];

  dataSource = new MatTableDataSource<MatterFeesSharingElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<MatterFeesSharingElement>(true, []);

  constructor(public dialog: MatDialog,
    private service: MatterFeesSharingService, private router: Router,
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
      data: this.selection.selected[0].timeKeeperCode,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].timeKeeperCode);

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
    const dialogRef = this.dialog.open(FeessharingNewComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: {
        pageflow: data, matter: this.cs.decrypt(this.route.snapshot.params.code).code, matterdesc: this.matterdesc,
        timeKeeperCode: data != 'New' ? this.selection.selected[0].timeKeeperCode : null
        , code: data != 'New' ? this.selection.selected[0].matterNumber : null
      }
    });

    dialogRef.afterClosed().subscribe(result => {

      // this.getAllListData();
      window.location.reload();
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
  timeKeeperCodelist: any[] = [];
  statuslist: any[] = [];

  getall(excel: boolean = false) {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.timeKeeperCode.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.timeKeeperCodelist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: any[]) => {
        res.forEach((x) => {
          x.name = this.timeKeeperCodelist.find(y => y.key == x.timeKeeperCode)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
        })
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno);

        if (excel)
          this.excel.exportAsExcel(res, "Fees Sharing");
        this.dataSource = new MatTableDataSource<MatterFeesSharingElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<MatterFeesSharingElement>(true, []);
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
        "Time Keeper Code": x.timeKeeperCode,
        'Name': x.name,
        "Fees Sharing %": x.feeSharingPercentage,
        "Status  ": x.statusIddes,
        'Created By': x.createdBy,
        'Created Date': this.cs.dateapi(x.createdOn),
      });

    })
    this.excel.exportAsExcel(res, "Fees Sharing");
  }
  getAllListData() {
    this.getall();
  }

  searchStatusList = {
    statusId: [38, 39]
  };
  searhform = this.fb.group({
    endCreatedOn: [],
    timeKeeperCode: [],
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
    this.searhform.controls.endCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endCreatedOn.value));
    this.searhform.controls.startCreatedOn.patchValue(this.cs.day_callapiSearch(this.searhform.controls.startCreatedOn.value));


    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.timeKeeperCode.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.timeKeeperCodelist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {

        res.forEach((x) => {
          x.name = this.timeKeeperCodelist.find(y => y.key == x.timeKeeperCode)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
        })
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno);
        this.dataSource = new MatTableDataSource<MatterFeesSharingElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<MatterFeesSharingElement>(true, []);
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