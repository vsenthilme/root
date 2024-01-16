
  import { SelectionModel } from "@angular/cdk/collections";
  import { Component, OnInit, ViewChild } from "@angular/core";
  import { FormBuilder, FormControl, Validators } from "@angular/forms";
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
import { ReportServiceService } from "../../report-service.service";
  
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
    selector: 'app-client-matter',
    templateUrl: './client-matter.component.html',
    styleUrls: ['./client-matter.component.scss']
  })
  export class ClientMatterComponent implements OnInit {
  
  
    screenid = 1183;
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
    multiClientList: any[] = [];
  
    multiSelectDocumentType: any[] = [];
    multiDocumentType: any[] = [];
  thisDocumentType: any[] = [];
  
    selectedClassId: any[] = [];

    multiReferralList: any[] = [];
    selectedReferralId: any[] = [];
    submitted = false;
  
    multiMatterList: any[] = [];
    multiCaseSubList: any[] = [];
  
    form = this.fb.group({
     assignedTimeKeeper:  [[], ],
     billingFormatCode:  [[], ],
     billingMode:  [[], ],
     caseCategory:  [[], ],
     caseSubCategory:  [[], ],
     classId:   [, Validators.required],
     clientId:  [[], ],
     corporateName:  [[], ],
     legalAssistant:  [[], ],
     matterNumber:  [[], ],
     originatingTimeKeeper:  [[], ],
     paralegal:  [[], ],
     partner:  [[], ],
     petitionerName:  [[], ],
     billingFrequency:  [[], ],
     responsibleTimeKeeper:  [[], ],
     statusId: [[],  Validators.required],
     timeKeeperStatus:  [[], ],
     timeKeeper:  [[], ],
    });
  
    displayedColumns: string[] = [
      'select',
      'classId',
      'clientId',
      'firstLastName',
      'matterNumber', 
      'matterText',
      'caseOpenedDate', 
      'caseClosedDate', 
      'billModeText', 
      'caseCategory', 
      'caseSubCategory',
      'paraLegal',
      'petitionerName',
      'corporateName',
      'legalAssist',
      'assignedTk',
      'originatingTk',
      'responsibleTk',
      'partner',
    ];
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
      private service: ReportServiceService,
      private cs: CommonService,
      private spin: NgxSpinnerService,
      private excel: ExcelService,
      private fb: FormBuilder,
      public toastr: ToastrService,
      private cas: CommonApiService,
      private auth: AuthService) { }
    RA: any = {};
      
    ngOnInit(): void {
  
      this.RA = this.auth.getRoleAccess(this.screenid);
      this.getAllDropDown();
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
   
      this.spin.show();
      this.sub.add(this.service.getMatterListing(this.form.getRawValue()).subscribe(res => {
        this.dataSource.data = res;
        this.spin.hide()
        this.dataSource.paginator = this.paginator;
         this.dataSource.sort = this.sort;
        this.spin.hide();
       // this.dataSource.data.forEach((data: any) => {
       //   data.potentialClientId = this.multiMatterList.find(y => y.value == data.potentialClientId)?.label;
       // })
        this.table = true;
        this.search = false;
        this.back = true;
      },
        err => {
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
  
    multiselecttimekeeperList: any[] = [];
    multiSelectCaseCatList: any[] = [];
    multiSelectCaseSubCatList: any[] = [];
    multitimekeeperList: any[] = [];
    timekeeperCodelist: any[] = [];

    timekeeperStatusList: any[] = [];
    multiselecttimekeeperStatusList: any[] = [];
    multitimekeeperStatusList: any[] = [];
    multiSelectstatusIdList: any[] = [];

    billingFreqList: any[] = [];
    multiSelectbillingFreqList: any[] = [];

    timeKeepersList: any[] = [];
    userIdList: any[] = [];
    legalAssistantList: any[] = [];
    ParalegalList: any[] = [];
    assignedTimeList: any[] = [];
    originatingTimeList: any[] = [];
    respTimeList: any[] = [];
    partnerList: any[] = [];
    matterAssignmentIdList: any[] = [];
    petitionerList: any[] = [];
    corporationList: any[] = [];
    getAllDropDown() {
      this.cas.getalldropdownlist([
        this.cas.dropdownlist.matter.dropdown.url,
        this.cas.dropdownlist.setup.timeKeeperCode.url,
        this.cas.dropdownlist.setup.billingFrequencyId.url,
        this.cas.dropdownlist.matter.matterAssignment.url,
        this.cas.dropdownlist.setup.userId.url,
      ]).subscribe((results: any) => {
        results[0].classList.forEach((x: any) => {
          this.multiSelectClassList.push({ value: x.key, label: x.key + '-' + x.value });
        });
        results[0].caseCategoryList.forEach((x: any) => {
          this.multiSelectCaseCatList.push({ value: x.key, label: x.key + '-' + x.value });
        });
        results[0].clientNameList.forEach((x: any) => {
          this.petitionerList.push({ value: x.key, label: x.key + '-' + x.value });
        });
        results[0].clientNameList.forEach((x: any) => {
          this.corporationList.push({ value: x.value, label: x.key + '-' + x.value });
        });
        results[0].subCaseCategoryList.forEach((x: any) => {
          this.multiSelectCaseSubCatList.push({ value: x.key, label: x.key + '-' + x.value });
        });
         results[0].statusIdList.forEach((x: any) => {
          this.multiSelectstatusIdList.push({ value: x.key, label: x.key + '-' + x.value });
        });
        this.timekeeperCodelist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.timeKeeperCode.key);
        this.timekeeperCodelist.forEach((x: { key: string; value: string; }) => this.multitimekeeperList.push({ value: x.key, label: x.key + '-' + x.value }))
        this.multiselecttimekeeperList = this.multitimekeeperList;

        this.timekeeperStatusList = results[1];
        this.timekeeperStatusList.forEach((x: { timekeeperStatus: string; }) => this.multitimekeeperStatusList.push({ value: x.timekeeperStatus, label: x.timekeeperStatus }))
        this.multiselecttimekeeperStatusList = this.cas.removeDuplicatesFromArrayNew(this.multitimekeeperStatusList);

        this.billingFreqList = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.setup.billingFrequencyId.key);
        console.log(this.billingFreqList)
        this.billingFreqList.forEach((x: { key: string; value: string; }) => this.multiSelectbillingFreqList.push({ value: x.key, label: x.value }));
        this.multiSelectbillingFreqList = this.multiSelectbillingFreqList;

        this.matterAssignmentIdList = results[3];
        this.userIdList = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.userId.key);
        this.userIdList.forEach(user => {
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
            this.partnerList.push({ value: user.key, label: user.value })
            break;
          }
        }
        //mainAttorney
        for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
          if (user.key == this.matterAssignmentIdList[i].responsibleTimeKeeper) {
            this.respTimeList.push({ value: user.key, label: user.value })
            break;
          }
        }
        //paralegal
        for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
          if (user.key == this.matterAssignmentIdList[i].referenceField2) {
            this.ParalegalList.push({ value: user.key, label: user.value })
            break;
          }
        }
        //assignedTK
        for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
          if (user.key == this.matterAssignmentIdList[i].assignedTimeKeeper) {
            this.assignedTimeList.push({ value: user.key, label: user.value })
            break;
          }
        }
        //legal assistant
        for (let i = 0; i < this.matterAssignmentIdList.length; i++) {
          if (user.key == this.matterAssignmentIdList[i].legalAssistant) {
            this.legalAssistantList.push({ value: user.key, label: user.value })
            break;
          }
        }
      })

      }, (err) => {
        this.toastr.error(err, "");
      });
    }
  
  
    public errorHandling = (control: string, error: string = "required") => {
      return this.form.controls[control].hasError(error) && this.submitted;
    }
    
  email = new FormControl('', [Validators.required, Validators.email]);
    getErrorMessage() {
      if (this.email.hasError('required')) {
        return ' Field should not be blank';
      }
      return this.email.hasError('email') ? 'Not a valid email' : '';
    }
    reset() {
      this.form.reset();
      this.form.controls.classId.patchValue([]);
      this.form.controls.billingFrequency.patchValue([]);
      this.form.controls.caseCategory.patchValue([]);
      this.form.controls.caseSubCategory.patchValue([]);
      this.form.controls.billingFrequency.patchValue([]);
      this.form.controls.clientId.patchValue([]);
      this.form.controls.statusId.patchValue([]);
      this.form.controls.timeKeeper.patchValue([]);
      this.form.controls.timeKeeperStatus.patchValue([]);
    }
  
    downloadexcel() {
      // if (excel)
      var res: any = [];
      this.dataSource.data.forEach(x => {
        res.push({
          'Class ID': x.classId == 1 ? 'L&E' : 'Immigration',
          'Client ID': x.clientId,
          'Client Name ': x.firstLastName,
          'Matter No': x.matterNumber,
          'Matter Description': x.matterText,
          'Matter Opened Date' : this.cs.dateapiutc0(x.caseOpenedDate),
          'Matter Closed Date' : this.cs.dateapiutc0(x.caseClosedDate),
          'Billing Mode' : x.billModeText,
          'Case Category' : x.caseCategory,
          'Case Sub Category' : x.caseSubCategory,
          'Petitioner' : x.petitionerName,
          'Corporation' : x.corporateName,
          'Legal Assistant' : x.legalAssist,
          'Paralegal' : x.paraLegal,
          'Assigned Time Keeper' : x.assignedTk,
          'Attorney' : x.responsibleTk,
          'Originating' : x.originatingTk,
          'partner' : x.Partner,
  
        });
  
      })
      this.excel.exportAsExcel(res, "Matter Listing");
    }
  
  }
  
  
  
  

