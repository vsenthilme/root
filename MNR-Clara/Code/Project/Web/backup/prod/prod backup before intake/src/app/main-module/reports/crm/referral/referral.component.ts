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
  selector: 'app-referral',
  templateUrl: './referral.component.html',
  styleUrls: ['./referral.component.scss']
})
export class ReferralComponent implements OnInit {
  screenid = 1160;
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
  submitted = false;

  multiPotentialList: any[] = [];

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
    classId:  [, [Validators.required]],
   // classIdFE: [, [Validators.required]],
    fromCreatedOn: [new Date("01/01/00 00:00:00"), [Validators.required]],
    toCreatedOn: [this.cs.todayapi(), [Validators.required]],
    referralId: [,],
   // referralIdFE: [[],],
  });

  displayedColumns: string[] = [
    'select',
    'classId',
    'inquiryFirstNameLastName',
    'emailId',
    'referralId',
      'potentialClientId', 
   'clientId', 
   'clientFirstNameLastName',];
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
    private excel: ExcelService,
    private fb: FormBuilder,
    public toastr: ToastrService,
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
    if (this.selectedClassId && this.selectedClassId.length > 0) {
      this.form.patchValue({ classId: this.selectedClassId[0].id });
    }
    if (this.selectedReferralId && this.selectedReferralId.length > 0) {
      let data: any[] = []
      this.selectedReferralId.forEach((a: any) => data.push(a.id))
      this.form.patchValue({ referralId: data });
    }
    this.form.controls.fromCreatedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.fromCreatedOn.value));
    this.form.controls.toCreatedOn.patchValue(this.cs.day_callapiSearch(this.form.controls.toCreatedOn.value));
    this.spin.show();
    this.sub.add(this.service.getReferralReport(this.form.getRawValue()).subscribe(res => {
      this.dataSource.data = res;
      this.spin.hide()
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
      this.dataSource.data.forEach((data: any) => {
     //   data.potentialClientId = this.multiPotentialList.find(y => y.value == data.potentialClientId)?.label;
      })
      this.spin.hide();
      this.table = true;
      this.search = false;
      //this.fullscreen = true;
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
  getAllDropDown() {
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.classId.url,
      this.cas.dropdownlist.setup.referralId.url,
      this.cas.dropdownlist.crm.potentialClientId.url,
    ]).subscribe((results: any) => {
      results[0].forEach((classData: any) => {
        this.multiClassList.push({ value: classData.classId, label: classData.classId + ' - ' + classData.classDescription })
        this.multiClassList = this.multiClassList.filter(classData => classData.value == 1 || classData.value == 2)
      })
      this.multiSelectClassList = this.multiClassList;

      results[1].forEach((referralData: any) => {
        this.multiReferralList.push({ value: referralData.referralId, label: referralData.referralId + "-" + referralData.referralDescription, classId: referralData.classId })
      })
      this.multiSelectReferralList = this.multiReferralList;

      results[2].forEach((potntialClient: any) => {
        this.multiPotentialList.push({ value: potntialClient.potentialClientId, label: potntialClient.potentialClientId + ' - ' + potntialClient.firstNameLastName })
      })

    }, (err) => {
      this.toastr.error(err, "");
    });
  }

  onItemSelect(item: any, type?: any) {
    // if(type == 'PREBILL') {
    //   this.filterBasedOnPreBill();
    // }
  }
  OnItemDeSelect(item: any, type?: any) {
    console.log(item)
    // if(type == 'PREBILL') {
    //   let data:any = [];
    //   this.selectedItems2.forEach(remove2 => {
    //     console.log(remove2)
    //     if(remove2.preBillNumber != item.id){
    //       data.push(remove2);
    //     }
    //   });
    //   this.selectedItems2 = data ;

    //   this.multiselectprebillbatchList = [];

    //   this.selectedItems1.forEach(element => {
    //     this.multiprebillbatchList.forEach((data :any)=> {
    //       if(data['preBillNumber'] == (element.id)){
    //        this.multiselectprebillbatchList.push(data)
    //       }
    //     })
    //   });
    // }
  }
  onSelectAll(items: any, type?: any) {
    if (type == 'PREBILL') {
      this.filterBasedOnPreBill();
    }
  }
  onDeSelectAll(items: any, type?: any) {
    // if(type == 'PREBILL') {
    //   this.selectedItems2 = [];
    //   this.multiselectprebillbatchList = this.multiprebillbatchList;

    // }
  }
  filterBasedOnPreBill() {
    // this.multiselectprebillbatchList = [];
    //   this.selectedItems1.forEach(element => {
    //     this.multiprebillbatchList.forEach((data :any)=> {
    //       if(data['preBillNumber'] == (element.id)){
    //        this.multiselectprebillbatchList.push(data)
    //       }
    //     })
    //   });
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
  reset() {
    this.form.reset();
  }

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        
        'Class': x.classId,
        'Name': x.inquiryFirstNameLastName,
        'Email': x.emailId,
        'Referral ID': (x.referralDesc != null ? x.referralDesc : ''),
        'Potential Client ID': x.potentialClientId,
        'Client ID': x.clientId,
        "Client Name ": x.clientFirstNameLastName

      });

    })
    this.excel.exportAsExcel(res, "Referral");
  }

}

