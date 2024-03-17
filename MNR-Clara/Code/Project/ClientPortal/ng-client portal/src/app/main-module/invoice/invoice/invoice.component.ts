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
import { DatePipe } from "@angular/common";

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
 let result1 = result.filter(x => x.referenceField10 == "Sent");
    this.ELEMENT_DATA = result1;
    console.log(this.ELEMENT_DATA);
    this.dataSource = new MatTableDataSource<any>(result1);
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
    let currentDate = this.datePipe.transform(new Date, 'MM-dd-yyyy');
    this.sub.add(this.service.getInvoicePdfData(element.invoiceNumber).subscribe((res: any) => {
      console.log(res)
      if (res != null) {
        let groupedTimeTickets = this.cs.groupByData(res.timeTicketDetail.timeTickets, (data: any) => data.taskCode);

        var dd: any;
        dd = {
          pageSize: "A4",
          pageOrientation: "portrait",
          pageMargins: [40, 95, 40, 60],
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
                    [{ text: 'Client Number', bold: true, fontSize: 9 }, { text: ': ' + res.reportHeader.clientId, fontSize: 9 }, { text: '', fontSize: 9 }, { text: '', fontSize: 9 }, { text: currentDate, fontSize: 9 }],
                    [{ text: 'Matter', bold: true, fontSize: 9 }, { text: ': ' + res.reportHeader.matterNumber, fontSize: 9 }, { text: '', fontSize: 9 }, { text: '', fontSize: 9 }, { text: 'Page : ' + currentPage, fontSize: 9 }]
                  ]
                },
                margin: [40, 30]
              }]
            } else {
              return [{
                columns: [
                  {
                    image: logo.headerLogo,
                    fit: [100, 100],
                    width: 20
                  },
                  {
                    stack: [
                      { text: 'Monty & Ramirez LLP', alignment: 'center', bold: true, lineHeight: 1.3, fontSize: 10 },
                      { text: '150 W Parker Road \n 3rd Floor \n Houston, TX 77076 \n Telephone: 281-493-5529 \n Fax: 281.493.5983', alignment: 'center', fontSize: 9 }
                    ]
                  }],
                margin: [40, 20]
              }]
            }
          },
          footer(currentPage: number, pageCount: number, pageSize: any): any {
            if (currentPage < pageCount) {
              return [{
                text: 'Continued on Next Page',
                style: 'header',
                alignment: 'center',
                bold: true,
                fontSize: 6
              }]
            }
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
                  [{ text: (res.reportHeader.addressLine3 != null ? res.reportHeader.addressLine3 : ''), fontSize: 9 }, { text: '', fontSize: 9 }, { text: '', fontSize: 9 }, { text: '', fontSize: 9 }, { text: this.datePipe.transform(element.invoiceDate, 'MM-dd-yyyy'), fontSize: 9 }],
                ]
              },
            },
            '\n',
            { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] },
            '\n',
            {
              text: 'Client Number :    ' + res.reportHeader.clientId + ' - ' + res.reportHeader.clientName,
              alignment: 'left',
              fontSize: 9,
              lineHeight: 1.3
            },
            {
              text: 'Matter :    ' + res.reportHeader.matterNumber + ' - ' + res.reportHeader.matterDescription,
              alignment: 'left',
              fontSize: 9,
              lineHeight: 1.3
            },
            {
              text: 'For Services Rendered Through ' + this.datePipe.transform(res.reportHeader.caseOpenedDate, 'MM-dd-yyyy'),
              alignment: 'left',
              bold: true,
              fontSize: 9
            },
            '\n',
            { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] },
            '\n'
          ],
          defaultStyle
        };

        //Time tickets
        if (groupedTimeTickets.size > 0) {
          // border: [left, top, right, bottom]
          // border: [false, false, false, false]
          let bodyArray: any[] = [];
          bodyArray.push([{ text: 'Date', bold: true, decoration: 'underline', fontSize: 9, border: [false, false, false, false] }, { text: 'Timekeeper', bold: true, decoration: 'underline', fontSize: 9, border: [false, false, false, false] }, { text: 'Description', bold: true, decoration: 'underline', fontSize: 9, border: [false, false, false, false] }, { text: 'Hours', bold: true, decoration: 'underline', fontSize: 9, border: [false, false, false, false] }, { text: 'Amount', bold: true, decoration: 'underline', fontSize: 9, border: [false, false, false, false] }]);
          let totalHours = 0;
          let totalBillableAmount = 0;
          groupedTimeTickets.get(null).forEach((timeTicket, i) => {
            totalHours = totalHours + (timeTicket.approvedBillableTimeInHours != null ? timeTicket.approvedBillableTimeInHours : 0.00);
            totalBillableAmount = totalBillableAmount + (timeTicket.approvedBillableAmount != null ? timeTicket.approvedBillableAmount : 0.00);
            bodyArray.push([
              { text: this.datePipe.transform(timeTicket.createdOn, 'MM-dd-yyyy'), fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
              { text: 'Brittany Mortimer', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
              { text: timeTicket.ticketDescription != null ? timeTicket.ticketDescription : '', fontSize: 9, border: [false, false, false, false] },
              { text: timeTicket.approvedBillableTimeInHours != null ? timeTicket.approvedBillableTimeInHours : '0.00', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
              { text: timeTicket.approvedBillableAmount != null ? timeTicket.approvedBillableAmount : '0.00', fontSize: 9, border: [false, false, false, false], lineHeight: 2 }
            ])
            if ((i + 1) == groupedTimeTickets.get(null).length) {
              bodyArray.push([
                { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
                { text: '', fontSize: 9, border: [false, false, false, false], lineHeight: 2 },
                { text: '', fontSize: 9, border: [false, false, false, false] },
                { text: totalHours, fontSize: 9, border: [false, true, false, false], lineHeight: 2, bold: true },
                { text: totalBillableAmount, fontSize: 9, border: [false, true, false, false], lineHeight: 2, bold: true }
              ])
            }
          });
          dd.content.push(
            '\n',
            {
              text: 'Fees',
              style: 'header',
              alignment: 'center',
              bold: true
            },
            '\n',
            {
              table: {
                // layout: 'noBorders', // optional
                // heights: [,60,], // height for each row
                headerRows: 0,
                widths: [100, 100, 200, 50, '*'],
                body: bodyArray
              }
            },
            { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] }
          )
        }
        //Timekeeper Summary
        if (res.timeKeeperSummary) {
          if (res.timeKeeperSummary.length > 0) {
            let stackArray: any[] = [];
            stackArray.push(
              {
                text: 'Timekeeper Summary',
                alignment: 'center',
                fontSize: 11,
                lineHeight: 1.3,
                bold: true
              });
            res.timeKeeperSummary.forEach((timeKeeper, i) => {
              stackArray.push({
                text: 'Timekeeper Approved Bill Amount ' + timeKeeper.approvedBillableAmount + ' Time Ticket Amount ' + timeKeeper.timeTicketAmount,
                alignment: 'center',
                fontSize: 9,
                lineHeight: 1.3,
              })
            });
            dd.content.push('\n', {
              stack: stackArray
            }, '\n', { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 515, y2: 0, lineWidth: 1.2 }] }, '\n')
          }
        }

        //Cost detail
        if (res.expenseEntry.length > 0) {
          let bodyArray: any[] = [];
          bodyArray.push([{ text: 'Date', bold: true, decoration: 'underline', fontSize: 9, border: [false, false, false, false] }, { text: 'Description', bold: true, decoration: 'underline', fontSize: 9, border: [false, false, false, false] }, { text: 'Amount', bold: true, decoration: 'underline', fontSize: 9, border: [false, false, false, false] }]);
          let total = 0;
          res.expenseEntry.forEach((expense, i) => {
            total = total + (expense.expenseAmount != null ? expense.expenseAmount : 0.00);
            bodyArray.push([
              { text: this.datePipe.transform(expense.createdOn, 'MM-dd-yyyy'), fontSize: 9, border: [false, false, false, false] },
              { text: expense.expenseDescription != null ? expense.expenseDescription : '', fontSize: 9, border: [false, false, false, false] },
              { text: expense.expenseAmount != null ? expense.expenseAmount : '0.00', fontSize: 9, border: [false, false, false, false] }
            ])
            if ((i + 1) == res.expenseEntry.length) {
              bodyArray.push([
                { text: '', fontSize: 9, border: [false, false, false, false] },
                { text: 'Total Costs', fontSize: 9, border: [false, false, false, false], bold: true },
                { text: total, fontSize: 9, border: [false, true, false, false], bold: true }
              ])
            }
          });
          dd.content.push(
            '\n',
            {
              text: 'Cost Detail',
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

        //Payment detail
        if (res.paymentDetail.length > 0) {
          let bodyArray: any[] = [];
          bodyArray.push([{ text: 'Date', bold: true, decoration: 'underline', fontSize: 9, border: [false, false, false, false] }, { text: 'Description', bold: true, decoration: 'underline', fontSize: 9, border: [false, false, false, false] }, { text: 'Amount', bold: true, decoration: 'underline', fontSize: 9, border: [false, false, false, false] }]);
          let total = 0;
          res.paymentDetail.forEach((payment, i) => {
            total = total + (payment.paymentAmount != null ? payment.paymentAmount : 0);
            bodyArray.push([
              { text: this.datePipe.transform(payment.createdOn, 'MM-dd-yyyy'), fontSize: 9, border: [false, false, false, false] },
              { text: payment.paymentDescription != null ? payment.paymentDescription : '', fontSize: 9, border: [false, false, false, false] },
              { text: payment.paymentAmount != null ? payment.paymentAmount : '0.00', fontSize: 9, border: [false, false, false, false] }
            ])
            if ((i + 1) == res.paymentDetail.length) {
              bodyArray.push([
                { text: '', fontSize: 9, border: [false, false, false, false] },
                { text: 'Total Payments Received:', fontSize: 9, border: [false, false, false, false], bold: true },
                { text: total, fontSize: 9, border: [false, true, false, false], bold: true }
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

        //Final Summary
        if (res.finalSummary) {

          dd.content.push(
            {
              stack: [{
                text: 'Current Invoice Summary',
                alignment: 'center',
                fontSize: 11,
                lineHeight: 1.3,
                bold: true
              }]
            },
            {
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
                          fontSize: 9,
                          lineHeight: 1.3,
                          bold: true,
                          border: [false, false, false, false],
                        },
                        {
                          text: (res.finalSummary.priorBalance != null ? res.finalSummary.priorBalance : '0.00'),
                          alignment: 'right',
                          fontSize: 9,
                          lineHeight: 1.3,
                          border: [false, false, false, false]
                        }
                      ],
                      [
                        {
                          text: 'Payments Received :',
                          alignment: 'right',
                          fontSize: 9,
                          lineHeight: 1.3,
                          bold: true,
                          border: [false, false, false, false]
                        },
                        {
                          text: (res.finalSummary.paymentReceived != null ? res.finalSummary.paymentReceived : '0.00'),
                          alignment: 'right',
                          fontSize: 9,
                          lineHeight: 1.3,
                          border: [false, false, false, false]
                        }
                      ],
                      [
                        {
                          text: 'Unpaid Prior Balance :',
                          alignment: 'right',
                          fontSize: 9,
                          lineHeight: 1.3,
                          bold: true,
                          border: [false, false, false, false]
                        },
                        {
                          text: ((res.finalSummary.priorBalance != null ? res.finalSummary.priorBalance : 0) - (res.finalSummary.paymentReceived != null ? res.finalSummary.paymentReceived : 0)),
                          alignment: 'right',
                          fontSize: 9,
                          lineHeight: 1.3,
                          border: [false, true, false, false]
                        }
                      ],
                      [
                        {
                          text: 'Current Fees :',
                          alignment: 'right',
                          fontSize: 9,
                          lineHeight: 1.3,
                          bold: true,
                          border: [false, false, false, false]
                        },
                        {
                          text: (res.finalSummary.currentFees != null ? res.finalSummary.currentFees : '0.00'),
                          alignment: 'right',
                          fontSize: 9,
                          lineHeight: 1.3,
                          border: [false, false, false, false]
                        }
                      ],
                      [
                        {
                          text: 'Advanced Costs :',
                          alignment: 'right',
                          fontSize: 9,
                          lineHeight: 1.3,
                          bold: true,
                          border: [false, false, false, false]
                        },
                        {
                          text: (res.finalSummary.advancedCost != null ? res.finalSummary.advancedCost : '0.00'),
                          alignment: 'right',
                          fontSize: 9,
                          lineHeight: 1.3,
                          border: [false, false, false, false]
                        }
                      ],
                      [
                        {
                          text: 'TOTAL AMOUNT DUE :',
                          alignment: 'right',
                          fontSize: 9,
                          lineHeight: 1.3,
                          bold: true,
                          border: [false, false, false, false]
                        },
                        {
                          text:
                            (((res.finalSummary.priorBalance != null ? res.finalSummary.priorBalance : 0) -
                              (res.finalSummary.paymentReceived != null ? res.finalSummary.paymentReceived : 0)) +
                              (res.finalSummary.currentFees != null ? res.finalSummary.currentFees : 0) +
                              (res.finalSummary.advancedCost != null ? res.finalSummary.advancedCost : 0)),
                          alignment: 'right',
                          fontSize: 9,
                          lineHeight: 1.3,
                          bold: true,
                          border: [false, true, false, true]
                        }
                      ],
                    ],
                    alignment: "center"
                  }
                },
                { width: '*', text: '\n\n     (Last Payment Date : ' + (res.finalSummary.dateOfLastPayment != null ? this.datePipe.transform(res.finalSummary.dateOfLastPayment, 'MM-dd-yyyy') : '') + ")", fontSize: 9 },
              ]
            })
        }

        dd.content.push("\n\n",
          {
            stack: [
              {
                text: 'Should you have any questions pertaining to this invoice,',
                alignment: 'center',
                fontSize: 10,
                lineHeight: 1.1,
                bold: true
              }, {
                text: 'do not hesitate to contact us at',
                alignment: 'center',
                fontSize: 10,
                lineHeight: 1.1,
                bold: true
              }, {
                text: 'accounting@montyramirezlaw.com',
                alignment: 'center',
                fontSize: 10,
                lineHeight: 1.1,
                bold: true
              },
            ]
          })
        pdfMake.createPdf(dd).download('Invoice - ' + element.invoiceNumber);
      } else {
        this.toastr.info("No data available", "Pdf Generate");
      }
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

}