
  import { SelectionModel } from '@angular/cdk/collections';
  import { Component, OnInit, ViewChild } from '@angular/core';
  import { FormBuilder } from '@angular/forms';
  import { MatDialog } from '@angular/material/dialog';
  import { MatPaginator } from '@angular/material/paginator';
  import { MatSort } from '@angular/material/sort';
  import { MatTableDataSource } from '@angular/material/table';
  import { Router, ActivatedRoute } from '@angular/router';
  import { NgxSpinnerService } from 'ngx-spinner';
  import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DeleteComponent } from 'src/app/common-field/delete/delete.component';
  import { CommonService } from 'src/app/common-service/common-service.service';
  import { AuthService } from 'src/app/core/core';
import { PaymentNewComponent } from './payment-new/payment-new.component';
import { PaymentService } from './payment.service';
import { agreementLogo } from "../../../../../assets/font/agreement-logo.js";

import { ToWords } from 'to-words';

import * as _moment from 'moment';

  // import the pdfmake library
import pdfMake from "pdfmake/build/pdfmake";
// importing the fonts and icons needed
import pdfFonts from "../../../../../assets/font/vfs_fonts";
import { defaultStyle } from "../../../../../app/config/customStyles";
import { fonts } from "../../../../../app/config/pdfFonts";
import { HttpRequest, HttpResponse } from "@angular/common/http";
import { logo } from "../../../../../assets/font/logo.js";
import { DecimalPipe, DatePipe } from '@angular/common';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import moment from 'moment';
import { AgreementService } from '../agreement/agreement.service';
// PDFMAKE fonts
pdfMake.vfs = pdfFonts.pdfMake.vfs;
pdfMake.fonts = fonts;


// let fonts1 = {
//   Roboto: {
//       normal: 'node_modules/roboto-font/fonts/Roboto/roboto-regular-webfont.ttf',
//       bold: 'node_modules/roboto-font/fonts/Roboto/roboto-bold-webfont.ttf',
//       italics: 'node_modules/roboto-font/fonts/Roboto/roboto-italic-webfont.ttf',
//       bolditalics: 'node_modules/roboto-font/fonts/Roboto/roboto-bolditalic-webfont.ttf'
//   }
// };


  export interface PeriodicElement {
    usercode: string;
    name: string;
    admin: string;
    role: string;
    userid: string;
    password: string;
    status: string;
    email: string;
    phoneno: string;
    Agreement: string;
    paiddate: string;
  }
  
  const ELEMENT_DATA: PeriodicElement[] = [
    {usercode: "test", name: 'test', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test', phoneno: 'test',Agreement: 'test',paiddate: 'test'},
  ];
 
  @Component({
    selector: 'app-payment',
    templateUrl: './payment.component.html',
    styleUrls: ['./payment.component.scss']
  })
  export class PaymentComponent implements OnInit {
    customerName: any[] = [];
    storageName:any[] = [];
    periodDate: any;
    oneDateBefore: any;
  
  
    constructor(private router: Router,
      private fb: FormBuilder,
      public toastr: ToastrService,
      private cs: CommonService,
      private spin: NgxSpinnerService,
      private auth: AuthService,
      public dialog: MatDialog,
      private route: ActivatedRoute,
      private service: PaymentService,
      private cas: CommonService,
      private commonApi: CommonApiService,
      private decimalPipe: DecimalPipe,
      public datePipe: DatePipe,
      
      private agreementService: AgreementService,
    ) { }

    sub = new Subscription();
    ELEMENT_DATA: any[] = [];
    nameDropdown: any;
    ngOnInit() {


      this.commonApi.getalldropdownlist([
        this.commonApi.dropdownlist.trans.customerDropdown.url,
        this.commonApi.dropdownlist.trans.storageDropdown.url,
      ]).subscribe((results: any) => {
        results[0].customerDropDown.forEach((x: any) => {
          this.customerName.push({ value: x.customerCode, label: x.customerName });
        })
        results[1].storageDropDown.forEach((x: any) => {
          this.storageName.push({ value: x.storeNumber, label: x.storeId });
        })
        this.search()
      }, (err) => {
        this.toastr.error(err, "");
        this.spin.hide();
      });
    

    }
  
  
  
    title1 = "Organisation Setup";
    title2 = "Company";
  
  
  
  
  
    isShowDiv = false;
    public icon = 'expand_more';
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
      console.log('show:' + this.showFloatingButtons);
    }
  
    displayedColumns: string[] = ['select', 'voucherId', 'Doc', 'customerFullName','voucherDate','contractNumber', 'voucherAmount', 'phase','storeName' , 'modeOfPayment', 'voucherStatus'];
    dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
    selection = new SelectionModel<any>(true, []);
    
  
    /** Whether the number of selected elements matches the total number of rows. */
    isAllSelected() {
      const numSelected = this.selection.selected.length;
      const numRows = this.dataSource.data.length;
      return numSelected === numRows;
    }
  
    /** Selects all rows if they are not all selected; otherwise clear selection. */
    toggleAllRows() {
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
      return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.usercode + 1}`;
    }
  
    clearselection(row: any) {
      this.selection.clear();
      this.selection.toggle(row);
    }
  
  
    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;
  
    
    ngAfterViewInit() {
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }

    
    materialMaster(data: any = 'new'): void {
      let paramdata = "";
      paramdata = this.cs.encrypt({pageflow: data });
      this.router.navigate(['/main/materialmasters/material-new/' + paramdata]);
    }




    getAll() {
      this.spin.show();
      this.sub.add(this.service.Getall().subscribe((res: any[]) => {
        console.log(res)
        this.dataSource = new MatTableDataSource<any>(res);
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
       this.dataSource.paginator = this.paginator;
        this.spin.hide();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
  
  
    openDialog(data: any = 'New'): void {
      if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.warning("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
      const dialogRef = this.dialog.open(PaymentNewComponent, {
        disableClose: true,
       // width: '55%',
        maxWidth: '80%',
        data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].voucherId : null}
      });
  
      dialogRef.afterClosed().subscribe(result => {
         this.search();
      });
    }
  
    deleteDialog() {
      if (this.selection.selected.length === 0) {
        this.toastr.warning("Kindly select any Row", "Notification",{
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
      const dialogRef = this.dialog.open(DeleteComponent, {
        disableClose: true,
        width: '40%',
        maxWidth: '80%',
        position: { top: '9%', },
        data: this.selection.selected[0].voucherId,
      });
  
      dialogRef.afterClosed().subscribe(result => {
  
        if (result) {
          this.deleterecord(this.selection.selected[0].voucherId);
  
        }
      });
    }
    deleterecord(id: any) {
      this.spin.show();
      this.sub.add(this.service.Delete(id).subscribe((res) => {
        this.toastr.success(id + " Deleted successfully.","Notification",{
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.search();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }
     applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
  
  downloadexcel() {
      var res: any = [];
      this.dataSource.data.forEach(x => {
        res.push({
          "Voucher No": x.voucherId,
          "Customer Name":x.customerFullName,
          "VoucherDate":this.cs.dateapiutc0(x.voucherDate),
          "Agreement No": x.contractNumber,
          "Amount Paid": x.voucherAmount,
          "Payment Mode": x.modeOfPayment,
          "Status":x.voucherStatus,
          
        
        });
  
      })
      this.cs.exportAsExcel(res, "Payment");
    }
    multiselectvoucherIdList: any[] = [];
    multiselectmodeOfPaymentList: any[] =[];
    searhform = this.fb.group({
      contractNumber: [],
      customerName: [],
      endDate: [],
      isActive: [],
      modeOfPayment: [],
      serviceType: [],
      startDate: [],
      storeNumber: [],
      voucherId: [],
      voucherStatus: [],
   });
   search(){
    this.spin.show();
    this.service.search(this.searhform.value).subscribe(res => {
      res.forEach((x) => this.multiselectvoucherIdList.push({value: x.voucherId, label: x.voucherId}));
      this.multiselectvoucherIdList = this.cs.removeDuplicatesFromArrayNew(this.multiselectvoucherIdList);
   
      res.forEach((x) => this.multiselectmodeOfPaymentList.push({value: x.modeOfPayment, label: x.modeOfPayment}));
      this.multiselectmodeOfPaymentList = this.cs.removeDuplicatesFromArrayNew(this.multiselectmodeOfPaymentList);
      res.forEach(element => {
        element['customerFullName'] =  this.customerName.find(y => y.value == element.customerName)?.label;
        element['storageName'] =  this.storageName.find(y => y.value == element.storeNumber)?.label;
      });
      this.dataSource = new MatTableDataSource<any>(res);
      this.selection = new SelectionModel<any>(true, []);
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
      this.spin.hide();
    }, err => {

      this.cs.commonerror(err);
      this.spin.hide();

    });
  }

  resetSearch(){
    this.searhform.reset();
  }
  send_date=new Date();
  formattedDate : any;
  storeNumberList : any
  generatePdf(res) {
    this.agreementService.search({agreementNumber: [res.contractNumber]}).subscribe(result1 => {
      if(result1.length > 0){
        this.storeNumberList = result1[0].referenceField4
      }
 
    let splitAmount = res.voucherAmount.split('.');  
    let kwdDecimal = splitAmount[0];
    let filsDecimal = splitAmount[1] ? splitAmount[1].padEnd(3, '0') : '000';
    this.spin.show();
      //Receipt List
        var dd: any;
        let headerTable: any[] = [];
        
        headerTable.push([
          { image: logo.unlinkLogo, width:'520', margin:[280, 0, 0, 0], alignment:'center', bold: false, fontSize: 13, border: [false, false, false, false] },
        // { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },
     //    { text: 'Monty & Ramirez LLP \n 150 W Parker Road, 3rd Floor Houston, TX 77076',  alignment: 'center', fontSize: 13, border: [false, false, false, false] },
          { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },
          { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },
        ]);
       
        dd = {
          pageSize: "A4",
          pageOrientation: "Landscape",
          pageMargins: [40, 95, 40, 60],
          header(currentPage: number, pageCount: number, pageSize: any): any {
            return [
              {
                table: {
                  // layout: 'noBorders', // optional
                  // heights: [,60,], // height for each row
                  headerRows: 1,
                  widths: ['*', 200, '*'],
                  body: headerTable
                },
                margin: [20, 0, 20, 20]
              }
            ]
          },
          styles: {
            anotherStyle: {
              bordercolor: '#6102D3'
            }
          },
          
          footer(currentPage: number, pageCount: number, pageSize: any): any {
            return [
            //   {
            //   text: 'Page ' + currentPage + ' of ' + pageCount,
            //   style: 'header',
            //   alignment: 'center',
            //   bold: false,
            //   fontSize: 6
            // }
            { image: logo.footerLogo, width: 800,height:140,  bold: false, margin: [20, -75, 0, 20], fontSize: 13, border: [false, false, false, false] },
          ]
          },
          content: ['\n'],
          defaultStyle
        };

        let terms225Array: any[] = [];
        terms225Array.push([
          { text: '', bold: false, fontSize: 14, border: [false, false, false, false] },
          { text: '', bold: false, fontSize: 14, border: [false, false, false, false] },
          { text: '', bold: false, fontSize: 14, border: [false, false, false, false] },
          { text: ' Voucher No:' + '   ' + (res.voucherId), bold: true, alignment:'right', fontSize: 14, border: [false, false, false, false],color:'red' },
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [80, 80, 370, 200],
              body: terms225Array
            },
            margin: [0, 40, 0, 5]
          }
        )
        let termsArray: any[] = [];
        termsArray.push([
          { text: 'KWD'+ '   '+'دينار كويتي'.split(" ").reverse().join(" "), bold: false, fontSize: 14, border: [false, false, false, false] },
          { text: 'Fils'+ '   '+'فلس'.split(" ").reverse().join(" "), bold: false, fontSize: 14, border: [false, false, false, false] },
          { text: '', bold: false, fontSize: 14, border: [false, false, false, false] },
          { text: 'Agreement No:' + '   ' + (res.contractNumber), bold: true, alignment:'right', fontSize: 14, border: [false, false, false, false],color:'red' },
         
        ]);
        termsArray.push([
          { text: kwdDecimal, bold: false, fontSize: 14, border: [true, true, true, true] },
          { text: filsDecimal, bold: false, fontSize: 14, border: [true, true, true, true] },
          { text: '', bold: false, fontSize: 14, border: [false, false, false, false] },
          { text: 'Date  '+ this.datePipe.transform(res.voucherDate, 'd MMM, y', 'GMT') +'التاريخ :'.split(" ").reverse().join(" "),  alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
         
        ]);
        
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [80, 80, 400, 170],
              body: termsArray
            },
            margin: [0, -18, 0, 5]
          }
        )

        
        let bodyArray: any[] = [];
        bodyArray.push([
          { text: 'Received from Mr./Mrs./Mssrs:', bold: false,alignment:'left', fontSize: 14, border: [false, false, false, false] },
          { text: (res.customerFullName), bold: false, alignment:'left', fontSize: 14,border: [false, false, false, true], borderColor: ['', '', '', '#808080']},
          { text: 'استلمنا من السيد / السيدة :'.split(" ").reverse().join(" "), bold: false,alignment:'right',  fontSize: 14, border: [false, false, false, false] },
         
        ]);
        
        const toWords = new ToWords();
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [141,'*',102],
              body: bodyArray
            },
            margin: [0, 0, 0, 0]
          }
        )
        let words = toWords.convert(Number(res.voucherAmount));
        let bodyArray4: any[] = [];
        bodyArray4.push([
          { text: 'The SUM of KWD:', bold: false,alignment:'left', fontSize: 14, margin: [0, 20, 0, 0], border: [false, false, false, false] },
          { text: res.voucherAmount != null ? words + ' KWD' : ' - ', bold: false, alignment:'left', margin: [0, 20, 0, 0], fontSize: 14, border: [false, false, false, true],borderColor: ['', '', '', '#808080'] },
          { text:' مبلغ وقدره د.ك :'.split(" ").reverse().join(" "), bold: false,alignment:'right', margin: [0, 20, 0, 0], fontSize: 14, border: [false, false, false, false] },
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [88,'*',64],
              body: bodyArray4
            },
            margin: [0, -10, 0, 10]
          }
        )
        
       
        let body1Array: any[] = [];
        body1Array.push([
          { text: 'Bank:', bold: false,alignment:'left', fontSize: 14, border: [false, false, false, false] },
          { text: (res.bank), bold: false, alignment:'left', fontSize: 14, border: [false, false, false, true],borderColor: ['', '', '', '#808080'] },
          { text: 'بنك :'.split(" ").reverse().join(" "), bold: false,alignment:'right', fontSize: 14, border: [false, false, false, false] },
          { text: '', bold: false,alignment:'left', fontSize: 14, border: [false, false, false, false] },
          { text: 'Payment Mode:', bold: false,alignment:'left', fontSize: 14, border: [false, false, false, false] },
          { text: (res.modeOfPayment), bold: false, alignment:'left', fontSize: 14, border: [false, false, false, true],borderColor: ['', '', '', '#808080'] },
          { text: 'طريقة الدفع :'.split(" ").reverse().join(" ") ,bold: false,alignment:'right', fontSize: 14, border: [false, false, false, false] },
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [27, 122, 25, 10, 69 , '*',52],
              body: body1Array
            },
            margin: [0, 0, 0, 10]
          }
        )
        let body2Array: any[] = [];

        if(res.period == "WEEKLY"){
          let courtDate: Date = new Date(res.voucherDate);
         this.periodDate = new Date();
         this.periodDate.setDate(courtDate.getDate() + 6);
        }
        
if(res.period == "MONTHLY"){
  this.oneDateBefore = moment(new Date(res.voucherDate)).add(1,'M');
  this.periodDate = moment(new Date(this.oneDateBefore._d)).subtract(1,'d');
}
if(res.period == "QUARTERLY"){
  this.oneDateBefore = moment(new Date(res.voucherDate)).add(3,'M');
  this.periodDate = moment(new Date(this.oneDateBefore._d)).subtract(1,'d');
}
if(res.period == "HALFYEARLY"){
  this.oneDateBefore = moment(new Date(res.voucherDate)).add(6,'M');
  this.periodDate = moment(new Date(this.oneDateBefore._d)).subtract(1,'d');
}
if(res.period == "YEARLY"){
  this.oneDateBefore = moment(new Date(res.voucherDate)).add(12,'M');
  this.periodDate = moment(new Date(this.oneDateBefore._d)).subtract(1,'d');
}

        body2Array.push([
          { text: 'Being:', bold: false,alignment:'left', fontSize: 14, border: [false, false, false, false] },
          { text: (res.storeName != null ? res.storeName : '') + (res.period != null ?  ' (' + (this.datePipe.transform(res.startDate, 'd MMM, y','GMT') + ' To ' + this.datePipe.transform(res.endDate, 'd MMM, y','GMT') + ')') : 'No-Data'), bold: false, alignment:'left', fontSize: 14, border: [false, false, false, true],borderColor: ['', '', '', '#808080'] },
          { text: 'وذلك عن :'.split(" ").reverse().join(" "), bold: false,alignment:'right', fontSize: 14, border: [false, false, false, false] },
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [33,'*',47],
              body: body2Array
            },
            margin: [0, 0, 0, 20]
          }
        )
        let body3Array: any[] = [];

      

        body3Array.push([
          { text: 'Receiver Sign:', bold: false,alignment:'left', fontSize: 14, border: [false, false, false, false] },
          { text: (res.createdBy), bold: false, alignment:'left', fontSize: 14, border: [false, false, false, true], borderColor: ['', '', '', '#808080']},
          { text:'توقيع المستلم :'.split(" ").reverse().join(" "), bold: false,alignment:'right', fontSize: 14, border: [false, false, false, false] },
          { text: '', bold: false,alignment:'left', fontSize: 14, border: [false, false, false, false] },
          { text: '', bold: false,alignment:'left', fontSize: 14, border: [false, false, false, false] },
          { text: '', bold: false, alignment:'center', fontSize: 14, border: [false, false, false, false],},
          { text:''  ,bold: false,alignment:'right', fontSize: 14, border: [false, false, false, false] },
         
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [65,60,60,20,70,'*',70],
              body: body3Array
            },
            margin: [0, 0, 0, 13]
          }
        )


      //  pdfMake.createPdf(dd).download('Payment Receipt -' + res.voucherId);
        pdfMake.createPdf(dd).open();
   
      this.spin.hide();
    });
  }

  }
  
  
  
  
  
  