import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatTableDataSource } from "@angular/material/table";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { GeneralMatterService } from "../General/general-matter.service";
// import the pdfmake library
import pdfMake from "pdfmake/build/pdfmake";
// importing the fonts and icons needed
import pdfFonts from "./../../../../../assets/font/vfs_fonts.js";
import { defaultStyle } from "./../../../../config/customStyles";
import { fonts } from "./../../../../config/pdfFonts";
import { DatePipe, DecimalPipe } from "@angular/common";
import { MatSort } from "@angular/material/sort";

// PDFMAKE fonts
pdfMake.vfs = pdfFonts.pdfMake.vfs;
pdfMake.fonts = fonts;

@Component({
  selector: 'app-matter-activiy',
  templateUrl: './matter-activiy.component.html',
  styleUrls: ['./matter-activiy.component.scss']
})
export class MatterActiviyComponent implements OnInit {
  screenid = 1167;
  public icon = 'expand_more';
  isShowDiv = false;
  fullscreen = false;
  search = true;
  back = false;
  showFloatingButtons: any;
  toggle = true;
  arBalance1: any;
  plusOneDate: Date;
  matterDescription: any;
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

  form = this.fb.group({
    matterNumber: [, [Validators.required]],
    clientName: [],
    fromPostingDate: [new Date("01/01/00 00:00:00"), [Validators.required]],
    toPostingDate: [this.cs.todayapi(), [Validators.required]],
    toPostingDate1: [],
  });


  displayedColumns: string[] = ['select', 'transactionDate', 'type','transactionID', 'code', 'debit', 'credit', 'status', 'remarks',];
  dataSource = new MatTableDataSource<any>();
  selection = new SelectionModel<any>(true, []);

  RA: any = {};
  submitted = false;
  sub = new Subscription();
  matterClientList: any[] = [];
  matterSelectList: any[] = [];
  arBalance = 0;
  debitTotal = 0
  creditTotal = 0;

  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination
  

  userId = this.auth.userID;

  constructor(
    public dialog: MatDialog,
    private auth: AuthService,
    private cs: CommonService,
    private spin: NgxSpinnerService,
    private fb: FormBuilder,
    private decimalPipe: DecimalPipe,
    private excel: ExcelService,
    public toastr: ToastrService,
    public service: GeneralMatterService,
    private cas: CommonApiService,
    private datePipe: DatePipe
  ) { }

  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllDropDown();


    this.form.controls.matterNumber.valueChanges.subscribe(x => {
      console.log(this.matterClientList)
      this.matterDescription = this.matterClientList.find(y => y.matterNumber == x)?.matterDescription;
      console.log(this.matterDescription)
    })
  }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  getAllDropDown() {
    this.spin.show();
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.matter.matterNumberList.url,
    ]).subscribe((results: any) => {
      this.spin.hide();
      this.matterClientList = results[0].matterDropDown;
      this.matterClientList.forEach(matter => {
        this.matterSelectList.push({ value: matter.matterNumber, label: (matter.matterNumber + "-" + matter.matterDescription) });
      });
    });
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
    // this.plusOneDate = new Date(this.form.controls.toPostingDate.value);
    // this.plusOneDate.setDate(this.plusOneDate.getDate() + 1);

    // this.form.controls.toPostingDate1.patchValue(this.plusOneDate)

    let obj: any = {};

    obj.fromPostingDate = this.cs.dateNewFormat(this.form.controls.fromPostingDate.value);
    obj.toPostingDate = this.cs.dateNewFormat(this.form.controls.toPostingDate.value);
    obj.matterNumber = this.form.controls.matterNumber.value;

    this.spin.show();
    this.sub.add(this.service.getMatterBillingActivityReport(obj).subscribe(res => {
      this.spin.hide();
      this.arBalance = 0;
      this.debitTotal = 0;
      this.creditTotal = 0;
      let list: any[] = [];
      res.invoice.forEach(element => {
        this.debitTotal = this.debitTotal + (element.debit != null ? element.debit : 0);
        element['type'] = 'Invoice';
        element['status'] = 'Billed';
        element['transactionDate'] = element.postingDate;
        if(element.debit){
          list.push(element);
        }
     
      });
      res.payment.forEach(element => {
        this.creditTotal = this.creditTotal + (element.credit != null ? element.credit : 0);
        element['type'] = 'Payment';
        element['transactionDate'] = element.paymentDate;
        // element['status'] = 'Billed';
        if(element.credit){
          list.push(element);
        }
      });
      this.arBalance = this.debitTotal - this.creditTotal;
      this.arBalance1  = '$ ' + this.decimalPipe.transform(this.arBalance, "1.2-2") 
      console.log(list)
      this.dataSource.data = list;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;

      this.search = false;
      this.back = true;
    },
      err => {
        this.submitted = false;
        this.cs.commonerror(err);
        this.spin.hide();
      }));
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

  togglesearch() {
    this.search = false;
    this.fullscreen = false;
    this.back = true;
  }
  backsearch() {
    this.search = true;
    this.fullscreen = true;
    this.back = false;
 
  }
  reset() {
    this.form.reset();
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

  onItemSelect(item) {
    console.log(item)
    console.log(this.form.controls.matterNumber.value)
    let clientObj: any = this.matterClientList.filter(x => x.matterNumber == item.value);
    this.form.controls.clientName.patchValue(clientObj[0].clientName);
  }

  generatePdf() {
    let currentDate = this.datePipe.transform(new Date, 'MM-dd-yyyy');         // start CLARA/AMS/2022/140 mugilan 05-10-2022
    let currentTime = this.datePipe.transform(new Date, 'hh:mm');
    let userId = this.userId;
    console.log(userId)
    if (this.dataSource.data.length > 0) {
      let obj: any = this.matterClientList.filter(x => x.matterNumber == this.form.controls.matterNumber.value);
      let matter = obj[0].matterNumber + " - " + obj[0].matterDescription;
      let client = obj[0].clientId + ' - ' + obj[0].clientName;
      var dd: any;
      dd = {
        pageSize: "A4",
        pageOrientation: "portrait",
        content: [],
        pageMargins: [40, 110, 40, 40],
        header(currentPage: number, pageCount: number, pageSize: any): any {
          return [
            {
              table: {
                // layout: 'noBorders', // optional
                // heights: [,60,], // height for each row
                headerRows: 1,
                widths: ['*', 100],
                body: [[
                  { text: 'Matter Billing Detail', bold: true, italics: true, fontSize: 15, border: [false, false, false, false] },
                  { text: 'Report Date: ' + currentDate, bold: true, fontSize: 9, border: [false, false, false, false] },
                ], [
                  { text: '', bold: true, fontSize: 9, border: [false, false, false, false] },
                  { text: 'Report Time: ' + currentTime, bold: true, fontSize: 9, border: [false, false, false, false] },
                ], [
                  { text: '', bold: true, fontSize: 9, border: [false, false, false, false] },
                  { text: 'Page: ' + currentPage + ' of ' + pageCount, bold: true, fontSize: 9, border: [false, false, false, false] },
                ], [
                  { text: 'Monty & Ramirez LLP', bold: true, fontSize: 9, border: [false, false, false, false] },
                  { text: 'User ID: ' + userId, bold: true, fontSize: 9, border: [false, false, false, false] },
                ]]
              },
              margin: [40, 20]
            }
          ]
        },
        defaultStyle
      };

      // border: [left, top, right, bottom]
      // border: [false, false, false, false]
      let bodyArray: any[] = [];
      bodyArray.push([
        { text: 'Date', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Code', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Type', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Description', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Debit', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Credit', bold: true, fontSize: 9, border: [false, true, false, true] },
        { text: 'Billing Status', bold: true, fontSize: 9, border: [false, true, false, true] }
      ]);
      console.log( this.dataSource.data)
      this.dataSource.data.sort((a, b) => (a.transactionDate > b.transactionDate) ? -1 : 1);
      this.dataSource.data.forEach((data, i) => {
        bodyArray.push([
          { text: data.type == 'Invoice' ? this.datePipe.transform(data.postingDate, 'MM-dd-yyyy', 'GMT00:00') : this.datePipe.transform(data.paymentDate, 'MM-dd-yyyy', 'GMT00:00'), fontSize: 9, border: [false, false, false, false], lineHeight: 2 },   // start CLARA/AMS/2022/140 mugilan 05-10-2022
          { text: data.type == 'Invoice' ? data.code : data.paymentCode, fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
          { text: data.type == 'Invoice' ? 'INVOICE' : data.transactionType, fontSize: 9, border: [false, false, false, false] },
          { text: data.type == 'Invoice' ? data.description : data.paymentDesc, fontSize: 9, border: [false, false, false, false] },
          { text: data.type == 'Invoice' ? data.debit != null ?  '$' + this.decimalPipe.transform(data.debit, "1.2-2") : '$0.00' : '$0.00', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
          { text: data.type == 'Payment' ? data.credit != null ? '$' + this.decimalPipe.transform(data.credit, "1.2-2") : '$0.00' : '$0.00', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
          { text: data.type == 'Invoice' ? data.status : data.statusId, fontSize: 9, border: [false, false, false, false], lineHeight: 2 }
        ])
      });
      //total row
      bodyArray.push([
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
        { text: 'Total:', fontSize: 9, border: [false, false, false, false] },
        { text: this.debitTotal != null ? '$' + this.decimalPipe.transform(this.debitTotal, "1.2-2") : '$0.00', fontSize: 9, border: [false, true, false, true], lineHeight: 2 },
        { text: this.creditTotal != null ? '$' + this.decimalPipe.transform(this.creditTotal, "1.2-2") : '$0.00', fontSize: 9, border: [false, true, false, true], lineHeight: 2 },
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 }
      ])
      //ar balance total row
      bodyArray.push([
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
        { text: 'AR Balance', fontSize: 9, border: [false, false, false, false] },
        { text: this.arBalance != null ? '$' + this.decimalPipe.transform(this.arBalance, "1.2-2") : '$0.00', fontSize: 9, border: [false, true, false, true], lineHeight: 2 },
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
        { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 }
      ])
      dd.content.push(
        '\n',
        {
          layout: 'noBorders', // optional
          table: {
            headerRows: 0,
            widths: [70, 110,  100, 140, 60],
            body: [
              [{ text: 'Date Range :', fontSize: 8, alignment: 'left', bold: true }, { text: this.datePipe.transform(this.form.controls.fromPostingDate.value, 'MM-dd-yyyy', 'GMT00:00') + " - " + this.datePipe.transform(this.form.controls.toPostingDate.value, 'MM-dd-yyyy', 'GMT00:00'), fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8 }],    // start CLARA/AMS/2022/140 mugilan 05-10-2022
              [{ text: 'Client     :', fontSize: 8, alignment: 'left', bold: true }, { text: client, fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8, bold: true }],
              [{ text: 'Matter     :', fontSize: 8, alignment: 'left', bold: true }, { text: matter, fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8 }, { text: '', fontSize: 8, bold: true }]

            ]
          },
        },
        '\n',
        {
          table: {
            // layout: 'noBorders', // optional
            // heights: [,60,], // height for each row
            headerRows: 0,
            widths: [70, 90, 90, 150, 50, 50, '*'],
            body: bodyArray
          }
        }
      )
      //pdfMake.createPdf(dd).download('Matter Billing Detail');
       pdfMake.createPdf(dd).open();
    } else {
      this.toastr.info("No data available", "Pdf Generate");
    }
  }



  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data = this.dataSource.data.sort((a, b) => (a.transactionDate > b.transactionDate) ? 1 : -1);
    this.dataSource.data.forEach(x => {
      res.push({
        "Transaction Date":this.cs.dateapiutc0(x.transactionDate),
        'Transaction Type': x.type == 'Invoice' ? 'INVOICE' : x.transactionType,
        "Invoice / Payment No": x.type == 'Invoice' ? x.transactionID : x.paymentNumber,
        "Code": x.type == 'Invoice' ? x.code : x.paymentCode,
        "Debit  ": x.type == 'Invoice' ?  this.decimalPipe.transform(x.debit, "1.2-2") : '0.00',
        'Credit': x.type == 'Payment' ?  this.decimalPipe.transform(x.credit, "1.2-2") : '0.00',
        'Status': x.type == 'Invoice' ? x.status : x.statusId,
        'Remarks':  x.type == 'Invoice' ? x.description : x.paymentDesc
      });

    

    })
    res.push({
      "Transaction Date": '',
      'Transaction Type': '',
      "Invoice / Payment No": '',
      "Code": 'Total',
      "Debit  ": this.debitTotal,
      'Credit': this.creditTotal,
      'Status': '',
      'Remarks':  '',
    });
    this.excel.exportAsExcel(res, "Matter Activity");
  }

}
