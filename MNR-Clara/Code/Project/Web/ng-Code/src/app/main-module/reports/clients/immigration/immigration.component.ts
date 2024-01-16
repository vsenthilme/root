import { SelectionModel } from "@angular/cdk/collections";
import { DatePipe } from "@angular/common";
import { ThrowStmt } from "@angular/compiler";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators } from "@angular/forms";
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
import { PrebillService } from "src/app/main-module/accounts/prebill/prebill.service";

@Component({
  selector: 'app-immigration',
  templateUrl: './immigration.component.html',
  styleUrls: ['./immigration.component.scss']
})
export class ImmigrationComponent implements OnInit {
  screenid = 1162;
  public icon = 'expand_more';
  isShowDiv = false;
  table = false;
  fullscreen = false;
  search = true;
  back = false;
  showFloatingButtons: any;
  toggle = true;
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

  sub = new Subscription();

  multiSelectClassList: any[] = [];
  multiClassList: any[] = [];
  selectedClassId: any[] = [{ id: 2, itemName: "2 - Immigration" }];

  multiSelectReferralList: any[] = [];
  multiReferralList: any[] = [];
  selectedReferralId: any[] = [];

  multiSelectStatusList: any[] = [];
  multiStatusList: any[] = [];
  selectedStatusId: any[] = [];

  multiSelectClientCategoryList: any[] = [];
  multiClientCategoryList: any[] = [];
  selectedClientCategoryId: any[] = [];

  multiSelectClientList: any[] = [];
  multiClientList: any[] = [];
  selectedClientId: any[] = [];

  multiSelectDocketwiseList: any[] = [];
  multiDocketwiseList: any[] = [];
  selectedDocketwiseId: any[] = [];

  submitted = false;

  statusIdList: any[] = [];
  referralIdList: any[] = [];
  dockerWiseList: any[] = [];
  clientList: any[] = [];
  clientCategoryList: any[] = [];

  dropdownSettings = {
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
  dropdownSettings1 = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };
  form = this.fb.group({
    classId: [2, [Validators.required]],
    // classIdFE: [, [Validators.required]],
    fromCreatedOn: [[],],
    fromCreatedOnFE: [new Date("01/01/00 00:00:00"), [Validators.required]],
    toCreatedOn: [[],],
    toCreatedOnFE: [this.cs.todayapi(), [Validators.required]],
    clientCategoryId: [[],],
    clientCategoryIdFE: [[],],
    clientId: [[],],
    clientIdFE: [,],
    docketwiseId: [[],],
    docketwiseIdFE: [,],
    referralId: [[],],
    referralIdFE: [,],
    statusId: [[], [Validators.required]],
    statusIdFE: [,],
    fromDateClosed: ['',],
    toDateClosed: ['',],
  });

  displayedColumns: string[] = ['select', 'clientId','inquiryNumber', 'corporationClientId', 'firstName', 'lastName', 'firstNameLastName', 'referralIdDescription', 'clientCategoryDesc', 'addressLine3', 'contactNumber', 'emailId', 'fax', 'homeNo', 'alternateEmailId', 'socialSecurityNo', 'consultationDate',  'consultedBy', 'createdOn', 'createdBy','updatedBy', 'signedAgreement', 'statusIdDescription', 'docketwiseReferenceId', 'potentialClientId', 'closedDate'];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

  constructor(
    public dialog: MatDialog,
    private service: PrebillService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    private fb: FormBuilder,
    private excel: ExcelService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    public datepipe: DatePipe,
    private auth: AuthService,
  ) { }
  RA: any = {};
  startDate: any;
  currentDate: Date;

  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllDropDown();
    this.dropdownSettings3.disabled = true;
    this.currentDate = new Date();
    let yesterdayDate = new Date();
    let currentMonthStartDate = new Date();
    yesterdayDate.setDate(this.currentDate.getDate() - 1);
    this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
   currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
  this.form.controls.fromCreatedOnFE.patchValue(currentMonthStartDate);
  }

  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination


  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }
  getAllDropDown() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.client.clientId.url,
      this.cas.dropdownlist.setup.referralId.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.clientCategoryId.url,
    ]).subscribe((results: any) => {

      //class
      results[0].forEach((classData: any) => {
        this.multiClassList.push({ value: classData.classId, label: classData.classId + ' - ' + classData.classDescription });
      })
      this.multiClassList = this.multiClassList.filter(classData => classData.value == 2)
      this.multiSelectClassList = this.multiClassList;
      console.log(this.form.controls.classId)
      //client
      this.clientList = results[1].filter((data: any) => data.classId == 2);
      console.log(this.clientList)
      this.clientList.forEach((client: any) => {
        this.multiClientList.push({ value: client.clientId, label: client.clientId + ' - ' + client.firstNameLastName });
      })
      this.multiSelectClientList = this.multiClientList;

      //referral
      this.referralIdList = results[2];
      this.referralIdList.forEach((referral: any) => {
        for (let i = 0; i < this.clientList.length; i++) {
          if (referral.referralId == this.clientList[i].referralId) {
            this.multiReferralList.push({ value: referral.referralId, label: referral.referralId + ' - ' + referral.referralDescription });
            break;
          }
        }
      })
      this.multiSelectReferralList = this.multiReferralList;

      //status
      this.statusIdList = results[3];
      this.statusIdList.forEach((status: any) => {
        for (let i = 0; i < this.clientList.length; i++) {
          if (status.statusId == this.clientList[i].statusId) {
            console.log(2)
            this.multiStatusList.push({ value: status.statusId, label: status.statusId + ' - ' + status.status });
            break;
          }
        }
      })
      this.multiSelectStatusList = this.multiStatusList;

      //client-category
      this.clientCategoryList = results[4];
      this.clientCategoryList.forEach((clientCategory: any) => {
        for (let i = 0; i < this.clientList.length; i++) {
          if (clientCategory.clientCategoryId == this.clientList[i].clientCategoryId) {
            this.multiClientCategoryList.push({ value: clientCategory.clientCategoryId, label: clientCategory.clientCategoryId + ' - ' + clientCategory.clientCategoryDescription });
            break;
          }
        }
      })
      this.multiSelectClientCategoryList = this.multiClientCategoryList;

      //docketwise
      this.clientList.forEach((client: any) => {
        if (client.referenceField9 != null) {
          this.multiDocketwiseList.push({ value: client.referenceField9, label: client.referenceField9 });
        }
      })
      this.multiSelectDocketwiseList = this.multiDocketwiseList;
    }, (err) => {
      this.toastr.error(err, "");
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  @ViewChild(MatSort) sort: MatSort; // Pagination
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
    this.form.controls.fromDateClosed.patchValue(this.cs.day_callapiSearch(this.form.controls.fromDateClosed.value));
    this.form.controls.toDateClosed.patchValue(this.cs.day_callapiSearch(this.form.controls.toDateClosed.value));
    this.spin.show();
    this.sub.add(this.service.getClientImmigrationReport(this.form.getRawValue()).subscribe(res => {
      this.spin.hide();
      this.dataSource.data = res;
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.table = true;
      this.search = false;
      //this.fullscreen = true;
      this.back = true;
    },
      err => {
        this.spin.hide();
        this.submitted = false;
        this.cs.commonerror(err);
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

  onItemSelect(item: any, type?: any) {
    if (type == 'CLASS') {
      this.filterBasedOnClass();
    }
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

  filterBasedOnClass() {
    // this.multiSelectInquiryAssignedList = [];
    // this.selectedInquiryAssignedId = [];
    // this.form.controls.inquiryAssignedToRefField4FE.setValue([]);
    // this.multiInquiryAssignedList.forEach(element => {
    //   if (element['classId'] == this.selectedClassId[0].id) {
    //     this.multiSelectInquiryAssignedList.push(element)
    //   }
    // });

    // this.multiSelectAttorneyList = [];
    // this.selectedAttorneyId = [];
    // this.form.controls.consultingAttorneyRefField2FE.setValue([]);
    // this.multiAttorneyList.forEach(element => {
    //   if (element['classId'] == this.selectedClassId[0].id) {
    //     this.multiSelectAttorneyList.push(element)
    //   }
    // });

  }

  reset() {
    this.form.reset();
    this.form.controls.classId.patchValue(2)
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
        'Client Category': x.clientCategoryDesc,
        'Billing Address': x.addressLine3,
        'Phone Number': x.contactNumber,
        ' Email Address ': x.emailId,
        ' Fax': x.fax,
        'Home Phone': x.homeNo,
        'Alt. Email': x.alternateEmailId,
        'Tax ID or SSN': x.socialSecurityNo,
        'Docketwise Reference ID': x.docketwiseReferenceId,
        'Prospective Client ID': x.potentialClientId,
        'Const. Date': this.cs.dateapi(x.consultationDate),
        'Const. By': x.consultedBy,
        'Opened Date': this.cs.dateapi(x.createdOn),
        'Opened By/Created By': x.createdBy,
        'Updated On': this.cs.dateapi(x.updatedOn),
        'Updated By': x.updatedBy,
        'Signed Agreement': x.signedAgreement,
        'Referral Source': x.referralIdDescription,
        'Status': x.statusIdDescription,
        'Closed Date': this.cs.dateapi(x.closedDate),
      });

    })
    this.excel.exportAsExcel(res, "New Client - Immigration");
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

