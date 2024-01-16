import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { DatePipe, DecimalPipe, Location } from "@angular/common";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { GeneralMatterService } from "src/app/main-module/matters/case-management/General/general-matter.service";
import { PrebillService } from "../../prebill/prebill.service";
import { QuotationService } from "../../quotation/quotations-list/quotation.service";
import { BillComponent } from "../bill.component";
import { BillService } from "../bill.service";
import { Client2Component } from "./client2/client2.component";
import { TotalamountComponent } from "./totalamount/totalamount.component";
import { SavedInvoiceComponent } from "./saved-invoice/saved-invoice.component";

import pdfMake from "pdfmake/build/pdfmake";
import pdfFonts from "./../../../../../assets/font/vfs_fonts.js";
import { defaultStyle } from "./../../../../config/customStyles";
import { fonts } from "./../../../../config/pdfFonts";
import { diamondlogo } from "../../../../../assets/font/dimond_logo.js";
import { resizedLogo } from "../../../../../assets/font/resized_logo.js";
// PDFMAKE fonts
pdfMake.vfs = pdfFonts.pdfMake.vfs;
pdfMake.fonts = fonts;
interface SelectItem {
  id: string;
  itemName: string;
}


// export interface PeriodicElement {
//   documenttype: string;
//   approvaldate: string;
//   expirationdate: string;
//   eligibility: string;
//   remainder: string;
//   remainderdate: string;
//   matter: string;
//   prebillamount: string;
//   status: string;
//   nooftickets: string;
//   billpert: string;
//   client2: string;
//   client22: string;
//   totalCost: string;
//   clientAmount1: string;
// }
// const ELEMENT_DATA: PeriodicElement[] = [
//   { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', client2: 'Test1', clientAmount1: 'amount', totalCost: 'Test1', client22: 'Test1', eligibility: 'Test', remainder: 'Test', billpert: 'Test', remainderdate: 'Test', matter: 'Test', nooftickets: 'Test', prebillamount: 'Test', status: 'Test', },
//   { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', client2: 'Test1', clientAmount1: 'amount', totalCost: 'Test1', client22: 'Test1', eligibility: 'Test', remainder: 'Test', billpert: 'Test', remainderdate: 'Test', matter: 'Test', nooftickets: 'Test', prebillamount: 'Test', status: 'Test', },
//   { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', client2: 'Test1', clientAmount1: 'amount', totalCost: 'Test1', client22: 'Test1', eligibility: 'Test', remainder: 'Test', billpert: 'Test', remainderdate: 'Test', matter: 'Test', nooftickets: 'Test', prebillamount: 'Test', status: 'Test', },
//   { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', client2: 'Test1', clientAmount1: 'amount', totalCost: 'Test1', client22: 'Test1', eligibility: 'Test', remainder: 'Test', billpert: 'Test', remainderdate: 'Test', matter: 'Test', nooftickets: 'Test', prebillamount: 'Test', status: 'Test', },
//   { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', client2: 'Test1', clientAmount1: 'amount', totalCost: 'Test1', client22: 'Test1', eligibility: 'Test', remainder: 'Test', billpert: 'Test', remainderdate: 'Test', matter: 'Test', nooftickets: 'Test', prebillamount: 'Test', status: 'Test', },
//   { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', client2: 'Test1', clientAmount1: 'amount', totalCost: 'Test1', client22: 'Test1', eligibility: 'Test', remainder: 'Test', billpert: 'Test', remainderdate: 'Test', matter: 'Test', nooftickets: 'Test', prebillamount: 'Test', status: 'Test', },
//   { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', client2: 'Test1', clientAmount1: 'amount', totalCost: 'Test1', client22: 'Test1', eligibility: 'Test', remainder: 'Test', billpert: 'Test', remainderdate: 'Test', matter: 'Test', nooftickets: 'Test', prebillamount: 'Test', status: 'Test', },
//   { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', client2: 'Test1', clientAmount1: 'amount', totalCost: 'Test1', client22: 'Test1', eligibility: 'Test', remainder: 'Test', billpert: 'Test', remainderdate: 'Test', matter: 'Test', nooftickets: 'Test', prebillamount: 'Test', status: 'Test', },
//   { documenttype: "test", approvaldate: 'test', expirationdate: 'Test1', client2: 'Test1', clientAmount1: 'amount', totalCost: 'Test1', client22: 'Test1', eligibility: 'Test', remainder: 'Test', billpert: 'Test', remainderdate: 'Test', matter: 'Test', nooftickets: 'Test', prebillamount: 'Test', status: 'Test', },


// ];
@Component({
  selector: 'app-newbill-list',
  templateUrl: './newbill-list.component.html',
  styleUrls: ['./newbill-list.component.scss']
})


export class NewbillListComponent implements OnInit {
  filter = true;
  //screenid: 1128 | undefined;
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;

  statusearchList: any;
  clientList: any;
  clientname: any[] = [];
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

  constructor(private fb: FormBuilder,
    private auth: AuthService,
    public dialog: MatDialog,
    private service: BillService,
    private matterservice: GeneralMatterService,
    private serviceperbill: PrebillService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    private spin: NgxSpinnerService,
    private route: ActivatedRoute, private router: Router,
    private location: Location,
    private cs: CommonService,
    private decimalPipe: DecimalPipe,
    private datePipe: DatePipe,

    private pdf: BillComponent,
  ) { }
  isRun = false;
  ELEMENT_DATA: any[] = [];
  ngOnInit(): void {
    this.dropdownlist();
    this.dropdownSettings2.disabled = true;
  }
  //saved for later --mugilan
  //displayedColumns: string[] = ['select', 'clientId', 'matterNumber', 'partnerAssigned', 'totalAmount','totalCost', 'nooftickets', 'prebillamount', 'billpert', 'clientAmount1','client2', 'client22'];

  displayedColumns: string[] = ['select', 'clientId', 'preBillNumber', 'matterNumber', 'partnerAssigned', 'totalAmount', 'prebillamount', 'billpert', 'client22'];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.documenttype + 1}`;
  }
  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  reset() {
    // this.searhform.reset();
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
  back() {
    //  this.router.navigate(['/main/accounts/billlist']);
    this.location.back();
  }
  totalamount(element: any): void {
    // element.addInvoiceLine_t[0].billableAmount, element.addInvoiceLine_t[1].billableAmount
    const dialogRef = this.dialog.open(TotalamountComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '60%',
      position: { top: '6.5%' },
      data: element
    });

    dialogRef.afterClosed().subscribe(result => {

    });
  }
  client2(element: any): void {
    console.log(element)
    const dialogRef = this.dialog.open(Client2Component, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: element
    });

    dialogRef.afterClosed().subscribe(result => {
      let total = 0;
      element.addInvoiceLine_t.forEach((line: any) => {
        total = total + line.cAmount + line.fAmount;
      })
      this.form.controls.formAmt.patchValue(result)
      element.invoiceAmount = total
    });
  }
  form = this.fb.group({
    // assignedTimeKeeper: [],
    // billingFormatCode: [],
    // billingFrequency: [],
    // billingMode: [],
    // caseCategory: [],
    // caseSubCategory: [],
    // clientId: [],
    // feesCutoffDate: [, [Validators.required]],
    matterNumber: [,],
    preBillBatchNumber: [,],
    preBillNumber: [,],
    startPreBillDate: [,],
    formAmt: [,],
    endPreBillDate: [,],
    referenceText: [,],
    //  invoiceDate: [, [Validators.required]],
    invoiceDate: [this.cs.todayCallApi()],

    statusId: [
      [29]
    ]

  });
  sub = new Subscription();

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
  list: any[] = [];
  showfilter() {
    this.filter = !this.filter
  }
  Run() {

    this.submitted = true;
    // this.form.patchValue({matterNumber: this.selectedItems[0].id});
    // this.form.patchValue({preBillBatchNumber: this.selectedItems2[0].id});
    // this.form.patchValue({preBillNumber: this.selectedItems1[0].id});
    this.form.updateValueAndValidity;
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
    // if (this.selectedItems[0].id != null){
    //   this.form.patchValue({matterNumber: this.selectedItems[0].id});
    // }
    // if (this.selectedItems2[0].id != null){
    //   this.form.patchValue({preBillBatchNumber: this.selectedItems2[0].id});
    // }
    // if (this.selectedItems1[0].id != null){
    //   this.form.patchValue({preBillNumber: this.selectedItems1[0].id});
    // }

    // if (this.selectedItems1 && this.selectedItems1.length > 0)
    // this.form.patchValue({ preBillNumber: [this.selectedItems1[0].id] });
    // if (this.selectedItems2 && this.selectedItems2.length > 0)
    // this.form.patchValue({ preBillBatchNumber: [this.selectedItems2[0].id] });
    // if (this.selectedItems && this.selectedItems.length > 0)
    // this.form.patchValue({ matterNumber: [this.selectedItems[0].id] });


    // if (this.selectedItems && this.selectedItems.length > 0) {
    //   let multimatterListList: any[] = []
    //   this.selectedItems.forEach((a: any) => multimatterListList.push(a.id))
    //   this.form.patchValue({ matterNumber: multimatterListList });
    // }else{
    //   this.form.controls.matterNumber.patchValue(null);
    // }

    // if (this.selectedItems2 && this.selectedItems2.length > 0) {
    //   let multiprebillbatchList: any[] = []
    //   this.selectedItems2.forEach((a: any) => multiprebillbatchList.push(a.id))
    //   this.form.patchValue({ preBillBatchNumber: multiprebillbatchList });
    // }else{
    //   this.form.controls.preBillBatchNumber.patchValue(null);
    // }

    // if (this.selectedItems1 && this.selectedItems1.length > 0) {
    //   let multiprebillList: any[] = []
    //   this.selectedItems1.forEach((a: any) => multiprebillList.push(a.id))
    //   this.form.patchValue({ preBillNumber: multiprebillList });
    // }else{
    //   this.form.controls.preBillNumber.patchValue(null);
    // }

    this.form.controls.endPreBillDate.patchValue(this.cs.dateNewFormat(this.form.controls.endPreBillDate.value));
    this.form.controls.startPreBillDate.patchValue(this.cs.dateNewFormat(this.form.controls.startPreBillDate.value));


    if (this.form.controls.preBillNumber.value == null && this.form.controls.preBillBatchNumber.value == null && this.form.controls.matterNumber.value == null && this.form.controls.startPreBillDate.value == null) {
      this.toastr.error("Prebill Batch No / Prebill No / Matter No / Prebill date any one should be filled", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }

    this.filter = false;

    this.spin.show();

    this.sub.add(this.service.invoiceExecute(this.form.getRawValue()).subscribe(res => {
      this.form.disable();
      this.spin.hide();
      res.forEach((x: any) => {
        x.invoiceDate = this.cs.day_callapiSearch(this.form.controls.invoiceDate.value);
        x.postingDate = this.cs.day_callapiSearch(this.form.controls.invoiceDate.value);
        x.referenceText = this.form.controls.referenceText.value;
        //   x.clientId = this.clientname.find(y => y.key == x.clientId)?.value;
        x['clientname'] = this.clientname.find(y => y.key == x.clientId)?.value;
        x.addInvoiceLine_t = [];
        x.addInvoiceLine_t.push({
          "fAmount": x.totalAmount["1"],
          "cAmount": x.totalAmount["2"],

          "clientId": [{ id: this.clientname.find(y => y.key == x.clientId)?.key, itemName: this.clientname.find(y => y.key == x.clientId)?.value }]

          // "glAccount": "string",

          // "itemNumber": 1,

        });


        x.invoiceAmount = x.totalAmount["1"] + x.totalAmount["2"]
      })

      this.isRun = true;
      this.list.push(res);
      this.step = 1;
      console.log(res)
      this.dataSource = new MatTableDataSource<any>(res);
      // this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
      this.selection = new SelectionModel<any>(true, []);
      this.masterToggle();

      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();

    }));

  }
  selectedItems: SelectItem[] = [];
  multiselectmatterListList: any[] = [];
  multimatterListList: any[] = [];
  selectedItems1: SelectItem[] = [];
  multiselectprebillListList: any[] = [];
  multiprebillList: any[] = [];
  selectedItems2: any[] = [];
  multiselectprebillbatchList: any[] = [];
  multiprebillbatchList: any[] = [];

  selectedItems3: any[] = [];
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];

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
  dropdownSettings2 = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  matterList: any = [];
  dropdownlist() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.statusId.url,
      this.cas.dropdownlist.setup.caseCategoryId.url,
      this.cas.dropdownlist.client.clientId.url,
    ]).subscribe((results) => {
      // this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);
      this.clientname = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.client.clientId.key);
      this.spin.hide();
      // this.sub.add(this.service.Getall().subscribe((res: GeneralMatterElement[]) => {


      //   res.forEach((x) => {
      //     // x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
      //     x.statusId = this.statuslist.find(y => y.key == x.statusId)?.value;
      //     x.caseCategoryId = this.caseCategoryIdList.find(y => y.key == x.caseCategoryId)?.value;
      //     x.clientId = this.clientlist.find(y => y.key == x.clientId)?.value;
      //   })
      //   if (this.auth.classId != '3')
      //     this.ELEMENT_DATA = res.filter(x => x.classId === Number(this.auth.classId));
      //   else
      //     this.ELEMENT_DATA = res;

      //   // this.ELEMENT_DATA = res;
      //   this.caseInformationList = [];

      //   const categories = this.ELEMENT_DATA.map(person => ({
      //     caseInformationNo: person.caseInformationNo,
      //   }));
      //   const distinctThings = categories.filter(
      //     (thing, i, arr) => arr.findIndex(t => t.caseInformationNo === thing.caseInformationNo) === i
      //   );
      //   distinctThings.forEach(x => {

      //     this.caseInformationList.push({ key: x.caseInformationNo, value: x.caseInformationNo });
      //   });

      //   if (excel)
      //     this.excel.exportAsExcel(this.ELEMENT_DATA.sort((a, b) => (a.matterNumber > b.matterNumber) ? -1 : 1));
      //   this.dataSource = new MatTableDataSource<GeneralMatterElement>(this.ELEMENT_DATA.sort((a, b) => (a.matterNumber > b.matterNumber) ? -1 : 1));
      //   // this.dataSource = new MatTableDataSource<GeneralMatterElement>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
      //   this.selection = new SelectionModel<GeneralMatterElement>(true, []);
      //   this.dataSource.sort = this.sort;
      //   this.dataSource.paginator = this.paginator;
      //   this.spin.hide();
      // }, err => {
      //   this.cs.commonerror(err);
      //   this.spin.hide();
      // }));
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });

    // this.sub.add(this.service.getmatter().subscribe(res => {
    //   this.matterList = res;
    //   this.matterList.forEach((x: { matterNumber: string }) => this.multimatterListList.push({ value: x.matterNumber, label: x.matterNumber }))
    //   this.multiselectmatterListList = this.multimatterListList;

    //   this.spin.hide();

    // },
    //   err => {
    //     this.cs.commonerror(err);
    //     this.spin.hide();
    //   }));
    this.sub.add(this.matterservice.getAllSearchDropDown().subscribe((data: any) => {
      if (data) {
        data.matterList.forEach((x: any) => this.multimatterListList.push({ value: x.key, label: x.key + '-' + x.value }));
      }
    }, (err) => {
      this.toastr.error(err, "");
    }));
    this.sub.add(this.service.GetClientdetails().subscribe(res => {
      this.clientList = res;
      this.clientList.forEach((x: { clientId: string, firstNameLastName: string }) => this.multiclientList.push({ id: x.clientId, itemName: x.clientId + '-' + x.firstNameLastName }))
      this.multiselectclientList = this.multiclientList;

      this.spin.hide();

    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));

    this.sub.add(this.service.getprebill().subscribe(res => {
      this.matterList = res;
      this.statusearchList = this.matterList.filter((element: { statusId: any; }) => {
        return element.statusId === 29;

      })
      this.statusearchList.forEach((x: { preBillNumber: string }) => this.multiprebillList.push({ value: x.preBillNumber, label: x.preBillNumber }))
      this.multiselectprebillListList = this.multiprebillList;

      this.spin.hide();

    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    this.sub.add(this.service.getprebillbatch().subscribe(res => {
      this.matterList = res;
      this.statusearchList = this.matterList.filter((element: { statusId: 29; }) => {
        return element.statusId === 29;

      })
      this.statusearchList.forEach((x: any) => this.multiprebillbatchList.push({ value: x.preBillBatchNumber, label: x.preBillBatchNumber, preBillNumber: x.preBillNumber }))
      this.multiselectprebillbatchList = this.multiprebillbatchList;
      this.spin.hide();

    },
      err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    this.spin.hide();

  }
  save() {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }

    let objArray: any[] = [];
    this.selection.selected.forEach((data: any) => {
      data.addInvoiceLine = [];
      data.invoiceDate = this.cs.dateNewFormat(this.form.controls.invoiceDate.value);
      data.referenceText = this.form.controls.referenceText.value;
      data.addInvoiceLine_t.forEach((y: any) => {
        var obj: any = {};
        obj.classId = data.classId;
        obj.clientId = y.clientId[0].id;
        obj.feesCostCutoffDate = data.feesCostCutoffDate;
        obj.invoiceAmount = y.fAmount + y.cAmount;
        obj.invoiceDate = data.invoiceDate;
        obj.languageId = data.languageId;
        obj.matterNumber = data.matterNumber;
        obj.numberOfTimeTickets = data.numberOfTimeTickets;
        obj.partnerAssigned = data.partnerAssigned;
        obj.paymentCutoffDate = data.paymentCutoffDate;
        obj.postingDate = data.postingDate;
        obj.preBillBatchNumber = data.preBillBatchNumber;
        obj.preBillDate = data.preBillDate;
        obj.preBillNumber = data.preBillNumber;
        obj.referenceText = data.referenceText;
        obj.startDateForPreBill = data.startDateForPreBill;
        obj.statusId = data.statusId;
        obj.totalAmount = data.totalAmount;
        obj.addInvoiceLine_t = [y];
        let line: any[] = [];
        line.push({
          matterNumber: obj.matterNumber,
          classId: obj.classId,
          billableAmount: y.fAmount != null ? y.fAmount : 0,
          itemNumber: 1,
        });
        line.push({
          matterNumber: obj.matterNumber,
          classId: obj.classId,
          billableAmount: y.cAmount != null ? y.cAmount : 0,
          itemNumber: 2,
        });
        obj.addInvoiceLine = line;
        objArray.push(obj);
      })

      // x.addInvoiceLine_t.forEach((y: any) => {
      //   y.clientId = y.clientId[0].id;
      // })
    });
    this.spin.show();
    this.sub.add(this.service.Create(objArray).subscribe(res => {
      if (res.erroredOutPrebillNumbers.length == 0) {
        this.toastr.success("Invoice saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.autoGeneratePdf(res.createdInvoiceHeaders[0]);
      } else {
        this.toastr.error("Invoice failed!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
      }
      this.spin.hide();

      // this.router.navigate(['/main/accounts/billlist']);
      this.showSaveStatus(res);
   
    }, err => {
      //  this.cs.commonerror(err);
      this.toastr.error("Invoice failed!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();

    }));

  }
  onItemSelect(item: any, type?: any) {
    if (type == 'PREBILL') {
      this.filterBasedOnPreBill();
    }
  }
  OnItemDeSelect(item: any, type?: any) {
    if (type == 'PREBILL') {
      let data: any = [];
      this.selectedItems2.forEach(remove2 => {
        if (remove2.preBillNumber != item.id) {
          data.push(remove2);
        }
      });
      this.selectedItems2 = data;

      this.multiselectprebillbatchList = [];

      this.selectedItems1.forEach(element => {
        this.multiprebillbatchList.forEach((data: any) => {
          if (data['preBillNumber'] == (element.id)) {
            this.multiselectprebillbatchList.push(data)
          }
        })
      });
    }
  }
  onSelectAll(items: any, type?: any) {
    if (type == 'PREBILL') {
      this.filterBasedOnPreBill();
    }
  }
  onDeSelectAll(items: any, type?: any) {
    if (type == 'PREBILL') {
      this.selectedItems2 = [];
      this.multiselectprebillbatchList = this.multiprebillbatchList;

    }
  }
  filterBasedOnPreBill() {
    this.multiselectprebillbatchList = [];
    this.form.controls.preBillNumber.value.forEach(element => {
      this.multiprebillbatchList.forEach((data: any) => {
        if (data['preBillNumber'] == (element.value)) {
          this.multiselectprebillbatchList.push(data)
        }
      })
    });
  }

  getBillableAmount() {
    let total = 0;
    this.dataSource.data.forEach(element => {
      total = total + (element.addInvoiceLine_t[0].fAmount != null ? element.addInvoiceLine_t[0].fAmount : 0) + (element.addInvoiceLine_t[0].cAmount != null ? element.addInvoiceLine_t[0].cAmount : 0);
    })
    return (Math.round(total * 100) / 100);
  }



  showSaveStatus(element): void {
    const dialogRef = this.dialog.open(SavedInvoiceComponent, {
      disableClose: true,
      width: '80%',
      maxWidth: '90%',
      position: { top: '6.5%' },
      data: element
    });

    dialogRef.afterClosed().subscribe(result => {

    });
  }



  //uploadInvoiceQuotation
  autoGeneratePdf(element: any) {
    let currentDate = this.datePipe.transform(new Date, 'MM-dd-yyyy');
    this.sub.add(this.service.getInvoicePdfData(element.invoiceNumber).subscribe((res: any) => {
      console.log(res)
      if (res != null) {
        var dd: any;
        dd = {
          pageSize: "A4",
          pageOrientation: "portrait",
          pageMargins: [40, 95, 40, 30],
          info: {
            title: 'Invoice - ' + element.invoiceNumber,
            author: 'MnR Clara',
            subject: 'Invoice Pdf Document',
            keywords: 'Contains invocie data',
          },
          header(currentPage: number, pageCount: number, pageSize: any): any {
            if (currentPage != 1) {
              return [{
                layout: 'noBorders', // optional
                table: {
                  // headers are automatically repeated if the table spans over multiple pages
                  // you can declare how many rows should be treated as headers
                  headerRows: 0,
                  widths: [60, 150, 140, 50, '*'],

                  body: [

                    [{ text: 'Client Number', bold: true, fontSize: 10 }, { text: ': ' + res.reportHeader.clientId, fontSize: 10 }, { text: '', fontSize: 10 }, { text: '', fontSize: 10 }, { text: '', fontSize: 10 }],
                    [{ text: 'Matter Number', bold: true, fontSize: 10 }, { text: ': ' + res.reportHeader.matterNumber, fontSize: 10 }, { text: '', fontSize: 10 }, { text: '', fontSize: 10 }, { text: 'Page : ' + currentPage, fontSize: 10 }],

                  ]
                },
                margin: [40, 30]
              }]
            } else {
              return [{

                table: {
                  headerRows: 0,
                  widths: [130, 400,],

                  body: [

                    [
                      { image: diamondlogo.smallLogo, fit: [60, 60], bold: true, fontSize: 12, border: [false, false, false, false] },
                      { image: resizedLogo.resized, fit: [230, 230], bold: true, fontSize: 12, border: [false, false, false, false] },
                    ],

                  ]
                },
                margin: [40, 20, 40, 10]

                // columns: [
                // {
                // image: logo.headerLogo,
                // fit: [100, 100],
                // width: 20
                // },
                // stack: [
                //  // { image: logo.headerLogo, fit: [200, 200], bold: true, fontSize: 12, border: [false, false, false, false] },
                //   // { image: logo.diamondLogo, fit: [200, 200], bold: true, fontSize: 12, border: [false, false, false, false] },
                //   { text: 'Monty & Ramirez LLP', alignment: 'center', bold: true, lineHeight: 1.3, fontSize: 11 },
                //   { text: '150 W Parker Road \n 3rd Floor \n Houston, TX 77076 \n Telephone: 281-493-5529 \n Fax: 281.493.5983', alignment: 'center', fontSize: 10 }, { text: 'Incoive No: ' + (element.invoiceNumber), alignment: 'center', fontSize: 10, },


                // ],
                //margin: [20, 0, 20, 20]
                //  margin: [20, 20]
                // }],
              }
              ]
            }
          },
          footer(currentPage: number, pageCount: number, pageSize: any): any {
            //CLARA/AMS/2022/149 mugilan 12-10-222

            //    if (currentPage < pageCount) {
            return [{
              text: '150 W Parker Road | 3rd Floor | Houston, TX 77076 | Telephone: 281.493.5529 | Fax: 281.493.5983',
              style: 'header',
              alignment: 'center',
              bold: false,
              fontSize: 11
            }
            ]
            //    }

            //CLARA/AMS/2022/149 mugilan 12-10-222
          },
          content: [
            '\n',
            {
              layout: 'noBorders', // optional
              table: {
                // headers are automatically repeated if the table spans over multiple pages
                // you can declare how many rows should be treated as headers
                headerRows: 0,
                widths: [150, 75, 100, 80, 140],
                body: [
                  [{ text: (res.reportHeader.clientName != null ? res.reportHeader.clientName : ''), fontSize: 10 },
                  { text: '', fontSize: 10 },
                  { text: '', fontSize: 10 },
                  { text: '', fontSize: 10 },

                  { text: this.datePipe.transform(element.invoiceDate, 'MMMM d, y'), fontSize: 10 }],
                  [{ text: (res.reportHeader.referenceField3 != null ? res.reportHeader.referenceField3 : ''), fontSize: 10 },
                  { text: '', fontSize: 10 },
                  { text: '', fontSize: 10 },
                  { text: '', fontSize: 10 },
                  { text: 'Invoice No: ' + element.invoiceNumber, fontSize: 10 }],

                  [{ text: (res.reportHeader.referenceField5 != null ? res.reportHeader.referenceField5 : '') + ', ' + (res.reportHeader.referenceField6 != null ? res.reportHeader.referenceField6 : '') + ' ' + (res.reportHeader.referenceField7 != null ? res.reportHeader.referenceField7 : ''), fontSize: 10 },
                  { text: '', fontSize: 10 },
                  { text: '', fontSize: 10 },
                  { text: '', fontSize: 10 },

                  { text: '', fontSize: 10 }],
                ],

              },
            },
            // {
            //   layout: 'noBorders', // optional
            //   table: {
            //     // headers are automatically repeated if the table spans over multiple pages
            //     // you can declare how many rows should be treated as headers
            //     headerRows: 0,
            //     widths: [150, 75, 100, 80,  140],
            //     body: [
            //       [{ text: (res.reportHeader.addressLine15 != null ? res.reportHeader.addressLine15 : ''), fontSize: 10 },
            //       { text: (res.reportHeader.addressLine16 != null ? res.reportHeader.addressLine16 : ''), fontSize: 10 },
            //       { text: (res.reportHeader.addressLine17 != null ? res.reportHeader.addressLine17 : ''), fontSize: 10 },
            //       { text: (res.reportHeader.addressLine18 != null ? res.reportHeader.addressLine18 : ''), fontSize: 10 },

            //       { text: 'Date: ' + this.datePipe.transform(element.invoiceDate, 'MM-dd-yyyy'), fontSize: 10 }],

            //     ]
            //   },
            // },
            '\n',
            { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] },
            '\n',
            {
              text: 'Client Number :    ' + res.reportHeader.clientId + ' - ' + res.reportHeader.clientName,
              alignment: 'left',
              fontSize: 10,
              lineHeight: 1.3
            },
            {
              text: 'Matter Number:    ' + res.reportHeader.matterNumber + ' - ' + res.reportHeader.matterDescription,
              alignment: 'left',
              fontSize: 10,
              lineHeight: 1.3
            },
            {
              text: res.reportHeader.invoiceRemarks,
              alignment: 'left',
              bold: true,
              fontSize: 10
            },
            '\n',
            { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] },
            '\n'
          ],
          defaultStyle
        };

        if (res.timeTicketDetail != null) {
          // let groupedTimeTickets = this.cs.groupByData(res.timeTicketDetail.timeTickets.sort((a, b) => (a.createdOn > b.createdOn) ? 1 : -1), (data: any) => data.taskCode);
          let groupedTimeTickets = this.cs.groupByData(res.timeTicketDetail.timeTickets.sort((a, b) => (a.taskCode > b.taskCode) ? 1 : -1), (data: any) => data.taskCode);
          console.log(groupedTimeTickets)
          let taskCode: any[] = [];
          res.timeTicketDetail.timeTickets.forEach(data => {
            if (!taskCode.includes(data.taskCode)) {
              taskCode.push(data.taskCode);
            }
          })
          //Time tickets
          if (groupedTimeTickets.size > 0) {
            // border: [left, top, right, bottom]
            // border: [false, false, false, false]
            let bodyArray: any[] = [];
            bodyArray.push([{ text: 'Date', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Timekeeper', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Description', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Hours', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Amount', bold: true, decoration: 'underline', alignment: 'right', fontSize: 10, border: [false, false, false, false] }]);

            taskCode.forEach(task => {
              let taskIndex = 0;
              let totalHours = 0;
              let totalBillableAmount = 0;

              groupedTimeTickets.get(task).sort((a, b) => (a.timeTicketDate > b.timeTicketDate) ? 1 : -1).forEach((timeTicket, i) => {
                // let description = decodeURIComponent(timeTicket.ticketDescription);
                let description = timeTicket.ticketDescription;
                totalHours = totalHours + (timeTicket.billableTimeInHours != null ? timeTicket.billableTimeInHours : 0.00);
                totalBillableAmount = totalBillableAmount + (timeTicket.billableAmount != null ? timeTicket.billableAmount : 0.00);
                if (taskIndex == 0) {
                  bodyArray.push([
                    { text: timeTicket.taskCode != null ? timeTicket.taskCode : '', fontSize: 10, bold: true, border: [false, false, false, false], lineHeight: 2 },
                    { text: timeTicket.taskText != null ? timeTicket.taskText : '', colSpan: 4, fontSize: 10, bold: true, border: [false, false, false, false], lineHeight: 2 },
                  ])
                  taskIndex++;
                }
                if (timeTicket.billType != "Non-Billable") {
                  bodyArray.push([
                    { text: this.datePipe.transform(timeTicket.createdOn, 'MM-dd-yyyy'), fontSize: 10, border: [false, false, false, false] },
                    { text: timeTicket.timeTicketName != null ? timeTicket.timeTicketName : '', fontSize: 10, border: [false, false, false, false] },
                    { text: timeTicket.ticketDescription != null ? description : '', fontSize: 10, border: [false, false, false, false] },
                    { text: timeTicket.billableTimeInHours != null ? this.decimalPipe.transform(timeTicket.billableTimeInHours, "1.1-1") : '0.00', fontSize: 10, border: [false, false, false, false] },
                    { text: timeTicket.billableAmount != null ? timeTicket.billableAmount != 0 ? '$ ' + this.decimalPipe.transform(timeTicket.billableAmount, "1.2-2") : '$ ' + this.decimalPipe.transform(timeTicket.billableAmount, "1.2-2") + ' NC' : '$ 0.00 NC', alignment: 'right', fontSize: 10, border: [false, false, false, false] }
                  ])
                }
                if ((i + 1) == groupedTimeTickets.get(task).length) {
                  bodyArray.push([
                    { text: '', fontSize: 10, border: [false, false, false, true] },
                    { text: '', fontSize: 10, border: [false, false, false, true] },
                    { text: timeTicket.taskText != null ? timeTicket.taskText : '', bold: true, fontSize: 10, border: [false, false, false, true] },
                    { text: this.decimalPipe.transform(totalHours, "1.1-1"), fontSize: 10, border: [false, true, false, true], bold: true },
                    { text: '$ ' + this.decimalPipe.transform(totalBillableAmount, "1.2-2"), fontSize: 10, alignment: 'right', border: [false, true, false, true], bold: true }
                    // { text: this.decimalPipe.transform(res.timeTicketDetail.sumOfTotalHours, "1.1-1"), fontSize: 10, border: [false, true, false, true], bold: true },
                    // { text: '$ ' + this.decimalPipe.transform(res.timeTicketDetail.sumOfTotalAmount, "1.2-2"), fontSize: 10, alignment: 'right', border: [false, true, false, true], bold: true }
                  ])
                }
              });
            })
            dd.content.push(
              '\n',
              {
                text: 'Fees',
                style: 'header',
                alignment: 'center',
                decoration: 'underline',
                bold: true
              },
              '\n',
              {
                table: {
                  // layout: 'noBorders', // optional
                  // heights: [,60,], // height for each row
                  headerRows: 0,
                  widths: [60, 60, 250, 30, '*'],
                  body: bodyArray
                }
              },
              // { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] }
            )
          }
        }
        //Timekeeper Summary
        if (res.timeKeeperSummary) {
          if (res.timeKeeperSummary.length > 0) {
            let stackArray: any[] = [];
            stackArray.push(
              {
                text: 'Timekeeper Summary',
                alignment: 'center',
                fontSize: 12,
                lineHeight: 1.3,
                bold: true
              });
            dd.content.push('\n', {
              stack: stackArray
            },)

            let stackArray1: any[] = [];
            res.timeKeeperSummary.forEach((timeKeeper, i) => {
              // this.timekeeperService.Get(timeKeeper.timeTicketCode).subscribe(timekeeperres => {
              //     this.userType.Get(timekeeperres.userTypeId, timekeeperres.classId, timekeeperres.languageId).subscribe(userTypeRes => {

              let timeTickerHours = timeKeeper.timeTicketHours != null ? this.decimalPipe.transform(timeKeeper.timeTicketHours, "1.1-1") : '0.0'

              let timeTicketAmount = timeKeeper.timeTicketAmount != null ? this.decimalPipe.transform(timeKeeper.timeTicketAmount, "1.2-2") : '$0.00';

              stackArray1.push({
                text: 'Timekeeper ' + timeKeeper.timeTicketName + (res.reportHeader.billingFormatId == 11 ? ' - ' + (timeKeeper.userTypeDescription ? timeKeeper.userTypeDescription == 'ATTORNEY' ? 'ASSOCIATE' : timeKeeper.userTypeDescription : '') + ' - ' : '') + ' worked ' + timeTickerHours + ' hours at ' + (timeKeeper.billType == 'nocharge' ? 'No Charge' : timeKeeper.timeTicketAmount != null && timeKeeper.timeTicketHours != null ? this.decimalPipe.transform(timeKeeper.timeTicketAssignedRate, "1.2-2") : '$0.00') + (timeKeeper.billType != 'nocharge' ? ' per hour, totaling ' + '$ ' + timeTicketAmount : ''),
                alignment: 'center',
                fontSize: 10,
                lineHeight: 1.3,
              })
              console.log(stackArray1)

              // })
              // })
            });
            dd.content.push('\n', {
              stack: stackArray1
            }, '\n',)
          }
        }

        //Cost detail
        if (res.expenseEntry != null) {
          if (res.expenseEntry.length > 0) {
            let bodyArray: any[] = [];
            bodyArray.push([{ text: 'Date', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Description', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Amount', bold: true, alignment: 'center', decoration: 'underline', fontSize: 10, border: [false, false, false, false] }]);
            let total = 0;
            let total1 = 0;
            // res.expenseEntry = res.expenseEntry.sort((a, b) => (a.createdOn > b.createdOn) ? 1 : -1);
            res.expenseEntry.forEach((expense, i) => {
              //  let positiveExpenseAmount = (expense.expenseAmount < 0 ? expense.expenseAmount * -1 : expense.expenseAmount)
              let positiveExpenseAmount = (expense.expenseAmount)

              total = total + (expense.expenseAmount != null ? expense.expenseAmount : 0.00);
              total1 = total + (res.reportHeader.administrativeCost != null ? res.reportHeader.administrativeCost : 0.00);
              let positiveTotal = (total1)
              //   let positiveTotal = (total1 < 0 ? total1 * -1 : total1)
              console.log(res.expenseEntry.length)
              bodyArray.push([
                { text: this.datePipe.transform(expense.createdOn, 'MM-dd-yyyy'), fontSize: 10, border: [false, false, false, false] },
                { text: expense.expenseDescription != null ? expense.expenseDescription : '', fontSize: 10, border: [false, false, false, false] },
                { text: positiveExpenseAmount != null ? '$ ' + this.decimalPipe.transform(positiveExpenseAmount, "1.2-2") : '$ 0.00', fontSize: 10, alignment: 'right', border: [false, false, false, false] }
              ])
              if (res.reportHeader.administrativeCost && (i + 1) == res.expenseEntry.length) {
                console.log(res)
                bodyArray.push([
                  { text: this.datePipe.transform(res.reportHeader.caseOpenedDate, 'MM-dd-yyyy'), fontSize: 10, border: [false, false, false, false] },
                  { text: 'Administrative Cost', fontSize: 10, border: [false, false, false, false] },
                  { text: res.reportHeader.administrativeCost != null ? '$ ' + this.decimalPipe.transform(res.reportHeader.administrativeCost, "1.2-2") : '$ 0.00', fontSize: 10, alignment: 'right', border: [false, false, false, false] }
                ])
              }
              if ((i + 1) == res.expenseEntry.length) {
                bodyArray.push([
                  { text: '', fontSize: 10, border: [false, false, false, false] },
                  { text: '', fontSize: 10, border: [false, false, false, false], bold: true },
                  { text: res.reportHeader.administrativeCost ? '$ ' + this.decimalPipe.transform(positiveTotal, "1.2-2") : '$ ' + this.decimalPipe.transform(positiveTotal, "1.2-2"), fontSize: 10, alignment: 'right', border: [false, true, false, false], bold: true }
                ])
              }
            });
            dd.content.push(
              '\n',
              {
                text: 'Cost',
                style: 'header',
                decoration: 'underline',
                alignment: 'center',
                bold: true
              },
              '\n',
              {
                table: {
                  // layout: 'noBorders', // optional
                  // heights: [,60,], // height for each row
                  headerRows: 0,
                  widths: [60, 350, '*'],
                  body: bodyArray
                }
              },
              { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] },
              '\n'
            )
          }
        }

        //Payment detail
        if (res.paymentDetail) {
          if (res.paymentDetail.length > 0) {
            let bodyArray: any[] = [];
            bodyArray.push([{ text: 'Date', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Description', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }, { text: 'Amount', bold: true, decoration: 'underline', fontSize: 10, border: [false, false, false, false] }]);
            let total = 0;
            res.paymentDetail.forEach((payment, i) => {
              total = total + (payment.amount != null ? payment.amount : 0);
              bodyArray.push([
                { text: this.datePipe.transform(payment.postingDate, 'MM-dd-yyyy'), fontSize: 10, border: [false, false, false, false] },
                { text: payment.description != null ? payment.description : 'Payment Received for ' + res.reportHeader.matterNumber + ' on ' + this.datePipe.transform(payment.postingDate, 'MM-dd-yyyy'), fontSize: 10, border: [false, false, false, false] },
                { text: payment.amount != null ? '$ ' + this.decimalPipe.transform(payment.amount, "1.2-2") : '$ 0.00', fontSize: 10, border: [false, false, false, false] }
              ])
              if ((i + 1) == res.paymentDetail.length) {
                bodyArray.push([
                  { text: '', fontSize: 10, border: [false, false, false, false] },
                  { text: 'Total Payments Received:', fontSize: 10, border: [false, false, false, false], bold: true },
                  { text: '$ ' + this.decimalPipe.transform(total, "1.2-2"), fontSize: 10, border: [false, true, false, false], bold: true }
                ])
              }
            });
            dd.content.push(
              '\n',
              {
                text: 'Payment Detail',
                style: 'header',
                alignment: 'center',
                bold: true
              },
              '\n',
              {
                columns: [
                  { width: 50, text: '' },
                  {
                    width: 300,
                    table: {
                      widths: [100, 200, 50],
                      headerRows: 0,
                      body: bodyArray,
                      alignment: "center"
                    }
                  },
                  { width: 10, text: '' },
                ]
              },
              { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] },
              '\n'
            )
          }
        }

        //Final Summary
        if (res.finalSummary) {

          let totalfinalamount = 0;

          totalfinalamount = ((res.finalSummary.priorBalance != null ? res.finalSummary.priorBalance : 0) - (res.finalSummary.paymentReceived != null ? res.finalSummary.paymentReceived : 0));

          let totaldueamount = 0;
          // totaldueamount = (((res.finalSummary.priorBalance != null ? res.finalSummary.priorBalance : 0) -
          //   (res.finalSummary.paymentReceived != null ? res.finalSummary.paymentReceived : 0)) +
          //   (res.finalSummary.currentFees != null ? res.finalSummary.currentFees : 0) +
          //   (res.finalSummary.advancedCost != null ? res.finalSummary.advancedCost : 0));

          totaldueamount = ((res.finalSummary.advancedCost != null ? res.finalSummary.advancedCost : 0) +
            (res.finalSummary.currentFees != null ? res.finalSummary.currentFees : 0) +
            (res.finalSummary.priorBalance != null ? res.finalSummary.priorBalance : 0) -
            ((res.finalSummary.paymentReceived != null ? res.finalSummary.paymentReceived : 0)));

          dd.content.push(
            {
              stack: [{
                text: 'Current Invoice Summary',
                alignment: 'center',
                fontSize: 12,
                lineHeight: 1.3,
                bold: true,
                pageBreak: 'before',
              }]
            })
          let positiveAdvanceCost = (res.finalSummary.advancedCost)
          //       let positiveAdvanceCost = (res.finalSummary.advancedCost < 0 ? res.finalSummary.advancedCost * -1 : res.finalSummary.advancedCost)
          let obj: any = {
            columns: [
              { width: '*', text: '' },
              {
                width: 'auto',
                table: {
                  body: [
                    [
                      {
                        text: 'Prior Balance :',
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, false, false, false],
                      },
                      {
                        text: (res.finalSummary.priorBalance != null ? '$ ' + this.decimalPipe.transform(res.finalSummary.priorBalance, "1.2-2") : '$ 0.00'),
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        border: [false, false, false, false]
                      }
                    ],
                    [
                      {
                        text: 'Payments Received :',
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, false, false, false]
                      },
                      {
                        text: (res.finalSummary.paymentReceived != null ? '$ ' + this.decimalPipe.transform(res.finalSummary.paymentReceived, "1.2-2") : '$ 0.00'),
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        border: [false, false, false, false]
                      }
                    ],
                    [
                      {
                        text: 'Unpaid Prior Balance :',
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, false, false, false]
                      },
                      {
                        text: '$ ' + (this.decimalPipe.transform(totalfinalamount, "1.2-2")),
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        border: [false, true, false, false]
                      }
                    ],
                    [
                      {
                        text: 'Current Fees :',
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, false, false, false]
                      },
                      {
                        text: (res.finalSummary.currentFees != null ? '$ ' + this.decimalPipe.transform(res.finalSummary.currentFees, "1.2-2") : '$ 0.00'),
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        border: [false, false, false, false]
                      }
                    ],
                    [
                      {
                        text: 'Advanced Costs :',
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, false, false, false]
                      },
                      {
                        text: (positiveAdvanceCost != null ? '$ ' + this.decimalPipe.transform(positiveAdvanceCost, "1.2-2") : '$ 0.00'),
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        border: [false, false, false, false]
                      }
                    ],
                    [
                      {
                        text: (totaldueamount < 0 ? 'CREDIT BALANCE, DO NOT PAY' : 'TOTAL AMOUNT DUE :'),
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, false, false, false]
                      },
                      {
                        //    text: '$ ' + this.decimalPipe.transform(totaldueamount < 0 ? totaldueamount * -1 : totaldueamount, "1.2-2"),
                        text: '$ ' + this.decimalPipe.transform(totaldueamount, "1.2-2"),
                        alignment: 'right',
                        fontSize: 10,
                        lineHeight: 1.3,
                        bold: true,
                        border: [false, true, false, true]
                      }
                    ],
                  ],
                  alignment: "center"
                }
              }
            ]
          }
          if (res.finalSummary.paymentReceived != null && res.finalSummary.paymentReceived != 0) {
            obj.columns.push({ width: '210', text: '\n\n     (Last Payment Date : ' + (res.finalSummary.dateOfLastPayment != null ? this.datePipe.transform(res.finalSummary.dateOfLastPayment, 'MM-dd-yyyy') : '') + ")", fontSize: 10 })
          } else {
            obj.columns.push({ width: '210', text: '', fontSize: 10 })
          }
          dd.content.push(obj);
        }

        dd.content.push("\n\n",
          {
            stack: [
              {
                text: 'Should you have any questions pertaining to this invoice,',
                alignment: 'center',
                fontSize: 11,
                lineHeight: 1.1,
                bold: true
              }, {
                text: 'do not hesitate to contact us at',
                alignment: 'center',
                fontSize: 11,
                lineHeight: 1.1,
                bold: true
              }, {
                text: 'accounting@montyramirezlaw.com',
                alignment: 'center',
                fontSize: 11,
                lineHeight: 1.1,
                bold: true
              },
            ]
          })
        // pdfMake.createPdf(dd).download('Invoice - ' + element.invoiceNumber + ' - ' + element.matterNumber + ' - ' + res.reportHeader.matterDescription);
        // pdfMake.createPdf(dd).open();

        const pdfDocGenerator = pdfMake.createPdf(dd);
        pdfDocGenerator.getBlob((blob) => {
          var file = new File([blob], 'Invoice - ' + element.invoiceNumber + ".pdf"); //  + (new Date().getDate()) +'-'+ (new Date().getMonth() + 1) + '-' + new Date().getFullYear() + '_' + this.cs.timeFormat(new Date()) +  
          if (file) {
            this.spin.show();
            this.service.uploadfile(file, 'Invoice/' + element.matterNumber).subscribe((resp: any) => {
              this.service.Update({referenceField1: resp.file,}, element.invoiceNumber).subscribe( res => {
                this.spin.hide();
              })
            })
          }
          this.spin.hide();
        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();
        });

      } else {
        this.toastr.error("No data available", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
      }
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
}

