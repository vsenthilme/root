import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators } from "@angular/forms";
import { MatTableDataSource } from "@angular/material/table";
import { NgxSpinnerService } from "ngx-spinner";
import { DatePipe, DecimalPipe } from "@angular/common";
import { ToastrService } from "ngx-toastr";
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { ReportServiceService } from "../../report-service.service";
import { MatSort } from "@angular/material/sort";
@Component({
  selector: 'app-wip-aged',
  templateUrl: './wip-aged.component.html',
  styleUrls: ['./wip-aged.component.scss']
})
export class WipAgedPBComponent implements OnInit {
  screenid = 1166;
  public icon = 'expand_more';
  isShowDiv = false;
  table = true;
  fullscreen = false;
  search = true;
  back = false;
  showFloatingButtons: any;
  toggle = true;
  RA: any = {};
  startDate: any;
  currentDate: Date;

  displayedColumns: string[] = [
    "select",
    'classId',
    "billingModeId",
    "clientName",
    "fees",
    'partners',
    "matterName",
    "priorBalance",
    "responsibleAttorneys",
    "softCosts",
    "timeTicketAmount30To60Days",
    "timeTicketAmount61To90Days",
    "timeTicketAmount91DaysTo120Days",
    "total"
  ];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

  sub = new Subscription();

  multiClassList: any[] = [];
  multiClientList: any[] = [];
  multiStatusList: any[] = [];
  multiMatterList: any[] = [];
  multiCaseCategoryList: any[] = [];
  multiCaseCategoryListAllData: any[] = [];
  multiCaseSubCategoryList: any[] = [];
  multiSubCaseCategoryListAllData: any[] = [];
  multiPartnerList: any[] = [];

  submitted = false;

  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };

  form = this.fb.group({
    classId: [,],
    classIdFE: [,],
    clientId: [,],
    clientIdFE: [,],
    caseCategoryId: [,],
    caseCategoryIdFE: [,],
    caseSubCategoryId: [,],
    caseSubCategoryIdFE: [,],
    statusId: [,],
    statusIdFE: [,],
    partner: [,],
    partnerFE: [,],
    matterNumber: [,],
    matterNumberFE: [,],
    toDate: [,],
    fromDate: [, ],
    toDateFE: [this.cs.todayapi(), [Validators.required]],
    fromDateFE: [new Date("01/01/00 00:00:00"), [Validators.required]]

  });

  constructor(
    private service: ReportServiceService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    private excel: ExcelService,
    private decimalPipe: DecimalPipe,
    private fb: FormBuilder,
    public toastr: ToastrService,
    private cas: CommonApiService,
    public datepipe: DatePipe,
    private auth: AuthService
  ) { }


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true })
  sort: MatSort;

  
  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllDropDown();
    this.currentDate = new Date();
    let yesterdayDate = new Date();
    let currentMonthStartDate = new Date();
    yesterdayDate.setDate(this.currentDate.getDate() - 1);
    this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
   currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
  this.form.controls.fromCaseOpenDate.patchValue(currentMonthStartDate);
  }


  
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  searchkeyboard() {
    this.form.controls.matterNumber.patchValue([this.form.controls.matterNumber.value])
    if (this.form.controls.matterNumber.value == "")
      this.form.reset();
    this.getWipReportData();
  }

  filtersearch() {
    this.submitted = true;
    if (this.form.invalid) {
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

    // if (this.form.controls.classId.value && !this.form.controls.caseCategoryId.value && !this.form.controls.partner.value!) {
    //   this.toastr.error(
    //     "Please fill partner or case categories fields to continue",
    //     "Notification", {
    //     timeOut: 2000,
    //     progressBar: false,
    //   }
    //   );
    //   return;
    // }
 
    this.form.controls.toDate.patchValue(this.cs.dateNewFormat(this.form.controls.toDateFE.value));
    this.form.controls.fromDate.patchValue(this.cs.dateNewFormat(this.form.controls.fromDateFE.value));

    this.spin.show();
    //this.getWipReportData();
    this.sub.add(this.service.getMatterWipAgedPBReport(this.form.getRawValue()).subscribe(res => {
      this.dataSource.data = res;
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.totalRecords = this.dataSource.data.length;
      // this.dataSource.data.forEach((data: any) => {
      //   data.statusId = this.statusIdList.find(y => y.key == data.statusId)?.value;
      // })
      this.spin.hide();
      this.table = true;
      this.search = false;
      //this.fullscreen = true;
      this.back = true;
    },
      err => {
        this.submitted = false;
        this.cs.commonerror(err);
        this.spin.hide();
      }));

  }
  pageNumber = 0;
  pageSize = 250;
  totalRecords = 0;


  pageHandler($event: PageEvent) {
    this.pageNumber = $event.pageIndex;
    this.pageSize = $event.pageSize;
    this.getWipReportData();

  }


  getWipReportData() {
    this.spin.show()
    this.service.getWipReportDetails(this.form.getRawValue(), this.pageNumber, this.pageSize).subscribe(

      result => {
        this.dataSource = new MatTableDataSource<any>(result.content);
        //this.dataSource.sort = this.sort;
        this.totalRecords = result.totalElements;
        this.spin.hide();
      },
      error => {
        console.log(error);
        this.spin.hide();
      }
    );
  }

  togglesearch() {
    this.search = false;
    this.table = true;
    this.fullscreen = false;
    this.back = true;
  }
  backsearch() {
    this.table = true;
    this.search = true;
    this.fullscreen = true;
    this.back = false;
  }
  reset() {
    this.form.reset();
  }
  caseCategoryIdList1: any[] = [];
  caseSubCategoryIdList1: any[] = [];


  getAllDropDown() {
    this.spin.show;
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.matter.dropdown.url,
      this.cas.dropdownlist.setup.userId.url,
      this.cas.dropdownlist.setup.caseCategoryId.url,
      this.cas.dropdownlist.setup.caseSubcategoryId.url,
    ]).subscribe((results: any) => {
      this.spin.hide;
      //class
      results[0].classList.forEach((classData: any) => {
        if (classData.key == 1 || classData.key == 2) {
          this.multiClassList.push({ value: classData.key, label: classData.key + ' - ' + classData.value })
        }
      })

      //client
      results[0].clientNameList.forEach((client: any) => {
        this.multiClientList.push({ value: client.key, label: client.key + ' - ' + client.value });
      })

      //matter
      results[0].matterList.forEach((matter: any) => {
        this.multiMatterList.push({ value: matter.key, label: matter.key + ' - ' + matter.value });
      })

      //caseCategory
      results[0].caseCategoryList.forEach((category: any) => {
        this.multiCaseCategoryList.push({ value: category.key, label: category.key + ' - ' + category.value });
      })
      this.multiCaseCategoryListAllData = this.multiCaseCategoryList
      //caseSubCategory
      results[0].subCaseCategoryList.forEach((subCategory: any) => {
        this.multiCaseSubCategoryList.push({ value: subCategory.key, label: subCategory.key + ' - ' + subCategory.value });
      })
      this.multiSubCaseCategoryListAllData = this.multiCaseSubCategoryList
      //status
      results[0].statusIdList.forEach((status: any) => {
        this.multiStatusList.push({ value: status.key, label: status.key + ' - ' + status.value });
      })

      this.caseCategoryIdList1 = results[2]
      this.caseSubCategoryIdList1 = results[3]
      //partner
      let userData: any[] = results[1];
      userData.forEach(user => {
        if (user.userTypeId == 1) {
          this.multiPartnerList.push({ value: user.userId, label: user.userId + '-' + user.fullName })
        }
      })
    }, (err) => {
      this.spin.hide;
      this.toastr.error(err, "");
    });
  }

  public errorHandling = (control: string, error: string = "required") => {
    if (control.includes('.')) {
      const controls = this.form.get(control);
      return controls ? controls.hasError(error) : false;

    }
    return this.form.controls[control].hasError(error);
  }
  getErrorMessage(type: string) {
    if (!this.form.valid && this.submitted) {
      if (this.form.controls[type].hasError('required')) {
        return 'Field should not be blank';
      } else {
        return '';
      }
    } else {
      return '';
    }
  }

  downloadexcel() {
    // this.service.getMatterWipAgedPBReport(this.form.getRawValue()).subscribe(result => {
    var res: any = [];
    this.dataSource.data.forEach(x => {
      this.headerDataLine(x, 'timeTicketHours', res);
      this.headerDataLine(x, 'fees', res);
      this.headerDataLine(x, 'hardCost', res);
      this.headerDataLine(x, 'softCost', res);
    })
    this.excel.exportAsExcel(res, "WIP Aged Report");
    // },
    //   error => {
    //     console.log(error);
    //     this.spin.hide();
    //   }
    // );
  }

  headerDataLine(element, type: any, res: any[]) {
    res.push({
      'Class': type == 'timeTicketHours' ? (element.classId == 1 ? 'Labor & Employement' : 'Immigration') : '',
      'Client / Matter': type == 'timeTicketHours' ? (element.clientNumber + '/' + element.matterNumber) : '',
      'Name / Matter Desc ': type == 'timeTicketHours' ? (element.clientName + '/' + element.matterName) : '',
      'Billing Mode': type == 'timeTicketHours' ? (element.billingModeId) : type == 'fees' ? 'Prior Balance: ' + (element.priorBalance != null ? element.priorBalance : '$ 0.00') : '',
      'Partners': element.partners,
      'Resp': type == 'timeTicketHours' ? (element.responsibleAttorneys) : '',
      'Details': type == 'timeTicketHours' ? 'Hours' : type == 'fees' ? 'Fees' : type == 'hardCost' ? 'Hard Cost' : type == 'softCost' ? 'Soft Cost' : '',
      'Current': type == 'timeTicketHours' ? (element.current != null ? element.current[type] != null ? element.current[type] : '0.0' : '0.0') : (element.current != null ? element.current[type] != null ? this.decimalPipe.transform(element.current[type], "1.2-2") : '$ 0.00' : '0.00'),
      '31-60 Days': type == 'timeTicketHours' ? (element.from30To60Days != null ? element.from30To60Days[type] != null ? element.from30To60Days[type] : '0.0' : '0.0') : (element.from30To60Days != null ? element.from30To60Days[type] != null ? this.decimalPipe.transform(element.from30To60Days[type], "1.2-2") : '$ 0.00' : '0.00'),
      '61-90 Days': type == 'timeTicketHours' ? (element.from61To90Days != null ? element.from61To90Days.timeTicketHours != null ? element.from61To90Days.timeTicketHours : '0.0' : '0.0') : (element.from61To90Days != null ? element.from61To90Days[type] != null ? this.decimalPipe.transform(element.from61To90Days[type], "1.2-2") : '$ 0.00' : '$ 0.00'),
      '91-120 Days': type == 'timeTicketHours' ? (element.from91To120DDays != null ? element.from91To120DDays.timeTicketHour != null ? element.from91To120DDays.timeTicketHours : '0.0' : '0.0') : (element.from91To120DDays != null ? element.from91To120DDays[type] != null ? this.decimalPipe.transform(element.from91To120DDays[type], "1.2-2") : '$ 0.00' : '$ 0.00'),
      'Over 120 Days': type == 'timeTicketHours' ? (element.over120Days != null ? element.over120Days.timeTicketHours != null ? element.over120Days.timeTicketHours : '0.0' : '0.0') : (element.over120Days != null ? element.over120Days[type] != null ? this.decimalPipe.transform(element.over120Days[type], "1.2-2") : '$ 0.00' : '$ 0.00'),
      'Total': type == 'timeTicketHours' ? (this.decimalPipe.transform(this.totalcurrency(element), "1.2-2")) : type == 'fees' ? (this.decimalPipe.transform(this.totalfees(element), "1.2-2")) : type == 'hardCost' ? (this.decimalPipe.transform(this.totalhardCost(element), "1.2-2")) : type == 'softCost' ? (this.decimalPipe.transform(this.totalhardCost(element), "1.2-2")) : '0.00',
    });
  }

  totalcurrency(element) {
    let total = 0;
    total = total + (element.current != null ? element.current.timeTicketHours != null ? element.current.timeTicketHours : 0 : 0) +
      (element.from30To60Days != null ? element.from30To60Days.timeTicketHours != null ? element.from30To60Days.timeTicketHours : 0 : 0) +
      (element.from61To90Days != null ? element.from61To90Days.timeTicketHours != null ? element.from61To90Days.timeTicketHours : 0 : 0) +
      (element.from91To120DDays != null ? element.from91To120DDays.timeTicketHours != null ? element.from91To120DDays.timeTicketHours : 0 : 0) +
      (element.over120Days != null ? element.over120Days.timeTicketHours != null ? element.over120Days.timeTicketHours : 0 : 0);
    return (Math.round(total * 100) / 100);
  }

  totalfees(element) {
    let total = 0;
    total = total + (element.current != null ? element.current.fees != null ? element.current.fees : 0 : 0) +
      (element.from30To60Days != null ? element.from30To60Days.fees != null ? element.from30To60Days.fees : 0 : 0) +
      (element.from61To90Days != null ? element.from61To90Days.fees != null ? element.from61To90Days.fees : 0 : 0) +
      (element.from91To120DDays != null ? element.from91To120DDays.fees != null ? element.from91To120DDays.fees : 0 : 0) +
      (element.over120Days != null ? element.over120Days.fees != null ? element.over120Days.fees : 0 : 0);
    return (Math.round(total * 100) / 100);
  }

  totalhardCost(element) {
    let total = 0;
    total = total + (element.current != null ? element.current.hardCost != null ? element.current.hardCost : 0 : 0) +
      (element.from30To60Days != null ? element.from30To60Days.hardCost != null ? element.from30To60Days.hardCost : 0 : 0) +
      (element.from61To90Days != null ? element.from61To90Days.hardCost != null ? element.from61To90Days.hardCost : 0 : 0) +
      (element.from91To120DDays != null ? element.from91To120DDays.hardCost != null ? element.from91To120DDays.hardCost : 0 : 0) +
      (element.over120Days != null ? element.over120Days.hardCost != null ? element.over120Days.hardCost : 0 : 0);
    return (Math.round(total * 100) / 100);
  }
  totalsoftCosts(element) {
    let total = 0;
    total = total + (element.current != null ? element.current.softCosts != null ? element.current.softCosts : 0 : 0) +
      (element.from30To60Days != null ? element.from30To60Days.softCosts != null ? element.from30To60Days.softCosts : 0 : 0) +
      (element.from61To90Days != null ? element.from61To90Days.softCosts != null ? element.from61To90Days.softCosts : 0 : 0) +
      (element.from91To120DDays != null ? element.from91To120DDays.softCosts != null ? element.from91To120DDays.softCosts : 0 : 0) +
      (element.over120Days != null ? element.over120Days.softCosts != null ? element.over120Days.softCosts : 0 : 0);
    return (Math.round(total * 100) / 100);
  }


  getcaseCategoryId(code: string) {
    console.log(code)
    if (code.length == 1) {
      this.multiCaseCategoryList = [];
   console.log(this.caseCategoryIdList1 )
      this.caseCategoryIdList1 .forEach(Category => {
        if (code == Category.classId) {
          this.multiCaseCategoryList.push({ value: Category.caseCategoryId, label: Category.caseCategoryId + '-' + Category.caseCategory })
        }
      })
    }else{
      this.multiCaseCategoryList = this.multiCaseCategoryListAllData
    }
  }

  getcasesubCategoryId(code: string) {

    if (code.length == 1) { 
      this.multiCaseSubCategoryList = [];
      this.caseSubCategoryIdList1.forEach(Category => {
        if (code == Category.classId) {
          this.multiCaseSubCategoryList.push({ value: Category.caseSubcategoryId, label: Category.caseSubcategoryId + '-' + Category.subCategory })
        }
      })
    }else{
      this.multiCaseSubCategoryList = this.multiSubCaseCategoryListAllData
    }
  }

  onItemSelect(item: any) {
   this.getcaseCategoryId(this.form.controls.classId.value)
  this.getcasesubCategoryId(this.form.controls.classId.value)
  if(item.value.length < 1){
      this.form.controls.classId.patchValue(null)
    }
  }
  OnSelectMatter(item: any) {
    if(item.value.length < 1){
      this.form.controls.matterNumber.patchValue(null)
    }
  }
  OnSelectClient(item: any) {
    if(item.value.length < 1){
      this.form.controls.clientId.patchValue(null)
    }
  }
  OnSelectCase(item: any) {
    console.log(this.multiCaseCategoryList)
    if(item.value.length < 1){
      this.form.controls.caseCategoryId.patchValue(null)
    }
  }
  OnSelectCaseSub(item: any) {
    if(item.value.length < 1){
      this.form.controls.caseSubCategoryId.patchValue(null)
    }
  }
  OnSelectPartner(item: any) {
    if(item.value.length < 1){
      this.form.controls.partner.patchValue(null)
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.name + 1}`;
  }

}

