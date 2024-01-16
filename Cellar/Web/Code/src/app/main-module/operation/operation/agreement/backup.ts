
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
import { AgreementNewComponent } from './agreement-new/agreement-new.component';
import { AgreementService } from './agreement.service';
  

  // import the pdfmake library
  import pdfMake from "pdfmake/build/pdfmake";
  // importing the fonts and icons needed
  import pdfFonts from "../../../../../assets/font/vfs_fonts";
  import { defaultStyle } from "../../../../../app/config/customStyles";
  import { fonts } from "../../../../../app/config/pdfFonts";
  import { HttpRequest, HttpResponse } from "@angular/common/http";
  import { agreementLogo } from "../../../../../assets/font/agreement-logo.js";
import { DatePipe, DecimalPipe } from '@angular/common';
import { CommonApiService } from 'src/app/common-service/common-api.service';
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
    paymentref: string;
  }
  
  const ELEMENT_DATA: PeriodicElement[] = [
    {usercode: "test", name: 'test', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test',paymentref: 'test'},
  ];

@Component({
  selector: 'app-agreement',
  templateUrl: './agreement.component.html',
  styleUrls: ['./agreement.component.scss']
})

@Injectable({
  providedIn: 'root'
})

export class AgreementComponent implements OnInit {

 
  constructor(private router: Router,
    private fb: FormBuilder,
    public toastr: ToastrService,
    public cs: CommonService,
    private spin: NgxSpinnerService,
    private auth: AuthService,
    public dialog: MatDialog,
    private route: ActivatedRoute,
    private service: AgreementService,
    private decimalPipe: DecimalPipe,
    public datePipe: DatePipe,
    private cas: CommonApiService,
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

  displayedColumns: string[] = ['select', 'agreementNumber', 'referenceField1', 'Doc', 'startDate','endDate',  'customerName', 'civilIDNumber','status'];
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
    const dialogRef = this.dialog.open(AgreementNewComponent, {
      disableClose: true,
    //  width: '55%',
      maxWidth: '80%',
      data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].agreementNumber : null}
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
      data: this.selection.selected[0].agreementNumber,
    });

    dialogRef.afterClosed().subscribe(result => {

      if (result) {
        this.deleterecord(this.selection.selected[0].agreementNumber);

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
        "Agreement No": x.agreementNumber,
        "From Date": this.cs.dateapiutc0(x.startDate),
        "To Date": this.cs.dateapiutc0(x.endDate),
        "Store ID" : x.storageName,
        "Customer Name": x.customerName,
        "Civil ID": x.civilIDNumber,
        "Reference No": x.password,
        "Last Billed Amount": x.status,
        "	Voucher No": x.email,
        "Payment Ref": x.paymentref,   
      });

    })
    this.cs.exportAsExcel(res, "Agreement");
  }

  multiselectagreementNumberList: any[] = [];
  multiselectstoreNumberList: any[] = [];
  searhform = this.fb.group({
    agreementNumber: [],
    customerName: [],
    isActive: [],
    nationality: [],
    quoteNumber: [],
    storeNumbers: [],
 });

 customerIDList: any[] = [];
 dropdownlist() {
  this.spin.show();
  this.cas.getalldropdownlist([
    this.cas.dropdownlist.trans.customerID.url,
  ]).subscribe((results) => {
    this.customerIDList = this.cas.foreachlist2(results[0], this.cas.dropdownlist.trans.customerID.key);
    this.spin.hide();
  }, (err) => {
    this.toastr.error(err, "");
    this.spin.hide();
  });
}

 search(){

  this.spin.show();
  this.service.search(this.searhform.value).subscribe(res => {
   
    res.forEach((x) => this.multiselectagreementNumberList.push({value: x.agreementNumber, label: x.agreementNumber}));
    this.multiselectagreementNumberList = this.cs.removeDuplicatesFromArrayNew(this.multiselectagreementNumberList);
   
    res.forEach((x) => this.multiselectstoreNumberList.push({value: x.storeNumbers, label: x.storeNumbers}));
    this.multiselectstoreNumberList = this.cs.removeDuplicatesFromArrayNew(this.multiselectstoreNumberList);
    res.forEach(element => {
      element['customerFullName'] =  this.customerName.find(y => y.value == element.customerName)?.label;
      element['storageName'] =  this.storageName.find(y => y.value == element.storeNumbers[0])?.label;
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

generatePdf(res) {
  console.log(res)
  this.spin.show();
    //Receipt List
      var dd: any;
      let headerTable: any[] = [];
      
      headerTable.push([
       { image: agreementLogo.headerLogo2, width:'420', margin:[50, 0, 0, 0], bold: false, fontSize: 13, border: [false, false, false, false] },
      // { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },
   //    { text: 'Monty & Ramirez LLP \n 150 W Parker Road, 3rd Floor Houston, TX 77076',  alignment: 'center', fontSize: 13, border: [false, false, false, false] },
        { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },
        { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
     
      dd = {
        pageSize: "A4",
        pageOrientation: "portrait",
        pageMargins: [25, 95, 25, 60],
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
          { image: agreementLogo.headerLogoFooter, width: 550,  bold: false, margin: [20, -40, 0, 30], fontSize: 13, border: [false, false, false, false] },
        ]
        },
        content: ['\n'],
        defaultStyle
      };


      let termsArray: any[] = [];
      termsArray.push([
        { text: 'Agreement No  '+ res.agreementNumber ,bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: 'الاتفاقية رقم: ', bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: '', bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: 'Date  '+ this.datePipe.transform(res.createdOn, 'd MMM, y',) +'التاريخ '  , bold: false, fontSize: 14, border: [false, false, false, false] },
       
      ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [160,90 , 155, 120],
            body: termsArray
          },
          margin: [0, 0, 0, 10]
        }
      )
      let customerDetails: any[] = [];
      customerDetails.push([
        { text: 'Customer Name',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.customerFullName), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'اسم العميل ', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      customerDetails.push([
        { text: 'Civil ID No',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.civilIDNumber), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text:'رقم البطاقة المدنية ', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      customerDetails.push([
        { text: 'Nationality',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.nationality), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'الجنسية ', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      customerDetails.push([
        { text: 'Address',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.address), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'العنوان ', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      customerDetails.push([
        { text: 'Email',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.email), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'البريد الإلكتروني', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [80, '*', 80],
            body: customerDetails
          },
          margin: [0, 0, 0, 10]
        }
      )
      let customerDetails1: any[] = [];
      customerDetails1.push([
        { text: 'Fax No: ',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.faxNumber), borderColor: ['', '', '', '#ddd'],bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'رقم الفاكس ', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
        
        { text: 'Mobile No: ',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.phoneNumber),borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'رقم الموبايل', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
        
        { text: 'Phone No:' ,bold: false, fontSize: 13, border: [false, false, false, false] },
        { text: (res.secondaryNumber), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 13, border: [false, false, false, true] },
        { text: 'رقم الهاتف ', alignment:'right', bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [35, 50, 50, 50, 55, 50, 50, 55, '*'],
            body: customerDetails1
          },
          margin: [0, 5, 0, 5]
        }
      )
      let description: any[] = [];
      description.push([
        { text: '1. ' + (res.itemsToBeStored1 != null ? res.itemsToBeStored1 : ''), bold: false, fontSize: 14, border: [true, true, true, true] },
        { text: '2. ' + (res.itemsToBeStored2 != null ? res.itemsToBeStored2 : ''), bold: false, fontSize: 14, border: [true, true, true, true] },
        { text: '3. ' + (res.itemsToBeStored3 != null ? res.itemsToBeStored3 : ''), bold: false, fontSize: 14, border: [true, true, true, true] },
        { text: '4. ' + (res.itemsToBeStored4 != null ? res.itemsToBeStored4 : ''),  bold: false, fontSize: 14, border: [true, true, true, true] },
      ]);;
      description.push([
        { text: '5. ' + (res.itemsToBeStored5 != null ? res.itemsToBeStored5 : ''),bold: false, fontSize: 14, border: [true, true, true, true] },
        { text: '6. ' + (res.itemsToBeStored6 != null ? res.itemsToBeStored6 : ''), bold: false, fontSize: 14, border: [true, true, true, true] },
        { text: '7. ' + (res.itemsToBeStored7 != null ? res.itemsToBeStored7 : ''), bold: false, fontSize: 14, border: [true, true, true, true] },
        { text: '8. ' + (res.itemsToBeStored8 != null ? res.itemsToBeStored8 : ''),  bold: false, fontSize: 14, border: [true, true, true, true] },
      ]);;
      description.push([
        { text: '9. ' + (res.itemsToBeStored9 != null ? res.itemsToBeStored9 : ''),bold: false, fontSize: 14, border: [true, true, true, true] },
        { text: '10. ' + (res.itemsToBeStored10 != null ? res.itemsToBeStored10 : ''), bold: false, fontSize: 14, border: [true, true, true, true] },
        { text: '11. ' + (res.itemsToBeStored11 != null ? res.itemsToBeStored11 : ''), bold: false, fontSize: 14, border: [true, true, true, true] },
        { text: '12. ' + (res.itemsToBeStored12 != null ? res.itemsToBeStored12 : ''),  bold: false, fontSize: 14, border: [true, true, true, true] },
      ]);
      dd.content.push(
        '\n',
        {
          text: 'Descriptions of items to be stored',
          fontSize: 14,
          bold: true, 
        },
        '\n',
            {
          table: {
            headerRows: 1,
            widths: ['*', '*', '*', '*',],
            body: description
          },
          margin: [0, -15, 0, 10]
        },
        '\n'
      )


      let rentDetails: any[] = [];
      rentDetails.push([
        { text: 'Rent Period ',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.rentPeriod), borderColor: ['', '', '', '#ddd'],bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'فترة الايجار ', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },

        { text: '', bold: false, fontSize: 14, border: [false, false, false, false] },

        { text: 'Room Size: ',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.size),borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'حجم الغرفة ', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
        

      ]);
      rentDetails.push([
        { text: 'Rent Value',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.rent), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'قيمة الإيجار', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
        
        { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },

        { text: 'Type ',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.agreementType), borderColor: ['', '', '', '#ddd'],bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'النوع', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
        
   
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [80, 90, 60, 20, 80, 90, '*'],
            body: rentDetails
          },
          margin: [0, -10, 0, 10]
        }
      )
      
      let rentDetails2: any[] = [];
      rentDetails2.push([
        { text: 'Store No',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.storageName), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'رقم المتجر', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);

      rentDetails2.push([
  

        { text: 'Room No:' ,bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.storeNumbers.length > 0 ? res.storeNumbers[0].storeNumber : '') ,borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'رقم الغرفة ', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },

      ]);
      rentDetails2.push([
        { text: 'Deposit',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.deposit), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'مقدم ', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      rentDetails2.push([
        { text: 'Insurance',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.insurance), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'تأمين ', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      rentDetails2.push([
        { text: 'Others',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.others), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'أخرى', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [90, '*', 60],
            body: rentDetails2
          },
          margin: [0, 0, 0, 10]
        }
      )

      let paymentMethod: any[] = [];
      paymentMethod.push([
        { text: 'Payment Method',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.paymentTerms), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'طريقة السداد', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: 'Total Amount',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text:  res.totalAfterDiscount != null ? this.decimalPipe.transform(res.totalAfterDiscount, "1.2-2") + ' KWD' : '0.000 KWD', borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'المجموع ', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      paymentMethod.push([
        { text: 'Start Date',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: this.datePipe.transform(res.startDate, 'dd-MM-yyyy'), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'تاريخ البداية ', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },

        { text: 'Ending Date',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: this.datePipe.transform(res.endDate, 'dd-MM-yyyy'), borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'تاريخ النهاية', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      paymentMethod.push([
        { text: 'Co.Rep.Signature',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: '', borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'توقيع الموظف', alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: 'Client Signature',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: '', borderColor: ['', '', '', '#ddd'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'توقيع العميل ', alignment:'right', bold: false,  fontSize: 14, border: [false, false, false, false] },
      ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [80, 95, 80, 80, 80,'*',],
            body: paymentMethod
          },
          margin: [0, 0, 0, 10]
        }
      )
      
 

      let paymenMethod: any[] = [];
      paymenMethod.push([
        { text: ' ',bold: false, fontSize: 16, border: [false, false, false, false] },
      
     
        { text: '   ', alignment:'right', bold: false, fontSize: 16, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*','*'],
            body: paymenMethod
          },
          margin: [0, 1000, 0, 0]
        }
      )


      let payment6Method: any[] = [];
      payment6Method.push([
        { text: ' Unilink General Trading and Cont. Company',bold: false, fontSize: 16, border: [false, false, false, false] },
      
     
        { text: 'ركة يوني لينك للتجارة العامة والمقاولات ', alignment:'right', bold: false, fontSize: 16, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [500,500],
            body: payment6Method
          },
          margin: [0, 0, 10, 0]
        }
      )
      let payment1Method: any[] = [];
  
       payment1Method.push([
        { text: 'This Agreement and contract were made in Kuwait between : , ',bold: false, fontSize: 13, border: [false, false, false, false] },
      
     
        { text: '  تم الإتفاق والتعاقد في الكويت بين كل من: ', alignment:'right', bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
      payment1Method.push([
        { text: 'First Party: Unilink General Trading and Contracting Company, its address: Shuwaikh Second Industrial Are, Bank St. Landlord : Saqran No. 64a',bold: false, fontSize: 13, border: [false, false, false, false] },
      
     
        { text: 'الطرف الأول: شركة يوني لينك للتجارة العامة والمقاولات – عنوانها: شويخ الصناعية الثانية – شارع البنوك - قسيمة الصقران رقم 6  ', alignment:'right', bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
      payment1Method.push([
        { text: 'Second party : Is the tenant whose details are given in the firstpage  ',bold: false, fontSize: 13, border: [false, false, false, false] },
      
     
        { text: 'الطرف الثاني: وهو المستأجر وبياناته المذكورة في الصفحة الأولى.' , alignment:'right', bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*','*'],
            body: payment1Method
          },
          margin: [0, 0, 10, 0]
        }
      )
      let payment2Method: any[] = [];
      payment2Method.push([
        { text: 'Preamble ',bold: false, fontSize: 13, border: [false, false, false, false] },
      
     
        { text: ' مهيد', alignment:'right', bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);

       payment2Method.push([
        { text: 'As the first party owns the right of usufruct and exploitation of the aforementioned building, which is in the  form of warehouses suitable for self-storage, and the second party has expressed its desire to lease the warehouse with the specifications mentioned above. Once signed by the two parties, this contract is considered binding on both of them with the conditions stipulated in this contract and all the details set out therein, and the conditions agreed upon between the two parties, and the first party shall be responsible for providing storage and monitoring services and other things that are agreed upon, and the Second party shall be obliged to pay the rental value agreed upon in this contract and he is obligated to pay it on the first of every month in advance . ',bold: false, fontSize: 13, border: [false, false, false, false] },
      
     
        { text: 'حيث أن الطرف الأول يمتلك حق الإنتفاع والإستغلال للمبنى المذكور، وهي على شكل مخازن صالحة للتخزين الذاتي، والطرف الثاني قد أبدى رغبته في  استئجار المخزن بالمواصفات المذكورة أعلاه، فيعتبر هذا العقد بمجرد توقيعه من الطرفين ملزماً لكليهما بالشروط المنصوص عليها بهذا العقد وكافة  التفاصيل المبينة فيه، والشروط المتفق عليها بين الطرفين، ويكون على عاتق الطرف الأول تقديم خدمات التخزين والمراقبة وغيره مما يتم الإتفاق عليه،  وعلى الطرف الثاني سداد القيمة الإيجارية المتفق عليها في هذا العقد، ويلتزم بسدادها في الأول من كل شهر مقدماَ.  ', alignment:'right', bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
     
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*','*'],
            body: payment2Method
          },
          margin: [0, 0, 10, 0]
        }
      )
      let payment3Method: any[] = [];
      payment3Method.push([
        { text: '1. The foregoing preamble is considered an integral part of this contract and is the basis of the contract. eamble ',bold: false, fontSize: 13, border: [false, false, false, false] },
      
     
        { text: ' مهيد', alignment:'right', bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*','*'],
            body: payment3Method
          },
          margin: [0, 0, 10, 0]
        }
      )
      let payment7Method: any[] = [];
      payment7Method.push([
        { text: '2. The rental value ',bold: false, fontSize: 13, border: [false, false, false, false] },
      
     
        { text: '2 .القيمة الإيجارية' ,alignment:'right', bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
      payment7Method.push([
        { text: 'The two parties have agreed on the rental value of the contract, which is mentioned in the first page - and it will be unchanged for the duration of the agreed contract period and First party has the right for the party The first party has the right after the expiry of the contract term and the second partys desire to renew it , to increase the rental value according to what it deems appropriate, and the second party has the right to accept or reject the rent . In the event that the second party rejects the new rent, he must vacate the property at the end of the specified period, otherwise he is considered usurping the leased property and first party has the right to evicting him from it on an urgent lawsuit , and the rent from the date of expiry of the contract until the date of eviction and delivery is double the rent mentioned in the preamble clause of the contract and it shall not be considered as a penalty clause, but rather it is an obligation accepted by the second party and it applies to him without any dispute from him .',bold: false, fontSize: 13, border: [false, false, false, false] },
      
     
        { text: 'اتفق الطرفان على القيمة الإيجارية للعقد هي الواردة فيه – الصفحة الأولى وتكون دون تغيير لطوال مدة العقد المتفق عليها وللطرف الأول بعد انتهاء مدة العقد ورغبة الطرف الثاني في تجديده أن يزيد القيمة الايجارية وفق ما يراه مناسبا وللطرف الثاني الحق في قبول أو رفض الأجرة الجديدة، وفي حالة رفض الطرف الثاني للأجرة الجديدة عليه أن يخلي العين في نهاية المدة المحددة وإلا يعد غاصبا للعين المؤجرة يحق للطرف الأول إخلائه منها بدعوى مستعجلة وتكون الأجرة من تاريخ انتهاء العقد حتى تاريخ الإخلاء والتسليم ضعف الأجرة الواردة ببند التمهيد من العقد ولا يعد ذلك من قبيل الشرط الجزائي وإنما هو التزام ارتضاه الطرف الثاني ويسري في حقه دون منازعة منه' ,alignment:'right', bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*','*'],
            body: payment7Method
          },
          margin: [0, 0, 10, 0]
        }
      )
      let payment8Method: any[] = [];
      payment8Method.push([
        { text: '3. Renewal of the contract',bold: false, fontSize: 13, border: [false, false, false, false] },
      
     
        { text:  '4. الإيداع ' ,alignment:'right', bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
      payment8Method.push([
        { text:'The renewal of the contract is automatic unless one of the parties notifies the other of its desire to terminate the contract,at least one week before the expiry of the contract period from the date specified in contract front page.In the event that the contract term is more than a month, a notice must be given for a period of no less than three weeks before the expiry of the contract term, and the notice shall be by electronic letter to the email of the other party and registered on the top of the contract, and the notification by this means is considered productive from the date of its sending to the other party and Second party’s exceeding of the period specified in the contract without notifying the termination or renewal shall be considered as an act of usurpation that will result in doubling the rent mentioned in the contract  until the date of evacuation and delivery by the Second party and the second party’s transfer of his goods from the property and leaving it without delivery to the first party is not deemed to have vacated the property, but rather he must either formally vacate it and handing over the key to the first party or obtaining a letter of eviction from the latter, signed by the legal representative of the first party, and no action to the contrary shall be considered and the it is not considered as second party has vacated the property and is obliged to double the estimated rent for the property until the eviction and delivery is complete by not following what is contained in this  clause ',bold: false, fontSize: 13, border: [false, false, false, false] },
      
     
        { text:  'يكون تجديد العقد تلقائيا ما لم يخطر أحد الطرفين الآخر برغبته في إنهاء العقد، قبل انتهاء مدة العقد باسبوع على الأقل من التاريخ المحدد فيالعقد الصفحة الأولى وفي حال كانت مدة العقد أكثر من شهر يتعين الإخطار بمدة لا تقل عن ثلاثة أسابيع قبل انتهاء مدة العقد ويكون الإخطار برسالة إلكترونية على البريد الإلكتروني للطرف الآخر والمسجل بصدر العقد، ويعد الإخطار بهذه الوسيلة منتجا لآثاره من تاريخ إرساله للطرف الآخرويكون تجاوز الطرف الثاني المدة المحددة في العقد دون الإخطار بالانهاء أو التجديد بمثابة غصب للعين يرتب في ذمته ضعف الأجرة الواردة في العقد حتى تاريخ الإخلاء والتسليم ولا يعد نقل الطرف الثاني لبضاعته من العين وتركها بدون تسليم للطرف الأول أنه أخلى العين وإنما يتعين عليه إما إخلائها رسميا وتسليم المفتاح للطرف الأول أو الحصول على كتاب بالإخلاء من الأخير موقع من الممثل القانوني للطرف الأول ولا يعتد بأي إجراء بخلاف ذلك ولا يعد الطرف الثاني قد أخلى العين ويلزم بضعف الأجرة المقدرة للعين حتى تمام الإخلاء والتسليم بعدم اتباع ما ورد في هذا البند' ,alignment:'right', bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
      payment8Method.push([
        { text:'4. Deposit ',bold: false, fontSize: 13, border: [false, false, false, false] },
        { text:'5 .التأمين',bold: false, fontSize: 13, border: [false, false, false, false] },
     
      ]);
      payment8Method.push([
        { text:'The second party is obligated, at the request of the first party, to provide a deposit amount whose value is : KD. …. ( Kuwaiti Dinars………………………) it remains with the first party for the duration of this contract, and it is returned to him at the end of the contract after the leased warehouse is delivered, and the first party has the right to deduct from it what the store has suffered damages or to meet the unpaid rental amounts, and in the event that the amount is insufficient, the second party is  obliged to pay the difference immediately, the customer is responsible for providing the necessary insurance coverage for the goods, and Unilink does not bear the  risk of losing those goods in whole or in part during storage, and the customer may request the first party to arranges insurance coverage on its behalf, and in this case  it is subject to the conditions and exceptions proposed by the first party’s regulation that is presented to the client based on a specific request, the customer is  responsible for paying the insurance subscription for the stored items and the total value checked to date, and the failure to activate what is stated in this clause is not considered as a waiver by the first party of its commitment, and the first party has the right to ask the second party at any time to abide by what was stated in the clause , otherwise the second party is considered as violated the contract, the first party has the right to evacuate him from the property immediately, vacate his belongings, and hand them over to the district police station or the execution department to which the leased property belongs and the second party is obligated to provide the first party with an insurance policy valid for the duration of the contract, whether original or renewed, stipulating that the first party be compensated for the damage he suffers as a result of the damage to the leasehold in case the insured risk is realized ',bold: false, fontSize: 13, border: [false, false, false, false] },
        { text:'يلتزم الطرف الثاني ، بناءً على طلب الطرف الأول ، بتقديم مبلغ وديعة قيمته: دينار كويتي. …. (دينار كويتي ………………………………………مع الطرف الأول طوال مدة هذا العقد ، ويتم إرجاعه إليه في نهاية العقد بعد تسليم المستودع المؤجر ، ويكون للطرف الأول الحق في اقتطاع ما لحق المحل من أضرار أو سداد الإيجارات غير المدفوعة ، وفي حالة عدم كفاية المبلغ يكون الطرف الثاني يلتزم العميل بدفع الفرق على الفور ، ويكون العميل مسؤولاً عن توفير التغطية التأمينية اللازمة للبضائع ، ولا تتحمل Unilink خطر فقدان تلك البضائع كليًا أو جزئيًا أثناء التخزين ، ويجوز للعميل أن يطلب من الطرف الأول ترتيب تغطية تأمينية نيابة عنه ، وفي هذه الحالة يخضع للشروط والاستثناءات التي تقترحها لائحة الطرف الأول والتي يتم تقديمها للعميل بناءً على طلب محدد ، يكون العميل مسؤول عن دفع الاشتراك التأميني للأصناف المخزنة وإجمالي القيمة المحددة حتى تاريخه ، وعدم تفعيل ما ورد في هذا البند ليس كذلك. يعتبر تنازلاً من قبل الطرف الأول عن التزامه ، ويحق للطرف الأول أن يطلب من الطرف الثاني في أي وقت الالتزام بما ورد في الفقرة. وإلا اعتبر الطرف الثاني مخالفاً للعقد فيحق للطرف الأول إخلائه من العقار فوراً وإخلاء متعلقاته ، وتسليمها إلى مركز شرطة المنطقة أو قسم التنفيذ الذي ينتمي إليه العقار المؤجر ويلتزم الطرف الثاني بتقديم الأول الطرف الذي لديه بوليصة تأمين سارية المفعول طوال مدة العقد سواء كانت أصلية أو مجددة تنص على تعويض الطرف الأول عن الضرر الذي تعرض له. يعاني نتيجة الضرر الذي يلحق بعقد الإيجار في حالة تحقق الخطر المؤمن عليه ',bold: false, fontSize: 13, border: [false, false, false, false] },
     
      ]);
      payment8Method.push([
        { text:'5. Purpose of leasing ' ,bold: false, fontSize: 13, border: [false, false, false, false] },
        { text:'6 .الغرض من ايجار المخزن',bold: false, fontSize: 13, border: [false, false, false, false] },
     
      ]);
      payment8Method.push([
        { text:'The purpose of leasing the warehouse is to use it to store the above-mentioned materials, and the second party is not allowed to use the rented warehouse except for the purpose assigned to him or by obtaining the written consent of the first party, otherwise he will bear all the responsibilities resulting from that and in case the Second party violates this condition, the first party has the right to ask the second party to immediately vacate the leasehold and no later than a week from the date of notification of this desire by mail , otherwise, his hand will be considered a usurping hand. The first party has the right to expel him from it on an urgent lawsuit , with the first party preserving the right to recourse against the second party for the necessary compensation for the damage he sustained, if any . ',bold: false, fontSize: 13, border: [false, false, false, false] },
        { text:'الغرض من ايجار المخزن هو استغلاله في تخزين المواد المذكورة أعلاه ولا يجوز للطرف الثاني استعمال المخزن المؤجر إلا للغرض المخصص له أو بحصوله على موافقة الطرف الأول الكتابية وإلا سوف يتحمل كافة المسؤوليات المترتبة على ذلك وفي حالة مخالفة الطرف الثاني لهذا الشرط يحق للطرف الأول أن يطلب من الطرف الثاني إخلاء العين فوراً وفي موعد غايته اسبوع من تاريخ إخطاره بهذه الرغبة على البريد الإلكتروني وإلا تعد يده يد غاصبة يحق للطرف الأول طرده منها بدعوى مستعجلة مع حفظ من الطرف الأول في الرجوع على الطرف الثاني بالتعويضات اللازمة عما أصابه من ضرر في حال وجود ',bold: false, fontSize: 13, border: [false, false, false, false] },
     
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*','*'],
            body: payment8Method
          },
          margin: [0, 0, 30, 0]
        }
      )
      let payment9Method: any[] = [];
      payment9Method.push([
         { text:'7. Delay in the Payment   ' ,bold: false, fontSize: 13, border: [false, false, false, false] },
       { text:'8 .التأخير في السداد',bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
      payment9Method.push([
        { text:'The two parties agreed that in the event of a three-month delay in payment, the first party has the right to seize the movables located in the store in fulfillment of the rental value that is owed and overdue by the second party, the first party has the right to sue the second party to pay all the overdue amounts in case of insufficiency  of assets to cover the amounts owed to the first party, and the first party has the right to sell the goods of the second party by attracting three offers to buy and sell them  up to the highest price and deduction of the amounts owed to him from the price. In the event of insufficient sale proceeds, the first party has the right to recourse to the second party with what remains for him or to obtain ruling on selling assets by public auction and obtaining the proceeds of the sale from the execution department, and the signature of the second party on this contract is an endorsement and authorization from him that the first party has the right to carry out these procedures, and the first party has the right to put a lock on the leased property and detain the stored goods until they are sold by any method it deems appropriate . ' ,bold: false, fontSize: 13, border: [false, false, false, false] },
        { text:'ة اتفق الطرفان أنه في حالة التأخير لمدة ثلاثة أشهر عن السداد، يحق للطرف الأول الحجز على المنقولات الكائنة بالمخزن وفاء الإيجارية المستحقة والمتأخرة على الطرف الثاني، يحق للطرف الأول مقاضاة الطرف الثاني لسداد جميع المستحقات المتأخرة في حالة عدم كفايةالموجودات لتغطية المبالغ المستحقة للطرف الأول، ويحق للطرف الأول بيع بضاعة الطرف الثاني وذلك باستجلاب ثلاثة عروض للشراء وبيعها لأعلى سعر وخصم المبالغ المستحقة له من الثمن وفي حالة عدم كفاية حصيلة البيع يحق للطرف الأول الرجوع على الطرف الثاني بما تبقى له أو الحصول على حكم بيع الموجودات بالمزاد العلني والحصول على حصيلية البيع من إدارة التنفيذ ويعد توقيع الطرف الثاني على هذا العقد بمثابة إقرار وتفويض منهللطرف الأول للقيام بهذه الإجراءات ويحق للطرف الأول أن يضع قفلاً على العين المؤجرة وحبس البضاعة المخزنة لحين بيعها بأي طريق يراه مناسب ' ,bold: false, fontSize: 13, border: [false, false, false, false] },
     ]);
     payment9Method.push([
      { text:' 8. Third party ' ,bold: false, fontSize: 13, border: [false, false, false, false] },
    { text:'9 .طرف ثالث',bold: false, fontSize: 13, border: [false, false, false, false] },
   ]);
   payment9Method.push([
    { text:'The first party is not responsible for closing the warehouse of the second party, nor it is responsible for the entry of a third party who is not authorized to enter the store who has a key of the store. The second party is not entitled to assign the leased warehouse to a third party, and it is not permissible to include a partner with him in the lease contract or to enter Sub-lessee unless after obtaining permission from the first party the lessor, and in case the second party violates this obligation, the first party has the right to terminate the contract and vacate him from the leased property by an urgent eviction lawsuit, and counting his hand on the property as a usurper   ' ,bold: false, fontSize: 13, border: [false, false, false, false] },
  { text:'الطرف الأول غير مسؤول عن غلق المخزن الخاص بالطرف الثاني، كما انه غير مسؤول عن دخول طرف ثالث غير مرخص له بالدخول للمخزن ولديه مفتاح المخزن. لا يحق للطرف الثاني التنازل عن المخزن المؤجر إلى الغير، كما لا يجوز إدخال شريك معه في عقد الإيجار أو إدخال مؤجر من الباطن إلا بعد الحصول على إذن من الطرف الأول المؤجر وفي حالة مخالفة الطرف الثاني لهذا الالتزام يحق للطرف الأول إنهاء العقد وإخلائه من العين المؤجرة بدعوى طرد مستعجلة وتعد يده على العين يد غاصبة.',bold: false, fontSize: 13, border: [false, false, false, false] },
 ]);
 payment9Method.push([
  { text:' 9. Air conditioning malfunctions  ' ,bold: false, fontSize: 13, border: [false, false, false, false] },
{ text:'',bold: false, fontSize: 13, border: [false, false, false, false] },
]);
payment9Method.push([
  { text:' The first party is obligated to repair the malfunctions of the central air conditioning and electrical installations, and the first party is not responsible for the malfunctions resulting from misuse by Second party or any changes he or his subordinates or employees make to the warehouse and in cases of their misuse of central air-conditioning devices or electrical installation in the leasehold resulted in a loss for the first party, the latter has the right to recourse to the second party with these losses and repair faults at his expense. He the Second party – has no right - to argue about that or refuse to pay, otherwise he is considered to be in violation of the terms of this contract and may be vacated from the leased property .  ' ,bold: false, fontSize: 13, border: [false, false, false, false] },
{ text:'',bold: false, fontSize: 13, border: [false, false, false, false] },
]);
payment9Method.push([
  { text:' 10. Termination of the Contract   ' ,bold: false, fontSize: 13, border: [false, false, false, false] },
{ text:'10 .أعطال التكييف',bold: false, fontSize: 13, border: [false, false, false, false] },
]);
payment9Method.push([
  { text:' In the event that the second party expresses his desire to terminate the contract, he undertakes to enable the first party to enter whoever he wishes to inspect the  leased warehouse. Both parties have the right to terminate the contract by notice to the other party at least one week before its expiry date, and the second party is obligated to leave the storage place clean and organized in the same condition it was in when the agreement began and when it violates it, the first party has the right  to charge the second party fees for cleaning and disposal of abandoned goods and delivery of the leasehold shall be in accordance with minutes of receipt and  delivery signed by both parties, and the second party does not consider that he has delivered the leased property except by signing the minutes of receipt from both parties or those on their behalf .  ' ,bold: false, fontSize: 13, border: [false, false, false, false] },
{ text:'يلتزم الطرف الأول بإصلاح أعطال التكييف المركزي والتركيبات الكهربائية، كما أن الطرف الأول ليس مسؤولاً عن الأعطال الناتجةعن سوء استعمال الطرف الثاني أو أي تغيرات يحدثها أو أحد تابعيه أو مستخدميه للمخزن وفي حالة إساءتهم لاستخدام أجهزة التكييف المركزي أوالتركيبات الكهربائية بالعين ونتج عن ذلك خسارة للطرف الأول، يحق للأخير الرجوع على الطرف الثاني بهذه الخسائر وإصلاح الأعطال على حسابه ولايحق له للطرف الثاني المجادلة في ذلك أو الامتناع عن السداد وإلا عد مخالفا لشروط هذا العقد ويجوز إخلائه من العين المؤجرة',bold: false, fontSize: 13, border: [false, false, false, false] },
]);
payment9Method.push([
  { text:' 11. The applicable law   ' ,bold: false, fontSize: 13, border: [false, false, false, false] },
{ text:'11 .إنهاء العقد',bold: false, fontSize: 13, border: [false, false, false, false] },
]);
payment9Method.push([
  { text:' The two parties agreed that all disputes arising from this contract shall be resolved amicably and within a maximum period of two weeks from the date of notification  by either party to the other party in violation or dispute, otherwise the Judicial Arbitration Center at the Ministry of Justice shall be competent to settle the dispute   ' ,bold: false, fontSize: 13, border: [false, false, false, false] },
{ text:'في حالة إبداء الطرف الثاني رغبته بإنهاء العقد، فإنه يتعهد بتمكين الطرف الأول بإدخال من يرغب لمعاينة المخزن المؤجر. وللطرفين أينهيا العقد بإشعار للآخر قبل اسبوع على الأقل من تاريخ انتهائه، ويلتزم الطرف الثاني بترك مكان التخزين نظيف ومنظم بنفس الحالة التي كان عليها عندبدأ الإتفاقية وعند مخالفته لذلك يحق للطرف الأول تحميل الطرف الثاني رسوم النظافة والتخلص من البضائع المهجورة ويكون تسليم العين بموجب محضر استلام وتسليم بتوقيع الطرفين ولا يعد الطرف الثاني أنه قد سلم العين المؤجرة إلا بتوقيع محضر الإستلام من طرفيه أو ممن ينوب عنهم',bold: false, fontSize: 13, border: [false, false, false, false] },]);
payment9Method.push([
  { text:'12. This agreement is made in Arabic and English. In the event of any dispute as to the terms and conditions the Arabic version shall prevail.   ' ,bold: false, fontSize: 13, border: [false, false, false, false] },
{ text:'12 .حرر هذا العقد باللغتين العربية والإنجليزية. في حال وجود أي نزاع بشأن الشروط والأحكام، تعتمد ترجمة اللغة العربية',bold: false, fontSize: 13, border: [false, false, false, false] },
]); 
dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*','*'],
            body: payment9Method
          },
          margin: [0, 0, 30, 0]
        }
      )
     pdfMake.createPdf(dd).download('Agreement- ' + res.agreementNumber);
      //pdfMake.createPdf(dd).open();
 
    this.spin.hide();
}

}



