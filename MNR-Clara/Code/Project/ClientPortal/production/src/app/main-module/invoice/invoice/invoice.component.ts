import { SelectionModel } from "@angular/cdk/collections";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { CommonService } from "src/app/common-service/common-service.service";
import { AuthService } from "src/app/core/core";
import { InvoiceService } from "../invoice.service";

import * as pdfMake from "pdfmake/build/pdfmake";

import pdfFonts from "../../../../assets/font/vfs_fonts.js";
import { defaultStyle } from "./../../../config/customStyles";
import { fonts } from "./../../../config/pdfFonts";
import { logo } from "../../../../assets/font/logo.js";
import { diamondlogo } from "../../../../assets/font/dimond_logo.js";
import { resizedLogo } from "../../../../assets/font/resized_logo.js";
import { DatePipe, DecimalPipe } from "@angular/common";

// PDFMAKE fonts
pdfMake.vfs = pdfFonts.pdfMake.vfs;
pdfMake.fonts = fonts;


export interface ordermanagement {
  lineno: string;
  partner: string;
  product: string;
  status: string;
  date:string;
  description:string;
  }
  
  const ELEMENT_DATA: ordermanagement[] = [
  { lineno: '300001 -01',status: 'Open', partner: '800001',date: '02-02-2022', product: '3500',description: 'Aldi Arias Lopez - PERM'},
  { lineno: '300001 -02',status: 'Paid', partner: '800001',date: '02-02-2022', product: '4500',description: 'Aldi Arias Lopez - PERM'},
  { lineno: '300001 -03',status: 'Open', partner: '800001',date: '02-02-2022', product: '3500',description: 'Aldi Arias Lopez - PERM'},
  { lineno: '300001 -04',status: 'Paid', partner: '800001',date: '02-02-2022', product: '4500',description: 'Aldi Arias Lopez - PERM'},
  
  ];
@Component({
  selector: 'app-invoice',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.scss']
})
export class InvoiceComponent implements OnInit {
  displayedColumns: string[] = ['matterNumber','invoiceNumber','invoiceAmount','remainderdate','invoiceDate','statusId','no'];
  sub = new Subscription();
  ELEMENT_DATA: any[] = [];
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  public icon = 'expand_more';
  constructor(public dialog: MatDialog,
    private service: InvoiceService,
   // private cas: CommonApiService,
    public toastr: ToastrService,
    private router: Router, 
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private decimalPipe: DecimalPipe,
   // private excel: ExcelService,
   private datePipe: DatePipe,
    private fb: FormBuilder,
    private auth: AuthService) { }
    routeto(url: any, id: any) {
      localStorage.setItem('crrentmenu', id);
      this.router.navigate([url]);
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
  showFiller = false;
  animal: string | undefined;
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;

    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  ngOnInit(): void {
   // this.auth.isuserdata();

    this.getAll();
  }
  
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);

 
  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }

  }
  @ViewChild(MatSort, { static: true })
  sort!: MatSort;
  @ViewChild(MatPaginator, { static: true })
  paginator!: MatPaginator; // Pagination
 // Pagination
 getAll() {
  this.spin.show();
  this.sub.add(this.service.search({ clientId: [this.auth.clientId] }).subscribe((res: any[]) => {
    
 let result = res.filter((x: any) =>   x.statusId == 52 || x.statusId == 51 || x.statusId == 53 || x.statusId == 54);
    this.ELEMENT_DATA = result;
    console.log(this.ELEMENT_DATA);
    this.dataSource = new MatTableDataSource<any>(result);
    this.selection = new SelectionModel<any>(true, []);
    this.dataSource.sort = this.sort;
   this.dataSource.paginator = this.paginator;
    this.spin.hide();
  }, err => {
    this.cs.commonerror(err);
    this.spin.hide();
  }));
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.matterNumber + 1}`;
  }






  clearselection(row: any) {

    this.selection.clear();
    this.selection.toggle(row);
  }

  invoiceUrl(url :any){
    window.open(url, '_blank');
  }

  generatePdf(element: any) {
    console.log(element)
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
                  [{ text: (res.reportHeader.referenceField4 != null ? res.reportHeader.referenceField4 : '') + ', ' + (res.reportHeader.referenceField3 != null ? res.reportHeader.referenceField3 : ''), fontSize: 10 },
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
                  dontBreakRows: true,
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
            // let stackArray: any[] = [];
            // stackArray.push(
            //   {
            //     text: 'Timekeeper Summary',
            //     alignment: 'center',
            //     fontSize: 12,
            //     lineHeight: 1.3,
            //     bold: true
            //   });
            // dd.content.push('\n', {
            //   stack: stackArray
            // },)
            let stackArray1: any[] = [];
            res.timeKeeperSummary.forEach((timeKeeper, i) => {
              // this.timekeeperService.Get(timeKeeper.timeTicketCode).subscribe(timekeeperres => {
              //     this.userType.Get(timekeeperres.userTypeId, timekeeperres.classId, timekeeperres.languageId).subscribe(userTypeRes => {

              let timeTickerHours = timeKeeper.timeTicketHours != null ? this.decimalPipe.transform(timeKeeper.timeTicketHours, "1.1-1") : '0.0'
              let timeTicketAmount = timeKeeper.timeTicketAmount != null ? this.decimalPipe.transform(timeKeeper.timeTicketAmount, "1.2-2") : '$0.00';

              stackArray1.push({
                //   text: 'Timekeeper ' + timeKeeper.timeTicketName  + ' worked ' + timeTickerHours + ' hours at ' + (timeKeeper.billType == 'nocharge' ? 'No Charge' : timeKeeper.timeTicketAmount != null && timeKeeper.timeTicketHours != null ? this.decimalPipe.transform(timeKeeper.timeTicketAssignedRate, "1.2-2") : '$0.00') + (timeKeeper.billType != 'nocharge' ? ' per hour, totaling '+ '$ ' + timeTicketAmount : '') ,
                text: 'Timekeeper ' + timeKeeper.timeTicketName + (res.reportHeader.billingFormatId == 11 ? ' - ' + (timeKeeper.userTypeDescription ? timeKeeper.userTypeDescription == 'ATTORNEY' ? 'ASSOCIATE' : timeKeeper.userTypeDescription : '') + ' - ' : '') + ' worked ' + timeTickerHours + ' hours at ' + (timeKeeper.billType == 'nocharge' ? 'No Charge' : timeKeeper.timeTicketAmount != null && timeKeeper.timeTicketHours != null ? this.decimalPipe.transform(timeKeeper.timeTicketAssignedRate, "1.2-2") : '$0.00') + (timeKeeper.billType != 'nocharge' ? ' per hour, totaling ' + '$ ' + timeTicketAmount : ''),
                alignment: 'center',
                fontSize: 10,
                lineHeight: 1.3,
              })

              // })
              // })
            });
            dd.content.push('\n', {
              unbreakable: true,
              stack: [
                {
                  text: 'Timekeeper Summary',
                  alignment: 'center',
                  fontSize: 12,
                  lineHeight: 1.3,
                  bold: true
                },
                stackArray1
              ]
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
              {
                unbreakable: true,
                stack: [
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
                ],
              }

            )
          }
        }
        //Payment detail
        if (res.paymentDetail != null && res.paymentDetail.length > 0) {
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

            {
              unbreakable: true,
              stack: [
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
                        dontBreakRows: true,
                        alignment: "center"
                      }
                    },
                    { width: 10, text: '' },
                  ]
                },
                { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] },
                '\n'
              ]
            }

          )
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

          let positiveAdvanceCost = (res.finalSummary.advancedCost)
          dd.content.push(

            {
              unbreakable: true,
              alignment: 'center',
              stack: [{
                text: 'Current Invoice Summary',

                fontSize: 12,
                lineHeight: 1.3,
                bold: true,
              },
              {
                columns: [
                  { width: 150, text: '' },
                  {
                    width: 200,
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
                    }
                  },
                  { width: '*', alignment: 'left', text: '\n\n     (Last Payment Date : ' + (res.finalSummary.dateOfLastPayment != null ? this.datePipe.transform(res.finalSummary.dateOfLastPayment, 'MM-dd-yyyy') : '') + ")", fontSize: 10 }
                ],
                // columns1: [{ width: '210', text: '\n\n     (Last Payment Date : ' + (res.finalSummary.dateOfLastPayment != null ? this.datePipe.transform(res.finalSummary.dateOfLastPayment, 'MM-dd-yyyy') : '') + ")", fontSize: 10 }]

              },
              {
                text: 'Should you have any questions pertaining to this invoice,',
                alignment: 'center',
                fontSize: 11,
                lineHeight: 1.1,
                margin: [0, 30, 0, 0],
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
        }
        pdfMake.createPdf(dd).download('Invoice - ' + element.invoiceNumber + ' - ' + element.matterNumber + ' - ' + res.reportHeader.matterDescription);
        // pdfMake.createPdf(dd).open();
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