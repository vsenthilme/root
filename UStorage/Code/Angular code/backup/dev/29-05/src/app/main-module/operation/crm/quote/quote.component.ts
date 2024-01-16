



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
import { QuoteNewComponent } from './quote-new/quote-new.component';
import { QuoteService } from './quote.service';
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
    phoneno: string;
    Agreement: string;
  }
  
  const ELEMENT_DATA: PeriodicElement[] = [
    {usercode: "test", name: 'test', admin: 'test', role: 'test', userid: 'test', password: 'test', status: 'test', email: 'test', phoneno: 'test',Agreement: 'test'},
  ];
 

  @Component({
    selector: 'app-quote',
    templateUrl: './quote.component.html',
    styleUrls: ['./quote.component.scss']
  })
  export class QuoteComponent implements OnInit {
  
    constructor(private router: Router,
      private fb: FormBuilder,
      public toastr: ToastrService,
      private cs: CommonService,
      private spin: NgxSpinnerService,
      private auth: AuthService,
      public dialog: MatDialog,
      private route: ActivatedRoute,
      private service: QuoteService,
      private cas: CommonApiService,
      private decimalPipe: DecimalPipe,
      public datePipe: DatePipe,
      
    ) { }

    sub = new Subscription();
    ELEMENT_DATA: any[] = [];
    customerName: any[] = [];
    ngOnInit() {
      
    this.cas.getalldropdownlist([
      this.cas.dropdownlist.trans.customerDropdown.url,
    ]).subscribe((results: any) => {
      results[0].customerDropDown.forEach((x: any) => {
        this.customerName.push({ value: x.customerCode, label: x.customerName });
      })
      this.search()
    }, (err) => {
      this.toastr.error(err, "");
    });
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
  
    displayedColumns: string[] = ['select', 'quoteId', 'Doc','enquiryReferenceNumber', 'customerName', 'mobileNumber', 'requirement', 'email', 'customerGroup', 'storeSize', 'rent','status', 'notes','createdBy','createdOn'];
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
    checkboxLabel(row?: PeriodicElement): string {
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



    openDialog(data: any = 'New'): void {
      if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.warning("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
      const dialogRef = this.dialog.open(QuoteNewComponent, {
        disableClose: true,
       // width: '55%',
        maxWidth: '80%',
        data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].quoteId : null}
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
        data: this.selection.selected[0].quoteId,
      });
  
      dialogRef.afterClosed().subscribe(result => {
  
        if (result) {
          this.deleterecord(this.selection.selected[0].quoteId);
  
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
          "Quote ID": x.quoteId,
          "Inquiry Ref No ": x.enquiryReferenceNumber,
          "Customer Name" : x.customerFullName,
          "	Phone No": x.mobileNumber,
          "Requirement": x.requirement,
          "Email": x.email,
          "Type of Customer": x.customerGroup,
          "Store Size": x.storeSize,
          "Rent": x.rent,
          "Notes": x.notes,
          "Created By":x.createdBy,
          "Created On":x.createdOn
        });
  
      })
      this.cs.exportAsExcel(res, "Quote");
    }

    multiselectQuoteList: any[] = [];
    searhform = this.fb.group({
       customerGroup: [],
        customerName: [],
        email: [],
        enquiryReferenceNumber: [],
        isActive: [],
        mobileNumber: [],
        quoteId: []
    });

    search(){
      this.service.search(this.searhform.value).subscribe(res => {
        this.spin.hide();
        res.forEach((x) => this.multiselectQuoteList.push({value: x.quoteId, label: x.quoteId}));
        this.multiselectQuoteList = this.cs.removeDuplicatesFromArrayNew(this.multiselectQuoteList);

        res.forEach(element => {
          element['customerFullName'] =  this.customerName.find(y => y.value == element.customerName)?.label;
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


    generatePdf(res) {
      console.log(res)
     // res.totalAfterDiscount
        //Receipt List
          var dd: any;
          let headerTable: any[] = [];
          
          headerTable.push([
           { image: workOrderLogo1.headerLogo1, fit: [200, 200], alignment: 'left',  bold: false, fontSize: 12, border: [false, false, false, false] },
      //     { image: workOrderLogo1.headerLogo1, fit: [180, 180], alignment: 'center',  bold: false, fontSize: 12, border: [false, false, false, false] },
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
                  margin: [-10, 0, 0, 20]
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
              
              { image: res.requirement == "Packing & Moving" ? workOrderLogo1.ulogistics1 : workOrderLogo1.ustorage, width: 570,   bold: false, margin: [10, -80, 0, 0], fontSize: 12, border: [false, false, false, false] },
            ]
            },
            content: ['\n'],
            defaultStyle,
            
          };
    
          let headerArray: any[] = [];
          headerArray.push([
            { text: 'TO:  ', bold: false, fontSize: 14, border: [false, false, false, false] },
            { text: '' , bold: true, alignment: 'center', fontSize: 14, border: [false, false, false, false],},
            { text: 'DATE:  '+'  '+ this.datePipe.transform(res.createdOn, 'd MMM, y','GMT'), bold: false, fontSize: 14, border: [false, false, false, false] },
          ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [140, 255, '*'],
                body: headerArray
              },
              margin: [0, -30, 10, 0]
            }
          )
          let header1Array: any[] = [];
          header1Array.push([
            { text: 'ATTN:'+'  '+ (res.customerFullName), bold: false, fontSize: 14, border: [false, false, false, false] },
            { text: '' , bold: true, alignment: 'center', fontSize: 14, border: [false, false, false, false],},
            { text: 'REF: '+' '+ (res.enquiryReferenceNumber), bold: false, fontSize: 14, border: [false, false, false, false] },
          ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [140, 255, '*'],
                body: header1Array
              },
              margin: [0, -5, 10, 0]
            }
          )
          let bodyArray: any[] = [];
          bodyArray.push([
            { text: 'SUBJECT:', bold: false, fontSize: 14, border: [false, false, false, false] },
            { text: 'Quotation for Storage / Packing & Moving.' , bold: true, alignment: 'center', fontSize: 14, border: [false, false, false, true],},
            { text: '', bold: false, fontSize: 14, border: [false, false, false, false] },
          ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [48, 250, '*'],
                body: bodyArray
              },
              margin: [0, 10, 10, 0]
            }
          )
          let body1Array: any[] = [];
          body1Array.push([
            { text: 'Thank you for this opportunity to quote. Reference is made to the stated subject and in accordance with the pictures / telephonic conversation provided, we are pleased to offer the best for the following scope of works. ', bold: false, fontSize: 14, border: [false, false, false, false] },
            
          ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: ["*"],
                body: body1Array
              },
              margin: [0, 10, 10, 0]
            }
          )
          let body2Array: any[] = [];
          body2Array.push([
            { text: 'Item', bold: true, fontSize: 14, border: [true, true, true, true] },
            { text: 'Description' , bold: true, alignment: 'center', fontSize: 14, border: [true, true, true, true],},
            { text: res.requirement == "Packing & Moving" ? 'Rent/Charges (KWD) ' : 'Rent (KWD) ', bold: true, fontSize: 14, border: [true, true, true, true] },
            { text: ' Specifications ', bold: true, fontSize: 14, border: [true, true, true, true] },
          ]);
          body2Array.push([
            { text: '1',  fontSize: 14, border: [true, true, true, true] },
            { text: (res.requirement == 'Others' ? res.notes : res.requirement == 'Both' ? 'StorageUnit and Packing & Moving' : res.requirement) ,  alignment: 'center', fontSize: 14, border: [true, true, true, true],},
            { text: res.rent != null ?  'KWD ' + this.decimalPipe.transform(res.rent, "1.3-3") : '0.00KWD',  fontSize: 14, border: [true, true, true, true] },
            { text: (res.storeSize),  fontSize: 14, border: [true, true, true, true] },
          ]);
          // body2Array.push([
          //   { text: '2', bold: true, fontSize: 14, border: [true, true, true, true] },
          //   { text: '' , bold: true, alignment: 'center', fontSize: 14, border: [true, true, true, true],},
          //   { text: ' ', bold: true, fontSize: 14, border: [true, true, true, true] },
          //   { text: ' ', bold: true, fontSize: 14, border: [true, true, true, true] },
          // ]);
          // body2Array.push([
          //   { text: '3', bold: true, fontSize: 14, border: [true, true, true, true] },
          //   { text: '' , bold: true, alignment: 'center', fontSize: 14, border: [true, true, true, true],},
          //   { text: ' ', bold: true, fontSize: 14, border: [true, true, true, true] },
          //   { text: ' ', bold: true, fontSize: 14, border: [true, true, true, true] },
          // ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [45, 250, 100,"*"],
                body: body2Array
              },
              margin: [3, 6, 10, 0]
            }
          )
          let body3Array: any[] = [];
          body3Array.push([
            { text: 'Notes: ', bold: true, fontSize: 14, border: [false, false, false, false] },
            { text: (res.notes), bold: true, fontSize: 14, border: [false, false, false, true] },
            
          ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [50,"*"],
                body: body3Array
              },
              margin: [0, 5, 10, 0]
            }
          )
          let body4Array: any[] = [];
          body4Array.push([
            { text: 'Quotation Validity: ', bold: true, fontSize: 14, border: [false, false, false, false] },
            { text: '5DAYS ', bold: false, fontSize: 14, border: [false, false, false, false] },
            
          ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [120,50],
                body: body4Array
              },
              margin: [0, 10, 10, 0]
            }
          )
          let body5Array: any[] = [];
          body5Array.push([
            { text: 'Features and Facilities: ', bold: true, fontSize: 14, border: [false, false, false, false] },
            
            
          ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [120],
                body: body5Array
              },
              margin: [0, 5, 10, 0]
            }
          )
          let body6Array: any[] = [];
          body6Array.push([
            { text: 'Open 7 Days a Week', bold: true, fontSize: 14, border: [true, true, false, true] },
            { text: '-', bold: true, fontSize: 14, border: [false, true, false, true] },
            { text: 'Weekly to Yearly Leases ', bold: false, fontSize: 14, border: [false, true, true, true] },
            
          ]);
          body6Array.push([
            { text: 'Insurance Available (Optional)', bold: true, fontSize: 14, border: [true, true, false, true] },
            { text: '-', bold: true, fontSize: 14, border: [false, true, false, true] },
            { text: 'Great Customer Service ', bold: false, fontSize: 14, border: [false, true, true, true] },
            
          ]);
          body6Array.push([
            { text: 'Great Customer Service', bold: true, fontSize: 14, border: [true, true, false, true] },
            { text: '-', bold: true, fontSize: 14, border: [false, true, false, true] },
            { text: 'Moving and Packing supplies ', bold: false, fontSize: 14, border: [false, true, true, true] },
            
          ]);
          body6Array.push([
            { text: 'Truck Available for Renting ', bold: true, fontSize: 14, border: [true, true, false, true] },
            { text: '-', bold: true, fontSize: 14, border: [false, true, false, true] },
            { text: ' Drive up Access', bold: false, fontSize: 14, border: [false, true, true, true] },
            
          ]);
          body6Array.push([
            { text: 'Passenger Elevator ', bold: true, fontSize: 14, border: [true, true, false, true] },
            { text: '-', bold: true, fontSize: 14, border: [false, true, false, true] },
            { text: 'Trolley & Hand Carts Available ', bold: false, fontSize: 14, border: [false, true, true, true] },
            
          ]);
          body6Array.push([
            { text: 'Individual Storage Area ', bold: true, fontSize: 14, border: [true, true, false, true] },
            { text: '-', bold: true, fontSize: 14, border: [false, true, false, true] },
            { text: ' Access -24 hours ', bold: false, fontSize: 14, border: [false, true, true, true] },
            
          ]);
          body6Array.push([
            { text: 'Free porter service', bold: true, fontSize: 14, border: [true, true, false, true] },
            { text: '-', bold: true, fontSize: 14, border: [false, true, false, true] },
            { text: ' ', bold: false, fontSize: 14, border: [false, true, true, true] },
            
          ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [200,100,200],
                body: body6Array
              },
              margin: [3, 10, 10, 0]
            }
          )
          let body7Array: any[] = [];
          body7Array.push([
            { text: 'We await for your approval to our quote and waiting to deal with you. Meanwhile if you have any question or concerns do not hesitate to contact us and your feedback will be highly appreciated. ', bold: false, fontSize: 14, border: [false, false, false, false] },
            
          ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: ["*"],
                body: body7Array
              },
              margin: [0, 5, 10, 0]
            }
          )
          let body8Array: any[] = [];
          body8Array.push([
            { text: 'Thanking you and assuring you our best service and attention at all time ', bold: false, fontSize: 14, border: [false, false, false, false] },
            
          ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: ["*"],
                body: body8Array
              },
              margin: [0, 5, 10, 0]
            }
          )
          let body9Array: any[] = [];
          body9Array.push([
            { text: 'Store Incharge:', bold: true, fontSize: 14, border: [false, false, false, false] },
            { text: '', bold: false, fontSize: 14, border: [false, false, false, false] },
          ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: [100,'*'],
                body: body9Array
              },
              margin: [0, 2, 10, 0]
            }
          )
          let body10Array: any[] = [];
          body10Array.push([
            { text: 'U Storage (Division of Unlink General Trading and Contracting Co.)', bold: false, fontSize: 14, border: [false, false, false, false] },
           
          ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: ['*'],
                body: body10Array
              },
              margin: [0, 2, 10, 0]
            }
          )
          let body11Array: any[] = [];
          body11Array.push([
            { text: 'Contact: 1800255', bold: true, fontSize: 14, border: [false, false, false, false] },
           
          ]);
          
          dd.content.push(
            {
              table: {
                headerRows: 1,
                widths: ['*'],
                body: body11Array
              },
              margin: [0, 2, 10, 0]
            }
          )



          
          pdfMake.createPdf(dd).download('Quote' + res.quoteId);
        // pdfMake.createPdf(dd).open();
     
        this.spin.hide();
    }

  }
  
  