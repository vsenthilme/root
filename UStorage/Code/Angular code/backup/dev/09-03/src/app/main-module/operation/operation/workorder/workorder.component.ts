




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
import { WorkorderNewComponent } from './workorder-new/workorder-new.component';
import { WorkorderService } from './workorder.service';


  // import the pdfmake library
  import pdfMake from "pdfmake/build/pdfmake";
  // importing the fonts and icons needed
  import pdfFonts from "../../../../../assets/font/vfs_fonts";
  import { defaultStyle } from "../../../../../app/config/customStyles";
  import { fonts } from "../../../../../app/config/pdfFonts";
  import { HttpRequest, HttpResponse } from "@angular/common/http";
  import { workOrderLogo1 } from "../../../../../assets/font/workorder-logo1.js";
import { DecimalPipe, DatePipe } from '@angular/common';
import { CustomerService } from 'src/app/main-module/Masters -1/customer-master/customer.service';
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
    selector: 'app-workorder',
    templateUrl: './workorder.component.html',
    styleUrls: ['./workorder.component.scss']
  })
  export class WorkorderComponent implements OnInit {
    total: number;


    constructor(private router: Router,
      private fb: FormBuilder,
      public toastr: ToastrService,
      private cs: CommonService,
      private spin: NgxSpinnerService,
      private auth: AuthService,
      public dialog: MatDialog,
      private route: ActivatedRoute,
      private service: WorkorderService,
      private cas: CommonService,
      private decimalPipe: DecimalPipe,
      public datePipe: DatePipe,
      private customerService: CustomerService,
    ) { }

    sub = new Subscription();
    ELEMENT_DATA: any[] = [];

    ngOnInit() {
      this.search();
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

    displayedColumns: string[] = ['select', 'workOrderId','customerId','Doc', 'remarks', 'createdBy', 'workOrderProcessedBy', 'processedTime', 'leadTime', 'jobCardType','status'];
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
      const dialogRef = this.dialog.open(WorkorderNewComponent, {
        disableClose: true,
       // width: '55%',
        maxWidth: '80%',
        data: { pageflow: data, code: data != 'New' ? this.selection.selected[0].workOrderId : null}
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
        data: this.selection.selected[0].workOrderId,
      });

      dialogRef.afterClosed().subscribe(result => {

        if (result) {
          this.deleterecord(this.selection.selected[0].workOrderId);

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
          "WorkOrder ID":x.workOrderId,
          "Customer ID": x.customerId,
          "Remarks": x.remarks,
          "Created by": x.createdBy,
          "	Processed by": x.workOrderProcessedBy,
          "	Processed Time": x.processedTime,
          "Lead Time": x.leadTime,
          "Job Card": x.jobCardType,
         });

      })
      this.cs.exportAsExcel(res, "Workorder");
    }
    multiselectworkOrderList: any[] = [];
    multiselectcustomerIdList: any[] = [];
    multiselectjobCardList: any[] = [];
    multiselectprocessedByList: any[] = [];
    searhform = this.fb.group({
      codeId: [],
      createdBy: [],
      customerId: [],
      endDate: [],
      isActive: [],
      jobCard: [],
      jobCardType: [],
      processedBy: [],
      startDate: [],
      status: [],
      workOrderId: [],
   });
   search(){
    this.service.search(this.searhform.value).subscribe(res => {
      this.spin.hide();
      res.forEach((x) => this.multiselectworkOrderList.push({value: x.workOrderId, label: x.workOrderId}));
      this.multiselectworkOrderList = this.cs.removeDuplicatesFromArrayNew(this.multiselectworkOrderList);

      res.forEach((x) => this.multiselectcustomerIdList.push({value: x.customerId, label: x.customerId}));
      this.multiselectcustomerIdList = this.cs.removeDuplicatesFromArrayNew(this.multiselectcustomerIdList);

      res.forEach((x) => this.multiselectjobCardList.push({value: x.jobCard, label: x.jobCard}));
      this.multiselectjobCardList = this.cs.removeDuplicatesFromArrayNew(this.multiselectjobCardList);

      res.forEach((x) => this.multiselectprocessedByList.push({value: x.processedBy, label: x.processedBy}));
      this.multiselectprocessedByList = this.cs.removeDuplicatesFromArrayNew(this.multiselectprocessedByList);


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
    this.spin.show();
      //Receipt List
      this.sub.add(this.customerService.search({customerCode: [res.customerId]}).subscribe((result1) => {
        result1.forEach(customer => {


        var dd: any;
        let headerTable: any[] = [];

        headerTable.push([
         { image: workOrderLogo1.ulogisticsheader, fit: [180, 180], alignment: 'left', margin: [20, 0, 0, 0],  bold: true, fontSize: 12, border: [false, false, false, false] },
       //  { image: workOrderLogo1.headerLogo1, fit: [180, 180], alignment: 'center',  bold: true, fontSize: 12, border: [false, false, false, false] },
        ]);

        dd = {
          pageSize: "A4",
          pageOrientation: "portrait",
          pageMargins: [20, 90, 20, 20],
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
            //   bold: true,
            //   fontSize: 6
            // }

        //  { image: workOrderLogo1.invoiceFooter, width: 550,   bold: true, margin: [30, -70, 0, 20], fontSize: 12, border: [false, false, false, false] },
        { image: workOrderLogo1.ulogistics1, width: 570,    bold: false, margin: [10, -80, 0, 0], fontSize: 12, border: [false, false, false, false] },
          ]
          },
          content: ['\n'],
          defaultStyle,

        };
        let headerArray1: any[] = [];
        headerArray1.push([
          
           { text: 'Service Agreement/'  + 'اتفاقية  خدمات'.split(" ").reverse().join(" "), bold: true, alignment: 'left', fontSize: 15, border: [false, false, false, false],margin:[200,0,0,30] },
         
        ]);

        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [ '*'],
              body: headerArray1
            },
            margin: [0, -50, 0, 0]
          }
        )

        let headerArray: any[] = [];
        headerArray.push([
          { text: 'Agreement No:', bold: true, fontSize: 15, border: [false, false, false, false] },
          { text: (res.workOrderId), bold: true, fontSize: 15, border: [false, false, false, false],margin:[0,0,0,0] },
          { text: 'رقم  الاتفاقية:'.split(" ").reverse().join(" "), bold: true, fontSize: 15, border: [false, false, false, false],margin:[0,0,0,0] },
          { text: '', bold: true, fontSize: 15, border: [false, false, false, false],margin:[0,0,0,0] },
          // { text: 'Service Agreement/'  + 'اتفاقية خدمات', bold: true, alignment: 'center', fontSize: 15, border: [false, false, false, false] },
          { text: 'Team No:'  , bold: true, fontSize: 15, alignment: 'left', border: [false, false, false, false],margin:[0,0,0,0] },
          { text: (res.workOrderProcessedBy) , bold: true, fontSize: 15, alignment: 'left', border: [false, false, false, false],margin:[-24,0,0,0] },
          { text: 'رقم  الفريق:'.split(" ").reverse().join(" ")  , bold: true, fontSize: 15, alignment: 'left', border: [false, false, false, false],margin:[38,0,0,0] },
        ]);

        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [80,84,80,10,75, '*', '*'],
              body: headerArray
            },
            margin: [0, -12, 0, 0]
          }
        )



        let table1: any[] = [];
        
        table1.push([
          { text: 'Date',  fontSize: 13, alignment: 'left', border: [true, true, true, true] },
          { text: this.datePipe.transform(res.workOrderDate, 'd MMM, y'),  fontSize: 13, alignment: 'center', border: [true, true, true, true] },
          { text: 'التاريخ'.split(" ").reverse().join(" "),  fontSize: 13, alignment: 'right', border: [true, true, true, true] },
        ]);
        table1.push([
          { text: 'Customer Name',  fontSize: 13, alignment: 'left', border: [true, true, true, true] },
          { text: (customer.customerName),  fontSize: 13, alignment: 'center', border: [true, true, true, true] },
          { text: 'اسم  العميل'.split(" ").reverse().join(" "),  fontSize: 13, alignment: 'right', border: [true, true, true, true] },
        ]);
        table1.push([
          { text: 'Civil ID No',  fontSize: 13, alignment: 'left', border: [true, true, true, true] },
          { text: (customer.civilId),  fontSize: 13, alignment: 'center', border: [true, true, true, true] },
          { text: 'رقم  البطاقة  المدنية'.split(" ").reverse().join(" ") ,  fontSize: 13, alignment: 'right', border: [true, true, true, true] },
        ]);
        table1.push([
          { text: 'Tel No',  fontSize: 13, alignment: 'left', border: [true, true, true, true] },
          { text: (customer.phoneNumber),  fontSize: 13, alignment: 'center', border: [true, true, true, true] },
          { text: 'رقم  الهاتف'.split(" ").reverse().join(" "),  fontSize: 13, alignment: 'right', border: [true, true, true, true] },
        ]);
        table1.push([
          { text: 'From Address',  fontSize: 13, alignment: 'left', border: [true, true, true, true] },
          { text: res.fromAddress,  fontSize: 13, alignment: 'center', border: [true, true, true, true] },
          { text: 'من  العنوان'.split(" ").reverse().join(" "),  fontSize: 13, alignment: 'right', border: [true, true, true, true] },
        ]);
        table1.push([
          { text: 'To Address',  fontSize: 13, alignment: 'left', border: [true, true, true, true] },
          { text: (res.toAddress),  fontSize: 13, alignment: 'center', border: [true, true, true, true] },
          { text: 'الى  العنوان'.split(" ").reverse().join(" "),  fontSize: 13, alignment: 'right', border: [true, true, true, true] },
        ]);
        table1.push([
          { text: 'Start Date and Time',  fontSize: 13, alignment: 'left', border: [true, true, true, true] },
          { text: this.datePipe.transform(res.startDate, 'dd-MM-yyyy')  + res.startTime,  fontSize: 13, alignment: 'center', border: [true, true, true, true] },
          { text: 'وقت وتاريخ البدء'.split(" ").reverse().join(" "),  fontSize: 13, alignment: 'right', border: [true, true, true, true] },
        ]);
        table1.push([
          { text: 'End Date and Time',  fontSize: 13, alignment: 'left', border: [true, true, true, true] },
          { text: this.datePipe.transform(res.endDate, 'dd-MM-yyyy') + res.endTime,  fontSize: 13, alignment: 'center', border: [true, true, true, true] },
          { text: 'وقت وتاريخ الانتهاء'.split(" ").reverse().join(" "),  fontSize: 13, alignment: 'right', border: [true, true, true, true] },
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [150, '*', 150],
              body: table1
            },
            margin: [0, 0, 0, 5]
          }
        )

        let table2: any[] = [];
        table2.push([
          { text: 'Service Description' + '\n' + 'وصف  الخدمة'.split(" ").reverse().join(" "),  alignment: 'center',   fontSize: 13, border: [true, true, true, true] },
          { text: 'Qty' + '\n' + ' الكمية'.split(" ").reverse().join(" "),   fontSize: 13, alignment: 'center', border: [true, true, true, true] },
          { text: 'Unit Cost' + '\n' + ' تكلفة  الوحدة'.split(" ").reverse().join(" "),alignment: 'center',  fontSize: 13, border: [true, true, true, true] },
          { text: 'KWD' + '\n' + 'دينار'.split(" ").reverse().join(" "), alignment: 'center', fontSize: 13, border: [true, true, true, true] },
          { text: 'FILS' + '\n' + 'فلس'.split(" ").reverse().join(" "), alignment: 'center', fontSize: 13, border: [true, true, true, true] },
        ]);

        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: ['*', 40, 60, 40, 40],
              body: table2
            },

          }
        )

        let table2Child: any[] = [];


//         let total = total != null ? total.toString().split('.') : '';
// let totalKWD = total[0];
// let totalFill = total[1] ? total[1].padEnd(3, '0') : '000';

    let total = 0
    res.itemServices.forEach(element => {
    total = total + element.itemServiceTotal;
    this.total = total

    let itemServiceTotal =  element.itemServiceTotal != null ? element.itemServiceTotal.toString().split('.') : ''
    let unitPriceKWD = itemServiceTotal[0];
    let unitPriceFill = itemServiceTotal[1] ? itemServiceTotal[1].padEnd(3, '0') : '000';

  table2Child.push([
    { text: (element.itemServiceName),    alignment: 'left',   fontSize: 13, border: [true, false, false, true] },
    { text: (element.itemName),  alignment: 'right',   fontSize: 13, border: [false, false, false, true] },
    { text: (element.itemServiceQuantity),   fontSize: 13, alignment: 'center', border: [true, false, true, true] },
    { text: element.itemServiceUnitPrice != null ? 'KWD ' + this.decimalPipe.transform(element.itemServiceUnitPrice, '1.3-3') : 'KWD 0.000',alignment: 'center',  fontSize: 13, border: [true, false, true, true] },
    { text: unitPriceKWD, alignment: 'center', fontSize: 13, border: [true, false, true, true] },
    { text: unitPriceFill, alignment: 'center', fontSize: 13, border: [true, false, true, true] },
  ]);
});

        //       let kwdTotal = parseInt(otherKwd) + parseInt(tapeKwd) + parseInt(stretchFilmKwd) + parseInt(nylonRollKwd) + parseInt(bubbleWrapKwd) + parseInt(corrugatedRollKwd) + parseInt(lboxSizeWithoutPackingKwd) +
        // parseInt(lboxSizeWithPackingKwd) + parseInt(sboxSizeWithoutPackingKwd) + parseInt(sboxSizeWithPackingKwd) + parseInt(dismantlingKwd) + parseInt(wrappingKwd) + parseInt(movingKwd);

        // let filTotal = parseInt(otherFils) + parseInt(tapeFils) + parseInt(stretchFilmFils) + parseInt(nylonRollFils) + parseInt(bubbleWrapFils) + parseInt(corrugatedRollFils) + parseInt(lboxSizeWithoutPackingFils) +
        // parseInt(lboxSizeWithPackingFils) + parseInt(sboxSizeWithoutPackingFils) + parseInt(sboxSizeWithPackingFils) + parseInt(dismantlingFils) + parseInt(wrappingFils) + parseInt(movingfils);
        let totalPrice =  this.total != null ? this.total.toString().split('.') : '';
        let totalPriceKWD = totalPrice[0];
        let totalPriceFill = totalPrice[1] ? totalPrice[1].padEnd(3, '0') : '000';
        table2Child.push([
          { text: 'Total Amount:  ' + '', bold: 'true',  alignment: 'left',   fontSize: 13, border: [true, false, false, true] },
          { text: '',  alignment: 'right',   fontSize: 13, border: [false, false, false, true] },
          { text: '',   bold: 'true', fontSize: 13, alignment: 'center', border: [false, false, false, true] },
          { text: ' المبلغ  الاجمالي'.split(" ").reverse().join(" "), bold: 'true', alignment: 'center',  fontSize: 13, border: [false, false, true, true] },
          { text:  totalPriceKWD, alignment: 'center', fontSize: 13, border: [true, false, true, true] },
          { text: totalPriceFill, alignment: 'center', fontSize: 13, border: [true, false, true, true] },
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: ['*', '*', 40, 60, 40, 40],
              body: table2Child
            },

          }
        )
       
        let note: any[] = [];
        note.push([
          { text: 'Damaged items which the company agrees to move without any liablity upon the co.',     fontSize: 13, border: [false, false, false, false] },
          { text: '',  alignment: 'right',   fontSize: 13, border: [false, false, false, false] },
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: ['*', 100,],
              body: note
            },

          }
        )

        let remarks: any[] = [];
        remarks.push([
          { text: 'Remarks:' + ' / ' +  'توقيع العميل', bold: 'true',  alignment: 'left',   fontSize: 13, border: [false, false, false, false] },
          { text: (res.remarks),  alignment: 'left',   fontSize: 13, borderColor: ['', '', '', '#808080'], border: [false, false, false, true] },
          { text: 'ملاحظات:'.split(" ").reverse().join(" "),  bold: 'true',  alignment: 'right',   fontSize: 13, border: [false, false, false, false] },
        ]);

        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [92, '*', 35],
              body: remarks,
              margin: [20, 5, 40, 40]
            },

          }
        )
        let signature: any[] = [];
        signature.push([
          { text: 'Customer Signature' + ' / ' +  'توقيع  العميل'.split(" ").reverse().join(" "), bold: 'true',    fontSize: 13, border: [false, false, false, false],margin:[0,25,0,0] },
          { text: '',  alignment: 'right',   fontSize: 13, border: [false, false, false, true],margin:[0,25,0,0],borderColor: ['', '', '', '#808080']},
          { text: 'Receiver Signature' + ' / ' + 'توقيع  المستلم'.split(" ").reverse().join(" "), bold: 'true',    fontSize: 13, border: [false, false, false, false],margin:[0,25,0,0] },
          { text: '' , bold: 'true',  fontSize: 13, border: [false, false, false, true],margin:[0,25,0,0],borderColor: ['', '', '', '#808080'] },
        ]);
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [150, 110, 150, 110],
              body: signature
            },

          }
        )

        pdfMake.createPdf(dd).download('Work Order - ' + res.workOrderId);
   // pdfMake.createPdf(dd).open();

      this.spin.hide();
    });
        }));
  }
  }










