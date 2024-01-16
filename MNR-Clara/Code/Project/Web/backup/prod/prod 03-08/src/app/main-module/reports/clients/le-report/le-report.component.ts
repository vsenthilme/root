import { SelectionModel } from "@angular/cdk/collections";
import { DatePipe } from "@angular/common";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators } from "@angular/forms";
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
import { ReportServiceService } from "../../report-service.service";
@Component({
  selector: 'app-le-report',
  templateUrl: './le-report.component.html',
  styleUrls: ['./le-report.component.scss']
})
export class LeReportComponent implements OnInit {
  screenid = 1161;
  sub = new Subscription();
  public icon = 'expand_more';
  isShowDiv = false;
  table = false;
  fullscreen = false;
  search = true;
  back = false;
  showFloatingButtons: any;
  toggle = true;


  selectedColumn: any[] = [];
  columnList: any[];

  @ViewChild(MatSort) sort: MatSort; @ViewChild(MatPaginator) paginator: MatPaginator; // Pagination
  displayedColumns: string[] = ['select', 'clientId','inquiryNumber', 'corporationClientId',  'firstName', 'lastName', 'firstNameLastName', 'referralId','docketwiseReferenceId', 'clientCategoryDesc', 'addressLine3', 'contactNumber', 'emailId', 'fax', 'homeNo', 'alternateEmailId', 'socialSecurityNo', 'consultationDate', 'consultedBy', 'createdOn', 'createdBy', 'updatedOn', 'updatedBy', 'signedAgreement', 'statusId', 'referenceField4'];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

  submitted = false;
  classIdList: any[] = [];
  clientIdList: any[] = [];
  referralIdList: any[] = [];
  ststusIdList: any[] = [];
  clientCategoryList: any[] = [];

  selectedClass: any[] = [{ id: 1, itemName: "1 - Labor & Employment" }];
  multiselectclassList: any[] = [];
  multiclassList: any[] = [];

  selectedClient: any[] = [];
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];

  selectedReferral: any[] = [];
  multiselectReferralList: any[] = [];
  multiReferralList: any[] = [];

  selectedStatus: any[] = [];
  multiselectStatusList: any[] = [];
  multiStatusList: any[] = [];

  selecteClientCategorys: any[] = [];
  multiselectClientCategoryList: any[] = [];
  multiClientCategoryList: any[] = [];


  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  dropdownSettings1 = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 1
  };

  dropdownSettings3 = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 1,
    disabled: false
  };


  form = this.fb.group({
    classId: [1, [Validators.required]],
    // classIdFE: [, [Validators.required]],
    fromCreatedOn: [,],
    toCreatedOn: [,],
    fromCreatedOnFE: [new Date("01/01/00 00:00:00"),[Validators.required]],
    toCreatedOnFE: [this.cs.todayapi(),[Validators.required]],
    clientCategoryId: [[],],
    clientCategoryIdFE: [,],
    clientId: [[],],
    clientIdFE: [,],
    docketwiseId: [,],
    docketwiseIdFE: [,],
    referralId: [[],],
    referralIdFE: [,],
    statusId: [[],],
    statusIdFE: [,],
    fromDateClosed: [,],
    toDateClosed: [,],
    fromDateClosedFE: [,],
    toDateClosedFE: [,],
  });

  RA: any = {};
  startDate: any;
  currentDate: Date;
  constructor(
    private service: ReportServiceService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private excel: ExcelService,
    private auth: AuthService, 
    private fb: FormBuilder,
    public datepipe: DatePipe,
    private cs: CommonService,
  ) { 

    this.columnList = [
      {value: 'clientno', label: 'Client ID'},
      {value: 'corporationClientId', label: 'corporationClientId'},
      {value: 'name', label: 'name'},
      {value: 'email', label: 'email'},
      {value: 'attorney', label: 'attorney'}
  ];
  }
  
  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    this.dropdownlist();
    this.dropdownSettings3.disabled = true;

    this.selectedColumn = this.displayedColumns;
    console.log(this.selectedColumn)

    this.currentDate = new Date();
    let yesterdayDate = new Date();
    let currentMonthStartDate = new Date();
    yesterdayDate.setDate(this.currentDate.getDate() - 1);
    this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
   currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
  this.form.controls.fromCreatedOnFE.patchValue(currentMonthStartDate);
  }

  onItemSelect(x){
    console.log(x.value)
    this.selectedColumn = this.displayedColumns.filter(col => x.value.includes(col));
  }
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.client.clientId.url,
    this.cas.dropdownlist.setup.referralId.url,
    this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.setup.clientCategoryId.url,
    ]).subscribe((results: any) => {
      this.spin.hide();
      //class
      results[0].forEach((classData: any) => {
        this.multiclassList.push({ value: classData.classId, label: classData.classId + ' - ' + classData.classDescription });
      })
      this.multiclassList = this.multiclassList.filter(classData => classData.value == 1);
      this.multiselectclassList = this.multiclassList;
      //  this.clientIdList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.client.clientId.key);
      this.clientIdList = results[1].filter((data: any) => data.classId == 1);
      this.clientIdList.forEach((client: any) => {
        this.multiclientList.push({ value: client.clientId, label: client.clientId + ' - ' + client.firstNameLastName });
      })
      this.multiselectclientList = this.multiclientList;
      this.referralIdList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.setup.referralId.key);
      this.referralIdList.forEach((x: { key: string; value: string; }) => this.multiReferralList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselectReferralList = this.multiReferralList;

      this.ststusIdList = results[3];
      this.ststusIdList.forEach((status: any) => {
        for (let i = 0; i < this.clientIdList.length; i++) {
          if (status.statusId == this.clientIdList[i].statusId) {
            this.multiStatusList.push({ value: status.statusId, label: status.statusId + ' - ' + status.status });
            break;
          }
        }
      })
      this.multiselectStatusList = this.multiStatusList;
      this.clientCategoryList = this.cas.foreachlist_searchpage(results[4], this.cas.dropdownlist.setup.clientCategoryId.key);
      this.clientCategoryList.forEach((x: { key: string; value: string; }) => this.multiClientCategoryList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselectClientCategoryList = this.multiClientCategoryList;
    }, (err) => {
      this.spin.hide();
      this.toastr.error(err, "");
    });
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

    let  fromCreatedOn = this.cs.datePlusOne(this.form.controls.fromCreatedOnFE.value)  
    this.form.controls.fromCreatedOn.patchValue(this.cs.day_callapiSearch(fromCreatedOn));

    let  toCreatedOn = this.cs.datePlusOne(this.form.controls.toCreatedOnFE.value)  
    this.form.controls.toCreatedOn.patchValue(this.cs.day_callapiSearch(toCreatedOn));

    let  fromDateClosed = this.cs.datePlusOne(this.form.controls.fromDateClosedFE.value)  
    if(this.form.controls.fromDateClosedFE.value){this.form.controls.fromDateClosed.patchValue(this.cs.day_callapiSearch(fromDateClosed));}
    

    let  toDateClosed = this.cs.datePlusOne(this.form.controls.toDateClosedFE.value)  
    if(this.form.controls.toDateClosedFE.value){this.form.controls.toDateClosed.patchValue(this.cs.day_callapiSearch(toDateClosed));}
    this.spin.show();
    this.sub.add(this.service.getClientLandEReport(this.form.getRawValue()).subscribe(res => {
      this.spin.hide();
      res.forEach((data: any) => {
        data.statusId = this.multiselectStatusList.find(y => y.value == data.statusId)?.label;
        data.referralId = this.multiselectReferralList.find(y => y.value == data.referralId)?.label;
      })

      this.dataSource.data = res;
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
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

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
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
  reset() {
    this.form.reset();
    this.form.controls.classId.patchValue(1)
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
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        "Client ID  ": x.clientId,
        "Inquiry No  ": x.inquiryNumber,
        'Corp. Client Name': x.corporationClientId,
        "First Name": x.firstName,
        'Last Name': x.lastName,
        'Full Name ': x.firstNameLastName,
        'Referral Source': x.referralId,
        'Docketwise Referral ID': x.docketwiseReferenceId,
        'Client Category': x.clientCategoryDesc,
        'Billing Address': x.addressLine3,
        'Phone Number': x.contactNumber,
        ' Email Address ': x.emailId,
        ' Fax': x.fax,
        'Home Phone': x.home,
        'Alt. Email': x.alternateEmailId,
        'Tax ID or SSN': x.socialSecurityNo,
        'Const. Date': this.cs.dateapi(x.consultationDate),
        'Const. By': x.consultedBy,
        'Opened Date': this.cs.dateapi(x.createdOn),
        'Opened By/Created By': x.createdBy,
        'Updated On': this.cs.dateapi(x.updatedOn),
        'Updated By': x.updatedBy,
        'Signed Agreement': x.signedAgreement,
        'Status': x.statusId,
        'Closed Date': this.cs.dateapi(x.closedDate),
      });
    })
    this.excel.exportAsExcel(res, "New Client - L&E");
  }

}

