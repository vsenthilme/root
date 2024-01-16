import { SelectionModel } from "@angular/cdk/collections";
import { DatePipe, DecimalPipe } from "@angular/common";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder } from "@angular/forms";
import { MatDialog } from "@angular/material/dialog";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { DeleteComponent } from "src/app/common-field/dialog_modules/delete/delete.component";
import { CommonApiService, dropdownelement } from "src/app/common-service/common-api.service";
import { CommonService } from "src/app/common-service/common-service.service";
import { ExcelService } from "src/app/common-service/excel.service";
import { defaultStyle } from "src/app/config/customStyles";
import { AuthService } from "src/app/core/core";
import { PaymentLinkComponent } from "./payment-link/payment-link.component";
import { QuotationService } from "./quotation.service";
import { logo } from "../../../../../assets/font/logo.js";


import pdfMake from "pdfmake/build/pdfmake";
// importing the fonts and icons needed
import pdfFonts from "../../../../../assets/font/vfs_fonts.js"
import { fonts } from "./../../../../config/pdfFonts";
import { ClientGeneralService } from "src/app/main-module/client/client-general/client-general.service";


interface SelectItem {
  id: string;
  itemName: string;
}
@Component({
  selector: 'app-quotations-list',
  templateUrl: './quotations-list.component.html',
  styleUrls: ['./quotations-list.component.scss']
})
export class QuotationsListComponent implements OnInit {

  screenid = 1134;

  displayedColumns: string[] = ['select', 'clientId', 'matterNumber', 'quotationNo', 'quotationRevisionNo', 'quotationAmount', 'quotationDate', 'document', 'paymentlink', 'statusId',];
  public icon = 'expand_more';
  isShowDiv = false;
  showFloatingButtons: any;
  toggle = true;
  showFiller = false;
  animal: string | undefined;
  id: string | undefined;

  ELEMENT_DATA: any[] = [];
  dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA);
  selection = new SelectionModel<any>(true, []);
  statusIdF = { statusId: [1, 10, 52] };

  searchStatusList = { statusId: [1, 10, 12, 13] };
  intakeFormNumberList: dropdownelement[] = [];
  searhform = this.fb.group({
    caseCategoryId: [],
    clientId: [],
    clientIdFE: [],
    caseCategoryIdFE: [],
    createdBy: [],
    createdByFE: [],
    endQuotationDate: [],
    firstNameLastName: [],
    matterNumber: [],
    matterNumberFE: [],
    quotationNo: [],
    quotationNoFE: [],
    startQuotationDate: [],
    statusId: [],
    statusIdFE: [],
  });

  RA: any = {};
  sub = new Subscription();

  @ViewChild(MatSort)
  sort: MatSort;
  @ViewChild(MatPaginator)
  paginator: MatPaginator;
  classIdList: any[] = [];
  statuslist: any[] = [];
  clientIdlist: any[] = [];
  clientcatlist: any[] = [];
  quotationlist: any[] = [];
  userTypeList: any[] = [];
  matterList: any[] = [];

  selectedItems4: SelectItem[] = [];
  multiselectstatusList: any[] = [];
  multistatusList: any[] = [];

  selectedItems3: any[] = [];
  multiselectclientList: any[] = [];
  multiclientList: any[] = [];

  selectedItems5: any[] = [];
  multiselectclientcatList: any[] = [];
  multiclientcatList: any[] = [];

  selectedItems6: SelectItem[] = [];
  multiselectquotationList: any[] = [];
  multiquotationList: any[] = [];

  selectedItems7: SelectItem[] = [];
  multiselectcreatedList: any[] = [];
  multicreatedList: any[] = [];

  selectedItems8: SelectItem[] = [];
  multiselectmatterListList: any[] = [];
  multimatterListList: any[] = [];

  dropdownSettings = {
    singleSelection: false,
    text: "Select",
    selectAllText: 'Select All',
    unSelectAllText: 'UnSelect All',
    enableSearchFilter: true,
    badgeShowLimit: 2,
    disabled: false
  };

  pageNumber = 0;
  pageSize = 100;
  totalRecords = 0;

  constructor(public dialog: MatDialog,
    private service: QuotationService, private router: Router,
    public toastr: ToastrService,
    private spin: NgxSpinnerService,
    private cs: CommonService,
    private cas: CommonApiService,
    private excel: ExcelService,
    private fb: FormBuilder,
    public datePipe: DatePipe,
    private decimalPipe: DecimalPipe,
    private servicecom : ClientGeneralService,
    private auth: AuthService) { }

  ngOnInit(): void {
    this.RA = this.auth.getRoleAccess(this.screenid);
    this.getAllDropdownList();
  }

  pageHandler($event: PageEvent) {
    this.pageNumber = $event.pageIndex;
    this.pageSize = $event.pageSize;
    this.getAllListData();
  }

  getAllDropdownList(excel: boolean = false) {
    this.spin.show();
    this.cas.getalldropdownlist([this.cas.dropdownlist.setup.classId.url,
    this.cas.dropdownlist.setup.statusId.url,
    this.cas.dropdownlist.client.clientId.url,
    this.cas.dropdownlist.setup.clientCategoryId.url,
    this.cas.dropdownlist.accounting.quotationNo.url,
    this.cas.dropdownlist.setup.userId.url,
    this.cas.dropdownlist.matter.matterNumber.url,
    ]).subscribe((results) => {
      this.spin.hide();
      this.classIdList = this.cas.foreachlist_searchpage(results[0], this.cas.dropdownlist.setup.classId.key);

      this.statuslist = this.cas.foreachlist_searchpage(results[1], this.cas.dropdownlist.setup.statusId.key).filter(s => [1, 10, 52].includes(s.key));
      this.statuslist.forEach((x: { key: string; value: string; }) => this.multistatusList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselectstatusList = this.multistatusList;

      this.clientIdlist = this.cas.foreachlist_searchpage(results[2], this.cas.dropdownlist.client.clientId.key);
      this.clientIdlist.forEach((x: { key: string; value: string; }) => this.multiclientList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselectclientList = this.multiclientList;

      this.clientcatlist = this.cas.foreachlist_searchpage(results[3], this.cas.dropdownlist.setup.clientCategoryId.key);
      this.clientcatlist.forEach((x: { key: string; value: string; }) => this.multiclientcatList.push({ value: x.key, label: x.key + '-' + x.value }))
      this.multiselectclientcatList = this.multiclientcatList;

      this.quotationlist = this.cas.foreachlist_searchpage(results[4], this.cas.dropdownlist.accounting.quotationNo.key);
      this.quotationlist.forEach((x: { key: string; value: string; }) => this.multiquotationList.push({ value: x.key, label: x.value }))
      this.multiselectquotationList = this.multiquotationList;

      this.userTypeList = this.cas.foreachlist_searchpage(results[5], this.cas.dropdownlist.setup.userId.key);
      this.userTypeList.forEach((x: { key: string; value: string; }) => this.multicreatedList.push({ value: x.key, label: x.value }))
      this.multiselectcreatedList = this.multicreatedList;

      this.matterList = this.cas.foreachlist_searchpage(results[6], this.cas.dropdownlist.matter.matterNumber.key);
      this.matterList.forEach((x: { key: string; value: string; }) => this.multimatterListList.push({ value: x.key, label: x.key + ' / ' + x.value }))
      this.multiselectmatterListList = this.multimatterListList;
      this.getAllListData();
    }, (err) => {
      this.toastr.error(err, "");
      this.spin.hide();
    });
  }

  getAllListData(excel: boolean = false){
    this.spin.show();
    this.sub.add(this.service.getAllQuotationByPagination(this.pageNumber,this.pageSize).subscribe((res: any) => {
      this.spin.hide();
      if (this.auth.classId != '3')
        this.ELEMENT_DATA = res.content.filter(x => x.classId === Number(this.auth.classId) && x.deletionIndicator == 0);
      else
        this.ELEMENT_DATA = res.content.filter(x => x.deletionIndicator == 0);

      let quotationNumberArray: any[] = [];
      this.ELEMENT_DATA.forEach((x) => {
        quotationNumberArray.push(x.quotationNo);
        //  x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
        x['className'] = this.classIdList.find(y => y.key == x.classId)?.value;
        x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
        // x.clientId = this.clientIdlist.find(y => y.key == x.clientId)?.value;
        x['clientName'] = this.clientIdlist.find(y => y.key == x.clientId)?.value;
      })

      this.intakeFormNumberList = [];

      const categories = this.ELEMENT_DATA.map(person => ({
        intakeFormNumber: person.intakeFormNumber,
      }));
      const distinctThings = categories.filter(
        (thing, i, arr) => arr.findIndex(t => t.intakeFormNumber === thing.intakeFormNumber) === i
      );
      distinctThings.forEach(x => {
        this.intakeFormNumberList.push({ key: x.intakeFormNumber, value: x.intakeFormNumber });
      });

      if (this.ELEMENT_DATA.length > 0) {
        let groupedLastRevisionData: any[] = [];
        let groupedData = this.cs.groupByData(this.ELEMENT_DATA, (data: any) => data.quotationNo);
        let uniqueQuotationNumberArray = this.cs.removeDuplicateInArray(quotationNumberArray);
        uniqueQuotationNumberArray.forEach(quotationNumber => {
          let groupedQuotationList = groupedData.get(quotationNumber);
          groupedLastRevisionData.push(groupedQuotationList[0])
        });
        this.ELEMENT_DATA = groupedLastRevisionData;
      }
      if (excel)
        this.excel.exportAsExcel(this.ELEMENT_DATA.sort((a, b) => (a.quotationNo > b.quotationNo) ? -1 : 1));
      this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.quotationNo > b.quotationNo) ? -1 : 1));
      this.selection = new SelectionModel<any>(true, []);
      this.dataSource.sort = this.sort;
      this.totalRecords = res.totalElements;
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  deleteDialog() {
    if (this.selection.selected.length === 0) {
      this.toastr.error("Kindly select any Row", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    if (this.selection.selected[0].statusId != 1) {
      this.toastr.error("Quotation cannot be deleted.", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      return;
    }
    if (this.selection.selected.length === 10) {
      this.toastr.error("Quotation cannot be deleted.", "Notification", {
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
  paymentlink(data: any): void {
    const dialogRef = this.dialog.open(PaymentLinkComponent, {
      disableClose: true,
      width: '50%',
      maxWidth: '80%',
      position: { top: '6.5%' },
      data: data
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.spin.show();
        this.sub.add(this.service.Update(result, data.quotationNo, data.quotationRevisionNo).subscribe((res: any) => {
          this.spin.hide();
          data.referenceField10 = result;
          this.toastr.success(data.quotationNo + " updated successfully!", "Notification", {
            timeOut: 2000,
            progressBar: false,
          });
        }, err => {
          this.cs.commonerror(err);
          this.spin.hide();
        }));
      }
    });
  }

  deleterecord(id: any) {
    this.spin.show();
    this.sub.add(this.service.Delete(id.quotationNo, id.quotationRevisionNo).subscribe((res) => {
      this.toastr.success(id.quotationNo + " deleted successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();// this.getAllListData();
    window.location.reload();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }

  openDialog(data: any = 'new'): void {
    if (data != 'New')
      if (this.selection.selected.length === 0) {
        this.toastr.error("Kindly select any Row", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
    let paramdata = "";
    if (this.selection.selected.length > 0) {
      if (this.selection.selected[0].statusId == 10 && data == 'Edit') {
        this.toastr.error("Quotation cannot be edited.", "Notification", {
          timeOut: 2000,
          progressBar: false,
        });
        return;
      }
      paramdata = this.cs.encrypt({ code: this.selection.selected[0], pageflow: data });
      this.router.navigate(['/main/accounts/quotationsnew/' + paramdata]);
    }
    else {
      paramdata = this.cs.encrypt({ pageflow: data });
      this.router.navigate(['/main/accounts/quotationsnew/' + paramdata]);
    }
  }

  openPdf(data: any): void {
    let paramdata = this.cs.encrypt({ code: data });
    this.router.navigate(['/main/accounts/quotationpdf/' + paramdata]);
  }

  downloadexcel() {
    // if (excel)
    var res: any = [];
    this.ELEMENT_DATA.forEach(x => {
      res.push({
        "Client Name": x.clientName,
        'Matter No': x.matterNumber,
        "Quote No  ": x.quotationNo,
        "Rev No ": x.quotationRevisionNo,
        'Amount': x.quotationAmount,
        "Status  ": x.statusIddes,
        'Quote Date': this.cs.dateapi(x.createdOn),
      });
    })
    this.excel.exportAsExcel(res, "Quotation");
  }

  Clear() {
    this.reset();
  };

  search() {
    this.searhform.controls.startQuotationDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.startQuotationDate.value));
    this.searhform.controls.endQuotationDate.patchValue(this.cs.day_callapiSearch(this.searhform.controls.endQuotationDate.value));
    // if (this.selectedItems4 && this.selectedItems4.length > 0) {
    //   let multistatusList: any[] = []
    //   this.selectedItems4.forEach((a: any) => multistatusList.push(a.id))
    //   this.searhform.patchValue({ statusId: this.selectedItems4 });
    // }
    // if (this.selectedItems3 && this.selectedItems3.length > 0) {
    //   let multiclientList: any[] = []
    //   this.selectedItems3.forEach((a: any) => multiclientList.push(a.id))
    //   this.searhform.patchValue({ clientId: multiclientList });
    // }
    // if (this.selectedItems5 && this.selectedItems5.length > 0) {
    //   let multiclientcatList: any[] = []
    //   this.selectedItems5.forEach((a: any) => multiclientcatList.push(a.id))
    //   this.searhform.patchValue({ caseCategoryId: multiclientcatList });
    // }
    // if (this.selectedItems6 && this.selectedItems6.length > 0) {
    //   let multiquotationList: any[] = []
    //   this.selectedItems6.forEach((a: any) => multiquotationList.push(a.id))
    //   this.searhform.patchValue({ quotationNo: multiquotationList });
    // }
    // if (this.selectedItems7 && this.selectedItems7.length > 0) {
    //   let multicreatedList: any[] = []
    //   this.selectedItems7.forEach((a: any) => multicreatedList.push(a.id))
    //   this.searhform.patchValue({ createdBy: multicreatedList });
    // }
    // if (this.selectedItems8 && this.selectedItems8.length > 0) {
    //   let multimatterListList: any[] = []
    //   this.selectedItems8.forEach((a: any) => multimatterListList.push(a.id))
    //   this.searhform.patchValue({ matterNumber: multimatterListList });
    // }
    this.spin.show();

      this.sub.add(this.service.Search(this.searhform.getRawValue()).subscribe((res: any[]) => {
        this.spin.hide();
        if (this.auth.classId != '3')
          this.ELEMENT_DATA = res.filter(x => x.classId === Number(this.auth.classId));
        else
          this.ELEMENT_DATA = res;
        this.ELEMENT_DATA.forEach((x) => {
          x.classId = this.classIdList.find(y => y.key == x.classId)?.value;
          x.statusIddes = this.statuslist.find(y => y.key == x.statusId)?.value;
          x['clientName'] = this.clientIdlist.find(y => y.key == x.clientId)?.value;
        })
        this.dataSource = new MatTableDataSource<any>(this.ELEMENT_DATA.sort((a, b) => (a.clientId > b.clientId) ? -1 : 1));
        this.selection = new SelectionModel<any>(true, []);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      }, err => {
        this.cs.commonerror(err);
        this.spin.hide();
      }));

  }
  reset() {
    this.searhform.reset();
    this.getAllListData();
  }
  sendtoClient(data: any) {
    data.statusId = 10;
    this.sub.add(this.service.Update(data, data.quotationNo, data.quotationRevisionNo).subscribe(res => {
      this.toastr.success(res.quotationNo + " updated successfully!", "Notification", {
        timeOut: 2000,
        progressBar: false,
      });
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
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
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.quotationNo + 1}`;
  }
    clearselection(row: any) {
    if (!this.selection.isSelected(row)) {
      this.selection.clear();
    }
    this.selection.toggle(row);
  }

  ngOnDestroy() {
    if (this.sub != null) {
      this.sub.unsubscribe();
    }
  }



  generatePdf(element: any) {
    this.spin.show();
    console.log(element)
    this.sub.add(this.service.Get(element.quotationNo, element.quotationRevisionNo).subscribe(res => {
      this.sub.add(this.servicecom.Get(res.clientId).subscribe(clientRes => {
      //this.spin.hide();
      console.log(res)
      //Receipt List
      if (res) {
        let quotationDate = this.datePipe.transform(res.quotationDate, 'MM-dd-yyyy');
        let dueDate = this.datePipe.transform(res.dueDate, 'MM-dd-yyyy');
        let currentDate = this.datePipe.transform(new Date, 'dd-MMM-yyyy HH:mm')
        var dd: any;
        let headerTable: any[] = [];
        
        headerTable.push([
         { image: logo.headerLogo, fit: [180, 180], bold: true, fontSize: 12, border: [false, false, false, false] },
        // { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
     //    { text: 'Monty & Ramirez LLP \n 150 W Parker Road, 3rd Floor Houston, TX 77076',  alignment: 'center', fontSize: 12, border: [false, false, false, false] },
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
        ]);
       
        dd = {
          pageSize: "A4",
          pageOrientation: "portrait",
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
                margin: [20, 20, 20, 40]
              }
            ]
          },
          styles: {
            anotherStyle: {
              bordercolor: '#6102D3'
            }
          },
          
          footer(currentPage: number, pageCount: number, pageSize: any): any {
            return [{
              text: 'Page ' + currentPage + ' of ' + pageCount,
              style: 'header',
              alignment: 'center',
              bold: true,
              fontSize: 6
            }]
          },
          content: ['\n'],
          defaultStyle
        };

        let headerArray: any[] = [];
        headerArray.push([
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
          { text: 'Invoice', bold: true, alignment: 'left', fontSize: 16, border: [false, false, false, false] },
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
         
        ]);
        headerArray.push([
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
          { text: 'Date', bold: true, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], fontSize: 12,alignment: 'center',  style: ['anotherStyle'], border: [true, true, true, true] },
          { text: 'Invoice No', bold: true, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], alignment: 'center',    fontSize: 12, border: [true, true, true, true] },
        ]);
        headerArray.push([
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
          { text:  quotationDate,  fontSize: 12, alignment: 'center',   borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, true, true, true] },
          { text: res.quotationNo, alignment: 'center',  borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'],  fontSize: 12, border: [true, true, true, true] },
        ]);
        
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: ['*', 90, 90],
              body: headerArray
            },
            margin: [0, 20, 0, 10]
          }
        )

        let bodyArray: any[] = [];
        
        bodyArray.push([
          { text: 'Name & Address', bold: true, fontSize: 12, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, true, true, true] },
        ]);
        bodyArray.push([
          { text: clientRes.firstNameLastName,  fontSize: 12, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, false, true, false] },
        ]);
        bodyArray.push([
          { text: clientRes.addressLine1,  fontSize: 12, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, false, true, false] },
        ]);
        bodyArray.push([
          { text: clientRes.city + ', ' +  clientRes.state + ' - ' + clientRes.zipCode,  fontSize: 12, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, false, true, true] },
        ]);
        
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [200, '*', '*'],
              body: bodyArray
            },
            
          }
        )


        let termsArray: any[] = [];
        termsArray.push([
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
         
        ]);
        termsArray.push([
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
          { text: 'Terms',   borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: true, alignment: 'left',fontSize: 12, border: [true, true, true, true] },
          { text: 'Due Date', borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: true, alignment: 'left',   fontSize: 12, border: [true, true, true, true] },
        ]);
        termsArray.push([
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
          { text:  res.termDetails != null ? res.termDetails : '     ', borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'],  fontSize: 12, border: [true, true, true, true] },
          { text: dueDate != null ? dueDate : '  ', borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], alignment: 'left',  fontSize: 12, border: [true, true, true, true] },
        ]);
        
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: ['*', 180, 60],
              body: termsArray
            },
            margin: [0, 0, 0, 10]
          }
        )

        let tableArray: any[] = [];
    
          let date = this.datePipe.transform(res.dueDate, 'MM-dd-yyyy');
       
        tableArray.push([
          
          { text: 'Item', bold: true, fontSize: 12, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, true, true, true] },
          { text: 'Description', bold: true, fontSize: 12, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, true, true, true] },
          { text: 'Time Keeper', bold: true, fontSize: 12, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, true, true, true] },
          { text: 'Date', bold: true, fontSize: 12, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, true, true, true] },
          { text: 'Hours', bold: true, fontSize: 12, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, true, true, true] },
          { text: 'Rate/Hr', bold: true, fontSize: 12, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, true, true, true] },
          { text: 'Total', bold: true, alignment: 'center', fontSize: 12, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], border: [true, true, true, true] },
        ]);
        res.quotationLine.forEach((data, i) => {
        tableArray.push([
          { text: data.serialNumber, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: false, fontSize: 12, border: [true, true, true, true],},
          { text: data.itemDescription,  borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'],bold: false, fontSize: 12, border: [true, true, true, true] },
          { text: data.timekeeperCode, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: false, fontSize: 12, border: [true, true, true, true] },
          { text: date, bold: false, borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], fontSize: 12, border: [true, true, true, true] },
          { text: data.quantity !=null ? this.decimalPipe.transform(data.quantity, "1.2-2") : '$0.00', borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: false, fontSize: 12, border: [true, true, true, true] },
          { text: data.rateperHour != null ? '$ ' + this.decimalPipe.transform(data.rateperHour, "1.2-2") : '$0.00', borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: false, fontSize: 12, border: [true, true, true, true] },
          { text: data.totalAmount != null ? '$ ' + this.decimalPipe.transform(data.totalAmount, "1.2-2") : '$0.00', borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: false, alignment: 'right', fontSize: 12, border: [true, true, true, true] },
        ]);
        if ((i + 1) == res.quotationLine.length) {
        tableArray.push([
          { text: '', bold: false, fontSize: 12, border: [false, false, false, false],  },
          { text: '', bold: false, fontSize: 12, border: [false, false, false, false],  },
          { text: '', bold: false, fontSize: 12, border: [false, false, false, false],  },
          { text: '', bold: false, fontSize: 12, border: [false, false, false, false],  },
          { text: '', bold: false, fontSize: 12, border: [false, false, false, false],  },
          { text: 'Total:', borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: true, fontSize: 12, border: [false, true, false, true],  },
          { text: res.quotationAmount != null ? '$ ' + this.decimalPipe.transform(res.quotationAmount, "1.2-2") : '$0.00', alignment: 'right', bold: true, fontSize: 12,  border: [false, true, true, true], borderColor: ['#ddd', '#ddd', '#ddd', '#ddd']},
        ]);
      }
      });
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [30, 130, 70, 60, 40, 60, 70],
              body: tableArray,
              
            },
         //   margin: [0, 0, 0, 10]
          },
          '\n\n',
         
        )

        let phoneNumberArray: any[] = [];
        phoneNumberArray.push([
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
         
        ]);
        phoneNumberArray.push([
          { text: 'Phone Number', borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'], bold: true, alignment: 'left',   fontSize: 12, border: [true, true, true, true] },
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
        ]);
        phoneNumberArray.push([
          { text: clientRes.contactNumber != null ? clientRes.contactNumber : '    ', alignment: 'left', borderColor: ['#ddd', '#ddd', '#ddd', '#ddd'],  fontSize: 12, border: [true, true, true, true] },
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
          { text: '', bold: true, fontSize: 12, border: [false, false, false, false] },
        ]);
        
        dd.content.push(
          {
            table: {
              headerRows: 1,
              widths: [100, '*', '*'],
              body: phoneNumberArray
            },
          
          }
        )

        pdfMake.createPdf(dd).download('Invoice No - ' + res.quotationNo);
       // pdfMake.createPdf(dd).open();
      } else {
        this.toastr.info("No data available", "Pdf Download", {
          timeOut: 2000,
          progressBar: false,
        });
      }
      this.spin.hide();
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
    }, err => {
      this.cs.commonerror(err);
      this.spin.hide();
    }));
  }
}
