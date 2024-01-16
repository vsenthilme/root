
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
import { ToWords } from 'to-words';

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
import { ColumnFilter } from 'primeng/table';
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

  displayedColumns: string[] = ['select', 'agreementNumber', 'referenceField4','referenceField6', 'Doc', 'startDate','endDate',  'customerFullName', 'civilIDNumber','status','createdBy','createdOn'];
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
        "Store No":x.referenceField4,
        "Phase":x.location,
        "From Date": this.cs.dateapiutc0(x.startDate),
        "To Date": this.cs.dateapiutc0(x.endDate),
        "Customer Name": x.customerFullName,
        "Civil ID": x.civilIDNumber,
        "Status":x.status,
        "Created By":x.createdBy,
        "Created On":this.cs.dateapiutc0(x.createdOn)
        // "Reference No": x.password,
        // "Last Billed Amount": x.status,
        // "	Voucher No": x.email,
        // "Payment Ref": x.paymentref,   
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
finalStoreNumber: any[] = [];
comaStoreNumber: string;
generatePdf(res) {

  this.finalStoreNumber = [];
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
          if(currentPage ==1){
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
        }
        },
        styles: {
          anotherStyle: {
            bordercolor: '#6102D3'
          }
        },
        
        footer(currentPage: number, pageCount: number, pageSize: any): any {
          if(currentPage ==1){
          return [
          //   {
          //   text: 'Page ' + currentPage + ' of ' + pageCount,
          //   style: 'header',
          //   alignment: 'center',
          //   bold: false,
          //   fontSize: 6.10
          // }
            { image: agreementLogo.headerLogoFooter1, width: 550,  bold: false, margin: [20, -40, 0, 30], fontSize: 13, border: [false, false, false, false] },
        ]
      }
        },
        content: ['\n'],
        defaultStyle
      };

      let termsArray1: any[] = [];
      termsArray1.push([
        { text: 'Agreement No:  '+ res.agreementNumber ,bold: true, fontSize: 14, border: [false, false, false, false],color:'red' },
        { text: '', bold: false, fontSize: 14, border: [false, false, false, false],margin:[-22,0,0,0] },
       
        { text: '', bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: '' , bold: false,  alignment:'right', fontSize: 14, border: [false, false, false, false] },
       
      ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [160,90 , 135, 120],
            body: termsArray1
          },
          margin: [0, 0, 10, 10]
        }
      )
      let termsArray: any[] = [];
      termsArray.push([
        { text: ' ' ,bold: true, fontSize: 14, border: [false, false, false, false],color:'red' },
        { text: '', bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: '', bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: 'Date:  '+ this.datePipe.transform(res.createdOn, 'd MMM, y',) + '  ' + 'التاريخ :'.split(" ").reverse().join(" ") , bold: false,  alignment:'right', fontSize: 14, border: [false, false, false, false] },
       
      ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [160,80 , 137, 130],
            body: termsArray
          },
          margin: [0, -15, 10, 10]
        }
      )
      let customerDetails0: any[] = [];
      customerDetails0.push([
        { text: 'Customer Name:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.customerFullName), borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true],margin:[0,0,0,0],},
        { text: 'إسم  العميل :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
        
          table: {
            headerRows: 1,
            widths: [74, '*', 49],
            body: customerDetails0
          },
          margin: [0, -8, 0, 5]
        }
      )
      let customerDetails: any[] = [];
      customerDetails.push([
        { text: 'Civil ID No:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.civilIDNumber), borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text:'الرقم  المدني :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
        
          table: {
            headerRows: 1,
            widths: [54.5, '*', 54],
            body: customerDetails
          },
          margin: [0, -4, 0, 10]
        }
      )
      let customerDetails01: any[] = [];
      customerDetails01.push([
        { text: 'Nationality:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.nationality), borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'الجنسية :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
        
          table: {
            headerRows: 1,
            widths: [50.5, '*', 39],
            body: customerDetails01
          },
          margin: [0, -5, 0, 10]
        }
      )
      let customerDetails02: any[] = [];
      customerDetails02.push([
        { text: 'Address:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.address), borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'العنوان :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
        
          table: {
            headerRows: 1,
            widths: [38, '*', 35],
            body: customerDetails02
          },
          margin: [0, -5, 0, 10]
        }
      )
      let customerDetails03: any[] = [];
      customerDetails03.push([
        { text: 'e-mail:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.email), borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'البريد الألكتروني :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      
      dd.content.push(
        {
        
          table: {
            headerRows: 1,
            widths: [35, '*', 67],
            body: customerDetails03
          },
          margin: [0, -6, 0, 10]
        }
      )
      let customerDetails1: any[] = [];
      customerDetails1.push([
        { text: 'Fax No: ',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.faxNumber), borderColor: ['', '', '', '#808080'],bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'رقم  الفاكس :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
        
        { text: 'Mobile No: ',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.phoneNumber),borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'رقم  الموبايل :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
        
        { text: 'Phone No:' ,bold: false, fontSize: 13, border: [false, false, false, false] },
        { text: (res.secondaryNumber), borderColor: ['', '', '', '#808080'], bold: false, fontSize: 13, border: [false, false, false, true] },
        { text: 'رقم  الهاتف :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [35, 42, 52, 50, 52, 54, 43, 80, 50],
            body: customerDetails1
          },
          margin: [0, -10, 0, 2]
        }
      )
      let customerDetails1112: any[] = [];
      customerDetails1112.push([
        { text: 'Description of items stored:',bold: true, fontSize: 14, border: [false, false, false, false],color:'green' },
        { text: '',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text:'مواصفات  الامتعة  المخزنة :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false],color:'green' },
        
      
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*','*','*'],
            body: customerDetails1112
          },
          margin: [0, 5, 0, 2]
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
          table: {
            headerRows: 1,
            widths: ['*', '*', '*', '*',],
            body: description
          },
          margin: [0, -25, 0, 10]
        },
        '\n'
      )
      let rentDetails0212: any[] = [];
      rentDetails0212.push([
        { text: 'Total Storage Stuff Value:',bold: true, fontSize: 14, border: [false, false, false, false],color:'green' },
        { text: (res.deposit), borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'إجمالي  القيمة  للبضاعة  المخزنة :'.split(" ").reverse().join(" "), alignment:'right', bold: true, fontSize: 14, border: [false, false, false, false],color:'green' },
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [124, '*',120],
            body: rentDetails0212
          },
          margin: [0, -28, 0, 10]
        }
      )

      let rentDetails: any[] = [];
     // this.storeNumber=this.finalParticulars.map(element=>element).join(",");
     console.log(res.storeNumbers)
     console.log(this.storageName)
res.storeNumbers.forEach(id => {
  this.storageName.forEach(name => {
if(name.value == id){
  this.finalStoreNumber.push(name.label)
}
  })
});
console.log(this.finalStoreNumber)
console.log(this.finalStoreNumber)
this.comaStoreNumber = this.finalStoreNumber.map(element=>element).join(",");
      rentDetails.push([
        { text: 'Rent Period: ',bold: true, fontSize: 12, border: [false, false, false, false],color:'green', },
        { text: (res.rentPeriod), borderColor: ['', '', '', '#808080'],bold: false, fontSize: 12, border: [false, false, false, true],margin:[-2,0,0,0] },
        { text: 'مدة  الاشتراك :'.split(" ").reverse().join(" "), alignment:'left', bold: true, fontSize: 12, border: [false, false, false, false],color:'green',margin:[-1,0,0,0]  },

        { text: '', bold: false, fontSize: 12, border: [false, false, false, false] },

        { text: 'Room Size (SQM): ',bold: true, fontSize: 12, border: [false, false, false, false],color:'green',margin:[-1,0,0,0]  },
        { text: (res.referenceField5),borderColor: ['', '', '', '#808080'], bold: false, fontSize: 11, border: [false, false, false, true] ,margin:[-1,0,0,0] },
        { text: 'قياس  الغرفة :'.split(" ").reverse().join(" "), alignment:'left', bold: true, fontSize: 12, border: [false, false, false, false],color:'green', margin:[-1,0,0,0] },
        { text: '', bold: false, fontSize: 12, border: [false, false, false, false] },
        { text:'Room No:',bold: true, fontSize: 12, border: [false, false, false, false],color:'green',margin:[-1,0,0,0]  },
        { text: res.referenceField4,borderColor: ['', '', '', '#808080'], bold: false, fontSize: 11, border: [false, false, false, true], margin:[-1,0,0,0] },
        { text:  'رقم  الغرفة :'.split(" ").reverse().join(" "), alignment:'right', bold: true, fontSize: 12, border: [false, false, false, false],color:'green',margin:[-1,0,0,0]  },


      ]);
      rentDetails.push([
        { text: 'New:',bold: true, fontSize: 13, border: [false, false, false, false],color:'green' },
        { text: (res.agreementType == 'New' ? 'Yes' : '') ,borderColor: ['', '', '', '#808080'], bold: false, fontSize: 13, border: [false, false, false, true] ,margin:[-1,0,0,0]},
        { text: 'جديد :'.split(" ").reverse().join(" "), alignment:'left', bold: true, fontSize: 13, border: [false, false, false, false],color:'green', margin:[-1,0,0,0]},
        
        { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },

        { text: 'Renew: ',bold: true, fontSize: 13, border: [false, false, false, false],color:'green',margin:[-1,0,0,0] },
        { text: (res.agreementType == 'Renew' ? 'Yes' : '') , borderColor: ['', '', '', '#808080'],bold: false, fontSize: 13, border: [false, false, false, true],margin:[-1,0,0,0]},
        { text:'تجديد :'.split(" ").reverse().join(" "), alignment:'left', bold: true, fontSize: 13, border: [false, false, false, false],color:'green',margin:[-1,0,0,0] },
        { text: '', bold: false, fontSize: 13, border: [false, false, false, false] },
        { text: 'Transfer: ',bold: true, fontSize: 13, border: [false, false, false, false],color:'green', margin:[-1,0,0,0]},
        { text: (res.agreementType == 'Transfer' ? 'Yes' : '') , borderColor: ['', '', '', '#808080'],bold: false, fontSize: 13, border: [false, false, false, true],margin:[-1,0,0,0]},
        { text:'نقل :'.split(" ").reverse().join(" "), alignment:'left', bold: true, fontSize: 13, border: [false, false, false, false],color:'green',margin:[-1,0,0,0] },
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [55, 66, 47, -8, 80, 25, 47,-15,43,60,40],
            body: rentDetails
          },
          margin: [0, -10, 0, 10]
        }
      )
     
      
      let rentDetails021: any[] = [];
      const toWords = new ToWords();
      let words = toWords.convert(Number(res.rent));
      rentDetails021.push([
        { text: 'Rent Value:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (words ? words + ' Only ' : ''), borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'قيمة  الاشتراك :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [53, '*', 71.5],
            body: rentDetails021
          },
          margin: [0, -9, 0, 10]
        }
      )
      let rentDetails02: any[] = [];
      rentDetails02.push([
        { text: 'Deposit:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: res.deposit != null ? 'KWD ' + this.decimalPipe.transform(res.deposit, "1.3-3") : 'KWD 0.000', borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true],margin:[0,0,-1,0] },
        { text: 'قيمة  الدفعة  المقدمة :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [46.5, '*', 85],
            body: rentDetails02
          },
          margin: [0, -8, 0, 10]
        }
      )
      let rentDetails03: any[] = [];
      rentDetails03.push([
        { text: 'Insurance:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.insurance), borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'قيمة  التأمين :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      rentDetails03.push([
        { text: 'Others:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: (res.others), borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'رسوم  آخرى :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [47, '*', 53],
            body: rentDetails03
          },
          margin: [0, -8, 0, 10]
        }
      )

      let paymentMethod: any[] = [];
      paymentMethod.push([
        { text: 'Payment Method:',bold: true, fontSize: 14, border: [false, false, false, false] },
        { text: (res.paymentTerms), borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'طريقة  الدفع :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: 'Total Amount:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text:  res.rent != null ? 'KWD ' + this.decimalPipe.transform(res.rent, "1.3-3") : '0.000 KWD', borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'الأجمالي :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [85, 117, 52.5,65, 124,48],
            body: paymentMethod
          },
          margin: [0, -2, 0, 20]
        }
      )
      let paymentMethod112: any[] = [];
      paymentMethod112.push([
        { text: 'Start Date:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: this.datePipe.transform(res.startDate, 'dd-MM-yyyy','GMT'), borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'تاريخ  البداية :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },

        { text: 'Ending Date:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: this.datePipe.transform(res.endDate, 'dd-MM-yyyy','GMT'), borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'تاريخ  الانتهاء :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
      
       

      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [47, 130, 55, 58, 95,60],
            body: paymentMethod112
          },
          margin: [30, -10, 0, 35]
        }
      )
      let paymentMethod113: any[] = [];
      paymentMethod113.push([
        { text: 'Co.Rep.Signature:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: '', borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'توقيع  ممثل  الشركة :'.split(" ").reverse().join(" "), alignment:'right', bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: 'Client Signature:',bold: false, fontSize: 14, border: [false, false, false, false] },
        { text: '', borderColor: ['', '', '', '#808080'], bold: false, fontSize: 14, border: [false, false, false, true] },
        { text: 'توقيع  العميل :'.split(" ").reverse().join(" "), alignment:'right', bold: false,  fontSize: 14, border: [false, false, false, false] },
      ]);
      
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: [82, 88, 83, 73, 110,58],
            body: paymentMethod113
          },
          margin: [0, -20, 0, 20]
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


      let imageTerms: any[] = [];
      imageTerms.push([
        { image: agreementLogo.terms1, width: 555, height: 761,  bold: false, fontSize: 13, border: [false, false, false, false] },
      ]);
      dd.content.push(
        {
          table: {
            headerRows: 1,
            widths: ['*'],
            body: imageTerms
          },
        margin: [-10, -110, -10, 0]
        }
      )

      
    // pdfMake.createPdf(dd).download('Agreement- ' + res.agreementNumber);
      pdfMake.createPdf(dd).open();
 
    this.spin.hide();
}

}



