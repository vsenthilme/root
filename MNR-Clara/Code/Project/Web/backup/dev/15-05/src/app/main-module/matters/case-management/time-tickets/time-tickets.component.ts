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
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { MatterRateService } from "../rate-list/matter-rate.service";
import { TimeNewComponent } from "./time-new/time-new.component";
import { TimeTicketsElement, TimeTicketsService } from "./time-tickets.service";

interface SelectItem {
  id: string;
  itemName: string;
}

@Component({
  selector: 'app-time-tickets',
  templateUrl: './time-tickets.component.html',
  styleUrls: ['./time-tickets.component.scss']
})
export class TimeTicketsComponent implements OnInit {
  screenid = 1116;
  public icon = 'expand_more';

  displayedColumns: string[] = ['select', 'timeTicketNumber', 'timeTicketDescription','timeKeeperCode', 'createdOn', 'billType', 'timeTicketHours', 'defaultRate1', 'assignedRate','approvedBillableAmount', 'statusIddes','referenceField1'];


  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  matterdesc: any;
  statuslist1: dropdownelement[];
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
  checkboxLabel(row?: TimeTicketsElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.timeTicketNumber + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  ELEMENT_DATA: TimeTicketsElement[] = [];
  // displayedColumns: string[] = ['select', 'taskno', 'type', 'creation', 'deadline', 'remainder', 'originatting', 'responsible', 'legal', 'status',];

  dataSource = new MatTableDataSource<TimeTicketsElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<TimeTicketsElement>(true, []);

  constructor(public dialog: MatDialog,
    private service: TimeTicketsService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private matterRateService: MatterRateService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService) { }
  matterno: any;
  ClientFilter: any;
  RA: any = {};
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    this.billTypeList = [{ key: "Billable", value: "Billable" }, { key: "Non-Billable", value: "Non-Billable" }];

    sessionStorage.setItem('matter', this.route.snapshot.params.code);
    this.matterdesc = this.cs.decrypt(sessionStorage.getItem('matter')).code1;
    this.matterno = this.cs.decrypt(sessionStorage.getItem('matter')).code;
    this.ClientFilter = { matterNumber: this.matterno };
    this.getAllListData();

    this.billTypeList.forEach((x: { key: string; value: string; }) => this.multibilltypeList.push({value: x.key, label:  x.value}))

  }
  deleteDialog() {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }

    if (this.selection.selected[0].statusId != 33) {
      this.toastr.error("Time Ticket can't be deleted.", "Notification", {
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
      data: this.selection.selected[0].timeTicketNumber,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].timeTicketNumber);

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
      this.getAllListData();
      this.spin.hide();
      // window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  openDialog(data: any = 'new'): void {
    if (data != 'New') {
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    //  else if (this.selection.selected[0].statusId != 33 && data == "Edit") {
    //    this.toastr.error("Time Ticket can't be edited.", "Notification", {
    //      timeOut: 2000,
    //      progressBar: false,
    //    });
    //    return;
    //  }
      if (this.selection.selected[0].timeKeeperCode != this.auth.userId && this.auth.userTypeId != 7) {
        this.toastr.error("You are not authorized to edit others time tickets", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.cs.notifyOther(true);
        return;
      }

    }
    const dialogRef = this.dialog.open(TimeNewComponent, {
      disableClose: true,
      width: '70%',
      maxWidth: '80%',
      //position: { top: '6.5%' },
      data: { pageflow: data, matter: this.cs.decrypt(this.route.snapshot.params.code).code, matterdesc: this.matterdesc, code: data != 'New' ? this.selection.selected[0].timeTicketNumber : null }
    });

    dialogRef.afterClosed().subscribe(result => {

     this.getAllListData();
      // window.location.reload();
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
  timekeeperCodelist: any[] = [];
  statuslist: any[] = [];

  selectedItems2: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];
  multitimekeeperList: any[] = [];
  multibilltypeList: any[] = [];


    dropdownSettings = {
    singleSelection: false,
    text:"Select",
    selectAllText:'Select All',
    unSelectAllText:'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  matterRate: any
  getall(excel: boolean = false) {

    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.timeKeeperCode.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.timekeeperCodelist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.timekeeperCodelist.forEach((x: { key: string; value: string; }) => this.multitimekeeperList.push({value: x.key, label:  x.value}))

      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
      this.statuslist1 = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [33, 34, 35].includes(s.key));
      this.statuslist1.forEach((x: { key: string; value: string; }) => this.multistatusList.push({value: x.key, label:  x.value}))
    this.multiselectstatusList = this.multistatusList;
      this.spin.hide();

      this.spin.show();
    //  this.sub.add(this.service.Getall().subscribe((res: TimeTicketsElement[]) => {
        this.sub.add(this.service.Search({ matterNumber: [this.matterno] }).subscribe((res: TimeTicketsElement[]) => {
        res.forEach((x) => {
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x['timeKeeperCodeName'] = this.timekeeperCodelist.find(y => y.key == x.timeKeeperCode)?.value;
          x['assignedRate'] = this.matterRate.find(y => y.timeKeeperCode == x.timeKeeperCode)?.assignedRatePerHour;
          x['defaultRate1'] = this.matterRate.find(y => y.timeKeeperCode == x.timeKeeperCode)?.defaultRatePerHour;
          if(x.referenceField1 != null && x.referenceField1.includes("null")){
            console.log(x.referenceField1)
            x.referenceField1 = '';
          }

        })
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno && x.deletionIndicator == 0);

        if (excel)
          this.excel.exportAsExcel(res, "Time Tickets");
        this.dataSource = new MatTableDataSource<TimeTicketsElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<TimeTicketsElement>(true, []);
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

    this.matterRateService.getMatterRateByMatterNumber(this.matterno).subscribe(res => {
      this.matterRate = res;
    })
    this.spin.hide();

  }
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Time Ticket No": x.timeTicketNumber,
        "Description": x.timeTicketDescription,
        'Time Keeper': x.timeKeeperCodeName,
        'Time Ticket Date':  this.cs.dateapiutc0(x.timeTicketDate),
        "Time ticket Type  ": x.billType,
        'Time ticket hours ': x.timeTicketHours,
        "Billable Amount ": x.statusIddes,
        "Default Rate per Hour  ": x.defaultRate1,
        "Assigned Rate per Hour  ": x.assignedRate,
        "Billable Amount  ": x.approvedBillableAmount,
        "Billable Hours  ": x.approvedBillableTimeInHours,
        "Prebill No": x.referenceField1,
        "Status  ": x.statusIddes,
        'Creation Date ': this.cs.dateapiutc0(x.createdOn),
      });

    })

    res.push({
      "Time Ticket No": '',
      'Time Keeper':'',
      'Time Ticket Date':  '',
      "Time ticket Type  ": '',
      'Time ticket hours ': this.getTotalHours() ? this.getTotalHours() : '0',
      "Billable Amount ":  this.getBillableAmount() ? '$' + this.getBillableAmount() : '0',
      "Rate per Hour  ": '',
      "Billable Amount  ":'',
      "Status  ": '',
      'Creation Date ': '',
    });

    this.excel.exportAsExcel(res, "Time Tickets");
  }
  getAllListData() {
    this.getall();
  }


  billTypeList: dropdownelement[] = [];
  searchStatusList = {
    statusId: [33, 34, 35]
  };
  searhform = this.fb.group({
    billType: [],
    endCreatedOn: [],
    timeKeeperCode: [],
    timeTicketNumber: [],
    notesNumber: [],
    startCreatedOn: [],
    statusId: [],
    statusIdFE: [],
    sstartTimeTicketDate: [],
    sendTimeTicketDate: [],
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

    this.searhform.controls.sendTimeTicketDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endCreatedOn.value));
    this.searhform.controls.sstartTimeTicketDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.startCreatedOn.value));


    this.spin.show();

    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.timeKeeperCode.url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      this.timekeeperCodelist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
      this.spin.hide();

      this.spin.show();
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: TimeTicketsElement[]) => {
        res.forEach((x) => {
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x['timeKeeperCodeName'] = this.timekeeperCodelist.find(y => y.key == x.timeKeeperCode)?.value;
          x['assignedRate'] = this.matterRate.find(y => y.timeKeeperCode == x.timeKeeperCode)?.assignedRatePerHour;
          x['defaultRate1'] = this.matterRate.find(y => y.timeKeeperCode == x.timeKeeperCode)?.defaultRatePerHour;
          

          if(x.referenceField1 != null && x.referenceField1.includes("null")){
            console.log(x.referenceField1)
            x.referenceField1 = '';
          }
        })
        this.ELEMENT_DATA = res.filter(x => x.matterNumber == this.matterno && x.deletionIndicator == 0);


        this.dataSource = new MatTableDataSource<TimeTicketsElement>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<TimeTicketsElement>(true, []);
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

    this.spin.hide();

  }
  reset() {
    this.searhform.reset();
  }
  getTotalHours() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.timeTicketHours != null ? element.timeTicketHours : 0);
    })
    return (Math.round(total * 100) / 100);
  }
  getBillableAmount() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.approvedBillableAmount != null ? element.approvedBillableAmount : 0);
    })
    return (Math.round(total * 100) / 100);
  }
}