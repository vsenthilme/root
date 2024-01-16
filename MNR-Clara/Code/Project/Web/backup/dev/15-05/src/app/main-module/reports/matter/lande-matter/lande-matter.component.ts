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
import { PrebillService } from "src/app/main-module/accounts/prebill/prebill.service";
@Component({
  selector: 'app-lande-matter',
  templateUrl: './lande-matter.component.html',
  styleUrls: ['./lande-matter.component.scss']
})
export class LandeMatterComponent implements OnInit {
  screenid = 1163;
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


  displayedColumns: string[] = ['select', 'matterNumber', 'clientId', 'status', 'caseCategoryId', 'caseSubCategoryId', 'matterDescription', 'billModeText', 'matterOpenedDate', 'matterClosedDate', 'createdBy', 'updatedBy', 'referredBy', 'corporateName', 'originatingTimeKeeper', 'assignedTimeKeeper', 'responsibleTimeKeeper', 'notesText', 'typeOfMatter', 'reference', 'locationOfFile', 'defendants', 'causeNo', 'plaintiffs', 'advParty1Name', 'advParty1Email', 'advParty1CellPhone', 'advParty2Name', 'advParty2Email', 'advParty2CellPhone', 'judgeName', 'email', 'officeTelephone', 'agencyName', 'agentName', 'agentEmail', 'agentOfficeTelephone', 'scheduleDate', 'taskDate', 'tasksToDo','partner','legalAssistant','lawClerks'];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

  sub = new Subscription();

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
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };

  form = this.fb.group({
    classId: [{ value: 1, disabled: true }],
    classIdFE: [{ value: '', disabled: true }],
    referralId: [,],
    referralIdFE: [,],
    caseCategoryId: ['',],
    caseCategoryIdFE: [,],
    caseSubCategoryId: ['',],
    caseSubCategoryIdFE: [,],
    statusId: [, [Validators.required]],
    statusIdFE: [,],
    fromCaseOpenDate: [new Date("01/01/00 00:00:00"), [Validators.required]],
    toCaseOpenDate: [this.cs.todayapi(), [Validators.required]],
    fromCaseClosedDate: [,],
    toCaseClosedDate: [,],
    billingModeId: ['',],
    billingModeIdFE: [,],
    refferedBy: ['',],
    refferedByFE: [,],
    partner: ['',],
    partnerFE: [,],
    originatingTimeKeeper: ['',],
    originatingTimeKeeperFE: [,],
    responsibleTimeKeeper: ['',],
    responsibleTimeKeeperFE: [,],
    assignedTimeKeeper: ['',],
    assignedTimeKeeperFE: [,],
    legalAssistant: ['',],
    legalAssistantFE: [,],
    lawClerks: ['',],
    lawClerksFE: [,]
  });

  constructor(
    private service: PrebillService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    private fb: FormBuilder,
    private excel: ExcelService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    public datepipe: DatePipe,
    private auth: AuthService
  ) { }

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort)
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
    let obj: any = { searchMatterAssignmentLNEReport: {}, searchMatterLNEReport: {} };

    obj.searchMatterLNEReport.classId = this.form.controls.classId.value;
    obj.searchMatterLNEReport.fromCaseOpenDate = this.cs.day_callapiSearch(this.form.controls.fromCaseOpenDate.value);
    obj.searchMatterLNEReport.toCaseOpenDate = this.cs.day_callapiSearch(this.form.controls.toCaseOpenDate.value);
    obj.searchMatterLNEReport.fromCaseClosedDate = this.cs.day_callapiSearch(this.form.controls.fromCaseClosedDate.value);
    obj.searchMatterLNEReport.toCaseClosedDate = this.cs.day_callapiSearch(this.form.controls.toCaseClosedDate.value);

    // if (this.selectedBillingModeIdList && this.selectedBillingModeIdList.length > 0) {
    //   let data: any[] = []
    //   this.selectedBillingModeIdList.forEach((a: any) => data.push(a))
    //   obj.searchMatterLNEReport.billModeId = data;
    // }
    if (this.form.controls.billingModeId.value && this.form.controls.billingModeId.value.length > 0) {
      let data: any[] = []
      this.form.controls.billingModeId.value.forEach((a: any) => data.push(a))
      obj.searchMatterLNEReport.billingModeId = data;
    }
    if (this.form.controls.caseCategoryId.value && this.form.controls.caseCategoryId.value.length > 0) {
      let data: any[] = []
      this.form.controls.caseCategoryId.value.forEach((a: any) => data.push(a))
      obj.searchMatterLNEReport.caseCategoryId = data;
    }
    if (this.form.controls.caseSubCategoryId.value && this.form.controls.caseSubCategoryId.value.length > 0) {
      let data: any[] = []
      this.form.controls.caseSubCategoryId.value.forEach((a: any) => data.push(a))
      obj.searchMatterLNEReport.caseSubCategoryId = data;
    }
    if (this.form.controls.refferedBy.value && this.form.controls.refferedBy.value > 0) {
      let data: any[] = []
      this.form.controls.refferedBy.value.forEach((a: any) => data.push(a))
      obj.searchMatterLNEReport.refferedBy = data;
    }

    if (this.form.controls.statusId.value && this.form.controls.statusId.value.length > 0) {
      let data: any[] = []
      this.form.controls.statusId.value.forEach((a: any) => data.push(a))
      obj.searchMatterLNEReport.statusId = data;
    }


    //  obj.searchMatterAssignmentLNEReport.classId = this.form.controls.classId.value;
    if (this.form.controls.assignedTimeKeeper.value && this.form.controls.assignedTimeKeeper.value.length > 0) {
      console.log(2)
      let data: any[] = []
      this.form.controls.assignedTimeKeeper.value.forEach((a: any) => data.push(a))
      obj.searchMatterAssignmentLNEReport.assignedTimeKeeper = data;
    }
    if (this.form.controls.lawClerks.value && this.form.controls.lawClerks.value.length > 0) {
      let data: any[] = []
      this.form.controls.lawClerks.value.forEach((a: any) => data.push(a))
      obj.searchMatterAssignmentLNEReport.lawClerks = data;
    }
    if (this.form.controls.legalAssistant.value && this.form.controls.legalAssistant.value.length > 0) {
      let data: any[] = []
      this.form.controls.legalAssistant.value.forEach((a: any) => data.push(a))
      obj.searchMatterAssignmentLNEReport.legalAssistant = data;
    }

    if (this.form.controls.originatingTimeKeeper.value && this.form.controls.originatingTimeKeeper.value.length > 0) {
      let data: any[] = []
      this.form.controls.originatingTimeKeeper.value.forEach((a: any) => data.push(a))
      obj.searchMatterAssignmentLNEReport.originatingTimeKeeper = data;
    }

    if (this.form.controls.partner.value && this.form.controls.partner.value.length > 0) {
      let data: any[] = []
      this.form.controls.partner.value.forEach((a: any) => data.push(a))
      obj.searchMatterAssignmentLNEReport.partner = data;
    }

    if (this.form.controls.responsibleTimeKeeper.value && this.form.controls.responsibleTimeKeeper.value.length > 0) {
      let data: any[] = []
      this.form.controls.responsibleTimeKeeper.value.forEach((a: any) => data.push(a))
      obj.searchMatterAssignmentLNEReport.responsibleTimeKeeper = data;
    }


    this.spin.show();
    this.sub.add(this.service.getMatterLnEReport(obj).subscribe(res => {
      this.spin.hide();
      this.dataSource.data = res;
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.dataSource.data.forEach((data: any) => {
        data.statusId = this.statusIdList.find(y => y.key == data.statusId)?.value;
      })
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
    this.multiClassList.forEach((classData: any) => {
      if (classData.value == 1) {
        this.selectedClassId.push(classData);
        this.form.controls.classId.patchValue(classData.value);
        this.form.controls.classIdFE.patchValue(classData.label);
      }
    })
  }

  getAllDropDown() {
    this.spin.show;
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.matter.matterNumber.url,
      this.cas.dropdownlist.setup.caseCategoryId.url,
      this.cas.dropdownlist.setup.caseSubcategoryId.url,
      this.cas.dropdownlist.setup.billingModeId.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.timeKeeperCode.url,
      this.cas.dropdownlist.matter.matterAssignment.url,
      this.cas.dropdownlist.setup.userId.url,
    ]).subscribe((results: any) => {
      results[0].forEach((classData: any) => {
        this.multiClassList.push({ value: classData.classId, label: classData.classId + ' - ' + classData.classDescription })
      })
      this.multiClassList.forEach((classData: any) => {
        if (classData.value == 1) {
          this.selectedClassId.push(classData);
          this.form.controls.classId.patchValue(classData.value);
          this.form.controls.classIdFE.patchValue(classData.label);
        }
      })

      this.matterIdList = results[1];
      let filterMatter: any[] = [];
      this.matterIdList.forEach(matter => {
        if (matter.classId == 1) {
          filterMatter.push(matter);
        }
      })
      this.matterIdList = filterMatter;

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
      this.multiSelectBillingModeIdList = this.multiBillingModeIdList;

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
      this.multiSelectReferralList = this.multiReferralList;

      this.matterAssignmentIdList = results[7];
      let filterMatterAssignment: any[] = [];
      this.matterAssignmentIdList.forEach(matterAssignment => {
        if (matterAssignment.classId == 1) {
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
    this.dataSource.data.forEach(x => {
      res.push({
        'Matter File': x.matterNumber,
        "Client ID  ": x.clientId,
        "Active/Inactive": x.status,
        'Case Category ': x.caseCategoryId,
        'Case Sub Category': x.caseSubCategoryId,
        'Matter Descp': x.matterDescription,
        'Billing Mode': x.billModeText,
        'Matter Opened Date': this.cs.dateapi(x.matterOpenedDate),
        'Matter Closed Date': this.cs.dateapi(x.matterClosedDate),
        ' Created By ': x.createdBy,
        'Updated by': x.updatedBy,
        'Referred by': x.referredBy,
        'Corp. Name/Client Name': x.corporateName,
        'Case Sold By': x.originatingTimeKeeper,
        'Assigned Assistant': x.assignedTimeKeeper,
        'Atty. Handling case': x.responsibleTimeKeeper,
        'Attorneys Notes': x.notesText,
        'Matter Type': x.typeOfMatter,
        'Reference': x.reference,
        'Location of the File': x.locationOfFile,
        'Defendant': x.defendants,
        'Cause Number': x.causeNo,
        'Opp. Party- Name': x.advParty1Name,
        'Plaintiff': x.plaintiffs,
        'Opp.Party - Email': x.advParty1Email,
        'Opp. Party - Phone': x.advParty1CellPhone,
        'Cause Opp. Counsel- Name': x.advParty2Name,
        'Opp. Counsel - Email': x.advParty2Email,
        'Opp. Counsel - Phone': x.officeTelephone,
        'Judge Name': x.judgeName,
        'Court Email': x.email,
        'Court Phone': x.officeTelephone,
        'Agency Name': x.agencyName,
        'Agent Name': x.agentName,
        'Agent Email': x.agentEmail,
        'Agent Phone': x.agentOfficeTelephone,
        'Schedule Date': this.cs.dateapi(x.scheduleDate),
        'Task Date': this.cs.dateapi(x.taskDate),
        'Task To Do': x.tasksToDo,
        'Partner': x.partner,
        'Legal Assistant': x.legalAssistant,
        'Law Clerk': x.lawClerks,
      });

    })
    this.excel.exportAsExcel(res, "L&E Matter");
  }

}

