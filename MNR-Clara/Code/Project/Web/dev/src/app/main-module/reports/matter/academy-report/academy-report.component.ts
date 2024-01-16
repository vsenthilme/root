import { SelectionModel } from '@angular/cdk/collections';
import { DatePipe, DecimalPipe } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Validators } from 'ngx-editor';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { ReportServiceService } from '../../report-service.service';
import { ClientGeneralService } from 'src/app/main-module/client/client-general/client-general.service';

@Component({
  selector: 'app-academy-report',
  templateUrl: './academy-report.component.html',
  styleUrls: ['./academy-report.component.scss']
})
export class AcademyReportComponent implements OnInit {
  screenid = 1195;
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
    "clientId",
    "matterNumber",
    "matterDescription",
    "partner",
   "assignedTimekeeper",
    "paralegal",
    "legalAssistant",
    "clientName",
    "position",
   "dateOfHire",
    "city",
    "state",
    "caseSubCategoryDescription",
    "eligibilityDate",
    "expirationDate",
    "receiptDate",
    "academyComments",
    "mrjobDescriptionNote",
    "mrnivnote",
    "mrivnote",
    "h1BEndDate",
    "priorityDate",
    "auditEmail",
    "leadership",
    "hrPartners",
    "emailId",
  ];
  multiClassList: any[] = [];
  selectedClassId: any[] = [];

  multiSelectReferralList: any[] = [];
  multiReferralList: any[] = [];
  selectedReferralId: any[] = [];

  multiSelectStatusList: any[] = [];
  multiStatusList: any[] = [];
  selectedStatusId: any[] = [];

  caseCategoryIdList: any[] = [];
  selectedCaseCategoryItems: any[] = [];
  multiSelectCaseCategoryList: any[] = [];
  multiCaseCategoryList: any[] = [];

  caseSubCategoryIdList: any[] = [];
  selectedSubCaseCategoryItems: any[] = [];
  multiSelectSubCaseCategoryList: any[] = [];
  multiSubCaseCategoryList: any[] = [];

  multiSelectBillingModeIdList: any[] = [];
  multiBillingModeIdList: any[] = [];
  billingModeIdList: any[] = [];
  selectedBillingModeIdList: any[] = [];

  multiSelectCaseSoldByIdList: any[] = [];
  multiCaseSoldByIdList: any[] = [];
  selectedCaseSoldByIdList: any[] = [];

  multiSelectPartnerIdList: any[] = [];
  multiPartnerIdList: any[] = [];
  selectedPartnerId: any[] = [];

  multiSelectMainAttorneyIdList: any[] = [];
  multiMainAttorneyIdList: any[] = [];
  selectedMainAttorneyId: any[] = [];

  multiSelectAssignedTKIdList: any[] = [];
  multiAssignedTKIdList: any[] = [];
  selectedAssignedTKId: any[] = [];

  multiSelectLegalAssistantIdList: any[] = [];
  multiLegalAssistantIdList: any[] = [];
  selectedLegalAssistantId: any[] = [];

  multiSelectLawClerksIdList: any[] = [];
  multiLawClerksIdList: any[] = [];
  selectedLawClerkId: any[] = [];

  submitted = false;

  statusIdList: any[] = [];
  referralIdList: any[] = [];
  userIdList: any[] = [];
  matterIdList: any[] = [];
  matterAssignmentIdList: any[] = [];

  dropdownSettings = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
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
    assignedTimeKeeper: [],
  caseCategory: [],
  caseSubCategory: [],
  classId: [{ value: [2], disabled: true }],
  classIdFE: [{ value: '2 - IMMIGRATION', disabled: true }],
  clientId: [],
  corpClientId: [],
  documentType: [],
  fromDate: [],
  fromFiledDate: [],
  legalAssistant: [],
  matterNumber: [],
  originatingTimeKeeper: [],
  paralegal: [],
  partner: [],
  responsibleTimeKeeper: [],
  toDate: [],
  toFiledDate: [],
  toFiledDateFE:[],
  fromFiledDateFE:[],
  });
  
  dispList: any[] = [];
  selectedDisplay: any[] = [];

  constructor(
    private service: ReportServiceService,
    private Clientservice: ClientGeneralService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    private fb: FormBuilder,
    public toastr: ToastrService,
    private excel: ExcelService,
    private cas: CommonApiService,
    private auth: AuthService,
    public datepipe: DatePipe,
    private decimalPipe: DecimalPipe,
  ) { 
   
  }

  //RA: any = {};

  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    // this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllDropDown();
    this.currentDate = new Date();
    let yesterdayDate = new Date();
    let currentMonthStartDate = new Date();
    yesterdayDate.setDate(this.currentDate.getDate() - 1);
    this.startDate = this.datepipe.transform(yesterdayDate, 'dd');
   currentMonthStartDate.setDate(this.currentDate.getDate() - this.startDate);
  this.form.controls.fromFiledDate.patchValue(currentMonthStartDate);
  }

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort)
  sort: MatSort;
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
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);
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
console.log(this.matterAssignmentIdList);
console.log(this.multiSelectMainAttorneyIdList);
console.log(this.multiSelectAssignedTKIdList);
let obj: any = { searchpartnerReport: {} };
    if (this.form.controls.assignedTimeKeeper.value && this.form.controls.assignedTimeKeeper.value.length > 0) {
      let data: any[] = []
      this.form.controls.assignedTimeKeeper.value.forEach((a: any) => data.push(a));

    }
     if (this.form.controls.responsibleTimeKeeper.value && this.form.controls.responsibleTimeKeeper.value.length > 0) {
      let data: any[] = []
      this.form.controls.responsibleTimeKeeper.value.forEach((a: any) => data.push(a))
     
    }
    // if (this.selectedMatter && this.selectedMatter.length > 0) {
    //   this.selectedMatter.forEach(data => {
    //     this.form.controls.matterNumber.value.push(data.id);
    //   })
    // }

    // if (this.selectedCaseCategoryItems && this.selectedCaseCategoryItems.length > 0) {
    //   this.selectedCaseCategoryItems.forEach(data => {
    //     this.form.controls.caseCategoryId.value.push(data.id);
    //   })
    // }
    // if (this.selectedSubCaseCategoryItems && this.selectedSubCaseCategoryItems.length > 0) {
    //   this.selectedSubCaseCategoryItems.forEach(data => {
    //     this.form.controls.caseSubCategoryId.value.push(data.id);
    //   })
    // }

    // if (this.selectedStatusId && this.selectedStatusId.length > 0) {
    //   this.form.controls.statusId.patchValue(this.selectedStatusId[0].id);
    // }

    // if (this.selectedTimeKeeperId && this.selectedTimeKeeperId.length > 0) {
    //   this.form.controls.timeKeepers.patchValue(this.selectedTimeKeeperId[0].id);
    // }

    // this.form.controls.fromBillingDate.patchValue(this.cs.dateNewFormat(this.form.controls.fromBillingDateFE.value));
    // this.form.controls.toBillingDate.patchValue(this.cs.dateNewFormat(this.form.controls.toBillingDateFE.value));
    this.form.controls.fromFiledDate.patchValue(this.cs.dateNewFormat1(this.form.controls.fromFiledDateFE.value));
    this.form.controls.toFiledDate.patchValue(this.cs.dateNewFormat1(this.form.controls.toFiledDateFE.value));

    this.spin.show();
    this.sub.add(this.service.getAcademyReport(this.form.getRawValue()).subscribe(res => {

      res.forEach((x:any)=>{
       // x['totalAmout'] =  x.hardCost + x.softCost + x.feeBilled;
        x['positiveSoftCost'] = (x.softCost < 0 ? x.softCost * -1 : x.softCost);
        x['positiveHardCost'] = (x.hardCost < 0 ? x.hardCost * -1 : x.hardCost);
        x['positiveAdminCost'] = (x.adminCost < 0 ? x.adminCost * -1 : x.adminCost);
      })

      this.dataSource.data = res;
      this.dataSource.sort = this.sort;
      //console.log(this.sort)
      //console.log(this.paginator)
      this.dataSource.paginator = this.paginator;
      this.dataSource.data.forEach((data: any) => {
        data.statusId = this.statusIdList.find(y => y.key == data.statusId)?.value;
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
   this.form.controls.classId.patchValue([2]);
   this.form.controls.classIdFE.patchValue('2 - IMMIGRATION')
  }
  originatingTimeList:any[]=[];
  legalAssistantList: any[] = [];
  ParalegalList: any[] = [];
partnerIdList:any[]=[];
multipartnerList:any[]=[];
corporationList:any[]=[];
multiMatterList:any[]=[];
multiclientList:any[]=[];
clientList:any[]=[];
filtersclient:any[]=[];
multiselectclientList:any[]=[];
getAllDropDown() {
  this.spin.show;
  this.cas.getalldropdownlist([
    this.cas.dropdownlist.matter.dropdown.url,
    this.cas.dropdownlist.matter.matterNumber.url,
    this.cas.dropdownlist.setup.caseCategoryId.url,
    this.cas.dropdownlist.setup.caseSubcategoryId.url,
    this.cas.dropdownlist.setup.billingModeId.url,
    this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.setup.timeKeeperCode.url,
    this.cas.dropdownlist.matter.matterAssignment.url,
    this.cas.dropdownlist.setup.userId.url,
  ]).subscribe((results: any) => {
    this.sub.add(this.Clientservice.GetClientdetails().subscribe(res => {
      console.log(res);
      this.clientList = res;
      this.filtersclient = this.clientList.filter((element: {
        clientCategoryId: string;
      }) => {
        console.log(element)
        return element.clientCategoryId == '1';
      });
      this.filtersclient.forEach((x: {
        clientId: string;firstNameLastName: string;
      }) => this.multiclientList.push({
        value: x.clientId,
        label: x.clientId + '-' + x.firstNameLastName
      }))
      this.multiselectclientList = this.multiclientList;

    },
    err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
    console.log(this.corporationList);
  results[0].matterList.forEach((x: any) => {
  this.multiMatterList.push({ value: x.key, label: x.key + '-' + x.value });
     })
    this.matterIdList = results[1];
    let filterMatter: any[] = [];
    this.matterIdList.forEach(matter => {
      if (matter.classId == 2) {
        filterMatter.push(matter);
      }
    })
    this.matterIdList = filterMatter;
console.log(this.matterIdList)
    //caseCategory
    this.caseCategoryIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.caseCategoryId.key);
    this.caseCategoryIdList.forEach(category => {
      for (let i = 0; i < this.matterIdList.length; i++) {
        if (category.key == this.matterIdList[i].caseCategoryId) {
          this.multiCaseCategoryList.push({ value: category.key, label: category.value })
          break;
        }
      }
    })
    this.multiSelectCaseCategoryList = this.multiCaseCategoryList;

    //caseSubCategory
    this.caseSubCategoryIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.caseSubcategoryId.key);
    this.caseSubCategoryIdList.forEach(subCategory => {
      for (let i = 0; i < this.matterIdList.length; i++) {
        if (subCategory.key == this.matterIdList[i].caseSubCategoryId) {
          this.multiSubCaseCategoryList.push({ value: subCategory.key, label: subCategory.value })
          break;
        }
      }
    })
    this.multiSelectSubCaseCategoryList = this.multiSubCaseCategoryList;

    //billingMode
    this.billingModeIdList = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.billingModeId.key);
    this.billingModeIdList.forEach(billinMode => {
      for (let i = 0; i < this.matterIdList.length; i++) {
        if (billinMode.key == this.matterIdList[i].billingModeId) {
          this.multiBillingModeIdList.push({ value: billinMode.key, label: billinMode.value })
          break;
        }
      }
    })
   
    //status
    this.statusIdList = this.cas.foreachlist(results[5], this.cas.dropdownlist.setup.statusId.key);
    this.statusIdList.forEach(status => {
      for (let i = 0; i < this.matterIdList.length; i++) {
        if (status.key == this.matterIdList[i].statusId) {
          this.multiStatusList.push({ value: status.key, label: status.value })
          break;
        }
      }
    })
    this.multiSelectStatusList = this.multiStatusList;

    //referral
    this.referralIdList = this.cas.foreachlist(results[6], this.cas.dropdownlist.setup.timeKeeperCode.key);
    this.referralIdList.forEach(referral => {
     // for (let i = 0; i < this.matterIdList.length; i++) {
      //  if (referral.key == this.matterIdList[i].referenceField12) {
          this.multiReferralList.push({ value: referral.key, label: referral.value })
     //     break;
      //  }
    // }
    })
    console.log(this.multiReferralList)
    this.multiSelectReferralList = this.multiReferralList;

    this.matterAssignmentIdList = results[7];
    let filterMatterAssignment: any[] = [];
    this.matterAssignmentIdList.forEach(matterAssignment => {
      if (matterAssignment.classId == 2) {
        filterMatterAssignment.push(matterAssignment);
      }
    })
    this.matterAssignmentIdList = filterMatterAssignment;

    this.userIdList = this.cas.foreachlist(results[8], this.cas.dropdownlist.setup.userId.key);
  
    this.userIdList.forEach(user => {
      //case sold by
      for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
        if (user.key == this.matterAssignmentIdList[i].originatingTimeKeeper) {
          this.multiCaseSoldByIdList.push({ value: user.key, label: user.value })
          break;
        }
      }
      for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
        if (user.key == this.matterAssignmentIdList[i].originatingTimeKeeper) {
          console.log(user.key);
          this.originatingTimeList.push({ value: user.key, label: user.value });
          break;
        }
      }
      //partner
      for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
        if (user.key == this.matterAssignmentIdList[i].partner) {
          this.multiPartnerIdList.push({ value: user.key, label: user.value })
          break;
        }
      }
      //mainAttorney
      for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
        if (user.key == this.matterAssignmentIdList[i].responsibleTimeKeeper) {
          this.multiMainAttorneyIdList.push({ value: user.key, label: user.value })
          break;
        }
      }
      //assignedTK
      for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
        if (user.key == this.matterAssignmentIdList[i].assignedTimeKeeper) {
          this.multiAssignedTKIdList.push({ value: user.key, label: user.value })
          break;
        }
      }
      //legal assistant
      for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
        if (user.key == this.matterAssignmentIdList[i].legalAssistant) {
          this.multiLegalAssistantIdList.push({ value: user.key, label: user.value })
          break;
        }
      }
      //law clerks
      for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
        if (user.key == this.matterAssignmentIdList[i].referenceField1) {
          this.multiLawClerksIdList.push({ value: user.key, label: user.value })
          break;
        }
      }
        //paralegal
        for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
          if (user.key == this.matterAssignmentIdList[i].referenceField2) {
            this.ParalegalList.push({ value: user.key, label: user.value })
            break;
          }
        }  //legal assistant
        for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
          if (user.key == this.matterAssignmentIdList[i].legalAssistant) {
            this.legalAssistantList.push({ value: user.key, label: user.value })
            break;
          }
        }

    })
    this.multiSelectCaseSoldByIdList = this.multiCaseSoldByIdList;
    this.multiSelectPartnerIdList = this.multiPartnerIdList;
    this.multiSelectMainAttorneyIdList = this.multiMainAttorneyIdList;
    this.multiSelectAssignedTKIdList = this.multiAssignedTKIdList;
    this.multiSelectLegalAssistantIdList = this.multiLegalAssistantIdList;
    this.multiSelectLawClerksIdList = this.multiLawClerksIdList;
    this.spin.hide;
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
    // if (excel)
    var res: any = [];
    let sortedData = this.dataSource.data.sort((a, b) => (a.postingDate > b.postingDate) ? -1 : 1)
    this.dataSource.data.forEach(x => {
      res.push({
        'Client ID ': x.clientId,
        'Matter Number':  x.matterNumber,
        'Matter Description':  x.matterDescription,
        'Partner': x.partner,
        'Assigned Timekeeper': x.assignedTimekeeper,
        "Legal Assistant 1":x.paralegal,
        'Legal Assistant 2': x.legalAssistant,
        'Client Name': x.clientName,
        'Position': x.position,
        'Date of Hire':this.cs.excel_date(x.dateOfHire),
        "City":x.city,
        "State":x.state,
        "Visa":x.caseSubCategoryDescription,
        "Earliest Date to file":this.cs.excel_date(x.eligibilityDate),
        "I-94 Expiration Date/EAD":this.cs.excel_date(x.expirationDate),
        "Validity Starts":this.cs.excel_date(x.receiptDate),
        "Academy Comments":x.academyComments,
        "M&R Notes Job Description":x.mrjobDescriptionNote,
        "NIV M&R Notes":x.mrnivnote,
        "IV-M&R Notes":x.mrivnote,
        "End of 5th Year H1B": this.cs.excel_date(x.h1BEndDate),
        "I-140 Priority Date": this.cs.excel_date(x.priorityDate),
        "Audit Email":x.auditEmail,
        "Leadership":x.leadership,
        "HR Partners":x.hrPartners,
        "Employee Email":x.emailId,
       // ' Remaining Balance': (x.remainingBalance != null ? this.decimalPipe.transform(x.remainingBalance, "1.2-2") : '0.00'),
  
      
      });

    })
    

    this.excel.exportAsExcel(res, "Academy Report");
  }
  getBillableAmount() {
     let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.feeBilled != null ? element.feeBilled : 0);
    })
     return total;
   }
 totalbilledAmount() {
  let total = 0;
 this.dataSource.data.forEach(element => {
   total = total + (element.totalBilled != null ? element.totalBilled : 0);
 })
  return total;
  }
totalhrs() {
  let total = 0;
 this.dataSource.data.forEach(element => {
   total = total + (element.totalHours != null ? element.totalHours : 0);
 })
  return total;
  }
softcost() {
  let total = 0;
 this.dataSource.data.forEach(element => {
   total = total + (element.softCost != null ? element.softCost : 0);
 })
  return total;
  }
adminCost() {
  let total = 0;
 this.dataSource.data.forEach(element => {
   total = total + (element.adminCost != null ? element.adminCost : 0);
 })
  return total;
  }
Hardcost() {
  let total = 0;
 this.dataSource.data.forEach(element => {
   total = total + (element.hardCost != null ? element.hardCost : 0);
 })
  return total;
  }

paidAmount(){
  let total = 0;
  this.dataSource.data.forEach(element => {
    total = total + (element.paidAmount != null ? element.paidAmount : 0);
  })
   return total;
   }
miscdebt() {
  let total = 0;
 this.dataSource.data.forEach(element => {
   total = total + (element.miscDebits != null ? element.miscDebits : 0);
 })
 return total;
  }
getbalancetotal() {
  let total = 0;
 this.dataSource.data.forEach(element => {
   total = total + (element.balance != null ? element.balance : 0);
 })
 return total;
  }
getcosttotal() {
  let total = 0;
 this.dataSource.data.forEach(element => {
   total = total + (element.totalCost != null ? element.totalCost : 0);
 })
 return total;
  }



}


