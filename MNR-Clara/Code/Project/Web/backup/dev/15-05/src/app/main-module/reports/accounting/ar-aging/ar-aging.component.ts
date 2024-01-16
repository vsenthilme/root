import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { ExcelService } from 'src/app/common-service/excel.service';
import { AuthService } from 'src/app/core/core';
import { ReportServiceService } from '../../report-service.service';

@Component({
  selector: 'app-ar-aging',
  templateUrl: './ar-aging.component.html',
  styleUrls: ['./ar-aging.component.scss']
})
export class ArAgingComponent implements OnInit {
  screenid = 1165;
  public icon = 'expand_more';
  isShowDiv = false;
  table = true;
  fullscreen = false;
  search = true;
  back = false;
  showFloatingButtons: any;
  toggle = true;
  RA: any = {};

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true })
  sort: MatSort;
  
  displayedColumns: string[] = [
    "select",
    'clientNumber',
    "clientName",
    'matterNumber',
    "matterName",
    "classId",
    "caseCategoryId",
    "caseSubCategoryId",
    "matterOpenDate",
    "totalAmountDue",
    "unpaidCurrent",
    "unpaid30To60Days",
    "unpaid61To90Days",
    "unpaid91DaysTo120Days",
    "unpaidOver120",
    "lastPaymentDate",
    "feeReceived",
    "billingNotes",
    "accountingPhoneNumber",
  ];

  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

  sub = new Subscription();

  multiClassList: any[] = [];
  selectedClassId: any[] = [];
  multiSelectClassList: any[] = [];

  selectedClient: any[] = [];
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];

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

  multiSelectTimeKeeperIdList: any[] = [];
  multiTimeKeeperIdList: any[] = [];
  selectedTimeKeeperId: any[] = [];

  selectedMatter: any[] = [];
  multiSelectMatterList: any[] = [];
  multiMatterList: any[] = [];

  submitted = false;

  statusIdList: any[] = [];
  matterIdList: any[] = [];
  timeKeeperIdList: any[] = [];
  clientIdList: any[] = [];

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
    classId: [,[Validators.required]],
    clientId: [[],],
    caseCategoryId: [[],],
    caseSubCategoryId: [[],],
    statusId: [,],
    timeKeepers: [[],],
    matterNumber: [[],],
  });

  constructor(
    private service: ReportServiceService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    private excel: ExcelService,
    private fb: FormBuilder,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private auth: AuthService
  ) { }

  //RA: any = {};
    
  


  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    // this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllDropDown();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
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

    // if (this.selectedClassId && this.selectedClassId.length > 0) {
    //   this.form.controls.classId.patchValue(this.selectedClassId[0].id);
    // }
    // if (this.selectedClient && this.selectedClient.length > 0) {
    //   this.selectedClient.forEach(data => {
    //     this.form.controls.clientId.value.push(data.id);
    //   })
    // }
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
    //   this.selectedTimeKeeperId.forEach(data => {
    //     this.form.controls.timeKeepers.value.push(data.id);
    //   })
    // }

    this.spin.show();
    this.sub.add(this.service.getARAgingReport(this.form.getRawValue()).subscribe(res => {
      this.dataSource.data = res;
    this.dataSource.sort = this.sort;
    console.log(this.sort)
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
  }

  getAllDropDown() {
    this.spin.show;
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.matter.dropdown.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.timeKeeperCode.url,
    ]).subscribe((results: any) => {

      //class
      results[0].classList.forEach((classData: any) => {
        if (classData.key == 1 || classData.key == 2) {
          this.multiClassList.push({ value: classData.key, label: classData.key + ' - ' + classData.value })
        }
      })
      this.multiSelectClassList = this.multiClassList;

      //client
      results[0].clientNameList.forEach((client: any) => {
        this.multiclientList.push({ value: client.key, label: client.key + ' - ' + client.value });
      })
      this.multiselectclientList = this.multiclientList;

      //matter
      results[0].matterList.forEach((matter: any) => {
        this.multiMatterList.push({ value: matter.key, label: matter.key + ' - ' + matter.value });
      })
      this.multiSelectMatterList = this.multiMatterList;

      //caseCategory
      results[0].caseCategoryList.forEach((category: any) => {
        this.multiCaseCategoryList.push({ value: category.key, label: category.key + ' - ' + category.value });
      })

      this.multiSelectCaseCategoryList = this.multiCaseCategoryList;

      //caseSubCategory
      results[0].subCaseCategoryList.forEach((subCategory: any) => {
        this.multiSubCaseCategoryList.push({ value: subCategory.key, label: subCategory.key + ' - ' + subCategory.value });
      })
      this.multiSelectSubCaseCategoryList = this.multiSubCaseCategoryList;

      //status
      this.statusIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [26, 27, 28, 29, 30, 36].includes(s.key));
      console.log(this.statusIdList)
       this.statusIdList.forEach(status => {
      //   for (let i = 0; i < this.matterIdList.length; i++) {
      //     if (status.key == this.matterIdList[i].statusId) {
            this.multiStatusList.push({ value: status.key, label: status.value })
      //       break;
      //     }
      //   }
       })
      // results[0].statusIdList.forEach((status: any) => {
      //   this.multiStatusList.push({ value: status.key, label: status.key + ' - ' + status.value });
      // })
      this.multiSelectStatusList = this.multiStatusList;

      //timekeeper
      this.timeKeeperIdList = this.cas.foreachlist(results[2], this.cas.dropdownlist.setup.timeKeeperCode.key);
      this.timeKeeperIdList.forEach(timekeeper => {
        this.multiTimeKeeperIdList.push({ value: timekeeper.key, label: timekeeper.value })
      })
      this.multiSelectTimeKeeperIdList = this.multiTimeKeeperIdList;

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
        'Client ID': x.clientNumber,
        'Matter No': x.matterNumber,
        'Accounting PhoneNumber': x.accountingPhoneNumber,
        'Class': (x.classId == 1 ? 'L&E' : 'Immigration'),
        "Case Category ID": x.caseCategoryId,
        'Case Sub Category ID ': x.caseSubCategoryId,
        ' Matter Open Date ': this.cs.dateapi(x.matterOpenDate),
        'Client Name': x.clientName,
        'Matter Name':  x.matterName,
        ' Total Amount Due': x.totalAmountDue != null && x.totalAmountDue > 0 ? x.totalAmountDue : 0,
        'Unpaid Current':  x.unpaidCurrent != null && x.unpaidCurrent > 0 ? x.unpaidCurrent : 0,
        'Unpaid 30 To 60 Days': x.unpaid30To60Days != null && x.unpaid30To60Days > 0 ? x.unpaid30To60Days : 0,
        'Unpaid 60 Days To 90 Days' : x.unpaid61To90Days != null && x.unpaid61To90Days > 0 ? x.unpaid61To90Days : 0,
        'Unpaid 91 Days To 120 Days': x.unpaid91DaysTo120Days != null && x.unpaid91DaysTo120Days > 0 ? x.unpaid91DaysTo120Days : 0,
        'Unpaid Over 120':  x.unpaidOver120 != null && x.unpaidOver120 > 0 ? x.unpaidOver120 : 0,
        'Fee Received': x.feeReceived,
        'Last Payment Date':  this.cs.dateapi(x.lastPaymentDate),
        "Billing Notes": x.billingNotes,
      });
    
    })
    res.push({
      'Client ID': '',
      'Matter No': '',
      'Accounting PhoneNumber': '',
      'Class': '',
      "Case Category ID":'',
      'Case Sub Category ID ': '',
      ' Matter Open Date ': '',
      'Client Name': '',
      'Matter Name':  '',
      ' Total Amount Due': this.totalamtdue(),
      'Unpaid Current':  this.unpaidcurdue(),
      'Unpaid 30 To 60 Days': this.unthirtyninetydue(),
      'Unpaid 60 Days To 90 Days' : this.unsixtyninetydue(),
      'Unpaid 91 Days To 120 Days': this.unninetyonetwentydue() ,
      'Unpaid Over 120': this.over120due() ,
      'Fee Received': this.feerecevied() ,
      'Last Payment Date':  '',
      "Billing Notes": '',
    });
    this.excel.exportAsExcel(res, "AR Aging Report");
  }
  totalamtdue() {
    let total = 0;
   this.dataSource.data.forEach(element => {
     total = total + (element.totalAmountDue != null && element.totalAmountDue > 0 ? element.totalAmountDue : 0);
   })
   return total;
    }
  unpaidcurdue() {
    let total = 0;
   this.dataSource.data.forEach(element => {
     total = total + (element.unpaidCurrent != null && element.unpaidCurrent > 0 ? element.unpaidCurrent : 0);
   })
   return total;
    }
  unthirtyninetydue() {
    let total = 0;
   this.dataSource.data.forEach(element => {
     total = total + (element.unpaid30To60Days != null && element.unpaid30To60Days > 0 ? element.unpaid30To60Days : 0);
   })
   return total;
    }
  unsixtyninetydue() {
    let total = 0;
   this.dataSource.data.forEach(element => {
     total = total + (element.unpaid61To90Days != null && element.unpaid61To90Days > 0 ? element.unpaid61To90Days : 0);
   })
   return total;
    }
  unninetyonetwentydue() {
    let total = 0;
   this.dataSource.data.forEach(element => {
     total = total + (element.unpaid91DaysTo120Days != null && element.unpaid91DaysTo120Days > 0 ? element.unpaid91DaysTo120Days : 0);
   })
   return total;
    }
  over120due() {
    let total = 0;
   this.dataSource.data.forEach(element => {
     total = total + (element.unpaidOver120 != null && element.unpaidOver120 > 0  ? element.unpaidOver120 : 0);
   })
   return total;
    }
  feerecevied() {
    let total = 0;
   this.dataSource.data.forEach(element => {
     total = total + (element.feeReceived != null ? element.feeReceived : 0);
   })
   return total;
    }
}

