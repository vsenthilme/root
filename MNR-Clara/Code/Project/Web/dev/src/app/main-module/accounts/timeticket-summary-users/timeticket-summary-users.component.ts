import { SelectionModel } from '@angular/cdk/collections';
import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router, ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/dialog_modules/delete/delete.component';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { TimeNewComponent } from '../../matters/case-management/time-tickets/time-new/time-new.component';
import { TimeTicketsElement, TimeTicketsService } from '../../matters/case-management/time-tickets/time-tickets.service';
import { dropdownelement } from '../../setting/business/document-checklist/document-checklist.component';

// import the pdfmake library
import pdfMake from "pdfmake/build/pdfmake";
// importing the fonts and icons needed
import pdfFonts from "./../../../../assets/font/vfs_fonts.js";
import { defaultStyle } from "./../../../config/customStyles";
import { fonts } from "./../../../config/pdfFonts";


@Component({
  selector: 'app-timeticket-summary-users',
  templateUrl: './timeticket-summary-users.component.html',
  styleUrls: ['./timeticket-summary-users.component.scss']
})
export class TimeticketSummaryUsersComponent implements OnInit {

  screenid = 1175;
  sub = new Subscription();
  public icon = 'expand_more';
  isShowDiv = false;
  table = false;
  fullscreen = false;
  search = true;
  back = false;
  showFloatingButtons: any;
  toggle = true;
  statuslist1: any;
  clientlist: any[] = [];

  searhform = this.fb.group({
    billType: [],
    billTypeFE: [],
    endCreatedOn: [],
    timeKeeperCode: [,],
    timeKeeperCodeFE: [],
    matterNumber: [],
    startTimeTicketDate: [],
    endTimeTicketDate: []
    , matterNumberFE: [],
    notesNumber: [],
    startCreatedOn: [],
    statusId: [33,],
    statusIdFE: [],
  });
  nonbilledhrs: any;
  billedhrs: any;
  totalhrs: any;
  Clear() {
    this.reset();
  };

  timekeeperCodelist: any[] = [];
  statuslist: any[] = [];
  submitted = false;

  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  multiselectbillList: any[] = [];
  multibillList: any[] = [];

  multiselecttimekeeperList: any[] = [];
  multitimekeeperList: any[] = [];

  multiSelectMatterNoList: any[] = [];

  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  showFiller = false;
  animal: string | undefined;
  id: string | undefined;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true })
  sort: MatSort;

  ELEMENT_DATA: TimeTicketsElement[] = [];
  displayedColumns: string[] = ['select', 'classId','clientId', 'matterNumber','timeTicketNumber', 'timeTicketDate', 'createdOn', 'billType', 'timeTicketHours','approvedBillableTimeInHours', 'timeTicketAmount', 'approvedBillableAmount' , 'activityCode', 'timeTicketDescription',  'taskCode', 'defaultRate',  'timeKeeperCode', 'statusIddes'];
  dataSource = new MatTableDataSource<TimeTicketsElement>(this.ELEMENT_DATA);
  selection = new SelectionModel<TimeTicketsElement>(true, []);

  RA: any = {};
  billTypeList: dropdownelement[] = [{ key: "Billable", value: "Billable" }, { key: "Non-Billable", value: "Non-Billable" }];

  userId = this.auth.userID;
  matterClientList: any[] = [];

  constructor(public dialog: MatDialog,
    private service: TimeTicketsService, private router: Router,
    public toastr: ToastrService, private route: ActivatedRoute,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    private auth: AuthService,
    private datePipe: DatePipe,
    private decimalPipe: DecimalPipe
  ) { }

  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
 
    this.getall();
    this.searhform.controls.statusId.patchValue([33])
    this.getAllMatter();
    this.billTypeList.forEach((x: { key: string; value: string; }) => this.multibillList.push({ value: x.key, label: x.value }))
    this.multiselectbillList = this.multibillList;
  }

  getall(excel: boolean = false) {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.timeKeeperCode.url,
      this.cas.dropdownlist.setup.statusId.url
    ]).subscribe((results: any) => {
      this.timekeeperCodelist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.timekeeperCodelist.forEach((x: { key: string; value: string; }) => this.multitimekeeperList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselecttimekeeperList = this.multitimekeeperList;

      this.searhform.controls.timeKeeperCode.patchValue([this.auth.userID])
      this.statuslist1 = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [33, 34, 35, 46, 56].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({ value: x.key, label: x.value }))
      this.multiselectstatusList = this.multistatusList;
    }, (err) => {
      this.spin.hide();
      this.toastr.error(err, "");
    });
  }

  getAllMatter() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.matter.matterNumberList.url,
    ]).subscribe((results: any) => {
      this.spin.hide();
      this.matterClientList = results[0].matterDropDown;
      results[0].matterDropDown.forEach((x: any) => {
        this.clientlist.push({ value: x.clientId, label: x.clientId + '-' + x.clientName });
        this.multiSelectMatterNoList.push({ value: x.matterNumber, label: x.matterNumber + '-' + x.matterDescription });
      })
    }, (err) => {
      this.spin.hide();
      this.toastr.error(err, "");
    });
  }

  finalCreatedOn = false;
  filterStartDate = '';
  filterEndDate = '';
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
    this.searhform.controls.endTimeTicketDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endTimeTicketDate.value));
    this.searhform.controls.startTimeTicketDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.startTimeTicketDate.value));
    this.spin.show();
    this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: TimeTicketsElement[]) => {
      res.forEach((x) => {
        x.statusIddes = this.statuslist1.find(y => y.key == x.statusId)?.value;
        x.timeKeeperCode = this.timekeeperCodelist.find(y => y.key == x.timeKeeperCode)?.value;
        x['description'] = this.multiSelectMatterNoList.find(y => y.value == x.matterNumber)?.label;
        x['clientName'] = this.clientlist.find(y => y.value == x.clientId)?.label;
      })
      // this.ELEMENT_DATA = res
      this.ELEMENT_DATA = res.filter(x => x.deletionIndicator == 0);

      this.ELEMENT_DATA.sort((a, b) => (a.createdOn > b.createdOn) ? -1 : 1)
if(this.searhform.controls.startTimeTicketDate.value){
  this.finalCreatedOn = true
  this.filterStartDate = this.searhform.controls.startTimeTicketDate.value;
  this.filterEndDate = this.searhform.controls.endTimeTicketDate.value
}
      this.dataSource = new MatTableDataSource<TimeTicketsElement>(this.ELEMENT_DATA);
      this.selection = new SelectionModel<TimeTicketsElement>(true, []);
      this.dataSource.sort = this.sort;
      console.log(this.paginator)
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
      this.table = true;
      this.search = false;
      //this.fullscreen = true;
      this.back = true;
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  togglesearch() {
    this.search = false;
    this.table = true;
    this.fullscreen = false;
    this.back = true;
  }
  backsearch() {
    this.table = false;
    this.search = true;
    this.fullscreen = true;
    this.back = false;
  }
  public errorHandling = (control: string, error: string = "required") => {
    if (control.includes('.')) {
      const controls = this.searhform.get(control);
      return controls ? controls.hasError(error) : false;
    }
    return this.searhform.controls[control].hasError(error);
  }

  getErrorMessage(type: string) {
    if (!this.searhform.valid && this.submitted) {
      if (this.searhform.controls[type].hasError('required')) {
        return 'Field should not be blank';
      } else {
        return '';
      }
    } else {
      return '';
    }
  }
  
  reset() {
    this.finalCreatedOn =  false
    this.searhform.reset();
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
      this.spin.hide(); //this.getAllListData();
      //  window.location.reload();
      this.filtersearch();
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
      else if (this.selection.selected[0].statusId != 33 && data == "Edit") {
        this.toastr.error("Time Ticket can't be edited.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    }
    const dialogRef = this.dialog.open(TimeNewComponent, {
      width: '70%',
      maxWidth: '80%',
      // position: { top: '6.5%' },
      data: { pageflow: data, matter: this.selection.selected[0].matterNumber, matterdesc: this.selection.selected[0].description, code: data != 'New' ? this.selection.selected[0].timeTicketNumber : null }
    });
    dialogRef.afterClosed().subscribe(result => {
      //this.getAllListData();
      //   window.location.reload();
      this.filtersearch();
    });
  }

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      // let date = x.stimeTicketDate
      // if(x.stimeTicketDate != null && x.stimeTicketDate){
      //  let dateStringArray = x.stimeTicketDate.split('-')
      //  date = dateStringArray[1] + "-" + dateStringArray[2].split('T')[0] + "-" + dateStringArray[0]
      //  console.log(date)
      // }
      res.push({
        'Class': (x.classId == 1 ? 'L&E' : x.classId == 2 ? 'Immigration' : ''),
        "Client": x.clientId,
        'Matter': x.description,
        'Time Ticket Number': x.timeTicketNumber,
        "Date ": this.cs.excel_date(x.stimeTicketDate),
        "Created On" : this.cs.excel_date(x.screatedOn),
        "Time Ticket Type  ": x.billType,
        "Booked Hours  ": x.timeTicketHours,
        "Approved Hours  ": x.approvedBillableTimeInHours,
        'Booked Amount ': x.timeTicketAmount,
        'Approved Amount ': x.approvedBillableAmount,
        'Description': x.timeTicketDescription,
        "Task Code  ": x.taskCode,
        "Activity Code  ": x.activityCode,
        "Rate per Hour  ": x.defaultRate,
        "Time Keeper": x.timeKeeperCode,
        "Status": x.statusIddes,
        //   'Creation Date ': this.cs.dateapi(x.createdOn),     //error throwing
      });

    })
    res.push({
      "Client": '',
      'Matter': '',
      "Date ": '',
      "Created On ": '',
      "Time Ticket Type  ": 'Total',
      "Booked Hours  ": this.getTotalHours(),
      "Approved Hours  ": this.getApprovedHours(),
      'Booked Amount ': this.getBillableAmount(),
      'Approved Amount ': this.getApprovedAmount(),
      'Description': 'Total Billed',
      "Task Code  ": '',
      "Activity Code  ": 'Total Non-Billed',
      "Rate per Hour  ": '',
      "Time Keeper": '',
      "Status": '',
      //   'Creation Date ': this.cs.dateapi(x.createdOn),     //error throwing
    });
    this.excel.exportAsExcel(res, "Time Ticket Diary");
  }

  getBillableAmount() { 
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.timeTicketAmount != null ? element.timeTicketAmount : 0);
    })
    return total;
  }
  getTotalHours() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.timeTicketHours != null ? element.timeTicketHours : 0);
      this.totalhrs = total
    })
    return total;
  }

  getApprovedAmount() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.approvedBillableAmount != null ? element.approvedBillableAmount : 0);
    })
    return total;
  }
  getApprovedHours() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.approvedBillableTimeInHours != null ? element.approvedBillableTimeInHours : 0);
    })
    return total;
  }

  getbilledTotalHours() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      element.billType = element.billType.toLowerCase()
      if(element.billType == 'billable'){
        total = total + (element.timeTicketHours != null ? element.timeTicketHours : 0);
        this.billedhrs = this.decimalPipe.transform(total, "1.1-1")
      }
    })
    return total;
  }

  getnonbilledTotalHours() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      if(element.billType == 'non-billable'){
        total = total + (element.timeTicketHours != null ? element.timeTicketHours : 0);
        this.nonbilledhrs = this.decimalPipe.transform(total, "1.1-1")
      
      }
    })
    return total;

    
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
    this.selection.clear();
    this.selection.toggle(row);
  }

  generatePdf() {
    let currentDate = this.datePipe.transform(new Date, 'MM-dd-yyyy');
    let currentTime = this.datePipe.transform(new Date, 'hh:mm');
    let userId = this.userId;
    console.log(userId)
    if (this.dataSource.data.length > 0) {
      // let obj: any = this.matterClientList.filter(x => x.matterNumber == this.searhform.controls.matterNumber.value);
      // let matter = obj[0].matterNumber + " - " + obj[0].matterDescription;
      // let client = obj[0].clientId + ' - ' + obj[0].clientName;
      var dd: any;
      dd = {
        pageSize: "A4",
        pageOrientation: "portrait",
        content: [],
        pageMargins: [40, 110, 40, 40],
        header(currentPage: number, pageCount: number, pageSize: any): any {
          return [
            {
              table: {
                // layout: 'noBorders', // optional
                // heights: [,60,], // height for each row
                headerRows: 1,
                widths: ['*', 100],
                body: [[
                  { text: 'Time Ticket Diary Report', bold: true, italics: true, fontSize: 15, border: [false, false, false, false] },
                  { text: 'Report Date: ' + currentDate, bold: true, fontSize: 9, border: [false, false, false, false] },
                ], [
                  { text: '', bold: true, fontSize: 9, border: [false, false, false, false] },
                  { text: 'Report Time: ' + currentTime, bold: true, fontSize: 9, border: [false, false, false, false] },
                ], [
                  { text: '', bold: true, fontSize: 9, border: [false, false, false, false] },
                  { text: 'Page: ' + currentPage + ' of ' + pageCount, bold: true, fontSize: 9, border: [false, false, false, false] },
                ], [
                  { text: 'Monty & Ramirez LLP', bold: true, fontSize: 9, border: [false, false, false, false] },
                  { text: 'User ID: ' + userId, bold: true, fontSize: 9, border: [false, false, false, false] },
                ]]
              },
              margin: [40, 20]
            }
          ]
        },
        defaultStyle
      };

      // border: [left, top, right, bottom]
      // border: [false, false, false, false]
      let bodyArray: any[] = [];
      bodyArray.push([
        { text: 'Client Number\nClient Name', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Matter Number\nDescription', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Date', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Ticket\nNumber', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Hours', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Amount', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Type', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Task\nCode', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Activity\nCode', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Billing\nStatus', bold: true, fontSize: 9, border: [false, true, false, true] }
      ]);
      let billableHours = 0;
      let billableAmount = 0;
      let nonBillableHours = 0;
      let nonBillableAmount = 0;
      this.dataSource.data.forEach((data, i) => {
        if (data.billType == 'billable') {
          billableHours = billableHours + Number(data.timeTicketHours);
          billableAmount = billableAmount + Number(data.timeTicketAmount);
        } else if (data.billType == 'non-billable') {
          nonBillableHours = nonBillableHours + Number(data.timeTicketHours);
          nonBillableAmount = nonBillableAmount + Number(data.timeTicketAmount);
        }

        bodyArray.push([
          { text: data.clientId + '\n' + this.matterClientList.find(y => y.clientId == data.clientId)?.clientName + '\n' + "Time Ticket Text:", fontSize: 7, border: [false, false, false, false], lineHeight: 1.3 },
          { text: (data.matterNumber != null ? data.matterNumber : '') + '\n' + this.matterClientList.find(y => y.matterNumber == data.matterNumber)?.matterDescription + '\n' + data.timeTicketDescription, fontSize: 7, border: [false, false, false, false], lineHeight: 1.3 },
          { text: data.timeTicketDate != null ? this.datePipe.transform(data.timeTicketDate, 'MM-dd-yyyy') : '', fontSize: 7, border: [false, false, false, false], lineHeight: 1.3 },
          { text: data.timeTicketNumber != null ? data.timeTicketNumber : '', fontSize: 7, border: [false, false, false, false], lineHeight: 2 },
          { text: data.timeTicketHours != null ? this.decimalPipe.transform(data.timeTicketHours, "1.1-1") : '', alignment: 'right', fontSize: 7, border: [false, false, false, false], lineHeight: 2 },
          { text: data.timeTicketAmount != null ? '$ ' + this.decimalPipe.transform(data.timeTicketAmount, "1.2-2") : '', fontSize: 7,  alignment: 'right', border: [false, false, false, false], lineHeight: 2 },
          { text: data.billType != null ? data.billType == 'billable' ? 'BL' : data.billType == 'non-billable' ? 'NB' : data.billType == 'nocharge' ? 'NC' : '' : '', fontSize: 7, border: [false, false, false, false], lineHeight: 2 },
          { text: data.taskCode != null ? data.taskCode : '', fontSize: 7, border: [false, false, false, false], lineHeight: 2 },
          { text: data.activityCode != null ? data.activityCode : '', fontSize: 7, border: [false, false, false, false], lineHeight: 2 },
          { text: data.statusId != null ? this.statuslist1.find(y => y.key == data.statusId)?.value : '', fontSize: 7, border: [false, false, false, false], lineHeight: 2 }
        ])
      });
      //total row
      bodyArray.push([
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
        { text: '', fontSize: 9, border: [false, false, false, false] },
        { text: 'Totals For:' + this.searhform.controls.timeKeeperCode.value != null ? this.searhform.controls.timeKeeperCode.value : 'All', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
        { text: this.decimalPipe.transform(billableHours + nonBillableHours, "1.1-1"), alignment: 'right', fontSize: 9, border: [false, true, false, true], lineHeight: 2 },
        { text: this.decimalPipe.transform(billableAmount + nonBillableAmount, "1.2-2"), alignment: 'right', fontSize: 9, border: [false, true, false, true], lineHeight: 2 },
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 }
      ])
      dd.content.push(
        '\n',
        {
          layout: 'noBorders', // optional
          table: {
            headerRows: 0,
            widths: [70, 110, 100, 140, 60],
            body: [
              [{ text: 'Date Range  :', fontSize: 8, alignment: 'left', bold: true }, { text: this.searhform.controls.startTimeTicketDate.value != null ? this.datePipe.transform(this.searhform.controls.startTimeTicketDate.value, 'MM-dd-yyyy') + " - " + this.datePipe.transform(this.searhform.controls.endTimeTicketDate.value, 'MM-dd-yyyy') : 'All', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8 }],
              [{ text: 'Timekeeper  :', fontSize: 8, alignment: 'left', bold: true }, { text: this.searhform.controls.timeKeeperCode.value != null ? this.searhform.controls.timeKeeperCode.value : 'All', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8, bold: true }],
              [{ text: 'Client      :', fontSize: 8, alignment: 'left', bold: true }, { text: 'All', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8, bold: true }],
              [{ text: 'Matter      :', fontSize: 8, alignment: 'left', bold: true }, { text: 'All', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8, bold: true }],
              [{ text: 'Billing Type:', fontSize: 8, alignment: 'left', bold: true }, { text: 'All', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8, bold: true }],
              [{ text: 'Status:', fontSize: 8, alignment: 'left', bold: true }, { text: 'All', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8, bold: true }],
            ]
          },
        },
        '\n',
        {
          table: {
            // layout: 'noBorders', // optional
            // heights: [,60,], // height for each row
            headerRows: 1,
            widths: [80, 90, 40, 40, 25, 40, 20, 30, 40, 50],
            body: bodyArray
          }
        }
      );

      dd.content.push(
        '\n',
        {
          text: 'Timekeepers Totals',
          style: 'header',
          decoration: 'underline',
          alignment: 'center',
          bold: true
        },
        '\n',
        {
          columns: [
            { width: 100, text: '' },
            {
              width: 300,
              table: {
                widths: [100, 100, 100],
                headerRows: 0,
                body: [
                  [{ text: '', fontSize: 8, alignment: 'left', border: [true, true, false, false] }, { text: '', fontSize: 8, border: [false, true, false, false] }, { text: '', fontSize: 8, border: [false, true, true, false] }],
                  [{ text: 'Timekeeper:' + this.searhform.controls.timeKeeperCode.value != null ? this.searhform.controls.timeKeeperCode.value : 'All', fontSize: 8, alignment: 'left', bold: true, border: [true, false, false, false] }, { text: 'Type', fontSize: 8, border: [false, false, false, false] }, { text: this.searhform.controls.billType.value, fontSize: 8, border: [false, false, true, false] }],
                  [{ text: '', fontSize: 8, alignment: 'left', border: [true, false, false, false] }, { text: 'Billable Hours', fontSize: 8, border: [false, false, false, false] }, { text: this.decimalPipe.transform(billableHours, "1.1-1"), fontSize: 8,alignment: 'right', border: [false, false, true, false] }],
                  [{ text: '', fontSize: 8, alignment: 'left', border: [true, false, false, false] }, { text: 'Non-Billable Hours', fontSize: 8, border: [false, false, false, false] }, { text: this.decimalPipe.transform(nonBillableHours, "1.1-1"), fontSize: 8,alignment: 'right', border: [false, false, true, false] }],
                  [{ text: '', fontSize: 8, alignment: 'left', border: [true, false, false, false] }, { text: 'Total For' + this.searhform.controls.timeKeeperCode.value != null ? this.searhform.controls.timeKeeperCode.value : 'All', fontSize: 8, border: [false, false, false, false] }, { text: this.decimalPipe.transform(billableHours + nonBillableHours, "1.1-1"), alignment: 'right',fontSize: 8, bold: true, border: [false, false, true, false] }],
                  [{ text: '', fontSize: 8, alignment: 'left', border: [true, false, false, true] }, { text: '', fontSize: 8, border: [false, false, false, true] }, { text: '', fontSize: 8, bold: true, border: [false, false, true, true] }],
                ],
                alignment: "center"
              }
            },
            { width: 100, text: '' },
          ]
        },
        '\n'
      )

      dd.content.push(
        '\n',
        {
          text: 'Firm Totals',
          style: 'header',
          decoration: 'underline',
          alignment: 'center',
          bold: true
        },
        '\n',
        {
          columns: [
            { width: 100, text: '' },
            {
              width: 300,
              table: {
                widths: [100, 100, 100],
                headerRows: 0,
                body: [
                  [{ text: '', fontSize: 8, alignment: 'left', border: [true, true, false, false] }, { text: '', fontSize: 8, border: [false, true, false, false] }, { text: '', fontSize: 8, border: [false, true, true, false] }],
                  [{ text: '', fontSize: 8, alignment: 'left', bold: true, border: [true, false, false, false] }, { text: 'Type', fontSize: 8, border: [false, false, false, false] }, { text: this.searhform.controls.billType.value, fontSize: 8, border: [false, false, true, false] }],
                  [{ text: '', fontSize: 8, alignment: 'left', border: [true, false, false, false] }, { text: 'Billable Hours', fontSize: 8, border: [false, false, false, false] }, { text: this.decimalPipe.transform(billableHours, "1.1-1"), alignment: 'right', fontSize: 8, border: [false, false, true, false] }],
                  [{ text: '', fontSize: 8, alignment: 'left', border: [true, false, false, false] }, { text: 'Non-Billable Hours', fontSize: 8, border: [false, false, false, false] }, { text: this.decimalPipe.transform(nonBillableHours, "1.1-1"), alignment: 'right', fontSize: 8, border: [false, false, true, false] }],
                  [{ text: '', fontSize: 8, alignment: 'left', border: [true, false, false, false] }, { text: 'Grand Total', fontSize: 8, border: [false, false, false, false] }, { text:  this.decimalPipe.transform(billableHours + nonBillableHours, "1.1-1"),alignment: 'right', fontSize: 8, bold: true, border: [false, false, true, false] }],
                  [{ text: '', fontSize: 8, alignment: 'left', border: [true, false, false, true] }, { text: '', fontSize: 8, border: [false, false, false, true] }, { text: '', fontSize: 8, bold: true, border: [false, false, true, true] }],
                ],
                alignment: "center"
              }
            },
            { width: 100, text: '' },
          ]
        },
        '\n'
      )

      // pdfMake.createPdf(dd).download('Time Ticket Diary');
      pdfMake.createPdf(dd).open();
    } else {
      this.toastr.info("No data available", "Pdf Generate");
    }
  }
}
