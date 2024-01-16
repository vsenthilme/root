import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormControl, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatTableDataSource } from "@angular/material/table";
import { ActivatedRoute, Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Location } from "@angular/common";
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
    
    private pdf: BillComponent,
    ) { }
  isRun = false;
  ELEMENT_DATA: any[] = [];
  ngOnInit(): void {
    this.dropdownlist();
    this.dropdownSettings2.disabled=true;
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
    

    if(this.form.controls.preBillNumber.value == null && this.form.controls.preBillBatchNumber.value == null && this.form.controls.matterNumber.value == null && this.form.controls.startPreBillDate.value == null){
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
          billableAmount: y.fAmount,
          itemNumber: 1,
        });
        line.push({
          matterNumber: obj.matterNumber,
          classId: obj.classId,
          billableAmount: y.cAmount,
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
      if(res.erroredOutPrebillNumbers.length == 0){
        this.toastr.success("Invoice saved successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
      }else{
        this.toastr.error("Invoice failed!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
      }
      this.spin.hide();

     // this.router.navigate(['/main/accounts/billlist']);
     this.showSaveStatus(res);
    // this.pdf.autoGeneratePdf(res);
    }, err => {
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

  getBillableAmount(){
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
}

