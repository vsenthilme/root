import { SelectionModel } from "@angular/cdk/collections";
import { DatePipe } from "@angular/common";
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

export interface PeriodicElement {
  name: string;
  email: string;
  attorney: string;
  clientno: string;
  inquiry: string;
  date: string;
  by: string;
  followup: string;
  notes: string;
}

@Component({
  selector: 'app-prospective-client',
  templateUrl: './prospective-client.component.html',
  styleUrls: ['./prospective-client.component.scss']
})
export class ProspectiveClientComponent implements OnInit {
  screenid = 1158;
  public icon = 'expand_more';
  isShowDiv = false;
  table = true;
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
  selectedClassId: any[] = [];

  multiSelectReferralList: any[] = [];
  multiReferralList: any[] = [];
  selectedReferralId: any[] = [];

  multiSelectStatusList: any[] = [];
  multiStatusList: any[] = [];
  selectedStatusId: any[] = [];

  multiSelectOnBoardingStatusList: any[] = [];
  multiOnBoardingStatusList: any[] = [];
  selectedOnBoardingStatusId: any[] = [];

  multiSelectInquiryList: any[] = [];
  multiInquiryList: any[] = [];
  selectedInquiryId: any[] = [];

  multiSelectAttorneyList: any[] = [];
  multiAttorneyList: any[] = [];
  selectedAttorneyId: any[] = [];

  multiSelectRetainedByList: any[] = [];
  multiRetainedByList: any[] = [];
  selectedRetainedById: any[] = [];

  multiSelectPotentialClientList: any[] = [];
  selectedPotentialClient: any[] = [];
  potentialClientDetailsList: any[] = [];

  submitted = false;

  statusIdList: any[] = [];
  referralIdList: any[] = [];
  inquiryList: any[] = [];
  potentialClientList: any[] = [];
  userIdList: any[] = [];

  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 1
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
    classId: [, [Validators.required]],
    //classIdFE: [, [Validators.required]],
    fromCreatedOn: [new Date("01/01/00 00:00:00"), [Validators.required]],
    toCreatedOn: [this.cs.todayapi(), [Validators.required]],
    referralId: [,],
    //referralIdFE: [,],
    consultingAttorneyRefField4: [,],
   // consultingAttorneyRefField4FE: [,],
    inquiryNumber: [,],
    //inquiryNumberFE: [,],
    onBoardingStatusRefField3: [,],
  // onBoardingStatusRefField3FE: [,],
    potentialClientId: [,],
    //potentialClientIdFE: [,],
    retainedByRefField2: [,],
  //  retainedByRefField2FE: [,],
    statusId: [, [Validators.required]],
   // statusIdFE: [, [Validators.required]],
  });


  displayedColumns: string[] = ['select', 'classId', 'potentialClientId', 'firstNameLastName', 'addressLine1', 'contactNumber',  'emailId', 'referralId', 'referenceField4', 'referenceField2', 'statusId', 'onboardingStatus','socialSecurityNo'];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<PeriodicElement>(true, []);

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
  checkboxLabel(row?: PeriodicElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.name + 1}`;
  }
  constructor(
    public dialog: MatDialog,
    private service: PrebillService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    private fb: FormBuilder,
    public toastr: ToastrService,
    private excel: ExcelService,
    private cas: CommonApiService,
    public datepipe: DatePipe,
    private auth: AuthService) { }

    RA: any = {};

     startDate: any;
  currentDate: Date;
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllDropDown();
    this.currentDate = new Date();
    let yesterdayDate = new Date();
    let currentMonthStartDate = new Date();
    yesterdayDate.setDate(this.currentDate.getDate() - 1);
    this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
   currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
  this.form.controls.fromCreatedOn.patchValue(currentMonthStartDate);
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
  @ViewChild(MatPaginator) paginator: MatPaginator;
 @ViewChild(MatSort) sort: MatSort;
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
    this.table = true;
    this.search = true;
    this.fullscreen = true;
    this.back = false;
  }

  getAllDropDown() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.referralId.url,
      this.cas.dropdownlist.crm.inquiryNumber.url,
      this.cas.dropdownlist.crm.potentialClientId.url,
      this.cas.dropdownlist.setup.userId.url,
    ]).subscribe((results: any) => {
      results[0].forEach((classData: any) => {
        this.multiClassList.push({ value: classData.classId, label: classData.classId + ' - ' + classData.classDescription })
        this.multiClassList = this.multiClassList.filter(classData => classData.value == 1 || classData.value == 2)
      })
      this.multiSelectClassList = this.multiClassList;

      this.statusIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.statusId.key);

      this.referralIdList = results[2];

      this.inquiryList = this.cas.foreachlist(results[3], this.cas.dropdownlist.crm.inquiryNumber.key);

      this.potentialClientDetailsList = results[4];
      results[4].forEach((potentialData: any) => {
        this.potentialClientList.push({ value: potentialData.potentialClientId, label: potentialData.potentialClientId + ' - ' + potentialData.lastNameFirstName, classId: potentialData.classId })
      })
      this.multiSelectPotentialClientList = this.potentialClientList;

      this.userIdList = results[5];
      this.statusIdList.forEach(status => {
        //status
        for (let i = 0; i < this.potentialClientDetailsList.length; i++) {
          if (status.key == this.potentialClientDetailsList[i].statusId) {
            this.multiStatusList.push({ value: status.key, label: status.value })
            break;
          }
        }

        //on-boarding status
        for (let j = 0; j < this.potentialClientDetailsList.length; j++) {
          if (status.key == this.potentialClientDetailsList[j].referenceField3) {
            this.multiOnBoardingStatusList.push({ value: status.key, label: status.value })
            break;
          }
        }
      })
      this.multiSelectStatusList = this.multiStatusList;
      this.multiSelectOnBoardingStatusList = this.multiOnBoardingStatusList;

      //referral
      this.referralIdList.forEach(referral => {
        for (let i = 0; i < this.potentialClientDetailsList.length; i++) {
          if (referral.referralId == this.potentialClientDetailsList[i].referralId) {
            this.multiReferralList.push({ value: referral.referralId, label: referral.referralId + ' - ' + referral.referralDescription, classId: referral.classId })
            break;
          }
        }
      })
      this.multiSelectReferralList = this.multiReferralList;

      this.userIdList.forEach(user => {
        //consulting-attorney
        for (let i = 0; i < this.potentialClientDetailsList.length; i++) {
          if (user.userId == this.potentialClientDetailsList[i].referenceField4) {
            this.multiAttorneyList.push({ value: user.userId, label: user.fullName, classId: user.classId })
            break;
          }
        }

        //retained-by
        for (let i = 0; i < this.potentialClientDetailsList.length; i++) {
          if (user.userId == this.potentialClientDetailsList[i].referenceField2) {
            this.multiRetainedByList.push({ value: user.userId, label: user.fullName, classId: user.classId })
            break;
          }
        }
      })
      this.multiSelectAttorneyList = this.multiAttorneyList;
      this.multiSelectRetainedByList = this.multiRetainedByList;

      //inquiry
      this.potentialClientDetailsList.forEach((potential: any) => {
        this.multiInquiryList.push({ value: potential.inquiryNumber, label: potential.inquiryNumber, classId: potential.classId })
      })
      this.multiSelectInquiryList = this.multiInquiryList;

    }, (err) => {
      this.toastr.error(err, "");
    });
  }

  onItemSelect(item: any, type?: any) {
    if (type == 'CLASS') {
      this.filterBasedOnClass();
    }
  }
  filterBasedOnClass() {
    console.log(this.form.controls.classId.value)
    this.multiSelectReferralList = [];
    this.selectedReferralId = [];
    this.form.controls.referralId.setValue([]);
    this.multiReferralList.forEach(element => {
      if (element['classId'] == this.form.controls.classId.value) {
        this.multiSelectReferralList.push(element)
      }
    });

    this.multiSelectPotentialClientList = [];
    this.selectedPotentialClient = [];
    this.form.controls.potentialClientId.setValue([]);
    this.potentialClientList.forEach(element => {
      if (element['classId'] == this.form.controls.classId.value) {
        this.multiSelectPotentialClientList.push(element)
      }
    });

    this.multiSelectInquiryList = [];
    this.multiInquiryList.forEach(element => {
      if (element['classId'] == this.form.controls.classId.value) {
        this.multiSelectInquiryList.push(element)
      }
    });

    // this.multiSelectStatusList = [];
    // this.multiStatusList.forEach(element => {
    //   if (element['classId'] == this.selectedClassId[0].id) {
    //     this.multiSelectStatusList.push(element)
    //   }
    // });

    // this.multiSelectOnBoardingStatusList = [];
    // this.multiOnBoardingStatusList.forEach(element => {
    //   if (element['classId'] == this.selectedClassId[0].id) {
    //     this.multiSelectOnBoardingStatusList.push(element)
    //   }
    // });

    this.multiSelectAttorneyList = [];
    this.multiAttorneyList.forEach(element => {
      if (element['classId'] == this.form.controls.classId.value) {
        this.multiSelectAttorneyList.push(element)
      }
    });

    this.multiSelectRetainedByList = [];
    this.multiRetainedByList.forEach(element => {
      if (element['classId'] == this.form.controls.classId.value) {
        this.multiSelectRetainedByList.push(element)
      }
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

  // Pagination
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
    // if (this.selectedClassId && this.selectedClassId.length > 0) {
    //   this.form.patchValue({ classId: this.selectedClassId[0].id });
    // }
    // if (this.selectedReferralId && this.selectedReferralId.length > 0) {
    //   let data: any[] = []
    //   this.selectedReferralId.forEach((a: any) => data.push(a.id))
    //   this.form.patchValue({ referralId: data });
    // }
    // if (this.selectedPotentialClient && this.selectedPotentialClient.length > 0) {
    //   let data: any[] = []
    //   this.selectedPotentialClient.forEach((a: any) => data.push(a.id))
    //   this.form.patchValue({ potentialClientId: data });
    // }
    // if (this.selectedInquiryId && this.selectedInquiryId.length > 0) {
    //   let data: any[] = []
    //   this.selectedInquiryId.forEach((a: any) => data.push(a.id))
    //   this.form.patchValue({ inquiryNumber: data });
    // }
    // if (this.selectedStatusId && this.selectedStatusId.length > 0) {
    //   let data: any[] = []
    //   this.selectedStatusId.forEach((a: any) => data.push(a.id))
    //   this.form.patchValue({ statusId: data });
    // }
    // if (this.selectedOnBoardingStatusId && this.selectedOnBoardingStatusId.length > 0) {
    //   let data: any[] = []
    //   this.selectedOnBoardingStatusId.forEach((a: any) => data.push(a.id))
    //   this.form.patchValue({ onBoardingStatusRefField3: data });
    // }
    // if (this.selectedAttorneyId && this.selectedAttorneyId.length > 0) {
    //   let data: any[] = []
    //   this.selectedAttorneyId.forEach((a: any) => data.push(a.id))
    //   this.form.patchValue({ consultingAttorneyRefField4: data });
    // }
    // if (this.selectedRetainedById && this.selectedRetainedById.length > 0) {
    //   let data: any[] = []
    //   this.selectedRetainedById.forEach((a: any) => data.push(a.id))
    //   this.form.patchValue({ retainedByRefField2: data });
    // }
    this.form.controls.fromCreatedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.fromCreatedOn.value));
    this.form.controls.toCreatedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.toCreatedOn.value));
    this.spin.show();
    this.sub.add(this.service.getPotentialClientReport(this.form.getRawValue()).subscribe(res => {

      this.dataSource.data = res;
      this.dataSource.sort = this.sort;
       this.dataSource.paginator = this.paginator;
      this.dataSource.data.forEach((data: any) => {
        console.log(this.multiClassList)
        console.log(this.multiStatusList)
        console.log(this.referralIdList)
        data.classId = this.multiClassList.find(y => y.value == data.classId)?.label;
        data.statusId = this.multiStatusList.find(y => y.value == data.statusId)?.label;
        data.referralId = this.referralIdList.find(y => y.referralId == data.referralId)?.referralDescription;
        data.potentialClientId = this.potentialClientDetailsList.find(y => y.potentialClientId == data.potentialClientId)?.firstNameLastName;
        // if (data.referenceField3) {
        //   data.referenceField3 = this.multiOnBoardingStatusList.find(y => y.id == +data.referenceField3)?.label;
        // }

        if(data.referenceField3 == 3){
          data['onboardingStatus'] = 'Fees too High';
        }
        if(data.referenceField3 == 1){
          data['onboardingStatus'] = 'No Relif Available';
        }
        if(data.referenceField3 == 2){
          data['onboardingStatus'] = 'Waiting for Reform';
        }
        if(data.referenceField3 == 4){
          data['onboardingStatus'] = 'Second opinion only';
        } 
        if(data.referenceField3 == 5){
          data['onboardingStatus'] = 'Follow up with Client';
        }
      })

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
  reset() {
    this.form.reset();
  }

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        'Class': x.classId,
        "Prospective Client Name ": x.potentialClientId,
        "Name ": x.firstNameLastName,
        'Address ': x.addressLine1,
        'Phone No': x.contactNumber,
        'Email': x.emailId,
        'Referral Source': x.referralId,
        'Consulting Attorney': x.referenceField4,
        'Follow Up': x.referenceField2,
        'Status': x.statusId,
        'Onboarding Status': x.onboardingStatus,
        'SSN': x.socialSecurityNo,
      });

    })
    this.excel.exportAsExcel(res, "Prospective Client");
  }
}

