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
import { RateNewComponent } from "../rate-list/rate-new/rate-new.component";
import { IntakeSelectionComponent } from "./intake-selection/intake-selection.component";
import { MatterIntakeElement, MatterIntakeService } from "./matter-intake.service";

interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-matter-intake',
  templateUrl: './matter-intake.component.html',
  styleUrls: ['./matter-intake.component.scss']
})
export class MatterIntakeComponent implements OnInit {
  screenid = 1126;
  public icon = 'expand_more';

  displayedColumns: string[] = ['select', 'action', 'statusIddes', 'intakeFormNumber', 'intakeFormId', 'matterNumber', 'clientId', 'sentOn', 'receivedOn', 'approvedOn',];

  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  ClientFilter: any;
  matterdesc: any;
  statuslist1: any;
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.intakeFormNumber + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  ELEMENT_DATA: MatterIntakeElement[] = [];
  // displayedColumns: string[] = ['select', 'taskno', 'type', 'creation', 'deadline', 'remainder', 'originatting', 'responsible', 'legal', 'status',];

  dataSource = new MatTableDataSource<MatterIntakeElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<MatterIntakeElement>(true, []);

  constructor(public dialog: MatDialog,
    private service: MatterIntakeService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
  matterno: any = ""; RA: any = {};
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
    if (this.selection.selected[0].statusId == 37) {
      this.toastr.error("Selected Expense Code can't be deleted as this is already billed.", "Notification", {
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
      data: this.selection.selected[0].intakeFormNumber,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].intakeFormNumber);

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

      this.spin.hide(); window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  openDialog_Intake(pageflow: any = "validate", data: any = null) {

    if (pageflow != "validate") {

      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
      data = this.selection.selected[0];
    }
    let formname = this.cs.customerformname(data.intakeFormId);
    if (formname == '') {
      this.toastr.error(
        "Select from is invalid.",
        ""
      );

      return;
    }

    this.router.navigateByUrl('/main/matters/case-management/' + formname + '/' + this.cs.encrypt({ intakeFormNumber: data.intakeFormNumber, pageflow: pageflow }));


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
    const dialogRef = this.dialog.open(IntakeSelectionComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '3.7%', },
      data: { pageflow: data, matter: this.cs.decrypt(this.route.snapshot.params.code).code, matterdesc: this.matterdesc, code: data != 'New' ? this.selection.selected[0].intakeFormNumber : null }
    });

    dialogRef.afterClosed().subscribe(result => {
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
  statuslist: any[] = [];

  selectedItems2: SelectItem[] = [];
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

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.statuslist1 = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key).filter(s => [10, 38, 39].includes(s.key));
      this.statuslist1.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
    this.multiselectstatusList = this.multistatusList;
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: any[]) => {
        res.forEach((x) => {
          // x.intakeFormNumber  = this.intakeFormNumber list.find(y => y.key == x.noteTypeId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
        })
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno);

        if (excel)
          this.excel.exportAsExcel(res, "Intake");
        this.dataSource = new MatTableDataSource<MatterIntakeElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<MatterIntakeElement>(true, []);
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
        "Intake Form No": x.intakeFormNumber,
        'Intake Form': x.intakeFormId,
        "Matter": x.matterNumber,
        "Client": x.clientId,
        "Status  ": x.statusIddes,
        'Sent/Filled  Date': this.cs.dateapi(x.sentOn),
        'Received Date': this.cs.dateapi(x.receivedOn),
        'Approved Date': this.cs.dateapi(x.approvedOn),
      });

    })
    this.excel.exportAsExcel(res, "Intake");
  }
  getAllListData() {
    this.getall();
  }

  searchStatusList = {
    statusId: [38, 39]
  };
  searchIntakeList = {
    intakeFormId: [7, 10]
  };
  searhform = this.fb.group({
    eapprovedOn: [],
    // email: [],
    ereceivedOn: [],
    esentOn: [],
   // matterNumber: [[this.matterno]],
    intakeFormId: [],
    intakeFormNumber: [],
    sapprovedOn: [],
    sreceivedOn: [],
    ssentOn: [],
    statusId: [],
    statusIdFE: [],
  });
  Clear() {
    this.reset();
  };

  search() {

    
  // if (this.selectedItems2 && this.selectedItems2.length > 0){
  //   let multistatusList: any[]=[]
  //   this.selectedItems2.forEach((a: any)=> multistatusList.push(a.id))
  //   this.searhform.patchValue({statusId: multistatusList });
  // }

  
   // this.searhform.controls.matterNumber.patchValue(this.matterno);
    this.searhform.controls.eapprovedOn.patchValue(
      this.cs.day_callapiSearch(this.searhform.controls.eapprovedOn.value));
    this.searhform.controls.ereceivedOn.patchValue(
      this.cs.day_callapiSearch(this.searhform.controls.ereceivedOn.value));
    this.searhform.controls.esentOn.patchValue(
      this.cs.day_callapiSearch(this.searhform.controls.esentOn.value));
    this.searhform.controls.sapprovedOn.patchValue(
      this.cs.day_callapiSearch(this.searhform.controls.sapprovedOn.value));
    this.searhform.controls.sreceivedOn.patchValue(
      this.cs.day_callapiSearch(this.searhform.controls.sreceivedOn.value));
    this.searhform.controls.ssentOn.patchValue(
      this.cs.day_callapiSearch(this.searhform.controls.ssentOn.value));

    this.spin.show();
    this.cas.getalldropdownlist([
      // this.cas.dropdownlist.setup.intakeFormNumber .url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      // this.intakeFormNumber list = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.intakeFormNumber .key);
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {

        res.forEach((x) => {
          // x.noteTypeId = this.noteTypeIdlist.find(y => y.key == x.noteTypeId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
        })
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno);
        this.dataSource = new MatTableDataSource<MatterIntakeElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<MatterIntakeElement>(true, []);
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