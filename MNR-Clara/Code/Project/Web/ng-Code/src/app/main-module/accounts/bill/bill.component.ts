
import { SelectionModel } from "@angular/cdk/collections";
import { DatePipe, DecimalPipe } from "@angular/common";
import { Component, Injectable, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { of, Subscription } from "rxjs";
import { CommonApiService } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { AuthService } from "src/app/core/core";
import { BillService } from "./bill.service";
import { PaymentlinkComponent } from "./paymentlink/paymentlink.component";
import { HttpClient, HttpEvent, HttpErrorResponse } from '@angular/common/http'

// import the pdfmake library
import pdfMake from "pdfmake/build/pdfmake";
// importing the fonts and icons needed
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { GeneralMatterService } from "../../matters/case-management/General/general-matter.service";
import pdfFonts from "./../../../../assets/font/vfs_fonts.js";
import { defaultStyle } from "./../../../config/customStyles";
import { fonts } from "./../../../config/pdfFonts";
import { last } from "rxjs/operators";
import { HttpRequest, HttpResponse } from "@angular/common/http";
//import { logo } from "../../../../assets/font/logo.js";f
import { diamondlogo } from "../../../../assets/font/dimond_logo.js";
import { resizedLogo } from "../../../../assets/font/resized_logo.js";
import { TimekeeperService } from "../../setting/business/timekeeper/timekeeper.service";
import { UserTypeService } from "../../setting/admin/usertype-id/user-type.service";
// PDFMAKE fonts
pdfMake.vfs = pdfFonts.pdfMake.vfs;
pdfMake.fonts = fonts;
interface SelectItem {
  id: string;
  itemName: string;
}
@Injectable({
  providedIn: 'root'
})
@Component({
  selector: 'app-bill',
  templateUrl: './bill.component.html',
  styleUrls: ['./bill.component.scss']
})
export class BillComponent implements OnInit {
  screenid = 1140;
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  displayedColumns: string[] = ['select', 'invoiceNumber', 'preBillNumber', 'matterNumber', 'invoiceDate', 'partnerAssigned', 'remainderdate', 'textFile', 'invoiceAmount', 'totalPaidAmount', 'remainingBalance', 'paymentlink', 'statusIddes'];
  dataSource = new MatTableDataSource<any>([]);
  selection = new SelectionModel<any>(true, []);
  showFiller = false;
  animal: string | undefined;
  id: string | undefined;
  sub = new Subscription();
  statuslist: any[] = [];
  matterList: any[] = [];
  ELEMENT_DATA: any[] = [];

  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator; // Pagination
  selectedItems8: SelectItem[] = [];
  multiselectmatterListList: any[] = [];
  multimatterListList: any[] = [];

  selectedItems4: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];
  selectedItems5: SelectItem[] = [];
  multiselectinvoicenoList: any[] = [];
  multiinvoicenoList: any[] = [];

  selectedItems9: SelectItem[] = [];
  multiselecipartnerList: any[] = [];
  multipartnerList: any[] = [];
  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  searhform = this.fb.group({
    classId: [],
    clientId: [],
    createdBy: [],
    endInvoiceDate: [this.cs.todayapi(),],
    endInvoiceDateFE: [this.cs.todayapi(),],
    invoiceNumber: [],
    invoiceNumberFE: [],
    matterNumber: [],
    matterNumberFE: [],
    legalAssistant: [],
    partnerAssigned: [],
    partnerAssignedFE: [],
    startInvoiceDate: ['2022-06-30T18:30:00.000Z',],
    startInvoiceDateFE: ['2022-06-30T18:30:00.000Z',],
    statusId: [],
    statusIdFE: [],
  });
  RA: any = {};
  pageNumber = 0;
  pageSize = 100;
  totalRecords = 0;
  private setting = {
    element: {
      dynamicDownload: null as unknown as HTMLElement
    }
  }
  constructor(private fb: FormBuilder,
    private auth: AuthService,
    private service: BillService,
    public toastr: ToastrService,
    private cas: CommonApiService,
    public dialog: MatDialog,
    private excel: ExcelService,
    private spin: NgxSpinnerService,
    private router: Router,
    private cs: CommonService,
    private matterservice: GeneralMatterService,
    private decimalPipe: DecimalPipe,
    private datePipe: DatePipe,
    public HttpClient: HttpClient,
    public timekeeperService: TimekeeperService,
    public userType: UserTypeService,
  ) { }
  ngOnInit(): void {

    this.RA = this.auth.getRoleAccess(this.screenid);
    //   this.getAllDropdownList();
    this.search();
  }
  pageHandler($event: PageEvent) {
    this.pageNumber = $event.pageIndex;
    this.pageSize = $event.pageSize;
    this.getAllListData();
  }


  getAllDropdownList() {
    this.sub.add(this.matterservice.getAllSearchDropDown().subscribe((data: any) => {
      if (data) {
        data.matterList.forEach((x: any) => this.matterList.push({ value: x.key, label: x.key + '-' + x.value }));
        this.matterList.forEach((x: any) => this.multimatterListList.push({ value: x.value, label: x.label }));
        this.multiselectmatterListList = this.multimatterListList;
        this.multiselectmatterListList = this.cas.removeDuplicatesFromArrayNew(this.multiselectmatterListList);
      }
    }, (err) => {
      this.toastr.error(err, "");
    }));
    this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {
      let filterResult = res.filter(x => x.deletionIndicator == 0);
      filterResult.forEach((x: { invoiceNumber: string; }) => this.multiinvoicenoList.push({ value: x.invoiceNumber, label: x.invoiceNumber }))
      this.multiselectinvoicenoList = this.multiinvoicenoList;
      this.multiselectinvoicenoList = this.cas.removeDuplicatesFromArrayNew(this.multiselectinvoicenoList);
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));

    this.cas.getalldropdownlist([
      this.cas.dropdownlist.setup.statusId.url,
      // this.cas.dropdownlist.matter.matterNumber.url,
    ]).subscribe((results) => {
      //this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key);
      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key).filter(s => [51, 52, 53, 54].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselectstatusList = this.multistatusList;
      // this.matterList = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.matter.matterNumber.key);
      // this.matterList.forEach((x: { key: string; value: string; }) => this.multimatterListList.push({ value: x.key, label: x.key + ' / ' + x.value }))
      // this.multiselectmatterListList = this.multimatterListList;
      this.spin.hide();
      this.getAllListData();
    }, (err) => {
      this.toastr.error(err, "");
    });
  }
  getAllListData() {
    this.sub.add(this.service.getAllInvociecByPagination(this.pageNumber, this.pageSize).subscribe((res: any) => {
      // res.content.forEach((x: { invoiceNumber: string; }) => this.multiinvoicenoList.push({ value: x.invoiceNumber, label: x.invoiceNumber }))
      // this.multiselectinvoicenoList = this.multiinvoicenoList;
      // this.multiselectinvoicenoList = this.cas.removeDuplicatesFromArrayNew(this.multiselectinvoicenoList);
      res.content.forEach((x: { partnerAssigned: string; }) => this.multipartnerList.push({ value: x.partnerAssigned, label: x.partnerAssigned }))
      this.multiselecipartnerList = this.multipartnerList;
      this.multiselecipartnerList = this.cas.removeDuplicatesFromArrayNew(this.multiselecipartnerList);
      // this.ELEMENT_DATA = res.content;
      this.ELEMENT_DATA = res.content;   //.filter(x => x.deletionIndicator == 0)
      this.ELEMENT_DATA.forEach((x) => {
        x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
      })
      this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
      // this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.updatedOn > b.updatedOn) ? -1 : 1));
      this.selection = new SelectionModel<any>(true, []);
      this.dataSource.sort = this.sort;
      this.totalRecords = res.totalElements;
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
  billnew() {
    this.router.navigate(['/main/accounts/newbilllist']);
  }
  paymentlink(data: any): void {
    const dialogRef = this.dialog.open(PaymentlinkComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: data.referenceField10
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.spin.show();
        this.sub.add(this.service.Update({ referenceField10: result, statusId: 52 }, data.invoiceNumber).subscribe((res: any) => {
          this.spin.hide();
          data.referenceField10 = result;
          this.toastr.success(data.invoiceNumber + " updated successfully!", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
          window.location.reload();
        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));
      }
    });
  }
  openDialog(data: any = 'new'): void {
    if (data != 'New') {
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }

    }
    let paramdata = "";
    if (this.selection.selected.length > 0) {
      paramdata = this.cs.encrypt({ code: this.selection.selected[0], pageflow: data });
      this.router.navigate(['/main/accounts/billingdetails/' + paramdata]);
    }
    else {
      paramdata = this.cs.encrypt({ pageflow: data });
      this.router.navigate(['/main/accounts/billingdetails/' + paramdata]);
    }
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.documenttype + 1}`;
  }
  clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }
  Clear() {
    this.reset();
  };
  search() {
    this.searhform.controls.startInvoiceDate.patchValue(this.cs.dateNewFormat(this.searhform.controls.startInvoiceDateFE.value));
    this.searhform.controls.endInvoiceDate.patchValue(this.cs.dateNewFormat(this.searhform.controls.endInvoiceDateFE.value));
    // if (this.selectedItems8 && this.selectedItems8.length > 0) {
    //   let multimatterListList: any[] = []
    //   this.selectedItems8.forEach((a: any) => multimatterListList.push(a.id))
    //   this.searhform.patchValue({ matterNumber: multimatterListList });
    // }
    // if (this.selectedItems4 && this.selectedItems4.length > 0) {
    //   let multistatusList: any[] = []
    //   this.selectedItems4.forEach((a: any) => multistatusList.push(a.id))
    //   this.searhform.patchValue({ statusId: this.selectedItems4 });
    // }
    // if (this.selectedItems5 && this.selectedItems5.length > 0) {
    //   let multiinvoicenoList: any[] = []
    //   this.selectedItems5.forEach((a: any) => multiinvoicenoList.push(a.id))
    //   this.searhform.patchValue({ invoiceNumber: multiinvoicenoList });
    // }
    // if (this.selectedItems9 && this.selectedItems9.length > 0) {
    //   let multipartnerList: any[] = []
    //   this.selectedItems9.forEach((a: any) => multipartnerList.push(a.id))
    //   this.searhform.patchValue({ partnerAssigned: this.selectedItems9 });
    // }
    this.spin.show();
    this.sub.add(this.matterservice.getAllSearchDropDown().subscribe((data: any) => {
      if (data) {
        data.matterList.forEach((x: any) => this.matterList.push({ value: x.key, label: x.key + '-' + x.value }));
        this.matterList.forEach((x: any) => this.multimatterListList.push({ value: x.value, label: x.label }));
        this.multiselectmatterListList = this.multimatterListList;
        this.multiselectmatterListList = this.cas.removeDuplicatesFromArrayNew(this.multiselectmatterListList);
      }
    }, (err) => {
      this.toastr.error(err, "");
    }));

    this.cas.getalldropdownlist([
      // this.cas.dropdownlist.setup.matterNumber .url,
      this.cas.dropdownlist.setup.statusId.url,
    ]).subscribe((results) => {
      // this.matterNumber list = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.matterNumber .key);

      this.statuslist = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.statusId.key).filter(s => [51, 52, 53, 54].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselectstatusList = this.multistatusList;
      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {
        this.spin.hide();
        let filterResult = res;   //.filter(x => x.deletionIndicator == 0);
        filterResult.forEach((x: { invoiceNumber: string; }) => this.multiinvoicenoList.push({ value: x.invoiceNumber, label: x.invoiceNumber }))
        this.multiselectinvoicenoList = this.multiinvoicenoList;
        this.multiselectinvoicenoList = this.cas.removeDuplicatesFromArrayNew(this.multiselectinvoicenoList);
        res.forEach((x) => {
          // x.noteTypeId = this.noteTypeIdlist.find(y => y.key == x.noteTypeId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
        })
        res.forEach((x: { partnerAssigned: string; }) => this.multipartnerList.push({ value: x.partnerAssigned, label: x.partnerAssigned }))
        this.multiselecipartnerList = this.multipartnerList;
        this.multiselecipartnerList = this.cas.removeDuplicatesFromArrayNew(this.multiselecipartnerList);
        this.ELEMENT_DATA = res;    //.filter(x => x.deletionIndicator == 0)
        this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }
  reset() {
    this.searhform.reset();
  }
  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.dataSource.data.forEach(x => {
      res.push({
        'Invoice No': x.invoiceNumber,
        'Prebill No': x.preBillNumber,
        "Client ID": x.clientId,
        "Matter No  ": x.matterNumber,
        "Invoice Date ": x.invoiceDate,
        'Partner Assigned': x.partnerAssigned,
        'Invoice Amount': x.invoiceAmount,
        'Paid Amt': x.totalPaidAmount,
        'Balance Amt': x.remainingBalance,
        'Status': x.statusIddes,
      });
    })
    this.excel.exportAsExcel(res, "Invoice");
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
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  deleteDialog() {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    if (this.selection.selected[0].statusId == 54 || this.selection.selected[0].statusId == 53) {
      this.toastr.error("Paid invoices can't be deleted.", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    const dialogRef = this.dialog.open(DeleteComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: this.selection.selected[0],
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleterecord(this.selection.selected[0]);
      }
    });
  }
  deleterecord(id: any) {
    if (id.statusId == 51 || id.statusId == 52 || id.statusId == 53 || id.statusId == 54) {   //26-09
      this.spin.show();
      this.sub.add(this.service.delete(id.invoiceNumber).subscribe((res) => {
        this.toastr.success(id.invoiceNumber + " deleted successfully!", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        this.spin.hide();
        this.getAllListData();
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));
    } else {
      this.toastr.error(id.invoiceNumber + " Invoice cannot be deleted as the payment is already done!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
    }
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
                  [{ text:  (res.reportHeader.referenceField3 != null ? res.reportHeader.referenceField3 : '') + ', ' + (res.reportHeader.referenceField4 != null ? res.reportHeader.referenceField4 : ''), fontSize: 10 },
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

            dd.content.push("\n\n",
            {
              stack: [
                {
                  text: res.reportHeader.message,
                  alignment: 'left',
                  fontSize: 11,
                  lineHeight: 1.1,
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
  finalData: any[] = [];
  generateText(element: any) {
    console.log(element)
    this.finalData = [];
    this.sub.add(this.service.getInvoicePdfData(element.invoiceNumber).subscribe((res: any) => {
      if (res != null) {
        if (res.reportHeader.billingFormatId == '12') {
          let i = 0;
          let blankCell = ``;
          let clientIdMatterId = '';
          let invoiceTotal = 0;
          let firstDay: any = null;
          let lastDay: any = null;
          if (element.invoiceDate != null) {
            firstDay = this.datePipe.transform((new Date(element.invoiceDate).getFullYear() + '-' + (new Date(element.invoiceDate).getMonth() + 1) + '-' + 1), "yyyyMMdd",);
            lastDay = this.datePipe.transform((new Date(element.invoiceDate).getFullYear() + '-' + (new Date(element.invoiceDate).getMonth() + 2) + '-' + 0), "yyyyMMdd",);
            console.log(firstDay)
            console.log(element.invoiceDate)
          }
          if (res.finalSummary != null) {
            invoiceTotal = (res.finalSummary.advancedCost != null ? res.finalSummary.advancedCost : 0) + (res.finalSummary.currentFees != null ? res.finalSummary.currentFees : 0)
          }


          if (res.reportHeader != null) {
            clientIdMatterId = (res.reportHeader.clientId != null ? res.reportHeader.clientId : ``) + (res.reportHeader.matterNumber != null ? res.reportHeader.matterNumber : ``) + `[]`;
          }
          let text = `LEDES1998B[] \nINVOICE_DATE|INVOICE_NUMBER|CLIENT_ID|LAW_FIRM_MATTER_ID|INVOICE_TOTAL|BILLING_START_DATE|BILLING_END_DATE|INVOICE_DESCRIPTION|LINE_ITEM_NUMBER|EXP/FEE/INV_ADJ_TYPE|LINE_ITEM_NUMBER_OF_UNITS|LINE_ITEM_ADJUSTMENT_AMOUNT|LINE_ITEM_TOTAL|LINE_ITEM_DATE|LINE_ITEM_TASK_CODE|LINE_ITEM_EXPENSE_CODE|LINE_ITEM_ACTIVITY_CODE|TIMEKEEPER_ID|LINE_ITEM_DESCRIPTION|LAW_FIRM_ID|LINE_ITEM_UNIT_COST|TIMEKEEPER_NAME|TIMEKEEPER_CLASSIFICATION|CLIENT_MATTER_ID[]`;
          if (res.expenseEntry != null && res.expenseEntry.length > 0) {
            res.expenseEntry.forEach(expenseEntryData => {
              i++;
              text = text + `\n`;
              text = text +

                (element.invoiceDate != null ? (this.datePipe.transform(element.invoiceDate, "yyyyMMdd", "GMT00:00")) : ``) + `|` +    //CLARA/AMS/2022/138 mugilan 05-10-2022
                (element.invoiceNumber != null ? element.invoiceNumber : ``) + `|` +
                (res.reportHeader.clientId != null ? res.reportHeader.clientId : ``) + `|` +
                (res.reportHeader.matterNumber != null ? res.reportHeader.matterNumber : ``) + `|` +
                (invoiceTotal) + `|` +
                (firstDay != null ? firstDay : ``) + `|` + //billingStart date
                (lastDay != null ? lastDay : ``) + `|` + //billingEnd date      //CLARA/AMS/2022/138 mugilan 05-10-2022

                (res.reportHeader.invoiceRemarks != null ? res.reportHeader.invoiceRemarks : ``) + `|` +
                (i) + `|` + // line item number 
                ("E") + `|` + //hard code for expense
                // (expenseEntryData.numberofItems != null ? expenseEntryData.numberofItems : 0) + `|` +
                1 + `|` +
                ("0") + `|` + //line item adjustment amount
                (expenseEntryData.expenseAmount != null ? expenseEntryData.expenseAmount : 0) + `|` +
                (expenseEntryData.createdOn != null ? (this.datePipe.transform(expenseEntryData.createdOn, "yyyyMMdd")) : ``) + `|` +    //CLARA/AMS/2022/138 mugilan 05-10-2022
                (blankCell) + `|` +
                (expenseEntryData.expenseCode != null ? expenseEntryData.expenseCode : 0) + `|` +
                (blankCell) + `|` +
                (blankCell) + `|` +
                (expenseEntryData.expenseDescription != null ? expenseEntryData.expenseDescription.replaceAll('\n', '') : ``) + `|` +
                ("20-3389884") + `|` + //law firm id
                // (blankCell) + `|` +
                (expenseEntryData.expenseAmount != null ? expenseEntryData.expenseAmount : 0) + `|` +
                (blankCell) + `|` +
                (blankCell) + `|` + //time keeper calssification
                (clientIdMatterId)
            });
          }

          if (res.timeTicketDetail != null) {
            if (res.timeTicketDetail.timeTickets != null && res.timeTicketDetail.timeTickets.length > 0) {
              res.timeTicketDetail.timeTickets.forEach(element => {
                if (element.billType == 'billable') {
                  this.finalData.push(element)
                }
              });
              this.finalData.forEach(timeTicketData => {
                let totalAmount = 0;
                totalAmount = ((timeTicketData.billableTimeInHours) * (this.getAssignedRate(res.timeKeeperSummary, timeTicketData.timeTicketName)))
                i++;
                text = text + `\n`;
                text = text +
                  (element.invoiceDate != null ? (this.datePipe.transform(element.invoiceDate, "yyyyMMdd", "GMT00:00")) : ``) + `|` +            //CLARA/AMS/2022/138 mugilan 05-10-2022
                  (element.invoiceNumber != null ? element.invoiceNumber : ``) + `|` +
                  (res.reportHeader.clientId != null ? res.reportHeader.clientId : ``) + `|` +
                  (res.reportHeader.matterNumber != null ? res.reportHeader.matterNumber : ``) + `|` +
                  (invoiceTotal) + `|` +
                  (firstDay != null ? firstDay : ``) + `|` + //billingStart date
                  (lastDay != null ? lastDay : ``) + `|` + //billingEnd date
                  (res.reportHeader.invoiceRemarks != null ? res.reportHeader.invoiceRemarks : ``) + `|` +
                  (i) + `|` + // line item number 
                  ("F") + `|` + //hard code for timeticket
                  (timeTicketData.billableTimeInHours != null ? timeTicketData.billableTimeInHours : 0) + `|` +
                  ("0") + `|` + //line item adjustment amount
                  //  (timeTicketData.billableAmount != null ? timeTicketData.billableAmount : 0) + `|` +
                  this.decimalPipe.transform(totalAmount, "1.2-2") + `|` +
                  (timeTicketData.createdOn != null ? (this.datePipe.transform(timeTicketData.createdOn, "yyyyMMdd")) : ``) + `|` +           //CLARA/AMS/2022/138 mugilan 05-10-2022
                  (timeTicketData.taskCode != null ? timeTicketData.taskCode : ``) + `|` +
                  (blankCell) + `|` + //line item expense code
                  (timeTicketData.activityCode != null ? timeTicketData.activityCode : ``) + `|` +
                  (timeTicketData.timeTicketName != null ? timeTicketData.timeTicketName : ``) + `|` +
                  (timeTicketData.ticketDescription != null ? timeTicketData.ticketDescription.replaceAll('\n', '') : ``) + `|` +
                  ("20-3389884") + `|` + //law firm id
                  (this.getAssignedRate(res.timeKeeperSummary, timeTicketData.timeTicketName)) + `|` +
                  (this.getTimeKeepName(res.timeKeeperSummary, timeTicketData.timeTicketName)) + `|` +
                  "PT" + `|` + //time keeper calssification
                  (clientIdMatterId)
              });
            }
          }

          if ((res.timeTicketDetail == null || res.timeTicketDetail.timeTickets == null || res.timeTicketDetail.timeTickets.length == 0) && (res.expenseEntry == null || res.expenseEntry.length == 0)) {
            this.toastr.error("No data available", "Notification", {
              timeOut: 2000,
              progressBar: false,
            });
            return;
          }
          this.dyanmicDownloadByHtmlTag({
            fileName: 'Invoice - ' + element.invoiceNumber,
            text: text
          });
        } else {
          this.toastr.error("This is not a leeds invoice", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
        }
      } else {
        this.toastr.error("No data available", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
      }
    }));
  }

  private dyanmicDownloadByHtmlTag(arg: {
    fileName: string,
    text: string
  }) {
    if (!this.setting.element.dynamicDownload) {
      this.setting.element.dynamicDownload = document.createElement('a');
    }
    const element = this.setting.element.dynamicDownload;
    const fileType = arg.fileName.indexOf('.txt') > -1 ? 'text/json' : 'text/plain';
    element.setAttribute('href', `data:${fileType};charset=utf-8,${encodeURIComponent(arg.text)}`);
    element.setAttribute('download', arg.fileName);
    var event = new MouseEvent("click");
    element.dispatchEvent(event);
  }
  getTimeKeepName(timeKeeperSummary: any[], timeTicketCode: any) {
    let timeTicketKeeperName = '';
    if (timeKeeperSummary != null && timeKeeperSummary.length > 0) {
      let data = timeKeeperSummary.filter(x => timeTicketCode == x.timeTicketCode)[0].timeTicketName;
      return data != null ? data : '';
    } else {
      return timeTicketKeeperName;
    }
  }
  getAssignedRate(timeKeeperSummary: any[], timeTicketCode: any) {
    let timeTicketAssignedRate = 0;
    if (timeKeeperSummary != null && timeKeeperSummary.length > 0) {
      let data = timeKeeperSummary.filter(x => timeTicketCode == x.timeTicketCode)[0].timeTicketAssignedRate;
      return data != null ? data : 0;
    } else {
      return timeTicketAssignedRate;
    }
  }


}
