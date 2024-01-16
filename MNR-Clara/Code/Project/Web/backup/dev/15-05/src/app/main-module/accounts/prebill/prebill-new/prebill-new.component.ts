import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import { Location } from '@angular/common';
import { Subscription } from 'rxjs';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { CommonService } from 'src/app/common-service/common-service.service';
import { AuthService } from 'src/app/core/core';
import { GeneralMatterService } from 'src/app/main-module/matters/case-management/General/general-matter.service';
import { PrebillService } from '../prebill.service';
import { AssignPartnerComponent } from './assign-partner/assign-partner.component';
import { ExpenseDetailsComponent } from './expense-details/expense-details.component';
import { TimetocketDetailsComponent } from './timetocket-details/timetocket-details.component';

interface dropdownelement1 {
  id: string;
  itemName: string;
}
@Component({
  selector: 'app-prebill-new',
  templateUrl: './prebill-new.component.html',
  styleUrls: ['./prebill-new.component.scss']
})

export class PrebillNewComponent implements OnInit {

  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  isrun = true;
  isassign = false;
  checkboxselect = true;
  matterList: any[] = [];
  classList: any;
  assigntimelist: any;
  billmodelist: any;
  casesublist: any;
  resptimeList: any;
  casecatlist: any;
  classList1: any;
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



  disabled = false;
  step = 0;

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  panelOpenState = false;

  sub = new Subscription();

  displayedColumns: string[] = ['select', 'preBillDate', 'clientId', 'matterNumber', 'timeticket', 'expense', 'totalAmount',];
  dataSource = new MatTableDataSource<any>([]);
  selection = new SelectionModel<any>(true, []);
  form = this.fb.group({
    assignedTimeKeeper: [],
    assignedTimeKeeperFE: [],
    billingFormatCode: [],
    billingFrequency: [],
    billingMode: [],
    billingModeFE: [],
    caseCategory: [],
    caseCategoryFE: [],
    caseSubCategory: [],
    caseSubCategoryFE: [],
    clientId: [],
    classId: [, [Validators.required]],
    clientIdFE: [],
    classIdFE: [],
    feesCutoffDate: [, ],
    feesCutoffDateFE: [, [Validators.required]],
    matterNumber: [],
    matterNumberFE: [,],
    originatingTimeKeeper: [],
    paymentCutoffDateFE: [, [Validators.required]],
    paymentCutoffDate: [, ],
    preBillDateFE: [, [Validators.required]],
    preBillDate: [, ],
    responsibleTimeKeeper: [],
    responsibleTimeKeeperFE: [],
    startDate: [, ],
    startDateFE: [, [Validators.required]],
  },
  );

  isByIndividual: boolean;
  filtersection: boolean;
  constructor(private fb: FormBuilder,
    private auth: AuthService,
    private service: PrebillService, public dialog: MatDialog,
    public toastr: ToastrService, private serviceMatter: GeneralMatterService,
    private cas: CommonApiService,
    private location: Location,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,

    private cs: CommonService,) { }

  ngOnInit(): void {
    this.isByIndividual = true;
    this.filtersection = true;
    this.dropdownlist();
  }

  @ViewChild(MatPaginator, { static: true })
  paginator: MatPaginator; // Pagination


  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }
  onChange(event: any): void {
    this.form.reset();

  }
  back() {
    this.location.back();
  }
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }


  showFiller = false;
  animal: string | undefined;
  id: string | undefined;

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.preBillNumber + 1}`;
  }
  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  submitted = false;

  email = new FormControl('', [Validators.required, Validators.email]);
  public errorHandling = (control: string, error: string = "required") => {

    if (control.includes('.')) {
      const controls = this.form.get(control);
      return controls ? controls.hasError(error) : false && this.submitted;

    }
    return this.form.controls[control].hasError(error) && this.submitted;
  }
  getErrorMessage() {
    if (this.email.hasError('required')) {
      return ' Field should not be blank';
    }
    return this.email.hasError('email') ? 'Not a valid email' : '';
  }
  // getErrorMessage1() {
  //   if (this.form.controls.preBillDate.valid || this.form.controls.startDate.valid) {
  //     return 'atleast one';
  //   }
  //   else{
  //     return 'atleast okay';
  //   }
  // }



  selectedItems: dropdownelement1[] = [];
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];

  selectedItems2: any[] = [];
  selectedMatterItems: any[] = [];
  multiselectmatterList: any[] = [];
  multimatterList: any[] = [];

  selectedItems3: any[] = [];
  selectedClientItems: any[] = [];
  multiselectclassList: any[] = [];
  multiclassList: any[] = [];

  selectedItems4: dropdownelement1[] = [];
  multiselectbillmodeList: any[] = [];
  multibillmodeList: any[] = [];

  selectedItems5: any[] = [];
  multiselectcasecatList: any[] = [];
  multicasecatList: any[] = [];

  selectedItems6: any[] = [];
  multiselectresptimeList: any[] = [];
  multiresptimeList: any[] = [];

  selectedItems7: any[] = [];
  multiselectassigntimeList: any[] = [];
  multiassigntimeList: any[] = [];

  selectedItems8: any[] = [];
  multiselectcasesubList: any[] = [];
  multicasesubList: any[] = [];

  selectedItems9: dropdownelement1[] = [];
  multiselectclassList1: any[] = [];
  multiclassList1: any[] = [];


  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 1
  };

  dropdownSettings1 = {
    singleSelection: true,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2
  };
  clientList: any = [];

  matterfilterList: any = [];
  dropdownlist() {
    this.sub.add(this.serviceMatter.getAllSearchDropDown().subscribe((data: any) => {
      if (data) {
        data.matterList.forEach((x: any) => this.matterList.push({ value: x.key, label: x.key + '-' + x.value }));
        this.matterList.forEach((x: any) => this.multimatterList.push({ value: x.value, label: x.label }));
        this.multiselectmatterList = this.multimatterList

        data.clientNameList.forEach((x: any) => this.clientList.push({ value: x.key, label: x.key + '-' + x.value }));
        this.clientList.forEach((x: any) => this.multiclientList.push({ value: x.value, label: x.label }));
        this.multiselectclientList = this.multiclientList
      }
    }, (err) => {
      this.toastr.error(err, "");
    }));

    this.sub.add(this.service.getclassdetails().subscribe(res => {
      this.classList = res;
      this.classList.forEach((x: { classId: string; classDescription: string; }) => this.multiclassList.push({ value: x.classId, label: x.classId + "-" + x.classDescription }));
      this.multiclassList = this.multiclassList.filter(classData => classData.value == 1 || classData.value == 2)
      this.multiselectclassList = this.multiclassList;
    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));

    this.sub.add(this.service.getclassdetails().subscribe(res => {
      this.classList1 = res;
      this.classList1.forEach((x: { classId: string; classDescription: string; }) => this.multiclassList1.push({ value: x.classId, label: x.classId + "-" + x.classDescription }));
      this.multiclassList1 = this.multiclassList1.filter(classData => classData.value == 1 || classData.value == 2)
      this.multiselectclassList1 = this.multiclassList1;
    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));

    this.sub.add(this.service.getassigntimedetails().subscribe(res => {
      this.assigntimelist = res;
      this.assigntimelist.forEach((x: { timekeeperCode: string; timekeeperName: string; classId: string }) => this.multiassigntimeList.push({ value: x.timekeeperCode, label: x.timekeeperCode + "-" + x.timekeeperName, classId: x.classId }))
      this.multiselectassigntimeList = this.multiassigntimeList;
    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));

    this.sub.add(this.service.getresptimedetails().subscribe(res => {
      this.resptimeList = res;
      this.resptimeList.forEach((x: any) => this.multiresptimeList.push({ value: x.timekeeperCode, label: x.timekeeperCode + "-" + x.timekeeperName, classId: x.classId }))
      this.multiselectresptimeList = this.multiresptimeList;
    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));

    this.sub.add(this.service.getcasesubdetails().subscribe(res => {
      this.casesublist = res;
      this.casesublist.forEach((x: any) => this.multicasesubList.push({ value: x.caseSubcategoryId, label: x.caseSubcategoryId + "-" + x.subCategory, classId: x.classId, caseCategoryId: x.caseCategoryId }))
      this.multiselectcasesubList = this.multicasesubList;
    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));


    this.sub.add(this.service.getcasecatdetails().subscribe(res => {
      this.casecatlist = res;
      this.casecatlist.forEach((x: any) => this.multicasecatList.push({ value: x.caseCategoryId, label: x.caseCategoryId + "-" + x.caseCategory, classId: x.classId }))
      this.multiselectcasecatList = this.multicasecatList;
    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));


    this.sub.add(this.service.getbillmodedetails().subscribe(res => {
      this.billmodelist = res;
      this.billmodelist.forEach((x: { billingModeId: string; billingModeDescription: string; }) => this.multibillmodeList.push({ value: x.billingModeId, label: x.billingModeId + "-" + x.billingModeDescription }))
      this.multiselectbillmodeList = this.multibillmodeList;
    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
  }


  search = false;
  backicon = false;




  run() {
    this.selection.clear();
    this.checkboxselect = false;
    this.submitted = true; this.form.updateValueAndValidity;
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



    // if (this.isByIndividual === true) {
    //   if (this.form.controls.clientId.value)
    //     this.form.controls.clientId.patchValue([this.form.controls.clientId.value]);
    //   if (this.form.controls.classId.value)
    //     this.form.controls.classId.patchValue([this.form.controls.classId.value]);
    //   if (this.form.controls.matterNumber.value)
    //     this.form.controls.matterNumber.patchValue([this.form.controls.matterNumber.value]);
    // }
    if (this.isByIndividual === true) {


      // if (this.form.controls.clientId.value)
      //   this.form.patchValue({ clientId: this.selectedItems3[0].id });
      // if (this.form.controls.classId.value)
      //   this.form.patchValue({ classId: this.selectedItems[0].id });
      // if (this.form.controls.matterNumber.value)
      //   this.form.patchValue({ matterNumber: this.selectedItems2[0].id });
      if (this.form.controls.clientIdFE.value && this.form.controls.clientIdFE.value.length > 0)
        this.form.patchValue({ clientId: this.form.controls.clientIdFE.value });
      if (this.form.controls.matterNumberFE.value && this.form.controls.matterNumberFE.value.length > 0) { this.form.patchValue({ matterNumber: this.form.controls.matterNumberFE.value }); }
      else { this.form.patchValue({ matterNumber: null }); }

      if (this.form.controls.matterNumber.value == null) {
        this.toastr.error(
          "Please fill Matter No to continue",
          "Notification", {
          timeOut: 2000,
          progressBar: false,
        }
        );

        this.cs.notifyOther(true);
        return;
      }

    }
    if (this.form.controls.matterNumberFE.value && this.form.controls.matterNumberFE.value.length > 0) {
      let multiplematterNo: any[] = []
      this.form.controls.matterNumberFE.value.forEach((a: any) => multiplematterNo.push(a))
      this.form.patchValue({ matterNumber: multiplematterNo });
    }

    if (this.form.controls.clientIdFE.value && this.form.controls.clientIdFE.value.length > 0) {
      let multipleclientId: any[] = []
      this.form.controls.clientIdFE.value.forEach((a: any) => multipleclientId.push(a))
      this.form.patchValue({ clientId: multipleclientId });
    }

    // let newfeescutoff: Date = new Date(this.form.controls.feesCutoffDate.value);
    // newfeescutoff.setDate(newfeescutoff.getDate() + Number(1));
    // let feesCutoffDate = new Date(newfeescutoff);
    // this.form.controls.feesCutoffDate.patchValue(feesCutoffDate);

    // let newpaymentcutoff: Date = new Date(this.form.controls.paymentCutoffDate.value);
    // newpaymentcutoff.setDate(newpaymentcutoff.getDate() + Number(1));
    // let paymentCutoffDate = new Date(newpaymentcutoff);
    // this.form.controls.paymentCutoffDate.patchValue(paymentCutoffDate);

    this.form.controls.feesCutoffDate.patchValue(this.cs.dateNewFormat(this.form.controls.feesCutoffDateFE.value));
    this.form.controls.paymentCutoffDate.patchValue(this.cs.dateNewFormat(this.form.controls.paymentCutoffDateFE.value));
    this.form.controls.preBillDate.patchValue(this.cs.dateNewFormat(this.form.controls.preBillDateFE.value));
    this.form.controls.startDate.patchValue(this.cs.dateNewFormat(this.form.controls.startDateFE.value));

    this.spin.show();
    this.sub.add(this.service.excute(this.form.getRawValue(), this.isByIndividual).subscribe(res => {
      this.spin.hide();
      if (this.isByIndividual === true) {
        if (this.form.controls.clientId.value)
          this.form.controls.clientId.patchValue(this.form.controls.clientId.value[0]);
        if (this.form.controls.matterNumber.value)
          this.form.controls.matterNumber.patchValue(this.form.controls.matterNumber.value[0]);
      }
      // this.form.disable();
      this.isassign = true;
      this.isrun = false;
      this.filtersection = false;
      this.backicon = true;
      res.forEach((x: any) => x.clientId_name = this.clientList.find(y => y.value == x.clientId)?.label)
      this.dataSource = new MatTableDataSource<any>(res);
      this.selection.select(...this.dataSource.data);
      // this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
      // this.selection = new SelectionModel<any>(true, []);
    }, err => {
      if (this.isByIndividual === true) {
        if (this.form.controls.clientId.value)
          this.form.controls.clientId.patchValue(this.form.controls.clientId.value[0]);
        if (this.form.controls.matterNumber.value)
          this.form.controls.matterNumber.patchValue(this.form.controls.matterNumber.value[0]);
      }

      this.cs.commonerror(err);
      this.spin.hide();

    }));
  }

  backsearch() {
    this.filtersection = true;
    this.backicon = false;
    this.isassign = false;
    this.isrun = true;
  }

  assign() {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(AssignPartnerComponent, {
      disableClose: true,
      width: '100%',
      maxWidth: '55%',
      position: { top: '6.5%' },
    });

    dialogRef.afterClosed().subscribe((x: any) => {
      if (x) {
        this.assignPartnersave(x);
      }
    });
  }
  timeticket(element: any[]) {

    const dialogRef = this.dialog.open(TimetocketDetailsComponent, {
      disableClose: true,
      width: '100%',
      maxWidth: '75%',
      position: { top: '6.5%' },
      data: element,
    });

    // dialogRef.afterClosed().subscribe();
  }
  expense(element: any[]): void {

    const dialogRef = this.dialog.open(ExpenseDetailsComponent, {
      disableClose: true,
      width: '100%',
      maxWidth: '75%',
      position: { top: '6.5%' },
      data: element,
    });

    dialogRef.afterClosed().subscribe(result => {


    });
  }
  assignPartnersave(partner: any) {
    let i = 1;
    let list: any[] = [];
    this.selection.selected.forEach((x: any) => {
      list.push({
        "classId": x.classId,
        "clientId": x.clientId,
        "createdBy": this.auth.userID,
        "deletionIndicator": 0,
        "feesCostCutoffDate": this.cs.day_callapiSearch(this.form.controls.feesCutoffDate.value),
        "languageId": x.languageId,
        "matterNumber": x.matterNo,
        "numberOfTimeTickets": x.timeTicketCount,
        "partnerAssigned": partner,
        "paymentCutoffDate": this.cs.day_callapiSearch(this.form.controls.paymentCutoffDate.value),
        "preBillDate": this.cs.day_callapiSearch(this.form.controls.preBillDate.value),
        "startDateForPreBill": this.cs.day_callapiSearch(this.form.controls.startDate.value),

        "totalAmount": x.totalAmount,
        "updatedBy": this.auth.userID,
      });
    });
    this.spin.hide();
    this.sub.add(this.service.Create(list).subscribe(res => {
      this.spin.hide();
      this.toastr.success(res[0].preBillBatchNumber + " saved successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });

      //  if (i == this.selection.selected.length)
      this.router.navigate(['/main/accounts/prebilllist']);
      //   i++;
    }, err => {
      this.spin.hide();
      this.cs.commonerror(err);

    }));


  }


  openfilter() {
    this.search = !this.search;
  }


  onItemSelect(item: any, type?: any) {
    if (type == 'CLASS') {
      this.filterBasedOnClassId();
    } else if (type == 'CLASS_INDIVIDUAL') {
      this.form.controls.clientId.patchValue('');
      this.multiselectclientList = [];
      this.multiclientList.forEach((data: any) => {
        if (data['classId'] == (this.form.controls.classId.value[0])) {
          this.multiselectclientList.push(data);
        }
      })
      this.multiselectmatterList = [];
      this.form.controls.matterNumberFE.patchValue('');
      this.multimatterList.forEach((data: any) => {
        if (data['classId'] == (this.form.controls.classId.value[0])) {
          this.multiselectmatterList.push(data);
        }
      })
    } else if (type == 'CLIENT') {
      this.form.controls.matterNumberFE.patchValue('');
      this.multiselectmatterList = [];
      this.multimatterList.forEach((data: any) => {
        this.form.controls.clientIdFE.value.forEach(client => {
          if (data['clientId'] == (client)) {
            this.multiselectmatterList.push(data);
          }
        })
      })
    }
  }
  OnItemDeSelect(item: any, type?: any) {
    if (type == 'CLASS') {
      this.multiselectcasecatList = [];
      let data: any = [];
      this.selectedItems5.forEach(remove5 => {
        if (remove5.classId != item.id) {
          console.log(remove5)
          data.push(remove5);
        }
      });
      this.selectedItems5 = data;

      data = [];
      this.selectedItems8.forEach(remove8 => {
        if (remove8.classId != item.id) {
          data.push(remove8);
        }
      });
      this.selectedItems8 = data;

      data = [];
      this.selectedItems6.forEach(remove6 => {
        if (remove6.classId != item.id) {
          data.push(remove6);
        }
      });
      this.selectedItems6 = data;

      data = [];
      this.selectedItems7.forEach(remove7 => {
        if (remove7.classId != item.id) {
          data.push(remove7);
        }
      });
      this.selectedItems7 = data;
      data = [];
      this.selectedItems3.forEach(remove3 => {
        if (remove3.classId != item.id) {
          data.push(remove3);
        }
      });
      this.selectedItems3 = data;

      data = [];
      this.selectedItems2.forEach(remove2 => {
        if (remove2.classId != item.id) {
          data.push(remove2);
        }
      });
      this.selectedItems2 = data;

      this.multiselectcasesubList = [];
      this.multiselectresptimeList = [];
      this.multiselectassigntimeList = [];
      this.multiselectclientList = [];
      this.multiselectmatterList = [];

      this.selectedItems9.forEach(element => {
        this.multicasecatList.forEach((data: any) => {
          if (data['classId'] == (element.id)) {
            this.multiselectcasecatList.push(data)
          }
        })
        this.multicasesubList.forEach((data: any) => {
          if (data['classId'] == (element.id)) {
            this.multiselectcasesubList.push(data)
          }
        })
        this.multiresptimeList.forEach((data: any) => {
          if (data['classId'] == (element.id)) {
            this.multiselectresptimeList.push(data)
          }
        })
        this.multiassigntimeList.forEach((data: any) => {
          if (data['classId'] == (element.id)) {
            this.multiselectassigntimeList.push(data)
          }
        })
        this.multiclientList.forEach((data: any) => {
          if (data['classId'] == (element.id)) {
            this.multiselectclientList.push(data)
          }
        })
        this.multimatterList.forEach((data: any) => {
          if (data['classId'] == (element.id)) {
            this.multiselectmatterList.push(data)
          }
        })
      });
    }
  }
  onSelectAll(items: any, type?: any) {
    if (type == 'CLASS') {
      this.filterBasedOnClassId();
    }
  }
  onDeSelectAll(items: any, type?: any) {
    if (type == 'CLASS') {
      this.selectedItems2 = [];
      this.selectedItems3 = [];
      this.selectedItems5 = [];
      this.selectedItems6 = [];
      this.selectedItems7 = [];
      this.selectedItems8 = [];

      this.multiselectcasesubList = this.multicasesubList;
      this.multiselectresptimeList = this.multiresptimeList;
      this.multiselectassigntimeList = this.multiassigntimeList;
      this.multiselectclientList = this.multiclientList;
      this.multiselectmatterList = this.multimatterList;
    }
  }

  filterBasedOnClassId() {
    this.multiselectcasecatList = [];
    // this.form.controls.caseCategory.patchValue('');
    this.multiselectcasesubList = [];
    // this.form.controls.caseSubCategory.patchValue('');
    this.multiselectresptimeList = [];
    // this.form.controls.responsibleTimeKeeper.patchValue('');
    this.multiselectassigntimeList = [];
    // this.form.controls.assignedTimeKeeper.patchValue('');
    this.multiselectclientList = [];
    // this.form.controls.clientId.patchValue('');
    this.multiselectmatterList = [];
    // this.form.controls.matterNumber.patchValue('');

    this.form.controls.classId.value.forEach(element => {
      this.multicasecatList.forEach((data: any) => {
        if (data['classId'] == (element)) {
          this.multiselectcasecatList.push(data)
        }
      })
      this.multicasesubList.forEach((data: any) => {
        if (data['classId'] == (element)) {
          this.multiselectcasesubList.push(data)
        }
      })
      this.multiresptimeList.forEach((data: any) => {
        if (data['classId'] == (element)) {
          this.multiselectresptimeList.push(data)
        }
      })
      this.multiassigntimeList.forEach((data: any) => {
        if (data['classId'] == (element)) {
          this.multiselectassigntimeList.push(data)
        }
      })
      this.multiclientList.forEach((data: any) => {
        if (data['classId'] == (element)) {
          this.multiselectclientList.push(data)
        }
      })
      this.multimatterList.forEach((data: any) => {
        if (data['classId'] == (element)) {
          this.multiselectmatterList.push(data)
        }
      })
    });
  }

  getTotalAmount() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.totalAmount != null ? element.totalAmount : 0);
    })
    return (Math.round(total * 100) / 100);
  }

}
