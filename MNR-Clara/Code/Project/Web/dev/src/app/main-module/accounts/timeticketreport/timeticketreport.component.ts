import { SelectionModel } from '@angular/cdk/collections';
import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
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
import {  TimeTicketsService } from '../../matters/case-management/time-tickets/time-tickets.service';
import { dropdownelement } from '../../setting/business/document-checklist/document-checklist.component';
import { date } from '@rxweb/reactive-form-validators';

@Component({
  selector: 'app-timeticketreport',
  templateUrl: './timeticketreport.component.html',
  styleUrls: ['./timeticketreport.component.scss']
})
export class TimeticketreportComponent implements OnInit {

  screenid = 1196;
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
    classId: [,[ Validators.required]],
  timeKeeperCode: [],
  weekOfYear: [],
  year:[new Date().getFullYear(),],
  });
 
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

  ELEMENT_DATA: any[] = [];
  displayedColumns: string[] = ['select','timeKeeperCode','weekOfYear', 'fromDate','toDate','hoursCurrentWeek','hoursPreviousWeek','timeKeeperHours', 'complianceStatus',];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

  RA: any = {};


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
  restrictedAccess: boolean;
WeekList:any[]=[];
yearList:any[]=[];
  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);

    if (this.RA.createUpdate == false) {
      this.restrictedAccess = true;
    } else {
      this.restrictedAccess = false
    }
    
    this.getAllDropDown();
   // this.searhform.controls.statusId.patchValue([33])
   for (let i = 2000; i <= new Date().getFullYear(); i++) {
    this.yearList.push({ value: i, label: i });

    
  }
  
  }
  multiSelectClassList:any[]=[];
  timeKeeperIdList:any[]=[];
  multiTimeKeeperIdList:any[]=[];
  multiSelectTimeKeeperIdList:any[]=[];
  getAllDropDown() {
    this.spin.show;
    this.cas.getalldropdownlist([
       this.cas.dropdownlist.setup.classId.url,
      // this.cas.dropdownlist.client.clientId.url,
      // this.cas.dropdownlist.matter.matterNumber.url,
      // this.cas.dropdownlist.setup.caseCategoryId.url,
      // this.cas.dropdownlist.setup.caseSubcategoryId.url,
      //this.cas.dropdownlist.matter.dropdown.url,
     // this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.timeKeeperCode.url,
    ]).subscribe((results: any) => {

   
      results[0].forEach((x: any) => {
        this.multiSelectClassList.push({ value: x.classId, label: x.classId + '-' + x.classDescription });
        console.log(this.multiSelectClassList)
      }) 
     

      //timekeeper
      this.timeKeeperIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.timeKeeperCode.key);
      // this.timeKeeperIdList.forEach(timekeeper => {
      //   if(timekeeper.userTypeId == 1){
      //     this.multiTimeKeeperIdList.push({ value: timekeeper.key, label: timekeeper.value })
      //   }
        
      // })
      results[1].forEach(timekeeper => {
          this.multiTimeKeeperIdList.push({ value: timekeeper.timekeeperCode, label: timekeeper.timekeeperCode+'-'+timekeeper.timekeeperName })
      })
      //console.log(this.multiTimeKeeperIdList)
      this.multiSelectTimeKeeperIdList = this.multiTimeKeeperIdList;
      this.searhform.controls.timeKeeperCode.patchValue([this.auth.userID])

      this.service.SearchTimeTicket({
      year:new Date().getFullYear(),    
      }).subscribe(res => {
        this.WeekList = [];
        res.forEach(element => {
          this.WeekList.push({
            value: element.weekOfYear,
            label: "Week" + element.weekOfYear+' '+' '+"("+"("+(this.cs.excel_date((element.sfromDate))+")"+'-'+"("+(this.cs.excel_date(element.stoDate))+")")+")",
          });
          this.WeekList=this.cas.removeDuplicatesFromArrayNew(this.WeekList);
          console.log(this.WeekList);
        });
      
      });
      this.spin.hide;
    }, (err) => {
      this.spin.hide;
      this.toastr.error(err, "");
    });
  }
  onyearChange(value){
    this.service.SearchTimeTicket({year:value.value}).subscribe(res => {
      this.WeekList = [];
      res.forEach(element => {
        this.WeekList.push({
          value: element.weekOfYear,
          label: "Week" + element.weekOfYear+' '+' '+"("+(this.cs.excel_date((element.sfromDate))+'-'+(this.cs.excel_date(element.stoDate)))+")",
        });
        this.WeekList=this.cas.removeDuplicatesFromArrayNew(this.WeekList);
      });
    
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
    this.spin.show();
    this.sub.add(this.service.SearchTimeTicket(this.searhform.getRawValue()).subscribe((res) => {
      res.forEach((x) => {
      //  x.statusIddes = this.statuslist1.find(y => y.key == x.statusId)?.value;
    
      })
    console.log(res);

      this.dataSource.data = res;
      this.dataSource.sort = this.sort;
      //console.log(this.sort)
      //console.log(this.paginator)
      this.dataSource.paginator = this.paginator;
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
    this.searhform.controls.year.patchValue(new Date().getFullYear());
  
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
      data: this.selection.selected[0].timeTicketNumber,
    });
    dialogRef.afterClosed().subscribe(result => {
    
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

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        'Time Keeper Code':x.timeKeeperCode,
        "Week ": x.weekOfYear,
        "From Date" : this.cs.excel_date(x.sfromDate),
        "To Date  ": this.cs.excel_date(x.stoDate),
        "Hours Recorded Past Due":this.decimalPipe.transform(x.hoursCurrentWeek,'1.1-1'),
        "Hours Recorded On Time":this.decimalPipe.transform(x.hoursPreviousWeek,'1.1-1'),
        "Total Hrs":this.decimalPipe.transform(x.timeKeeperHours,'1.1-1'),
        // "Total Amount  ": x.timeKeeperAmount,
        // "Amount For Present Week  ": x.amountCurrentWeek,
        // 'Amount for Previous Week ': x.amountPreviousWeek,
        "Met Goals": x.complianceStatus == 0 ?'Compliant':'Non-Compliant',
        //   'Creation Date ': this.cs.dateapi(x.createdOn),     //error throwing
      });

    })
   
    this.excel.exportAsExcel(res, "Time Entry Compliance Report");
  }

  gettotalAmount() { 
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.timeKeeperAmount != null ? element.timeKeeperAmount : 0);
    })
    return total;
  }
  getAmountforprevious() { 
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.amountPreviousWeek != null ? element.amountPreviousWeek : 0);
    })
    return total;
  }
  getAmountforpresent() { 
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.amountCurrentWeek != null ? element.amountCurrentWeek : 0);
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
  checkboxLabel(row?: any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.timeTicketNumber + 1}`;
  }
  clearselection(row: any) {
    this.selection.clear();
    this.selection.toggle(row);
  }

 
}

