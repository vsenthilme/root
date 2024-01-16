
  import { SelectionModel } from '@angular/cdk/collections';
  import { Component, Injectable, OnInit, ViewChild } from '@angular/core';
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
import { InvoiceNewComponent } from './invoice-new/invoice-new.component';
import { InvoiceService } from './invoice.service';

import { ToWords } from 'to-words';
  
  // import the pdfmake library
  import pdfMake from "pdfmake/build/pdfmake";
  // importing the fonts and icons needed
  import pdfFonts from "../../../../../assets/font/vfs_fonts";
  import { defaultStyle } from "../../../../../app/config/customStyles";
  import { fonts } from "../../../../../app/config/pdfFonts";
  import { HttpRequest, HttpResponse } from "@angular/common/http";
  import { workOrderLogo1 } from "../../../../../assets/font/workorder-logo1.js";
import { DecimalPipe, DatePipe } from '@angular/common';
import { CommonApiService } from 'src/app/common-service/common-api.service';
import { StorageunitService } from 'src/app/main-module/Masters -1/material-master/storage-unit/storageunit.service';
import { AgreementService } from '../agreement/agreement.service';
  // PDFMAKE fonts
  pdfMake.vfs = pdfFonts.pdfMake.vfs;
  pdfMake.fonts = fonts;




  
  export interface PeriodicElement {
    usercode: string;
    name: string;
    admin: string;
    role: string;
    userid: string;
    password: string;
    status: string;
    email: string;
  }
  
  const ELEMENT_DATA: PeriodicElement[] = [
    {usercode: "test", name: 'test', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test'},
  ];
  @Component({
    selector: 'app-invoice',
    templateUrl: './invoice.component.html',
    styleUrls: ['./invoice.component.scss']
  })
  export class InvoiceComponent implements OnInit {
    singleParticulars: string;
  
  
    constructor(private router: Router,
      private fb: FormBuilder,
      public toastr: ToastrService,
      private cs: CommonService,
      private spin: NgxSpinnerService,
      private auth: AuthService,
      public dialog: MatDialog,
      private route: ActivatedRoute,
      private service: InvoiceService,
      private agreementService: AgreementService,
      private cas: CommonApiService,
      private decimalPipe: DecimalPipe,
      public storageService: StorageunitService,
      public datePipe: DatePipe
    ) { }

    sub = new Subscription();
    ELEMENT_DATA: any[] = [];
    customerName: any[] = [];
    storageName: any[] = [];
    ngOnInit() {

      this.cas.getalldropdownlist([
        this.cas.dropdownlist.trans.customerDropdown.url,
        this.cas.dropdownlist.trans.storageDropdown.url,
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
    }
  
    displayedColumns: string[] = ['select', 'invoiceNumber','customerFullName', 'Doc',  'invoiceCurrency', 'invoiceAmount', 'invoiceDiscount', 'totalAfterDiscount'];
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
    const dialogRef = this.dialog.open(InvoiceNewComponent, {
      disableClose: true,
   //   width: '55%',
      maxWidth: '80%',
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].invoiceNumber : null}
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
      data: this.selection.selected[0].invoiceNumber,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].invoiceNumber);

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
        "Invoice No	": x.invoiceNumber,
        "Currency": x.invoiceCurrency,
        "Amount": x.invoiceAmount,
        "Discount if Any": x.invoiceDiscount,
        "Total After Discount": x.totalAfterDiscount,
      
      });

    })
    this.cs.exportAsExcel(res, "Invoice");
  }

  multiselectinvoiceNumberList: any[] = [];
  searhform = this.fb.group({
    documentNumber:  [],
    customerId:  [],
    endDate:  [],
    invoiceNumber: [],
    isActive:  [],
    startDate:  [],
 });
 search(){
  this.service.search(this.searhform.value).subscribe(res => {
    this.spin.hide();
    res.forEach((x) => this.multiselectinvoiceNumberList.push({value: x.invoiceNumber, label: x.invoiceNumber}));
    this.multiselectinvoiceNumberList = this.cs.removeDuplicatesFromArrayNew(this.multiselectinvoiceNumberList);

    res.forEach(element => {
      element['customerFullName'] =  this.customerName.find(y => y.value == element.customerId)?.label;
    });
    this.dataSource = new MatTableDataSource<any>(res);
    this.selection = new SelectionModel<any>(true, []);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }, err => {

    this.cs.commonerror(err);
    this.spin.hide();

  });
}

resetSearch(){
  this.searhform.reset();
}

storageNumberList: any [] = [];
particulars: any [] = [];
finalParticulars: any [] = [];
storeNumber: any[] = [];
storeNumbersFilter: any;
pdfValidation(res){
  this.finalStoreNumber = [];
  this.spin.show();
  this.storageNumberList = [];
  this.finalParticulars = [];
  if(res.referenceField2 == "Agreement" && res.documentNumber){
    this.agreementService.search({agreementNumber: [res.documentNumber]}).subscribe(result1 => {
      
    this.storeNumbersFilter = result1[0].referenceField4
if(result1.length > 0){
  result1[0].storeNumbers.forEach(element => {
    this.storageNumberList.push(element.storeNumber);
  });
 console.log(this.storeNumbersFilter)
 this.storageNumberList.forEach(id => {
  this.storageName.forEach(name => {
if(name.value == id){
  this.finalStoreNumber.push(name.label)
}
  })
});
this.finalStoreNumber1 = this.finalStoreNumber.map(element=>element).join(",");


  this.storeNumber = this.storageNumberList
this.storageService.search({itemCode: this.storageNumberList}).subscribe(result => {
  this.storageNumberList = [];
  result.forEach(element => {
    if(element.storageType != null && element.storageType != "")
  this.particulars.push({ value: element.storageType})
  });
this.finalParticulars = this.cs.removeDuplicatesFromArrayNew1(this.particulars);
this.singleParticulars=this.finalParticulars.map(element=>element).join(",");
this.generatePdf(res, true)
})
}else{
  this.spin.hide();
  this.toastr.warning("No data available", "Pdf Download", {
    timeOut: 2000,
    progressBar: false,
  });
}
    });
  }else{
    this.generatePdf(res, false)
  }
}

finalStoreNumber: any[] = [];
finalStoreNumber1: any
generatePdf(res, agreement) {


 // res.totalAfterDiscount
    //Receipt List
      var dd: any;
      let headerTable: any[] = [];
      
      headerTable.push([
       { image: workOrderLogo1.headerLogo1, fit: [220, 220], alignment: 'left',  bold: false, fontSize: 12, border: [false, false, false, false] },
    //   { image: workOrderLogo1.headerLogo1, fit: [280, 280], alignment: 'center',  bold: false, fontSize: 12, border: [false, false, false, false] },
      ]);
     
      dd = {
        pageSize: "A4",
        pageOrientation: "portrait",
        pageMargins: [30, 90, 30, 20],
        header(currentPage: number, pageCount: number, pageSize: any): any {
          return [
            {
              table: {
                // layout: 'noBorders', // optional
                // heights: [,60,], // height for each row
                headerRows: 1,
                widths: ['*'],
                body: headerTable
              },
              margin: [-12, 5, 0, 20]
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
          
       { image: workOrderLogo1.ustorage, width: 570,    bold: false, margin: [10, -80, 0, 0], fontSize: 12, border: [false, false, false, false] },
        ]
        },
        content: ['\n'],
        defaultStyle,
        
      };

      let headerArray: any[] = [];
      headerArray.push([
        { text: '', bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: 'INVOICE' , bold: true, alignment: 'center', fontSize: 25, border: [false, false, false, false],},
        { text: '', bold: false, fontSize: 12, border: [false, false, false, false] },
      ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [140, 250, '*'],
            body: headerArray
          },
          margin: [1, -30, 0, 5]
        }
      )
      let header2Array: any[] = [];
      header2Array.push([
        { text: 'Bill To:', bold: false, fontSize: 16, border: [false, false, false, false],alignment:'left' },
        { text: (res.customerFullName) , bold: false, alignment: 'left', fontSize: 17, border: [false, false, false, false]},
        { text: 'Invoice Date:'+'  '+ this.datePipe.transform(res.invoiceDate, 'd MMM, y','GMT'), bold: false, fontSize: 17, border: [false, false, false, false],alignment:'right' },
      ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [50,'*' , 150],
            body: header2Array
          },
          margin: [1, -5, 0, 5]
        }
      )
      let header3Array: any[] = [];
      header3Array.push([
        { text: '', bold: false, fontSize: 17, border: [false, false, false, false] },
        { text: '' , bold: false, alignment: 'center', fontSize: 17, border: [false, false, false, false]},
        { text: 'Invoice No:'+'  '+ (res.invoiceNumber), bold: false, fontSize: 17, border: [false, false, false, false],alignment:'right' },
      ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [50, 80, '*'],
            body: header3Array
          },
          margin: [1, -2, 0, 5]
        }
      )
      let body1Array: any[] = [];
      body1Array.push([
        { text: 'Project:', bold: false, fontSize: 17, border: [false, false, false, false],alignment:'left' },
        { text: 'Ustorage Self Storage' , bold: false, alignment: 'left', fontSize: 17, border: [false, false, false, false]},
        { text: '', bold: false, fontSize: 16, border: [false, false, false, false],alignment:'right' },
      ]);
      body1Array.push([
        { text: 'Location:', bold: false, fontSize: 17, border: [false, false, false, false],alignment:'left' },
        { text: 'Shuwaikh, Kuwait' , bold: false, alignment: 'left', fontSize: 17, border: [false, false, false, false]},
        { text: '', bold: false, fontSize: 17, border: [false, false, false, false],alignment:'right' },
      ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [50,200 , '*'],
            body: body1Array
          },
          margin: [1, 2, 0, 5]
        }
      )
      let body2Array: any[] = [];
      body2Array.push([
        { text: ' WE DEBIT YOUR ACCOUNT WITH FOLLOWING DETAILED CHARGES :', bold: false, fontSize: 15, border: [false, false, false, false],alignment:'left' },
       
      ]);
     
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [ '*'],
            body: body2Array
          },
          margin: [1, 5, 0, 5]
        }
      )
      

        let body3Array: any[] = [];
        body3Array.push([
          { text: 'PARTI', bold: false, fontSize: 15, border: [true, true, false, false],alignment:'right' },
          { text: 'CULARS', bold: false, fontSize: 15, border: [false, true, false, true],alignment:'left', margin:[-9,0,0,0]},
          { text: 'UNIT' , bold: false, alignment: 'center', fontSize: 15,border: [true, true, true, true] },
          { text: 'QTY', bold: false, fontSize: 15,border: [true, true, true, true] ,alignment:'center' },
          { text: 'UNIT PRICE' , bold: false, alignment: 'center', fontSize: 15,border: [true, true, true, true] },
          { text: 'TOTAL AMOUNT', bold: false, fontSize: 15,border: [true, true, true, true] ,alignment:'center' }
        ]);
 console.log(this.storeNumbersFilter)
        body3Array.push([
          { text: 'Store No: ', bold: false, fontSize: 15, border: [true, true, false, false],alignment:'LEFT',margin:[2,0,0,0] },
          { text: ''+(agreement == true ? this.storeNumbersFilter : this.storeNumbersFilter ), bold: false, fontSize: 15, border: [false, true, false, false],alignment:'left',margin:[-50,0,0,0] },
          { text: (res.unit), bold: false, alignment: 'center', fontSize: 15,border: [true, true, true, false] },
          { text: (res.quantity), bold: false, fontSize: 15,border: [true, true, true, false] ,alignment:'center' },
          { text: (res.monthlyRent != null ? 'KWD ' + this.decimalPipe.transform(res.monthlyRent, '1.3-3') + ' ' + (res.unit != null ? ' per/' + res.unit : '') : '') , bold: false, alignment: 'center', fontSize: 14,border: [true, true, true, false] },
          { text: res.totalAfterDiscount != null ? 'KWD ' + this.decimalPipe.transform(res.totalAfterDiscount, '1.3-3') : '0.000 KWD', bold: false, fontSize: 15,border: [true, true, true, false] ,alignment:'center' }
        ]);
        body3Array.push([
          { text: '   '+'('+this.datePipe.transform(res.documentStartDate, 'd MMM, y',)+'  ', bold: false, fontSize: 13, border: [true, false, false, true],alignment:'left',margin:[2,-7,0,0] },
          { text: 'To'+'  '+this.datePipe.transform(res.documentEndDate, 'd MMM, y',)+')', bold: false, fontSize: 13, border: [false, false, false, true],alignment:'left',margin:[-40,-7,0,0] },
          { text: '', bold: false, alignment: 'center', fontSize: 15,border: [true, false, false, true] },
          { text: '', bold: false, fontSize: 15,border: [true, false, false, true] ,alignment:'center' },
          { text: '' , bold: false, alignment: 'center', fontSize: 15,border: [true, false, false, true] },
          { text: '', bold: false, fontSize: 15,border: [true, false, true, true] ,alignment:'center' }
        ]);
        body3Array.push([
          { text: 'TOTAL INVOICE  ', bold: false, fontSize: 15, border: [true, true, false, true],alignment:'right' },
          { text: 'AMOUNT IN KWD ', bold: false, fontSize: 15, border: [false, true, false, true],alignment:'left',margin:[-7,0,0,0] },
          { text: '' , bold: false, alignment: 'center', fontSize: 15,border: [false, false, false, true] },
          { text: '', bold: false, fontSize: 15,border: [false, false, false, true] ,alignment:'center' },
          { text: '' , bold: false, alignment: 'center', fontSize: 15,border: [false, false, false, true] },
          { text: res.totalAfterDiscount != null ? 'KWD ' + this.decimalPipe.transform(res.totalAfterDiscount, '1.3-3') : '0.000 KWD', bold: false, fontSize: 15,border: [false, true, true, true] ,alignment:'center' }
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [92,105,35,40, 125,90],
              body: body3Array
            },
            margin: [2, 10, 0, 5]
          }
        )
    
 
      let body4Array: any[] = [];
      const toWords = new ToWords();
      let words = toWords.convert(Number(res.totalAfterDiscount));
      body4Array.push([
        { text: 'Amount in Words:   Kuwait Dinar ' + words + ' Only ', bold: false, fontSize: 15, border: [false, false, false, false],alignment:'left' },
       
      ]);
     
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [ '*'],
            body: body4Array
          },
          margin: [1, 10, 0, 5]
        }
      )
if(res.referenceField3 == "Phase II"){
  let body5Array: any[] = [];
  body5Array.push([
    { text: 'BANK DETAILS : ', bold: false, fontSize: 15, border: [true, true, true, true],alignment:'left' },
    { text: '' , bold: false, alignment: 'left', fontSize: 15,border: [true, true, true, true] },
  ]);  
  body5Array.push([
    { text: 'Account No. ', bold: false, fontSize: 15,border: [true, true, true, true] ,alignment:'left' },
    { text: '361010014555' , bold: false, alignment: 'left', fontSize: 15,border: [true, true, true, true] },
  ]); 
  body5Array.push([
    { text: 'Bank', bold: false, fontSize: 15,border: [true, true, true, true] ,alignment:'left' },
    { text: 'KUWAIT FINANCE HOUSE' , bold: false, alignment: 'left', fontSize: 15,border: [true, true, true, true] },
  ]); 
  body5Array.push([
    { text: 'Iban No.', bold: false, fontSize: 15,border: [true, true, true, true] ,alignment:'left' },
    { text: 'KW07KFHO0000000000361010014555' , bold: false, alignment: 'left', fontSize: 15, border: [true, true, true, true]},
  ]);
  body5Array.push([
    { text: 'Swift Code', bold: false, fontSize: 15,border: [true, true, true, true] ,alignment:'left' },
    { text: 'KFHOKWKW' , bold: false, alignment: 'left', fontSize: 15,border: [true, true, true, true] },
  ]);
  dd.content.push(
    {
      table: {
        headerRows: 1,
        widths: [ 150,'*'],
        body: body5Array
      },
      margin: [2, 5, 0, 5]
    }
  ) 
  let body6Array: any[] = [];
  body6Array.push([
    { text: 'Terms:  ', bold: false, fontSize: 15, border: [false, false, false, false],alignment:'left' },
    { text: 'Cheque should be made and crossed in the name of UNILINK GEN TRAD AND CONT CO. WLL , or transfer the total amount to above mentioned bank details. The company is not responsible for any cash settlement without an official receipt.' , bold: false, alignment: 'left', fontSize: 15,border: [false, false, false, false] },
  ]); 
  dd.content.push(
    {
      table: {
        headerRows: 1,
        widths: [ 150,'*'],
        body: body6Array
      },
      margin: [1, 5, 0, 5]
    }
  )
}
else{
  let body5Array: any[] = [];
  body5Array.push([
    { text: 'BANK DETAILS : ', bold: false, fontSize: 15, border: [true, true, true, true],alignment:'left' },
    { text: '' , bold: false, alignment: 'left', fontSize: 15,border: [true, true, true, true] },
  ]);  
  body5Array.push([
    { text: 'Account No. ', bold: false, fontSize: 15,border: [true, true, true, true] ,alignment:'left' },
    { text: '011010161741' , bold: false, alignment: 'left', fontSize: 15,border: [true, true, true, true] },
  ]); 
  body5Array.push([
    { text: 'Bank', bold: false, fontSize: 15,border: [true, true, true, true] ,alignment:'left' },
    { text: 'KUWAIT INTERNATIONAL BANK' , bold: false, alignment: 'left', fontSize: 15,border: [true, true, true, true] },
  ]); 
  body5Array.push([
    { text: 'Iban No.', bold: false, fontSize: 15,border: [true, true, true, true] ,alignment:'left' },
    { text: 'KW04KWIB0000000000011010161741' , bold: false, alignment: 'left', fontSize: 15, border: [true, true, true, true]},
  ]);
  body5Array.push([
    { text: 'Swift Code', bold: false, fontSize: 15,border: [true, true, true, true] ,alignment:'left' },
    { text: 'KWIBKWKW' , bold: false, alignment: 'left', fontSize: 15,border: [true, true, true, true] },
  ]);
  dd.content.push(
    {
      table: {
        headerRows: 1,
        widths: [ 150,'*'],
        body: body5Array
      },
      margin: [1, 5, 0, 5]
    }
  ) 
  let body6Array: any[] = [];
  body6Array.push([
    { text: 'Terms:  ', bold: false, fontSize: 15, border: [false, false, false, false],alignment:'left' },
    { text: 'Cheque should be made and crossed in the name of UNILINK GENERAL TRADING AND CONTRACTING COMPANY , or transfer the total amount to above mentioned bank details. The company is not responsible for any cash settlement without an official receipt.' , bold: false, alignment: 'left', fontSize: 15,border: [false, false, false, false] },
  ]); 



  dd.content.push(
    {
      table: {
        headerRows: 1,
        widths: [ 150,'*'],
        body: body6Array
      },
      margin: [1, 5, 0, 5]
    }
  )
}
      let body7Array: any[] = [];
      body7Array.push([
        { text: 'Employee Name:  ' + res.createdBy, bold: false, fontSize: 17, border: [false, false, false, false],alignment:'left' },
       
      ]); 
      // body7Array.push([
      //   { text: ' Position: ', bold: false, fontSize: 17, border: [false, false, false, false],alignment:'left' },
       
      // ]); 
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*'],
            body: body7Array
          },
          margin: [1, 5, 0, 5]
        }
      ) 
    
      pdfMake.createPdf(dd).download('Invoice - ' + res.invoiceNumber);
     //pdfMake.createPdf(dd).open();
 
    this.spin.hide();
}

}





  
  
