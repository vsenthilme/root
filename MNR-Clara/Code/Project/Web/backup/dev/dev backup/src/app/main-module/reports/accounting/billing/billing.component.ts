import { SelectionModel } from '@angular/cdk/collections';
import { DatePipe, DecimalPipe } from '@angular/common';
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
  selector: 'app-billing',
  templateUrl: './billing.component.html',
  styleUrls: ['./billing.component.scss']
})
export class BillingComponent implements OnInit {
  screenid = 1185;
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
    "classIdDescription",
    "billingModeDescription",
   // "billingDate",
    "postingDate",
    "clientNumber",
    "clientName",
    "matterNumber",
    "matterName",
    "invoiceNumber",
    "feeBilled",
    "positiveHardCost",
    "positiveSoftCost",
    'miscDebits',
    "totalAmout",
    "totalHours",
    //"remainingBalance",
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
    classId: [[],[Validators.required]],
    clientId: [[],],
    caseCategoryId: [[],],
    caseSubCategoryId: [[],],
    statusId: [,],
    timeKeepers: [,],
    matterNumber: [[],],
   // fromBillingDate: [,],
   // toBillingDate: [,],
    fromPostingDate: [,],
    toPostingDate: [,],
   // fromBillingDateFE: [,],
   // toBillingDateFE: [,],
    fromPostingDateFE: [new Date("01/01/00 00:00:00"),[Validators.required]],
    toPostingDateFE: [this.cs.todayapi(),[Validators.required]],
  });

  constructor(
    private service: ReportServiceService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    private fb: FormBuilder,
    public toastr: ToastrService,
    private excel: ExcelService,
    private cas: CommonApiService,
    private auth: AuthService,
    public datepipe: DatePipe,
    private decimalPipe: DecimalPipe,
  ) { }

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
  this.form.controls.fromPostingDateFE.patchValue(currentMonthStartDate);
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
    //   this.selectedClassId.forEach(data => {
    //     this.form.controls.classId.value.push(data.id);
    //   })
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
    //   this.form.controls.timeKeepers.patchValue(this.selectedTimeKeeperId[0].id);
    // }

    // this.form.controls.fromBillingDate.patchValue(this.cs.dateNewFormat(this.form.controls.fromBillingDateFE.value));
    // this.form.controls.toBillingDate.patchValue(this.cs.dateNewFormat(this.form.controls.toBillingDateFE.value));
    this.form.controls.fromPostingDate.patchValue(this.cs.dateNewFormat(this.form.controls.fromPostingDateFE.value));
    this.form.controls.toPostingDate.patchValue(this.cs.dateNewFormat(this.form.controls.toPostingDateFE.value));

    this.spin.show();
    this.sub.add(this.service.getBillingReport(this.form.getRawValue()).subscribe(res => {

      res.forEach((x:any)=>{
        x['totalAmout'] =  x.hardCost + x.softCost + x.feeBilled;
        x['positiveSoftCost'] = (x.softCost < 0 ? x.softCost * -1 : x.softCost);
        x['positiveHardCost'] = (x.hardCost < 0 ? x.hardCost * -1 : x.hardCost);
      })

      this.dataSource.data = res;
      this.dataSource.sort = this.sort;
      console.log(this.sort)
      console.log(this.paginator)
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
      // this.cas.dropdownlist.setup.classId.url,
      // this.cas.dropdownlist.client.clientId.url,
      // this.cas.dropdownlist.matter.matterNumber.url,
      // this.cas.dropdownlist.setup.caseCategoryId.url,
      // this.cas.dropdownlist.setup.caseSubcategoryId.url,
      this.cas.dropdownlist.matter.dropdown.url,
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.timeKeeperCode.url,
    ]).subscribe((results: any) => {

   
      results[0].classList.forEach((x: any) => {
        this.multiSelectClassList.push({ value: x.key, label: x.key + '-' + x.value });
      }) 
      results[0].clientNameList.forEach((x: any) => {
        this.multiselectclientList.push({ value: x.key, label: x.key + '-' + x.value });
      }) 
      results[0].matterList.forEach((x: any) => {
        this.multiSelectMatterList.push({ value: x.key, label: x.key + '-' + x.value });
      }) 
      results[0].caseCategoryList.forEach((x: any) => {
        this.multiSelectCaseCategoryList.push({ value: x.key, label: x.key + '-' + x.value });
      }) 
      results[0].subCaseCategoryList.forEach((x: any) => {
        this.multiSelectSubCaseCategoryList.push({ value: x.key, label: x.key + '-' + x.value });
      }) 

      // results[0].forEach((classData: any) => {
      //   if(classData.classId == 1 || classData.classId == 2){
      //     this.multiClassList.push({ value: classData.classId, label: classData.classId + ' - ' + classData.classDescription })
      //   }
      // })
      // this.multiSelectClassList = this.multiClassList;


      // this.clientIdList = results[1];
      // this.clientIdList.forEach((client: any) => {
      //   this.multiclientList.push({ value: client.clientId, label: client.clientId + ' - ' + client.firstNameLastName });
      // })
      // this.multiselectclientList = this.multiclientList;

      // this.matterIdList = results[2];
      // this.matterIdList.forEach((matter: any) => {
      //   this.multiMatterList.push({ value: matter.matterNumber, label: matter.matterNumber + ' - ' + matter.matterDescription });
      // })
      // this.multiSelectMatterList = this.multiMatterList;


      // this.caseCategoryIdList = this.cas.foreachlist(results[3], this.cas.dropdownlist.setup.caseCategoryId.key);
      // this.caseCategoryIdList.forEach(category => {
      //   for (let i = 0; i < this.matterIdList.length; i++) {
      //     if (category.key == this.matterIdList[i].caseCategoryId) {
      //       this.multiCaseCategoryList.push({ value: category.key, label: category.value })
      //       break;
      //     }
      //   }
      // })
      // this.multiSelectCaseCategoryList = this.multiCaseCategoryList;


      // this.caseSubCategoryIdList = this.cas.foreachlist(results[4], this.cas.dropdownlist.setup.caseSubcategoryId.key);
      // this.caseSubCategoryIdList.forEach(subCategory => {
      //   for (let i = 0; i < this.matterIdList.length; i++) {
      //     if (subCategory.key == this.matterIdList[i].caseSubCategoryId) {
      //       this.multiSubCaseCategoryList.push({ value: subCategory.key, label: subCategory.value })
      //       break;
      //     }
      //   }
      // })
      // this.multiSelectSubCaseCategoryList = this.multiSubCaseCategoryList;

      //status
      this.statusIdList = this.cas.foreachlist(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [51, 52, 53, 54].includes(s.key));
      console.log(this.statusIdList);
      this.statusIdList.forEach(status => {
        // for (let i = 0; i < this.matterIdList.length; i++) {
        //   if (status.key == this.matterIdList[i].statusId) {
        //     this.multiStatusList.push({ value: status.key, label: status.value })
        //     break;
        //   }
        // }
        console.log(status);
        this.multiStatusList.push({ value: status.key, label: status.value
        
        })
      })
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
    let sortedData = this.dataSource.data.sort((a, b) => (a.postingDate > b.postingDate) ? -1 : 1)
    this.dataSource.data.forEach(x => {
      res.push({
        "Class ": x.classIdDescription,
        "Billing Mode": x.billingModeDescription,
       // 'Billing Date': this.cs.excel_date(x.billingDate),
        ' Posting Date ': this.cs.excel_date(x.postingDate),
        'Client ID ': x.clientNumber,
        'Client Name ': x.clientName,
        'Matter No':  x.matterNumber,
        'Matter Desc':  x.matterName,
        'Invoice Number': x.invoiceNumber,
        // 'Fee Billed': (x.feeBilled != null ? this.decimalPipe.transform(x.feeBilled, "1.2-2") : '0.00' ),
        // 'Hard Cost': (x.positiveHardCost != null ? this.decimalPipe.transform(x.positiveHardCost, "1.2-2") : '0.00'),
        // 'Soft Cost': (x.positiveSoftCost != null ? this.decimalPipe.transform(x.positiveSoftCost, "1.2-2") : '0.00'),
        // 'Misc Debits': (x.miscDebits != null ? this.decimalPipe.transform(x.miscDebits, "1.2-2") : '0.00'),
        // 'Total Billed': (x.totalAmout != null ? this.decimalPipe.transform(x.totalAmout as number, "1.2-2") : '0.00'),
        'Fee Billed': x.feeBilled,
        'Hard Cost': x.positiveHardCost,
        'Soft Cost': x.positiveSoftCost,
        'Misc Debits': x.miscDebits,
        'Total Billed': x.totalAmout,
        'Total Hours':  x.totalHours,
       // ' Remaining Balance': (x.remainingBalance != null ? this.decimalPipe.transform(x.remainingBalance, "1.2-2") : '0.00'),
  
      
      });

    })
      res.push({
        "Class ": '',
        "Billing Mode": '',
       // 'Billing Date': this.cs.excel_date(x.billingDate),
        ' Posting Date ': '',
        'Client ID ': '',
        'Client Name ': '',
        'Matter No':  '',
        'Matter Desc':  '',
        'Invoice Number': '',
        // 'Fee Billed': (x.feeBilled != null ? this.decimalPipe.transform(x.feeBilled, "1.2-2") : '0.00' ),
        // 'Hard Cost': (x.positiveHardCost != null ? this.decimalPipe.transform(x.positiveHardCost, "1.2-2") : '0.00'),
        // 'Soft Cost': (x.positiveSoftCost != null ? this.decimalPipe.transform(x.positiveSoftCost, "1.2-2") : '0.00'),
        // 'Misc Debits': (x.miscDebits != null ? this.decimalPipe.transform(x.miscDebits, "1.2-2") : '0.00'),
        // 'Total Billed': (x.totalAmout != null ? this.decimalPipe.transform(x.totalAmout as number, "1.2-2") : '0.00'),
        'Fee Billed': this.getBillableAmount(),
        'Hard Cost': this.Hardcost(),
        'Soft Cost': this.softcost(),
        'Misc Debits': this.miscdebt(),
        'Total Billed': this.totalbilledAmount() ,
        'Total Hours': this.totalhrs(),
       // ' Remaining Balance': (x.remainingBalance != null ? this.decimalPipe.transform(x.remainingBalance, "1.2-2") : '0.00'),
  
      
      });

    this.excel.exportAsExcel(res, "Billing Report");
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
   total = total + (element.totalAmout != null ? element.totalAmout : 0);
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
   total = total + (element.positiveSoftCost != null ? element.positiveSoftCost : 0);
 })
  return total;
  }
Hardcost() {
  let total = 0;
 this.dataSource.data.forEach(element => {
   total = total + (element.positiveHardCost != null ? element.positiveHardCost : 0);
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
}

